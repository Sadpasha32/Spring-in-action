// Мы создали этот интерфейс репозитория, чтобы можно было хранить объекты Taco_Order и заодно Taco

package tacos.data;
import java.util.Optional;
import tacos.TacoOrder;
public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}