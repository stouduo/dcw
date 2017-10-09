package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, String> {
    @Query(nativeQuery = true, value = "select r.* from role r left join user_role ur on r.id = ur.rid left join user u on u.username = ur.uid where u.username=:username")
    List<Role> findAllRoles(@Param("username") String username);
}
