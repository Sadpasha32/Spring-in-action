package tacos;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
@Data
@Entity// Объявляем данный класс сущностью JPA, то есть JPA создаст нам таблицу Ingredient
public class Taco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// Тут мы хотим, чтобы id генерировался самостоятельно
    private Long id;
    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;
    private Date createdAt = new Date();
    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany()// Тут связь Many to Many, так как у нас одни и теже ингредиенты могут быть в разных Taco
    private List<Ingredient> ingredients = new ArrayList<>();
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}