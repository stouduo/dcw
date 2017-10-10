package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.Form;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormRepository extends CrudRepository<Form, String> {
    List<Form> findAllByAuthor(String username);

}