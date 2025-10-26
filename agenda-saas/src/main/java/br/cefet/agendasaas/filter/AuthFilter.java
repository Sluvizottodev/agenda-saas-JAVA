package br.cefet.agendasaas.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class AuthFilter implements Filter {
	private volatile boolean initialized = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing special at init for now, but mark initialized
		this.initialized = true;
	}

	private boolean isPublicPath(HttpServletRequest req) {
		String path = req.getRequestURI();
		String ctx = req.getContextPath();
		if (ctx != null && !ctx.isEmpty() && path.startsWith(ctx)) {
			path = path.substring(ctx.length());
		}
		if (path == null || path.isEmpty() || "/".equals(path))
			return true;
		String p = path.toLowerCase();

		// Public application endpoints and static resources
		if (p.startsWith("/auth/"))
			return true; // login, cadastro, logout pages
		if (p.equals("/index.jsp") || p.equals("/index") || p.equals("/"))
			return true;
		if (p.equals("/style.css") || p.endsWith(".css") || p.endsWith(".js")
				|| p.endsWith(".png") || p.endsWith(".jpg") || p.endsWith(".jpeg")
				|| p.endsWith(".svg") || p.endsWith(".ico") || p.endsWith(".woff")
				|| p.endsWith(".woff2") || p.endsWith(".ttf"))
			return true;

		// public pages
		if (p.equals("/agendamento-sucesso.jsp") || p.equals("/agendar.jsp"))
			return true;

		return false;
	}

	private boolean isAjaxRequest(HttpServletRequest req) {
		String xrw = req.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equalsIgnoreCase(xrw))
			return true;
		String accept = req.getHeader("Accept");
		return accept != null && accept.contains("application/json");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!this.initialized || !(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// Ensure UTF-8 encoding for incoming requests
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (Exception ignored) {
		}

		if (isPublicPath(req)) {
			chain.doFilter(request, response);
			return;
		}

		Object usuario = req.getSession(false) != null ? req.getSession(false).getAttribute("usuarioLogado") : null;
		if (usuario == null) {
			if (isAjaxRequest(req)) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json;charset=UTF-8");
				resp.getWriter().write("{\"error\": \"login_required\"}");
				return;
			} else {
				// redirect to login with original path as next parameter
				String original = req.getRequestURI();
				String qs = req.getQueryString();
				if (qs != null && !qs.isEmpty())
					original += "?" + qs;
				String target = req.getContextPath() + "/auth/login.jsp?next="
						+ java.net.URLEncoder.encode(original, "UTF-8");
				resp.sendRedirect(target);
				return;
			}
		}

		// user present, continue
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		this.initialized = false;
	}
}
