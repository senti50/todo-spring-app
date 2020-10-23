package pl.senti.todoapp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggerFilter implements Filter {
    private static final Logger logger= LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(final ServletRequest servletRequest,final ServletResponse servletResponse,final FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest){
            var httpRequest=(HttpServletRequest) servletRequest;
            logger.info("[doFilter]"+httpRequest.getMethod()+" "+httpRequest.getRequestURI());
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


}