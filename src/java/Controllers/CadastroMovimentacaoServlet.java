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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Logout");
            return;
        }

        try {
            String tipoTransacao = request.getParameter("tipoTransacao");
            int contaOrigem = Integer.parseInt(request.getParameter("contaOrigem"));
            String contaDestinoStr = request.getParameter("contaDestino");
            Integer contaDestino = (contaDestinoStr != null && !contaDestinoStr.isEmpty()) ? Integer.parseInt(contaDestinoStr) : null;
            double valor = Double.parseDouble(request.getParameter("valor"));
            String descricao = request.getParameter("descricao");

            if (valor <= 0 || tipoTransacao == null || tipoTransacao.isEmpty()) {
                session.setAttribute("msgErro", "Valor ou tipo de transação inválido!");
                response.sendRedirect("Movimentacao?limite=4");
                return;
            }

            try (Connection conn = new Conexao().getConexao()) {

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
                        session.setAttribute("msgErro", "Conta de destino inválida para transferência.");
                        response.sendRedirect("Movimentacao?limite=4");
                        return;
                    }

                    String sqlSaida = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'SAIDA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlSaida)) {
                        ps.setInt(1, contaOrigem);
                        ps.setInt(2, contaDestino);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }

                    String sqlEntrada = "INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO) VALUES (?, ?, 'ENTRADA', ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlEntrada)) {
                        ps.setInt(1, contaDestino);
                        ps.setInt(2, contaOrigem);
                        ps.setDouble(3, valor);
                        ps.setString(4, descricao);
                        ps.executeUpdate();
                    }
                }

                // ✅ Sucesso
                session.setAttribute("msgSucesso", "Transação registrada com sucesso!");
                response.sendRedirect("Movimentacao?limite=4");

            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgErro", "Erro ao registrar movimentação: " + e.getMessage());
            response.sendRedirect("Movimentacao?limite=4");
            return;
        }


    }
}
