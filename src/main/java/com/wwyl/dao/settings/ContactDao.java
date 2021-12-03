package com.wwyl.dao.settings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Contact;

import javax.persistence.QueryHint;


/**
 * @author liujian
 */
@Repository
public interface ContactDao extends BaseRepository<Contact, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public Page<Contact> findByLinkmanLike(String name, Pageable pageable);

}
