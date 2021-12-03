<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-out">拣货</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<form id="stockin-form" action="${base}/store/stock-out/complete-picking?id=${stockOutId}" method="post" class="form-horizontal" data-parsley-validate>
  <div class="form-group">
 	 <label class="col-md-2 control-label">货品：</label>
    <div class="col-md-10">${stockOut.productName!}</div>
  </div>
  <div class="form-group">
      <label class="col-md-2 control-label">重量/单位：</label>
    <div class="col-md-10">${stockOut.weight!}/${stockOut.weightMeasureUnitName!}</div>
  </div>
  <div class="form-group">
    <#--<div class="col-md-2">数量/单位：</div>-->
     <label class="col-md-2 control-label">数量/单位：</label>
    <div class="col-md-10">${stockOut.amount!}/${stockOut.amountMeasureUnitName!}</div>
  </div>
  <div class="form-group">
  <label class="col-md-2 control-label">托盘标签：</label>
    <div class="col-md-10">${stockOut.storeContainer.label!}</div>
  </div>
 
  <div class="form-group">
 	<label class="col-md-2 control-label">储位：</label>
    <div class="col-md-10">
        	储位编码：${storeLocation.code!} &nbsp;  &nbsp; &nbsp;坐标X： ${storeLocation.coordX!} &nbsp; &nbsp; &nbsp; 坐标Y： ${storeLocation.coordY!}  &nbsp; &nbsp; &nbsp;坐标Z：${storeLocation.coordZ!}  &nbsp; &nbsp; &nbsp;  储位长：${storeLocation.length!}  &nbsp; &nbsp; &nbsp;储位宽：${storeLocation.width!} &nbsp;  &nbsp; &nbsp;储位高：${storeLocation.height!} &nbsp;  &nbsp; &nbsp;承重：${storeLocation.weight!}<br/><br/>
	 </div>
  	</div>
  	
  	 <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
        	<button type="submit" class="btn btn-primary">下架完成</button>
      </div>
    </div>
	</form>
 </div>
</#escape>