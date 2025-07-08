package Controllers;

import Models.Conexao;
import Models.Usuario; // Certifique-se que esta importação está correta
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.sql.*;

@WebServlet(name = "PerfilServlet", urlPatterns = {"/PerfilServlet"})
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Verifica se há usuário logado na sessão
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("Logout");
            return;
        }

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String idStr = request.getParameter("id");

        // Validação
        if (nome == null || email == null || senha == null || idStr == null ||
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || idStr.trim().isEmpty()) {
            out.println("<p style='color:red;'>Todos os campos são obrigatórios!</p>");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            Conexao conexaoBD = new Conexao();
            try (Connection conn = conexaoBD.getConexao()) {
                conn.setAutoCommit(true);
                String senhaHash = hashSenha(senha);

                String sql = "UPDATE USUARIO SET NOME = ?, EMAIL = ?, SENHA_HASH = ? WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senhaHash);
                    stmt.setInt(4, id);

                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        // Atualiza o objeto da sessão
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setSenha(senhaHash); // Certifique-se que esse método existe
                        request.getSession().setAttribute("usuario", usuario);

                        response.sendRedirect("Perfil.jsp?msg=sucesso");
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
