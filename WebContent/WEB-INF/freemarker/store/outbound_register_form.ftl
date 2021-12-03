<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-register">出库单</a> -
    <#if outboundRegister.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="outboundRegister-form" action="${base}/store/outbound-register/${outboundRegister.id!}" method="post"
          class="form-horizontal" data-parsley-validate onsubmit="return getData()">
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${outboundRegister.createdBy!}">
        <input type="hidden" name="createdTime"
               value="${outboundRegister.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">

        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${outboundRegister.serialNo}"
               readonly>
        <input type="hidden" class="form-control" name="outboundBooking.id" id="outboundBooking.id"
               value="${outboundRegister.outboundBooking.id}">
        <textarea style="display:none" class="form-control" name="productCheck" id="productCheck" rows="5"></textarea>
        <input type="hidden" class="form-control" name="stockOutStatus" id="stockOutStatus"
               value="${outboundRegister.stockOutStatus}">
        <div class="row">
            <div class="col-md-15 row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">出库类型:</label>
                        <div class="col-md-8">
                            <select id="outboundType" name="outboundType" class="form-control">
                                <#list outboundTypes as outboundType>
                                    <option value="${outboundType}"
                                            <#if outboundType == outboundRegister.outboundType>selected</#if>>${outboundType}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">客户:</label>
                        <div class="col-md-8">
                            <select id="customerId" name="customer.id" class="form-control" onchange="search(this.value)"
                                    data-parsley-required="true">
                                <option value="">请选择客户</option>
                                <#list customers as customer>
                                    <option value="${customer.id}"
                                            <#if customer.id == outboundRegister.customer.id>selected</#if>>${customer.text!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>


                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">出库时间:</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control Wdate"
                                   value="${outboundRegister.outboundTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}"
                                   name="outboundTime" id="outboundTime" placeholder="出库时间" readonly="true"
                                   data-parsley-required="true">
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
                                            <#if vehicleType.id == outboundRegister.vehicleType.id>selected</#if>>${vehicleType.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">车辆数目:</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" name="vehicleAmount" id="vehicleAmount"
                                   value="${outboundRegister.vehicleAmount}" data-parsley-type="integer"
                                   data-parsley-min="0">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">车牌号:</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" name="vehicleNumbers" id="vehicleNumbers"
                                   value="${outboundRegister.vehicleNumbers}">
                        </div>
                    </div>
                </div>


                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">车辆来源:</label>

                        <div class="col-md-8">
                            <select id="vehicleSource" name="vehicleSource" class="form-control">

                                <#list vehicleSources as vehicleSource>
                                    <option value="${vehicleSource}"
                                            <#if vehicleSource == outboundRegister.vehicleSource>selected</#if>>${vehicleSource}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label">是否携带委托书:</label>

                        <div class="col-md-8">

                            <input type="radio" name="haveProxy" checked value="true"
                                   <#if  outboundRegister.haveProxy>checked</#if>>携带
                            <input type="radio" name="haveProxy" value="false"
                                   <#if  outboundRegister.id?exists&&!outboundRegister.haveProxy>checked</#if>>未携带

                        </div>
                    </div>
                </div>






            <#--商品详细 -->
            <#-- 表格工具条 -->
                <div class="col-md-12" id="outbound-register-toolbar">
                    <div class="row-toolbar">
                    
                        <label class="col-md-2 control-label">生产日期:</label>

                    <div class="col-md-3">
                        <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" onClick="WdatePicker()"
                               name="productionDate" id="productionDate"
                               onchange="outRegisterjs.search(this.value)"
                               value=""
                               placeholder="生产日期">
                    </div>
                    
                    <label class="col-md-2 control-label">产地:</label>
                        <div class="col-md-3">
                            <input type="text" class="form-control" name="productionPlace" id="productionPlace"
                            onchange="outRegisterjs.search(this.value)"
                                   value="">
                        </div>
                        
                        
                        <div class="col-md-10">
                            <div style="text-align:right">
                                录入信息后，空白处点击鼠标左键，进行查询；保存前可选择[全部商品]预览所有已选商品数据 &nbsp;
                                <label class="sr-only">全部商品</label>
                                <select class="form-control" id="productId" name="productId"
                                        onchange="outRegisterjs.search(this.value)" style="width:220px;text-align:left;">
                                    <option value="">全部商品</option>

                                </select>
                            </div>

                        </div>
                        
                    </div>
                </div>
                        
            <#-- JEasyUI DataGrid 显示数据 -->
                <div class="col-md-12">
                    <div class="form-group">
                        <label class="col-md-2 control-label">出库明细:</label>
                        <div class="col-md-15">

                            <table class="easyui-datagrid" id="outbound-register-datagrid"
                                   toolbar="#outbound-register-toolbar"
                                   data-options="url:'${base}/store/outbound-register/bookinventory-list?outboundbookingId=${outboundRegister.outboundBooking.id}&&customerId=${outboundRegister.customer.id}&&outboundRegisterId=${outboundRegister.id}',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false,selectOnCheck:true,checkOnSelect:false,onLoadSuccess:loadSuccess">
                                <thead>
                                <tr>
                                    <th data-options="field:'batchProduct',checkbox:true"></th>
                                    <th data-options="field:'inboundRegisterSerialNo',width:160">批次</th>
                                    <th data-options="field:'productName',width:150">商品</th>
                                    <th data-options="field:'amount',width:80">库存数量</th>
                                    <th data-options="field:'outboundAmount',width:100,formatter: rowformater">出库数量</th>
                                    <th data-options="field:'totalStoreContainer',width:100">库存托数</th>
                                    <th data-options="field:'storeContainerCount' ,formatter:storeContainerCount,width:80">
                                        预计出库托数
                                    </th>
                                    <th data-options="field:'weight',width:90, formatter:fixedformater">库存重量</th>
                                    <th data-options="field:'weightMeasureUnitName',width:80">重量单位</th>
                                    <th data-options="field:'bearingCapacity',width:60">件/托</th>
                                    <th data-options="field:'productDetail',width:90">多品明细</th>
                                    <th data-options="field:'stockInTime',width:160,formatter:formatterDate">上架时间</th>
                                    <th data-options="field:'productionPlace',width:80">产地</th>
                                    <th data-options="field:'productionDate',width:160,formatter:formatterDate">生产日期</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="col-md-12 col-md-offset-2">
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>

            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        $('#productCheck').val('${productCheck}');

    <#-- 编辑时默认加载用户商品 -->
        <#if outboundRegister.id?exists>
            setingProduct(${outboundRegister.customer.id});
        </#if>
    });

    var outRegisterjs = {
        search: function () {
            if (getData()) { // save first
                $('#outbound-register-datagrid').datagrid('load', {
                    productId: $('#productId').val(),
                    outboundbookingId: "${outboundRegister.outboundBooking.id}",
                    selectCustomerId: $("#customerId").val(),
                    productionPlace: $("#productionPlace").val(),
                    productionDate: $("#productionDate").val(),
                    outboundRegisterId: "${outboundRegister.id}"
                    
                });
            }
        }
    };

    function getData() {
        var checkedArr = [];
        var productChecked = $('#productCheck').val();
        if (productChecked !== '') {
            checkedArr = productChecked.split(",");
        }

        var checkedItems = $('#outbound-register-datagrid').datagrid('getChecked');
        var result = true;
        var reg = "^[1-9]*[1-9][0-9]*$";
        $.each(checkedItems, function (index, item) {
            var outboundAmount = $('#val_' + item.batchProduct).val();
            var storeContainerCount = $('#str_' + item.batchProduct).val();
            if (outboundAmount.match(reg) == null || outboundAmount > item.amount || storeContainerCount.match(reg) == null || storeContainerCount > item.totalStoreContainer) {
                result = false;
                return result;
            }
            var checkedOutboundItem = item.batchProduct + "_" + outboundAmount + "_" + storeContainerCount;
            if(productChecked.indexOf(checkedOutboundItem) === -1){
                checkedArr.push(checkedOutboundItem);
            }
        });

        if (!result) {
            alert("选择的商品数量和托数需大于零并且不能大于库存数量");
        }
        $('#productCheck').val(checkedArr);
        return result;
    }

    function loadSuccess(data) {
        if (data) {
            var checkedArr;
            var productChecked = $('#productCheck').val();
            if (productChecked !== '') {
                checkedArr = productChecked.split(",");
            }

            $.each(data.rows, function (index, item) {
                <#if outboundBooking.id?exists>
                    <#list outboundBooking.outboundBookingItems as outboundBookingItem>
                        if (item.batchProduct == '${outboundBookingItem.batchProduct}') {
                            $('#outbound-register-datagrid').datagrid('checkRow', index);
                        }
                    </#list>
                <#else>
                    <#list outboundRegister.outboundRegisterItems as outboundRegisterItem>
                        if (item.batchProduct == '${outboundRegisterItem.batchProduct}') {
                            $('#outbound-register-datagrid').datagrid('checkRow', index);
                        }
                    </#list>
                </#if>

                for (productIndex in checkedArr) {
                    var productStr = checkedArr[productIndex];
                    var productDetail = productStr.split("_");
                    var productBatchNo = productDetail[0];
                    var productSn = productDetail[1];
                    var productAmt = productDetail[2];
                    var productCnt = productDetail[3];

                    var batchSn = productBatchNo + "_" + productSn;
                    if (item.batchProduct === batchSn) {
                        item.outboundAmount = productAmt;
                        item.storeContainerCount = productCnt;
                        $('#outbound-register-datagrid').datagrid('checkRow', index);
                        $('#outbound-register-datagrid').datagrid('updateRow', {
                            index: index,
                            row: item
                        });
                    }
                }
            });
        }
    }

    function search(value) {
        var id = value;
        if (id == '') {
            id = -1;
        }
        $('#outbound-register-datagrid').datagrid('load', {
            selectCustomerId: id
        })
        setingProduct(value);
    }

    function formatterDate(value) {
        if (value == null) {
            return value;
        } else {
            var date = new Date(value);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var hour = date.getHours();
            var min = date.getMinutes();
            var sec = date.getSeconds();
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
            dateFormat(date, 'yyyy-mm-dd HH:MM:SS');

            return date.format('yyyy-mm-dd HH:MM:ss');
        }
    }
    function rowformater(value, row, index) {
        return '<input type="text"  name="outboundAmount" id=val_' + row.batchProduct + ' style="color:red"  onchange="selectedproduct(this.id,this.value)"  data-parsley-type="integer"  value=' + value + ' >'
    }
    function fixedformater(value) {
        var v = value.toFixed(2);
        <!-- console.log(v); -->
        return v;
    }
    function storeContainerCount(value, row, index) {
        return '<input type="text"  name="storeContainerCount" id=str_' + row.batchProduct + ' style="color:red"  data-parsley-type="integer"  value=' + value + ' >'
    }

    function selectedproduct(id, value) {
        if (id.trim() == '') {
            return;
        }
        var batchProduct = id.replace(/val_/, "");
        var batchProductArry = batchProduct.split('_');
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" + batchProductArry[1],
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
                    $("#" + "str_" + batchProduct).val((value - value % ar.bearingCapacity) / ar.bearingCapacity + count);

                }
            }
        })
    }

    function setingProduct(value) {
        $("#productId").empty();
        $("#productId").prepend('<option  value= >全部商品</option>');

        if (value == '') {
            $("#productId").empty();
            return;
        }

        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" + value,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');
                if (ar.length > 0) {
                    for (var i = 0; i < ar.length; i++) {
                        var option = '<option  value=' + ar[i].id + '>' + ar[i].code + ar[i].name + '</option>';
                        $("#productId").append(option);
                    }
                }
            }
        })
    }
</script>
</#escape>
