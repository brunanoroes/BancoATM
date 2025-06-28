<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Investimentos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="container content-card">
        <h2>Investimentos</h2>

        <!-- Formulário para novo investimento -->
        <form action="InvestimentoServlet" method="post" class="row g-3 mb-4">
            <div class="col-md-4">
                <label for="contaId" class="form-label">Conta</label>
                <select class="form-select" name="contaId" id="contaId" required>
                    <option value="">Selecione</option>
                    <option value="1">Conta Corrente</option>
                    <option value="2">Conta Poupança</option>
                    <!-- Adicione opções dinamicamente conforme necessário -->
                </select>
            </div>

            <div class="col-md-4">
                <label for="tipoInvestimento" class="form-label">Tipo de Investimento</label>
                <select class="form-select" name="tipoInvestimento" id="tipoInvestimento" required>
                    <option value="">Selecione</option>
                    <option value="CDB">CDB</option>
                    <option value="Tesouro Direto">Tesouro Direto</option>
                    <option value="LCI">LCI</option>
                    <option value="Fundos">Fundos</option>
                </select>
            </div>

            <div class="col-md-4">
                <label for="valor" class="form-label">Valor Aplicado</label>
                <input type="number" step="0.01" class="form-control" name="valor" id="valor" required />
            </div>

            <div class="col-md-6">
                <label for="dataVencimento" class="form-label">Data de Vencimento</label>
                <input type="date" class="form-control" name="dataVencimento" id="dataVencimento" required />
            </div>

            <div class="col-md-6">
                <label for="rendimento" class="form-label">Rendimento Esperado (R$)</label>
                <input type="number" step="0.01" class="form-control" name="rendimento" id="rendimento" required />
            </div>

            <div class="col-12 text-end">
                <button type="submit" class="btn btn-yellow">Aplicar Investimento</button>
            </div>
        </form>

        <!-- Tabela de investimentos existentes -->
        <h4 class="form-label mt-4">Investimentos Ativos</h4>
        <table class="table table-dark table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tipo</th>
                    <th>Valor Aplicado</th>
                    <th>Rendimento Esperado</th>
                    <th>Data Aplicação</th>
                    <th>Vencimento</th>
                    <th>Conta</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <!-- Exemplo de dados fictícios -->
                <tr>
                    <td>101</td>
                    <td>Tesouro Direto</td>
                    <td>R$ 5.000,00</td>
                    <td>R$ 5.300,00</td>
                    <td>10/06/2025</td>
                    <td>10/06/2026</td>
                    <td>Conta Poupança</td>
                    <td>
                        <form action="CancelarInvestimentoServlet" method="post" class="d-inline">
                            <input type="hidden" name="id" value="101" />
                            <button type="submit" class="btn btn-sm btn-danger">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </button>
                        </form>
                    </td>
                </tr>
                <!-- Mais linhas viriam do backend -->
            </tbody>
        </table>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
