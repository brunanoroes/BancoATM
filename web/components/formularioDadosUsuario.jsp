<%@ page import="java.sql.*, java.security.MessageDigest" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");

    boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
    boolean isEdicao = request.getParameter("usuarioId") != null && !request.getParameter("usuarioId").isEmpty();
    String usuarioId = request.getParameter("usuarioId");

    String nome = "", email = "", tipo = "NORMAL", erro = "", mensagem = "";

    if (isPost) {
        // Processamento de cadastro/edição
        nome = request.getParameter("nome");
        email = request.getParameter("email");
        tipo = request.getParameter("tipoUsuario");
        String senha = request.getParameter("senha");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "Senha1@");

            if (isEdicao) {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE USUARIO SET NOME=?, EMAIL=?, TIPO_USUARIO=? WHERE ID=?"
                );
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, tipo);
                stmt.setInt(4, Integer.parseInt(usuarioId));
                stmt.executeUpdate();
                mensagem = "Usuário atualizado com sucesso!";
            } else {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(senha.getBytes("UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) sb.append(String.format("%02x", b));
                String senhaHash = sb.toString();

                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, TIPO_USUARIO) VALUES (?, ?, ?, ?)"
                );
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, senhaHash);
                stmt.setString(4, tipo);
                stmt.executeUpdate();
                mensagem = "Usuário cadastrado com sucesso!";
            }

            conn.close();
        } catch (Exception e) {
            erro = e.getMessage();
        }
    } else if (isEdicao) {
        // Carregamento de dados para edição
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "Senha1@");

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USUARIO WHERE ID = ?");
            stmt.setInt(1, Integer.parseInt(usuarioId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nome = rs.getString("NOME");
                email = rs.getString("EMAIL");
                tipo = rs.getString("TIPO_USUARIO");
            }

            conn.close();
        } catch (Exception e) {
            erro = e.getMessage();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdicao ? "Editar Usuário" : "Cadastrar Usuário" %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <h2><%= isEdicao ? "Editar Usuário" : "Cadastrar Novo Usuário" %></h2>

    <% if (!erro.isEmpty()) { %>
        <div class="alert alert-danger"><%= erro %></div>
    <% } else if (!mensagem.isEmpty()) { %>
        <div class="alert alert-success"><%= mensagem %></div>
    <% } %>

    <form method="post">
        <% if (isEdicao) { %>
            <input type="hidden" name="usuarioId" value="<%= usuarioId %>">
        <% } %>

        <div class="mb-3">
            <label for="nome" class="form-label">Nome:</label>
            <input type="text" name="nome" id="nome" class="form-control" value="<%= nome %>" required>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">E-mail:</label>
            <input type="email" name="email" id="email" class="form-control" value="<%= email %>" required>
        </div>

        <% if (!isEdicao) { %>
            <div class="mb-3">
                <label for="senha" class="form-label">Senha:</label>
                <input type="password" name="senha" id="senha" class="form-control" required>
            </div>
        <% } %>

        <div class="mb-3">
            <label for="tipoUsuario" class="form-label">Tipo de Usuário:</label>
            <select name="tipoUsuario" id="tipoUsuario" class="form-select" required>
                <option value="NORMAL" <%= "NORMAL".equals(tipo) ? "selected" : "" %>>Normal</option>
                <option value="ADMIN" <%= "ADMIN".equals(tipo) ? "selected" : "" %>>Admin</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">
            <%= isEdicao ? "Salvar Alterações" : "Cadastrar" %>
        </button>
    </form>
</body>
</html>
