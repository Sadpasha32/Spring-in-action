package tacos.security;
// Тут описана форма регистрации. В конце создаём новго юзера.
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.MyUser;
@Data
public class RegistrationForm { // Тут мы просто описываем форму, которая будет создаваться при создании нового пользователя
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    public MyUser toUser(PasswordEncoder passwordEncoder) {
        MyUser myUser =  new MyUser(
                username, passwordEncoder.encode(password),
                fullname, street, city, state, zip, phone);
        return myUser;
    }
}
