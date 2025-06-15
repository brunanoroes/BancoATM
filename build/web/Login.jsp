<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />

  <style>
    body {
        background-color: #f8f9fa;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    .login-card {
        background-color: #212529;
        color: white;
        border-radius: 12px;
        padding: 40px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
        width: 100%;
        max-width: 400px;
    }

    .form-label {
        color: #ffc107;
        font-weight: bold;
    }

    .form-control {
        background-color: #343a40;
        color: white;
        border: 1px solid #495057;
    }

    .form-control:focus {
        border-color: #ffc107;
        box-shadow: 0 0 0 0.25rem rgba(255, 193, 7, 0.25);
    }

    .btn-login {
        background-color: #ffc107;
        color: #212529;
        font-weight: bold;
        border: none;
    }

    .btn-login:hover {
        background-color: #e0a800;
    }

    .register-link {
        color: #ffc107;
        text-decoration: none;
    }

    .register-link:hover {
        text-decoration: underline;
    }

    .text-center {
        text-align: center;
    }
  </style>
</head>
<body>

  <div class="login-card">
    <h2 class="text-center mb-4">Login</h2>

    <form action="LoginServlet" method="post">
      <div class="mb-3">
        <label for="email" class="form-label">E-mail</label>
        <input type="email" class="form-control" id="email" name="email" required />
      </div>

      <div class="mb-3">
        <label for="senha" class="form-label">Senha</label>
        <input type="password" class="form-control" id="senha" name="senha" required />
      </div>

      <div class="d-grid">
        <button type="submit" class="btn btn-login">Entrar</button>
      </div>
    </form>

    <div class="text-center mt-3">
      <p>Não tem uma conta? <a href="Cadastro.jsp" class="register-link">Cadastre-se</a></p>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
