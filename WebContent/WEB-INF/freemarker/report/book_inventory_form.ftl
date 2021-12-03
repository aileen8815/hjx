<#escape x as x?html>
<header class="panel-heading">
    <a href="#">
        <#if 'ReportStockSummary' == prptName>
            库存汇总报表
        <#elseif 'ReportStockDetail' == prptName>
            库存明细报表
        <#elseif 'ReportStockDaily' == prptName>
            库存日报表
        </#if>
    </a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <div>
        <form id="bookinventory-form" target="reportFrame"
              action="${reportUrl}/pentaho/api/repos/%3Apublic%3A${prptName}.prpt/report" class="form-horizontal"
              data-parsley-validate>
            <input type="hidden" id="output-target" name="output-target" value="pageable/pdf">
            <input type="hidden" id="userid" name="userid" value="${user}">
            <input type="hidden" id="password" name="password" value="${password}">
            <input type="hidden" id="customers" name="customers" value="-1">
            <input type="hidden" id="customerText" name="customerText" value="">
            <input type="hidden" id="products" name="products" value="-1">
            <input type="hidden" id="productText" name="productText" value="">
            <input type="hidden" id="startDate" name="startDate" value="">
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label class="col-md-4 control-label">库区:</label>
                        <div class="col-md-8">
                            <select id="storeAreas" name="storeAreas" class="form-control" required>
                                <option value="-1">全部库区</option>
                                <#list storeAreaList as storeArea>
                                    <#if !storeArea.parent?exists>
                                        <option value="${storeArea.id}">${storeArea.code} ${storeArea.name}</option>
                                        <#list storeArea.children as area>
                                            <option value="${area.id}">
                                                &nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                        </#list>
                                    </#if>
                                </#list>
                            </select>
                            <input type="hidden" id="storeAreaText" name="storeAreaText" value="全部库区">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <label class="col-md-4 control-label">时间:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                   name="endDate"
                                   id="endDate" value="" required
                                   placeholder="报表时间">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <div class="col-md-offset-2 col-md-10">
                            <button type="submit" class="btn btn-primary" onClick="setReportTypeQuery()">查询</button> &nbsp;&nbsp;
                            <button type="submit" class="btn btn-primary" onclick="setExpReportType()">导出Excel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <iframe id="reportFrame" name="reportFrame" width="100%" height="600" frameborder="0"></iframe>
</div>
<script src="${base}/assets/report/report.js"></script>
<script type="text/javascript">
    $(function () {
        $("#endDate").val(getCurrentTime());
        $("#storeAreas").bind("change", function () {
            var checkText = $("#storeAreas").find("option:selected").text();
            $("#storeAreaText").val(checkText);
        });
    });

    function getCurrentTime() {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
    }

    function setReportTypeQuery() {
        setStartDate();
        $("#output-target").val('pageable/pdf');
    }

    function setExpReportType() {
        setStartDate();
        $("#output-target").val('table/excel;page-mode=flow');
    }

    function setStartDate() {
        var endDate = $('#endDate').val();
        var startDate = new Date(Date.parse(endDate.replace(/-/g, "/")));
        startDate = new Date(startDate.getTime() - 24 * 60 * 60 * 1000); // 前一天
        var y = startDate.getFullYear();
        var m = startDate.getMonth() + 1;
        var d = startDate.getDate();
        startDate = y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
        $("#startDate").val(startDate);
    }
</script>
</#escape>
