<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/system-config">系统参数</a> - 
    <#if systemConfig.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="systemConfig-form" action="${base}/settings/system-config/${systemConfig.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="createdBy" value="${systemConfig.createdBy!}">
    <input type="hidden" name="createdTime" value="${systemConfig.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
  	<input type="hidden" name="attribute" id="attribute" value="${systemConfig.attribute!}"  >
   <!-- <div class="form-group">
      <label class="col-md-1 control-label">属性:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${systemConfig.attribute!}" data-parsley-required="true" >
      </div>
    </div>-->
    <div class="form-group">
      <label class="col-md-2 control-label">${systemConfig.attribute!}：</label>
      <div class="col-md-3">
        <#if systemConfig.attribute='是否收取违约金' >
        		 <input type="radio" name="value" checked value="1"  <#if  systemConfig.value=='1'>checked</#if>>收取
                 <input type="radio" name="value" value="0"  <#if  systemConfig.value=='0'>checked</#if>>不收取
       
     	<#elseif systemConfig.attribute='报表服务器密码' >
     			<input type="password" class="form-control" name="value" id="value" value="${systemConfig.value!}" data-parsley-required="true">
     	<#else>
     	 		<input type="text" class="form-control" name="value" id="value" value="${systemConfig.value!}" data-parsley-required="true">
     	</#if>
     	 		
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>
</#escape>