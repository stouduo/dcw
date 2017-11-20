package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FormValueRepository extends PagingAndSortingRepository<FormValue, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM formvalue fv WHERE fv.form=:form AND del=FALSE order by fv.createTime desc LIMIT 30")
    List<FormValue> findRecentByForm(@Param("form") String form);

    @Query("select count(id) from FormValue where form=:form and del=false")
    long findFormValueCount(@Param("form") String formId);

    @Query("select count(id) from FormValue fv  where form=:form and del=false and to_days(fv.createTime) = to_days(now())")
    long findTodayFormValueCount(@Param("form") String formId);

    //  @Query("select fv from FormValue fv where form=:form and fv.value like CONCAT('%',:content,'%')")
    @Query("select fv from FormValue fv where form=:form and del=false and (fv.value like %:content% or fv.author like %:content% or fv.lastModifyPerson like %:content%) and fv.createTime >= :startTime and fv.createTime<=:endTime")
    Page<FormValue> findByContent(@Param("form") String formId, @Param("content") String content, @Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable page);

    //@Query("select fv from FormValue fv where form=:form and fv.value like CONCAT('%',:content,'%')")
    @Query("select fv from FormValue fv where form=:form and del=false and (fv.value like %:content% or fv.author like %:content% or fv.lastModifyPerson like %:content%) and fv.createTime >= :startTime and fv.createTime<=:endTime")
    List<FormValue> findByContent(@Param("form") String formId, @Param("content") String content, @Param("startTime") Date startTime, @Param("endTime") Date endTime, Sort sort);

    @Query("select count(id) from FormValue fv where form=:form and del=false")
    long findCountByForm(@Param("form") String formId);

    @Query("select count(id) from FormValue fv where fv.author=:author or fv.submitIP=:submitIP and del=false")
    long findSubmitCount(@Param("author") String username, @Param("submitIP") String ipAddress);

    @Modifying
    @Query("update FormValue f set f.del=true where f.form=:form")
    void delFormValues(@Param("form") String id);

    @Query("select fv from FormValue fv where fv.form=:form and fv.del=false")
    Page<FormValue> findAllByForm(@Param("form") String formId, Pageable page);

    @Query("select count(id) from FormValue fv where fv.submitIP=:submitIP and del=false")
    long findSubmitCount(@Param("submitIP") String ipAddress);
}
