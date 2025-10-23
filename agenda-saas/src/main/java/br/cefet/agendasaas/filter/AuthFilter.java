package br.cefet.agendasaas.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

	private volatile boolean initialized = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (filterConfig != null) {
			filterConfig.getServletContext();
		}
		this.initialized = true;
	}

	private boolean isPublicPath(HttpServletRequest req) {
		String path = req.getRequestURI().substring(req.getContextPath().length());
		if (path == null || path.isEmpty() || path.equals("/")) return true;
		String p = path.toLowerCase();
		return p.startsWith("/auth/")
				|| p.equals("/index.jsp")
				|| p.equals("/style.css")
				|| p.startsWith("/static/")
				|| p.startsWith("/assets/")
				|| p.startsWith("/webjars/")
				|| p.equals("/cliente/dashboardcliente_preview.html");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!this.initialized || !(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (isPublicPath(req)) {
			chain.doFilter(request, response);
			return;
		}

		Object usuario = req.getSession().getAttribute("usuarioLogado");
		if (usuario == null) {
			resp.sendRedirect(req.getContextPath() + "/auth/login.jsp");
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		this.initialized = false;
	}
}

