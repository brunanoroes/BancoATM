<div class="container mt-4 form-card">
  <h2>Cadastrar Nova Conta</h2>
  <form id="formConta" method="POST" action="/CadastrarConta">
    <!-- Você pode preencher esse campo com o ID do usuário logado -->
    <input type="hidden" name="usuarioId" value="1">

    <div class="mb-3">
      <label for="numeroConta" class="form-label">Número da Conta</label>
      <input type="text" class="form-control" id="numeroConta" name="numeroConta" required maxlength="20">
    </div>

    <div class="mb-3">
      <label for="tipoConta" class="form-label">Tipo de Conta</label>
      <select class="form-select" id="tipoConta" name="tipoConta" required>
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
