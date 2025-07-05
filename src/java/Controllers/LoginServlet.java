/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Models.Conexao;
import Models.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import jakarta.servlet.http.*;
import java.sql.*;

/**
 *
 * @author izuca
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private String hashSenha(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senha.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            out.println("<p style='color:red;'>Email e senha são obrigatórios!</p>");
            return;
        }

        try {
            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                String senhaHash = hashSenha(senha);

                String sql = "SELECT * FROM USUARIO WHERE EMAIL = ? AND SENHA_HASH = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, senhaHash);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            HttpSession session = request.getSession();
                            
                            Usuario usuario = new Usuario();
                            usuario.setId(rs.getInt("ID"));
                            usuario.setNome(rs.getString("NOME"));
                            usuario.setEmail(rs.getString("EMAIL"));
                            usuario.setTipo(rs.getString("TIPO_USUARIO")); 
                            usuario.setDataCriacao(rs.getTimestamp("DATA_CRIACAO"));
                            
                            session.setAttribute("usuario", usuario);
      
                            response.sendRedirect("Conta");
                        } else {
                            out.println("<p style='color:red;'>Credenciais inválidas!</p>");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Erro ao verificar credenciais: " + e.getMessage() + "</p>");
        }
    }
}
