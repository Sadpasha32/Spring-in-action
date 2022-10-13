package tacos;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tacos.data.IngredientRepository;
import tacos.Ingredient.Type;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }
    @PostMapping
    public String goToDesign(){
        return "redirect:/design";
    }
}
