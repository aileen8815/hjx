<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-container">仓储容器</a> - 批量初始化
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="storeContainer-form" action="${base}/settings/store-container/batch-create" method="post" class="form-horizontal" data-parsley-validate>
    <div class="form-group">
      <label class="col-md-2 control-label">容器类型:</label>
      <div class="col-md-4">
       <select id="storeContainerTypeId" name="storeContainerTypeId" class="form-control">
       <#list storeContainerTypes as type>
         <option value="${type.id}" <#if type.id == storeContainer.storeContainerType.id>selected</#if>>${type.name}</option>
       </#list>
       </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">生成数量:</label>
      <div class="col-md-4">
        <input type="text" name="amount" class="form-control" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">长:</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" name="length" class="form-control" value="120" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">宽:</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" name="width" class="form-control" value="100" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">高:</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" name="height" class="form-control" value="20" data-parsley-required="true">
            <span class="input-group-addon">厘米</span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">承重:</label>
      <div class="col-md-4">
        <div class="input-group">
            <input type="text" name="weight" class="form-control" value="1000" data-parsley-required="true">
            <span class="input-group-addon">公斤</span>
        </div>
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
