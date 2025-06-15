<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="container mt-4 form-card">
  <h2>Cadastrar Nova Conta</h2>
  <form id="formConta" method="POST" action="CadastroConta">
    <input type="hidden" name="usuarioId" id="usuarioIdInput">

    <div class="mb-3">
      <label for="numeroConta" class="form-label">Número da Conta</label>
      <input type="text" class="form-control" id="numeroConta" name="numero" required maxlength="20">
    </div>

    <div class="mb-3">
      <label for="tipoConta" class="form-label">Tipo de Conta</label>
      <select class="form-select" id="tipoConta" name="tipo" required>
        <option value="">Selecione</option>
        <option value="CORRENTE">Corrente</option>
        <option value="POUPANCA">Poupança</option>
        <option value="DIGITAL">Digital</option>
      </select>
    </div>

    <div class="mb-3">
      <label for="saldo" class="form-label">Saldo Inicial</label>
      <input type="number" class="form-control" id="saldo" name="saldo" step="0.01" min="0" placeholder="0,00">
    </div>

    <button type="submit" class="btn btn-warning">Cadastrar Conta</button>
  </form>
</div>
<script>
  function getParametro(nome) {
    const regex = new RegExp('[\\?&]' + nome + '=([^&#]*)');
    const resultados = regex.exec(window.location.search);
    return resultados === null ? '' : decodeURIComponent(resultados[1].replace(/\+/g, ' '));
  }

  function mostrarErroNaTela(mensagem) {
    const container = document.querySelector('.container') || document.body;
    container.innerHTML = '';
    const erroElem = document.createElement('p');
    erroElem.style.color = 'red';
    erroElem.style.fontWeight = 'bold';
    erroElem.style.fontSize = '1.2rem';
    erroElem.textContent = mensagem;
    container.appendChild(erroElem);
  }

  document.addEventListener('DOMContentLoaded', () => {
    const usuarioId = getParametro('usuarioId');
    if (!usuarioId) {
      mostrarErroNaTela('Você precisa estar logado para acessar esta página.');
      return;
    }

    document.getElementById('usuarioIdInput').value = usuarioId;
  });
</script>
