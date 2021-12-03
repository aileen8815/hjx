<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stockin">上架</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<form id="stockin-form" action="${base}/store/stockin/pickup?id=${stockInId}" method="post" class="form-horizontal" data-parsley-validate>
  <div class="form-group">
 	 <label class="col-md-2 control-label">货品：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.productName!}<br/><br/></div>
  </div>
  <div class="form-group">
      <label class="col-md-2 control-label">重量/单位：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.weight!}/${inboundReceiptItem.weightMeasureUnitName!}<br/><br/></div>
  </div>
  <div class="form-group">
    <#--<div class="col-md-2">数量/单位：</div>-->
     <label class="col-md-2 control-label">数量/单位：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.amount!}/${inboundReceiptItem.amountMeasureUnitName!}<br/><br/></div>
  </div>
  <div class="form-group">
  <label class="col-md-2 control-label">托盘标签：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.storeContainer.label!}<br/><br/></div>
  </div>
  <div class="form-group">
  <label class="col-md-2 control-label">多品明细：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.productDetail!}<br/><br/></div>
  </div>
 
<#--
  <div class="form-group">
 <label class="col-md-2 control-label">建议上架储位：</label>
    <div class="col-md-10">
      <#list storeLocations as storeLocation>
       	储位编码 ：${storeLocation.code!} &nbsp;  &nbsp; &nbsp;坐标X： ${storeLocation.coordX!} &nbsp; &nbsp; &nbsp; 坐标Y： ${storeLocation.coordY!}  &nbsp; &nbsp; &nbsp;坐标Z：${storeLocation.coordZ!}  &nbsp; &nbsp; &nbsp;  储位长：${storeLocation.length!}  &nbsp; &nbsp; &nbsp;储位宽：${storeLocation.width!} &nbsp;  &nbsp; &nbsp;储位高：${storeLocation.height!} &nbsp;  &nbsp; &nbsp;承重：${storeLocation.weight!}<br/><br/>
      </#list>
    </div>
  	</div>
  -->
  	<div class="form-group">
      <div class="col-md-offset-2 col-md-3">
         <button type="submit" class="btn btn-primary">取货</button>
      </div>
    </div>
	</form>
 </div>
</#escape>