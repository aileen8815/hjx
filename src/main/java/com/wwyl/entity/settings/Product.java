package com.wwyl.entity.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 库存商品
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PRODUCT")
@Cacheable
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class Product extends PersistableEntity {

    @NotBlank
    @Column(length = 20, nullable = false, unique = true)
    private String code; // 编码
    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;// 名称
    @Column(length = 255)
    private String remark; // 描述

    @Column(length = 100)
    private String priceRange; // 参考价格区间
    
    //用于区分普通，酮体，整托属性；计费标准使用
    @ManyToOne(fetch = FetchType.LAZY)
    private Packing commonPacking; // 常用包装
	@Column(length = 255)
	private String productionPlace; // 产地
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private double bearingCapacity;// 托盘承载件数
	@Column(length = 255)
	private String spec; // 规格
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private double weight;// 参考重量
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasureUnit commonUnit; // 常用计量单位	

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Product parent;
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("code")
    Set<Product> children;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;
  
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MeasureUnit getCommonUnit() {
        return commonUnit;
    }

    public void setCommonUnit(MeasureUnit commonUnit) {
        this.commonUnit = commonUnit;
    }

    public Packing getCommonPacking() {
        return commonPacking;
    }

    public void setCommonPacking(Packing packing) {
        this.commonPacking = packing;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getCommonPackingName() {
        return this.getCommonPacking() == null ? "" : this.getCommonPacking().getName();
    }

    public String getCommonUnitName() {
        return this.getCommonUnit() == null ? "" : this.getCommonUnit().getName();
    }
    
    public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getBearingCapacity() {
		return bearingCapacity;
	}

	public void setBearingCapacity(double bearingCapacity) {
		this.bearingCapacity = bearingCapacity;
	}
	
	public Product getParent() {
        return parent;
    }

    public void setParent(Product parent) {
        this.parent = parent;
    }
    
    public Set<Product> getChildren() {
        return children;
    }

    public void setChildren(Set<Product> children) {
        this.children = children;
    }

    public String getText() {
        return this.getCode() + " " + this.getName();
    }

    public int getTier() {
        if (this.getParent() == null) {
            return 0;
        } else {
            return this.getParent().getTier() + 1;
        }
    }
    public List<Long> getTreeNodeIds() {
        List<Long> fullPathIds = new ArrayList<Long>();
        fullPathIds.add(this.getId());
        if (CollectionUtils.isNotEmpty(children)) {
            for (Product child : children) {
                fullPathIds.add(child.getId());
                fullPathIds.addAll(child.getTreeNodeIds());
            }
        }
        return fullPathIds;
    }
   
   public  void clone(Product  product)  {
	   this.bearingCapacity=product.getBearingCapacity();
	   this.commonPacking=product.getCommonPacking();
	   this.commonUnit=product.getCommonUnit();
	   this.name=product.getName();
	   this.priceRange=product.getPriceRange();
	   this.productCategory=product.getProductCategory();
	   this.productionPlace=product.getProductionPlace();
	   this.remark=product.getRemark();
	   this.spec=product.getSpec();
	   this.weight=product.getWeight();
   }
}
