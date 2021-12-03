<#escape x as x?html>
<header class="panel-heading"> 
  <div class="row">
    <div class="col-md-5">
      <h4> 
           入库单
        </a> -
        单号：${inboundRegister.serialNo!} <span><strong>[${inboundRegister.stockInStatus}]</strong></span> </h4>
    </div>
    <div class="col-md-7">
      <div class="btn-group pull-right"> </div>
    </div>
  </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
 
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
</div>
<br/>
 
<div class="row">
  <div class="col-md-12">
    <h4>入库登记单明细： </h4>
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
          <th>预计托盘数</th>
          <th>预计保管时间</th>
          <th>规格</th>
          <th>产地</th>
          <th>生产日期</th>
          <th>保质期</th>
       </tr>
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
        <td>${item.qualityGuaranteePeriod}</td>
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
          <th>预计保管时间</th>
          <th>规格</th>
          <th>保质期</th>
          <th>是否合格</th>
          <th>验货人</th>
          <th>验货时间</th>
          </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, totalWeight = 0>
      <#if inboundRegister.inboundReceiptItems?has_content>
      <#list inboundRegister.inboundReceiptItems as item>
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
        <td>${item.receiptor.name!}</td>
        <td>${item.receiptTime?default(.now)?string('yyyy-MM-dd HH:mm')}</td>
          </tr>
      </#list>
      </#if>
      <tr>
        <td colspan="13" align="right">收货总数量：${totalAmount}, 总重量 ${totalWeight}</td>
      </tr>
      </tbody>
      
    </table>
  </div>
</div>
 
 
   
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
      <div>
       </div>
 
    </div>
  </div>
 
 
 
</#escape> 