<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Models.Usuario" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Transações</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="container content-card">
        <h2>Transações</h2>

        <!-- Formulário para nova transação -->
        <form action="TransacaoServlet" method="post" class="row g-3 mb-4">
            <div class="col-md-4">
                <label for="tipoTransacao" class="form-label">Tipo de Transação</label>
                <select class="form-select" name="tipoTransacao" id="tipoTransacao" required>
                    <option value="">Selecione</option>
                    <option value="Deposito">Depósito</option>
                    <option value="Saque">Saque</option>
                    <option value="Transferencia">Transferência</option>
                </select>
            </div>

            <div class="col-md-4">
                <label for="contaOrigem" class="form-label">Conta de Origem</label>
                <select class="form-select" name="contaOrigem" id="contaOrigem" required>
                    <option value="">Selecione</option>
                    <option value="1">Conta Corrente</option>
                    <option value="2">Conta Poupança</option>
                </select>
            </div>

            <div class="col-md-4">
                <label for="contaDestino" class="form-label">Conta de Destino</label>
                <select class="form-select" name="contaDestino" id="contaDestino">
                    <option value="">(Somente para transferência)</option>
                    <option value="1">Conta Corrente</option>
                    <option value="2">Conta Poupança</option>
                </select>
            </div>

            <div class="col-md-6">
                <label for="valor" class="form-label">Valor (R$)</label>
                <input type="number" step="0.01" class="form-control" name="valor" id="valor" required />
            </div>

            <div class="col-md-6">
                <label for="descricao" class="form-label">Descrição</label>
                <input type="text" class="form-control" name="descricao" id="descricao" required />
            </div>

            <div class="col-12 text-end">
                <button type="submit" class="btn btn-warning">Registrar Transação</button>
            </div>
        </form>

        <!-- Tabela de transações existentes -->
        <h4 class="form-label mt-4">Últimas Transações</h4>
        <%@ include file="/components/movimentacoes.jsp" %>
        <a id="link-ver-mais" href="#" class="ver-mais-link">Ver mais</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<script>
    function getParametro(nome) {
        const regex = new RegExp('[\\?&]' + nome + '=([^&#]*)');
        const resultados = regex.exec(window.location.search);
        return resultados === null ? '' : decodeURIComponent(resultados[1].replace(/\+/g, ' '));
    }

    if (usuarioId) {
        const link = document.getElementById('link-ver-mais');
        link.href = '/BancoATM/Extrato.jsp?usuarioId=' + encodeURIComponent(usuarioId);
    }
</script>
