<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract/index-fee-item?serialNo=${serialNo}">周期费用</a>
    <#if contractFeeItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeContractFeeItem-form" action="${base}/tenancy/store-contract/${storeContractFeeItem.id!}/save-fee-item" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeContractFeeItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeContractFeeItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
    <input type="hidden" class="form-control" name="storeContract.id" id="storeContract.id" value="${storeContractFeeItem.storeContract.id}" >

    <div class="form-group">
      <label class="col-md-1 control-label">收费项目:</label>
      <div class="col-md-3">
      <select id="feeItemId" name="feeItem.id"  class="form-control">       
        <#list feeItems as feeItem>
     	  <option value="${feeItem.id}" <#if feeItem.id == storeContractFeeItem.feeItem.id>selected</#if>>${feeItem.name}</option>
	    </#list>
      </select>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">收费金额:</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="amount" id="amount" value="${storeContractFeeItem.amount}" data-parsley-required="true" data-parsley-type="number" data-parsley-gt="0">
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">收费周期:</label>
      <div class="col-md-2">
        <input type="text" class="form-control" name="period" id="period" value="${storeContractFeeItem.period}" data-parsley-required="true" data-parsley-gt="0">
      </div>
      <label id="unitLabel" class="col-md-1 control-label">单位：月份</label>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">操作类别:</label>
      <div class="col-md-3">
      <select id="operateType" name="operateType"  class="form-control">           
        <#list operateTypes as operateType>
     	  <option value="${operateType}" <#if operateType == storeContractFeeItem.operateType>selected</#if>>${operateType}</option>                 
        </#list>
      </select>        
      </div>
    </div>
      
    <div class="form-group">
      <div class="col-md-offset-1 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>      
      </div>
    </div>
  </form>
</div>

<script type="text/javascript">

	var feeitemjs = {
		save: function() {	
			var a = $('#amount').val();
	    	if (a <= 0) {
	      		alert("请输入金额");
	      	} else {
	          	location.href = "${base}/tenancy/store-contract/${storeContractFeeItem.id!}/save-fee-item";   	      	
	      	}
		}
	}
	
	function setUnitLabel(){
		var feeItemId = $('#feeItemId').val();
		if(feeItemId == 3){
			$('#unitLabel').text('单位: 天');
		}else{
			$('#unitLabel').text('单位: 月份');
		}
	}
	
	$("#feeItemId").bind("change",function(){
		setUnitLabel();
	});
	
	setUnitLabel();
</script>    

</#escape>
