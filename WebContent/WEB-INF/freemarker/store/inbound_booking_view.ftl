<#escape x as x?html>
<header class="panel-heading">
    <div class="row">
        <div class="col-md-6">
            <h4>
            <#if inboundBooking.bookingStatus!='未审核'>
                <a href="${base}/store/inbound-booking">入库预约</a>
            <#else>
                 <a href="${base}/store/inbound-booking/check-index">入库预约审核</a>
            </#if>
                -单号：${inboundBooking.serialNo!}
                <span><strong>[${inboundBooking.bookingStatus!}]</strong></span>
            </h4>
        </div>
        <div class="col-md-6">
            <div class="btn-group pull-right">
               <#if inboundBooking.bookingStatus!='已完成'&&inboundBooking.bookingStatus!='已取消'>
                  <#if inboundBooking.bookingStatus!='未审核'>
                    <a href="${base}/store/inbound-booking/${inboundBooking.id}/edit" class="btn btn-primary">编辑预约单</a>

                    <#if inboundBooking.bookingStatus=='已受理'>
                        <a href="${base}/store/inbound-register/${inboundBooking.id}/register" class="btn btn-primary">正式登记</a>
                        <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal" onclick="t1()">延迟预约</a>
                    </#if>

                    <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal" onclick="t2()">取消预约</a>
				  <#else>
				  	<a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#location-selector-modal" onclick="t1()">预约单审核</a>
				  </#if>

                </#if>
            </div>
        </div>
    </div>
</header>

