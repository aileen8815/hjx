<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract">合同退租</a> 
     
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="payment-form" action="${base}/tenancy/store-contract/save-to-payment" method="post" class="form-horizontal"  data-parsley-validate>
 
	
	  <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">客户:</label>
					<div class="col-md-8">
                        <select id="customerId" name="customer.id" class="form-control" onchange="selectedCustomer(this.value)"  data-parsley-required="true" >
      					<option value="">请选择客户</option>
      				 	<#list customers as customer>
     					<option value="${customer.id}"  <#if customer.id == customerid>selected</#if>>${customer.code}&nbsp;&nbsp;${customer.name}</option>
	  				 	</#list>
      			 </select>
                    </div>
               </div>
       </div>
	
   <div class="col-md-5">
   <div class="form-group">
      <label class="col-md-4 control-label">合同:</label>
      <div class="col-md-8">
      <select id="storecantractId" name="storecantractId" onchange="selectedStoreContract(this.value)"  class="form-control"  data-parsley-required="true">
     	 <option value="" >请选择合同</option>;
     	 <#list storeContracts as storeContract2>
     	 <option value="${storeContract2.id}"  <#if storeContract2.id == storeContract.id>selected</#if> >${storeContract2.contractNo}</option>
	   	 </#list>
       </select>
 
      </div>
    </div>
   </div>
    <div   id="feeitems"   <#if !storeContract.id?exists>style="display:none" </#if>>
     
       <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">签订日期:</label>
					<div class="col-md-8">
                     <label class="control-label">${storeContract.signedDate?default(.now)?string('yyyy-MM-dd HH:mm')}</label>
      			
                    </div>
               </div>
       </div>
       <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">开始时间:</label>
					<div class="col-md-8">
                      <label class="control-label">${storeContract.startDate?default(.now)?string('yyyy-MM-dd HH:mm')}</label>
      		
                    </div>
               </div>
       </div>
       <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">结束时间:</label>
					<div class="col-md-8">
                    <label class="control-label">${storeContract.endDate?default(.now)?string('yyyy-MM-dd HH:mm')}</label>
      			
                    </div>
               </div>
       </div>
       <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">计费起始日期:</label>
					<div class="col-md-8">
                     <label class="control-label">${storeContract.chargeDate?default(.now)?string('yyyy-MM-dd HH:mm')}</label>
      		
                    </div>
               </div>
       </div>
     <#list storeContractFeeItems  as storeContractFeeItem>
     <div class="col-md-5">
     <div class="form-group">
      <label class="col-md-4 control-label">${storeContractFeeItem.feeItem.name}:</label>
      <div class="col-md-8">
       	<input type="hidden" class="form-control" name="feeitem" value="${storeContractFeeItem.feeItem.id}"  >
        <input type="text" class="form-control ttt" name="money" value="0" data-parsley-required="true"  data-parsley-type="number"  data-parsley-min="0">
	  </div>
    	</div>
    	</div>
   	</#list>
      </div>
       <div class="col-md-10">
    <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
		<button type="button"   onclick="testSubmit()"  class="btn btn-primary">保存</button>
      </div>
    </div>
    </div>
  </form>
</div>

<script>
	function testSubmit(){
		var total = 0;
		$(".ttt").each(function(){
				total = total + Number($(this).val());
			});
		if (total <= 0) {
				alert('退租退费合计为零，不能退租退费。');
				return false;
		}
		if(confirm('退租退费费用合计' + total + '元，确认退租吗？') ){
			$("#payment-form").submit();
		}
		
	}

	function selectedCustomer(value) {
        if (value == '') {
            $("#storecantractId").empty();
            return;            
        }
       
       	$("#feeitems").hide();
        $("#storecantractId").empty();
        $.ajax({
            url: "${base}/tenancy/store-contract/select-customer",
            data: {customerId:value},
            type: "POST",
            success: function (data) {
                var ar = eval('(' + data + ')');
                 $("#storecantractId").append("<option value=>请选择合同</option>");
            	 for (var i = 0; i < ar.length; i++) {
             			var info='<option value='+ar[i].id+'>'+ar[i].contractNo+'</option>'	
             			 
               			 $("#storecantractId").append(info);
            	}
            }
        })
		 
    }
		function selectedStoreContract(value) {
				var customerId=$("#customerId").val(); 
        	  	location.href = "${base}/tenancy/store-contract/select-store-contract?storeContractId="+value+"&&customerId="+customerId;
   	 	}

 
</script>

</#escape>
