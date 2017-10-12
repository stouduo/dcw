package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.FormLog;
import com.stouduo.dcw.dto.IFormLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface FormLogRepository extends PagingAndSortingRepository<FormLog, String> {
    @Query("select fl,f.title as formName from FormLog fl,Form f where f.id=fl.form and fl.user=:user")
    Page<IFormLogDTO> findPageByParams(Pageable pageable, @Param("user") String username);
}
