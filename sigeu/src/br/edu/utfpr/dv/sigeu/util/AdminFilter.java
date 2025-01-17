package br.edu.utfpr.dv.sigeu.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.service.UriPermissaoService;

public class AdminFilter implements Filter {
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {

	    boolean loginOk = false;

	    HttpServletRequest requestHttp = ((HttpServletRequest) request);

	    String uri = requestHttp.getRequestURI();
	    Pessoa pessoaLogin = (Pessoa) requestHttp.getSession().getAttribute(LoginFilter.SESSION_PESSOA_LOGIN);

	    if (isPublicPage(uri)) {
		loginOk = true;
	    } else {
		if (pessoaLogin != null) {
		    loginOk = UriPermissaoService.verificaPermissaoDeAcesso(pessoaLogin, uri);
		} else {
		    loginOk = true;
		}
	    }

	    if (!loginOk) {
		// Manda para página de erro
		String contextPath = requestHttp.getContextPath();
		((HttpServletResponse) response).sendRedirect(contextPath + "/Oops.xhtml");
	    } else {
		// Continua na página solicitada
		chain.doFilter(request, response);
	    }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	/**
	 * Verifica se a página é pública (não requer autenticação)
	 *
	 * @param uri
	 * @return
	 */
	private boolean isPublicPage(String uri) {
	    uri = uri.trim().toLowerCase();
	    return uri.equals("/sigeu/logoff") || uri.equals("/sigeu/login") || uri.equals("/sigeu/oops.xhtml")
		    || uri.startsWith("/sigeu/javax");
	}
}