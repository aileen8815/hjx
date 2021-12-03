<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-register/${registerId}">出库单</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">


<#-- JEasyUI DataGrid 显示数据 -->
    <table class="easyui-datagrid" toolbar="#outbound-register-toolbar" id="outbound-register-datagrid"
           data-options="url:'${base}/store/outbound-register/item-list?registerId=${registerId}',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false">
        <thead>
        <tr>
            <th data-options="field:'storeContainerId',checkbox:true"></th>
            <th data-options="field:'inboundRegisterSerialNo',width:150">批次号</th>
            <th data-options="field:'storeContainerLabel',width:80">托盘标签</th>
            <th data-options="field:'storeLocationCode',width:100">储位编码</th>
            <th data-options="field:'customeName',width:60">客户</th>
            <th data-options="field:'productName',width:120">商品</th>
            <th data-options="field:'weight',width:60">重量</th>
            <th data-options="field:'weightMeasureUnitName',width:70">重量单位</th>
            <th data-options="field:'amount',width:60">数量</th>
            <th data-options="field:'amountMeasureUnitName',width:70">数量单位</th>
            <th data-options="field:'stockInTime',width:90,formatter:formatterDate">入库日期</th>
            <th data-options="field:'productDetail',width:100">多品明细</th>
            <th data-options="field:'qualityGuaranteePeriod',width:100">保质期（天）</th>
            <th data-options="field:'productionPlace',width:80">产地</th>
            <th data-options="field:'productionDate',width:160,formatter:formatterDate">生产日期</th>
        </tr>
        </thead>
    </table>

<#-- 表格工具条 -->
    <div class="col-md-12" id="outbound-register-toolbar">
        <div class="row row-toolbar">
            <div class="col-md-4">
                <div class="m-b-sm">
                    <div class="btn-group">
                        <button id="person-add" type="button" class="btn btn-primary" onclick="storelocationtypejs.add();"><i
                                class="fa fa-plus-square"></i> 分派拣货单
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div style="text-align:right">
                    <form class="form-inline" operator="form">
                        <div class="form-group">
                            <label class="sr-only" for="storeAreaId">选择库区</label>
                            <select class="form-control" id="storeAreaId" name="storeAreaId"
                                    style="width:150px;text-align:left;margin-right:6px;">
                                <option value="">选择库区</option>
                                <#list storeAreas as storeArea>
                                    <#if !storeArea.parent?exists>
                                        <option value="${storeArea.id}"
                                                <#if storeArea.id == inboundRegisterItem.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                                        <#list storeArea.children as area>
                                            <option value="${area.id}"
                                                    <#if area.id == inboundRegisterItem.storeArea.id>selected</#if>>&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                        </#list>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="sr-only" for="storeAreaId">选择商品</label>
                            <select class="form-control" id="productId" name="productId"
                                    style="width:220px;text-align:left;margin-right:6px;">
                                <option value="">请选择商品</option>
                                <#list products as product>
                                    <option value="${product.id!}">${product.code!} ${product.name!}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="btn-group">
                            <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button">
                                <i class="fa fa-search"></i> 查询
                            </button>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var storelocationtypejs = {
        search: function () {
            $('#outbound-register-datagrid').datagrid('load', {
                product: $('#productId').val(),
                storeArea: $('#storeAreaId').val()
            });
        },
        add: function () {
            var row = $('#outbound-register-datagrid').datagrid('getSelected');
            if (row) {
                var checkedItems = $('#outbound-register-datagrid').datagrid('getChecked');
                var ids = [];
                var weight = 0;
                var amount = 0;
                $.each(checkedItems, function (index, item) {
                    ids.push(item.storeContainerId);
                    weight = weight + item.weight;
                    amount = amount + item.amount;
                });

                alert("出库单总重量${weight},总数量${amount};本次选中托盘总重量" + weight + ",总数量" + amount);
                location.href = "${base}/store/outbound-register/save-item?registerId=${registerId}&&ids=" + ids;
            } else {
                alert('请选择托盘！');
            }
        }

    };

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
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
        }
    }

</script>

</#escape>
