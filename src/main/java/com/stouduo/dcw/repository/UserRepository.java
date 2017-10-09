package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

}
