<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/store/outbound-register/${outboundRegisterId}">详情</a> - 
  <#if outboundCheckItem.id?exists>编辑<#else>新建</#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="outboundCheckItem-form" action="" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${outboundCheckItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${outboundCheckItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="outboundRegisterId" id="outboundRegisterId" value="${outboundRegister.id}" >
    <input type="hidden" class="form-control" name="id" id="id" value="${outboundCheckItem.id}" >
    <input type="hidden" class="form-control" name="stockInTime" id="stockInTime" value="${stockOut.stockInTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}" >
    <input type="hidden" class="form-control" name="settledTime" id="settledTime" value="${stockOut.settledTime}" >
    <input type="hidden" class="form-control" name="storeLocation.id" id="storeLocationId" value="${stockOut.storeLocation.id}" >
    <input type="hidden" class="form-control" name="tallyArea.id" id="tallyArea.id" value="${stockOut.tallyArea.id}">
    <div class="form-group">
      <label class="col-md-2 control-label">登记单号:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control"  value="">
        ${outboundRegister.serialNo} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">客户:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control"  value="">
        ${outboundRegister.customer.text!}</div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">托盘:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control" name="storeContainer.id" id="storeContainer.id" value="${stockOut.storeContainer.id}">
        ${stockOut.storeContainer.label} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">货品:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control" name="product.id" id="product.id" value="${stockOut.product.id}">
        ${stockOut.product.name} </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">数量:</label>
      <div class="col-md-3">
        <div class="input-group">
          <input type="text" class="form-control" name="amount" id="amount" value="${stockOut.amount}" data-parsley-required="true"  data-parsley-type="integer">
          <input type="hidden" class="form-control" name="amountMeasureUnit.id" id="amountMeasureUnit.id" value="${stockOut.amountMeasureUnit.id}">
          <span class="input-group-addon">${stockOut.amountMeasureUnit.name}</span> </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">重量:</label>
      <div class="col-md-3">
        <div class="input-group">
          <input type="text" class="form-control" name="weight" id="weight" value="${stockOut.weight}" data-parsley-required="true"  data-parsley-type="number">
          <input type="hidden" class="form-control" name="weightMeasureUnit.id" id="weightMeasureUnit.id" value="${stockOut.weightMeasureUnit.id}">
          <span class="input-group-addon">${stockOut.weightMeasureUnit.name}</span> </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">规格:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control"  value="">
        ${stockOut.spec}
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">包装:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control" name="packing.id" id="packing.id" value="${stockOut.packing.id}">
        ${stockOut.packing.name} 
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">产地:</label>
      <div class="col-md-3">
        <input type="hidden" class="form-control"  value="">
        ${stockOut.productionPlace!}
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
        <button type="button"  class="btn btn-primary"  onclick="save();">完成</button>
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

 function nextadd(){
 	$('#outboundCheckItem-form').attr("action", "${base}/store/outbound-check/next-add-item");
 	$('#outboundCheckItem-form').submit();
 }
  function save(){
 	 $('#outboundCheckItem-form').attr("action", "${base}/store/outbound-check/${stockOut.id!}/save-item");
     $('#outboundCheckItem-form').submit();
 }
   function checkStoreContainer(value){
   	if(value==""){
   	return ;
   	}
 	location.href = "${base}/store/outbound-check/check-storeContainer?outboundRegisterId=${outboundRegister.id}&storeContainerId="+value;
   }    
</script>
</#escape> 