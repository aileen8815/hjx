<#escape x as x?html>
<header class="panel-heading">
     <a href="${base}/finance/payment/${paymentId}/view">费用</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="paymentItem-form" action="${base}/finance/payment/${id}/save-item" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${paymentItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${paymentItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="paymentStatus" value="${paymentItem.paymentStatus!}">
    <input type="hidden" name="payment.id" value="${paymentId!}">
    <div class="form-group">
      <label class="col-md-2 control-label">费用项目:</label>
      <div class="col-md-3">
        <select id="feeItemId" name="feeItem.id" data-parsley-required="true" class="form-control">
            <#list feeItems as feeItem>
            <option value="${feeItem.id}"  <#if feeItem.id== paymentItem.feeItem.id>selected</#if>>${feeItem.name}</option>
            </#list>
        </select>
      </div>

 	<label class="col-md-2 control-label">商品数量（件）:</label>
   	<div class="col-md-3">
	        <input type="text" class="form-control" name="amountPiece" id="amountPiece" value="${paymentItem.amountPiece}"
	               data-parsley-type="integer" data-parsley-required="true"
	               data-parsley-min="0">
	      </div>       
 	</div>
 	
 	<div class="form-group">
 	<label class="col-md-2 control-label">托盘数量（托）:</label>
   		<div class="col-md-3">
	        <input type="text" class="form-control" name="storeContainerCount" id="storeContainerCount" value="${paymentItem.storeContainerCount}"
	               data-parsley-type="integer" data-parsley-required="true"
	               data-parsley-min="0">
	    </div>    
 	<label class="col-md-2 control-label">商品重量（公斤）:</label>
   		<div class="col-md-3">
	        <input type="text" class="form-control" name="weight" id="weight" value="${paymentItem.weight}"
	               data-parsley-type="number" data-parsley-required="true"
	               data-parsley-min="0">
	    	
	    </div>  
	 </div>  

    <div class="form-group">
      <label class="col-md-2 control-label">应收金额：</label>
      <div class="col-md-3">
       	<input type="text" class="form-control" name="amount" id="amount"  value="${paymentItem.amount!}" data-parsley-required="true" data-parsley-type="number"  data-parsley-gt="0">
      </div>
      <label class="col-md-2 control-label">实收金额：</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="actuallyAmount" id="actuallyAmount"   value="${paymentItem.actuallyAmount!}" data-parsley-required="true" data-parsley-type="number"  data-parsley-gt="0">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">收费标准：</label>
      <div class="col-md-3">
     	 <input type="text" class="form-control" name="ruleComment" id="ruleComment"  value="${paymentItem.ruleComment!}" readonly="true">
   	 </div>
    </div>
    <div class="form-group">
	    <div class="col-md-offset-2">
		    <div class="col-md-2">
		        <a href="javascript:;" class="btn btn-primary" id="btnCalc" >计费</a>  
		    </div>
		    <div class="col-md-2">
		        <button class="btn btn-primary" type="submit" >保存</button> 
		    </div>
	    </div>
    </div>
    </div>
  </form>
</div>
<script type="text/javascript">
	$(function(){
		$('#btnCalc').click(function(){
			var feeItemId = $('#feeItemId').val();
			var weight = $('#weight').val() ? $('#weight').val():0;
			var amountPiece = $('#amountPiece').val()? $('#amountPiece').val():0;
			var storeContainerCount = $('#storeContainerCount').val()? $('#storeContainerCount').val():0;
			var url = "${base}/finance/payment/${paymentId}/calculate-item?weight=" + weight+ "&amountPiece=" + amountPiece 
									+ "&storeContainerCount=" + storeContainerCount +"&feeitemId=" +feeItemId;
			$.getJSON(url, function(data){
				$('#amount').val(data.amount);
				$('#actuallyAmount').val(data.amount);
				$('#ruleComment').val(data.ruleComment);
			});
		});
	});
</script>
</#escape>
