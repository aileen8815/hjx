<#escape x as x?html>
<header class="panel-heading">
	<div class="row">
    <div class="col-md-6">
    <h4><a href="${base}/customer/customer-outbound-booking">出库预约</a> - 
	单号：${outboundBooking.serialNo!}
     <span><strong>[${outboundBooking.bookingStatus!}]</strong></span>
	</h4>
    </div>
    <div class="col-md-6">
    <div class="btn-group pull-right">
       <#if outboundBooking.bookingStatus=='未审核'> 
       <a href="${base}/customer/customer-outbound-booking/${outboundBooking.id}/edit" class="btn btn-primary">编辑预约单</a>&nbsp; 
       </#if>  
    </div>
    </div>
    </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-2">预约方式：</div>
    <div class="col-md-4">${outboundBooking.bookingMethod.name!} &nbsp;</div>

    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${outboundBooking.customer.name!} &nbsp; </div>
    <div class="col-md-2">预约仓间：</div>
    <div class="col-md-4">${outboundBooking.storeArea.name!} &nbsp;</div>

    <div class="col-md-2">预约时间：</div>
    <div class="col-md-4">${outboundBooking.bookTime?string("yyyy-MM-dd HH:mm")!} &nbsp; </div>
    <div class="col-md-2">申请出库时间：</div>
    <div class="col-md-4">${outboundBooking.applyOutboundTime?string("yyyy-MM-dd HH:mm")!} &nbsp; </div>
    
    <div class="col-md-2">来车类型：</div>
    <div class="col-md-4">${outboundBooking.vehicleType.name!} &nbsp; </div>
    <div class="col-md-2">来车台数：</div>
    <div class="col-md-4">${outboundBooking.vehicleAmount!} &nbsp; </div>
    
    <div class="col-md-2">车牌号：</div>
    <div class="col-md-4">${outboundBooking.vehicleNumbers!} &nbsp; </div>
    
	<div class="col-md-2">联系人：</div>
    <div class="col-md-4">${outboundBooking.contact.contactinfo!} &nbsp;</div>
    <div  class="col-md-6">&nbsp;</div>
    
    <div class="col-md-2">客户留言：</div>
    <div class="col-md-10">${outboundBooking.note!} &nbsp; </div>
    
    
    <div class="col-md-2">确认人：</div>
    <div class="col-md-4">${outboundBooking.confirmOperator.name!} &nbsp; </div>
	<div class="col-md-2">确认备注：</div>
    <div class="col-md-4">${outboundBooking.confirmRemark!} &nbsp; </div>
  </div>

  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>出库登记单明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>预计重量</th>
                   <th>规格</th>
                   <th>包装</th>
                   <th>批次</th>
                </tr>
            </thead>
            <tbody>
            <#assign totalAmount = 0, totalWeight = 0>
            <#if outboundBooking.outboundBookingItems?has_content>
            <#list outboundBooking.outboundBookingItems as item>
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.product.code!} ${item.product.name!}</td>
                    <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                    <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                    <td>${item.spec!}</td>
                    <td>${item.packing.name!}</td>
                    <td>${item.batchs}</td>
               
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

  

</div>

</#escape>
