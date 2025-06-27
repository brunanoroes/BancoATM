  <style>
    .navbar-brand {
      font-weight: bold;
      font-size: 1.4rem;
      letter-spacing: 1px;
    }

    .nav-link {
      font-size: 1.1rem;
      transition: 0.3s;
    }

    .nav-link:hover {
      color: #ffc107 !important; /* Amarelo estilo banco */
    }

    .navbar-dark .navbar-nav .nav-link.active {
      color: #ffffff;
      font-weight: bold;
    }

    .navbar-toggler {
      border: none;
    }

    .navbar-toggler:focus {
      box-shadow: none;
    }

    .navbar {
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
    }
  </style>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="/BancoATM/Home.jsp?usuarioId=1">
      <i class="bi bi-bank"></i> MeuBanco
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarNav" aria-controls="navbarNav"
            aria-expanded="false" aria-label="Alternar navegação">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" href="/BancoATM/Transacao.jsp?limite=4"><i class="bi bi-cash-stack"></i> Transa&ccedil;&atilde;o</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/BancoATM/Investimento.jsp"><i class="bi bi-graph-up-arrow"></i> Investimento</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/BancoATM/Extrato.jsp"><i class="bi bi-receipt"></i> Extrato</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/BancoATM/Perfil.jsp"><i class="bi bi-person-circle"></i> Perfil</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-danger" href="/BancoATM/Login.jsp"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

