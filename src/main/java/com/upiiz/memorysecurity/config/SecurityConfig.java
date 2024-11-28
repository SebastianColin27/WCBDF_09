package com.upiiz.memorysecurity.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    //security filter chain
    //Bean -  Singleton si se tiene solo una instancia
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configurar los filtros personalizados
        return httpSecurity.httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http->{
                    http.requestMatchers(HttpMethod.GET,"/api/v2/listar").hasAnyAuthority("READ");
                    http.requestMatchers(HttpMethod.GET,"/api/v2/actualizar").hasAnyAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.GET,"/api/v2/eliminar").hasAnyAuthority("DELETE");
                    http.requestMatchers(HttpMethod.GET,"/api/v2/crear").hasAnyAuthority("CREATE");
                    http.anyRequest().denyAll();

                }).build();
    }

    //autentication manager - lo vamos a obtener de una instancia que ya existe
    @Bean
    public AuthenticationManager authenticationManager() throws  Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //autentication privider -DAO - proporcionar los usuarios
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        return daoAuthenticationProvider;
    }

    //passwordencoder
    public PasswordEncoder passwordEncoder(){
       // return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    //UserDetailService->base de datos o usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService(){
        //definir usuarios en memoria
        //no vamos a obtenerlos de una base de datos
        UserDetails usuarioMiguel= User.withUsername("miguel").password("miguel1234").roles("ADMIN").authorities("READ", "CREATE","UPDATE","DELETE").build();
        UserDetails usuarioRodrigo=User.withUsername("Rodrigo").password("rodrigo1234").roles("USER").authorities("READ","UPDATE").build();
        UserDetails usuarioInvitado=User.withUsername("guest").password("guest").roles("GUEST").authorities("READ").build();
        List<UserDetails> userDetailsList=new ArrayList<>();
        userDetailsList.add(usuarioMiguel);
        userDetailsList.add(usuarioRodrigo);
        userDetailsList.add(usuarioInvitado);

        return new InMemoryUserDetailsManager();
    }
}
