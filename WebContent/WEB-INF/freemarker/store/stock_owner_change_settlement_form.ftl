<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-owner-change/index-payment?customerType=${customerType}"><#if customerType==0>卖方客户<#else> 买方客户</#if>货权转移结算</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="inboundReceiptItem-form" action="${base}/store/stock-owner-change/${id}/charge" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="customerType" value="${customerType}">
    <input type="hidden" name="operationType" value="${operationType}">
    <div class="form-group">
      <label class="col-md-2 control-label">结算方式:</label>
      <#if customer.chargeType.id == 1><label class="col-md-2 control-label">${customer.chargeType.name!}</label></#if>
      <div class="col-md-4"  <#if customer.chargeType.id == 1>style="display:none"</#if>>
        <select id="chargeTypeId" name="chargeTypeId" class="form-control"  onchange="selectcharge(this.value)">
            <#list chargeTypes as chargeType>
            <option value="${chargeType.id}"  <#if customer.chargeType.id == chargeType.id>selected</#if>>${chargeType.name}</option>
            </#list>
        </select>
      </div>
      <div class="col-md-offset-2 col-md-4"  id="yanchi" <#if customer.chargeType.id == 2>style="display:none"</#if>>
          <label class="checkbox-inline"><input type="checkbox" id="delayed" name="delayed" value="true"> 延迟缴费</label>
        </div>
    </div>
    <#list calculatedResults as calculatedResult>
    <div class="form-group">
      <label class="col-md-2 control-label">应收${calculatedResult.feeItem.name}：</label>
      <div class="col-md-4">
       <input type="hidden" class="form-control" name="feeitem"   value="${calculatedResult.feeItem.id!}" data-parsley-required="true" data-parsley-type="number">
        <input type="text" class="form-control" name="money"   value="${calculatedResult.amount!}" data-parsley-required="true" data-parsley-type="number">
      </div>
      
       <label class="col-md-2 control-label">实收${calculatedResult.feeItem.name}：</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="actuallyMoney"   value="${calculatedResult.discountRate*calculatedResult.amount!}" data-parsley-required="true" data-parsley-type="number">
      </div>
    </div>
    </#list>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-10">
        <button class="btn btn-primary" >保存</button>  
      </div>
    </div>
  </form>
</div>
<script>
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
