<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Página Inicial</title>
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

    .form-label {
        font-weight: bold;
        color: #ffc107; /* Amarelo estilo banco */
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

    .btn-save {
        background-color: #ffc107;
        color: #212529;
        font-weight: bold;
        border: none;
    }

    .btn-save:hover {
        background-color: #e0a800;
    }

    h2 {
        color: #ffffff;
        margin-bottom: 20px;
    }
  </style>
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="container mt-4 form-card">
        <h2>Bem-vindo!</h2>

        <div id="container-contas">
            <%@ include file="/components/contas.jsp" %>
        </div>

       <%@ include file="/components/cadastrocontas.jsp" %>
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

