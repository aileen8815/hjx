<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/person">人员资料</a> - 
    <#if person.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="person-form" action="${base}/person/${person.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${person.createdBy!}">
    <input type="hidden" name="createdTime" value="${person.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-2 control-label">名字:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="firstName" id="firstName" value="${person.firstName!}" data-parsley-required="true" data-parsley-type="email">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">姓氏:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="lastName" id="lastName" value="${person.lastName!}" data-parsley-required="true">
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
