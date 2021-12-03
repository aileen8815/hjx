<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-register/${id}">出库单结算</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="inboundReceiptItem-form" action="${base}/store/outbound-register/${id}/charge" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${inboundReceiptItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${inboundReceiptItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-2 control-label">结算方式:</label>
      <#if customer.chargeType.id == 1><label class="col-md-2 control-label">${customer.chargeType.name!}</label></#if>
      <div class="col-md-2"  <#if customer.chargeType.id == 1>style="display:none"</#if>>
        <select id="chargeTypeId" name="chargeTypeId" class="form-control" onchange="selectcharge(this.value)">
            <#list chargeTypes as chargeType>
            <option value="${chargeType.id}"  <#if customer.chargeType.id == chargeType.id>selected</#if>>${chargeType.name}</option>
            </#list>
        </select>
      </div>
   
      <div class="col-md-offset-2 col-md-4"  id="yanchi"    <#if customer.chargeType.id == 2>style="display:none"</#if>>
          <label class="checkbox-inline"><input type="checkbox" id="delayed" name="delayed" value="true"> 延迟缴费</label>
      </div>
    </div>
 	<#list calculatedResults as calculatedResult>
    <div class="form-group">
    <#if "${calculatedResult.feeItem.id!}" == 3 >
		  <label class="col-md-2 control-label">应收${calculatedResult.feeItem.name}：</label>
		  <div class="col-md-2">
		   <input type="hidden" class="form-control" name="feeitem" id="feeitem${calculatedResult_index}"  value="${calculatedResult.feeItem.id!}" data-parsley-required="true" data-parsley-type="number">
		    <input type="text" class="form-control" name="money" id="money${calculatedResult_index}"  value="${calculatedResult.amount!}" data-parsley-required="true" readonly="true" data-parsley-type="number">
		  </div>
		  
		   <label class="col-md-2 control-label">实收${calculatedResult.feeItem.name}：</label>
		  <div class="col-md-2">
		    <input type="text" class="form-control" name="actuallyMoney" id="actuallyMoney${calculatedResult_index}" value="${calculatedResult.discountRate*calculatedResult.amount!}" data-parsley-required="true" data-parsley-type="number">
		  </div>
      <#else>
      	  <label class="col-md-2 control-label">应收${calculatedResult.feeItem.name}：</label>
		  <div class="col-md-2">
		   <input type="hidden" class="form-control" name="feeitem" id="feeitem${calculatedResult_index}"  value="${calculatedResult.feeItem.id!}" data-parsley-required="true" data-parsley-type="number">
		    <input type="text" class="form-control" name="money" id="money${calculatedResult_index}"  value="0" data-parsley-required="true" readonly="true" data-parsley-type="number">
		  </div>
		  
		   <label class="col-md-2 control-label">实收${calculatedResult.feeItem.name}：</label>
		  <div class="col-md-2">
		    <input type="text" class="form-control" name="actuallyMoney" id="actuallyMoney${calculatedResult_index}" value="0" data-parsley-required="true" data-parsley-type="number">
		  </div>
      </#if>
        <input type="text" class="col-md-2 control-label" name="ruleComment" id="ruleComment${calculatedResult_index}"  value="${calculatedResult.ruleComment!}" readonly="true">
	</div>
      
      <div class="form-group">
      <#if "${calculatedResult.feeItem.name!}" != "仓储费">
		<#if "${calculatedResult.ruleComment!}"?contains("托盘")>
      <label class="col-md-2 control-label">托盘（托）：</label>
      <div class="col-md-2">
        <input type="text" class="form-control" name="storeContainerCount" id="storeContainerCount${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="weight" id="weight${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="amountPiece" id="amountPiece${calculatedResult_index}" value="0" data-parsley-type="number">
      </div>
    <#elseif "${calculatedResult.ruleComment!}"?contains("重量")> 
      <label class="col-md-2 control-label">重量（公斤）：</label>
      <div class="col-md-2">
        <input type="hidden" class="form-control" name="storeContainerCount" id="storeContainerCount${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="text" class="form-control" name="weight" id="weight${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="amountPiece" id="amountPiece${calculatedResult_index}" value="0" data-parsley-type="number">
      </div>
     <#elseif "${calculatedResult.ruleComment!}"?contains("数量")> 
      <label class="col-md-2 control-label">数量 （件）：</label>
      <div class="col-md-2">
        <input type="hidden" class="form-control" name="storeContainerCount" id="storeContainerCount${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="weight" id="weight${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="text" class="form-control" name="amountPiece" id="amountPiece${calculatedResult_index}" value="0" data-parsley-type="number">
     </div>
      </#if>
      <#else>
      <input type="hidden" class="form-control" name="storeContainerCount" id="storeContainerCount${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="weight" id="weight${calculatedResult_index}" value="0" data-parsley-type="number">
      	<input type="hidden" class="form-control" name="amountPiece" id="amountPiece${calculatedResult_index}" value="0" data-parsley-type="number">
      </#if>
      <div class="col-md-2">
      <#if "${calculatedResult.feeItem.name!}" != "仓储费">
        <a href="javascript:;" class="btn btn-primary" name="btnCalc"  id="btnCalc${calculatedResult_index}">单项计费</a>
        </#if>
      </div>
      <script type="text/javascript">
      $(function(){
		$('#btnCalc${calculatedResult_index}').click(function(){
			var feeItemId = $('#feeitem${calculatedResult_index}').val();;
			var amountPiece = $('#amountPiece${calculatedResult_index}').val()? $('#amountPiece${calculatedResult_index}').val():0;
			var weight = $('#weight${calculatedResult_index}').val()? $('#weight${calculatedResult_index}').val():0;
			var storeContainerCount = $('#storeContainerCount${calculatedResult_index}').val()? $('#storeContainerCount${calculatedResult_index}').val():0;
			if ((amountPiece == '') && (weight == '') && (storeContainerCount == ''))
			{
				alert("计算单项费用托盘数量或总重量不能为空！");
			}else
			{
			var url = "${base}/store/outbound-register/${id}/calculate-item?feeitemId=" + feeItemId + 
				"&storeContainerCount=" + storeContainerCount + "&weightAmount=" + weight+"&amountPiece=" + amountPiece;
			$.getJSON(url, function(data){
				$('#money${calculatedResult_index}').val(data.amount);
				$('#actuallyMoney${calculatedResult_index}').val(data.amount);
				$('#ruleComment${calculatedResult_index}').val(data.ruleComment);
			});
			}
		});
	});
      </script>
    </div>
    </#list>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-10">
        <button class="btn btn-primary" >保存</button>  
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">
function selectcharge(value){
	if(value==2){
	$("#yanchi").hide();
	$("#delayed").attr("checked",false);
	}else{
	$("#yanchi").show();
	}
	
}
</script>
</#escape>
