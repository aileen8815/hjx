package com.wwyl.service.settings;

import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.dao.settings.SystemConfigDao;
import com.wwyl.entity.settings.SystemConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SystemConfigService {

	@Resource
	private SystemConfigDao systemConfigDao;

	public List<SystemConfig> findAll() {
		return this.systemConfigDao.findAll();
	}

	public SystemConfig findOne(Long id) {
		return (SystemConfig) this.systemConfigDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(SystemConfig systemConfig) {
		this.systemConfigDao.save(systemConfig);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.systemConfigDao.delete(id);
	}
	public  Map<Object,String>   getReportVal(){
		Map<Object,String>  result=new HashMap<Object, String>();
		List<SystemConfig>  list=findAll();
		if(list==null||list.isEmpty()){
			new RuntimeException("报表参数未设置");
		}
		for (SystemConfig systemConfig : list) {
			if (SystemConfigKey.报表服务器URL.equals(systemConfig.getAttribute())
					|| SystemConfigKey.报表服务器密码.equals(systemConfig
							.getAttribute())
					|| SystemConfigKey.报表服务器用户名.equals(systemConfig
							.getAttribute())) {
				result.put(systemConfig.getAttribute(), systemConfig.getValue());
			}
			 
		}
		return result;
		
	}
}