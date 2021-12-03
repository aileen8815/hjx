<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/finance/standing-book-daily/summary">冷库客户结算收费汇总表</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form class="form-horizontal" data-parsley-validate>
        <input type="hidden" id="userid" name="userid" value="${user}">
        <input type="hidden" id="password" name="password" value="${password}">
        <input type="hidden" id="output-target" name="output-target" value="pageable/pdf">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="col-md-1 control-label">开始日期:</label>
                    <div class="col-md-2">
                        <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                               name="startTime" id="startDate" value="${.now?string('yyyy-MM-dd')}" placeholder="开始日期">
                    </div>
                    <label class="col-md-1 control-label">截止日期:</label>
                    <div class="col-md-2">
                        <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                               name="endTime" id="endDate" value="${.now?string('yyyy-MM-dd')}" placeholder="结束日期">
                    </div>
                    <label class="col-md-1 control-label">客户:</label>
                    <div class="col-md-2">
                        <select id="customerid" name="customerid" class="form-control">
                            <option value="0">全部客户</option>
                            <#list customerlist as customer>
                                <option value="${customer.id}">${customer.text!}</option>
                            </#list>
                        </select>
                    </div>
                    <div class="col-md-1">
                        <button type="button" class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <table id="accounting-table" class="table table-bordered table-striped">
    </table>
</div>

<script src="${base}/assets//bootstrap-table/bootstrap-table.min.js"></script>
<script src="${base}/assets/bootstrap-table/extensions/export/bootstrap-table-export.js"></script>
<script src="${base}/assets/bootstrap-table/extensions/export/tableExport.js"></script>
<script>
    $(function () {
        $('#btnQuery').click(function () {
            loadReport();
        })
    })

    function numberFormat(value, row, index) {
        if (0 === value) {
            return '';
        }
        return value;
    }
    
    function conversionUnit(value, row, index) {
        if (0 === value) {
            return '';
        }else
        {
        value = value/1000;
        }
        return value;
    }

    function loadReport() {
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        var customerid = $('#customerid').val();

        var dataUrl = "${base}/finance/standing-book-daily/search-summary?customerId="
                + customerid + "&startDate=" + startDate + "&endDate=" + endDate;

        var $table = $('#accounting-table');
        $table.bootstrapTable({
            url: dataUrl,
            undefinedText: "",
            showExport: true,
            exportTypes: ['csv', 'txt', 'excel'],
            columns: [
                [
                    {
                        field: 'customerName',
                        title: '客户<br>名称',
                        rowspan: 2,
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: '入库',
                        colspan: 3,
                        align: 'center'
                    },
                    {
                        title: '出库',
                        colspan: 3,
                        align: 'center'
                    },
                    {
                        title: '结存',
                        colspan: 3,
                        align: 'center'
                    },
                    {
                        title: '当日仓储费',
                        colspan: 4,
                        align: 'center'
                    },
                    {
                        field: 'shipment',
                        title: '转运费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'sorting',
                        title: '分拣费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'handling',
                        title: '装卸费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'shrinkwrap',
                        title: '缠膜费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'unloading',
                        title: '倒货费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'ketonehandling',
                        title: '酮体 &nbsp;<br>装卸费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'writeCode',
                        title: '抄码费',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'receivableFee',
                        title: '应收<br>金额',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'actualFee',
                        title: '实收<br>金额',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'receivableBalance',
                        title: '应收<br>余额',
                        formatter: numberFormat,
                        rowspan: 2,
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        title: '备注',
                        rowspan: 2,
                        valign: 'middle'
                    }
                ],
                [
                    {
                        field: 'inboundContainerCount',
                        title: '托盘<br>个数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'inboundWeightCount',
                        title: '重量<br>吨数',
                        formatter: conversionUnit,
                        align: 'right'
                    },
                    {
                        field: 'inboundAmountCount',
                        title: '货物<br>件数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'outboundContainerCount',
                        title: '托盘<br>个数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'outboundWeightCount',
                        title: '重量<br>吨数',
                        formatter: conversionUnit,
                        align: 'right'
                    },
                    {
                        field: 'outboundAmountCount',
                        title: '货物<br>件数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'stockContainerCount',
                        title: '托盘<br>个数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'stockWeightCount',
                        title: '重量<br>吨数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'stockAmountCount',
                        title: '货物<br>件数',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'containerStorage',
                        title: '计 &nbsp;<br>托盘',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'rentalAreaStorage',
                        title: '计 &nbsp;<br>库间',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'weightStorage',
                        title: '计 &nbsp;<br>重量',
                        formatter: numberFormat,
                        align: 'right'
                    },
                    {
                        field: 'amountStorage',
                        title: '计 &nbsp;<br>件数',
                        formatter: numberFormat,
                        align: 'right'
                    }
                ]
            ]
        });

        $table.bootstrapTable('refresh', {url: dataUrl});
    }
</script>
</#escape>
