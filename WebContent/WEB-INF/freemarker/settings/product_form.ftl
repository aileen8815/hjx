<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/customer/${customer.id}">商品信息</a> - 
    <#if product.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeLocationType-form" action="${base}/settings/customer/${product.id!}/save-product" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${product.createdBy!}">
    <input type="hidden" name="createdTime" value="${product.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="customerId" value="${customer.id!}">
    <input type="hidden" name="code" value="${product.code!}">
    <#if product.parent?exists>
    <div class="form-group">
      <label class="col-md-2 control-label">上级商品:</label>
      <div class="col-md-4">
        <input type="hidden" name="parent.id" value="${product.parent.id!}">
        <input type="text" class="form-control" value="${product.parent.code!} ${product.parent.name!}" disabled>
      </div>
    </div>
    </#if>
   <#-- <div class="form-group">
      <label class="col-md-2 control-label">编码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="code" id="code" value="${product.code!}" data-parsley-required="true"     
          data-parsley-remote="${base}/settings/product/valid-code?oldCode=${product.code!}" 
          data-parsley-error-message="代码必须填写并不能重复">
      </div>
    </div>-->
    <div class="form-group">
      <label class="col-md-2 control-label">名称:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="name" id="name" value="${product.name!}" data-parsley-required="true">
      </div>
    </div>
   	  <div class="form-group">
      <label class="col-md-2 control-label">商品类别:</label>
      <div class="col-md-4">
      <select id="productCategoryId" name="productCategory.id"   data-parsley-required="true"  class="form-control">
       <option value="">请选择商品类型</option>
       <#list productCategorys as productCategory>
         <option value="${productCategory.id}" <#if productCategory.id == product.productCategory.id>selected</#if>>${productCategory.code}${productCategory.name}</option>
       </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">常用包装:</label>
      <div class="col-md-4">
      <select id="packingId" name="commonPacking.id" class="form-control">
       <#list packings as packing>
     	 <option value="${packing.id}" <#if packing.id == product.commonPacking.id>selected</#if>>${packing.name}</option>
	   </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">产地:</label>
      <div class="col-md-4">
      <input type="text" class="form-control" name="productionPlace" id="productionPlace" value="${product.productionPlace!}">
      </div>
    </div>
     <div class="form-group">
      <label class="col-md-2 control-label">规格:</label>
      <div class="col-md-4">
      <input type="text" class="form-control" name="spec" id="spec" value="${product.spec!}">
      </div>
    </div>
 	<div class="form-group">
      <label class="col-md-2 control-label">参考重量:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="weight" id="weight" value="${product.weight!}" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">计量单位:</label>
      <div class="col-md-4">
      <select id="commonUnitId" name="commonUnit.id" class="form-control">
       <#list measureUnit as measureUnit>
     	 <option value="${measureUnit.id}" <#if measureUnit.id == product.commonUnit.id>selected</#if>>${measureUnit.name}</option>
	   </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">托盘承载件数:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="bearingCapacity" id="bearingCapacity" value="${product.bearingCapacity}" data-parsley-required="true"
                       data-parsley-type="integer"  data-parsley-gt="0">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">价格参考区间:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="priceRange" id="priceRange" value="${product.priceRange!}"  >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">备注:</label>
      <div class="col-md-4">
    	<input type="text" class="form-control" name="remark" id="remark" value="${product.remark!}">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-4">
        <button type="submit" class="btn btn-primary" onclick="getWeightDefault();">保存</button>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">
	function getWeightDefault()
	{
		var weight = $("#weight").val();
		
		if((weight.trim() ==''))
		{
			$("#weight").val('0');
		}
	}
</script>
</#escape>