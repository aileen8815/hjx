<#escape x as x?html>
<header class="panel-heading">
    <div class="row">
        <div class="col-md-6">
            <h4>
                <a href="${base}/customer/customer-inbound-booking">入库预约</a> -
                	单号：${inboundBooking.serialNo!}
                <span><strong>[${inboundBooking.bookingStatus!}]</strong></span>
            </h4>
        </div>
        <div class="col-md-6">
            <div class="btn-group pull-right">
                <#if inboundBooking.bookingStatus=='未审核'>
                    <a href="${base}/customer/customer-inbound-booking/${inboundBooking.id}/edit" class="btn btn-primary">编辑预约单</a>
                </#if>
            </div>
        </div>
    </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
     	 
        <div class="col-md-2">预约仓间：</div>
        <div class="col-md-4">${inboundBooking.storeArea.name!} &nbsp;</div>
		<div class="col-md-2">预约方式：</div>
        <div class="col-md-4">${inboundBooking.bookingMethod.name!} &nbsp;</div>
       
        <div class="col-md-2">申请入库时间：</div>
        <div class="col-md-4">${inboundBooking.applyInboundTime!}&nbsp;</div>
		<div class="col-md-2">来车类型：</div>
        <div class="col-md-4">${inboundBooking.vehicleType.name!} &nbsp;</div>
        
        <div class="col-md-2">来车台数：</div>
        <div class="col-md-4">${inboundBooking.vehicleAmount!} &nbsp;</div>
		<div class="col-md-2">车牌号：</div>
        <div class="col-md-4">${inboundBooking.vehicleNumbers!} &nbsp; </div>
        
        <div class="col-md-2">联系人：</div>
        <div class="col-md-4">${inboundBooking.contact.contactinfo!} &nbsp;</div>
		<div class="col-md-2">预约时间：</div>
        <div class="col-md-4">${inboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>

        <div class="col-md-2">客户留言：</div>
        <div class="col-md-10">${inboundBooking.note!} &nbsp;</div>
        </div>
  <br/><br/>
  <div class="row">
        <div class="col-md-12">
            <h4>预约商品明细：  <#if inboundBooking.bookingStatus=='未审核'><a href="${base}/customer/customer-inbound-booking/new-item?inboundBookingId=${inboundBooking.id}"><i class="fa fa-plus-square"></i> 新建</a></#if></h4>
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
                    <th>生产日期</th>
                    <th>保质期</th>
                    <th>预计保管时间</th>
                	<#if inboundBooking.bookingStatus=='未审核'>
                    <th>操作</th>
                    </#if>
                </tr>
                </thead>
            <tbody>
                <#if inboundBooking.inboundBookingItems?has_content>
                    <#assign totalAmount = 0, totalWeight = 0>
                    <#list inboundBooking.inboundBookingItems as item>
                        <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                    <tr>
                        <td>${item_index + 1}</td>
                        <td>${item.product.code!} ${item.product.name!}</td>
                        <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                        <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                        <td>${item.spec!}</td>
                        <td>${item.packing.name!}</td>
                        <td> <#if item.productionDate?exists>${item.productionDate?string('yyyy-MM-dd')}</#if></td>
                        <td>${item.qualityGuaranteePeriod}</td>
                        <td>${item.storeDuration}</td>
                       <#if inboundBooking.bookingStatus=='未审核'>
                        <td><a href="javascript:void(0)"   onclick="edit(${item.id})">编辑</a>
                        <a href="javascript:void(0)"  onclick="del(${item.id})">删除</a></td>
                       </#if>
                    </tr>
                    </#list>
                <tr>
                    <td colspan="10" align="right">登记总数量：${totalAmount}, 总重量 ${totalWeight}</td>
                </tr>
                </tbody>
                </#if>
            </table>
            <div>
            </div>

        </div>
    </div>
 
  <script type="text/javascript">
       
        function edit(itemId) {
        	location.href="${base}/customer/customer-inbound-booking/"+itemId+"/edit-item?inboundBookingId=${inboundBooking.id}";
            
        }
        function del(itemId) {
        	if(confirm("确认删除！")){
          	  location.href="${base}/customer/customer-inbound-booking/"+itemId+"/delete-item?inboundBookingId=${inboundBooking.id}";
        	}
        }
    </script>
</#escape>
