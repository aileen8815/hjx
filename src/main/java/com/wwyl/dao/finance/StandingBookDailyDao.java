package com.wwyl.dao.finance;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.StandingBookDaily;

/**
 * @author hehao
 */
@Repository
public interface StandingBookDailyDao extends BaseRepository<StandingBookDaily, Long> {
    @Query("select s from StandingBookDaily s  where s.inboundDateTime = ?1 and s.customer.id = ?2")
    StandingBookDaily findByDateTimeAndCustomerId(Date dateTime, Long customerId);

    @Query("select s from StandingBookDaily s " +
            "where s.inboundDateTime >= ?1 and s.inboundDateTime <= ?2 and s.customer.id = ?3 " +
            "order by s.inboundDateTime")
    List<StandingBookDaily> findByDateRangeAndCustomerId(Date startDate, Date endDate, Long customerId);

}
