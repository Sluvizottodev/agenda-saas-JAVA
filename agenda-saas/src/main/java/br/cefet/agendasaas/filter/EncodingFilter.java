package br.cefet.agendasaas.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Forces UTF-8 on every request/response to keep accented characters intact.
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    private static final String UTF_8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!UTF_8.equalsIgnoreCase(request.getCharacterEncoding())) {
            request.setCharacterEncoding(UTF_8);
        }
        if (!UTF_8.equalsIgnoreCase(response.getCharacterEncoding())) {
            response.setCharacterEncoding(UTF_8);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
