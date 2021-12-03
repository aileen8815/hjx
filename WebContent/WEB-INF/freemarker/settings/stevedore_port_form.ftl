<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/stevedore-port">装卸口管理</a> - 
    <#if stevedorePort.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="stevedorePort-form" action="${base}/settings/stevedore-port/${stevedorePort.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">    
    <div class="form-group">
      <label class="col-md-1 control-label">编码:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${stevedorePort.code!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">名称:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="name" id="name" value="${stevedorePort.name!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">状态:</label>
      <div class="col-md-3">
        <select name="stevedorePortStatus"  class="form-control m-bot15">
 			<#list stevedorePortStatus as Statuses>
 				<option value="${Statuses}"  <#if Statuses == stevedorePort.stevedorePortStatus>selected</#if>>${Statuses}</option>
   			</#list>
    	</select>
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