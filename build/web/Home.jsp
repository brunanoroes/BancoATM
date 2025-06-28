<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Página Inicial</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="container mt-4 form-card">
        <h2>Bem-vindo!</h2>

        <div id="container-contas">
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
        </div>

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
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        carregarContas();
    });

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
    function getParametro(nome) {
        const regex = new RegExp('[\\?&]' + nome + '=([^&#]*)');
        const resultados = regex.exec(window.location.search);
        return resultados === null ? '' : decodeURIComponent(resultados[1].replace(/\+/g, ' '));
    }

    function carregarContas() {
        let saldoTotal = 0;
        fetch('Conta')
            .then(res => res.json())
            .then(data => {
            const tbody = document.querySelector('#contas-table tbody');
            if (!tbody) return;

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

                const tdAcoes = document.createElement('td');
                const btn = document.createElement('button');
                btn.className = 'btn btn-sm btn-danger';
                btn.innerHTML = '<i class="bi bi-x-circle"></i> Apagar';

                btn.addEventListener('click', () => {
                Swal.fire({
                    title: 'Tem certeza?',
                    text: 'Você deseja apagar esta conta?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Sim, apagar',
                    cancelButtonText: 'Cancelar',
                    reverseButtons: true
                }).then((result) => {
                    if (result.isConfirmed) {
                    fetch('ApagarConta', {
                        method: 'POST',
                        headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'conta=' + encodeURIComponent(conta.conta)
                    })
                    .then(res => res.json())
                    .then(resultado => {
                        Swal.fire({
                        icon: resultado.status === 'sucesso' ? 'success' : 'error',
                        title: resultado.status === 'sucesso' ? 'Sucesso!' : 'Erro!',
                        text: resultado.mensagem
                        }).then(() => {
                        if (resultado.status === 'sucesso') {
                            carregarContas(); // recarrega a lista sem dar reload na página
                        }
                        });
                    })
                    .catch(err => {
                        console.error(err);
                        Swal.fire('Erro', 'Erro ao apagar conta.', 'error');
                    });
                    }
                });
                });

                tdAcoes.appendChild(btn);
                tr.appendChild(tdAcoes);

                saldoTotal += parseFloat(conta.saldo);
                tbody.appendChild(tr);
            });

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

