package com.arimac.backend.pmtool.projectmanagementtool.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String ORGANIZATION_ADMIN = "ORGANIZATION_ADMIN";
    private static final String WORKLOAD  = "WORKLOAD";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                //User Endpoints
                .antMatchers(HttpMethod.GET, "/users").hasRole(USER)
                .antMatchers(HttpMethod.POST, "/users").hasAnyRole(ADMIN, SUPER_ADMIN, ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.PUT, "/users/{userId}").hasRole(USER)
                .antMatchers(HttpMethod.GET, "/users/{userId}").hasRole(USER)
                .antMatchers( "/users/{userId}/slack/**").hasRole(USER)
                .antMatchers(HttpMethod.GET, "/users/project/{projectId}/**").hasRole(USER)
                .antMatchers(HttpMethod.POST, "/projects").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/projects/{projectId}").hasAnyRole(SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.PUT, "/projects/{projectId}").hasAnyRole(SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.PUT, "/projects/{projectId}/weight").hasAnyRole(SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.POST, "/projects/{projectId}/users/{userId}/block").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                //workload
                .antMatchers(HttpMethod.GET, "/projects/tasks/users/workload").hasAnyRole(WORKLOAD,ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.GET, "/projects/tasks/users/{userId}/workload").hasRole(USER)
                //projects
                .antMatchers(HttpMethod.GET, "/projects/all").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN,WORKLOAD)
                //filter
                .antMatchers(HttpMethod.GET, "/projects/workload/filter").hasAnyRole(WORKLOAD,ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                //template
                .antMatchers("/template/**").hasAnyRole(WORKLOAD,ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                //Admin
                .antMatchers("/users/activate").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers("/users/deactivate").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers("/admin/user/roles").hasAnyRole(USER,ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers("/admin/**").hasAnyRole(USER,ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                //Skill Matrix  CHeck LAter
                .antMatchers(HttpMethod.GET, "/category").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers(HttpMethod.POST, "/category").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                .antMatchers("/category").hasAnyRole(ADMIN,SUPER_ADMIN,ORGANIZATION_ADMIN)
                //Analytics Endpoint
                .antMatchers("/analytics/**").hasAnyRole(ORGANIZATION_ADMIN,SUPER_ADMIN,ADMIN,WORKLOAD)
                .anyRequest()
                //.authenticated()
                .permitAll();
                 http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}