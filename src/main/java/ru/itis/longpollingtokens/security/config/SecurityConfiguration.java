package ru.itis.longpollingtokens.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.itis.longpollingtokens.security.filters.TokenAuthenticationFilter;
import ru.itis.longpollingtokens.security.providers.TokenAuthenticationProvider;

@Configuration // говорим, что у нас конфигурационный класс
@EnableGlobalMethodSecurity(prePostEnabled = true) // включаем проверку безопасности через аннотации
@EnableWebSecurity // включаем безопасность
@ComponentScan("ru.itis") // говорим, чтобы искал все компоненты в наших пакетах
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // подключем провайдер, который мы написали
    @Autowired
    private TokenAuthenticationProvider provider;

    // конфигурируем AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // прикручиваем наш провайдер
        auth.authenticationProvider(provider);
    }
    // конфигурирем саму безопасность
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // отключаем csrf
        http.csrf().disable();
        // отключаем сессии
        http.sessionManagement().disable();

        // добавляем наш фильтр
        http.addFilterBefore(new TokenAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.cors().disable().authorizeRequests()
                .antMatchers("/login-token", "/login-cred").permitAll()
                .antMatchers("/chat").permitAll();
    }

}
