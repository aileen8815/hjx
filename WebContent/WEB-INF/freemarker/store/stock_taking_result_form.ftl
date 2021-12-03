<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-taking/${stockTakingResult.stockTaking.id}">盘点单</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <!-- 明细表单 -->
    <form id="stockTakingResult-form" action="${base}/store/stock-taking/save-result" method="post" class="form-horizontal" data-parsley-validate>
      <input type="hidden" name="stockTaking.id" value="${stockTakingResult.stockTaking.id!}">
      <input type="hidden" name="id" value="${stockTakingResult.id!}">
       <input type="hidden" class="form-control" name="toStoreLocation.id" id="toStoreLocationId"    value="${stockTakingResult.toStoreLocation.id}"  >
      <div class="form-group">
        <label class="col-md-2 control-label">储位:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="storeLocation.code" id="locationCode" data-parsley-required="true"  value="${stockTakingResult.storeLocation.code}"  readonly >
         <input type="hidden" class="form-control" name="storeLocation.id" id="locationId"    value="${stockTakingResult.storeLocation.id}"  >
        </div>
      
      </div>
       
      <div id="modal-item-ext">
          <div class="form-group">
            <label class="col-md-2 control-label">托盘:</label>
            <div class="col-md-4">
            <input type="text" class="form-control" id="storeContainerName" name="storeContainer.label" value="${stockTakingResult.storeContainer.label}" data-parsley-required="true"    <#if stockTakingResult.storeContainer?exists>readonly</#if>>
            <input type="hidden" class="form-control" id="storeContainerId" name="storeContainer.id" value="${stockTakingResult.storeContainer.id}">
                
            </div>
            
          </div>
		  <div class="form-group">
            <label class="col-md-2 control-label">客户:</label>
            <div class="col-md-4">
           		<select id="seller" name="customer.id" class="form-control"  onchange="selectedCustomer(this.value)"  data-parsley-required="true">
      			<option value="">请选择客户</option>
       			<#list customers as customer>
     			<option value="${customer.id}" <#if customer.id == stockTakingResult.customer.id>selected</#if>>${customer.custemer}${customer.name}</option>
	  			</#list>
      			</select>
            </div>
          </div>	
	
          <div class="form-group">
            <label class="col-md-2 control-label">商品:</label>
            <div class="col-md-4">
             <select id="productId" name="product.id" class="form-control"   onchange="selectedproduct(this.value)"  data-parsley-required="true">
                 	<option value="">请选择商品</option>
					<#list products as product>
                        <option value="${product.id}"
                                <#if product.id == stockTakingResult.product.id>selected</#if>>${product.code}${product.name}</option>
                    </#list>
         </select>
            <#--
              <input type="text" class="form-control" id="productName" name="productName" readonly value="${stockTakingResult.product.text!}"   data-parsley-required="true">
      		  <input type="hidden" name="product.id" id="productId" value="${stockTakingResult.product.id!}">
            -->
            </div>
          </div>
          
          <div class="form-group">
            <label class="col-md-2 control-label">库存重量:</label>
            <div class="col-md-4">
 				<input type="text" class="form-control" name="weight" id="weight" value="${stockTakingResult.weight}" readonly>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 control-label">盘点重量:</label>
            <div class="col-md-3">
              <input type="text" class="form-control" name="stockTakingWeight" id="stockTakingWeight" value="${stockTakingResult.stockTakingWeight}"   data-parsley-required="true" data-parsley-type="number"   data-parsley-gt="0">
            </div>
            <div class="col-md-1">
              <select class="form-control" id="weightMeasureUnit" name="weightMeasureUnit.id">
                <#list weightMeasureUnits as weightMeasureUnit>
                  <option value="${weightMeasureUnit.id!}"   <#if weightMeasureUnit.id == stockTakingResult.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name} </option>
                </#list>
              </select>
            </div>
          </div>
            <div class="form-group">
            <label class="col-md-2 control-label">库存数量:</label>
            <div class="col-md-4">
 				<input type="text" class="form-control" name="amount" id="amount" value="${stockTakingResult.amount}" readonly>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 control-label">盘点数量:</label>
            <div class="col-md-3">
              <input type="text" class="form-control" name="stockTakingAmount" id="stockTakingAmount" value="${stockTakingResult.stockTakingAmount}"  data-parsley-required="true" data-parsley-type="integer"   data-parsley-gt="0">
            </div>
            <div class="col-md-1">
              <select class="form-control" id="amountMeasureUnit" name="amountMeasureUnit.id">
                 <#list amountMeasureUnits as amountMeasureUnit>
                  <option value="${amountMeasureUnit.id!}"   <#if amountMeasureUnit.id == stockTakingResult.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name} </option>
                </#list>
              </select>
            </div>
          </div>
         
      
      <div class="form-group">
        <div class="col-md-offset-2 col-md-9">
          <button class="btn btn-success">保存</button>
        </div>
      </div>
    </form>

<#--<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
<script>
$(function () {
    $('#productName').ztreeSelect({
        hiddenInput: '#productId',
        url: "${base}/settings/product/ztree"
    });
});-->
<script>
function selectedCustomer(value) {
        $("#productId").empty();
        $("#productId").prepend('<option  value= >请选择商品</option>');
       
        if (value == '') {
            $("#productId").empty();
            return;
        }
        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" +value,
            data: '',
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 if(ar.length>0){
           		   	for (var i = 0; i < ar.length; i++) {
                	var option = '<option  value='+ar[i].id+'>'+ar[i].code+ar[i].name+'</option>';
                	$("#productId").prepend(option);
            		}
           		}
         	}
        })	
    }
   
 	function selectedproduct(value) {
     	 
     	 
        if (value == '') {
           return;
        }
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" +productId,
            data: '',
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 $("#packingId").val(ar.commonPacking.id);
           		 $("#spec").val(ar.spec);
           		  $("#productionPlace").val(ar.productionPlace);
           		 var amount= $("#amount").val();
           		
           		 var reg = "^[1-9]*[1-9][0-9]*$";
           		 if(amount.match(reg)!=null){
           		 	var count=0;
           		 	if(amount%ar.bearingCapacity>0){
           		 		count=1
           		 	}
					$("#storeContainerCount").val((amount-amount%ar.bearingCapacity)/ar.bearingCapacity+count);
					
					$("#weight").val(amount*ar.weight);
           		 }
           	}
        })	
    }  
</script>

</div>
</#escape>