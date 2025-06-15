package Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.*;

import Models.Conexao;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String idStr = request.getParameter("id"); // Novo parâmetro

        System.out.println("Recebido nome: " + nome);
        System.out.println("Recebido email: " + email);
        System.out.println("Recebido senha: " + senha);
        System.out.println("Recebido id: " + idStr);

        if (nome == null || email == null || senha == null || idStr == null ||
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || idStr.trim().isEmpty()) {
            out.println("<p style='color:red;'>Todos os campos são obrigatórios!</p>");
            return;
        }

        try {
            int id = Integer.parseInt(idStr); // Converte o ID para int

            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                System.out.println("Conexão com o banco estabelecida.");
                conn.setAutoCommit(true);

                String senhaHash = hashSenha(senha);
                System.out.println("Senha hash gerada: " + senhaHash);

                String sql = "UPDATE USUARIO SET NOME = ?, EMAIL = ?, SENHA_HASH = ? WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senhaHash);
                    stmt.setInt(4, id);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        System.out.println("Atualização realizada. Linhas afetadas: " + linhasAfetadas);
                        out.println("<p style='color:green;'>Usuário atualizado com sucesso!</p>");
                    } else {
                        out.println("<p style='color:red;'>Usuário não encontrado com o ID informado!</p>");
                    }
                }
            } finally {
                conexaoBD.closeConexao();
            }

        } catch (NumberFormatException e) {
            out.println("<p style='color:red;'>ID inválido!</p>");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar no banco:");
            e.printStackTrace();
            out.println("<p style='color:red;'>Erro ao atualizar usuário: " + e.getMessage() + "</p>");
        }
    }
    
    protected void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String idStr = request.getParameter("id"); // Novo parâmetro

        System.out.println("Recebido nome: " + nome);
        System.out.println("Recebido email: " + email);
        System.out.println("Recebido senha: " + senha);
        System.out.println("Recebido id: " + idStr);

        if (nome == null || email == null || senha == null || idStr == null ||
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || idStr.trim().isEmpty()) {
            out.println("<p style='color:red;'>Todos os campos são obrigatórios!</p>");
            return;
        }

        try {
            int id = Integer.parseInt(idStr); // Converte o ID para int

            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                System.out.println("Conexão com o banco estabelecida.");
                conn.setAutoCommit(true);

                String senhaHash = hashSenha(senha);
                System.out.println("Senha hash gerada: " + senhaHash);

                String sql = "UPDATE USUARIO SET NOME = ?, EMAIL = ?, SENHA_HASH = ? WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senhaHash);
                    stmt.setInt(4, id);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        System.out.println("Atualização realizada. Linhas afetadas: " + linhasAfetadas);
                        out.println("<p style='color:green;'>Usuário atualizado com sucesso!</p>");
                    } else {
                        out.println("<p style='color:red;'>Usuário não encontrado com o ID informado!</p>");
                    }
                }
            } finally {
                conexaoBD.closeConexao();
            }

        } catch (NumberFormatException e) {
            out.println("<p style='color:red;'>ID inválido!</p>");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar no banco:");
            e.printStackTrace();
            out.println("<p style='color:red;'>Erro ao atualizar usuário: " + e.getMessage() + "</p>");
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
