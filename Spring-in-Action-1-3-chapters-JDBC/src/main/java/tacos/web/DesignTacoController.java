package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tacos.Ingredient;
import tacos.Taco;
import tacos.Ingredient.Type; // чтобы сразу использовать Type, а не Ingridient.Type
import tacos.TacoOrder;
import tacos.data.IngredientRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Это логгер, в коде я отправляю ему информацию, и он выводит её в логи.
@Controller // говорим Spring, чтобы он создал bean элемент DesignTacoController, который будет обрабатывать запросы
@RequestMapping("/design") // это означает, что Spring будет обрабатывать данный код при запросах на странице localhost:8080/desing
@SessionAttributes("tacoOrder") // для этой сессии мы сделали атрибут tacoOrder, он понадобится потом, для метода POST, так как созданный тако на этой сессии мы сможем передать
                                // его в другую сессию, скорее всего при использовании redirect!
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;
    @Autowired // явно указывам связывание для нашего созданного репозитория.
    // Создаём именно объект интерфейса, так как в будущем можем просто переписать реализацию на другую.
    // Наш JdbcIngredientRepository имлементриует интерфейс репозитория, так что проблем не будет
    public DesignTacoController(IngredientRepository ingredientRepository){
        this.ingredientRepo = ingredientRepository;
    }
    @ModelAttribute // создаём атрибут, который будет хранить в себе все ингредиенты для тако
    public void addIngredientsMode(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();// Вместо кода ниже, просто получаем все ингредиенты с базы данных
//        List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType((List<Ingredient>) ingredients, type)); // filterByType мы используем, чтобы для определённого атрибута типа
//                                                                                                // сопоставить лист значений этого типа.
//                                                                                                // Например, для типа WRAP будет сопоставлен список Flour Tortilla, Corn Tortilla
//                                                                                                // Это схоже с форматом словаря, у нас есть key WRAP и value List of wraps!
        }

    }

        @PostMapping // Метод, который будет срабатывать при запросе POST
                        // При описании представления desig в начале описания было написано, что формируется запрос POST
                        // То-есть при сборке своего тако мы создаём объект taco в сессии и при нажатии на кнопку subbmite мы отпраляем запрос POST c текущем тако

        public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) { // в параметрах указан атрибут @ModelAttribete чтобы передать наш тако в текущий заказ
            if(errors.hasErrors()){
                return "design";
            }
            tacoOrder.addTaco(taco);
            log.info("Processing taco: {}", taco); // Вот пример использования логгера! Тут просто показывается какой тако был собран и добавлен в заказ
            return "redirect:/orders/current"; // Происходит перенаправление на страницу с текущим заказом
        }
        @ModelAttribute(name = "tacoOrder") // создаём атрибут, который будет находиться только на уровне данной сессии
        public TacoOrder order(){
            return new TacoOrder();
        }
        @ModelAttribute(name = "taco") // тоже создаём атрибут, который будет храниться на уровне этой сессии
        public Taco taco() {
            return new Taco();
        }
        @GetMapping // Тут мы говорим, что данный метод будет работать при запросе Get
        public String showDeginForm(){
            return "design";
        }
        private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
            return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList()); // будет возвращать лист значений только определённого типа!!
            // постарайся по подробнее изучить stream в Java
        }
}
