<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-freight/">出库发货</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
 <div class="row">
    <div class="col-md-12">
        <h3>发货单号：${outboundFreight.serialNo!}</h3>
        <hr/>
    </div>

    <div class="col-md-2">登记单号：</div>
    <div class="col-md-4">${outboundFreight.outboundRegister.serialNo!} &nbsp;</div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${outboundFreight.customer.name!} &nbsp; </div>

    <div class="col-md-2">发货理货区：</div>
    <div class="col-md-4">${outboundFreight.tallyArea.name!} &nbsp;</div>
    <div class="col-md-2">承运商：</div>
    <div class="col-md-4">${outboundFreight.carrier.fullName!} &nbsp;</div>

	<div class="col-md-2">发货时间：</div>
    <div class="col-md-4">${outboundFreight.freightTime?string("yyyy-MM-dd HH:mm")!} &nbsp; </div>
    <div class="col-md-2">发货人：</div>
    <div class="col-md-4">${outboundFreight.operator.name!} &nbsp; </div>
    
    <div class="col-md-2">车型：</div>
    <div class="col-md-4">${outboundFreight.vehicleType.name!} &nbsp; </div>
    <div class="col-md-2">车牌号：</div>
    <div class="col-md-4">${outboundFreight.vehicleNo!} &nbsp; </div>    
    
    <div class="col-md-2">司机：</div>
    <div class="col-md-4">${outboundFreight.driver!} &nbsp; </div>
    <div class="col-md-2">司机身份证：</div>
    <div class="col-md-4">${outboundFreight.driverIdNo!} &nbsp; </div>
  </div>

  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>&nbsp;&nbsp;出库发货单明细：</h4>
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
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundFreight.outboundFreightItems?has_content>
            <#list outboundFreight.outboundFreightItems as item>
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.product.name!}</td>
                    <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                    <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                    <td>${item.spec!}</td>
                    <td>${item.packing.name!}</td>
                </tr>
            </#list>
            </#if>
            <tr>
                <td colspan="10" align="right">发货总数量：${totalAmount}, 总重量 ${totalWeight}</td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
 
  <div class="row">
    <div class="col-md-12">
       <a href="${base}/store/outbound-freight/${outboundFreight.id}/edit?outboundCheckId=${outboundFreight.outboundRegister.id}" class="btn btn-primary">编辑发货单</a>  &nbsp; 
	</div>
  </div>
 
</div>
</#escape>
