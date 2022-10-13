package tacos.security;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

@Controller
@RequestMapping("/register") // Будем работать с этим контроллером по адресу /register
public class RegistrationController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public RegistrationController( // Подключаем наш репозиторий и passwordEncoder.
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping
    public String registerForm() {
        return "registration";
    } // Обрабатываем GET запрос по адресу /register

    @PostMapping// Форма registration будет создовать POST запрос на создание нового пользователя
    public String processRegistration(RegistrationForm form) {
        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/login"; // Перенапрявляем пользователя на страницу входа
    }
}
