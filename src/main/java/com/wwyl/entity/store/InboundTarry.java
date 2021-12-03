package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.TallyArea;

import javax.persistence.*;

/**
 * Created by fyunli on 14-6-3.
 */
@Entity
@Table(name="TJ_INBOUND_TARRY")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class InboundTarry extends PersistableEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private InboundRegister inboundRegister;//入库登记单
    @ManyToOne(fetch = FetchType.LAZY)
    private TallyArea tallyArea;//理货区
    @Basic
    private boolean checked;//是否清点完成
    private String  handsetAddress; //手持机标识
    public InboundRegister getInboundRegister() {
        return inboundRegister;
    }

    public void setInboundRegister(InboundRegister inboundRegister) {
        this.inboundRegister = inboundRegister;
    }

    public TallyArea getTallyArea() {
        return tallyArea;
    }

    public void setTallyArea(TallyArea tallyArea) {
        this.tallyArea = tallyArea;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

	public String getHandsetAddress() {
		return handsetAddress;
	}

	public void setHandsetAddress(String handsetAddress) {
		this.handsetAddress = handsetAddress;
	}
    
}
