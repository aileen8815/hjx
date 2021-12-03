<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/product-category">商品分类</a> - 
    <#if productCategory.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="department-form" action="${base}/settings/product-category/${productCategory.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${productCategory.createdBy!}">
    <input type="hidden" name="createdTime" value="${productCategory.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <#if productCategory.parent?exists>
    <div class="form-group">
      <label class="col-md-1 control-label">上级商品:</label>
      <div class="col-md-3">
      <input type="hidden" name="parent.id" value="${productCategory.parent.id!}">
        <input type="text" class="form-control" value="${productCategory.parent.code!} ${productCategory.parent.name!}" disabled>
      </div>
    </div>
    </#if>
    <div class="form-group">
      <label class="col-md-1 control-label">编码:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="code" id="code" value="${productCategory.code!}"    data-parsley-remote="${base}/settings/product-category/exists?oldCode=${productCategory.code!}"  data-parsley-required="true"  data-parsley-error-message="编码必须填写并不能重复">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-1 control-label">名称:</label>
      <div class="col-md-3">
        <input type="text" class="form-control" name="name" id="name" value="${productCategory.name!}" data-parsley-required="true">
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