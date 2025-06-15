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

       <%@ include file="/components/contas.jsp" %>

        <h4 class="form-label mt-4">Últimas Movimentações</h4>
        <%@ include file="/components/movimentacoes.jsp" %>
        <a href="/BancoATM/Extrato.jsp?usuarioId=1">Ver Mais</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
