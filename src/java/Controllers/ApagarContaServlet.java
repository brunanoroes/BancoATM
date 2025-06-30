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

        HttpSession session = request.getSession();

        String contaNumero = request.getParameter("conta");

        if (contaNumero == null || contaNumero.trim().isEmpty()) {
            session.setAttribute("msgErro", "Número da conta não fornecido.");
            response.sendRedirect("Conta");
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
                    session.setAttribute("msgErro", "Conta não encontrada.");
                    response.sendRedirect("Conta");
                    return;
                }

                double saldo = rs.getDouble("SALDO");
                if (saldo > 0) {
                    session.setAttribute("msgErro", "Não é possível apagar uma conta com saldo maior que zero.");
                    response.sendRedirect("Conta");
                    return;
                }
            }

            // Se passou na verificação, pode apagar
            String deleteSql = "DELETE FROM CONTA WHERE NUMERO_CONTA = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, contaNumero);
                int linhasAfetadas = deleteStmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    session.setAttribute("msgSucesso", "Conta apagada com sucesso!");
                } else {
                    session.setAttribute("msgErro", "Erro ao apagar. Conta não encontrada.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgErro", "Erro ao apagar conta: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }

        response.sendRedirect("Conta");
    }
}

