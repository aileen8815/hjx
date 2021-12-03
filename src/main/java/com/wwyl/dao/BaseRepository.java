package com.wwyl.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

import javax.persistence.QueryHint;

/**
 * @author fyunli
 */
@NoRepositoryBean
public abstract interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, RevisionRepository<T, ID, Integer> {

    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    T findOne(ID id);

}
