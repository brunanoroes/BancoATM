<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
  <link rel="stylesheet" href="css/style.css">
    <style>
      body { background-color: #3a3f44 !important; }
  </style>
</head>
<body>
     <%
        String msgSucesso = (String) session.getAttribute("msgSucesso");
        String msgErro = (String) session.getAttribute("msgErro");
        if (msgSucesso != null) {
    %>
        <div class="alert alert-success text-center" role="alert"><%= msgSucesso %></div>
    <%
            session.removeAttribute("msgSucesso");
        }
        if (msgErro != null) {
    %>
        <div class="alert alert-danger text-center" role="alert"><%= msgErro %></div>
    <%
            session.removeAttribute("msgErro");
        }
    %>
  <% if ("sucesso".equals(request.getParameter("msg"))) { %>
    <div class="position-fixed top-0 start-50 translate-middle-x z-3 mt-3">
        <div class="toast align-items-center text-bg-success border-0 show" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    Usuário cadastrado com sucesso!
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Fechar"></button>
            </div>
        </div>
    </div>
    <% } %>
<div class="d-flex justify-content-center align-items-center min-vh-100">
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
</div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
