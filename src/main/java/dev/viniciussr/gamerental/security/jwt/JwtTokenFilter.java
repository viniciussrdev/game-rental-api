package dev.viniciussr.gamerental.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import dev.viniciussr.gamerental.exception.jwt.JwtAlgorithmMismatchException;
import dev.viniciussr.gamerental.exception.jwt.JwtSignatureVerificationException;
import dev.viniciussr.gamerental.exception.jwt.JwtTokenExpiredException;
import dev.viniciussr.gamerental.exception.jwt.JwtVerificationException;
import dev.viniciussr.gamerental.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta requisições HTTP para verificar a presença e validade de tokens JWT.
 * <p>
 * Se o token for válido, autentica o usuário no contexto de segurança do Spring.
 * Executado uma única vez por requisição.
 * </p>
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtTokenFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filtra a requisição, validando o token JWT caso presente no header Authorization.
     *
     * @param request            Requisição HTTP.
     * @param response           Resposta HTTP.
     * @param filterChain        Cadeia de filtros.
     * @throws ServletException  Erro durante o processamento do filtro.
     * @throws IOException       Erro de entrada/saída.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtém o valor do header Authorization
        String authHeader = request.getHeader("Authorization");

        // Se o header estiver ausente ou malformado, ignora a autenticação
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrai o token removendo o prefixo "Bearer ".
            String token = authHeader.replace("Bearer ", "");

            jwtService.validateToken(token); // Valida o token JWT

            String email = JWT.decode(token).getSubject(); // Extrai o e-mail (subject) do token

            User user = (User) userDetailsService.loadUserByUsername(email); // Carrega o usuário com base no e-mail

            // Cria o authToken com os dados do usuário e suas permissões
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // Autentica o usuário no contexto do Spring Security
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Em caso de falha na validação, retorna mensagem de erro e interrompe a requisição
            } catch (JWTVerificationException e) {

            switch (e) {

                case TokenExpiredException tokenExpiredException ->
                        throw new JwtTokenExpiredException("Token expirado");

                case SignatureVerificationException signatureVerificationException ->
                        throw new JwtSignatureVerificationException("Assinatura inválida");

                case AlgorithmMismatchException algorithmMismatchException ->
                        throw new JwtAlgorithmMismatchException("Algoritmo inválido");

                default -> throw new JwtVerificationException("Falha na verificação do token");
            }
        }
            // Continua o processamento da requisição
        filterChain.doFilter(request, response);
    }
}