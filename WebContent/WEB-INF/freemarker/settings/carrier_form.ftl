<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/carrier">承运商信息</a> - 
    <#if carrier.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="carrier-form" action="${base}/settings/carrier/${carrier.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${carrier.createdBy!}">
    <input type="hidden" name="createdTime" value="${carrier.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-2 control-label">编码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="code" id="code" value="${carrier.code!}"  data-parsley-required="true">
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">简称:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="shortName" id="shortName" value="${carrier.shortName!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">全称:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="fullName" id="fullName" value="${carrier.fullName!}">
      </div>
    </div>
     <div class="form-group">
      <label class="col-md-2 control-label">类型:</label>
      <div class="col-md-4">
         <select name="carrierType.id" class="form-control abc">
 			<#list carrierTypes as carrier_type>
 				<option value="${carrier_type.id}"  <#if carrier_type.id == carrier.carrierType.id>selected</#if>>${carrier_type.name}</option>
   			</#list>
    	</select>
      </div>
    </div>
     <div class="form-group">
      <label class="col-md-2 control-label">详细地址:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="address" id="address" value="${carrier.address!}">
      </div>
    </div>
   	<div class="form-group">
      <label class="col-md-2 control-label">车队资料:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="intro" id="intro" value="${carrier.intro!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">联系人:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="linkman" id="linkman" value="${carrier.linkman!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">电话:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="tel" id="tel" value="${carrier.tel!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">传真:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="fax" id="fax" value="${carrier.fax!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">承运人评级:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="rank" id="rank" value="${carrier.rank!}">
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