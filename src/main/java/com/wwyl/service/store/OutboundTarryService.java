package com.wwyl.service.store;

 
import java.util.ArrayList;
 
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
 
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 



import com.wwyl.Enums.StockOutStatus;
import com.wwyl.dao.store.OutboundTarryDao;
 
import com.wwyl.entity.store.OutboundTarry;
 
 
 

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class OutboundTarryService {

	@Resource
	private OutboundTarryDao  outboundTarryDao;
	public List<OutboundTarry> findAll() {
		return outboundTarryDao.findAll();
	}

	public OutboundTarry findOne(Long id) {
		return outboundTarryDao.findOne(id);
	}
 
	 
	public List<OutboundTarry> findOutboundTarryByConditions(final String handsetAddress,final StockOutStatus[] stockOutstatus) {
		Specification<OutboundTarry> specification = new Specification<OutboundTarry>() {
			@Override
			public Predicate toPredicate(Root<OutboundTarry> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path handsetAddressPath = root.get("handsetAddress");
				Path stockOutStatusPath = root.get("outboundRegister").get("stockOutStatus");
				Path registerTimePath = root.get("outboundRegister").get("registerTime");
				
				if (StringUtils.isNotBlank(handsetAddress)) {
					Predicate p1 = criteriaBuilder.equal(handsetAddressPath, handsetAddress);
					list.add(p1);
				}
				 
				if (stockOutstatus.length>0) {
					Predicate p2 = stockOutStatusPath.in((Object[]) stockOutstatus);
					list.add(p2);
				}
				 
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(registerTimePath)));
				return criteriaBuilder.and(list.toArray(p));
			}
		};
		return outboundTarryDao.findAll(specification);
	}

	public Map  findByhandsetTask(){
			List	list=outboundTarryDao.findHandsetTask();
			Map map=new LinkedHashMap();
			for (Object object : list) {
				Object[]  arr=(Object[]) object;
				map.put(arr[0], arr[1]);
			}
			return map;
	}
	
	
}
