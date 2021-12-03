package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wwyl.dao.security.OperatorDao;
import com.wwyl.dao.settings.StoreAreaDao;
import com.wwyl.dao.store.StoreAreaAssigneeDao;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.store.StoreAreaAssignee;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StoreAreaAssigneeService {
 
	@Resource
	private StoreAreaAssigneeDao storeAreaAssigneeDao;
	
	@Resource
	private OperatorDao operatorDao;

	@Resource
	private StoreAreaDao storeAreaDao;
	private List<StoreArea> childrenList=null;
	public List<StoreArea> findStoreAreas(Long id) {
		StoreArea storeAreas = storeAreaDao.findByParent(id).get(0);
		childrenList=new ArrayList<StoreArea>();
		childrenList.add(storeAreas);
		if(storeAreas.getChildren()!=null){
			getChildrens(storeAreas.getChildren());
		}
		
		return childrenList;
	}
	/**
	 * 得到选中仓间下的所有子仓间
	 * @return
	 */
	public void getChildrens(Set<StoreArea> storeAreaSet){
			StoreArea[] storeAreArray=new StoreArea[storeAreaSet.size()];
			storeAreaSet.toArray(storeAreArray);
			for(int i=0;storeAreArray!=null&&i<storeAreArray.length;i++){
				childrenList.add(storeAreArray[i]);
				if(storeAreArray[i].getChildren()!=null){//如果是有子仓间的话，则继续往下遍历
				 getChildrens(storeAreArray[i].getChildren());
			}
		}

	}
	public List<Operator> findAllOperators() {
		return operatorDao.findAll();
	}

	public Operator findOperatorById(Long id) {
		return operatorDao.findOne(id);
	}
	


	public List<StoreAreaAssignee> findAllStoreAreaAssignees() {
		return storeAreaAssigneeDao.findAll();
	}
	public List<StoreAreaAssignee> findByOperatorId(Long operatorId) {
		return storeAreaAssigneeDao.findByOperatorId(operatorId);
	}

	@Transactional(readOnly = false)
	public void  save(StoreAreaAssignee storeAreaAssignee,Long[] storeAreas){
		 List<StoreAreaAssignee> storeAreaAssignees=	storeAreaAssigneeDao.findByOperatorId(storeAreaAssignee.getOperator().getId());
		for (StoreAreaAssignee storeAreaAssignee2 : storeAreaAssignees) {
			storeAreaAssigneeDao.delete(storeAreaAssignee2.getId());
		}
		 
	//	List<StoreArea> storeAreas=	findStoreAreas(storeAreaAssignee.getStoreArea().getId());
	 //List<StoreAreaAssignee> storeAreaAssignees=	storeAreaAssigneeDao.findByOperatorId(storeAreaAssignee.getOperator().getId());
		StoreAreaAssignee storeAreaAssigneed=null;
		for (Long   storeAreaid : storeAreas) {
		/*	 if(storeids.contains(Long.valueOf(storeAreaid))){
				 //已经被分配该仓管员的仓间
				 continue;
			 }*/
			StoreArea  store=new StoreArea();
			store.setId(storeAreaid);
			storeAreaAssigneed=new StoreAreaAssignee();
			storeAreaAssigneed.setStoreArea( store);
			storeAreaAssigneed.setOperator(storeAreaAssignee.getOperator());
			
			 storeAreaAssigneeDao.save(storeAreaAssigneed);
		}
		
	}
	@Transactional(readOnly = false)
	public void  delete(Long id){
		 StoreAreaAssignee storeAreaAssignee= storeAreaAssigneeDao.findOne(id);
		 List<StoreArea> storeAreas =findStoreAreas(storeAreaAssignee.getStoreArea().getId());

		 int size= storeAreas.size();
		 for (int i = 0; i < size; i++) {
			 storeAreaAssigneeDao.deleteBystoreAreaId(storeAreas.get(i).getId());
		}
		 //删除库存的子项
		
/*		 //如果该库存有父项且的父项没有了子项，删除父项
		 StoreArea  parentStoreArea=storeAreaAssignee.getStoreArea().getParent();
		 if(parentStoreArea!=null){
		 List<StoreArea> parentStoreAreaschildren =findStoreAreas(storeAreaAssignee.getStoreArea().getId());
		 	if(parentStoreAreaschildren!=null&&parentStoreAreaschildren.size()==1){
		 		storeAreaAssigneeDao.deleteBystoreAreaId(parentStoreArea.getId()+"");
		 	}
		 }*/
	}

}
