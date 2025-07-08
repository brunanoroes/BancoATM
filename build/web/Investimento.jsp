<%@page import="Models.Conta"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, Models.Investimento, java.text.SimpleDateFormat, java.math.BigDecimal" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Investimentos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
  <link rel="stylesheet" href="css/style.css">
  <style>
    body { background-color: #3a3f44 !important; }
  </style>
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

<div class="container mt-5 content-card">
    <h2>Investimentos</h2>
 <% String erro = (String) request.getAttribute("erro"); %>
    <% if (erro != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= erro %>
        </div>
    <% } %>
    <!-- Formulário para novo investimento -->
    <form action="Investimento?action=insert" method="post" class="row g-3 mb-4">
        <div class="col-md-4">
            <label for="contaId" class="form-label">Conta</label>
            <select class="form-select" name="contaId" id="contaId" required>
                <option value="">Selecione</option>
                <%
                    List<Conta> contas = (List<Conta>) request.getAttribute("contas");
                    if (contas != null) {
                        for (Conta conta : contas) {
                %>
                            <option value="<%= conta.getId() %>">
                                <%= conta.getTipo() %> - Nº <%= conta.getConta() %>
                            </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="col-md-4">
            <label for="tipoInvestimento" class="form-label">Tipo de Investimento</label>
            <select class="form-select" name="tipoInvestimento" id="tipoInvestimento" required>
                <option value="">Selecione</option>
                <option value="CDB">CDB</option>
                <option value="Tesouro Direto">Tesouro Direto</option>
                <option value="LCI">LCI</option>
                <option value="Fundos">Fundos</option>
            </select>
        </div>

        <div class="col-md-4">
            <label for="valorAplicado" class="form-label">Valor Aplicado</label>
            <input type="number" step="0.01" class="form-control" name="valorAplicado" id="valorAplicado" required />
        </div>

        <div class="col-md-6">
            <label for="dataVencimento" class="form-label">Data de Vencimento</label>
            <input type="date" class="form-control" name="dataVencimento" id="dataVencimento" required />
        </div>

        <div class="col-md-6">
            <label class="form-label">Taxa Anual</label>
            <input type="text" class="form-control" value="15%" readonly />
        </div>

        <div class="col-12 text-end">
            <button type="submit" class="btn btn-yellow">Aplicar Investimento</button>
        </div>
    </form>

    <!-- Tabela de investimentos existentes -->
    <h4 class="form-label mt-4">Investimentos Ativos</h4>
    <table class="table table-dark table-striped">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Valor Aplicado</th>
                <th>Rendimento Esperado</th>
                <th>Taxa Anual (%)</th>
                <th>Data Aplicação</th>
                <th>Vencimento</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Investimento> investimentos = (List<Investimento>) request.getAttribute("investimentos");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if (investimentos != null && !investimentos.isEmpty()) {
                    for (Investimento inv : investimentos) {
            %>
                        <tr>
                            <td><%= inv.getId() %></td>
                            <td><%= inv.getTipoInvestimento() %></td>
                            <td>R$ <%= inv.getValorAplicado() %></td>
                            <td>R$ <%= inv.getRendimentoEsperado() %></td>
                            <td><%= inv.getTaxaAnualPercentual() %>%</td>
                            <td><%= inv.getDataAplicacao() != null ? sdf.format(inv.getDataAplicacao()) : "-" %></td>
                            <td><%= inv.getDataVencimento() != null ? sdf.format(inv.getDataVencimento()) : "-" %></td>
                            <td>
                                <form action="Investimento" method="get" class="d-inline">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="id" value="<%= inv.getId() %>" />
                                    <button type="submit" class="btn btn-sm btn-danger">
                                        <i class="bi bi-x-circle"></i> Resgatar
                                    </button>
                                </form>
                            </td>
                        </tr>
            <%
                    }
                } else {
            %>
                    <tr>
                        <td colspan="8" class="text-center">Nenhum investimento ativo.</td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

