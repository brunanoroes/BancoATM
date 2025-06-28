package Controllers;

import Models.Conexao;
import Models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "CadastroContaServlet", urlPatterns = {"/CadastroConta"})
public class CadastroContaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        int usuarioId = usuario.getId();

        String numero = request.getParameter("numero");
        String tipo = request.getParameter("tipo");
        String saldoStr = request.getParameter("saldo");

        System.out.println("Cadastro Conta - Dados recebidos:");
        System.out.println("usuarioId: " + usuarioId);
        System.out.println("numero: " + numero);
        System.out.println("tipo: " + tipo);
        System.out.println("saldo: " + saldoStr);

        if (numero == null || tipo == null || numero.trim().isEmpty() || tipo.trim().isEmpty()) {
            session.setAttribute("msgErro", "Número e tipo da conta são obrigatórios.");
            response.sendRedirect("Conta");
            return;
        }

        double saldo = 0;
        if (saldoStr != null && !saldoStr.trim().isEmpty()) {
            try {
                saldo = Double.parseDouble(saldoStr);
            } catch (NumberFormatException e) {
                System.err.println("Saldo inválido: " + saldoStr);
                session.setAttribute("msgErro", "Saldo inválido. Usando 0 como valor padrão.");
                saldo = 0;
            }
        }

        try (Connection conn = new Conexao().getConexao()) {
            // Verifica se o número da conta já existe
            String checkSql = "SELECT COUNT(*) FROM CONTA WHERE NUMERO_CONTA = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, numero);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        session.setAttribute("msgErro", "Número de conta já cadastrado!");
                        response.sendRedirect("Conta");
                        return;
                    }
                }
            }

            // Insere nova conta
            String sql = "INSERT INTO CONTA (USUARIO_ID, NUMERO_CONTA, TIPO_CONTA, SALDO) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                stmt.setString(2, numero);
                stmt.setString(3, tipo);
                stmt.setDouble(4, saldo);
                int rows = stmt.executeUpdate();
                System.out.println("Linhas inseridas: " + rows);
                if (rows > 0) {
                    session.setAttribute("msgSucesso", "Conta cadastrada com sucesso!");
                } else {
                    session.setAttribute("msgErro", "Não foi possível cadastrar a conta.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgErro", "Erro ao cadastrar conta: " + e.getMessage());
        }

        response.sendRedirect("Conta");
    }
}
