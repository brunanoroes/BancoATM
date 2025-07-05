package Controllers;

import Models.Conexao;
import Models.Conta;
import Models.Investimento;
import Models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InvestimentoServlet", urlPatterns = {"/Investimento"})
public class InvestimentoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Logout");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int usuarioId = usuario.getId();
        String action = request.getParameter("action");

        try (Connection conn = new Conexao().getConexao()) {

            // Executar extração automática de vencidos
            extrairInvestimentosVencidos(conn);
            
            // Se action for nula ou igual a "list", lista os investimentos
            if (action == null || "list".equals(action)) {
                List<Investimento> investimentos = listarInvestimentosPorUsuario(conn, usuarioId);
                List<Conta> contas = listarContasPorUsuario(conn, usuarioId); // ✅ NOVO
                request.setAttribute("investimentos", investimentos);
                request.setAttribute("contas", contas); // ✅ NOVO
                request.getRequestDispatcher("/Investimento.jsp").forward(request, response); // Corrigido caminho relativo
            } 
            else if ("delete".equals(action)) {
                int investimentoId = Integer.parseInt(request.getParameter("id"));
                cancelarInvestimento(conn, investimentoId); // <-- AQUI FAZ A LÓGICA COMPLETA
                response.sendRedirect("Investimento?action=list");
            }
        } catch (Exception e) {
            throw new ServletException("Erro no processamento: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("Logout");
            return;
        }

        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            try (Connection conn = new Conexao().getConexao()) {

                int contaId = Integer.parseInt(request.getParameter("contaId"));
                String tipo = request.getParameter("tipoInvestimento");
                BigDecimal valor = new BigDecimal(request.getParameter("valorAplicado"));
                String dataVenc = request.getParameter("dataVencimento");
                
                BigDecimal taxa = new BigDecimal("15.00");

                // Calcular rendimento proporcional (regra simples: taxa anual / 365 * dias)
                LocalDate hoje = LocalDate.now();
                LocalDate vencimento = LocalDate.parse(request.getParameter("dataVencimento"));
                long dias = ChronoUnit.DAYS.between(hoje, vencimento);

                BigDecimal rendimento = valor
                    .multiply(taxa)
                    .multiply(new BigDecimal(dias))
                    .divide(new BigDecimal(100 * 365), 2, RoundingMode.HALF_UP);

                // Usar java.sql.Date corretamente
                Timestamp dataVencimento = Timestamp.valueOf(dataVenc + " 00:00:00");

                conn.setAutoCommit(false); // Início da transação

                try {
                    // 1. Verificar saldo
                    BigDecimal saldoAtual = BigDecimal.ZERO;
                    String verificaSaldo = "SELECT SALDO FROM CONTA WHERE ID = ?";
                    try (PreparedStatement psSaldo = conn.prepareStatement(verificaSaldo)) {
                        psSaldo.setInt(1, contaId);
                        ResultSet rs = psSaldo.executeQuery();
                        if (rs.next()) {
                            saldoAtual = rs.getBigDecimal("SALDO");
                        } else {
                            throw new ServletException("Conta não encontrada.");
                        }
                    }

                    if (saldoAtual.compareTo(valor) < 0) {
                        throw new ServletException("Saldo insuficiente para aplicar investimento.");
                    }

                    // 2. Debitar da conta
                    String debitarSql = "UPDATE CONTA SET SALDO = SALDO - ? WHERE ID = ?";
                    try (PreparedStatement psDebito = conn.prepareStatement(debitarSql)) {
                        psDebito.setBigDecimal(1, valor);
                        psDebito.setInt(2, contaId);
                        psDebito.executeUpdate();
                    }

                    Timestamp dataAplicacao = new Timestamp(System.currentTimeMillis()); // data atual
                    // 3. Inserir investimento
                    String sql = "INSERT INTO INVESTIMENTO (CONTA_ID, TIPO_INVESTIMENTO, VALOR_APLICADO, DATA_APLICACAO, DATA_VENCIMENTO, RENDIMENTO_ESPERADO, TAXA_ANUAL_PERCENTUAL) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, contaId);
                        ps.setString(2, tipo);
                        ps.setBigDecimal(3, valor);
                        ps.setTimestamp(4, dataAplicacao);
                        ps.setTimestamp(5, dataVencimento);
                        ps.setBigDecimal(6, rendimento);
                        ps.setBigDecimal(7, taxa);
                        ps.executeUpdate();
                    }

                    conn.commit(); // Tudo certo!

                } catch (Exception e) {
                    conn.rollback();
                    throw new ServletException("Erro ao aplicar investimento: " + e.getMessage(), e);
                } finally {
                    conn.setAutoCommit(true);
                }

                response.sendRedirect("Investimento?action=list");

            } catch (Exception e) {
                throw new ServletException("Erro ao inserir investimento: " + e.getMessage(), e);
            }
        }
    }

    private List<Investimento> listarInvestimentosPorUsuario(Connection conn, int usuarioId) throws SQLException {
        List<Investimento> lista = new ArrayList<>();

        String sql = """
            SELECT i.ID, i.CONTA_ID, i.TIPO_INVESTIMENTO, i.VALOR_APLICADO, i.DATA_APLICACAO,
                   i.DATA_VENCIMENTO, i.RENDIMENTO_ESPERADO, i.TAXA_ANUAL_PERCENTUAL
            FROM INVESTIMENTO i
            JOIN CONTA c ON i.CONTA_ID = c.ID
            WHERE c.USUARIO_ID = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Investimento inv = new Investimento();
                inv.setId(rs.getInt("ID"));
                inv.setContaId(rs.getInt("CONTA_ID"));
                inv.setTipoInvestimento(rs.getString("TIPO_INVESTIMENTO"));
                inv.setValorAplicado(rs.getBigDecimal("VALOR_APLICADO"));
                inv.setDataAplicacao(rs.getTimestamp("DATA_APLICACAO"));
                inv.setDataVencimento(rs.getTimestamp("DATA_VENCIMENTO"));
                inv.setRendimentoEsperado(rs.getBigDecimal("RENDIMENTO_ESPERADO"));
                inv.setTaxaAnualPercentual(rs.getBigDecimal("TAXA_ANUAL_PERCENTUAL"));
                lista.add(inv);
            }
        }

        return lista;
    }

    private void extrairInvestimentosVencidos(Connection conn) throws SQLException {
        String selectSql = """
            SELECT ID, CONTA_ID, VALOR_APLICADO, RENDIMENTO_ESPERADO
            FROM INVESTIMENTO
            WHERE DATA_VENCIMENTO IS NOT NULL
              AND DATA_VENCIMENTO <= CURRENT_TIMESTAMP
        """;

        String updateContaSql = "UPDATE CONTA SET SALDO = SALDO + ? WHERE ID = ?";
        String deleteSql = "DELETE FROM INVESTIMENTO WHERE ID = ?";

        try (
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            PreparedStatement updateStmt = conn.prepareStatement(updateContaSql);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)
        ) {
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                int investimentoId = rs.getInt("ID");
                int contaId = rs.getInt("CONTA_ID");
                BigDecimal valorAplicado = rs.getBigDecimal("VALOR_APLICADO");
                BigDecimal rendimento = rs.getBigDecimal("RENDIMENTO_ESPERADO");

                BigDecimal totalReceber = valorAplicado.add(rendimento);

                // 1. Creditar na conta
                updateStmt.setBigDecimal(1, totalReceber);
                updateStmt.setInt(2, contaId);
                updateStmt.executeUpdate();

                // 2. Remover investimento
                deleteStmt.setInt(1, investimentoId);
                deleteStmt.executeUpdate();
            }
        }
    }
    
    private List<Conta> listarContasPorUsuario(Connection conn, int usuarioId) throws SQLException {
    List<Conta> contas = new ArrayList<>();

    String sql = "SELECT ID, NUMERO_CONTA, TIPO_CONTA, SALDO, DATA_ABERTURA FROM CONTA WHERE USUARIO_ID = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // ou outro formato que você use

        while (rs.next()) {
            Conta conta = new Conta();
            conta.setId(rs.getInt("ID"));
            conta.setConta(rs.getString("NUMERO_CONTA"));
            conta.setTipo(rs.getString("TIPO_CONTA"));
            conta.setSaldo(rs.getBigDecimal("SALDO").doubleValue()); 
            conta.setData(sdf.format(rs.getTimestamp("DATA_ABERTURA")));
            contas.add(conta);
        }
    }

    return contas;
    }
    
    private void cancelarInvestimento(Connection conn, int investimentoId) throws SQLException {
    // 1. Buscar dados do investimento (valor aplicado, data aplicação, taxa, conta_id)
    String sqlBuscar = "SELECT VALOR_APLICADO, DATA_APLICACAO, TAXA_ANUAL_PERCENTUAL, CONTA_ID FROM INVESTIMENTO WHERE ID = ?";
    BigDecimal valorAplicado;
    Timestamp dataAplicacao;
    BigDecimal taxaAnual;
    int contaId;

    try (PreparedStatement psBuscar = conn.prepareStatement(sqlBuscar)) {
        psBuscar.setInt(1, investimentoId);
        try (ResultSet rs = psBuscar.executeQuery()) {
            if (!rs.next()) {
                throw new SQLException("Investimento não encontrado para id: " + investimentoId);
            }
            valorAplicado = rs.getBigDecimal("VALOR_APLICADO");
            dataAplicacao = rs.getTimestamp("DATA_APLICACAO");
            taxaAnual = rs.getBigDecimal("TAXA_ANUAL_PERCENTUAL");
            contaId = rs.getInt("CONTA_ID");
        }
    }

    // 2. Calcular dias investidos até hoje
    long diasInvestidos = 0;
    if (dataAplicacao != null) {
        java.time.LocalDate dataInicio = dataAplicacao.toLocalDateTime().toLocalDate();
        java.time.LocalDate hoje = java.time.LocalDate.now();
        diasInvestidos = java.time.temporal.ChronoUnit.DAYS.between(dataInicio, hoje);
        if (diasInvestidos < 0) diasInvestidos = 0; // evita negativo
    }

    // 3. Calcular rendimento proporcional: valorAplicado * (taxaAnual / 100) * (diasInvestidos / 365)
    BigDecimal rendimento = BigDecimal.ZERO;
    if (diasInvestidos > 0) {
        rendimento = valorAplicado
            .multiply(taxaAnual)
            .multiply(BigDecimal.valueOf(diasInvestidos))
            .divide(BigDecimal.valueOf(100 * 365), 2, java.math.RoundingMode.HALF_UP);
    }

    // 4. Total a creditar na conta
    BigDecimal total = valorAplicado.add(rendimento);

    // 5. Atualizar saldo da conta somando total
    String sqlAtualizaSaldo = "UPDATE CONTA SET SALDO = SALDO + ? WHERE ID = ?";
    try (PreparedStatement psAtualiza = conn.prepareStatement(sqlAtualizaSaldo)) {
        psAtualiza.setBigDecimal(1, total);
        psAtualiza.setInt(2, contaId);
        psAtualiza.executeUpdate();
    }

    // 6. Apagar investimento
    String sqlDelete = "DELETE FROM INVESTIMENTO WHERE ID = ?";
    try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
        psDelete.setInt(1, investimentoId);
        psDelete.executeUpdate();
    }
    }
}


