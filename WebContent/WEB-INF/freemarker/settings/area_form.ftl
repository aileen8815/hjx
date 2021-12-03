<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/area">行政区划</a> - 
    <#if area.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="area-form" action="${base}/settings/area/${area.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${area.createdBy!}">
    <input type="hidden" name="createdTime" value="${area.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <#if area.parent?exists>
    <div class="form-group">
      <label class="col-md-1 control-label">上级区域:</label>
      <div class="col-md-3">
        <input type="hidden" name="parent.id" value="${area.parent.id!}">
        <input type="text" class="form-control" value="${area.parent.code!} ${area.parent.name!}" disabled>
      </div>
    </div>
    </#if>
    <div class="form-group">
      <label class="col-md-1 control-label">编码:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${area.code!}" data-parsley-required="true" data-parsley-remote="${base}/settings/area/exists?oldCode=${area.code!}"  data-parsley-error-message="代码必须填写并不能重复">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">名称:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="name" id="name" value="${area.name!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">说明:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="remark" id="remark" value="${area.remark!}">
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