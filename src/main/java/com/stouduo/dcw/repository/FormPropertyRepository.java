package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormPropertyRepository extends CrudRepository<FormProperty, String> {
    @Query("select fp from FormProperty fp where fp.form=:form order by fp.index asc")
    List<FormProperty> findAllByForm(@Param("form") String form);

    List<FormProperty> findByForm(@Param("form") String formId);
}
