package ru.lugaonline.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lugaonline.model.Pay;

import java.util.Date;
import java.util.List;

public interface PayRepository extends CrudRepository<Pay, Long> {

    List<Pay> findByTel(String tel);

    List<Pay> findByDate(Date date);

    Pay save(Pay pay, int id);
}
