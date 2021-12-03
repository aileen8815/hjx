<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/profile/change-password">修改密码</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#if message?exists>
  <div class="alert alert-warning">${message!}</div>
  </#if>
  <#if true != success>
  <form id="person-form" action="${base}/security/profile/change-password" method="post" class="form-horizontal" data-parsley-validate>
    <div class="form-group">
      <label class="col-md-2 control-label">请输入旧密码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="oldPassword" id="oldPassword" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">请输入新密码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="newPassword" id="newPassword" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">请输入确认密码:</label>
      <div class="col-md-4">
        <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" data-parsley-required="true" data-parsley-equalto="#newPassword">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-4">
        <button type="submit" class="btn btn-primary">修改密码</button>
      </div>
    </div>
  </form>
  </#if>
</div>
</#escape>
