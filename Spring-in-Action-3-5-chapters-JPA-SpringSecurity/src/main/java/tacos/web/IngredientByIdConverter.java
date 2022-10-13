package tacos.web;

// Данный класс нужен, так как мы в представлении desing передаём только id тако, поэтому нам нужно конвертнуть id в целый ингридиент
// UPD. Обновляем конвертор на использование базы данных
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.Ingredient;

import java.util.HashMap;
import java.util.Map;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

@Component // показываем Spring, что нужно использовать этот конвертор
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private IngredientRepository ingredientRepo;
    @Autowired // опять явно указываем связь
    public IngredientByIdConverter(IngredientRepository ingredientRepo){
        this.ingredientRepo = ingredientRepo;
    }
//    private Map<String, Ingredient> ingredientMap = new HashMap<>();
//    public IngredientByIdConverter() {
//        ingredientMap.put("FLTO",
//        new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
//        ingredientMap.put("COTO",
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
//        ingredientMap.put("GRBF",
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
//        ingredientMap.put("CARN",
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN));
//        ingredientMap.put("TMTO",
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
//        ingredientMap.put("LETC",
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES));
//        ingredientMap.put("CHED",
//                new Ingredient("CHED", "Cheddar", Type.CHEESE));
//        ingredientMap.put("JACK",
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
//        ingredientMap.put("SLSA",
//                new Ingredient("SLSA", "Salsa", Type.SAUCE));
//        ingredientMap.put("SRCR",
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
//    }
    @Override
    public Ingredient convert(String id){
        return ingredientRepo.findById(id).orElse(null); // так как у нас findById возвращает Optional, то нам
        // придётся использовать orElse, так можно получить значение содержащееся в Optional или null, если там пусто.
    }
}
