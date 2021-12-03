<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/store/inbound-register/${inboundRegister.id}">入库验货</a> -
  <#if inboundReceiptItem.id?exists>编辑<#else>新建</#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="inboundReceiptItem-form" action="" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${inboundReceiptItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${inboundReceiptItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="inboundRegisterId" id="inboundRegisterId" value="${inboundRegister.id}" >
    <input type="hidden" class="form-control" name="id" id="id" value="${inboundReceiptItem.id}" >
    <input type="hidden" class="form-control" name="storeArea.id" id="storeArea.id" value="${inboundRegisterItem.storeArea.id}" >
    <#if inboundRegister.inboundTarrys?has_content>
    <#list inboundRegister.inboundTarrys as inboundTarry>
    <input type="hidden" class="form-control" name="tarryArea.id" id="tarryArea.id" value="${inboundTarry.tallyArea.id!}" >
    </#list>
    </#if>
    <div class="form-group">
      <label class="col-md-2 control-label">登记单号:</label>
      <div class="col-md-3">
        <input type="hidden">
        ${inboundRegister.serialNo} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">客户:</label>
      <div class="col-md-3">
        <input type="hidden">
        ${inboundRegister.customer.name} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">商品:</label>
      <div class="col-md-3">
        <input type="hidden" name="product.id" id="productId" value="${inboundRegisterItem.product.id!}">
        ${inboundRegisterItem.product.name!} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">托盘:</label>
      <div class="col-md-3">
      
       <input type="text" class="form-control" id="storeContainerId"   name="storeContainer.id"  value="${inboundReceiptItem.storeContainer.id}"  data-parsley-required="true"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">重量:</label>
      <div class="col-md-3">
        <div class="input-group">
          <input type="text" class="form-control" name="weight" id="weight" value="${inboundReceiptItem.weight}" data-parsley-required="true"  data-parsley-type="number"  data-parsley-gt="0">
          <input type="hidden" class="form-control" name="weightMeasureUnit.id" id="weightMeasureUnit.id" value="${inboundRegisterItem.weightMeasureUnit.id}">
          <span class="input-group-addon">${inboundRegisterItem.weightMeasureUnit.name}</span> </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">数量:</label>
      <div class="col-md-3">
        <div class="input-group">
          <input type="text" class="form-control" name="amount" id="amount" value="${inboundReceiptItem.amount}" data-parsley-required="true"  data-parsley-type="integer"   data-parsley-gt="0">
          <input type="hidden" class="form-control" name="amountMeasureUnit.id" id="amountMeasureUnit.id" value="${inboundRegisterItem.amountMeasureUnit.id}">
          <span class="input-group-addon">${inboundRegisterItem.amountMeasureUnit.name}</span> </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">规格:</label>
      <div class="col-md-3">
        <input type="hidden" name="spec" id="spec" value="${inboundRegisterItem.spec!}">
        ${inboundRegisterItem.spec} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">包装:</label>
      <div class="col-md-3">
        <input type="hidden" name="packing.id" id="packing.id" value="${inboundRegisterItem.packing.id!}">
        ${inboundRegisterItem.packing.name!} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">产地:</label>
      <div class="col-md-3">
         <input type="hidden" name="productionPlace" id="productionPlace" value="${inboundRegisterItem.productionPlace!}">
        ${inboundRegisterItem.productionPlace!} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">保质期（天）:</label>
      <div class="col-md-3">
      	 <input type="hidden" name="qualityGuaranteePeriod" id="qualityGuaranteePeriod" value="${inboundRegisterItem.qualityGuaranteePeriod!}">
        ${inboundRegisterItem.qualityGuaranteePeriod} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">生产日期:</label>
      <div class="col-md-3">
        <input type="hidden" name="productionDate" id="productionDate" value="<#if inboundRegisterItem.productionDate?exists>${inboundRegisterItem.productionDate?string('yyyy-MM-dd')}</#if>">
        <#if inboundRegisterItem.productionDate?exists>${inboundRegisterItem.productionDate?string('yyyy-MM-dd')}</#if> </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">预期保管时间（天）:</label>
      <div class="col-md-3">
         <input type="hidden" name="storeDuration" id="storeDuration" value="${inboundRegisterItem.storeDuration!}">
        ${inboundRegisterItem.storeDuration} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">是否合格:</label>
      <div class="col-md-3">
        <input type="hidden">
        <input type="radio" name="qualified"  checked  value="true"  <#if true == inboundReceiptItem.qualified>checked</#if>>合格 <input type="radio" name="qualified"   value="false"  <#if inboundReceiptItem?exists&&false == inboundReceiptItem.qualified>checked</#if> >不合格 </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">多品明细:</label>
      <div class="col-md-3">
         <input type="hidden" name="productDetail" id="productDetail" value="${inboundRegisterItem.productDetail!}">
        ${inboundRegisterItem.productDetail!} </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-1" >
        <button  type="button" class="btn btn-primary"  onclick="save();">完成</button>
      </div>
    </div>
  </form>
</div>
<#--
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
-->
<script type="text/javascript">
$(function(){
		 $("#storeContainerId").select2({
            placeholder: "",
            minimumInputLength: 3,
            ajax: { 
                 url: "${base}/store/book-inventory/findstoreContainers",
                dataType: 'json',
                data: function (term, page) {
                    return {
                        label: term   
                    };
                },
                results: function (data, page) {  
                  
                    var realNames = new Array();    
                         for (var i = 0; i < data.length; i++) {
                              realNames.push({id: data[i].id, text: data[i].label});
                       }
                    return {results: realNames};
                }
            },
            initSelection: function(element, callback) {
             
	            var id = $("#storeContainerId").val();
				if (id !== "") {
					$.ajax("${base}/settings/store-container/"+id, {
					dataType: "json"
					}).done(function(data) { callback(data); });
				}
			} 
        });
	})
 
 function nextadd(){
 	$('#inboundReceiptItem-form').attr("action", "${base}/store/inbound-receipt/next-add-item");
 	$('#inboundReceiptItem-form').submit();
 }
  function save(){
 	 $('#inboundReceiptItem-form').attr("action", "${base}/store/inbound-receipt/save-item");
     $('#inboundReceiptItem-form').submit();
 }
</script>
</#escape> 