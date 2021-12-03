<#escape x as x?html>
<header class="panel-heading"> <#include "/print/lodop.ftl">
  <div class="row">
    <div class="col-md-5">
      <h4> <a href="${base}/store/inbound-register?operationType=${operationType}"> <#if operationType==1>
        结算
        <#elseif operationType==2>
        延迟付款审核
        <#elseif operationType==3>
        付款
        <#elseif operationType==4>
        预分配储位
        <#else>
        入库单
        </#if> </a> -
        单号：${inboundRegister.serialNo!} <span><strong>[${inboundRegister.stockInStatus}]</strong></span> </h4>
    </div>
    <div class="col-md-7">
      <div class="btn-group pull-right"> <#if inboundRegister.stockInStatus!='已完成'&&inboundRegister.stockInStatus!='已作废'>
        <#if operationType==0>
        <#if inboundRegister.stockInStatus=='已上架' || inboundRegister.stockInStatus=='已清点'> <a href="javascript:void(0)"    onclick="complete()"  class="btn btn-primary">已付款完成入库</a></#if>
        <#if inboundRegister.stockInStatus=='已派送'> <a href="${base}/store/inbound-register/${inboundRegister.id!}/edit" class="btn btn-primary">编辑单据</a> <#if inboundRegister.inboundRegisterItems?has_content> <a id="inboundPrint" href="javascript:;" onclick="print(this)" ref="${base}/print/inbound-register?id=${inboundRegister.id!}" class="btn btn-primary">打印登记单</a>&nbsp;
        </#if>
        </#if>
        <#if inboundRegister.stockInStatus=='已清点' || inboundRegister.stockInStatus=='已上架' || inboundRegister.stockInStatus=='上架中'> <a id="inboundPrint" href="javascript:;" onclick="print(this)" ref="${base}/print/inbound-receipt?id=${inboundRegister.id!}" class="btn btn-primary">打印收货单</a>
        </#if>
        <#if inboundRegister.inboundRegisterItems?has_content&&inboundRegister.stockInStatus!='已清点'&&inboundRegister.stockInStatus!='上架中'&&inboundRegister.stockInStatus!='已上架'>
        <a id="inboundPrintLabel"  onclick="printLabel()" class="btn btn-primary">打印标签</a>
        <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal">分派任务</a> 
        </#if>
		 <#if inboundRegister.inboundRegisterItems?has_content&&inboundRegister.stockInStatus!='已清点'&&inboundRegister.stockInStatus!='已上架'>
	         <#if inboundRegister.inboundTarrys?has_content>
		        <#if inboundRegister.inboundReceiptItems?has_content> <a href="${base}/store/inbound-register/${inboundRegister.id!}/complete-receipt" class="btn btn-primary">清点完成</a>
		        </#if>
	        </#if>
        </#if>
        
        <#elseif operationType==1> <a href="${base}/store/inbound-register/${inboundRegister.id!}/settlement" class="btn btn-primary">结算</a>&nbsp;
        <#elseif operationType==3> <a href="javascript:void(0)" onclick="payment()" class="btn btn-primary">付款</a> <#--<#elseif operationType==4> <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal2">分配储位</a>-->
        </#if>        
        </#if>
        <#if inboundRegister.payment?has_content> 
        	<a id="inboundPayment" href="javascript:;" onclick="print(this)" ref="${base}/print/inbound-payment?id=${inboundRegister.id!}" class="btn btn-primary">付款单打印</a>&nbsp;
        </#if>
        <#if inboundRegister.stockInStatus=='已作废'> <a href="${base}/store/inbound-register/${inboundRegister.id!}/start-using" class="btn btn-primary">重新启用</a> </#if> </div>
    </div>
  </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<#if newbooking>
<div class="alert alert-success fade in">
  <button data-dismiss="alert" class="close close-sm" type="button"> <i class="fa fa-times"></i> </button>
  <strong></strong> 预约单有未登记商品，是否需要把未登记商品生成新的预约单？&nbsp;&nbsp;&nbsp;<a href="${base}/store/inbound-booking/new-difference?inboundRegisterId=${inboundRegister.id}"
                                                                               class="btn btn-primary">确认</a> </div>
