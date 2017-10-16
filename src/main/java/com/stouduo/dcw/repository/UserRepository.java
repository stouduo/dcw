package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

    @Query("select u from User u where u.tel=:username or u.email=:username")
    User findByTelOrEmail(@Param("username") String username);
}
