package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormRepository extends PagingAndSortingRepository<Form, String> {
    Page<Form> findAllByAuthorAndDel(String username, boolean del, Pageable pageable);


    @Modifying
    @Query("update Form f set f.viewCount=f.viewCount+1 where f.id=:id")
    void updateViewCount(@Param("id") String formId);

    @Modifying
    @Query("update Form f set f.resultViewCount=f.resultViewCount+1 where f.id=:id")
    void updateResultViewCount(@Param("id") String formId);

    @Query("select count(f) from Form f where author=:author and del=false and labels is null")
    long findByLabelIsNull(@Param("author") String username);

    @Query("select count(f) from Form f where author=:author and del=false")
    long findFormCountByAuthor(@Param("author") String username);

    @Modifying
    @Query("update Form f set f.del=true where f.id=:id")
    void delForm(@Param("id") String id);
}