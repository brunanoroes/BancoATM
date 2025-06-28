package Controllers;

import Models.Conexao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "CadastroMovimentacaoServlet", urlPatterns = {"/CadastroMovimentacao"})
public class CadastroMovimentacaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pega a sessão e garante login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            // Parâmetros do formulário
            String tipoTransacao = request.getParameter("tipoTransacao");
            int contaOrigem = Integer.parseInt(request.getParameter("contaOrigem"));
            String contaDestinoStr = request.getParameter("contaDestino");
            Integer contaDestino = (contaDestinoStr != null && !contaDestinoStr.isEmpty()) ? Integer.parseInt(contaDestinoStr) : null;
            double valor = Double.parseDouble(request.getParameter("valor"));
            String descricao = request.getParameter("descricao");

            // Validação mínima
            if (valor <= 0 || tipoTransacao == null || tipoTransacao.isEmpty()) {
                response.sendRedirect("Transacao.jsp?erro=valorInvalido");
                return;
            }

            try (Connection conn = new Conexao().getConexao()) {

                // Para Depósito ou Saque, só 1 registro
                if (tipoTransacao.equalsIgnoreCase("Deposito")) {
                    String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, 'ENTRADA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, contaOrigem);
                        ps.setDouble(2, valor);
                        ps.setString(3, descricao);
                        ps.executeUpdate();
                    }

                } else if (tipoTransacao.equalsIgnoreCase("Saque")) {
                    String sql = "INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, 'SAIDA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, contaOrigem);
                        ps.setDouble(2, valor);
                        ps.setString(3, descricao);
                        ps.executeUpdate();
                    }

                } else if (tipoTransacao.equalsIgnoreCase("Transferencia")) {
                    if (contaDestino == null || contaDestino.equals(contaOrigem)) {
                        response.sendRedirect("Transacao.jsp?erro=contaDestinoInvalida");
                        return;
                    }

                    // Saída da conta de origem
                    String sqlSaida = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'SAIDA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlSaida)) {
                        ps.setInt(1, contaOrigem);
                        ps.setInt(2, contaDestino);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }

                    // Entrada na conta de destino
                    String sqlEntrada = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'ENTRADA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlEntrada)) {
                        ps.setInt(1, contaDestino);
                        ps.setInt(2, contaOrigem);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }
                }

                // Redireciona para ver as movimentações ou uma tela de sucesso
                response.sendRedirect("Movimentacao?limite=4");

            }

        } catch (Exception e) {
            throw new ServletException("Erro ao registrar movimentação: " + e.getMessage(), e);
        }
    }
}
