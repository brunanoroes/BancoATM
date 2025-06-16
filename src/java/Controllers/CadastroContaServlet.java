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

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usuarioId = request.getParameter("usuarioId");
        String numero = request.getParameter("numero");
        String tipo = request.getParameter("tipo");
        String saldoStr = request.getParameter("saldo");

        if (usuarioId == null || numero == null || tipo == null || saldoStr == null ||
            usuarioId.trim().isEmpty() || numero.trim().isEmpty() || tipo.trim().isEmpty()) {
            out.print("{\"status\":\"erro\", \"mensagem\":\"Campos obrigatórios estão vazios.\"}");
            return;
        }

        try {
            Double saldo = Double.parseDouble(saldoStr);

            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                conn.setAutoCommit(true);

                String sql = "INSERT INTO CONTA (USUARIO_ID, NUMERO_CONTA, TIPO_CONTA, SALDO) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuarioId);
                    stmt.setString(2, numero);
                    stmt.setString(3, tipo);
                    stmt.setDouble(4, saldo);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        out.print("{\"status\":\"sucesso\", \"mensagem\":\"Conta cadastrada com sucesso!\"}");
                    } else {
                        out.print("{\"status\":\"erro\", \"mensagem\":\"Nenhuma linha inserida.\"}");
                    }
                }
            } finally {
                conexaoBD.closeConexao();
            }

        } catch (NumberFormatException e) {
            out.print("{\"status\":\"erro\", \"mensagem\":\"Saldo inválido! Digite um número válido.\"}");
        } catch (SQLIntegrityConstraintViolationException e) {
            out.print("{\"status\":\"erro\", \"mensagem\":\"Número de conta já existe. Escolha outro.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"erro\", \"mensagem\":\"Erro ao cadastrar conta: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
