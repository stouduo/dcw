package com.stouduo.dcw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean //该注解表示 spring 容器不会创建该对象
public interface BaseRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, JpaRepository<T, ID> {

    /**
     * sql查询
     *
     * @param sql
     * @param args
     * @return
     */
    List<Map<String, String>> findAllByParams(String sql, Object... args);

    /**
     * sql分页查询
     *
     * @param sql
     * @param args
     * @return
     */
    Page<Map<String, String>> findPageByParams(String sql, Pageable pageable, Object... args);


}