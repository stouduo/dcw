package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormPropertyRepository extends CrudRepository<FormProperty, String> {
    List<FormProperty> findAllByForm(String form);

    @Query("select fp.name from FormProperty fp where fp.form=:form")
    List<String> findByForm(@Param("form") String formId);
}
