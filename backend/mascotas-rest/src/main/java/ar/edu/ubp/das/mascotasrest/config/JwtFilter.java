package ar.edu.ubp.das.mascotasrest.config;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/sesiones") || path.startsWith("/refugios") || path.startsWith("/veterinarias");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            rechazar(response);
            return;
        }
        try {
            Claims claims = jwtUtil.leerToken(authorization.substring(7));
            UsuarioBE usuario = new UsuarioBE();
            usuario.setCuil(claims.getSubject());
            usuario.setIdRefugio(claims.get("idRefugio", Integer.class));
            request.setAttribute("usuario", usuario);
        }
        catch (JwtException e) {
            rechazar(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void rechazar(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("La sesión no es válida o expiró. Vuelva a iniciar sesión.");
    }

}
