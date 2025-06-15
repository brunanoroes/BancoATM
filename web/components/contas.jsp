<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Contas</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
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

<script>
    function getParametro(nome) {
        var regex = new RegExp('[\\?&]' + nome + '=([^&#]*)');
        var resultados = regex.exec(window.location.search);
        return resultados === null ? '' : decodeURIComponent(resultados[1].replace(/\+/g, ' '));
    }

    // Função para mostrar erro na tela
    function mostrarErroNaTela(mensagem) {
    // Seleciona o container onde quer mostrar o erro, ou cria um novo elemento
    var container = document.querySelector('.container') || document.body;

    // Cria um elemento p com a mensagem
    var erroElem = document.createElement('p');
    erroElem.style.color = 'red';
    erroElem.style.fontWeight = 'bold';
    erroElem.style.fontSize = '1.2rem';
    erroElem.textContent = mensagem;

    // Limpa conteúdo anterior (se quiser)
    container.innerHTML = '';
    // Adiciona o erro
    container.appendChild(erroElem);
    }
    var usuarioId = getParametro('usuarioId');
    if (!usuarioId) {
    mostrarErroNaTela('Você precisa estar logado para acessar esta página.');
    } else {
    let saldoTotal = 0;
  fetch('Conta?usuarioId=' + usuarioId)
    .then(res => res.json())
    .then(data => {
      const tbody = document.querySelector('#contas-table tbody');
      tbody.innerHTML = '';

      data.forEach(conta => {
      const tr = document.createElement('tr');

      const tdConta = document.createElement('td');
      tdConta.textContent = conta.conta;
      tr.appendChild(tdConta);

      const tdTipo = document.createElement('td');
      tdTipo.textContent = conta.tipo;
      tr.appendChild(tdTipo);

      const tdData = document.createElement('td');
      tdData.textContent = conta.data;
      tr.appendChild(tdData);

      const tdSaldo = document.createElement('td');
      tdSaldo.textContent = parseFloat(conta.saldo).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      });
      tr.appendChild(tdSaldo);

      saldoTotal += parseFloat(conta.saldo);

      tbody.appendChild(tr);
    });

      // Agora que terminou de somar, atualiza o valor na tela
      document.getElementById('saldo-total').textContent = saldoTotal.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      });
    })
    .catch(err => {
      console.error('Erro ao carregar contas:', err);
      alert('Erro ao carregar contas.');
    });

}
</script>
</body>
</html>
