<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Transações</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />

  <style>
    body {
        background-color: #f8f9fa;
    }

    .content-card {
        background-color: #212529;
        color: white;
        border-radius: 12px;
        padding: 30px;
        margin-top: 40px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
    }

    .form-label {
        color: #ffc107;
        font-weight: bold;
    }

    .form-control, .form-select {
        background-color: #343a40;
        color: white;
        border: 1px solid #495057;
    }

    .form-control:focus, .form-select:focus {
        border-color: #ffc107;
        box-shadow: 0 0 0 0.25rem rgba(255, 193, 7, 0.25);
    }

    .btn-yellow {
        background-color: #ffc107;
        color: #212529;
        font-weight: bold;
        border: none;
    }

    .btn-yellow:hover {
        background-color: #e0a800;
    }

    table thead th {
        color: #ffc107;
    }
  </style>
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
                <button type="submit" class="btn btn-yellow">Registrar Transação</button>
            </div>
        </form>

        <!-- Tabela de transações existentes -->
        <h4 class="form-label mt-4">Histórico de Transações</h4>
        <table class="table table-dark table-striped">
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
                <!-- Exemplo de dados -->
                <tr>
                    <td>12/06/2025</td>
                    <td>Pagamento de boleto</td>
                    <td>Saque</td>
                    <td class="text-danger">R$ -300,00</td>
                    <td>Conta Corrente</td>
                    <td>-</td>
                </tr>
                <tr>
                    <td>10/06/2025</td>
                    <td>Depósito salário</td>
                    <td>Depósito</td>
                    <td class="text-success">R$ 3.500,00</td>
                    <td>Conta Corrente</td>
                    <td>-</td>
                </tr>
                <tr>
                    <td>08/06/2025</td>
                    <td>Transferência entre contas</td>
                    <td>Transferência</td>
                    <td class="text-warning">R$ 1.000,00</td>
                    <td>Conta Corrente</td>
                    <td>Conta Poupança</td>
                </tr>
            </tbody>
        </table>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
