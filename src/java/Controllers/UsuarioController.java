package Controllers;

import Models.Conexao;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
@UsuarioController("/usuarioController")
public class UsuarioController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Conexao conexaoBD = new Conexao();
        Connection conn = conexaoBD.getConexao();

        try {
            String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.executeUpdate();

            response.sendRedirect("sucesso.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Erro ao salvar usuário: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
    }
}
