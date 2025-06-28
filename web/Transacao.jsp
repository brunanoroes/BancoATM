<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Models.Usuario" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Transações</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
  <link rel="stylesheet" href="css/style.css" />
  <style>
    body { background-color: #3a3f44 !important; }
  </style>

</head>
<body>

<%
    String msgSucesso = (String) session.getAttribute("msgSucesso");
    String msgErro = (String) session.getAttribute("msgErro");
    if (msgSucesso != null) {
%>
<div class="alert alert-success" role="alert"><%= msgSucesso %></div>
<%
        session.removeAttribute("msgSucesso");
    }
    if (msgErro != null) {
%>
<div class="alert alert-danger" role="alert"><%= msgErro %></div>
<%
        session.removeAttribute("msgErro");
    }
%>

<%@ include file="/components/navbar.jsp" %>

<div class="container mt-5 content-card">
    <h2>Transações</h2>

    <!-- Formulário para nova transação -->
    <form action="CadastroMovimentacao" method="post" class="row g-3 mb-4">
        <div class="col-md-4">
            <label for="tipoTransacao" class="form-label">Tipo de Transação</label>
            <select class="form-select" name="tipoTransacao" id="tipoTransacao" required>
                <option value="">Selecione</option>
                <option value="Deposito" <%= "Deposito".equals(request.getParameter("tipoTransacao")) ? "selected" : "" %>>Depósito</option>
                <option value="Saque" <%= "Saque".equals(request.getParameter("tipoTransacao")) ? "selected" : "" %>>Saque</option>
                <option value="Transferencia" <%= "Transferencia".equals(request.getParameter("tipoTransacao")) ? "selected" : "" %>>Transferência</option>
            </select>
        </div>

        <div class="col-md-4">
            <label for="contaOrigem" class="form-label">Conta Origem</label>
            <select class="form-select" name="contaOrigem" id="contaOrigem" required>
                <option value="">Selecione</option>
                <%
                    List<String[]> contasUsuario = (List<String[]>) request.getAttribute("contasUsuario");
                    if (contasUsuario != null) {
                        for (String[] conta : contasUsuario) {
                            String id = conta[0];
                            String numero = conta[1];
                            String tipoConta = conta[2];
                %>
                <option value="<%= id %>" <%= id.equals(request.getParameter("contaOrigem")) ? "selected" : "" %>>
                    <%= numero %> - <%= tipoConta %>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="col-md-4">
            <label for="contaDestino" class="form-label">Conta Destino</label>
            <input type="text" class="form-control" name="contaDestino" id="contaDestino"
                   placeholder="Campo somente para transferências"
                   value="<%= request.getParameter("contaDestino") != null ? request.getParameter("contaDestino") : "" %>"
                   disabled />
        </div>

        <div class="col-md-6">
            <label for="valor" class="form-label">Valor (R$)</label>
            <input type="number" step="0.01" class="form-control" name="valor" id="valor" required
                   value="<%= request.getParameter("valor") != null ? request.getParameter("valor") : "" %>" />
        </div>

        <div class="col-md-6">
            <label for="descricao" class="form-label">Descrição</label>
            <input type="text" class="form-control" name="descricao" id="descricao" required
                   value="<%= request.getParameter("descricao") != null ? request.getParameter("descricao") : "" %>" />
        </div>

        <div class="col-12 text-end">
            <button type="submit" class="btn btn-warning">Registrar Transação</button>
        </div>
    </form>

    <!-- Tabela de transações existentes -->
    <h4 class="form-label mt-4">Últimas Transações</h4>
    <%@ include file="/components/movimentacoes.jsp" %>
    <a id="link-ver-mais" href="/BancoATM/Movimentacao" class="ver-mais-link">Ver mais</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Habilita/desabilita campo contaDestino conforme tipoTransacao selecionado
    const tipoTransacaoSelect = document.getElementById('tipoTransacao');
    const contaDestinoInput = document.getElementById('contaDestino');

    function toggleContaDestino() {
        if (tipoTransacaoSelect.value === 'Transferencia') {
            contaDestinoInput.disabled = false;
            contaDestinoInput.required = true;
        } else {
            contaDestinoInput.disabled = true;
            contaDestinoInput.required = false;
            contaDestinoInput.value = '';
        }
    }

    tipoTransacaoSelect.addEventListener('change', toggleContaDestino);
    window.addEventListener('load', toggleContaDestino);
</script>

</body>
</html>
