<#escape x as x?html>
<style>
    .datagrid-view {
        min-height: 300px;
    }

    .datagrid-row {
        height: 30px;
    }
</style>
<header class="panel-heading">
    <a href="${base}/store/inbound-register">入库单</a> -
    <#if inboundRegister.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundRegister-form" action="${base}/store/inbound-register/${inboundRegister.id!}" method="post" class="form-horizontal" data-parsley-validate onsubmit="return getData()">
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundRegister.createdBy!}">
        <input type="hidden" name="createdTime" value="${inboundRegister.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="registerTime" value="${inboundRegister.registerTime}">
        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${inboundRegister.serialNo}" readonly>
        <input type="hidden" class="form-control" name="inboundBooking.id" id="inboundBooking.id" value="${inboundRegister.inboundBooking.id}">
        <input type="hidden" class="form-control" name="productCheck" id="productCheck" value="${productCheck}">
        <input type="hidden" class="form-control" name="stockInStatus" id="stockInStatus" value="${inboundRegister.stockInStatus}">

        <div class="row">
            <div class="col-md-11 row">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">客户:</label>

                            <div class="col-md-8">
                                <select id="customerId" name="customer.id" class="form-control" data-parsley-required="true">
                                    <option value="">请选择客户</option>
                                    <#list customers as customer>
                                        <option value="${customer.id}"
                                                <#if customer.id == inboundRegister.customer.id>selected</#if>>${customer.text!}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">入库类型:</label>

                            <div class="col-md-8">
                                <select id="inboundType" name="inboundType" class="form-control">
                                    <#list inboundTypes as inboundType>
                                        <option value="${inboundType}" <#if inboundType == inboundRegister.inboundType>selected</#if>>${inboundType}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">入库时间:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control Wdate"  name="inboundTime" id="inboundTime"
                                       value="${inboundRegister.inboundTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}" placeholder="入库时间"
                                       readonly="true" data-parsley-required="true">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">来车类型:</label>

                            <div class="col-md-8">
                                <select id="vehicleTypeId" name="vehicleType.id" class="form-control">
                                    <option value="">请选择车辆类型</option>
                                    <#list vehicleTypes as vehicleType>
                                        <option value="${vehicleType.id}"
                                                <#if vehicleType.id == inboundRegister.vehicleType.id>selected</#if>>${vehicleType.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">车辆数目:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control" name="vehicleAmount" id="vehicleAmount" value="${inboundRegister.vehicleAmount}"
                                       data-parsley-type="integer"
                                       data-parsley-min="0">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">车牌号:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control" name="vehicleNumbers" id="vehicleNumbers" value="${inboundRegister.vehicleNumbers}">
                            </div>
                        </div>
                    </div>
                </div>

                <#if inboundRegister.inboundBooking.id?exists && !inboundRegister.id?exists>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="col-md-2 control-label">入库明细:</label>

                                <div class="col-md-10">

                                    <table class="easyui-datagrid" id="inbound-register-datagrid"
                                           data-options="url:'${base}/store/inbound-register/inboundbook-items?inboundbookingId=${inboundRegister.inboundBooking.id}',
                                        method:'get',
                                        rownumbers:true,
                                        singleSelect:false,
                                        selectOnCheck:true,
                                        checkOnSelect:false,
                                        pagination:false,
                                        fitColumns:true,
                                        collapsible:false,onLoadSuccess:loadSuccess">
                                        <thead>
                                        <tr>
                                            <th data-options="field:'productId',checkbox:true"></th>
                                            <th data-options="field:'productName',width:100">商品</th>
                                            <th data-options="field:'amount',width:80,formatter: rowformater">数量</th>
                                            <th data-options="field:'storeContainerCount',formatter: storeContainerCount,width:80">预计托盘数</th>
                                            <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
                                            <th data-options="field:'weight',width:80">重量</th>
                                            <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
                                            <th data-options="field:'storeDuration',width:80">预期保管（天）</th>
                                            <th data-options="field:'qualityGuaranteePeriod',width:80">保质期（天）</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-4">
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>


<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择储位</h4>
            </div>
            <div id="selector-body" class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>


    function getData() {
        if ('${inboundRegister.inboundBooking.id}' == '' || '${inboundRegister.id}' != '') {
            return true;

        }
        var checkedItems = $('#inbound-register-datagrid').datagrid('getChecked');
        var ids = [];
        var reg = "^[1-9]*[1-9][0-9]*$";
        var result = true;
        $.each(checkedItems, function (index, item) {
            var inboundAmount = $('#val_' + item.productId).val();
            var storeContainerCount = $('#str_' + item.productId).val();
            if (inboundAmount.match(reg) == null || storeContainerCount.match(reg) == null) {
                result = false;
                return result;
            }
            ids.push(item.productId + ":" + inboundAmount + ":" + storeContainerCount);
        });
        $('#productCheck').val(ids);
        if (!result) {
            alert("选择的商品数量或预计托盘数需大于零");
        }
        return result;

    }
    function vali() {

        $.ajax({
            url: "${base}/store/inbound-register/vali",
            data: {"inboundBookingId": '${inboundRegister.inboundBooking.id}', "productChecks": $('#productCheck').val()},
            async: false,
            type: "get",
            success: function (data) {
                if (data == 'true') {
                    if (confirm("预约商品还有未登记，需要将剩下商品生成新的预约单吗？")) {
                        $('#newBooking').val(true);
                    }
                }
            }
        })

    }

    function rowformater(value, row, index) {
        return '<input type="text"  name="inboundAmount" id=val_' + row.productId + ' style="color:red"  data-parsley-type="integer"  onchange="selectedproduct(this.id,this.value)"  value=' + value + ' >'
    }
    function storeContainerCount(value, row, index) {
        return '<input type="text"  name="storeContainerCount" id=str_' + row.productId + ' style="color:red"  data-parsley-type="integer"  value=' + value + ' >'
    }

    function selectedproduct(id, value) {
        if (id.trim() == '') {
            return;
        }
        var productId = id.replace(/val_/, "");
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" + productId,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');
                var reg = "^[1-9]*[1-9][0-9]*$";
                if (value.match(reg) != null) {
                    var count = 0;
                    if (value % ar.bearingCapacity > 0) {
                        count = 1
                    }
                    $("#" + "str_" + productId).val((value - value % ar.bearingCapacity) / ar.bearingCapacity + count);

                }
            }
        })
    }
	
	function loadSuccess(data){
 		if(data){
			$.each(data.rows, function(index, item){
		       $('#inbound-register-datagrid').datagrid('checkRow', index);
			});
		}
	}
</script>

</#escape>
