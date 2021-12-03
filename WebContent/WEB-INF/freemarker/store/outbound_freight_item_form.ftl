<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-freight/index-item?outboundFreightId=${outboundFreightId}&outboundCheckId=${outboundCheckId}">出库发货</a> - 
    <#if outboundFreightItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="outboundFreightItem-form" action="" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${outboundFreightItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${outboundFreightItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
   	<input type="hidden" class="form-control" name="id" id="id" value="${outboundFreightItem.id}" >
   	<input type="hidden" class="form-control" name="outboundFreightId" id="outboundFreightId" value="${outboundFreightId}" >
   	<input type="hidden" class="form-control" name="outboundCheckId" id="outboundCheckId" value="${outboundCheckId}" >

   
    <div class="form-group">
      <label class="col-md-2 control-label">商品:</label>
      <div class="col-md-3">
      <select id="productId" name="product.id" data-parsley-required="true"  onchange="selectedproduct()"	class="form-control" >
       <option value="">请选择商品</option>
       <#list products  as product>
     	 <option value="${product.id}" <#if product.id == outboundFreightItem.product.id>selected</#if>>${product.code}${product.name}</option>
	   </#list>
       </select>
      </div>
    </div>
     
    <div class="form-group">
      <label class="col-md-2 control-label">数量:</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="amount" id="amount" value="${outboundFreightItem.amount}"  onchange="selectedproduct()" data-parsley-required="true"  data-parsley-type="integer">
      </div>
    </div>
    
     <div class="form-group">
      <label class="col-md-2 control-label">数量单位:</label>
      <div class="col-md-3">
      <select id="amountMeasureUnitId" name="amountMeasureUnit.id" class="form-control"  data-parsley-required="true">
      
       <#list amountMeasureUnits as amountMeasureUnit>
     	 <option value="${amountMeasureUnit.id}" <#if amountMeasureUnit.id == outboundFreightItem.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name}</option>
	   </#list>
       </select>
      </div>
	</div>
	
     <div class="form-group">
      <label class="col-md-2 control-label">重量:</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="weight" id="weight" value="${outboundFreightItem.weight}" data-parsley-required="true"  data-parsley-type="number">
      </div>
    </div>
   	  <div class="form-group">
      <label class="col-md-2 control-label">重量单位:</label>
      <div class="col-md-3">
      <select id="weightMeasureUnitId" name="weightMeasureUnit.id" class="form-control" data-parsley-required="true">
      
       <#list weightMeasureUnits as weightMeasureUnit>
     	 <option value="${weightMeasureUnit.id}" <#if weightMeasureUnit.id == outboundFreightItem.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name}</option>
	   </#list>
       </select>
      </div>
 
    </div>

    <div class="form-group">
      <label class="col-md-2 control-label">规格:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="spec" id="spec" value="${outboundFreightItem.spec}" >
      </div>
    </div>
    
     <div class="form-group">
      <label class="col-md-2 control-label">包装:</label>
      <div class="col-md-3">
      <select id="packingId" name="packing.id" class="form-control">
       <option value="">请选择包装类型</option>
       <#list packings as packing>
     	 <option value="${packing.id}" <#if packing.id == outboundFreightItem.packing.id>selected</#if>>${packing.name}</option>
	   </#list>
       </select>
     	 </div>
      </div>
      <div class="form-group">
      <label class="col-md-2 control-label">产地:</label>
       <div class="col-md-3">
       	<input type="text" class="form-control" name="productionPlace" id="productionPlace" value="${outboundFreightItem.productionPlace!}">
        </div>
      </div>
            
     <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
      	
      	<button  class="btn btn-primary"  onclick="save();">保存</button>
      </div>
       
    </div>
  </form>
</div>
<script type="text/javascript">

  function save(){
 	 $('#outboundFreightItem-form').attr("action", "${base}/store/outbound-freight/${outboundFreightItem.id!}/save-item");
     $('#outboundFreightItem-form').submit();
 }

 
     function selectedproduct() {
     	var productId=$("#productId").val();
     	 
        if (productId == '') {
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
</#escape>
