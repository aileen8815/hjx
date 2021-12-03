<#escape x as x?html>
<header class="panel-heading"> <#include "/print/lodop.ftl"> <#if payment.paymentStatus=='延付待审核'> <a href="${base}/finance/payment/index-delay">延迟付款审核</a> <#else> <a href="${base}/finance/payment">费用</a> </#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
<div class="row">
  <div class="col-md-12">
    <h3> 单号：${payment.serialNo!}
      <div class="pull-right">状态 ${payment.paymentStatus}</div>
      <div class="pull-right">
      	<#if payment.paymentItems?has_content>
      		<a id="paymentFee" href="javascript:;" onclick="print(this)" ref="${base}/print/payment-fee?id=${payment.id!}" class="btn btn-primary">缴费单打印</a>&nbsp;
   		</#if>
   	  </div>
    </h3>
    <hr/>
  </div>
  <div class="col-md-2">结算依据：</div>
  <div class="col-md-10">${payment.paymentObjectSerialNo!}&nbsp;</div>
  <div class="col-md-2">客户：</div>
  <div class="col-md-4">${payment.customer.name!} &nbsp;</div>
  <div class="col-md-2">出入库方式：</div>
  <div class="col-md-4">${payment.paymentBoundType!} &nbsp;</div>
  <div class="col-md-2">费用类型：</div>
  <div class="col-md-4">${payment.paymentType!} &nbsp;</div>
  <div class="col-md-2">商品名称：</div>
  <div class="col-md-4">${payment.product.name!} &nbsp;</div>
  <div class="col-md-2">结算时间：</div>
  <div class="col-md-4"> ${payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
  <div class="col-md-2">结算员：</div>
  <div class="col-md-4">${payment.settledBy.name!} &nbsp;</div>
  <div class="col-md-2">计费类型：</div>
  <div class="col-md-4">${payment.chargeType.name!} &nbsp;</div>
</div>
<br/>
<br/>
<div class="row">
  <div class="col-md-12">
    <h4>费用明细： <#if payment.temporary==1&&payment.paymentStatus=='未付款'><a href="${base}/finance/payment/new-item?paymentId=${payment.id}"><i class="fa fa-plus-square"></i> 新建</a></#if></h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th style="text-align:center;">收费项</th>
          <th style="text-align:center;">数量（件）</th>
          <th style="text-align:center;">重量（公斤）</th>
          <th style="text-align:center;">托盘（托）</th>
          <th style="text-align:right;">应收</th>
          <th style="text-align:right;">实收</th>
          <#if payment.temporary==1&&payment.paymentStatus=='未付款'>
          <th style="text-align:center;">操作</th>
          </#if> </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, actuallyAmount = 0>
      <#if payment.paymentItems?has_content>
      <#list payment.paymentItems as item>
      <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
      <tr>
        <td>${item_index + 1}</td>
        <td style="text-align:center;">${item.feeItem.name!}</td>
        <td style="text-align:center;">${item.amountPiece!}</td>
        <td style="text-align:center;">${item.weight!}</td>
        <td style="text-align:center;">${item.storeContainerCount!}</td>
        <td style="text-align:right;">￥${item.amount?string('0.00')} </td>
        <td style="text-align:right;">￥${item.actuallyAmount?string('0.00')} </td>
        <#if payment.temporary==1&&payment.paymentStatus=='未付款'>
        <th  style="text-align:center;"> <a href="${base}/finance/payment/${item.id}/edit-item">编辑</a> </th>
        </#if> </tr>
      </#list>
      </#if>
      <tr>
        <td colspan="10" align="right">应收金额：￥${totalAmount?string('0.00')}, 实收金额：￥${actuallyAmount?string('0.00')}</td>
      </tr>
      </tbody>
    </table>
    <div> </div>
  </div>
</div>
<#if paymentlist?has_content>

<div class="row">
  <div class="col-md-12">
    <h4>延付已生效列表：</h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th>付款</th>
          <th>单号</th>
          <th>应收总金额</th>
          <th>实收总金额</th>
          <th>结算员</th>
          <th>结算日期</th>
        </tr>
      </thead>
      <tbody>
      <#list paymentlist as payment>
      <#assign totalAmount = 0, actuallyAmount = 0>
      <#if payment.paymentItems?has_content>
      <#list payment.paymentItems as item>
      <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
      </#list>
      </#if>
      <tr>
        <td>${payment_index + 1}</td>
        <td><label class="checkbox-inline">
          <input type="checkbox"   name="paymentId" value="${payment.id}">
          </label></td>
        <td>${payment.serialNo!}</td>
        <td>${totalAmount?string.currency!} </td>
        <td>${actuallyAmount?string.currency!} </td>
        <td>${payment.settledBy.name!} </td>
        <td>${payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')} </td>
      </tr>
      </#list>
      </tbody>
    </table>
  </div>
</div>
</#if>
<div class="row" id="fukuan">
  <!-- 由于sql原因，不能点击撤回付款，直接点击【收款单据】进入开单
   <div class="col-md-12"> <#if payment.paymentStatus=='已付款'> <a href="javascript:void(0);" onClick="CancelPayment();" class="btn btn-primary">撤回付款</a> </#if> -->
    <#if payment.temporary==1&&payment.paymentStatus=='未付款'> <a href="${base}/finance/payment/${payment.id}/edit"   class="btn btn-primary">编辑</a> </#if>
    <#if payment.paymentItems?has_content>
    <#if payment.paymentStatus=='未付款'||payment.paymentStatus=='延付已拒绝'||payment.paymentStatus=='延付已生效'> <a href="javascript:void(0);" onClick="payment();" class="btn btn-primary">付款</a> </#if>
    </#if> </div>
</div>
<#if payment.paymentStatus=='延付待审核'>
<div class="row"  id="t2" >
  <form  action="${base}/finance/payment/${payment.id!}/delay" method="get" class="form-horizontal" >
    <input type="hidden" name="paymentStatusType"  id="paymentStatus"  value="">
    <div class="col-md-12  ">
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
    </div>
  </form>
</div>
</#if>
<!-- Modal -->
<div class="modal fade" id="location-selector-modal-payment" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">付款</h4>
      </div>
      <form action="${base}/finance/payment/${payment.id!}/payment" method="post" class="form-horizontal"  data-parsley-validate>
        <div id="selector-body" class="modal-body">
          <input type="hidden" class="form-control" name="payments" id="payments" value="" >
          <div class="row">
            <div class="form-group">
              <label class="col-md-3 control-label">会员ID:</label>
              <div class="col-md-5">
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
              <label class="col-md-3 control-label">IC卡密码:</label>
              <div class="col-md-5">
                <input type="password" class="form-control" name="password" id="password" value="" data-parsley-required="true" >
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" onclick="colse()">付款</button>
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
	function  setval(value){
		$("#paymentStatus").val(value);
  	}
  	function  payment(){   
  		var ids=[];  	 
		 $('input:checkbox:checked').each(function() {
            ids.push($(this).val());
        });
  		ids.push("${payment.id!}");
  		$("#payments").val(ids);
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
	
   function  CancelPayment(){
    if(confirm("确认撤回付款！")){
  	 location.href = "${base}/finance/payment/${payment.id!}/cancel-payment";
  	}  		
  	}
  	function colse(){
  		$('#location-selector-modal-payment').modal('hide')
  		$('#fukuan').hide();
  		alert("正在向一体化平台请求付款，等待返回结果，勿跳转页面，点击【确定】按钮关闭提示信息！");
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
	
	function print(button) {
        var printurl = $(button).attr("ref");
        $.get(printurl, function (data) {
            LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            LODOP.SET_PRINT_PAGESIZE(1,2410,1400,0);
            LODOP.ADD_PRINT_HTM(25, 0, 2400, 1400, data);

            LODOP.PREVIEW();
        });

    }
 </script>
</#escape> 