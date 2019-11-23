package ru.lugaonline.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lugaonline.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByIdVk(Integer idVk);

    User getByTel(String tel);

    User save(User user, int id);

    User update(User user, int id);

    boolean deleteByTel(String tel);
}
