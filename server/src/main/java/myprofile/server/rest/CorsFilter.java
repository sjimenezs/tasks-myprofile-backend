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
    private static final String HEADER_NAME_ACCES_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        cors((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void cors(HttpServletRequest req, HttpServletResponse resp) {

        var origin = req.getHeader("Origin");
        if (origin == null) {
            origin = req.getHeader("origin");
        }
        if (origin != null && (origin.toLowerCase().endsWith("demo.samueljimenez.co") || "TRUE".equalsIgnoreCase(System.getenv("DEVELOP")))) {
            resp.setHeader(HEADER_NAME_ACCES_CONTROL_ALLOW_ORIGIN, "https://demo.samueljimenez.co");

            if ("TRUE".equalsIgnoreCase(System.getenv("DEVELOP"))) {
                resp.setHeader(HEADER_NAME_ACCES_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");
            }

            resp.addHeader(HEADER_NAME_ACCES_CONTROL_ALLOW_ORIGIN, origin);
            resp.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, username");
            resp.addHeader("Access-Control-Request-Method", "POST,GET");
        }
    }
}

