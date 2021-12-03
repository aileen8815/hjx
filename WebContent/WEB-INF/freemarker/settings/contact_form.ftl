<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/customer/${customer.id!}">客户信息</a> - 
    <#if contact.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="customer-form" action="${base}/settings/customer/${contact.id!}/save-contact?" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${contact.createdBy!}">
    <input type="hidden" name="createdTime" value="${contact.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="customerId" id="customerId" value="${customer.id!}" data-parsley-required="true" >
     <input type="hidden" class="form-control" name="id" id="id" value="${contact.id!}"  >
    <div class="form-group">
      <div class="col-md-2"   align="right">客户ID:</div>
      <div class="col-md-4">
        ${customer.custemerId!}
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-2"   align="right">客户姓名:</div>
      <div class="col-md-4">
    	 ${customer.name!}
      </div>
    </div>
     <div class="form-group">
      <div class="col-md-2"   align="right">身份证号:</div>
      <div class="col-md-4">
       ${customer.credentialsNo!}
      </div>
    </div>

     <div class="form-group">
      <label class="col-md-2 control-label">联系人:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="linkman" id="linkman" value="${contact.linkman!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">手机:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="mobile" id="mobile" value="${contact.mobile!}" data-parsley-required="true">
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">电话:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="tel" id="tel" value="${contact.tel!}"  >
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">传真:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="fax" id="fax" value="${contact.fax!}"  >
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">电子信箱:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="email" id="email" value="${contact.email!}"  data-parsley-type="email">
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">邮编:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="zip" id="zip" value="${contact.zip!}" 	>
      </div>
    </div>
      <div class="form-group">
      <label class="col-md-2 control-label">地址:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="address" id="address" value="${contact.address!}" >
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