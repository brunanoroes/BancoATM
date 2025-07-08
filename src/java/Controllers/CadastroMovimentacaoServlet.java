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

        try {
            String tipoTransacao = request.getParameter("tipoTransacao");
            int contaOrigemId = Integer.parseInt(request.getParameter("contaOrigem"));
            String contaDestinoStr = request.getParameter("contaDestino");
            String contaDestinoNumero = (contaDestinoStr != null && !contaDestinoStr.isEmpty())
            ? contaDestinoStr : null;
            double valor = Double.parseDouble(request.getParameter("valor"));
            String descricao = request.getParameter("descricao");

            if (valor <= 0 || tipoTransacao == null || tipoTransacao.isEmpty()) {
                session.setAttribute("msgErro", "Valor ou tipo de transação inválido!");
                response.sendRedirect("Movimentacao?limite=4");
                return;
            }

            try (Connection conn = new Conexao().getConexao()) {
                conn.setAutoCommit(false);
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
                
                String subtractValueSQL = "UPDATE CONTA SET SALDO = SALDO - ? WHERE ID = ?";
                String addValueSQL = "UPDATE CONTA SET SALDO = SALDO + ? WHERE ID = ?";
                if (tipoTransacao.equalsIgnoreCase("Deposito")) {
                    // Adiciona valor na conta:
                    try (PreparedStatement ps = conn.prepareStatement(addValueSQL)) {
                        ps.setDouble(1, valor); // valor transferido
                        ps.setInt(2, contaOrigemId);   
                        ps.executeUpdate();
                        conn.commit();
                    }
                    insertMovimentacao(conn, contaOrigemId, "ENTRADA", valor, descricao, session);

                } else if (tipoTransacao.equalsIgnoreCase("Saque")) {
                    // Subtrai valor na conta:
                    try (PreparedStatement ps = conn.prepareStatement(subtractValueSQL)) {
                        ps.setDouble(1, valor); // valor sacado
                        ps.setInt(2, contaOrigemId);   
                        ps.executeUpdate();
                        conn.commit();
                    }
                    insertMovimentacao(conn, contaOrigemId, "SAIDA", valor, descricao, session);

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
                            
                    // Atualiza o saldo das contas origem e destino na tabela "CONTA" do BD: 
                    // 1. Subtrai valor da conta de origem:
                    try (PreparedStatement ps = conn.prepareStatement(subtractValueSQL)) {
                        ps.setDouble(1, valor); 
                        ps.setInt(2, contaOrigemId);   
                        ps.executeUpdate();
                    }
    
                    // 2. Adiciona valor na conta de destino:
                    try (PreparedStatement ps = conn.prepareStatement(addValueSQL)) {
                        ps.setDouble(1, valor); 
                        ps.setInt(2, contaDestinoId);   
                        ps.executeUpdate();
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
                conn.commit();
                session.setAttribute("msgSucesso", "Transação registrada com sucesso!");
                response.sendRedirect("Movimentacao?limite=4");

            }

        }

        
        catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgErro", "Erro ao registrar movimentação: " + e.getMessage());
            response.sendRedirect("Movimentacao?limite=4");
        }
    }
   
                    
        protected void insertMovimentacao(Connection conn, int contaId, String tipoMovimentacao, double valor, String descricao, HttpSession session) throws SQLException {
            String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, contaId);
                ps.setString(2, tipoMovimentacao);
                ps.setDouble(3, valor);
                ps.setString(4, descricao);
                ps.executeUpdate();
                conn.commit();
            }
            catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("msgErro", "Erro ao registrar movimentação: " + e.getMessage());
                conn.rollback();
            }
        }
    
}
