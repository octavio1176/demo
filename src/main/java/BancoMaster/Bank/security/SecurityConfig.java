package BancoMaster.Bank.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter)
    {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws  Exception
    {
        httpSecurity

                .csrf(AbstractHttpConfigurer::disable)


                 .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                    .authorizeHttpRequests


                        (h-> h.requestMatchers("/API/register", "/API/login"
                                        , "/API/confirmation", "/API/criar", "/API/forgot-password","/API/reset-password")


                                .permitAll().anyRequest().permitAll())


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
