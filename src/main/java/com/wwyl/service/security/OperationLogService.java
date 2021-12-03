package com.wwyl.service.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.security.OperationLogDao;
import com.wwyl.entity.security.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by fyunli on 15/1/12.
 */
@Service
public class OperationLogService {

    @Resource
    OperationLogDao operationLogDao;

    public OperationLog save(OperationLog operationLog){
        return operationLogDao.save(operationLog);
    }
    public Page<OperationLog> findOperationLogs(final Long operationId, final Date startTime, final Date endTime, int page, int rows) {
		Specification<OperationLog> specification = new Specification<OperationLog>() {
			@Override
			public Predicate toPredicate(Root<OperationLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path operateTime = root.get("operateTime");
				Path operator = root.get("operator").get("id");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date	startDate=null;
				
				if(startTime==null){
					try {
					  startDate=format.parse("1900-12-12");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					  startDate=startTime;
				}
				Date	endDate=null;
				if(endTime==null){
						endDate=new Date();
				}else{
						endDate=endTime;
				}
				List<Predicate> list = new ArrayList<Predicate>();
				if(startTime!=null||endTime!=null){
					Predicate	p0 = criteriaBuilder.between(operateTime, startDate, endDate);
				    list.add(p0);
				}
				
				if (operationId != null ) {
					Predicate p1 = criteriaBuilder.equal(operator, operationId);
					list.add(p1);
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return operationLogDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
}