<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}">
<div class="panel-body main-content-wrapper site-min-height">
    <div class="row">
        <div class="col-md-2">客户：</div>
        <div class="col-md-4">${inboundBooking.customer.name!}</div>
        <div class="col-md-2">预约仓间：</div>
        <div class="col-md-4">${inboundBooking.storeArea.name!} &nbsp;</div>

        <div class="col-md-2">预约方式：</div>
        <div class="col-md-4">${inboundBooking.bookingMethod.name!} &nbsp;</div>
        <div class="col-md-2">申请入库时间：</div>
        <div class="col-md-4">${inboundBooking.applyInboundTime?string('yyyy-MM-dd HH:mm')}&nbsp;</div>

        <div class="col-md-2">联系人：</div>
        <div class="col-md-4">${inboundBooking.contact.contactinfo!} &nbsp;</div>
        <div class="col-md-2">来车类型：</div>
        <div class="col-md-4">${inboundBooking.vehicleType.name!} &nbsp;</div>

        <div class="col-md-2">来车台数：</div>
        <div class="col-md-4">${inboundBooking.vehicleAmount!} &nbsp;</div>
        <div class="col-md-2">车牌号：</div>
        <div class="col-md-4">${inboundBooking.vehicleNumbers!} &nbsp; </div>

        <div class="col-md-2">预约时间：</div>
        <div class="col-md-4">${inboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
        <div class="col-md-2">客户留言：</div>
        <div class="col-md-4">${inboundBooking.note!} &nbsp;</div>

        <div class="col-md-2">审核人：</div>
        <div class="col-md-4">${inboundBooking.confirmOperator.name!} &nbsp; </div>
        <div class="col-md-2">审核时间：</div>
        <div class="col-md-4">
            <#if inboundBooking.confirmTime?exists>
            ${inboundBooking.confirmTime?string("yyyy-MM-dd HH:mm")!}
            </#if> &nbsp;
        </div>

        <div class="col-md-2">审核备注：</div>
        <div class="col-md-4">${inboundBooking.confirmRemark!} &nbsp; </div>
    </div>
    <br/><br/>

    <div class="row">
        <div class="col-md-12">
            <h4>预约商品明细：  <#if inboundBooking.bookingStatus!='已完成'&&inboundBooking.bookingStatus!='已取消'><a
                    href="${base}/store/inbound-booking/new-item?inboundBookingId=${inboundBooking.id}"><i class="fa fa-plus-square"></i> 新建</a></#if></h4>
        </div>
        <div class="col-md-12">

            <table class="table table-striped table-advance table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>商品</th>
                    <th>数量</th>
                    <th>重量</th>
                    <th>预计托盘数</th>
                    <th>预计保管时间</th>
                    <th>规格</th>
                    <th>包装</th>
                    <th>产地</th>
                    <th>生产日期</th>
                    <th>保质期</th>
                    <#if inboundBooking.bookingStatus!='已完成'&&inboundBooking.bookingStatus!='已取消'>
                        <th>操作</th>
                    </#if>
                </tr>
                </thead>
            <tbody>
                <#assign productIds = "">
                <#if inboundBooking.inboundBookingItems?has_content>
                    <#assign totalAmount = 0, totalWeight = 0>
                    <#list inboundBooking.inboundBookingItems as item>
                        <#if (item_index > 0)>
                            <#assign productIds = productIds + ",">
                        </#if>
                        <#assign productIds = productIds + item.product.id>

                        <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                    <tr>
                        <td>${item_index + 1}</td>
                        <td>${item.product.code!} ${item.product.name!}</td>
                        <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                        <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                        <td>${item.storeContainerCount!}</td>
                        <td>${item.storeDuration}</td>
                        <td>${item.spec!}</td>
                        <td>${item.packing.name!}</td>
                        <td>${item.productionPlace!}</td>
                        <td><#if item.productionDate?exists>${item.productionDate?string('yyyy-MM-dd')}</#if></td>
                        <td>${item.qualityGuaranteePeriod}</td>
                        <#if inboundBooking.bookingStatus!='已完成'&&inboundBooking.bookingStatus!='已取消'>
                            <td><a href="javascript:void(0)" onclick="edit(${item.id})">编辑</a>
                                <a href="javascript:void(0)" onclick="del(${item.id})">删除</a></td>
                        </#if>
                    </tr>
                    </#list>
                <tr>
                    <td colspan="12" align="right">登记总数量：${totalAmount}, 总重量 ${totalWeight}</td>
                </tr>
                </tbody>
                </#if>
            </table>
            <div>
            </div>

        </div>
    </div>
    <div>
        <h4>预约情况概览</h4>

        <div class="row">
            <div class="col-md-4">
                截止${inboundBooking.applyInboundTime?string('yyyy-MM-dd HH:mm')}
            </div>
            <div class="col-md-8">
                <#if inboundBooking.inboundBookingItems?has_content>
                    预约商品：
                    <input type="radio" class="outline-radio-all" name="outline-product" value="0"> 全部 &nbsp;
                    <#list inboundBooking.inboundBookingItems as item>
                        <input type="radio" class="outline-radio" name="outline-product" value="${item.product.id}"> ${item.product.code!}${item.product.name!} &nbsp;
                    </#list>
                </#if>
            </div>
        </div>
        <br/>
        <div id="show-outline">
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" width="50%">
    <div class="modal-dialog modal-dialog-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <#if inboundBooking.bookingStatus!='未审核'>
                <h4 class="modal-title" id="L1">延迟预约</h4>
                <h4 class="modal-title" id="L2">取消预约</h4>
                <#else>
                <h4 class="modal-title" id="L3">预约审核</h4>
                </#if>
            </div>
            <form action="" method="post" id="storeform" class="form-horizontal"   data-parsley-validate>
                <input type="hidden" name="confirm"  id="confirm" value="true">
                <div class="row" id="boundTime">
                    </br>
                    <label class="col-md-3 control-label">预约入库时间:</label>
                    <input type="text" class="form-control Wdate" style="width:35%"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00',minDate:'(.now)'})"
                           name="applyInboundTime" id="applyInboundTime"
                           value="<#if inboundBooking.applyInboundTime?exists>${inboundBooking.applyInboundTime?string('yyyy-MM-dd HH:00')}</#if>"
                           placeholder="预约入库时间" data-parsley-required="true">
                </div>
                </br>
                <div class="row">
                    <label class="col-md-3 control-label">备注:</label>
                    <textarea name="remark" id="remark" rows="2" style="width:60%" class="form-control"></textarea>
                </div>
                <div class="modal-footer">
                    <div class="row" id="commonbuttons" <#if inboundBooking.bookingStatus=='未审核'>style="display:none"</#if>>
                    <button type="submit" class="btn btn-primary">保存</button>
                   <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
					</div>
					<div class="row" id="checkbuttons"  <#if inboundBooking.bookingStatus!='未审核'>style="display:none"</#if>>
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

<script>
    $(function () {
        var outlineAllUrl = "${base}/ajax/outline/booking?startDate=${.now}&endDate=${inboundBooking.applyInboundTime!}&productIds=${productIds}";
        showOutline(outlineAllUrl);

        $('.outline-radio-all').click(function () {
            showOutline(outlineAllUrl);
        });

        $('.outline-radio').click(function () {
            showOutline("${base}/ajax/outline/booking?startDate=${.now}&endDate=${inboundBooking.applyInboundTime!}&productIds=" + $(this).val());
        });
    })

    function showOutline(url) {
        var $outline = $('#show-outline');
        $outline.html('<div class="alert alert-warning">正在加载数据......</div>');
        $.get(url, function (data) {
            $outline.html(data);
        });
    }

    function t1() {
        $("#t1").show();
        $("#L1").show();
        $("#L2").hide();

        $("#boundTime").show();
        $("#storeform").attr("action", "${base}/store/inbound-booking/retard?id=${inboundBooking.id!}");
    }

    function t2() {
        $("#t1").hide();
        $("#L2").show();
        $("#L1").hide();

        $("#boundTime").hide();
        $("#storeform").attr("action", "${base}/store/inbound-booking/cancel?id=${inboundBooking.id!}");
    }
    function edit(itemId) {
        location.href = "${base}/store/inbound-booking/" + itemId + "/edit-item?inboundBookingId=${inboundBooking.id}";

    }
    function del(itemId) {
        if (confirm("确认删除！")) {
            location.href = "${base}/store/inbound-booking/" + itemId + "/delete-item?inboundBookingId=${inboundBooking.id}";
        }
    }
   function setcheck(value){
   	 	$("#confirm").val(value);
   	 	$("#storeform").attr("action", "${base}/store/inbound-booking/${inboundBooking.id!}/save-check");
   }
</script>
</#escape>
