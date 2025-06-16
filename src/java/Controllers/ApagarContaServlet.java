package Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import Models.Conexao;

@WebServlet(name = "ApagarContaServlet", urlPatterns = {"/ApagarConta"})
public class ApagarContaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String contaNumero = request.getParameter("conta");

        if (contaNumero == null || contaNumero.trim().isEmpty()) {
            out.print("{\"status\":\"erro\", \"mensagem\":\"Número da conta não fornecido.\"}");
            return;
        }

        Conexao conexaoBD = new Conexao();
        try (Connection conn = conexaoBD.getConexao()) {
            conn.setAutoCommit(true);

            // Verifica se a conta existe e se o saldo é 0
            String verificaSql = "SELECT SALDO FROM CONTA WHERE NUMERO_CONTA = ?";
            try (PreparedStatement verificaStmt = conn.prepareStatement(verificaSql)) {
                verificaStmt.setString(1, contaNumero);
                ResultSet rs = verificaStmt.executeQuery();

                if (!rs.next()) {
                    out.print("{\"status\":\"erro\", \"mensagem\":\"Conta não encontrada.\"}");
                    return;
                }

                double saldo = rs.getDouble("SALDO");
                if (saldo > 0) {
                    out.print("{\"status\":\"erro\", \"mensagem\":\"Não é possível apagar uma conta com saldo maior que zero.\"}");
                    return;
                }
            }

            // Se passou na verificação, pode apagar
            String deleteSql = "DELETE FROM CONTA WHERE NUMERO_CONTA = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, contaNumero);
                int linhasAfetadas = deleteStmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    out.print("{\"status\":\"sucesso\", \"mensagem\":\"Conta apagada com sucesso!\"}");
                } else {
                    out.print("{\"status\":\"erro\", \"mensagem\":\"Erro ao apagar. Conta não encontrada.\"}");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"erro\", \"mensagem\":\"Erro ao apagar conta: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        } finally {
            conexaoBD.closeConexao();
        }
    }
}
