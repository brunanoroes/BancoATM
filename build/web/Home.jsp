<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Conta" %>
<%@ page import="Models.Usuario" %>

<%
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect("Login.jsp");
        return;
    }

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    List<Conta> contas = (List<Conta>) request.getAttribute("contas");
    if (contas == null) contas = new java.util.ArrayList<>();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Minhas Contas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

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

<div class="container mt-5 form-card">

    <h2>Minhas Contas</h2>

    <%
        double saldoTotal = 0;
        for (Conta c : contas) {
            saldoTotal += c.getSaldo();
        }
    %>

    <div class="text-end mb-3">
        <h5>Saldo Total: <strong>R$ <%= String.format("%.2f", saldoTotal).replace('.', ',') %></strong></h5>
    </div>

    <table id="extrato-table" class="table table-dark table-striped">
        <thead>
            <tr>
                <th>Conta</th>
                <th>Tipo</th>
                <th>Data de Abertura</th>
                <th>Saldo</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (Conta c : contas) {
                    String saldoClass = c.getSaldo() >= 0 ? "valor-positivo" : "valor-negativo";
                    String saldoFormatado = String.format("%.2f", c.getSaldo()).replace('.', ',');
            %>
            <tr>
                <td><%= c.getConta() %></td>
                <td><%= c.getTipo() %></td>
                <td><%= c.getData() %></td>
                <td class="<%= saldoClass %>">R$ <%= saldoFormatado %></td>
                <td>
                    <!-- Botão para abrir o modal -->
                    <button type="button" 
                            class="btn btn-danger btn-sm" 
                            data-bs-toggle="modal" 
                            data-bs-target="#confirmModal<%= c.getConta() %>">
                        Apagar
                    </button>

                    <!-- Modal -->
                    <div class="modal fade" id="confirmModal<%= c.getConta() %>" tabindex="-1" aria-labelledby="confirmModalLabel<%= c.getConta() %>" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="confirmModalLabel<%= c.getConta() %>">Confirmar Exclusão</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
                                </div>
                                <div class="modal-body">
                                    Tem certeza de que deseja apagar a conta <strong><%= c.getConta() %></strong>?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                    <form method="post" action="ApagarConta" style="display:inline;">
                                        <input type="hidden" name="conta" value="<%= c.getConta() %>" />
                                        <button type="submit" class="btn btn-danger">Sim, Apagar</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <hr>

    <h2>Cadastrar Nova Conta</h2>

    <form method="post" action="CadastroConta" class="form-card">
        <div class="mb-3">
            <label for="numeroConta" class="form-label">Número da Conta</label>
            <input type="text" id="numeroConta" name="numero" maxlength="20" required class="form-control">
        </div>
        <div class="mb-3">
            <label for="tipoConta" class="form-label">Tipo de Conta</label>
            <select id="tipoConta" name="tipo" required class="form-select">
                <option value="">Selecione</option>
                <option value="CORRENTE">Corrente</option>
                <option value="POUPANCA">Poupança</option>
                <option value="DIGITAL">Digital</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="saldo" class="form-label">Saldo Inicial</label>
            <input type="number" id="saldo" name="saldo" min="0" step="0.01" class="form-control">
        </div>
        <button type="submit" class="btn btn-warning">Cadastrar Conta</button>
    </form>

</div>

</body>
</html>
