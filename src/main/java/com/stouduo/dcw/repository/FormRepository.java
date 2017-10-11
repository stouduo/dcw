package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.Form;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormRepository extends CrudRepository<Form, String> {
    List<Form> findAllByAuthor(String username);


    @Modifying
    @Query("update Form f set f.viewCount=f.viewCount+1 where f.id=:id")
    void updateViewCount(@Param("id") String formId);

    @Modifying
    @Query("update Form f set f.resultViewCount=f.resultViewCount+1 where f.id=:id")
    void updateResultViewCount(@Param("id") String formId);
}