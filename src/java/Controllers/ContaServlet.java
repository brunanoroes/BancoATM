package Controllers;

import Models.Conta;
import Models.Conexao;
import Models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ContaServlet", urlPatterns = {"/Conta"})
public class ContaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Logout");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int usuarioId = usuario.getId();

        List<Conta> contas = new ArrayList<>();

        String sql = "SELECT NUMERO_CONTA, TIPO_CONTA, SALDO, DATA_ABERTURA FROM CONTA WHERE USUARIO_ID = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                String numero = rs.getString("NUMERO_CONTA");
                String tipo = rs.getString("TIPO_CONTA");
                double saldo = rs.getDouble("SALDO");
                String dataAbertura = sdf.format(rs.getTimestamp("DATA_ABERTURA"));

                contas.add(new Conta(numero, dataAbertura, tipo, saldo));
            }

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar contas: " + e.getMessage(), e);
        }

        request.setAttribute("contas", contas);
        request.getRequestDispatcher("Home.jsp").forward(request, response);
    }
}
