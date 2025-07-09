package Controllers;

import Models.Conexao;
import Models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "CadastroMovimentacaoServlet", urlPatterns = {"/CadastroMovimentacao"})
public class CadastroMovimentacaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Logout");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int usuarioId = usuario.getId();

        String tipoTransacao = request.getParameter("tipoTransacao");
        String contaDestinoStr = request.getParameter("contaDestino");
        int contaOrigemId = Integer.parseInt(request.getParameter("contaOrigem"));
        double valor = Double.parseDouble(request.getParameter("valor"));
        String descricao = request.getParameter("descricao");

        if (valor <= 0 || tipoTransacao == null || tipoTransacao.isEmpty()) {
            session.setAttribute("msgErro", "Valor ou tipo de transação inválido!");
            response.sendRedirect("Movimentacao?limite=4");
            return;
        }

        try (Connection conn = new Conexao().getConexao()) {
            conn.setAutoCommit(false);

            // ✅ 1) Valida se conta de origem pertence ao usuário logado
            if (!contaOrigemPertenceAoUsuario(conn, contaOrigemId, usuarioId)) {
                session.setAttribute("msgErro", "Conta de origem não encontrada ou não pertence a você.");
                response.sendRedirect("Movimentacao?limite=4");
                return;
            }

            // Queries base
            String subtractSQL = "UPDATE CONTA SET SALDO = SALDO - ? WHERE ID = ?";
            String addSQL = "UPDATE CONTA SET SALDO = SALDO + ? WHERE ID = ?";

            switch (tipoTransacao.toUpperCase()) {
                case "DEPOSITO":
                    try (PreparedStatement ps = conn.prepareStatement(addSQL)) {
                        ps.setDouble(1, valor);
                        ps.setInt(2, contaOrigemId);
                        ps.executeUpdate();
                    }
                    insertMovimentacao(conn, contaOrigemId, "ENTRADA", valor, descricao);
                    break;

                case "SAQUE":
                    try (PreparedStatement ps = conn.prepareStatement(subtractSQL)) {
                        ps.setDouble(1, valor);
                        ps.setInt(2, contaOrigemId);
                        ps.executeUpdate();
                    }
                    insertMovimentacao(conn, contaOrigemId, "SAIDA", valor, descricao);
                    break;

                case "TRANSFERENCIA":
                    if (contaDestinoStr == null || contaDestinoStr.isEmpty()) {
                        session.setAttribute("msgErro", "Conta de destino não informada para transferência.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    Integer contaDestinoId = buscarContaDestinoId(conn, contaDestinoStr);
                    if (contaDestinoId == null) {
                        session.setAttribute("msgErro", "Conta de destino não encontrada.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    if (contaDestinoId.equals(contaOrigemId)) {
                        session.setAttribute("msgErro", "Conta de destino não pode ser a mesma da origem.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    // Subtrai da origem
                    try (PreparedStatement ps = conn.prepareStatement(subtractSQL)) {
                        ps.setDouble(1, valor);
                        ps.setInt(2, contaOrigemId);
                        ps.executeUpdate();
                    }

                    // Adiciona na destino
                    try (PreparedStatement ps = conn.prepareStatement(addSQL)) {
                        ps.setDouble(1, valor);
                        ps.setInt(2, contaDestinoId);
                        ps.executeUpdate();
                    }

                    // Registra movimentações
                    insertMovimentacaoTransferencia(conn, contaOrigemId, contaDestinoId, valor, descricao);

                    break;

                default:
                    session.setAttribute("msgErro", "Tipo de transação inválido!");
                    response.sendRedirect("Movimentacao?limite=4");
                    return;
            }

            conn.commit();
            session.setAttribute("msgSucesso", "Transação registrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (request.getAttribute("conn") != null) {
                    ((Connection) request.getAttribute("conn")).rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            session.setAttribute("msgErro", "Erro ao registrar movimentação: " + e.getMessage());
        }

        response.sendRedirect("Movimentacao?limite=4");
    }

    private boolean contaOrigemPertenceAoUsuario(Connection conn, int contaOrigemId, int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CONTA WHERE ID = ? AND USUARIO_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contaOrigemId);
            ps.setInt(2, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private Integer buscarContaDestinoId(Connection conn, String numeroConta) throws SQLException {
        String sql = "SELECT ID FROM CONTA WHERE NUMERO_CONTA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroConta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        }
        return null;
    }

    private void insertMovimentacao(Connection conn, int contaId, String tipo, double valor, String descricao) throws SQLException {
        String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contaId);
            ps.setString(2, tipo);
            ps.setDouble(3, valor);
            ps.setString(4, descricao);
            ps.executeUpdate();
        }
    }

    private void insertMovimentacaoTransferencia(Connection conn, int contaOrigemId, int contaDestinoId, double valor, String descricao) throws SQLException {
        // Saída na origem
        String sqlSaida = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'SAIDA', ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlSaida)) {
            ps.setInt(1, contaOrigemId);
            ps.setInt(2, contaDestinoId);
            ps.setDouble(3, valor);
            ps.setString(4, descricao);
            ps.executeUpdate();
        }

        // Entrada na destino
        String sqlEntrada = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'ENTRADA', ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlEntrada)) {
            ps.setInt(1, contaDestinoId);
            ps.setInt(2, contaOrigemId);
            ps.setDouble(3, valor);
            ps.setString(4, descricao);
            ps.executeUpdate();
        }
    }
}
