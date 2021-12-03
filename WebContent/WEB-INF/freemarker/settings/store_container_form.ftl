<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-container">储藏容器信息</a> - 
    <#if storeContainer.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeContainer-form" action="${base}/settings/store-container/${storeContainer.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeContainer.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeContainer.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="code1" value="${storeContainer.code1!}">
    <input type="hidden" name="code2" value="${storeContainer.code2!}">
    <div class="form-group">
      <label class="col-md-2 control-label">标签号:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="label" id="label" value="${storeContainer.label!}" readonly >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">长</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" class="form-control" name="length" id="length" value="${storeContainer.length!}" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-2 control-label">宽</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" class="form-control" name="width" id="width" value="${storeContainer.width!}" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-2 control-label">高</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" class="form-control" name="height" id="height" value="${storeContainer.height!}" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">承重</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" class="form-control" name="weight" id="weight" value="${storeContainer.weight!}" data-parsley-required="true">
            <span class="input-group-addon">公斤</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">容器类型:</label>
      <div class="col-md-4">
       <select id="storeContainerTypeId" name="storeContainerType.id" class="form-control">
       <#list storeContainerTypes as type>
     	 <option value="${type.id}" <#if type.id == storeContainer.storeContainerType.id>selected</#if>>${type.name}</option>
	   </#list>
       </select>
      </div>
    </div>
    <#if storeContainer.id?has_content>
    <div class="form-group">
      <label class="col-md-2 control-label">状态:</label>
      <div class="col-md-4">        
          <select id="storeContainerStatus" name="storeContainerStatus" class="form-control">
          <#list storeContainerStatus as status>
            <#if (status_index > 0)>
    		<option value="${status}" <#if status == storeContainer.storeContainerStatus>selected</#if>>${status}
    		</#if>
    	  </#list>
    	  </select>
      </div>
    </div>
    <#else>
        <input type="hidden" name="storeContainerStatus" value="${storeContainer.storeContainerStatus}">
    </#if>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-4">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>
</#escape>