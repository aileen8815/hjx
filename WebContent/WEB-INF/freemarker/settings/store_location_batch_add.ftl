<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-location/">储位信息</a> - 批量初始化
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="person-form" action="${base}/settings/store-location/batch-create" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeLocation.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeLocation.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-1 control-label">所属仓间:</label>
      <div class="col-md-3">
       <select id="storeAreaId" name="storeArea.id" class="form-control" data-parsley-required="true">
       <option value="">请选择仓间</option>
       <#list storeAreas as area>
         <option value="${area.id}" <#if area.id == storeLocation.storeArea.id>selected</#if>>${area.text!}</option>
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
      <label class="col-md-1 control-label">行数:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordX" id="coordX" value="${storeLocation.coordX!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">列数:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordY" id="coordY" value="${storeLocation.coordY!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">层数:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="coordZ" id="coordZ" value="${storeLocation.coordZ!}" data-parsley-required="true" >
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">长:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="length" id="length" value="${storeLocation.length?default(120)}"  max="200" data-parsley-required="true" >
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-1 control-label">宽:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="width" id="width" value="${storeLocation.width?default(100)}"  max="200" data-parsley-required="true" >
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
        <div class="form-group">
      <label class="col-md-1 control-label">高:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="height" id="height" value="${storeLocation.height?default(180)}"  max="200" data-parsley-required="true" >
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">承重:</label>
      <div class="col-md-3">
        <div class="input-group">
            <input type="text" class="form-control" name="weight" id="weight" value="${storeLocation.weight?default(1000)}" data-parsley-required="true" >
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
