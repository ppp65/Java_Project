package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("*")); // 요청을 보내는 프론트엔드의 주소
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true); // 인증 정보를 포함
                    return corsConfiguration;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/index.html", "/rank.html", "/signup.html", "/css/**", "/js/**", "/api/check-auth", "/api/signup").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED) // 필요한 경우 세션 생성
                )
                .formLogin(form ->
                        form
                                .loginPage("/index.html")
                                .loginProcessingUrl("/api/login")
                                .usernameParameter("user_id")
                                .passwordParameter("password")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler())
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/api/logout")
                                .logoutSuccessUrl("/index.html")
                                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                                .deleteCookies("JSESSIONID") // 로그아웃 시 쿠키 삭제
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Spring Security의 SecurityContext에 사용자 정보 저장
            var securityContext = org.springframework.security.core.context.SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // HttpSession에 SecurityContext 저장
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            // 사용자 이름을 세션에 추가
            request.getSession().setAttribute("user", authentication.getName());

            response.setStatus(200);
            response.getWriter().write("{\"message\": \"로그인 성공\"}");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(401);
            response.getWriter().write("{\"message\": \"로그인 실패\"}");
        };
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");
        serializer.setUseHttpOnlyCookie(false);
        serializer.setUseSecureCookie(false);  // 개발 환경에서 HTTP를 사용 중인 경우 false로 설정
        return serializer;
    }
}
