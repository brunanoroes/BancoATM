package Controllers;

import Models.Conexao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "CadastroContaServlet", urlPatterns = {"/CadastroConta"})
public class CadastroContaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuarioId = request.getParameter("usuarioId");
        String numero = request.getParameter("numero");
        String tipo = request.getParameter("tipo");
        String saldoStr = request.getParameter("saldo");

        if (usuarioId == null || numero == null || tipo == null || saldoStr == null ||
                usuarioId.trim().isEmpty() || numero.trim().isEmpty() || tipo.trim().isEmpty()) {
            response.sendRedirect("Conta");
            return;
        }

        try {
            double saldo = Double.parseDouble(saldoStr);

            try (Connection conn = new Conexao().getConexao()) {
                String sql = "INSERT INTO CONTA (USUARIO_ID, NUMERO_CONTA, TIPO_CONTA, SALDO) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(usuarioId));
                stmt.setString(2, numero);
                stmt.setString(3, tipo);
                stmt.setDouble(4, saldo);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("Conta");
    }
}
