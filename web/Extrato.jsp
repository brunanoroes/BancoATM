<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Extrato Financeiro</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
   <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
   <style>
  body {
    background-color: #f8f9fa;
  }

  .form-card {
    background-color: #212529; /* bg-dark */
    color: white;
    border: none;
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.25);
    padding: 30px;
  }

  h2 {
    color: #ffffff;
    margin-bottom: 20px;
  }

  /* Estilo da tabela */
  #extrato-table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0 0.75rem; /* espaço entre as linhas */
  }

  #extrato-table thead tr {
    background-color: #343a40;
    color: #ffc107; /* amarelo banco */
    text-align: left;
    font-weight: 600;
  }

  #extrato-table tbody tr {
    background-color: #2c3035;
    border-radius: 8px;
    transition: background-color 0.3s ease;
  }

  #extrato-table tbody tr:hover {
    background-color: #3e444a;
  }

  #extrato-table th, #extrato-table td {
    padding: 12px 20px;
  }

  /* Valores positivos e negativos */
  .valor-positivo {
    color: #28a745 !important; /* verde */
    font-weight: bold;
  }

  .valor-negativo {
    color: #dc3545 !important; /* vermelho */
    font-weight: bold;
  }
</style>

</head>

<body>
    <%@ include file="/components/navbar.jsp" %>
    <div class="container mt-4 form-card">
            <h2>Extrato Financeiro</h2>
            <%@ include file="/components/movimentacoes.jsp" %>
    </div>

</body>
</html>
