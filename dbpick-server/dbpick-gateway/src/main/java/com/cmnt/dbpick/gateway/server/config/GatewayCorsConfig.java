package com.cmnt.dbpick.gateway.server.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lanhaibei on 18/4/3.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayCorsConfig implements Filter {

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        if (req instanceof HttpServletRequest) {
            response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
        } else {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.addHeader("Cache-Control", CacheControl.noStore().getHeaderValue());
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
