// Это интерфейс репозиторий для заказов. В CrudRepository от Spring Data уже описаны методы findAll, finaById, save и т.д
// Также так как это CRUD, то есть CreateReadUpdateDelete, то тут сразу описаны для этого методы.
// Класс CrudRepository является генериком. Первый параметр, это какого типа будут храниться объекты в репозитории,
// а второй параметр, это каким типом будет поле идентификатора.
// В нашем случае TacoOrder идентифицируется большим значением.
package tacos.data;
import org.springframework.data.repository.CrudRepository;
import tacos.TacoOrder;
public interface OrderRepository
        extends CrudRepository<TacoOrder, Long> {
}