package Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import Models.Conexao;

@WebServlet(name = "CadastroContaServlet", urlPatterns = {"/CadastroConta"})
public class CadastroContaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usuarioId = request.getParameter("usuarioId");
        String numero = request.getParameter("numero");
        String tipo = request.getParameter("tipo");
        String saldoStr = request.getParameter("saldo");

        System.out.println("Recebido numero: " + numero);
        System.out.println("Recebido tipo: " + tipo);
        System.out.println("Recebido saldo: " + saldoStr);

        if (usuarioId == null || numero == null || tipo == null || saldoStr == null ||
            usuarioId.trim().isEmpty() || numero.trim().isEmpty() || tipo.trim().isEmpty() || saldoStr.trim().isEmpty()) {
            out.println("<p style='color:red;'>Campos obrigatórios estão vazios!</p>");
            return;
        }

        try {
            Double saldo = Double.parseDouble(saldoStr);

            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                System.out.println("Conexão com o banco estabelecida.");
                conn.setAutoCommit(true);

                String sql = "INSERT INTO CONTA (USUARIO_ID, NUMERO_CONTA, TIPO_CONTA, SALDO) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuarioId);
                    stmt.setString(2, numero);
                    stmt.setString(3, tipo);
                    stmt.setDouble(4, saldo);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        out.println("<p style='color:green;'>Conta cadastrada com sucesso!</p>");
                    } else {
                        out.println("<p style='color:red;'>Nenhuma linha inserida no banco!</p>");
                    }
                }
            } finally {
                conexaoBD.closeConexao();
            }

        } catch (NumberFormatException e) {
            out.println("<p style='color:red;'>Saldo inválido! Digite um número válido.</p>");
        } catch (Exception e) {
            System.out.println("Erro ao inserir no banco:");
            e.printStackTrace();
            out.println("<p style='color:red;'>Erro ao cadastrar conta: " + e.getMessage() + "</p>");
        }
    }
}
