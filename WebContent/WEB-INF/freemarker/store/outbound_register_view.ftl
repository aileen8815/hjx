<#escape x as x?html>
<header class="panel-heading">
     <#include "/print/lodop.ftl">
    <div class="row">
    <div class="col-md-5">
    <h4><a href="${base}/store/outbound-register?operationType=${operationType}">
	 <#if operationType==1>
	 	结算
	 <#elseif operationType==2>
		 延迟付款审核
	 <#elseif operationType==3>
	 	付款
	 <#else>
		 出库单
	 </#if>

	</a> - 单号：${outboundRegister.serialNo!}
	<span><strong>[${outboundRegister.stockOutStatus}]</span></strong></h4>
    </div>
    <div class="col-md-7">
    <div class="btn-group pull-right">
  			<#if outboundRegister.stockOutStatus!='已完成'&&outboundRegister.stockOutStatus!='已作废'>
      		<#if outboundRegister.stockOutStatus=='已派送'>
      		<a href="${base}/store/outbound-register/${outboundRegister.id}/edit" class="btn btn-primary">编辑出库单</a>
      		
       		</#if>
       		<#if outboundRegister.outboundRegisterItems?has_content&&outboundRegister.stockOutStatus!='已清点'&&outboundRegister.stockOutStatus!='已拣货'>
       			<a href="javascript:void(0)" class="btn btn-primary"  data-toggle="modal" data-target="#location-selector-modal">分配装卸口</a>
       			<#if outboundRegister.outboundTarrys?has_content>
       				<a href="${base}/store/outbound-register/index-new-item?registerId=${outboundRegister.id}" class="btn btn-primary">列表分派出库托盘</a>
       				<a href="javascript:void(0)" class="btn btn-primary"  data-toggle="modal" data-target="#selector-location-modal">图形分派出库托盘</a>
       			</#if>
 			</#if>
 			<#if outboundRegister.outboundRegisterItems?has_content && outboundRegister.stockOutStatus=='已清点'>
      				<a id="outboundPrint" href="javascript:;"   onclick="print(this)"  ref="${base}/print/outbound-register?id=${outboundRegister.id!}" class="btn btn-primary">打印出库单</a>&nbsp;
       		</#if>
 		<#if outboundRegister.stockOuts?has_content&&outboundRegister.stockOutStatus!='已清点'>
       				<#if outboundRegister.outboundCheckItems?has_content>
       				<a href="javascript:void(0)"  onclick="queren()" class="btn btn-primary">清点完成</a>
 					</#if>
 		</#if>
       	<#if outboundRegister.payment.paymentStatus=="已付款"||outboundRegister.payment.paymentStatus=='延付已生效'&&operationType==0>
        	<a href="${base}/store/outbound-freight/new?outboundRegisterId=${outboundRegister.id}" class="btn btn-primary">出库发货</a>
        	<a href="${base}/store/outbound-register/complete?id=${outboundRegister.id!}" class="btn btn-primary">完成</a>
        </#if>
        
       <#if operationType==1>
         <a href="${base}/store/outbound-register/${outboundRegister.id!}/settlement" class="btn btn-primary">结算</a>
       <#elseif operationType==3>       
         <a href="javascript:void(0)"  onclick="payment()" class="btn btn-primary">付款</a>
       </#if>
       </#if>
       <#if outboundRegister.payment?has_content>
	       <a id="outboundPayment" href="javascript:;"   onclick="print(this)" ref="${base}/print/outbound-payment?id=${outboundRegister.id!}" class="btn btn-primary">缴费单打印</a>&nbsp;
	   </#if>
      	
       <#if outboundRegister.stockOutStatus=='已作废'>
         <a href="${base}/store/outbound-register/${outboundRegister.id!}/start-using" class="btn btn-primary">重新启用</a>
       </#if>
    </div>
    </div>
    </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
   <#if newbooking>
	<div class="alert alert-success fade in">
      <button data-dismiss="alert" class="close close-sm" type="button">
      <i class="fa fa-times"></i>
      </button>
      <strong></strong> 预约单有未登记商品，是否需要把未登记商品生成新的预约单？&nbsp;&nbsp;&nbsp;<a href="${base}/store/outbound-booking/new?outboundRegisterId=${outboundRegister.id}" class="btn btn-primary">确认</a>
	</div>
   </#if>
  <div class="row">

    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${outboundRegister.customer.name!} &nbsp; </div>
    <div class="col-md-2">预约单号：</div>
    <div class="col-md-4"><#if outboundRegister.outboundBooking?has_content>${outboundRegister.outboundBooking.serialNo!} <#else>无预约</#if></div>
   
	

    <div class="col-md-2">出库类型：</div>
    <div class="col-md-4">${outboundRegister.outboundType!} &nbsp; </div>
  
   
    <div class="col-md-2">出库时间：</div>
    <div class="col-md-4"><#if outboundRegister.outboundTime?exists>${outboundRegister.outboundTime?string("yyyy-MM-dd HH:mm")}</#if> &nbsp;</div>
   	<div class="col-md-2">来车类型：</div>
    <div class="col-md-4">${outboundRegister.vehicleType.name!} &nbsp; </div>
    
    <div class="col-md-2">来车台数：</div>
    <div class="col-md-4">${outboundRegister.vehicleAmount!} &nbsp; </div>
    <div class="col-md-2">车牌号：</div>
    <div class="col-md-10">${outboundRegister.vehicleNumbers!} &nbsp; </div>
    
    <div class="col-md-2">车辆来源：</div>
    <div class="col-md-4">${outboundRegister.vehicleSource!} &nbsp; </div>
    <div class="col-md-2">是否携带委托书：</div>
    <div class="col-md-4"> ${outboundRegister.haveProxy?string('携带','未携带')} &nbsp; </div>
	
	<div class="col-md-2">登记时间：</div>
    <div class="col-md-4">${outboundRegister.registerTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">登记人：</div>
    <div class="col-md-4">${outboundRegister.registerOperator.name!}&nbsp;</div>
	
	<#if outboundRegister.payment?has_content>
  	<div class="col-md-2">结算时间：</div>
    <div class="col-md-4">  ${outboundRegister.payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">结算员：</div>
    <div class="col-md-4">${outboundRegister.payment.settledBy.name!} &nbsp;</div>
    
    <div class="col-md-2">计费类型：</div>
    <div class="col-md-4">${outboundRegister.payment.chargeType.name!} &nbsp;</div>
    <div class="col-md-2">结算状态：</div>
    <div class="col-md-4">${outboundRegister.payment.paymentStatus!} &nbsp;</div>
    </#if> 
    <div class="col-md-2">装卸口：</div>
    <div class="col-md-4">
	  <#list outboundRegister.outboundTarrys as outboundTarry>
            ${outboundTarry.tallyArea.name!}  &nbsp;&nbsp;
       </#list>
	 &nbsp;</div>    
 
  <div class="col-md-2">手持机标识：</div> 
    <div class="col-md-4">
	  <#list outboundRegister.outboundTarrys as outboundTarry>
            ${outboundTarry.handsetAddress!}  &nbsp;&nbsp;
       </#list>
	 &nbsp;</div>    
  </div>

  <br/><br/>
  
  <#if operationType!=2&&operationType!=3>
  <div class="row">
    <div class="col-md-12">
        <h4>&nbsp;&nbsp;出库登记单明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>重量</th>
                   <th>批次</th>
                   <th>规格</th>
                   <th>包装</th>
                   <th>多品明细</th>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundRegister.outboundRegisterItems?has_content>
            <#list outboundRegister.outboundRegisterItems as item>
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.product.name!}</td>
                    <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                    <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                   	<td>${item.batchs!}</td>
                    <td>${item.spec!}</td>
                    <td>${item.packing.name!}</td>
                    <td>${item.productDetail!}</td>
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="10" align="right">预约总数量：${totalAmount},  预约总重量 ${totalWeight} </td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
        <h4>出库拣货单：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>拣货单号</th>
                   <th>装卸口</th>
                   <th>托盘标签</th>
                   <th>储位编码</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>重量</th>
                   <th>规格</th>
                   <th>包装</th>
                   <th>上架时间</th>
                   <th>拣货人</th>
                   <th>拣货状态</th>          
                   <th>操作</th>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundRegister.stockOuts?has_content>            
            <#list outboundRegister.stockOuts as stockOut>
                <#--<#assign item = stockOut.outboundRegisterItem>-->
                <#if stockOut.stockOutStatus !='已作废' >
                <#assign totalAmount = totalAmount + stockOut.amount, totalWeight = totalWeight + stockOut.weight>
                </#if>
                <tr>
                    <td>${stockOut_index + 1}</td>
                    <td>${stockOut.serialNo!}</td>
                    <td>${stockOut.tallyArea.name!}</td>
                    <td>${stockOut.storeContainer.label!}</td>
                    <td>${stockOut.storeLocation.code!}</td>
                    <td>${stockOut.product.name!}</td>
                    <td>${stockOut.amount!} ${stockOut.amountMeasureUnit.name!}</td>
                    <td>${stockOut.weight!} ${stockOut.weightMeasureUnit.name!}</td>
                    <td>${stockOut.spec!}</td>
                    <td>${stockOut.packing.name!}</td>
                    <td>${stockOut.stockInTime?string('yyyy-MM-dd')}</td>
                    <td>${stockOut.stockOutOperator.name!}</td>
                    <td>${stockOut.stockOutStatus!}</td>
                    <td>
                    <#if stockOut.stockOutStatus=='已拣货' && outboundRegister.stockOutStatus!='已清点'>
	                    <#if outboundCheckItems?has_content>            
	                    <#assign checked = 0>
	            			<#list outboundCheckItems as item>
		            			<#if stockOut.storeContainer.label == item.storeContainer.label>
		            			 <#assign checked = 1>
		            			</#if>
	            			</#list>
	            			<#if checked == 1>
	            				<a>验货完毕</a>
	            			<#else>
	            				<a href="${base}/store/outbound-check/new-item?stockOutId=${stockOut.id}">出库检验</a>
	            			</#if>
          				<#else>
                   			<a href="${base}/store/outbound-check/new-item?stockOutId=${stockOut.id}">出库检验</a>
                   		</#if>
                    </#if>&nbsp;
                    <#if stockOut.stockOutStatus=='待拣货' >
                    	<a href="${base}/store/outbound-register/del-stockout?stockoutId=${stockOut.id}&outboundRegisterId=${outboundRegister.id}">作废</a>
                    </#if>
 		   			</td>
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="14" align="right">拣货总数量：${totalAmount},  拣货总重量 ${totalWeight} </td>
            </tr>
            </tbody>
        </table>    
    </div>
  </div>
  
  <div class="row">
    <div class="col-md-12">
        <h4>已拣货汇总：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>商品</th>
                   <th>总数量</th>
                   <th>总重量</th>
                   <th>总托数</th>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0, totalContainerCount = 0>
            <#if outboundRegister.stockOuts?has_content>            
            <#list outItemTotal as stockOut>
                <#assign totalAmount = totalAmount + stockOut.AMOUNT, totalWeight = totalWeight + stockOut.WEIGHT, totalContainerCount = totalContainerCount + stockOut.CONTAINERCOUNT>
                <tr>
                    <td>${stockOut_index + 1}</td>
                    <td>${stockOut.PRODUCTNAME!}</td>
                    <td>${stockOut.AMOUNT!} ${stockOut.amountMeasureUnit.name!}</td>
                    <td>${stockOut.WEIGHT!} ${stockOut.weightMeasureUnit.name!}</td>
                    <td>${stockOut.CONTAINERCOUNT!}</td>
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="14" align="right">拣货总数量：${totalAmount},  拣货总重量： ${totalWeight},  拣货总托数： ${totalContainerCount} </td>
            </tr>
            </tbody>
        </table>    
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
        <h4>出库检验明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>托盘号</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>重量</th>
                   <th>规格</th>
                   <th>包装</th>
                   <th>验货人</th>
                   <th>验货时间</th>
                   <th>操作</th>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundCheckItems?has_content>            
            <#list outboundCheckItems as item>
            	<#if stockOut.stockOutStatus !='已作废' >
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                </#if>
                <tr>
                    <td>${item_index + 1}</td>                  
                    <td>${item.storeContainer.label!}</td>
                    <td>${item.product.name!}</td>
                    <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                    <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>  
                    <td>${item.spec!}</td>
                    <td>${item.packing.name!}</td>
                    <td>${item.checkOperator.name!}</td>
                    <td>${item.checkTime?default(.now)?string('yyyy-MM-dd HH:mm')}</td>
                    <td>
                    <#if outboundRegister.stockOutStatus!='已清点' >
                    <a href="${base}//store/outbound-check/${item.id}/delete-item?outboundRegisterId=${outboundRegister.id}">删除</a>
                    </#if>
                    </td>
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="12" align="right">检验总数量：${totalAmount},  检验总重量 ${totalWeight} </td>
            </tr>
            </tbody>
        </table>    
    </div>
  </div>
  <#else>
  <div class="row">
    <div class="col-md-12">
        <h4>出库费用明细：</h4>
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
            <#if outboundRegister.payment.paymentItems?has_content>
            <#list outboundRegister.payment.paymentItems as item>
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
  <#if operationType==2>
   <div class="row"  id="t2" >
    <form  action="${base}/store/outbound-register/${outboundRegister.id}/delay" method="get" class="form-horizontal" >
	<input type="hidden" name="paymentStatus"  id="paymentStatus"  value="">
 	<div class="col-md-12  ">
 	 <div class="form-group"  >
      <label class="col-md-1 control-label">备注：</label>
      <div class="col-md-6">
       <textarea class="form-control" name="remark" id="remark" rows="2"></textarea>
      </div>
      <div  col-md-1">
        <button type="submit" class="btn btn-primary"   onclick="setval('1')">同意</button>     <button type="submit" class="btn btn-primary"  onclick="setval('0')">拒绝</button>
      </div>
       <div  col-md-1">
  
      </div>
    </div> 
  </div>
 </form>
 </div>
</#if>
 <!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">请选择装卸口和手持机</h4>
      </div>
        <form  action="${base}/store/outbound-register/${outboundRegister.id!}/save-tallyArea" method="post" class="form-horizontal" >
        <div id="tallyArea-body" class="modal-body">
         <#--<#list tallyAreas as tallyArea>
     	 <label class="checkbox-inline"><input type="checkbox"   name="tallyAreas" value="${tallyArea.id}"   <#list outboundRegister.outboundTarrys as outboundTarry> <#if outboundTarry.tallyArea.id==tallyArea.id>checked</#if></#list> >${tallyArea.name} </label>
	     </#list>-->
	     
			<div class="row">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label class="col-md-4 control-label">装卸口:</label>

                            <div class="col-md-8">
                                <select id="tallyAreaId" name="tallyAreaId"  data-parsley-required="true" class="form-control">
                                    <option value="">请选择装卸口</option>
                                    <#list tallyAreas as tallyArea>
                                        <option value="${tallyArea.id}"
                                        <#list outboundRegister.outboundTarrys as outboundTarry>
                                                              <#if outboundTarry.tallyArea.id==tallyArea.id>selected</#if></#list>
                                        >${tallyArea.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-5">
                        <div class="form-group">
                            <label class="col-md-4 control-label">手持机:</label>
                            <div class="col-md-8">
                                <select id="handSetId" name="handSetId"  data-parsley-required="true" class="form-control">
                                    <option value="">请选择手持机</option>
                                    <#list handSets as handSet>
                                        <option value="${handSet.id}"
                                                 <#list outboundRegister.outboundTarrys as outboundTarry>
                                                              <#if outboundTarry.handsetAddress==handSet.mac>selected</#if></#list>>${handSet.name} (任务数${inboundTarry[handSet.mac]+outboundTarry[handSet.mac]})
										</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>	     
         </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-primary" >保存</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal" >关闭</button>

      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
  <div class="row">
    <div class="col-md-12">

   
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
            <form action="${base}/store/outbound-register/${outboundRegister.id!}/payment" method="post" class="form-horizontal"  data-parsley-validate>
                <div id="payment-body" class="modal-body">
                    
                  
      <div class="row">
      <div class="form-group">
        <label class="col-md-3 control-label">会员ID:</label>
        <div class="col-md-5">
          <input type="text" class="form-control" name="custemerId" id="custemerId" value="" data-parsley-required="true"  >
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
        <div class="col-md-5"> <input type="password" class="form-control" name="password" id="password" value="" data-parsley-required="true" > </div>
      </div>
      </div>
      			
      		</div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary"   onclick="colse()">付款</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->


<!--选择储位返回托盘modal-->
<!-- Modal -->
<div class="modal fade" id="selector-location-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog-full modal-location-selector">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择储位</h4>
            </div>
             <form class="form-inline"  action="${base}/store/outbound-register/save-item"   method="get">
            <div class="modal-body" id="selector-body">
            </div>
            <div class="modal-footer">
		 		<div style="text-align:left">
		          <input type="hidden" name="registerId" value="${outboundRegister.id}">
		         <textarea   style="display:none" class="form-control" name="ids" id="ids" rows="5"></textarea>
		         	 <div class="form-group">
 					  
		      		</div>
		      	
		        </div>
           		<button type="submit" class="btn btn-primary">保存</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                </form>
            </div>
            
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div><!-- /.modal -->

    <OBJECT id="ICCard" classid="clsid:186E79AE-FA06-4DFA-B43D-FD1EB3E7DD1C" width=0 height=0>
    </OBJECT>
     
  <script type="text/javascript">
      $.get('${base}/store/store-location-selector?customerId=${outboundRegister.customer.id}&allowSelectStatus=2&&showStock=true&&selectableStoreContainer=${selectableStoreContainer}', function (data) {
            $("#selector-body").html(data);
        });
       function locationNodeClick() {
            var result = $('#location-result');

            var ref = $(this).attr('ref');
            var rev = $(this).attr('rev');
		  if ($(result).text().indexOf(rev+",") == -1) {
              $(result).append(rev+",");
              $(this).parent().addClass('stat-selected');
          } else {
              var str="";
             if($(result).text().indexOf(",")!=-1){
                 str=$(result).text().replace(rev+",","");
             }
             $(this).parent().removeClass('stat-selected');
             $(result).text(str);
          }
          
          if ($("#ids").val().indexOf(ref+',') == -1) {
             var str=$("#ids").val()+ref+",";
             $("#ids").val(str)
          } else {
              var str="";
             if($("#ids").val().indexOf(",")!=-1){
                 str=$("#ids").val().replace(ref+",","");
             }
             $("#ids").val(str);
            
          }
           
        }
      
	function  setval(value){
		$("#paymentStatus").val(value);
  	}
  	function   queren(){
  		       $.ajax({
                    url: "${base}/store/outbound-register/${outboundRegister.id}/confirm-complete-check",
                    type: "get",
                    contentType: "application/json; charset=utf-8",
 					 dataType: "json",
                    success: function (data) {
                   
                    	 if(data.error==1){
                    	 	if(confirm(data.message)){
                    	 		location.href="${base}/store/outbound-register/${outboundRegister.id}/complete-check";
                    	 	}
                    	 }else if(data.error==0){
                    	 		location.href="${base}/store/outbound-register/${outboundRegister.id}/complete-check";
                    	 }
                    	 
                    }
                })
  	}
  	
  	function  payment(){
   	  $('#location-selector-modal-payment').modal('show');
    }
  	function  print(button){
      				var printurl = $(button).attr("ref");
      				$.get(printurl, function(data){
      					LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
      					LODOP.SET_PRINT_PAGESIZE(1,2400,1400,0);
      					LODOP.ADD_PRINT_HTM(25,0,2400,1400, data);
      					if($(button).attr("id")=='outboundPrint'){
      						LODOP.ADD_PRINT_BARCODE(25,510,"7.01cm","1.19cm","","${serialNo}");
      					}
      					LODOP.PREVIEW();
      				});
      				
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
 </script>
</#escape>
