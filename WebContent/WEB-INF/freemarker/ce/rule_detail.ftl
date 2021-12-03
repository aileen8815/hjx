<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/ce/rule-detail">计费标准明细查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form class="form-horizontal" data-parsley-validate>
        <input type="hidden" id="userid" name="userid" value="${user}">
        <input type="hidden" id="password" name="password" value="${password}">
        <input type="hidden" id="output-target" name="output-target" value="pageable/pdf">
        <div class="row">
            <div class="col-md-16">
                <div class="form-group">
                	<label class="col-md-1 control-label">客户等级:</label>
                    <div class="col-md-2">
                        <select id="customerGradeId" name="customerGradeId" class="form-control">
                            <option value="0">全部等级</option>
                            <#list customerGradelist as customerGrade>
                                <option value="${customerGrade.id}">${customerGrade.name!}</option>
                            </#list>
                        </select>
                    </div>
                    
                    <label class="col-md-1 control-label">业务类型:</label>
                    <div class="col-md-2">
                        <select class="easyui-combobox"
				            id="business-type"
				            style="width:160px; height: 35px;"
				            >
				        <option value="all">全部计费业务</option>
				        <option value="inbound">入库</option>
				        <option value="outbound">出库</option>
				        <option value="ownerchangeforbuyer">买方货权转移</option>
				        <option value="ownerchangeforseller">卖方货权转移</option>
				        <option value="monthknot">月结仓储费</option>
				    </select>
                    </div>
                    
                    <label class="col-md-1 control-label">收费项目:</label>
                    <div class="col-md-2">
                        <select id="feeItemId" name="feeItemId" class="form-control">
                            <option value="0">全部收费项目</option>
                            <#list feeItemlist as feeItem>
                                <option value="${feeItem.id}">${feeItem.name!}</option>
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
    <table id="rule-table" class="table table-bordered table-striped">
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

    function loadReport() {
    
    	var customerGradeId = $('#customerGradeId').val();
    	var feeItemId = $('#feeItemId').val();
    	var businessType = $("#business-type").combobox('getValue');
        var dataUrl = "${base}/ce/rule/search-rule-detail?customerGradeId="
        		+ customerGradeId + "&feeItemId=" + feeItemId + "&businessType=" + businessType;
        
        var $table = $('#rule-table');
        $table.bootstrapTable({
            url: dataUrl,
            undefinedText: "",
            showExport: true,
            exportTypes: ['csv', 'txt', 'excel'],
            columns: [
                    {
                        field: 'CUSTOMERGRADE',
                        title: '客户等级',
                        valign: 'middle'
                    },
                    {
                        field: 'BUSINESSTYPE',
                        title: '业务类型',
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'RULENAME',
                        title: '规则名称',
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'FEEITEMNAME',
                        title: '收费项目',
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'CALCUTITLE',
                        title: '计算依据',
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'FACTOR',
                        title: '值(元)',
                        align: 'right',
                        valign: 'middle'
                    },
                    {
                        field: 'PACKTYPE',
                        title: '包装类型',
                        align: 'right',
                        valign: 'middle'
                    }
            ]
        });
        $table.bootstrapTable('refresh', {url: dataUrl});
    }
</script>
</#escape>
