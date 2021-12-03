<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract">租赁合同</a>
</header>

<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
  
    <div class="col-md-12">
        <h3>
            单号：${storeContract.serialNo!}
            <div id="contractStatus" class="pull-right">状态 ${storeContract.status!}</div>
        </h3>
        <hr/>
    </div>

    <div class="col-md-2">合同编号：</div>
    <div class="col-md-10">${storeContract.contractNo!}</div>

    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${storeContract.customer.name!}</div>

    <div class="col-md-2">开始日期：</div>
    <div class="col-md-4">${storeContract.startDate?string("yyyy-MM-dd")!}</div>
    
    <div class="col-md-2">结束日期：</div>
    <div class="col-md-4">${storeContract.endDate?string("yyyy-MM-dd")!}</div>

    <div class="col-md-2">计费日期：</div>
    <div class="col-md-4">${storeContract.chargeDate?string("yyyy-MM-dd")!}</div>
    
    <div class="col-md-2">签订日期：</div>
    <div class="col-md-4">${storeContract.signedDate?string("yyyy-MM-dd")!}</div>

    <div class="col-md-2">备注：</div>
    <div class="col-md-4">${storeContract.remark!}</div>
  </div>
  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>固定费用明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
        		    <th data-options="width:50,fixed:true">#序号</th>
				   	<th data-options="width:50,fixed:true">收费项目</th>
				   	<th data-options="width:50,fixed:true">收费金额</th>
				 
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
         <#if paymentItems?has_content>
            <#list paymentItems as item>
					<#assign totalAmount = totalAmount + item.amount>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.feeItem.code!} ${item.feeItem.name!}</td>
                    <td>${item.amount!}</td>
                </tr>
            </#list>
       </#if>
             <tr>
                <td colspan="10" align="right">总金额：${totalAmount}</td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
 <a href="javascript:void(0);" onClick="payment();" class="btn btn-primary">付款</a>
<!-- Modal -->
<div class="modal fade" id="location-selector-modal-payment" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">付款</h4>
      </div>
      <form action="${base}/tenancy/store-contract/${paymentId!}/payment" method="post" class="form-horizontal"  data-parsley-validate>
        <div id="selector-body" class="modal-body">
          <input type="hidden" class="form-control" name="payments" id="payments" value="" >
          <div class="row">
            <div class="form-group">
              <label class="col-md-2 control-label">会员ID:</label>
              <div class="col-md-7">
                <input type="text" class="form-control" name="custemerId" id="custemerId" value="" data-parsley-required="true">
                <input type="hidden" class="form-control" name="aCardMac" id="aCardMac" value="" data-parsley-required="true">
                <input type="hidden" name="sMemberCode" id="sMemberCode" value="" >
              </div>
              <div class="col-md-1"> <a class="btn btn-info" onClick="getIDCardInfoFunc();"><i class="fa fa-ellipsis-v"></i> 读卡</a> </div>
            </div>
          </div>
          <div class="row">
            <div class="form-group">
              <label class="col-md-3 control-label">客户名称:</label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="name" id="name" value="" data-parsley-required="true" readonly="true">
              </div>
            </div>
          </div>
          <div class="row">
            <div class="form-group">
              <label class="col-md-2 control-label">IC卡密码:</label>
              
              <div class="col-md-5">
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
	function  payment(){   
  		var ids=[];  	 
  		$('#location-selector-modal-payment').modal('show');
  	}
  	
  	function getIDCardInfoFunc(){		
	var i = ICCard.readCardNo();
	var j = ICCard.readCardMac();
	if(i==-1){
	   alert("读卡失败");
	}else{
	   $("#sMemberCode").val(i);
	}       
	if(j==-1){
	   alert("读卡物理卡号失败");
	}else{
		$("#aCardMac").val(j);
	}
	if((i != -1) && (j != -1)){
		getCustomerMessage();
	}
	}
	
	function getCustomerMessage()
	{
		var cardMac = $("#aCardMac").val();
		var sMemberCode = $("#sMemberCode").val();
		
		if((sMemberCode.length != 10))
		{
	 	   $("#sMemberCode").val('');
		}
		
		if((cardMac.trim() ==''))
		{
			alert("物理卡号不能为空，请检查");
			return false;
		}
		
		$.ajax({
	        url: "${base}/settings/customer/get-customer-Message?cardMac=" + cardMac+"&sMemberCode="+sMemberCode,
	        data: '',
	        type: "get",
	        success: function (data) {
	            if (data == '')
	            {
	            	alert("1.此卡在冷库没有注册，无客户资料；2.请检查一体化平台是否连接正常");
			        return false;
	            }
	            else
	            {
	                var ar = eval('(' + data + ')');            
	                $("#name").val(ar.name);
	                $("#custemerId").val(ar.custemerId);
	            }
	        }
	       })
	       return true;
	}
   
</script>    

</#escape>
