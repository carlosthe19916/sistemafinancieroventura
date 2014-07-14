package org.ventura.sistemafinanciero.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author adam-bien.com
 */
@WebFilter("/AuthenticationFilter")
public class Authenticator implements Filter{

	private ServletContext context;
	
    @Override   
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();      
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	 
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
         
        String uri = req.getRequestURI();       
         
        HttpSession session = req.getSession(false);
         
        if(session == null && !(uri.endsWith("html") || uri.endsWith("LoginServlet"))){           
            res.sendRedirect("index.caja.html");
        }else{
            // pass the request along the filter chain
            chain.doFilter(request, response);
        }        
    }

    @Override
    public void destroy() {  }
}