</#if>
<div class="row">
  <div class="col-md-2">客户：</div>
  <div class="col-md-4">${inboundRegister.customer.name!}</div>
  <div class="col-md-2">预约单号：</div>
  <div class="col-md-4"><#if inboundRegister.inboundBooking?has_content>${inboundRegister.inboundBooking.serialNo!}<#else>无预约</#if></div>
  <div class="col-md-2">入库类型：</div>
  <div class="col-md-4">${inboundRegister.inboundType!}</div>
  <div class="col-md-2">入库时间：</div>
  <div class="col-md-4">${inboundRegister.inboundTime?string("yyyy-MM-dd HH:mm:ss")}</div>
  <div class="col-md-2">来车类型：</div>
  <div class="col-md-4">${inboundRegister.vehicleType.name!} &nbsp;</div>
  <div class="col-md-2">来车台数：</div>
  <div class="col-md-4">${inboundRegister.vehicleAmount!} &nbsp;</div>
  <div class="col-md-2">车牌号：</div>
  <div class="col-md-10">${inboundRegister.vehicleNumbers!} &nbsp; </div>
  <div class="col-md-2">登记人：</div>
  <div class="col-md-4">${inboundRegister.registerOperator.name!}&nbsp;</div>
  <div class="col-md-2">登记时间：</div>
  <div class="col-md-4">${inboundRegister.registerTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
  <#if inboundRegister.inboundTarrys?has_content>
  <div class="col-md-2">装卸口：</div>
  <div class="col-md-4"> <#list inboundRegister.inboundTarrys as inboundTarry>
    ${inboundTarry.tallyArea.name!}  &nbsp;&nbsp;
    </#list>
    &nbsp; </div>
  </#if>
  
  <#if inboundRegister.inboundTarrys?has_content>
  <div class="col-md-2">手持机标识：</div>
  <div class="col-md-4"> <#list inboundRegister.inboundTarrys as inboundTarry>
    ${inboundTarry.handsetAddress!}  &nbsp;&nbsp;
    </#list>
    &nbsp; </div>
  </#if>
  
  <#if inboundRegister.payment?has_content>
  <div class="col-md-2">结算时间：</div>
  <div class="col-md-4"> ${inboundRegister.payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
  <div class="col-md-2">结算员：</div>
  <div class="col-md-4">${inboundRegister.payment.settledBy.name!} &nbsp;</div>
  <div class="col-md-2">计费类型：</div>
  <div class="col-md-4">${inboundRegister.payment.chargeType.name!}&nbsp;</div>
  <div class="col-md-2">结算状态</div>
  <div class="col-md-4">${inboundRegister.payment.paymentStatus!}&nbsp; </div>
  </#if>
  
  
  
  <#--<#if inboundRegister.preStoreLocations?has_content>
  <div class="col-md-2">预分派储位：</div>
  <div class="col-md-10"> <#list inboundRegister.preStoreLocations as location>
    ${location.storeArea.name!} ${location.code!} &nbsp;&nbsp;
    </#list> </div>
  </#if>
  --> </div>
<br/>
<#if operationType!=2&&operationType!=3>
<div class="row">
  <div class="col-md-12">
    <h4>入库登记单明细：  <#if inboundRegister.stockInStatus=='已派送'><a href="${base}/store/inbound-register/new-item?inboundRegisterId=${inboundRegister.id}"><i class="fa fa-plus-square"></i> 新建</a></#if></h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th>商品</th>
          <th>数量</th>
          <th>重量</th>
          <th>包装</th>
          <th>仓间</th>
          <th>预计托盘</th>
          <th>预管时间</th>
          <th>规格</th>
          <th>产地</th>
          <th>生产日期</th>
          <th>多品备注</th>
          <#if inboundRegister.stockInStatus=='已派送'>
          <th>操作</th>
          </#if> </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, totalWeight = 0>
      <#if inboundRegister.inboundRegisterItems?has_content>
      <#list inboundRegister.inboundRegisterItems as item>
      <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
      <tr>
        <td>${item_index + 1}</td>
        <td>${item.product.name!}</td>
        <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
        <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
        <td>${item.packing.name!}</td>
        <td>${item.storeArea.code!}${item.storeArea.name!}</td>
        <td>${item.storeContainerCount!}</td>
        <td>${item.storeDuration}</td>
        <td>${item.spec!}</td>
        <td>${item.productionPlace!}</td>
        <td>${item.productionDate}</td>
        <td>${item.productDetail}</td>
        
        <td>
        <#if inboundRegister.stockInStatus=='已派送'||inboundRegister.stockInStatus=='上架中'>
        	<#if inboundRegister.inboundTarrys?has_content>
        	<a href="${base}/store/inbound-receipt/new-item?inboundRegisterItemId=${item.id!}">验货</a> 
        	</#if>
        	<#if inboundRegister.stockInStatus=='已派送'>
        	<a href="${base}/store/inbound-register/${item.id}/edit-item?inboundRegisterId=${inboundRegister.id}">编辑</a> 
        	<a href="javascript:void(0)" onclick="del(${item.id})">删除</a> 
        	</#if>
        </#if>
        <#if inboundRegister.stockInStatus !='已完成' && inboundRegister.stockInStatus !='已作废'>
        	<a href="${base}/store/inbound-register/${item.id}/edit-product-detail?inboundRegisterId=${inboundRegister.id}">多品录入</a> 
        </#if>
        </td>        
		</tr>
      </#list>
      </#if>
      <tr>
        <td colspan="13" align="right">登记总数量：${totalAmount}, 总重量 ${totalWeight}</td>
      </tr>
      </tbody>
      
    </table>
    <div> </div>
  </div>
</div>
<div class="row">
  <div class="col-md-12">
    <h4>入库收货检验明细：</h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th>商品</th>
          <th>数量</th>
          <th>重量</th>
          <th>托盘标签</th>
          <th>包装</th>
          <th>预管时间</th>	
          <th>规格</th>
          <th>保质期</th>
          <th>是否合格</th>
          <th>多品备注</th>
          <th>验货人</th>
          <th>验货时间</th>
          <#if inboundRegister.stockInStatus=='已派送'>
          <th>操作</th>
          </#if> </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, totalWeight = 0>
      <#if inboundReceiptItems?has_content>
      <#list inboundReceiptItems as item>
      <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
      <tr>
        <td>${item_index + 1}</td>
        <td>${item.product.name!}</td>
        <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
        <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
        <td>${item.storeContainer.label!}</td>
        <td>${item.packing.name!}</td>
        <td>${item.storeDuration}</td>
        <td>${item.spec!}</td>
        <td>${item.qualityGuaranteePeriod}</td>
        <td><#if !item.qualified>不</#if>合格</td>
        <td>${item.productDetail}</td>
        <td>${item.receiptor.name!}</td>
        <td>${item.receiptTime?default(.now)?string('yyyy-MM-dd HH:mm')}</td>
        <#if inboundRegister.stockInStatus=='已派送'>
        <td><a href="${base}/store/inbound-receipt/${item.id}/delete-item?inboundRegisterId=${inboundRegister.id}">删除</a></td>
        </#if> </tr>
      </#list>
      </#if>
      <tr>
        <td colspan="14" align="right">收货总数量：${totalAmount}, 总重量 ${totalWeight}</td>
      </tr>
      </tbody>
      
    </table>
  </div>
</div>
<#if inboundRegister.stockIns?has_content>
<div class="row">
  <div class="col-md-12">
    <h4>入库上架单：</h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th>上架单号</th>
          <th>商品</th>
          <th>数量</th>
          <th>重量</th>
          <th>托盘标签</th>
          <th>上架操作员</th>
          <th>上架储位编码</th>
          <th>上架状态</th>
        </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, totalWeight = 0>
      <#list stockIns as stockIn>
      <#assign totalAmount = totalAmount + stockIn.inboundReceiptItem.amount, totalWeight = totalWeight + stockIn.inboundReceiptItem.weight>
      <tr>
        <td>${stockIn_index + 1}</td>
        <td>${stockIn.serialNo!}</td>
        <td>${stockIn.inboundReceiptItem.product.name!}</td>
        <td>${stockIn.inboundReceiptItem.amount!} ${stockIn.inboundReceiptItem.amountMeasureUnit.name!}</td>
        <td>${stockIn.inboundReceiptItem.weight!} ${stockIn.inboundReceiptItem.weightMeasureUnit.name!}</td>
        <td>${stockIn.inboundReceiptItem.storeContainer.label!}</td>
        <td>${stockIn.stockInOperator.name!} </td>
        <td>${stockIn.storeLocation.code!}</td>
        <td>${stockIn.stockInStatus!}</td>
      </tr>
      </#list>
      <tr>
        <td colspan="10" align="right">上架总数量：${totalAmount}, 总重量 ${totalWeight}</td>
      </tr>
      </tbody>
      
    </table>
    <div> </div>
  </div>
  </#if>
  
  <#else>
  <div class="row">
    <div class="col-md-12">
      <h4>入库费用明细：</h4>
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
        <#if inboundRegister.payment.paymentItems?has_content>
        <#list inboundRegister.payment.paymentItems as item>
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
      <div> </div>
    </div>
  </div>
  </#if>
  <#if operationType==2>
  <div class="row" id="t2">
    <form action="${base}/store/inbound-register/${inboundRegister.id}/delay" method="get" class="form-horizontal">
      <input type="hidden" name="paymentStatusType" id="paymentStatus" value="">
      <div class="col-md-12  ">
        <div class="form-group">
          <label class="col-md-1 control-label">备注：</label>
          <div class="col-md-6">
            <textarea class="form-control" name="remark" id="remark" rows="2"></textarea>
          </div>
          <div col-md-1
                        ">
            <button type="submit" class="btn btn-primary" onclick="setval('1')">同意</button>
            <button type="submit" class="btn btn-primary" onclick="setval('0')">拒绝</button>
          </div>
          <div col-md-1
                    "> </div>
        </div>
      </div>
    </form>
  </div>
  </#if> </div>
<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">请选择装卸口和手持机</h4>
      </div>
      <form action="${base}/store/inbound-register/${inboundRegister.id!}/save-tallyArea" method="post" class="form-horizontal" data-parsley-validate>
        <div id="selector-body" class="modal-body"> <#--<#list tallyAreas as tallyArea>
          <label class="checkbox-inline"><input type="checkbox" name="tallyAreas"
                                                          value="${tallyArea.id}"   <#list inboundRegister.inboundTarrys as inboundTarry>
          <#if inboundTarry.tallyArea.id==tallyArea.id>checked</#if></#list> >${tallyArea.name} </label>
          </#list>  -->
          <div class="row">
            <div class="col-md-12">
              <div class="form-group">
                <label class="col-md-3 control-label">装卸口:</label>
                <div class="col-md-7">
                  <select id="tallyAreaId" name="tallyAreaId" data-parsley-required="true" class="form-control">
                    <option value="">选择装卸口</option>
                    <#list tallyAreas as tallyArea> <option value="${tallyArea.id}"
                                                <#list inboundRegister.inboundTarrys as inboundTarry> <#if inboundTarry.tallyArea.id==tallyArea.id>selected</#if></#list> >${tallyArea.name}
                    </option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-md-12">
              <div class="form-group">
                <label class="col-md-3 control-label">手持机:</label>
                <div class="col-md-7">
                  <select id="handSetId" name="handSetId" data-parsley-required="true" class="form-control">
                    <option value="">请选择手持机</option>
                    <#list handSets as handSet> <option value="${handSet.id}"
                                                <#list inboundRegister.inboundTarrys as inboundTarry> <#if inboundTarry.handsetAddress==handSet.mac>selected</#if></#list> >${handSet.name} (任务数${inboundTarry[handSet.mac]+outboundTarry[handSet.mac]})
                    </option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">保存</button>
          <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
        </div>
      </form>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- Modal -->
<div class="modal fade" id="location-selector-modal-payment" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">付款</h4>
      </div>
      <form action="${base}/store/inbound-register/${inboundRegister.id!}/payment" method="post" class="form-horizontal" data-parsley-validate>
        <div id="selector-body" class="modal-body">
          <div class="row">
            <div class="form-group">
              <label class="col-md-3 control-label">会员ID:</label>
              <div class="col-md-5">
           	    <input type="text" class="form-control" name="custemerId" id="custemerId" value="" data-parsley-required="true">
                <input type="hidden" class="form-control" name="aCardMac" id="aCardMac" value="" data-parsley-required="true">
                <input type="hidden" name="sMemberCode" id="sMemberCode" value="" >
              </div>
              <div class="col-md-1"><a class="btn btn-info" onclick="getIDCardInfoFunc();"><i class="fa fa-ellipsis-v"></i> 读卡</a></div>
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
                <input type="password" class="form-control" name="password" id="password" value="" data-parsley-required="true">
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary"  onclick="colse()">付款</button>
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
    <#--  $.get('${base}/store/store-location-selector?selectedLocationCode=${locationCodes}', function(data){
          $("#selector-body2").html(data);
      });
      function locationNodeClick() {
          var result = $('#location-result');
          var ref = $(this).attr('ref');
          if ($(result).text().indexOf(ref) == -1) {
              $(result).append(ref+",");
              $(this).parent().addClass('stat-selected');
          } else {
              var str="";
             if($(result).text().indexOf(",")!=-1){
                 str=$(result).text().replace(ref+",","");
             }
             $(this).parent().removeClass('stat-selected');
             $(result).text(str);
          }

          $('#locationCodes').val(result.text());

      }-->


    function del(itemId) {
        if (confirm("确认删除！")) {
            location.href = "${base}/store/inbound-register/" + itemId + "/delete-item?inboundRegisterId=${inboundRegister.id}";
        }
    }
    function setval(value) {
        $("#paymentStatus").val(value);
    }

    function payment() {
            $('#location-selector-modal-payment').modal('show');
    }

    function print(button) {
        var printurl = $(button).attr("ref");
        $.get(printurl, function (data) {
            LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            LODOP.SET_PRINT_PAGESIZE(1,2400,1400,0);
            LODOP.ADD_PRINT_HTM(25,0,2400,1400, data);
            if ($(button).attr("id") == 'inboundPrint') {
                LODOP.ADD_PRINT_BARCODE(25, 510, "7cm", "1.19cm", "", "${serialNo}");
            }

            LODOP.PREVIEW();
        });

    }
    function getIDCardInfoFunc() {
    
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
    
    function complete(){
    	if(confirm("请确认是否已结算付款，才可以完成入库单")){
    		location.href="${base}/store/inbound-register/complete?id=${inboundRegister.id!}";
    	}
    }
    
    function colse(){
  		$('#location-selector-modal-payment').modal('hide')
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
  	
 <!-- 测试TSC -->
 
	function printLabel(){
		$.ajax({  
			async : true,  
			cache:false,  
			type: 'get', 
			dataType : "text",   
			url: "${base}/store/inbound-register/items?serialNo="+'${inboundRegister.serialNo}',
			error: function () {//请求失败处理函数  
			    alert('打印标签请求失败');  
			},  
			success:function(data){ //请求成功后处理函数。    
				var inboundRegisterItems = eval('(' + data + ')');
				var TSCObj;
				var number=0;
				var productName ='';
				
				for(var i=0;i<inboundRegisterItems.length;i++){
				TSCObj = new ActiveXObject("TSCActiveX.TSCLIB");
						TSCObj.ActiveXopenport ("TSC TTP-243E Pro");
						TSCObj.ActiveXsetup ("45","35","3","5","0","5","0");
						
					for(var j=1;j<=inboundRegisterItems[i].storeContainerCount;j++){
						productName = inboundRegisterItems[i].product.name;
						TSCObj.ActiveXclearbuffer();
						TSCObj.ActiveXwindowsfont(20, 40, 25, 0, 3, 0, "宋体", "${inboundRegister.registerTime?string("yyyy-MM-dd")}");
						TSCObj.ActiveXwindowsfont(20, 70, 25, 0, 3, 0, "宋体", "${inboundRegister.customer.name}"+ " " +"农通");
						TSCObj.ActiveXwindowsfont(20, 100, 25, 0, 3, 0, "宋体", inboundRegisterItems[i].product.name);
						
						if(inboundRegisterItems[i].productionPlace == null){
							TSCObj.ActiveXwindowsfont(20, 130, 20, 0, 3, 0, "宋体", "产地:");
						}else
						{
							TSCObj.ActiveXwindowsfont(20, 130, 20, 0, 3, 0, "宋体", "产地:"+inboundRegisterItems[i].productionPlace);
						}
						if(inboundRegisterItems[i].productionPlace == null){
							TSCObj.ActiveXwindowsfont(20, 160, 20, 0, 3, 0, "宋体", "生产日期:");
						}else
						{
							TSCObj.ActiveXwindowsfont(20, 160, 20, 0, 3, 0, "宋体", "生产日期:"+formatterDate(inboundRegisterItems[i].productionDate));
						}
						
						TSCObj.ActiveXwindowsfont(20, 190, 20, 0, 3, 0, "宋体", "件数 "+" "+inboundRegisterItems[i].product.bearingCapacity+"");
						TSCObj.ActiveXwindowsfont(130, 190, 20, 0, 3, 0, "宋体", " 重量 "+" "+(inboundRegisterItems[i].product.bearingCapacity * inboundRegisterItems[i].product.weight).toFixed(2)+" Kg");
						TSCObj.ActiveXwindowsfont(160, 230, 20, 0, 3, 0, "宋体", j);
						TSCObj.ActiveXprintlabel ("1","1");
						number=j;
					}
						TSCObj.ActiveXclearbuffer();
						TSCObj.ActiveXwindowsfont(20, 40, 25, 0, 3, 0, "宋体", "${inboundRegister.registerTime?string("yyyy-MM-dd")}");
						TSCObj.ActiveXwindowsfont(20, 70, 25, 0, 3, 0, "宋体", "${inboundRegister.customer.name}"+ "  " +"农通");
						TSCObj.ActiveXwindowsfont(20, 100, 25, 0, 3, 0, "宋体", inboundRegisterItems[i].product.name+"");
						if(inboundRegisterItems[i].productionPlace == null){
							TSCObj.ActiveXwindowsfont(20, 130, 20, 0, 3, 0, "宋体", "产地:");
						}else
						{
							TSCObj.ActiveXwindowsfont(20, 130, 20, 0, 3, 0, "宋体", "产地:"+inboundRegisterItems[i].productionPlace);
						}
						if(inboundRegisterItems[i].productionPlace == null){
							TSCObj.ActiveXwindowsfont(20, 160, 20, 0, 3, 0, "宋体", "生产日期:");
						}else
						{
							TSCObj.ActiveXwindowsfont(20, 160, 20, 0, 3, 0, "宋体", "生产日期:"+formatterDate(inboundRegisterItems[i].productionDate));
						}
						TSCObj.ActiveXwindowsfont(20, 190, 20, 0, 3, 0, "宋体", "件数 "+" "+"");
						TSCObj.ActiveXwindowsfont(130, 190, 20, 0, 3, 0, "宋体", " 重量 "+" "+"     "+"Kg");
						TSCObj.ActiveXwindowsfont(160, 230, 20, 0, 3, 0, "宋体", number+1);
						TSCObj.ActiveXprintlabel ("1","1");
				}
				TSCObj.ActiveXcloseport();
			}
			
		});
	}
	
	function formatterDate(value) {
    if(value ==null)
    {
    return value;
    }else
    {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
   		dateFormat(date, 'yyyy-mm-dd');
   		
        return date.format('yyyy-mm-dd');
        }
    }
</script>
</#escape> 