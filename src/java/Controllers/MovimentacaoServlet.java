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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        Models.Usuario usuario = (Models.Usuario) session.getAttribute("usuario");
        int usuarioId = usuario.getId();

        String limiteStr = request.getParameter("limite");
        Integer limite = null;
        if (limiteStr != null) {
            try {
                limite = Integer.parseInt(limiteStr);
                if (limite <= 0) {
                    limite = null;
                }
            } catch (NumberFormatException e) {
                limite = null;
            }
        }

        List<Movimentacao> movimentacoes = new ArrayList<>();

        String sql = "SELECT m.DATA_MOVIMENTACAO, m.DESCRICAO, m.TIPO_MOVIMENTACAO, m.VALOR, c.TIPO_CONTA, c.NUMERO_CONTA " +
                    "FROM MOVIMENTACAO m " +
                    "INNER JOIN CONTA c ON m.CONTA_ID = c.ID " +
                    "WHERE c.USUARIO_ID = ? " +
                    "ORDER BY m.DATA_MOVIMENTACAO DESC ";

        if (limite != null) {
            sql += "LIMIT ?";
        }

        try (Connection conn = new Conexao().getConexao();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            if (limite != null) {
                ps.setInt(2, limite);
            }

            ResultSet rs = ps.executeQuery();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                String data = sdf.format(rs.getTimestamp("DATA_MOVIMENTACAO"));
                String descricao = rs.getString("DESCRICAO");
                String tipo = rs.getString("TIPO_MOVIMENTACAO");
                double valor = rs.getDouble("VALOR");
                String conta = rs.getString("NUMERO_CONTA") + " - " + rs.getString("TIPO_CONTA");

                movimentacoes.add(new Movimentacao(data, descricao, tipo, valor, conta));
            }

        } catch (Exception e) {
            throw new ServletException("Erro ao buscar movimentações: " + e.getMessage(), e);
        }

        // Passa a lista para o JSP
        request.setAttribute("movimentacoes", movimentacoes);
        if (limite == null) {
            request.getRequestDispatcher("Extrato.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("Transacao.jsp").forward(request, response);
        }
    }
}