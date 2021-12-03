<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stockin">上架</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<form id="stockin-form" action="${base}/store/stockin/complete-pickup?id=${stockInId}" method="post" class="form-horizontal" data-parsley-validate>
  <div class="form-group">
 	 <label class="col-md-2 control-label">货品：</label>
    <div class="col-md-10">${inboundReceiptItem.productName!}</div>
  </div>
  <div class="form-group">
      <label class="col-md-2 control-label">重量/单位：</label>
    <div class="col-md-10">${inboundReceiptItem.weight!}/${inboundReceiptItem.weightMeasureUnitName!}</div>
  </div>
  <div class="form-group">
    <#--<div class="col-md-2">数量/单位：</div>-->
     <label class="col-md-2 control-label">数量/单位：</label>
    <div class="col-md-10">${inboundReceiptItem.amount!}/${inboundReceiptItem.amountMeasureUnitName!}</div>
  </div>
  <div class="form-group">
  <label class="col-md-2 control-label">托盘标签：</label>
    <div class="col-md-10">${inboundReceiptItem.storeContainer.label!}</div>
  </div>
  <div class="form-group">
  <label class="col-md-2 control-label">多品明细：</label>
    <div class="col-md-10"><br/>${inboundReceiptItem.productDetail!}</div>
  </div>
  <div class="form-group">
  <#--
 <label class="col-md-2 control-label">建议上架储位：</label>
    <div class="col-md-10">
      <#list storeLocations as storeLocation>
       	储位编码 ：${storeLocation.code!} &nbsp;  &nbsp; &nbsp;坐标X： ${storeLocation.coordX!} &nbsp; &nbsp; &nbsp; 坐标Y： ${storeLocation.coordY!}  &nbsp; &nbsp; &nbsp;坐标Z：${storeLocation.coordZ!}  &nbsp; &nbsp; &nbsp;  储位长：${storeLocation.length!}  &nbsp; &nbsp; &nbsp;储位宽：${storeLocation.width!} &nbsp;  &nbsp; &nbsp;储位高：${storeLocation.height!} &nbsp;  &nbsp; &nbsp;承重：${storeLocation.weight!}<br/><br/>
      </#list>
    </div>
  </div>
  -->
  
	<div class="form-group">
      <label class="col-md-2 control-label">上架储位：</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="storeLocation" id="storeLocation" value="${storeLocation}" data-parsley-required="true" readonly>
      </div>
       <div class="col-md-1">
          <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
     </div>
      </div>
  	 <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
        <button type="submit" class="btn btn-primary">上架完成</button>
      </div>
    </div>
	</form>
 </div>
 
 
<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog-full modal-location-selector">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">选择储位</h4>
      </div>
      <div id="selector-body" class="modal-body">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal" >Close</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
    $.get('${base}/store/store-location-selector?allAreaBlank=1', function(data){
        $("#selector-body").html(data);
    });
	

    function locationNodeClick() {
        var result = $('#location-result');
        $("[title='"+result.text()+"']").parent().removeClass('stat-selected') ; 
        var title = $(this).attr('title');
        
        if ($(result).text().indexOf(title) == -1) {
            $(result).text(title);
            $(this).parent().addClass('stat-selected');
        } else {
            $(result).text('');
            $(this).parent().removeClass('stat-selected');
        }

        $('#storeLocation').val(title);

    }

      	
 
</script>
</#escape>