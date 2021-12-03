package com.wwyl.service.settings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.StoreContainerStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreContainerDao;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreContainerType;

/**
 * @author yche
 */
@Service
@Transactional(readOnly = true)
public class StoreContainerService {
	@Resource
	private StoreContainerDao storeContainerDao;
	@Resource
	private StoreContainerTypeService storeContainerTypeService;
	@Resource
	private SerialNumberService serialNumberService;

	public List<StoreContainer> findAll() {
		return storeContainerDao.findAll();
	}
	
	public List<StoreContainer> findUnusedAll(String label) {
		return storeContainerDao.findUnusedAll(label);
	}
	public StoreContainer findOne(Long id) {
		return storeContainerDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StoreContainer storeContainer) {
		if (StringUtils.isBlank(storeContainer.getLabel())) {
			storeContainer.setLabel(null);
		}
		storeContainerDao.save(storeContainer);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		storeContainerDao.delete(id);
	}

	public Page<StoreContainer> findStoreContainersByLabelLike(int page, int rows, String label) {
		if (StringUtils.isBlank(label)) {
			return storeContainerDao.findAllOrderByLabel(RepoUtils.buildPageRequest(page, rows));
		}
		return storeContainerDao.findByLabelLike("%" + label + "%", RepoUtils.buildPageRequest(page, rows));
	}
	public Page<StoreContainer> findStoreContainerByConditions(int page,
			int rows,final String label,final StoreContainerStatus storeContainerStatus,final Long storeContainerTypeId) {
		Specification<StoreContainer> specification = new Specification<StoreContainer>() {
			@Override
			public Predicate toPredicate(Root<StoreContainer> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path labelPath=	root.get("label");
				Path storeContainerTypePath=	root.get("storeContainerType").get("id");
				Path storeContainerStatusPath=	root.get("storeContainerStatus");
				
				if(StringUtils.isNotBlank(label)){
					Predicate p1 = criteriaBuilder.like(labelPath, "%" + label + "%");
					list.add(p1);
				}
				
				if (storeContainerTypeId != null) {
					Predicate p1 = criteriaBuilder.equal(storeContainerTypePath, storeContainerTypeId);
					list.add(p1);
				}
				if (storeContainerStatus != null) {
					Predicate p1 = criteriaBuilder.equal(storeContainerStatusPath, storeContainerStatus);
					list.add(p1);
				}
				 
				Predicate[] p = new Predicate[list.size()];
				 
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return storeContainerDao.findAll(specification,
				RepoUtils.buildPageRequest(page, rows));
	}
	public StoreContainer findStoreContainerByLabel(final String label) {
		Specification<StoreContainer> specification = new Specification<StoreContainer>() {
			@Override
			public Predicate toPredicate(Root<StoreContainer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("label").as(String.class), label);
			}
		};
		return storeContainerDao.findOne(specification);
	}

	@Transactional(readOnly = false)
	public void batchCreate(Long storeContainerTypeId, int amount, int length, int width, int height, int weight) {
		StoreContainerType storeContainerType = storeContainerTypeService.findOne(storeContainerTypeId);
		List<StoreContainer> storeContainers = new ArrayList<StoreContainer>();
		for (int i = 0; i < amount; i++) {
			StoreContainer storeContainer = new StoreContainer();
			storeContainer.setStoreContainerStatus(StoreContainerStatus.未绑定);
			storeContainer.setStoreContainerType(storeContainerType);
			storeContainer.setLength(length);
			storeContainer.setWidth(width);
			storeContainer.setHeight(height);
			storeContainer.setWeight(weight);
			storeContainers.add(storeContainer);
		}
		storeContainerDao.save(storeContainers);
	}

	private static String storeContainerBindLock = "storeContainerBindLock";

	@Transactional(readOnly = false)
	public void bind(String serialNo) {
		synchronized (storeContainerBindLock) {
			StoreContainer storeContainer = this.findStoreContainerByLabel(serialNo);
			if (storeContainer != null) {
				throw new RuntimeException("此托盘序列号已被使用");
			}
			storeContainer = this.getAnUnbindStoreContainer();
			if (storeContainer == null) {
				throw new RuntimeException("没有需要绑定RFID的托盘");
			}
			storeContainer.setLabel(serialNo);
			storeContainer.setStoreContainerStatus(StoreContainerStatus.正常);
			storeContainerDao.save(storeContainer);
			storeContainerDao.flush();
		}
	}

	private StoreContainer getAnUnbindStoreContainer() {
		Page<StoreContainer> page = storeContainerDao.findByStoreContainerStatusEquals(StoreContainerStatus.未绑定, RepoUtils.buildPageRequest(1, 1));
		if (page.getTotalElements() < 1) {
			return null;
		}
		return page.getContent().get(0);
	}

}
