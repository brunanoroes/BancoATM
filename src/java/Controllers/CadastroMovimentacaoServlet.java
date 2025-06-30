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

        try {
            String tipoTransacao = request.getParameter("tipoTransacao");
            int contaOrigemId = Integer.parseInt(request.getParameter("contaOrigem"));
            String contaDestinoStr = request.getParameter("contaDestino");
            String contaDestinoNumero = (contaDestinoStr != null && !contaDestinoStr.isEmpty())
            ? contaDestinoStr
            : null;
            double valor = Double.parseDouble(request.getParameter("valor"));
            String descricao = request.getParameter("descricao");

            if (valor <= 0 || tipoTransacao == null || tipoTransacao.isEmpty()) {
                session.setAttribute("msgErro", "Valor ou tipo de transação inválido!");
                response.sendRedirect("Movimentacao?limite=4");
                return;
            }

            try (Connection conn = new Conexao().getConexao()) {

                // ✅ Valida se conta de origem pertence ao usuário
                String sqlCheckOrigem = "SELECT COUNT(*) FROM CONTA WHERE ID = ? AND USUARIO_ID = ?";
                try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckOrigem)) {
                    psCheck.setInt(1, contaOrigemId);
                    psCheck.setInt(2, usuarioId);
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count == 0) {
                            session.setAttribute("msgErro", "Conta de origem não encontrada ou não pertence a você.");
                            response.sendRedirect("Movimentacao?limite=4");
                            return;
                        }
                    }
                }

                if (tipoTransacao.equalsIgnoreCase("Deposito")) {
                    String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, 'ENTRADA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, contaOrigemId);
                        ps.setDouble(2, valor);
                        ps.setString(3, descricao);
                        ps.executeUpdate();
                    }

                } else if (tipoTransacao.equalsIgnoreCase("Saque")) {
                    String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, 'SAIDA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, contaOrigemId);
                        ps.setDouble(2, valor);
                        ps.setString(3, descricao);
                        ps.executeUpdate();
                    }

                } else if (tipoTransacao.equalsIgnoreCase("Transferencia")) {
                    if (contaDestinoNumero == null) {
                        session.setAttribute("msgErro", "Conta de destino não informada para transferência.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    // 👉 Verifica se a conta de destino existe (número)
                    String sqlCheckDestino = "SELECT ID FROM CONTA WHERE NUMERO_CONTA = ?";
                    Integer contaDestinoId = null;
                    try (PreparedStatement psCheckDest = conn.prepareStatement(sqlCheckDestino)) {
                        psCheckDest.setString(1, contaDestinoNumero);  // usa setString!
                        ResultSet rsDest = psCheckDest.executeQuery();
                        if (rsDest.next()) {
                            contaDestinoId = rsDest.getInt("ID");
                        } else {
                            session.setAttribute("msgErro", "Conta de destino não encontrada.");
                            response.sendRedirect("Movimentacao?limite=4");
                            return;
                        }
                    }


                    // 👉 Impede transferir para a mesma conta
                    if (contaDestinoId.equals(contaOrigemId)) {
                        session.setAttribute("msgErro", "Conta de destino não pode ser a mesma da origem.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    // SAÍDA na origem
                    String sqlSaida = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'SAIDA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlSaida)) {
                        ps.setInt(1, contaOrigemId);
                        ps.setInt(2, contaDestinoId);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }

                    // ENTRADA na destino
                    String sqlEntrada = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'ENTRADA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlEntrada)) {
                        ps.setInt(1, contaDestinoId);
                        ps.setInt(2, contaOrigemId);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }
                }

                session.setAttribute("msgSucesso", "Transação registrada com sucesso!");
                response.sendRedirect("Movimentacao?limite=4");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgErro", "Erro ao registrar movimentação: " + e.getMessage());
            response.sendRedirect("Movimentacao?limite=4");
        }
    }
}
