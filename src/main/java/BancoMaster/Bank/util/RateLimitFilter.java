package BancoMaster.Bank.util;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final BancoMaster.Bank.util.RateLimiterService rateLimiterService;

    public RateLimitFilter(BancoMaster.Bank.util.RateLimiterService   rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();


        if (path.endsWith("/login") && "POST".equalsIgnoreCase(method)) {
            if (!processRateLimit(request, response)) {
                return;
            }
        }


        if (path.endsWith("/forgot-password") && "POST".equalsIgnoreCase(method)) {
            if (!processRateLimit(request, response)) {
                return;
            }
        }



        filterChain.doFilter(request, response);
    }


    private boolean processRateLimit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientIp = extractClientIp(request);
        Bucket bucket = rateLimiterService.resolveBucket(clientIp);

        if (!bucket.tryConsume(1)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format("{\"error\":" + " \"%s\"}", "too much requests for  login, try again later "));
            return false;
        }
        return true;
    }

    private String extractClientIp(@NonNull HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}