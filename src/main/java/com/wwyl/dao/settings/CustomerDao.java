package com.wwyl.dao.settings;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.CustomerGradeStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Customer;

import javax.persistence.QueryHint;

/**
 * @author liujian
 */
@Repository
public interface CustomerDao extends BaseRepository<Customer, Long> {

    @Override
    @Query("select c from Customer c order by c.custemerId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Customer> findAll();
    
    @Query("select c from Customer c where c.chargeType='2' order by c.custemerId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Customer> findMonthlyAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<Customer> findByNameLike(String name, Pageable pageable);
    
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Customer findByCustemerId(String custemerId);
   
    @Query("select c from Customer c  where  c.sMemberCode=?1  and c.password=?2")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Customer findBysMemberCodeAndPassword(String sMemberCode,String password);
    
    @Query("select c from Customer c where c.cdnr = ?1")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public Customer findCustomerByCdnr(String cdnr);
    
    @Query("select c from Customer c  where  c.custemerId=?1  and c.password=?2")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Customer findByCustemerIdAndPassword(String custemerId,String password);
    
    @Query("select c from Customer c where c.customerOID = ?1 or c.sMemberCode = ?2")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public Customer findCustomerByCustomerOIDOrMemberCode(String customerOID, String sMemberCode);

}
