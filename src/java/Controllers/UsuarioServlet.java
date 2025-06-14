package Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.*;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/banco?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Senha@1";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Receber dados do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipoUsuario = "NORMAL"; // fixo, já que no formulário não tem esse campo

        // Validar se os campos obrigatórios não estão vazios (pode melhorar depois)
        if (nome == null || email == null || senha == null ||
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return;
        }

        try {
            // Carregar driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Abrir conexão
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                // Criar hash da senha
                String senhaHash = hashSenha(senha);

                // Preparar SQL INSERT
                String sql = "INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, TIPO_USUARIO) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senhaHash);
                    stmt.setString(4, tipoUsuario);

                    stmt.executeUpdate();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
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
