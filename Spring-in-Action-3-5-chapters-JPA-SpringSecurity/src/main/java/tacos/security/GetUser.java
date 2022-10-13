package tacos.security;

// Тут мы описываем основной метод у UserDetailsService, чтобы искать пользователей в нашей базе

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacos.MyUser;
import tacos.data.UserRepository;
@Service // Говорим, что данный класс будет сервисов, так как имплементируем UserDetailsService
public class GetUser implements UserDetailsService{
    private UserRepository userRepo;
    @Autowired
    GetUser(UserRepository userRepo){
        this.userRepo = userRepo;
    } // подключаем наш репозиторий

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        MyUser user = userRepo.findByUsername(username); // JPA сама реализовала данный метод по ключевым словам
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found"); // Обязательно используем исключений, чтобы не рушить нашу программу
        // при вводе неверных данных
    }
}
