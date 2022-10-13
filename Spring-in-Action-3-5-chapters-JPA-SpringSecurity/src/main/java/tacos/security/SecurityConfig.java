package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration // Класс ниже будет конфигурацией
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }// Этим bean компонентом мы определили формат шифрования паролей наших пользователей
    // Теперь, где нужно будет использовать шифровщик паролей мы будем
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/orders").access("hasRole('USER')") // Тут мы говорим, что по данным
                // адресам может находиться юзер с ролью USER
                .antMatchers("/", "/**").access("permitAll()") // По всем остальным адресам открываем доступ всем
                .and()
                .formLogin()
                .loginPage("/login")  // Тут мы задаём страницу для входа
                .defaultSuccessUrl("/design", true) // Здесь мы задаём страницу, которая будет открываться
                //после успешного входа
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")// Это нужно, чтобы можно было спокойно проверять базу данных
                // без проверки CSRF
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .logout()
                .logoutSuccessUrl("/login");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // Тут самая важная часть
        // так как мы задаём какой userDetailsService нужно использовать и какой passwordEncoder. Это нужно, чтобы
        // Spring Security понимал откуда брать пользователей и как их проверять.
    }
}
