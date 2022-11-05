package com.learn.tacocloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class SecurityConfig {


    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design/**", "/orders/**","/login")
                .hasAnyRole("USER","ADMIN")
                .antMatchers("/h2-console/**")
                .hasRole("ADMIN")
//                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
//                .loginPage("/login")
                .defaultSuccessUrl("/design");
//                .loginProcessingUrl("/authenticate")
//                .usernameParameter("user")
//                .passwordParameter("pwd");
        return http.build();
    }

    /**
     * 用户详情
     */
    @Bean
    public UserDetailsService users() {
        // 创建用户编辑器
        User.UserBuilder users = User.builder();
        users.passwordEncoder(this.passwordEncoder()::encode);

        // 创建buzz角色用户
        UserDetails user = users
                .username("buzz")
                .password("infinity")
                .roles("USER")
                .build();
        // 创建woody角色用户
        UserDetails admin = users
                .username("woody")
                .password("bullseye")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

//    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

//    @Bean
    UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.builder()
                .passwordEncoder(this.passwordEncoder()::encode)
                .username("buzz")
                .password("infinity")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .passwordEncoder(this.passwordEncoder()::encode)
                .username("woody")
                .password("bullseye")
                .roles("USER", "ADMIN")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(admin);
        return users;
    }


    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
