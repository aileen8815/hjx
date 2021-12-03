<#escape x as x?html>
<header class="panel-heading">
 	<div class="row">
    <div class="col-md-6">
     <#if outboundBooking.bookingStatus!='未审核'>
    <h4><a href="${base}/store/outbound-booking">出库预约</a>
    <#else>
    <h4><a href="${base}/store/outbound-booking/check-index">出库预约审核</a>
    </#if>
    -单号：${outboundBooking.serialNo!}
     <span><strong>[${outboundBooking.bookingStatus!}]</strong></span>
	</h4>
    </div>
    <div class="col-md-6">
    <div class="btn-group pull-right">
       <#if outboundBooking.bookingStatus!='已完成'&&outboundBooking.bookingStatus!='已取消'> 
       	 <#if outboundBooking.bookingStatus!='未审核'>
       		<a href="${base}/store/outbound-booking/${outboundBooking.id}/edit" class="btn btn-primary">编辑预约单</a>  &nbsp; 
       		<#if outboundBooking.bookingStatus=='已受理'>
       		<a href="${base}/store/outbound-register/${outboundBooking.id}/register" class="btn btn-primary">正式登记</a>
       		<a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal" onclick="t1()">延迟预约</a>
       		</#if>
       		<a href="javascript:void(0)" class="btn btn-primary"  onclick="t2()"   data-toggle="modal" data-target="#location-selector-modal">取消预约</a>
         <#else>
         <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal" onclick="t1()">预约单审核</a>
         </#if>
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
   	<div class="col-md-2">客户留言：</div>
    <div class="col-md-4">${outboundBooking.note!} &nbsp; </div>

    <div class="col-md-2">确认人：</div>
    <div class="col-md-4">${outboundBooking.confirmOperator.name!} &nbsp; </div>
    <div class="col-md-2">确认时间：</div>
    <div class="col-md-4">   
     <#if outboundBooking.confirmTime?exists>
   		${outboundBooking.confirmTime?string("yyyy-MM-dd HH:mm")!}
   	</#if> &nbsp; 
   	</div>

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
                   <th>预分派托盘数</th>
                   <th>批次</th>
                   <th>规格</th>
                   <th>包装</th>
                   
                   
                   
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
                    <td>${item.storeContainerCount!}</td>
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
 
  <!-- Modal -->
    <div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" width="50%">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                     <#if outboundBooking.bookingStatus!='未审核'>
               	 	<h4 class="modal-title" id="L1">延迟预约</h4>
                	<h4 class="modal-title" id="L2">取消预约</h4>
                	<#else>
                	<h4 class="modal-title" id="L3">预约审核</h4>
               	 	</#if>
                     
                </div>
                <form action="" method="post" id="storeform" class="form-horizontal"   data-parsley-validate>
                    </br>
                     <input type="hidden" name="confirm"  id="confirm" value="true">
                    <div class="row" id="boundTime">
                        <label class="col-md-2 control-label">延迟时间:</label>
                        <input type="text" class="form-control Wdate" style="width:35%" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00',minDate:'(.now)'})"
                               name="applyOutboundTime" id="applyOutboundTime"
                               value="<#if outboundBooking.applyOutboundTime?exists>${outboundBooking.applyOutboundTime?string('yyyy-MM-dd HH:00')}</#if>"
                               placeholder="申请出库时间" data-parsley-required="true">
                    </div>
                    </br>
                    <div class="row">
                        <label class="col-md-2 control-label">备注:</label>
                        <textarea name="remark" id="remark" rows="2" style="width:60%" class="form-control"></textarea>
                    </div>
                    <div class="modal-footer">
                        <div class="row" id="commonbuttons" <#if outboundBooking.bookingStatus=='未审核'>style="display:none"</#if>>
                    	<button type="submit" class="btn btn-primary">保存</button>
                   		<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
						</div>
						<div class="row" id="checkbuttons"  <#if outboundBooking.bookingStatus!='未审核'>style="display:none"</#if>>
						<button type="submit" class="btn btn-primary"  onclick="setcheck(true)">受理</button>
						<button type="submit" class="btn btn-primary" onclick="setcheck(false)">不受理</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
						</div>

                    </div>
                </form>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    

  <div class="row">
    <div class="col-md-12">
    	
	</div>
  </div>

</div>
<script type="text/javascript">
 
	     function t1() {
            $("#t1").show();
            $("#L1").show();
            $("#L2").hide();
            $("#boundTime").show();
            $("#storeform").attr("action", "${base}/store/outbound-booking/retard?id=${outboundBooking.id!}");
        }
        function t2() {
            $("#t1").hide();
            $("#L2").show();
            $("#L1").hide();
            $("#boundTime").hide();
            $("#storeform").attr("action", "${base}/store/outbound-booking/cancel?id=${outboundBooking.id!}");
        }
        function setcheck(value){
   	 	$("#confirm").val(value);
   	 	$("#storeform").attr("action", "${base}/store/outbound-booking/${outboundBooking.id!}/save-check");
   		}
 </script >
</#escape>
