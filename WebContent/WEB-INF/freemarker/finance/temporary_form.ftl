<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/finance/payment">费用</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="payment-form" action="${base}/finance/payment/${id}/save" method="post" class="form-horizontal" data-parsley-validate>
   
    <input type="hidden" name="createdBy" value="${payment.createdBy!}">
	<input type="hidden" name="createdTime" value="${payment.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    
    <input type="hidden" name="settledBy.id" value="${payment.settledBy.id!}">
    <input type="hidden" name="settledTime" value="${payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
   	<input type="hidden" name="serialNo" value="${payment.serialNo!}">
   	<input type="hidden" name="paymentStatus" value="${payment.paymentStatus!}">
   	<input type="hidden" name="temporary" value="${payment.temporary!}">
    
    <div class="form-group">
      <label class="col-md-2 control-label">出入库方式:</label>
      <div class="col-md-4">
        <select id="paymentBoundType" name="paymentBoundType" class="form-control">
            <#list paymentBoundTypes as paymentBoundType>
            <option value="${paymentBoundType}"  <#if paymentBoundType== payment.paymentBoundType>selected</#if>>${paymentBoundType}</option>
            </#list>
        </select>
      </div>
      
      <label class="col-md-2 control-label">结算方式:</label>
      <div class="col-md-4">
        <select id="chargeTypeId" name="chargeType.id" class="form-control">
            <#list chargeTypes as chargeType>
            <option value="${chargeType.id}"  <#if chargeType.id== payment.chargeType.id>selected</#if>>${chargeType.name}</option>
            </#list>
        </select>
      </div>
   	</div>
   	
   	 <div class="form-group">
      <label class="col-md-2 control-label">客户:</label>
      <div class="col-md-4">
        <select id="customerId" name="customer.id" data-parsley-required="true" onchange="selectedCustomer(this.value)" class="form-control">
       	    <option value="">请选择客户</option>
            <#list customers as customer>
            <option value="${customer.id}"  <#if customer.id== payment.customer.id>selected</#if>>${customer.text!}</option>
            </#list>
        </select>
      </div>
      <label class="col-md-2 control-label">费用类型:</label>
      <div class="col-md-4">
        <select id="paymentType" name="paymentType" data-parsley-required="true" class="form-control">
            <#list paymentTypes as paymentType>
            <option value="${paymentType}"  <#if paymentType== payment.paymentType>selected</#if>>${paymentType}</option>
            </#list>
        </select>
      </div>
   	</div>
   	
   	<div class="form-group">
      <label class="col-md-2 control-label">选择商品:</label>
      <div class="col-md-4">
      
        <select id="productId" name="product.id" data-parsley-required="true" class="form-control">
        <option value="">请选择商品</option>
        <#list products as product>
            <option  value="${product.id}" <#if product.id == payment.product.id>selected</#if>>${product.name}</option>
        </#list>
        </select>
      </div>     	
 	</div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-10">
        <button class="btn btn-primary" >保存</button>  
      </div>
    </div>
  </form>
</div>
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
<script type="text/javascript">
   	function selectedCustomer(value) {
        $("#productId").empty();
       
        if (value == '') {
            $("#productId").empty();
            return;
        }
        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" +value,
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 if(ar.length>0){
           		   	for (var i = 0; i < ar.length; i++) {
                	var option = '<option  value='+ar[i].id+'>'+ar[i].code+ar[i].name+
                		'</option>';
                	$("#productId").append(option);
            		}
           		}
         	}
        })	
    }
</script>
</#escape>
