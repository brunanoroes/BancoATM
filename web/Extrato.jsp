<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Movimentacao" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Extrato Financeiro</title>
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

  <div class="container mt-5 form-card">
    <h2>Extrato Financeiro</h2>
    <%@ include file="/components/movimentacoes.jsp" %>
  </div>
</body>
</html>
