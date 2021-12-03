package com.wwyl.entity.settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StoreAreaRentStatus;
import com.wwyl.Enums.StoreAreaStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.store.StoreAreaAssignee;

/**
 * 仓间库区
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STORE_AREA")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StoreArea extends PersistableEntity {

    @NotBlank
    @Column(length = 20, nullable = false, unique = true)
    private String code;// 编号
    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;// 名称
    @Column(length = 50)
    private String contructArea;// 建筑面积
    @Column(length = 50)
    private String useArea;// 使用面积
    @Column(length = 100)
    private String temperatureCondition; // 温度条件
    @Basic
    private StoreAreaStatus storeAreaStatus;// 状态
    @Basic
    private StoreAreaRentStatus storeAreaRentStatus;// 状态
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreArea parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("code")
    private Set<StoreArea> children;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "TJ_STORE_AREA_PRODUCT", joinColumns = {@JoinColumn(name = "storearea")}, inverseJoinColumns = {@JoinColumn(name = "product")})
    private Set<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy = "storeArea", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<StoreAreaAssignee> storeAreaAssignees;

	@Basic
    private int layoutCorriderLine = 3;

    public Set<StoreArea> getChildren() {
        return children;
    }

    public void setChildren(Set<StoreArea> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContructArea() {
        return contructArea;
    }

    public void setContructArea(String contructArea) {
        this.contructArea = contructArea;
    }

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public String getTemperatureCondition() {
        return temperatureCondition;
    }

    public void setTemperatureCondition(String temperatureCondition) {
        this.temperatureCondition = temperatureCondition;
    }

    public StoreAreaStatus getStoreAreaStatus() {
        return storeAreaStatus;
    }

    public void setStoreAreaStatus(StoreAreaStatus stockAreaStatus) {
        this.storeAreaStatus = stockAreaStatus;
    }

    public StoreArea getParent() {
        return parent;
    }

    public void setParent(StoreArea parent) {
        this.parent = parent;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getText() {
        return this.getCode() + " " + this.getName();
    }

    public void setStoreProducts(Long[] products) {
        Set<Product> productset = new HashSet<Product>();
        if (ArrayUtils.isNotEmpty(products)) {
            for (Long productid : products) {
                Product product = new Product();
                product.setId(productid);
                productset.add(product);
            }
        }
        setProducts(productset);
    }

    public List<Long> getTreeNodeIds() {
        List<Long> fullPathIds = new ArrayList<Long>();
        fullPathIds.add(this.getId());
        if (CollectionUtils.isNotEmpty(children)) {
            for (StoreArea child : children) {
                fullPathIds.add(child.getId());
                fullPathIds.addAll(child.getTreeNodeIds());
            }
        }
        return fullPathIds;
    }

    public Set<StoreAreaAssignee> getStoreAreaAssignees() {
        return storeAreaAssignees;
    }

    public void setStoreAreaAssignees(Set<StoreAreaAssignee> storeAreaAssignees) {
        this.storeAreaAssignees = storeAreaAssignees;
    }

    public String getStoreAreaAssigneeText(){
        if(CollectionUtils.isEmpty(this.storeAreaAssignees)){
            return "";
        }

        String result = "";
        int index=0;
        int size=storeAreaAssignees.size();
        for(StoreAreaAssignee storeAreaAssignee: storeAreaAssignees){
        	index++;
            result += storeAreaAssignee.getOperatorUserName() + storeAreaAssignee.getOperatorName() + (index==size?"":", ");
            
        }
        return result;
    }

    public int getLayoutCorriderLine() {
        return layoutCorriderLine;
    }

    public void setLayoutCorriderLine(int layoutCorriderLine) {
        this.layoutCorriderLine = layoutCorriderLine;
    }

	public StoreAreaRentStatus getStoreAreaRentStatus() {
		return storeAreaRentStatus;
	}

	public void setStoreAreaRentStatus(StoreAreaRentStatus storeAreaRentStatus) {
		this.storeAreaRentStatus = storeAreaRentStatus;
	}
    
}
