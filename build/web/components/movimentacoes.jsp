<%@ page import="java.util.List" %>
<%@ page import="Models.Movimentacao" %>

<table id="extrato-table" class="table table-dark table-striped">
  <thead>
    <tr>
      <th>Data</th>
      <th>Descrição</th>
      <th>Tipo</th>
      <th>Valor</th>
      <th>Conta Origem</th>
      <th>Conta Destino</th>
    </tr>
  </thead>
  <tbody>
    <%
      List<Models.Movimentacao> movimentacoes = (List<Models.Movimentacao>) request.getAttribute("movimentacoes");
      if (movimentacoes != null && !movimentacoes.isEmpty()) {
        for (Models.Movimentacao m : movimentacoes) {
    %>
    <tr>
      <td><%= m.getData() %></td>
      <td><%= m.getDescricao() %></td>
      <td><%= m.getTipo() %></td>
      <td>R$ <%= String.format("%.2f", m.getValor()).replace('.', ',') %></td>
      <td><%= m.getContaOrigem() %></td>
      <td><%= m.getContaDestino() %></td>
    </tr>
    <%
        }
      } else {
    %>
    <tr>
      <td colspan="6" class="text-center">Nenhuma movimentação encontrada.</td>
    </tr>
    <%
      }
    %>
  </tbody>
</table>

