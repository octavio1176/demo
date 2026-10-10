package BancoMaster.Bank.security;
import BancoMaster.Bank.util.RateLimitFilter;
import BancoMaster.Bank.util.RateLimiterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, RateLimitFilter rateLimitFilter)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws  Exception
    {
        httpSecurity

                .csrf(AbstractHttpConfigurer::disable)

                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)



                 .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                    .authorizeHttpRequests


                        (h-> h.requestMatchers("/api/register", "/api/login"
                                        , "/api/confirmation","/api/forgot-password","/api/reset-password")

                                .permitAll().anyRequest().authenticated())


                .headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))


                .addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

}
