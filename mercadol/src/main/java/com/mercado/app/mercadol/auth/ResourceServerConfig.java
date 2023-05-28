package com.mercado.app.mercadol.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
//@EnableResourceServer//configuraciones para el cliente
public class ResourceServerConfig  {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        (authorize) -> authorize

                               // .requestMatchers("/auth/**", "/client/**").permitAll()
                                ///  .requestMatchers(HttpMethod.POST, "/api/v1/cliente/**").permitAll()
                                ///  .requestMatchers(HttpMethod.GET, "/api/v1/uploads/img/{nombreFoto:.+}").permitAll()
                                ///   .requestMatchers("/img/**","/**","/images/**","/static/**", "/", "/home", "/create/**", "/home/page/**", "/du", "/dd", "/dt", "/dc").permitAll()


                                .anyRequest().authenticated()

                )
                .cors().configurationSource(corsConfigurationSource()).and()
              ///  .requestMatchers(HttpMethod.POST, "/api/v1/cliente/**").permitAll()
              ///  .requestMatchers(HttpMethod.GET, "/api/v1/uploads/img/{nombreFoto:.+}").permitAll()

                //  .antMatchers(HttpMethod.GET, "/api/v1/uploads/img/{nombreFoto:.+}").hasAnyRole("USER", "ADMIN")

             ///   .requestMatchers("/img/**","/**","/images/**","/static/**", "/", "/home", "/create/**", "/home/page/**", "/du", "/dd", "/dt", "/dc").permitAll()

                /* .antMatchers(HttpMethod.GET, "/api/v1/products").hasAnyRole("USER", "ADMIN")
                 .antMatchers(HttpMethod.GET, "/api/v1//product/{id}").hasRole("ADMIN")
                 .antMatchers(HttpMethod.GET, "/api/v1/product/p/{nombre}").hasRole("ADMIN")
                 .antMatchers(HttpMethod.POST, "/api/v1/product/c").hasRole("ADMIN")
                 .antMatchers(HttpMethod.PUT, "/api/v1/product/u/{id}").hasRole("ADMIN")
                 .antMatchers(HttpMethod.DELETE, "/api/v1/product/d/{id}").hasRole("ADMIN") */

              ///  .anyRequest().authenticated()

               // .and().cors().configurationSource(corsConfigurationSource())
               .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri))));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roless");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://127.0.0.1:5500" ,"http://20.228.179.23:8080","http://localhost:8080","https://lemon-hill-02bded510.2.azurestaticapps.net"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}
