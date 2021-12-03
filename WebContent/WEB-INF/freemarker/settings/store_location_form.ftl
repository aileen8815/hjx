<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-location/">储位信息</a> - 
    <#if storeLocation.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="person-form" action="${base}/settings/store-location/${storeLocation.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeLocation.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeLocation.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
   <input type="hidden" name="code" value="${storeLocation.code!}">
    <div class="form-group">
      <label class="col-md-1 control-label">所属仓间:</label>
      <div class="col-md-3">
       <select id="storeAreaId" name="storeArea.id" class="form-control" data-parsley-required="true">
       <option value="">请选择仓间</option>
       <#list storeAreas as area>
         <option value="${area.id}" <#if area.id == storeLocation.storeArea.id>selected</#if>>
            <#if area.parent?exists>&nbsp;&nbsp;&nbsp;&nbsp;</#if>${area.text!}
         </option>
       </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">储位类型:</label>
      <div class="col-md-3">
       <select id="storeLocationTypeId" name="storeLocationType.id" class="form-control">
       <#list storeLocationTypes as storeLocationType>
       	 <#if   storeLocationType.code!=3>
         <option value="${storeLocationType.id}" <#if storeLocationType.id == storeLocation.storeLocationType.id>selected</#if>>${storeLocationType.name}</option>
       	 </#if>
       </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">标签号:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="label" id="label" value="${storeLocation.label!}" readonly >
      </div>
    </div>
   
    <#if storeLocation.label?has_content>
    <div class="form-group">
      <label class="col-md-1 control-label">状态:</label>
      <div class="col-md-3">
        <#if (storeLocation.storeLocationStatus != '使用中')>
            <select name="storeLocationStatus" id="storeLocationStatus" class="form-control">
            <#list storeLocationStatusList as status>
                <#if status_index > 0>
                <option value="${status}" <#if status == storeLocation.storeLocationStatus>selected</#if>>${status}</option>
                </#if>
            </#list>
            </select>
        <#else>
            <input type="hidden" name="storeLocationStatus" value="${storeLocation.storeLocationStatus}">
            <label class="control-label">使用中（不可修改）</label>
        </#if>
      </div>
    </div>
    <#else>
        <input type="hidden" name="storeLocationStatus" value="${storeLocation.storeLocationStatus}">
    </#if>
    <div class="form-group">
      <label class="col-md-1 control-label">行:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordX" id="coordX" value="${storeLocation.coordX!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">列:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordY" id="coordY" value="${storeLocation.coordY!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">层:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordZ" id="coordZ" value="${storeLocation.coordZ!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">长:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="length" id="length" value="${storeLocation.length!}" data-parsley-required="true"  max="200">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-1 control-label">宽:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="width" id="width" value="${storeLocation.width!}" data-parsley-required="true"  max="200">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-1 control-label">高:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="height" id="height" value="${storeLocation.height!}" data-parsley-required="true"  max="200">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">承重:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="weight" id="weight" value="${storeLocation.weight!}" data-parsley-required="true" >
            <span class="input-group-addon">公斤</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">描述:</label>
      <div class="col-md-3">
     <input type="text" class="form-control" name="remark" id="remark" value="${storeLocation.remark!}">
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
