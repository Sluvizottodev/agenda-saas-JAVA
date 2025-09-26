package br.cefet.agendaSaas.controller;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = usuarioDAO.buscarPorEmailSenha(email, senha);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);

            // Redireciona conforme o tipo
            switch (usuario.getTipo()) {
                case CLIENTE -> response.sendRedirect(request.getContextPath() + "/cliente/dashboard.jsp");
                case PRESTADOR -> response.sendRedirect(request.getContextPath() + "/prestador/dashboard.jsp");
                case ADMIN -> response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            }
        } else {
            request.setAttribute("erro", "E-mail ou senha inválidos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
