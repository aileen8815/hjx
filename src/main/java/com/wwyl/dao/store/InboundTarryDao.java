package com.wwyl.dao.store;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.InboundTarry;

/**
 * @author jianl
 */
@Repository
public interface InboundTarryDao extends BaseRepository<InboundTarry,Long>{
	@Query("select it from InboundTarry it where it.inboundRegister.id=?1")
	public List<InboundTarry> findByInboundRegisterId(Long inboundRegisterId);
	@Query("select it from InboundTarry it where it.handsetAddress=?1")
	public List<InboundTarry> findByInboundRegisterId(String  handsetAddress);
	@Query("select intarry.handsetAddress,count(intarry.id)  from  InboundTarry  intarry ,InboundRegister inregister   where   intarry.inboundRegister.id=inregister.id    and  inregister.stockInStatus=0  group by intarry.handsetAddress ")
	public List<Object> findHandsetTask();
	
	
}
