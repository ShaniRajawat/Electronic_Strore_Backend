package com.sr.electronic.store.Electronic_Store.config;

import com.sr.electronic.store.Electronic_Store.security.JwtAuthenticationEntryPoint;
import com.sr.electronic.store.Electronic_Store.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SuppressWarnings("ALL")
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS = {

            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/test",
            "/home"

    };

/*  Inmemory Method
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails admin1 = User.builder()
                .username("Shani")
                .password(passwordEncoder().encode("shani"))
                .roles("ADMIN")
                .build();
        UserDetails normal = User.builder()
                .username("Avinash")
                .password(passwordEncoder().encode("avinash"))
                .roles("NORMAL")
                .build();
        //user create
//        InMemoryUserDetailsManager - is implementation of UserDetailService
        return new InMemoryUserDetailsManager(admin1, normal);
    }
*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
/*
        http.authorizeRequests().anyRequest().authenticated().and()
                .formLogin()
                .loginPage("Login.html")
                .loginProcessingUrl("/process-url")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("error")
                .and()
                .logout()
                .logoutUrl("/do-logout");
*/
        http.csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/google").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET).permitAll()
//                        .requestMatchers(HttpMethod.PUT).permitAll()
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex-> ex.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    //CORS configuration
    @Bean
    public FilterRegistrationBean corsFilter(){

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:9090"));
        configuration.addAllowedOriginPattern("**");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**",configuration);


        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        filterRegistrationBean.setOrder(-110);
        return filterRegistrationBean;

    }

}
