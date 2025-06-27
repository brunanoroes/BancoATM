package Controllers;

import Models.Conta;
import Models.Conexao;
import Models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

@WebServlet(name = "ContaServlet", urlPatterns = {"/Conta"})
public class ContaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        Usuario usuario = request.getSession().getAttribute("usuario");

        int usuarioId = usuario.id;

        List<Conta> contas = new ArrayList<>();

        String sql = "SELECT c.TIPO_CONTA, c.SALDO, c.NUMERO_CONTA, c.DATA_ABERTURA " +
                     "FROM CONTA c " +
                     "WHERE c.USUARIO_ID = ?";

        Conexao conexao = new Conexao();
        Connection conn = conexao.getConexao();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("TIPO_CONTA");
                Double saldo = rs.getDouble("SALDO");
                String numeroConta = rs.getString("NUMERO_CONTA");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataAbertura = sdf.format(rs.getTimestamp("DATA_ABERTURA"));


                contas.add(new Conta(numeroConta, dataAbertura, tipo, saldo));
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
                .append("\"saldo\":").append(c.getSaldo()).append(",")
                .append("\"conta\":\"").append(escapeJson(c.getConta())).append("\",")
                .append("\"data\":\"").append(escapeJson(c.getData())).append("\"")
                .append("}");

            if (i < contas.size() - 1) json.append(",");
        }
        json.append("]");

        response.getWriter().write(json.toString());
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
}

