package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.MyUser;

public interface UserRepository extends CrudRepository<MyUser,Long> {
    MyUser findByUsername(String username); // Spring Data JPA сама реализует этот метод, так как может анализировать название
}
