/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Models.Conexao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.sql.*;

/**
 *
 * @author izuca
 */
@WebServlet(name = "CadastroServlet", urlPatterns = {"/CadastroServlet"})
public class CadastroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipoUsuario = "NORMAL";

        // Validação simples dos campos obrigatórios
        if (nome == null || email == null || senha == null ||
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            out.println("<p style='color:red;'>Campos obrigatórios estão vazios!</p>");
            return;
        }

        try {
            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                conn.setAutoCommit(true);

                String senhaHash = hashSenha(senha);
                
                String sqlInsert = "INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, TIPO_USUARIO) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senhaHash);
                    stmt.setString(4, tipoUsuario);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        response.sendRedirect("Login.jsp?msg=sucesso");
                    } else {
                        out.println("<p style='color:red;'>Erro ao cadastrar usuário!</p>");
                    }
                }
            } finally {
                conexaoBD.closeConexao();
            }

        } catch (NumberFormatException e) {
            out.println("<p style='color:red;'>ID inválido!</p>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Erro: " + e.getMessage() + "</p>");
        }
    }
    
    private String hashSenha(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senha.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
