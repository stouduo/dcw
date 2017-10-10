package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormProperty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormPropertyRepository extends CrudRepository<FormProperty, String> {
    List<FormProperty> findAllByForm(String form);
}
