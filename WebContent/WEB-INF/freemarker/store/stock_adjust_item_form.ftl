<#escape x as x?html>
<header class="panel-heading">
    <a href="javascript:void(0)">库存调整</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<!-- 明细表单 -->
<form id="stockAdjustItemform" action="${base}/store/stock-adjust/save-item" method="post" class="form-horizontal" data-parsley-validate>

    <input type="hidden" name="stockIn.id" id="stockInId" value="${stockAdjustItem.stockIn.id!}">

    <div class="form-group">
        <label class="col-md-2 control-label">储位:</label>

        <div class="col-md-4">
            <input type="text" class="form-control" name="storeLocation" id="storeLocation" data-parsley-required="true" readonly>
        </div>
        <div class="col-md-2">
            <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
        </div>
        <div class="col-md-2">
            <label class="checkbox-inline"><input type="checkbox" id="empty" name="empty" value="true"> 设置为空闲</label>
        </div>
    </div>
    <div id="modal-item-ext">
        <div class="form-group">
            <label class="col-md-2 control-label">托盘:</label>

            <div class="col-md-4">

                <select name="storeContainer.id" id="storeContainerid" class="form-control"">
                <option value=""></option>
                <#list storeContainers as storeContainer>
                    <option value="${storeContainer.id!}">${storeContainer.label} </option>
                </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">客户:</label>

            <div class="col-md-4">

                <select name="customer.id" id="customerid" class="form-control"">
                <option value=""></option>
                <#list customers as customer>
                    <option value="${customer.id!}">${customer.text!}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">商品:</label>

            <div class="col-md-4">
                <input type="hidden" name="product.id" id="productId">
                <input type="text" class="form-control" id="productName" name="productName" readonly>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">重量:</label>

            <div class="col-md-2">
                <input type="text" class="form-control" name="weight" id="weight" value="0">
            </div>
            <div class="col-md-2">
                <select class="form-control" id="weightMeasureUnit" name="weightMeasureUnit.id">
                    <#list weightMeasureUnits as weightMeasureUnit>
                        <option value="${weightMeasureUnit.id!}">${weightMeasureUnit.name} </option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">数量:</label>

            <div class="col-md-2">
                <input type="text" class="form-control" name="amount" id="amount" value="0">
            </div>
            <div class="col-md-2">
                <select class="form-control" id="amountMeasureUnit" name="amountMeasureUnit.id">
                    <#list amountMeasureUnits as amountMeasureUnit>
                        <option value="${amountMeasureUnit.id!}">${amountMeasureUnit.name} </option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">商品状态:</label>

            <div class="col-md-4">
                <select class="form-control" id="productStatus" name="productStatus.id">
                    <option value=""></option>
                    <#list statuses as status>
                        <option value="${status.id!}">${status.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">规格:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="spec" id="spec">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">包装:</label>

            <div class="col-md-4">
                <select class="form-control" id="packing" name="packing.id">
                    <option value=""></option>
                    <#list packings as packing>
                        <option value="${packing.id!}">${packing.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">保质期:</label>

            <div class="col-md-4">
                <div class="input-group">
                    <input type="text" class="form-control" id="quanlityGuaranteePeriod" name="quanlityGuaranteePeriod" value="0">
                    <span class="input-group-addon">天</span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">预计保管时间:</label>

            <div class="col-md-4">
                <div class="input-group">
                    <input type="text" class="form-control" id="storeDuration" name="storeDuration" value="0">
                    <span class="input-group-addon">天</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-md-2 control-label">生产日期:</label>

            <div class="col-md-4">
                <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" onClick="WdatePicker()" name="productionDate"
                       id="productionDate" value="<#if bookInventory.productionDate?exists>${bookInventory.productionDate?string('yyyy-MM-dd')}</#if>"
                       placeholder="生产日期">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">检验合格:</label>

            <div class="col-md-4">
                <input type="radio" id="qualified1" name="qualified" checked> 是 &nbsp;&nbsp;&nbsp;
                <input type="radio" id="qualified2" name="qualified"> 否
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-2 col-md-9">
            <button class="btn btn-success">保存</button>
        </div>
    </div>
</form>

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
</div>
<!-- /.modal -->

<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
<script>
    $(document).ready(function () {
        $('#productName').ztreeSelect({
            hiddenInput: '#productId',
            url: "${base}/settings/product/ztree"
        });

        $.get('${base}/store/store-location-selector?adjust=1', function (data) {
            $("#selector-body").html(data);
        });
    });

    function locationNodeClick() {
        var result = $('#location-result');
        var ref = $(this).attr('ref');
        if ($(result).text().indexOf(ref) == -1) {
            $(result).text(ref);
            $(this).parent().css('background', '#57AEE2');
        } else {
            $(result).text("");
            $(this).parent().css('background', '');
        }
        $('#storeLocation').val($(result).text());

        $.ajax({
            url: "${base}/store/stock-adjust/select-storelocation",
            data: {"storeLocation": $('#storeLocation').val()},
            type: "POST",
            success: function (data) {
                if (data == '') {
                    var storeLocation = $('#storeLocation').val();
                    $('#stockAdjustItemform')[0].reset()
                    $("#packing").select2("val", '');
                    $("#productId").select2("val", '');
                    $("#storeContainerid").select2("val", '');
                    $("#customerid").select2("val", '');
                    $('#storeLocation').val(storeLocation)
                } else {
                    var ar = eval('(' + data + ')');
                    setlinkmanform(ar);
                }

            }
        })

    }

    function setlinkmanform(ar) {
        $("#storeContainerid").select2("val", ar.storeContainer.id);
        $("#customerid").select2("val", ar.customer.id);
        $("#productId").val(ar.product.id);
        $("#productName").val(ar.product.text);
        $("#weight").val(ar.weight);
        $("#weightMeasureUnit").select2("val", ar.weightMeasureUnit.id);
        $("#amount").val(ar.amount);
        $("#amountMeasureUnit").select2("val", ar.amountMeasureUnit.id);
        $("#productStatus").select2("val", ar.productStatus.id);
        $("#spec").val(ar.spec);
        if (ar.packing != null) {
            $("#packing").select2("val", ar.packing.id);
        } else {
            $("#packing").select2("val", '');
        }
        $("#quanlityGuaranteePeriod").val(ar.quanlityGuaranteePeriod);
        $("#storeDuration").val(ar.storeDuration);
        $("#productionDate").val(formatterDate(ar.productionDate));
        if (ar.stockIn != null) {
            $("#stockInId").val(ar.stockIn.id);
        } else {
            $("#stockInId").val('');
        }

    }

    function formatterDate(value) {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();

        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);

    }
</script>

</div>
</#escape>