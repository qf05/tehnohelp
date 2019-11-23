package ru.lugaonline.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lugaonline.model.Invitation;

import java.util.Date;
import java.util.List;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    List<Invitation> findByIdVk(Integer idVk);

    List<Invitation> findByTel(String tel);

    List<Invitation> findByDate(Date date);

    Invitation save(Invitation user, int id);

}
