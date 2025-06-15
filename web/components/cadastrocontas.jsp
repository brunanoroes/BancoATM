<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="container mt-4 form-card" id="CadastroContaDiv">
  <h2>Cadastrar Nova Conta</h2>
  <form id="formConta">
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

<!-- SweetAlert2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  function getParametro(nome) {
    const regex = new RegExp('[\\?&]' + nome + '=([^&#]*)');
    const resultados = regex.exec(window.location.search);
    return resultados === null ? '' : decodeURIComponent(resultados[1].replace(/\+/g, ' '));
  }

  document.addEventListener('DOMContentLoaded', () => {
    const usuarioId = getParametro('usuarioId');
    if (!usuarioId) {
      document.getElementById('CadastroContaDiv').style.display = 'none'; // Esconde o formulário
      return;
    }
    document.getElementById('usuarioIdInput').value = usuarioId;

    document.getElementById('formConta').addEventListener('submit', async (e) => {
      e.preventDefault();

      const form = e.target;
      const formData = new FormData(form);
      
      try {
        const dados = new URLSearchParams(new FormData(form));

          const response = await fetch('CadastroConta', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: dados
          });

        const result = await response.json();

        Swal.fire({
          icon: result.status === "sucesso" ? "success" : "error",
          title: result.mensagem
        });

        if (result.status === "sucesso") {
          form.reset();
        }

      } catch (error) {
        console.error("Erro ao enviar requisição:", error);
        Swal.fire("Erro", "Falha na comunicação com o servidor.", "error");
      }
    });
  });
</script>
