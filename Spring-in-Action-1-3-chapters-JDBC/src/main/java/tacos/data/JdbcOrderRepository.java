package tacos.data;

import jdk.internal.org.objectweb.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public class JdbcOrderRepository implements OrderRepository{ // имплементировали интерфейс репозитория
    private JdbcOperations jdbcOperations;
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        // Шаг первый - создание так называемого шаблона запроса
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Taco_Order"
                                + "(delivery_name, delivery_street, delivery_city, "
                                + "delivery_state, delivery_zip, cc_number, "
                                + "cc_expiration, cc_cvv, placed_at) "
                                + "values (?,?,?,?,?,?,?,?,?)",
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
                ); // сюда мы записали нужный нам SQL запрос для добавления нового заказа
        pscf.setReturnGeneratedKeys(true); // Мы хотим, чтобы поле identity само генерировалось, поэтому ставим true


        order.setPlaceAt(new Date()); // Не забываем про дату создания заказа
        // Тут уже создаём непосредственно заказ
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
                order.getDeliveryName(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryZip(),
                order.getCcNumber(),
                order.getCcExpiration(),
                order.getCcCVV(),
                order.getPlaceAt()
        ));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(); // Это поле нужно, чтобы сохранить сгенерированный ключ заказа

        jdbcOperations.update(psc,keyHolder); // производим update
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId); // задаем id нашему заказу
        List<Taco> tacos = order.getTacos();
        int i = 0;
        for(Taco taco : tacos) {
            saveTaco(orderId,i++,taco); // сохраняем в таблице taco все тако с номером соответствующего заказа
        }
        return order;
    }
    // Метод по сохранению Taco
    private long saveTaco(Long orderId, int orderKey, Taco taco){
        taco.setCreatAt(new Date());
        //Создаём своего рода шаблон для запроса создания нового тако
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco "
                        + "(name, created_at, taco_order, taco_order_key) "
                        + "values (?, ?, ?, ?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        // Опять будем генерировать автоматически ключи
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatAt(),
                        orderId,
                        orderKey
                )); // создаём запрос на добавления
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc,keyHolder); // добавляем Taco в таблицу
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId,taco.getIngredients());// сохраняем все ингредиенты данного тако

        return  tacoId;
    }
    private void saveIngredientRefs(
            long tacoId, List<Ingredient> ingredientRefs) {
        int key = 0;
        for (Ingredient ingredientRef : ingredientRefs) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) "
                            + "values (?, ?, ?)",
                    ingredientRef.getId(), tacoId, key++); // заносим в таблицу связь между Taco и его ингредиентами
        }
    }

}
