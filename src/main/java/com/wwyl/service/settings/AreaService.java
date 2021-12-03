package com.wwyl.service.settings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.AreaDao;
import com.wwyl.entity.settings.Area;



/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class AreaService {

	@Resource
	private AreaDao areaDao;

	public List<Area> findRootareas(Long id) {
		List<Area> rootareas = new ArrayList<Area>();
		if(id==null){

		List<Area> areas = areaDao.findAll();
		 
		for (Area area : areas) {
			if (area.getParent() == null) {
				// 过滤因left join造成的重复
				if (!rootareas.contains(area)) {
					if(!area.getChildren().isEmpty()){
						area.setState("closed");
						area.setChildren(null);
					}
					rootareas.add(area);
				}
			}
		}
		
	}else{
		rootareas.addAll(areaDao.findOne(id).getChildren())	;
	}
		return rootareas;
	}

	public List<Area> findAll() {
		return areaDao.findAll();
	}

	public Area findOne(Long id) {
		return areaDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		if (area.getParent() != null && area.getParent().getId() == null) {
			area.setParent(null);
		}
		areaDao.save(area);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		areaDao.delete(id);
	}

	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(areaDao.findByCode(code));
	}
}
