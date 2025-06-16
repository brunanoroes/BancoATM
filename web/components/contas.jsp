<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

  <div class="container mt-4 form-card">
    <div>
      <h4 class="form-label">Saldo Total:</h4>
      <p id="saldo-total" style="font-size: 1.5rem; font-weight: bold; color: #ffc107;">
        <!-- Saldo será preenchido via JS -->
      </p>
    </div>
    <div>
      <h4 class="form-label mt-4">Contas</h4>
      <table id="contas-table" class="table table-dark table-striped">
        <thead>
          <tr><th>Conta</th><th>Tipo</th><th>Data de Abertura</th><th>Saldo</th></tr>
        </thead>
        <tbody>
          <!-- dados virão aqui via JS -->
        </tbody>
      </table>
    </div>
  </div>

