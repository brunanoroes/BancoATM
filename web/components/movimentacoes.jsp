<%@ page import="java.util.List" %>
<%@ page import="Models.Movimentacao" %>

<table id="extrato-table" class="table table-dark table-striped">
  <thead>
    <tr><th>Data</th><th>Descrição</th><th>Tipo</th><th>Valor</th><th>Nº Conta</th></tr>
  </thead>
  <tbody>
    <%
      List<Movimentacao> movimentacoes = (List<Movimentacao>) request.getAttribute("movimentacoes");
      if (movimentacoes != null && !movimentacoes.isEmpty()) {
          for (Movimentacao m : movimentacoes) {
    %>
    <tr>
      <td><%= m.getData() %></td>
      <td><%= m.getDescricao() %></td>
      <td><%= m.getTipo() %></td>
      <td>R$ <%= String.format("%.2f", m.getValor()).replace('.', ',') %></td>
      <td><%= m.getConta() %></td>
    </tr>
    <%
          }
      } else {
    %>
    <tr>
      <td colspan="5" class="text-center">Nenhuma movimentação encontrada.</td>
    </tr>
    <%
      }
    %>
  </tbody>
</table>
