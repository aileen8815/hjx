<#escape x as x?html>
<header class="panel-heading">
  <a href="${base}/report/report-arrearagefee">应收款明细报表</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div>
    <form id="payment-report" target="reportFrame" action="${reportUrl}/pentaho/api/repos/%3Apublic%3AArrearageFee.prpt/report"
      class="form-horizontal" data-parsley-validate>
      <input type ="hidden" id="userid" name="userid" value="${user}">
      <input type ="hidden" id="password" name="password" value="${password}">
      <input type ="hidden" id="output-target" name="output-target" value="pageable/pdf">
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <div class="col-md-10">
              <label class="col-md-2 control-label">截止日期:</label>
              <div class="col-md-3">
                <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endTime" id="endTime" value="${.now?string('yyyy-MM-dd')}" placeholder="结束日期" >
              </div>
              <label class="col-md-1 control-label">客户:</label>
              <div class="col-md-3">
                <select id="customerid" name="customerid" class="form-control" >
                  <option value="0">全部客户</option>
                  <#list customerlist as customer>
                  <option value="${customer.id}">${customer.text!}</option>
                  </#list>
                </select>
              </div>
              <div class="col-md-1">
                <button type="submit" class="btn btn-primary" OnClick="SetReportTypeQuery()">查询</button>
              </div>
              <div class="col-md-1">
                <button type="submit" class="btn btn-primary" OnClick="SetExpReportType()" >导出Excel</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  <iframe id="reportFrame" name="reportFrame" width="100%" height="600" frameborder="0"></iframe>
</div>
<script src="${base}/assets/report/report.js"></script>
<script>
function SetReportTypeQuery(){
	$("#output-target").val('pageable/pdf');
}
function SetExpReportType(){
	$("#output-target").val('table/excel;page-mode=flow');
}
</script>
</#escape> 