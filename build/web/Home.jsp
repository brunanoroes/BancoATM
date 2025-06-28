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
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

<div class="container mt-4 form-card">

    <h2>Minhas Contas</h2>

    <table id="extrato-table" class="table">
        <thead>
            <tr>
                <th>Conta</th>
                <th>Tipo</th>
                <th>Data de Abertura</th>
                <th>Saldo</th>
            </tr>
        </thead>
        <tbody>
            <%
                double saldoTotal = 0;
                for (Conta c : contas) {
                    saldoTotal += c.getSaldo();
                    String saldoClass = c.getSaldo() >= 0 ? "valor-positivo" : "valor-negativo";
                    String saldoFormatado = String.format("%.2f", c.getSaldo()).replace('.', ',');
            %>
            <tr>
                <td><%= c.getConta() %></td>
                <td><%= c.getTipo() %></td>
                <td><%= c.getData() %></td>
                <td class="<%= saldoClass %>">R$ <%= saldoFormatado %></td>
            </tr>
            <% } %>
        </tbody>
        <tfoot>
            <tr>
                <th colspan="3" style="text-align: right;">Saldo Total:</th>
                <th>R$ <%= String.format("%.2f", saldoTotal).replace('.', ',') %></th>
            </tr>
        </tfoot>
    </table>


    <hr>

    <h2>Cadastrar Nova Conta</h2>

    <form method="post" action="CadastroConta" class="form-card">
        <input type="hidden" name="usuarioId" value="<%= usuario.getId() %>">
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
        <button type="submit" class="btn btn-yellow">Cadastrar Conta</button>
    </form>

</div>

</body>
</html>
