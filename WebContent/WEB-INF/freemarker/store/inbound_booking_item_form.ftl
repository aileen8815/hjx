<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-booking/${inboundBookingId}">入库预约单</a> -
    <#if inboundBookingItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundBookingItem-form" action="${base}/store/inbound-booking/${inboundBookingItem.id!}/save-item" method="post" class="form-horizontal"
          data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundBookingItem.createdBy!}">
        <input type="hidden" name="createdTime" value="${inboundBookingItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="bookingStatus" value="已确认">
        <input type="hidden" class="form-control" name="inboundBooking.id" id="inboundBooking.id" value="${inboundBookingId}">
	
	<div class="row">
     <div class="col-md-11 row">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">商品:</label>

                    <div class="col-md-7">

                        <select id="productId" name="product.id" class="form-control" onchange="selectedproduct()" data-parsley-required="true">
                            <option value="">请选择商品</option>
                            <#list products as product>
                                <option value="${product.id}"
                                        <#if product.id == inboundBookingItem.product.id>selected</#if>>${product.code}${product.name}</option>
                            </#list>
                        </select>
 
                    </div>
                    <div class="col-md-2">
                     
                       <a class="btn btn-info"  data-toggle="modal" data-target="#add-custoemr-modal"><i class="fa fa-ellipsis-v"></i> 添加商品</a>
                     
                     </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">包装:</label>

                    <div class="col-md-7">
                        <select id="packingId" name="packing.id" class="form-control">
                            <option value="">请选择包装类型</option>
                            <#list packings as packing>
                                <option value="${packing.id}" <#if packing.id == inboundBookingItem.packing.id>selected</#if>>${packing.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">数量:</label>

                    <div class="col-md-4">
                        <input type="text" class="form-control" name="amount" id="amount" value="${inboundBookingItem.amount}" data-parsley-required="true"
                               data-parsley-type="integer" onchange="selectedproduct()" data-parsley-gt="0">
                    </div>
                    <div class="col-md-3">
                        <select id="amountMeasureUnitId" name="amountMeasureUnit.id" class="form-control">
                            <#list amountMeasureUnits as amountMeasureUnit>
                                <option value="${amountMeasureUnit.id}"
                                        <#if amountMeasureUnit.id == inboundBookingItem.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name}</option>
                            </#list>
                        </select>
                    </div>

                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">重量:</label>

                    <div class="col-md-4">
                        <input type="text" class="form-control" name="weight" id="weight" value="${inboundBookingItem.weight}" data-parsley-required="true"
                               data-parsley-type="number" data-parsley-gt="0">
                    </div>
                    <div class="col-md-3">
                        <select id="weightMeasureUnitId" name="weightMeasureUnit.id" class="form-control">

                            <#list weightMeasureUnits as weightMeasureUnit>
                                <option value="${weightMeasureUnit.id}"
                                        <#if weightMeasureUnit.id == inboundBookingItem.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name}</option>
                            </#list>
                        </select>
                    </div>

                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">预计托盘数:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="storeContainerCount" id="storeContainerCount"
                               value="${inboundBookingItem.storeContainerCount}"
                               data-parsley-required="true" data-parsley-type="integer" data-parsley-gt="0">
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">预期保管时间:</label>

                    <div class="col-md-7">
                        <div class="input-group">
                            <input type="text" class="form-control" name="storeDuration" id="storeDuration" value="${inboundBookingItem.storeDuration}"
                                   data-parsley-type="integer" data-parsley-min="0">
                            <span class="input-group-addon">天</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">生产日期:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" onClick="WdatePicker()"
                               name="productionDate"
                               id="productionDate"
                               value="<#if inboundBookingItem.productionDate?exists>${inboundBookingItem.productionDate?string('yyyy-MM-dd')}</#if>"
                               placeholder="生产日期">

                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">保质期:</label>

                    <div class="col-md-7">
                        <div class="input-group">
                            <input type="text" class="form-control" name="qualityGuaranteePeriod" id="qualityGuaranteePeriod"
                                   value="${inboundBookingItem.qualityGuaranteePeriod}" data-parsley-type="integer" data-parsley-min="0">
                            <span class="input-group-addon">天</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">

            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">产地:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="productionPlace" id="productionPlace" value="${inboundBookingItem.productionPlace!}">
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">规格:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="spec" id="spec" value="${inboundBookingItem.spec}">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-9">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
       </div>
     </div> 
    </form>
</div>
<#--<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
-->
<#include "/store/add_product_modal.ftl">
<script>
    function selectedproduct() {
        var productId = $("#productId").val();

        if (productId == '') {
            return;
        }
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" + productId,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');

                $("#packingId").select2("val", ar.commonPacking.id);
                $("#spec").val(ar.spec);
                $("#productionPlace").val(ar.productionPlace);
                var amount = $("#amount").val();

                var reg = "^[1-9]*[1-9][0-9]*$";
                if (amount.match(reg) != null) {
                    var count = 0;
                    if (amount % ar.bearingCapacity > 0) {
                        count = 1
                    }
                    $("#storeContainerCount").val((amount - amount % ar.bearingCapacity) / ar.bearingCapacity + count);

                    $("#weight").val((amount * ar.weight).toFixed(2));
                }
            }
        })
    }
</script>

</#escape>
