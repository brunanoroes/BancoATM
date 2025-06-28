<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Models.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("Logout"); // Redireciona caso não esteja logado
        return;
    }
%>
<!DOCTYPE html>
<html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Página Inicial</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <style>
        body { background-color: #3a3f44 !important; }
    </style>
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <% if ("sucesso".equals(request.getParameter("msg"))) { %>
    <div class="position-fixed top-0 start-50 translate-middle-x z-3 mt-3">
        <div class="toast align-items-center text-bg-success border-0 show" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    Perfil atualizado com sucesso!
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Fechar"></button>
            </div>
        </div>
    </div>
<% } %>
      <div class="container mt-5">
          <div class="form-card">
              <h2>Editar Usuário</h2>

              <form action="PerfilServlet" method="post">
                  <input type="hidden" name="id" value="<%= usuario.getId() %>">

                  <div class="mb-3">
                      <label for="nome" class="form-label">Nome <span class="text-danger">*</span></label>
                      <input type="text" id="nome" name="nome" class="form-control" required value="<%= usuario.getNome() %>">
                  </div>

                  <div class="mb-3">
                      <label for="email" class="form-label">E-mail <span class="text-danger">*</span></label>
                      <input type="email" id="email" name="email" class="form-control" required value="<%= usuario.getEmail() %>">
                  </div>

                  <div class="mb-3">
                      <label for="senha" class="form-label">Senha <span class="text-danger">*</span></label>
                      <input type="password" id="senha" name="senha" class="form-control" required placeholder="Digite a nova senha">
                  </div>

                  <div class="d-grid">
                      <button type="submit" class="btn btn-save btn-lg">Salvar Alterações</button>
                  </div>
              </form>
          </div>
      </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
