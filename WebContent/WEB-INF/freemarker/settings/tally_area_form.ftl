<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/tally-area">装卸口信息</a> - 
    <#if tallyArea.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="tallyAreaType-form" action="${base}/settings/tally-area/${tallyArea.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${tallyAreaType.createdBy!}">
    <input type="hidden" name="createdTime" value="${tallyAreaType.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-1 control-label">编码:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${tallyArea.code!}" data-parsley-required="true"     
          data-parsley-remote="${base}/settings/tally-area/valid-code?oldCode=${tallyArea.code!}" 
          data-parsley-error-message="代码必须填写并不能重复">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">名称:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="name" id="name" value="${tallyArea.name!}" data-parsley-required="true">
      </div>
    </div>
     <div class="form-group">
      <label class="col-md-1 control-label">类型:</label>
      <div class="col-md-3">
         <select name="tallyAreaType.id"  class="form-control">
 			<#list tallyAreaTypes as tally_Area_type>
 				<option value="${tally_Area_type.id}"  <#if tally_Area_type.id == tallyArea.tallyAreaType.id>selected</#if>>${tally_Area_type.name}</option>
   			</#list>
    	</select>
      </div>
    </div>
     <div class="form-group">
      <label class="col-md-1 control-label">状态:</label>
      <div class="col-md-3">
        <select name="tallyAreaStatus"  class="form-control">
 			<#list tallyAreaStatuses as Statuses>
 				<option value="${Statuses}"  <#if Statuses == tallyArea.tallyAreaStatus>selected</#if>>${Statuses}</option>
   			</#list>
    	</select>
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-1 control-label">备注:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="remark" id="remark" value="${tallyArea.remark!}" >
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