package myprofile.server.rest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        cors((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private void cors(HttpServletRequest req, HttpServletResponse resp) {

        var origin = req.getHeader("Origin");
        if (origin == null) {
            origin = req.getHeader("origin");
        }

        if (origin != null) {
            if (origin.toLowerCase().endsWith("demo.samueljimenez.co") || "TRUE".equalsIgnoreCase(System.getenv("DEVELOP"))) {
                resp.setHeader("Access-Control-Allow-Origin", origin);
                resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, username");
                resp.setHeader("Access-Control-Request-Method", "POST,GET");
            }
        }
    }
}

