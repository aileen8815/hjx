<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/department">组织结构</a> - 
    <#if department.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="department-form" action="${base}/settings/department/${department.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${department.createdBy!}">
    <input type="hidden" name="createdTime" value="${department.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <#if department.parent?exists>
    <div class="form-group">
      <label class="col-md-2 control-label">上级部门:</label>
      <div class="col-md-4">
        <input type="hidden" name="parent.id" value="${department.parent.id!}">
        <input type="text" class="form-control" value="${department.parent.code!} ${department.parent.name!}" disabled>
      </div>
    </div>
    </#if>
    <div class="form-group">
      <label class="col-md-2 control-label">编码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="code" id="code" value="${department.code!}" 
          data-parsley-required="true" 
          data-parsley-remote="${base}/settings/department/valid-code?oldCode=${department.code!}" 
          data-parsley-error-message="代码必须填写并不能重复">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">名称:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="name" id="name" value="${department.name!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">说明:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="remark" id="remark" value="${department.remark!}">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-4">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>
</#escape>