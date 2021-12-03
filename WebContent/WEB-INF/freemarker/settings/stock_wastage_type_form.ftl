<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/stock-wastage-type">报损类型</a> - 
    <#if stockWastageType.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="stockWastageType-form" action="${base}/settings/stock-wastage-type/${stockWastageType.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${stockWastageType.createdBy!}">
    <input type="hidden" name="createdTime" value="${stockWastageType.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-1 control-label">编码:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${stockWastageType.code!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">名称:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="name" id="name" value="${stockWastageType.name!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-1 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>
</#escape>