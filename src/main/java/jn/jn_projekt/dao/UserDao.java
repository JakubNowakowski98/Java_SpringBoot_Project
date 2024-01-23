package jn.jn_projekt.dao;

import jakarta.transaction.Transactional;
import jn.jn_projekt.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {

    public User findByLogin(String login);
    
    @Transactional
    void deleteByLogin(String login);
}
