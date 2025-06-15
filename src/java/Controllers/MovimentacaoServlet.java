package Controllers;

import Models.Movimentacao;
import Models.Conexao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "MovimentacaoServlet", urlPatterns = {"/Movimentacao"})
public class MovimentacaoServlet extends HttpServlet {
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

        List<Movimentacao> movimentacoes = new ArrayList<>();

        String sql = "SELECT m.DATA_MOVIMENTACAO, m.DESCRICAO, m.TIPO_MOVIMENTACAO, m.VALOR, c.TIPO_CONTA, c.NUMERO_CONTA " +
                     "FROM MOVIMENTACAO m " +
                     "INNER JOIN CONTA c ON m.CONTA_ID = c.ID " +
                     "WHERE c.USUARIO_ID = ? " +
                     "ORDER BY m.DATA_MOVIMENTACAO DESC";

        Conexao conexao = new Conexao();
        Connection conn = conexao.getConexao();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                String dataStr = sdf.format(rs.getTimestamp("DATA_MOVIMENTACAO"));
                String descricao = rs.getString("DESCRICAO");
                String tipo = rs.getString("TIPO_MOVIMENTACAO");
                double valor = rs.getDouble("VALOR");
                String conta = rs.getString("NUMERO_CONTA") + " - " + rs.getString("TIPO_CONTA");

                movimentacoes.add(new Movimentacao(dataStr, descricao, tipo, valor, conta));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar movimentações: " + e.getMessage(), e);
        } finally {
            conexao.closeConexao();
        }

        // Retorna JSON manualmente:
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < movimentacoes.size(); i++) {
            Movimentacao m = movimentacoes.get(i);
            json.append("{")
                .append("\"data\":\"").append(escapeJson(m.getData())).append("\",")
                .append("\"descricao\":\"").append(escapeJson(m.getDescricao())).append("\",")
                .append("\"tipo\":\"").append(escapeJson(m.getTipo())).append("\",")
                .append("\"valor\":").append(m.getValor()).append(",")
                .append("\"conta\":\"").append(escapeJson(m.getConta())).append("\"")
                .append("}");
            if (i < movimentacoes.size() - 1) json.append(",");
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
