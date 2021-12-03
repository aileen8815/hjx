<#escape x as x?html>
<header class="panel-heading">
    
       <div class="row">
        <div class="col-md-6">
            <h4>
             <a href="${base}/finance/payment/account-checking">对账单</a>-单号：${accountChecking.serialNo!}
                
            </h4>
        </div>
        <div class="col-md-6">
            <div class="btn-group pull-right">
              		<#if accountChecking.paymentStatus=='未付款'>
	              		<#if accountChecking.overdueFine>
	                     <a href="javascript:void(0)" class="btn btn-primary"  onclick="overdueFine(false)" >不收取违约金</a>&nbsp; 
						<#else>
						 <a href="javascript:void(0)" class="btn btn-primary"  onclick="overdueFine(true)" >收取违约金</a>&nbsp; 
						</#if>
	         			<a href="javascript:void(0)" class="btn btn-primary"  onclick="payment();" >付款</a>&nbsp; 
	         			<a href="javascript:void(0)" class="btn btn-primary"  onclick="complete()" >账单已付款完成</a>&nbsp; 
         			</#if>
            </div>
        </div>
    </div>
    
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
 
     
    <div class="col-md-2">对账周期：</div>
    <div class="col-md-10">${accountChecking.startTime?if_exists?string("yyyy-MM-dd HH:mm")}&nbsp;至&nbsp;${accountChecking.endTime?if_exists?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    
 
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${accountChecking.customer.name!} &nbsp;</div>

  	<div class="col-md-2">对账时间：</div>
    <div class="col-md-4">${accountChecking.checkingTime?if_exists?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
	<div class="col-md-2">应付款时间：</div>
    <div class="col-md-4">${accountChecking.payTime?if_exists?string('yyyy-MM-dd')}&nbsp;</div>
   	<div class="col-md-2">实际付款时间：</div>
    <div class="col-md-4"><#if accountChecking.actualPayTime?exists>${accountChecking.actualPayTime?if_exists?string('yyyy-MM-dd HH:mm')}</#if>&nbsp;</div>
    
    <div class="col-md-2">是否收取违约金：</div>
    <div class="col-md-4">${accountChecking.overdueFine?string('收取','不收取')} &nbsp;</div>
	<div class="col-md-2">付款状态：</div>
    <div class="col-md-4">${accountChecking.paymentStatus} &nbsp;</div>
    
	<div class="col-md-2"><#if accountChecking.overdueMoney?exists>违约金：</#if></div>
    <div class="col-md-4"><#if accountChecking.overdueMoney?exists><td>${accountChecking.overdueMoney?string.currency}</td></#if>
                     &nbsp;</div>
  </div>
  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>费用明细： </h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                	<th>#</th>
                    <th>收费单号</th>
                    <th>收费项</th>
                    <th>应收</th>
                    <th>实收</th>
                    <th>状态</th>
                    <#if accountChecking.paymentStatus=='未付款'>
          		   		<th>操作</th>
          			</#if>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, actuallyAmount = 0>
            <#if accountChecking.payments?has_content>
            <#list accountChecking.payments as paym>
           	   <#assign  items= paym.paymentItems>
            	<#list items as item>
                <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${paym.serialNo!}</td>
                    <td>${item.feeItem.name!}</td>
                    <td>${item.amount?string.currency} </td>
                    <td>${item.actuallyAmount?string.currency} </td>
                    <td>${item.paymentStatus!} </td>
                    <#if accountChecking.paymentStatus=='未付款'>
        				<td><a href="${base}/finance/payment/${item.id}/edit-account-checking-item?accountCheckingId=${accountChecking.id!}">编辑	</a></td>
        			</#if>
                </tr>
                </#list>
            </#list>
            </#if>
            <tr>
                <td colspan="10" align="right">应收总金额：${totalAmount}, 实收总金额：${actuallyAmount}</td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
  
<!-- Modal -->
<div class="modal fade" id="location-selector-modal-payment" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">付款</h4>
      </div>
      <form action="${base}/finance/payment/${accountChecking.id!}/payment-account" class="form-horizontal"  data-parsley-validate>
        <div id="selector-body" class="modal-body">
          <input type="hidden" class="form-control" name="payments" id="payments" value="" >
          <div class="row">
            <div class="form-group">
              <label class="col-md-2 control-label">卡号:</label>
              <div class="col-md-7">
                <input type="text" class="form-control" name="cardNo" id="cardNo" value="" data-parsley-required="true"  >
              </div>
              <div class="col-md-1"> <a class="btn btn-info" onClick="getIDCardInfoFunc();"><i class="fa fa-ellipsis-v"></i> 读卡</a> </div>
            </div>
          </div>
          <div class="row">
            <div class="form-group">
              <label class="col-md-2 control-label">密码:</label>
              <div class="col-md-7">
                <input type="password" class="form-control" name="password" id="password" value="" data-parsley-required="true" >
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">付款</button>
          <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
        </div>
      </form>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<OBJECT id="ICCard" classid="clsid:186E79AE-FA06-4DFA-B43D-FD1EB3E7DD1C" width=0 height=0>
</OBJECT>
<script type="text/javascript">
    function overdueFine(overduefine) {
        var msg="";
        if(overduefine){
        	msg="确认收取违约金";
        }else{
        	msg="确认不收取违约金";
        }
        if (confirm(msg)) {
            location.href = "${base}/finance/payment/${accountChecking.id}/overduefine?overduefine="+overduefine;
        }
    }
    
  	function  payment(){   
  		var ids=[];  	 
		 $('input:checkbox:checked').each(function() {
            ids.push($(this).val());
        });
  		ids.push("${accountChecking.id!}");
  		$("#payments").val(ids);
  		$('#location-selector-modal-payment').modal('show');
  	}
  	
  	function complete(){
    	if(confirm("请确认是否已结算付款，才可以完成对账单")){
    		location.href="${base}/finance/payment/${accountChecking.id!}/payment-complete";
    	}
    }
    
        
          function getIDCardInfoFunc(){		
	      var i = ICCard.readCardNo();
	      if(i==-1){
	   alert("读卡失败");
	      }else{
	   $("#cardNo").val(i);
	      }       
	    }
 </script>
</#escape>
