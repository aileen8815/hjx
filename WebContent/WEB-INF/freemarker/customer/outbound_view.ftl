<#escape x as x?html>
<header class="panel-heading">
 
    <div class="row">
    <div class="col-md-5">
    <h4> 
		 出库单
	</a> - 单号：${outboundRegister.serialNo!}
	<span><strong>[${outboundRegister.stockOutStatus}]</span></strong></h4>
    </div>
    <div class="col-md-7">
    <div class="btn-group pull-right">
  			 
    </div>
    </div>
    </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
   
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
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="10" align="right">预约总数量：${totalAmount}, 总重量 ${totalWeight}</td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
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
      
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundRegister.outboundCheckItems?has_content>            
            <#list outboundRegister.outboundCheckItems as item>
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
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
 
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="12" align="right">总数量：${totalAmount}, 总重量 ${totalWeight}</td>
            </tr>
            </tbody>
        </table>    
    </div>
  </div>
 
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
     
 
 
 

</div>

  
  
 
</#escape>
