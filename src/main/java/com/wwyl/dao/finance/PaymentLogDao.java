package com.wwyl.dao.finance;

import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.PaymentLog;

/**
 * @author jianl
 */
@Repository
public interface PaymentLogDao extends BaseRepository<PaymentLog, Long> {



}
