package tacos;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@Data
@Entity // Объявляем данный класс сущностью JPA, то есть JPA создаст нам таблицу Ingredient
@AllArgsConstructor// Добавили конструкторы для всех аргументов
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)// Добавили конструкторы без аргументов
public class Ingredient{ // Здесь не нужен Persistable, так как JPA сама будет определеять обновлять или добавлять определённую запись
    @Id// Говорим, что переменная ниже будет PRIMARY KEY
    private String id;
    private String name;
    private Type type;
    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}