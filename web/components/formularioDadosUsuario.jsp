<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Formulário Usuário</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

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
<div class="container mt-5">
    <div class="form-card">
        <h2>Editar Usuário / Cadastrar Usuário</h2>

        <form action="UsuarioController" method="post" accept-charset="UTF-8">
            <input type="hidden" name="usuarioId">

            <div class="mb-3">
                <label for="nome" class="form-label">Nome <span class="text-danger">*</span></label>
                <input type="text" id="nome" name="nome" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">E-mail <span class="text-danger">*</span></label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="senha" class="form-label">Senha <span class="text-danger">*</span></label>
                <input type="password" id="senha" name="senha" class="form-control" required>
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-save btn-lg">Salvar</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
