<#escape x as x?html>
<header class="panel-heading"> 结算单</header>
<div class="panel-body main-content-wrapper site-min-height">
<div class="row">
  <div class="col-md-12">
    <h3> 单号：${payment.serialNo!}
      <div class="pull-right">状态 ${payment.paymentStatus}</div>
    </h3>
    <hr/>
  </div>
  <div class="col-md-2">结算依据：</div>
  <div class="col-md-10">${payment.paymentObjectSerialNo!}&nbsp;</div>
  <div class="col-md-2">客户：</div>
  <div class="col-md-4">${payment.customer.name!} &nbsp;</div>
  <div class="col-md-2">费用类型：</div>
  <div class="col-md-4">${payment.paymentType!} &nbsp;</div>
  <div class="col-md-2">结算时间：</div>
  <div class="col-md-4"> ${payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
  <div class="col-md-2">结算员：</div>
  <div class="col-md-4">${payment.settledBy.name!} &nbsp;</div>
  <div class="col-md-2">计费类型：</div>
  <div class="col-md-4">${payment.chargeType.name!} &nbsp;</div>
  <div class="col-md-2">结算周期：</div>
  <div class="col-md-4">${payment.settleRange!} &nbsp;</div>
</div>
<br/>
<br/>
<div class="row">
  <div class="col-md-12">
    <h4>费用明细： </h4>
  </div>
  <div class="col-md-12">
    <table class="table table-striped table-advance table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th style="text-align:center;">收费项</th>
          <th style="text-align:right;">应收</th>
          <th style="text-align:right;">实收</th>
         </tr>
      </thead>
      <tbody>
      <#assign totalAmount = 0, actuallyAmount = 0>
      <#if payment.paymentItems?has_content>
      <#list payment.paymentItems as item>
      <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
      <tr>
        <td>${item_index + 1}</td>
        <td style="text-align:center;">${item.feeItem.name!}</td>
        <td style="text-align:right;">${item.amount?string.currency} </td>
        <td style="text-align:right;">${item.actuallyAmount?string.currency} </td>
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
 
  
</#escape> 