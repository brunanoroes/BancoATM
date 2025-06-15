package Controllers;

import Models.Conta;
import Models.Conexao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet(name = "ContaServlet", urlPatterns = {"/Conta"})
public class ContaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuarioIdStr = request.getParameter("usuarioId");
        if (usuarioIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Parâmetro usuarioId obrigatório.");
            return;
        }

        int usuarioId;
        try {
            usuarioId = Integer.parseInt(usuarioIdStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Parâmetro usuarioId inválido.");
            return;
        }

        List<Conta> contas = new ArrayList<>();

        String sql = "SELECT c.TIPO_CONTA, c.SALDO " +
                     "FROM CONTA c " +
                     "WHERE c.USUARIO_ID = ? " ;

        Conexao conexao = new Conexao();
        Connection conn = conexao.getConexao();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("TIPO_CONTA");
                Double saldo = rs.getDouble("SALDO");

                contas.add(new Conta(tipo, saldo));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar contas: " + e.getMessage(), e);
        } finally {
            conexao.closeConexao();
        }

        // Retorna JSON manualmente:
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            json.append("{")
            .append("\"tipo\":\"").append(escapeJson(c.getTipo())).append("\",")
            .append("\"saldo\":").append(c.getSaldo())
            .append("}");

            if (i < contas.size() - 1) json.append(",");
        }
        json.append("]");

        response.getWriter().write(json.toString());
    }

    // Função simples para escapar aspas e barras em JSON
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
}
