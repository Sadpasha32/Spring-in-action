package tacos;

import lombok.*;

@Data
public class IngredientRef {
    private final String ingredient;
    private long taco;
    private long taco_key;
}
