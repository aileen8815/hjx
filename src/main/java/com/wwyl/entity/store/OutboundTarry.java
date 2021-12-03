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
@Table(name="TJ_OUTBOUND_TARRY")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundTarry extends PersistableEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private OutboundRegister outboundRegister;//出库登记单
    @ManyToOne(fetch = FetchType.LAZY)
    private TallyArea tallyArea;//理货区
    private String  handsetAddress; //手持机标识
    @Basic
    private boolean checked;//是否清点完成

    
    public OutboundRegister getOutboundRegister() {
		return outboundRegister;
	}

	public void setOutboundRegister(OutboundRegister outboundRegister) {
		this.outboundRegister = outboundRegister;
	}

	public TallyArea getTallyArea() {
        return tallyArea;
    }

    public void setTallyArea(TallyArea tallyArea) {
        this.tallyArea = tallyArea;
    }
    
    public String getHandsetAddress() {
		return handsetAddress;
	}

	public void setHandsetAddress(String handsetAddress) {
		this.handsetAddress = handsetAddress;
	}

	public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
