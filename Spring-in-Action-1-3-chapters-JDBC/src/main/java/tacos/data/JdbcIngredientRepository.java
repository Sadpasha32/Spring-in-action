
// Тут мы описываем реализацию интерфейса репозитория ингредиентов

package tacos.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository // Эта аннотация схожа с аннотацией @Controller. Большую смысловую нагрузку в себе не несёт,
// просто spring сам будет создавать объект этого класса, то есть создаст bean компонент
public class JdbcIngredientRepository implements IngredientRepository {
    private JdbcTemplate jdbcTemplate;
    @Autowired // тут мы явно указываем, что нужно произвести связывание с JdbcTemplate
    // В принципе, если есть только один конструктор, то spring сам свяжет его, но бывают случаи, когда это нужно указать явно
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }// тут мы как бы передаём ссылку на наш jdbctemplate, чтобы в этом репозитории работать именно с нашей базой данных

    @Override
    public List<Ingredient> findAll() {
        return jdbcTemplate.query("select id,name,type from Ingredient",
        this::mapRowToIngredient);// передаётся RowMapper тут по ссылке, но можно его реалтзовать прям в скобках, также можно использовать лямду
        //query принимает SQL и реализацию RowMapper, то есть как обработать строку в объект
    }

    @Override
    public Optional<Ingredient> findById(String id) { // Немного об Optional. Он нужен, чтобы проще обрабатывать Null значения.
        List<Ingredient> results = jdbcTemplate.query(
                "select id,name,type from Ingredient where id=?",
                this::mapRowToIngredient,id); // тут query принимает не только два аргумента, так как в запросе
        // у нас есть ?, то есть вместо ? поставиться передаваемый параметр id.
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));// если наш запрос не выдаст результат, то
        // Optional.empty() выдаст пустой объект, а вот Optional.of(results.get(0)) создаст объест из результата
    }
    private  Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
            return new Ingredient(row.getString("id"),row.getString("name"),Ingredient.Type.valueOf(row.getString("type")));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update("insert into Ingredient (id,name,type) values(?,?,?)", // .update используют для запросов изменения или добавления
                ingredient.getId(),ingredient.getName(),ingredient.getType().toString()); // тут просто происходит добавление в таблицу Ingredient
        return ingredient;
    }
}
