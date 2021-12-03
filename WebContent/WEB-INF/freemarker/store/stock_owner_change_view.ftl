<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/store/stock-owner-change?customerType=${customerType}&&operationType=${operationType}"> 货权转移<#if customerType==0>卖方<#elseif customerType==1>买方</#if>
  <#if operationType==1>
  结算
  <#elseif operationType==2>
  延迟付款审核
  <#elseif operationType==3>
  付款
  </#if> </a> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-12">
      <h3>单号：${stockOwnerChange.serialNo!}</h3>
      <hr/>
    </div>
    <div class="col-md-2">卖方客户：</div>
    <div class="col-md-4">${stockOwnerChange.seller.name!} &nbsp; </div>
    <div class="col-md-2">买方客户：</div>
    <div class="col-md-4">${stockOwnerChange.buyer.name!} &nbsp; </div>
    <div class="col-md-2">转让时间：</div>
    <div class="col-md-4"><#if stockOwnerChange.changeTime?exists>${stockOwnerChange.changeTime?string("yyyy-MM-dd HH:mm")}</#if> &nbsp;</div>
    <div class="col-md-2">登记时间：</div>
    <div class="col-md-4">${stockOwnerChange.regTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">登记人：</div>
    <div class="col-md-4">${stockOwnerChange.register.name!}&nbsp;</div>
    <div class="col-md-2">状态：</div>
    <div class="col-md-4">${stockOwnerChange.stockOwnerChangeStatus!}&nbsp;</div>
    <#if customerType==0||!operationType?exists>
    <#if stockOwnerChange.sellerPayment?has_content>
    <div class="col-md-2">卖方结算时间：</div>
    <div class="col-md-4"> ${stockOwnerChange.sellerPayment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">卖方结算员：</div>
    <div class="col-md-4">${stockOwnerChange.sellerPayment.settledBy.name!} &nbsp;</div>
    <div class="col-md-2">卖方计费类型：</div>
    <div class="col-md-4">${stockOwnerChange.sellerPayment.chargeType.name!} &nbsp;</div>
    <div class="col-md-2">卖方结算状态：</div>
    <div class="col-md-4">${stockOwnerChange.sellerPayment.paymentStatus!} &nbsp;</div>
    </#if>
    </#if>
    <#if customerType==1||!operationType?exists>
    <#if stockOwnerChange.buyerPayment?has_content>
    <div class="col-md-2">买方结算时间：</div>
    <div class="col-md-4"> ${stockOwnerChange.buyerPayment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">买方结算员：</div>
    <div class="col-md-4">${stockOwnerChange.buyerPayment.settledBy.name!} &nbsp;</div>
    <div class="col-md-2">买方计费类型：</div>
    <div class="col-md-4">${stockOwnerChange.buyerPayment.chargeType.name!} &nbsp;</div>
    <div class="col-md-2">买方结算状态：</div>
    <div class="col-md-4">${stockOwnerChange.buyerPayment.paymentStatus!} &nbsp;</div>
    </#if>
    </#if> </div>
  <br/>
  <br/>
  <#if operationType !=2&&operationType!=3>
  <div class="row">
    <div class="col-md-12">
      <h4>转让商品详情：</h4>
    </div>
    <div class="col-md-12">
      <table class="table table-striped table-advance table-hover">
        <thead>
          <tr>
            <th>#</th>
            <th>商品</th>
            <th>数量</th>
            <th>重量</th>
            <th>规格</th>
            <th>包装</th>
            <th>保质期</th>
            <th>预计保管时间</th>
            <th>生产日期</th>
          </tr>
        </thead>
        <tbody>
        <#assign totalAmount = 0, totalWeight = 0>
        <#if stockOwnerChange.stockOwnerChangeRegisterItems?has_content>
        <#list stockOwnerChange.stockOwnerChangeRegisterItems as item>
        <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
        <tr>
          <td>${item_index + 1}</td>
          <td>${item.product.code!} ${item.product.name!}</td>
          <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
          <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
          <td>${item.spec!}</td>
          <td>${item.packing.name!}</td>
          <td>${item.qualityGuaranteePeriod}</td>
          <td>${item.storeDuration}</td>
          <td><#if item.productionDate?exists>${item.productionDate?string("yyyy-MM-dd HH:mm")}</#if></td>
        </tr>
        </#list>
        </#if>
        <tr>
          <td colspan="10" align="right">转让总数量：${totalAmount}, 转让总重量 ${totalWeight}</td>
        </tr>
        </tbody>
      </table>
      <div> </div>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <h4>转让托盘：</h4>
    </div>
    <div class="col-md-12">
      <table class="table table-striped table-advance table-hover">
        <thead>
          <tr>
            <th>#</th>
            <th>托盘标签</th>
            <th>储位编码</th>
            <th>储位排</th>
            <th>储位列</th>
            <th>储位层</th>
            <#if operationType!=1&&!stockOwnerChange.buyerPayment?has_content&&!stockOwnerChange.sellerPayment?has_content&&stockOwnerChange.stockOwnerChangeStatus!='已取消'&&stockOwnerChange.stockOwnerChangeStatus!="已完成">
            <th>操作</th>
            </#if> </tr>
        </thead>
        <tbody>
        <#if stockOwnerChange.stockOwnerChangeCheckItems?has_content>            
        <#list stockOwnerChange.stockOwnerChangeCheckItems as item>
        <tr>
          <td>${item_index + 1}</td>
          <td>${item.storeContainer.label!} </td>
          <td>${item.storeLocation.code!} </td>
          <td>${item.storeLocation.coordX!} </td>
          <td>${item.storeLocation.coordY!}</td>
          <td>${item.storeLocation.coordZ!}</td>
          <#if operationType!=1&&!stockOwnerChange.buyerPayment?has_content&&!stockOwnerChange.sellerPayment?has_content&&stockOwnerChange.stockOwnerChangeStatus!='已取消'&&stockOwnerChange.stockOwnerChangeStatus!="已完成">
          <td><a href="javascript:void(0)"     onclick="delCheckItem(${item.id})"  >删除</a></td>
          </#if> </tr>
        </#list>
        </#if>
        <tr> </tr>
        </tbody>        
      </table>
    </div>
  </div>
  <#else>
  <div class="row">
    <div class="col-md-12">
      <h4>费用明细：</h4>
    </div>
    <div class="col-md-12">
      <table class="table table-striped table-advance table-hover">
        <thead>
          <tr>
            <th>#</th>
            <th>收费项</th>
            <th>应收</th>
            <th>实收</th>
          </tr>
        </thead>
        <tbody>
        <#assign totalAmount = 0, actuallyAmount = 0>
        <#if customerType==0>
        <#list stockOwnerChange.sellerPayment.paymentItems as item>
        <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
        <tr>
          <td>${item_index + 1}</td>
          <td>${item.feeItem.name!}</td>
          <td>${item.amount?string.currency} </td>
          <td>${item.actuallyAmount?string.currency} </td>
        </tr>
        </#list>
        <#else> 
        <#list stockOwnerChange.buyerPayment.paymentItems as item>
        <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
        <tr>
          <td>${item_index + 1}</td>
          <td>${item.feeItem.name!}</td>
          <td>${item.amount?string.currency} </td>
          <td>${item.actuallyAmount?string.currency} </td>
        </tr>
        </#list>
        </#if>
        <tr>
          <td colspan="10" align="right">应收金额：${totalAmount?string.currency}, 实收金额：${actuallyAmount?string.currency}</td>
        </tr>
        </tbody>        
      </table>
    </div>
  </div>
  </#if>
  <div class="row">
    <div class="col-md-12"> <#if stockOwnerChange.stockOwnerChangeStatus!='已取消'&&stockOwnerChange.stockOwnerChangeStatus!="已完成">      
      <#if operationType ==1> <a href="${base}/store/stock-owner-change/${stockOwnerChange.id!}/settlement?customerType=${customerType}&&operationType=${operationType}" class="btn btn-primary">结算</a> <#elseif operationType==2>
      <form  action="${base}/store/stock-owner-change/${stockOwnerChange.id}/delay" method="post" class="form-horizontal" >
        <input type="hidden" name="paymentStatus"  id="paymentStatus"  value="">
        <input type="hidden" name="customerType"  id="customerType"  value="${customerType}">
        <input type="hidden" name="operationType"  id="operationType"  value="${operationType}">
        <div class="form-group"  >
          <label class="col-md-1 control-label">备注：</label>
          <div class="col-md-6">
            <textarea class="form-control" name="remark" id="remark" rows="2"></textarea>
          </div>
          <div  col-md-1">
            <button type="submit" class="btn btn-primary"   onclick="setval('1')">同意</button>
            <button type="submit" class="btn btn-primary"  onclick="setval('0')">拒绝</button>
          </div>
          <div  col-md-1"> </div>
        </div>
      </form>
      <#elseif operationType==3> <a href="javascript:void(0)"    onclick="payment()"  class="btn btn-primary">付款</a> <#else>
      <#if !stockOwnerChange.sellerPayment?has_content&&!stockOwnerChange.buyerPayment?has_content> <a href="${base}/store/stock-owner-change/${stockOwnerChange.id}/edit" class="btn btn-primary">编辑</a> &nbsp; <a href="${base}/store/stock-owner-change/index-new-item?stockOwnerChangeId=${stockOwnerChange.id}" class="btn btn-primary">选择转让托盘</a> <a href="javascript:void(0)" onclick="cancel()" class="btn btn-primary">取消</a> </#if>
      <#if (stockOwnerChange.sellerPayment.paymentStatus=='已付款'&&stockOwnerChange.buyerPayment.paymentStatus=='已付款')||(stockOwnerChange.sellerPayment.paymentStatus=='延付已生效'&&stockOwnerChange.buyerPayment.paymentStatus=='已付款')||(stockOwnerChange.sellerPayment.paymentStatus=='已付款'&&stockOwnerChange.buyerPayment.paymentStatus=='延付已生效')> <a href="javascript:void(0)"   onclick="complete()" class="btn btn-primary">完成</a> </#if>  
      </#if>
      </#if> </div>
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
      <form action="${base}/store/stock-owner-change/${stockOwnerChange.id!}/payment?customerType=${customerType}&&operationType=${operationType}" method="post" class="form-horizontal"  data-parsley-validate>
        <div id="selector-body" class="modal-body">
          <div class="row">
            <div class="form-group">
              <label class="col-md-3 control-label">会员ID:</label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="custemerId" id="custemerId" value="" data-parsley-required="true">
                <input type="hidden" class="form-control" name="aCardMac" id="aCardMac" value="" data-parsley-required="true">
                <input type="hidden" name="sMemberCode" id="sMemberCode" value="" >
              </div>
              <div class="col-md-1"> <a class="btn btn-info" onclick="getIDCardInfoFunc();"><i class="fa fa-ellipsis-v"></i> 读卡</a> </div>
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
              <label class="col-md-3 control-label">IC卡密码:</label>
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
<script >
	function delCheckItem(id){
		 var result= confirm("请确认删除？");
		 if(result){
		 	location.href = "${base}/store/stock-owner-change/" + id + "/delete-check-item?stockOwnerChangeId=${stockOwnerChange.id!}";
		 }
	}
	function cancel(){
		 var result= confirm("请确认取消？");
		 if(result){
		 	location.href = "${base}/store/stock-owner-change/cancel?id=${stockOwnerChange.id!}";
		 }
	}
	function complete(){
		 var result= confirm("请确认完成？");
		 if(result){
		 	location.href = "${base}/store/stock-owner-change/complete?id=${stockOwnerChange.id!}";
		 }
	}
	
	function payment(){		 
		 	 $('#location-selector-modal-payment').modal('show');
	}
	
	function  setval(value){
	 
		$("#paymentStatus").val(value);
	}
	
	function getIDCardInfoFunc(){		
	var i = ICCard.readCardNo();
	var j = ICCard.readCardMac();
	if(i==-1){
	   alert("读卡会员卡号失败");
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