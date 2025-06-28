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
            response.sendRedirect("Logout");
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
        List<String[]> contasUsuario = new ArrayList<>();

        String sqlMovimentacoes = "SELECT " +
                "m.DATA_MOVIMENTACAO, m.DESCRICAO, m.TIPO_MOVIMENTACAO, m.VALOR, " +
                "c.NUMERO_CONTA AS CONTA_ORIGEM_NUMERO, c.TIPO_CONTA AS CONTA_ORIGEM_TIPO, " +
                "cr.NUMERO_CONTA AS CONTA_DESTINO_NUMERO, cr.TIPO_CONTA AS CONTA_DESTINO_TIPO " +
                "FROM MOVIMENTACAO m " +
                "INNER JOIN CONTA c ON m.CONTA_ID = c.ID " +
                "LEFT JOIN CONTA cr ON m.CONTA_RELACIONADA_ID = cr.ID " +
                "WHERE c.USUARIO_ID = ? " +
                "ORDER BY m.DATA_MOVIMENTACAO DESC ";

        if (limite != null) {
            sqlMovimentacoes += "LIMIT ?";
        }

        try (Connection conn = new Conexao().getConexao()) {

            // 👉 BUSCA MOVIMENTAÇÕES
            try (PreparedStatement ps = conn.prepareStatement(sqlMovimentacoes)) {
                ps.setInt(1, usuarioId);
                if (limite != null) {
                    ps.setInt(2, limite);
                }

                ResultSet rs = ps.executeQuery();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                while (rs.next()) {
                    String data = sdf.format(rs.getTimestamp("DATA_MOVIMENTACAO"));
                    String descricao = rs.getString("DESCRICAO");
                    String tipo = rs.getString("TIPO_MOVIMENTACAO");
                    double valor = rs.getDouble("VALOR");

                    String contaOrigem = rs.getString("CONTA_ORIGEM_NUMERO") + " - " + rs.getString("CONTA_ORIGEM_TIPO");
                    String contaDestino = rs.getString("CONTA_DESTINO_NUMERO") != null
                            ? rs.getString("CONTA_DESTINO_NUMERO") + " - " + rs.getString("CONTA_DESTINO_TIPO")
                            : "-";

                    movimentacoes.add(new Movimentacao(data, descricao, tipo, valor, contaOrigem, contaDestino));
                }
            }

            // 👉 BUSCA CONTAS DO USUÁRIO
            String sqlContas = "SELECT ID, NUMERO_CONTA, TIPO_CONTA FROM CONTA WHERE USUARIO_ID = ?";
            try (PreparedStatement psContas = conn.prepareStatement(sqlContas)) {
                psContas.setInt(1, usuarioId);
                ResultSet rsContas = psContas.executeQuery();

                while (rsContas.next()) {
                    String id = String.valueOf(rsContas.getInt("ID"));
                    String numero = rsContas.getString("NUMERO_CONTA");
                    String tipo = rsContas.getString("TIPO_CONTA");
                    contasUsuario.add(new String[]{id, numero, tipo});
                }
            }

        } catch (Exception e) {
            throw new ServletException("Erro ao buscar movimentações ou contas: " + e.getMessage(), e);
        }

        // 👉 SETA NO REQUEST
        request.setAttribute("movimentacoes", movimentacoes);
        request.setAttribute("contasUsuario", contasUsuario);

        if (limite == null) {
            request.getRequestDispatcher("Extrato.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("Transacao.jsp").forward(request, response);
        }
    }
}
