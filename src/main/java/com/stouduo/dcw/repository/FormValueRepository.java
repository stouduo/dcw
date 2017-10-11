package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FormValueRepository extends PagingAndSortingRepository<FormValue, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM formvalue fv WHERE fv.form=:form LIMIT 50")
    List<FormValue> findAllByForm(@Param("form") String form);

    @Query("select count(FormValue.id) from FormValue where form=:form")
    long findFormValueCount(@Param("form") String formId);

    @Query("select count(fv.id) from FormValue fv  where form=:form and to_days(fv.creatTime) = to_days(now())")
    long findTodayFormValueCount(@Param("form") String formId);

    @Query("select fv from FormValue fv where form=:form and fv.value like %?2%")
    Page<FormValue> findByContent(@Param("form") String formId, @Param("value") String content, Sort sort, Pageable page);

    @Query("select fv from FormValue fv where form=:form and fv.value like %?2%")
    List<FormValue> findByContent(@Param("form") String formId, @Param("value") String content, Sort sort);

    @Query("select count(fv.id) from FormValue fv where form=:form")
    long findCountByForm(@Param("form") String formId);
}
