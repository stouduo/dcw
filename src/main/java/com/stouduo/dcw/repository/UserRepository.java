package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

    List<User> findByUsernameOrTelOrEmail(String username, String tel, String email);

    @Query("select u from User u where u.tel=:username or u.email=:username or u.username=:username")
    User findByTelOrEmail(@Param("username") String username);

    User findByUsername(String username);
}
