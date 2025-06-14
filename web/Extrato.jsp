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

    .extrato-card {
        background-color: #212529;
        color: white;
        border-radius: 12px;
        padding: 25px 30px;
        margin-top: 30px;
        box-shadow: 0 4px 8px rgba(0,0,0,0.25);
    }

    h2 {
        color: #ffc107;
        margin-bottom: 20px;
    }

    .table thead th {
        border-bottom: 2px solid #ffc107;
    }

    .table-dark tbody tr:hover {
        background-color: #343a40;
    }

    .valor-positivo {
        color: #28a745; /* verde */
        font-weight: 600;
    }

    .valor-negativo {
        color: #dc3545; /* vermelho */
        font-weight: 600;
    }
  </style>
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="container">
        <div class="extrato-card">
            <h2>Extrato Financeiro</h2>

            <table class="table table-dark table-striped">
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Descrição</th>
                        <th>Valor</th>
                        <th>Conta</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Dados estáticos para exemplo -->
                    <tr>
                        <td>12/06/2025</td>
                        <td>Pagamento Energia</td>
                        <td class="valor-negativo">R$ -230,00</td>
                        <td>Conta Corrente</td>
                    </tr>
                    <tr>
                        <td>10/06/2025</td>
                        <td>Depósito Salário</td>
                        <td class="valor-positivo">R$ 3.500,00</td>
                        <td>Conta Corrente</td>
                    </tr>
                    <tr>
                        <td>09/06/2025</td>
                        <td>Compra Supermercado</td>
                        <td class="valor-negativo">R$ -450,50</td>
                        <td>Conta Cartão</td>
                    </tr>
                    <tr>
                        <td>07/06/2025</td>
                        <td>Transferência recebida</td>
                        <td class="valor-positivo">R$ 1.200,00</td>
                        <td>Conta Poupança</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
