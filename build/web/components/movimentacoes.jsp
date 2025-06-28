<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Extrato Financeiro</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />

</head>

<body>
    <div class="container mt-4 form-card">
      <table id="extrato-table" class="table table-dark table-striped">
        <thead>
            <tr><th>Data</th><th>Descrição</th><th>Tipo</th><th>Valor</th><th>Nº Conta</th></tr>
        </thead>
        <tbody>
            <!-- dados virão aqui via JS -->
        </tbody>
      </table>
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
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("Login.jsp"); // Redireciona caso não esteja logado
        return;
    }
    var usuarioId = usuario.id;
    var limite = getParametro('limite');
    if (!usuarioId) {
    mostrarErroNaTela('Você precisa estar logado para acessar esta página.');
    } else {

    let url = 'Movimentacao?usuarioId=' + usuarioId;
    if (limite) {
      url += '&limite=' + limite;
    }

    fetch(url)
      .then(res => res.json())
      .then(data => {
        const tbody = document.querySelector('#extrato-table tbody');
        tbody.innerHTML = '';

        data.forEach(mov => {
          const tr = document.createElement('tr');

          const tdData = document.createElement('td');
          tdData.textContent = mov.data; // já está em formato "dd/mm/yyyy"
          tr.appendChild(tdData);

          const tdDesc = document.createElement('td');
          tdDesc.textContent = mov.descricao;
          tr.appendChild(tdDesc);

          // NOVA CÉLULA TIPO
          const tdTipo = document.createElement('td');
          tdTipo.textContent = mov.tipo;
          tr.appendChild(tdTipo);

          const tdValor = document.createElement('td');

          var sinal = (mov.tipo === 'ENTRADA') ? '+' : '-';

          tdValor.textContent = "R$ " + sinal + Number(mov.valor).toFixed(2).replace('.', ',');

          tdValor.className = (mov.tipo === 'ENTRADA') ? 'valor-positivo' : 'valor-negativo';

          tr.appendChild(tdValor);

          const tdConta = document.createElement('td');
          tdConta.textContent = mov.conta || ''; // ajuste conforme seu JSON
          tr.appendChild(tdConta);


          tbody.appendChild(tr);
        });

      })
      .catch(err => {
        console.error('Erro ao carregar movimentações:', err);
        alert('Erro ao carregar extrato.');
      });
}
</script>
</body>
</html>
