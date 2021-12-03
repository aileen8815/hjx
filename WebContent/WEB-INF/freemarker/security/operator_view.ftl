<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/operator">操作员管理</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-2">登录名：</div>
    <div class="col-md-10">${operator.username!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">姓　名：</div>
    <div class="col-md-10">${operator.name!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">性　别：</div>
    <div class="col-md-10">${operator.sex!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">生　日：</div>
    <div class="col-md-10">${operator.birthday?default(.now)?string('yyyy-MM-dd')}</div>
  </div>
  <div class="row">
    <div class="col-md-2">所属部门：</div>
    <div class="col-md-10">${operator.department.text!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">手机号：</div>
    <div class="col-md-10">${operator.mobile!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">电子邮箱：</div>
    <div class="col-md-10">${operator.email!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">联系地址：</div>
    <div class="col-md-10">${operator.address!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">邮政编码：</div>
    <div class="col-md-10">${operator.zip!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">电　话：</div>
    <div class="col-md-10">${operator.tel!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">传　真：</div>
    <div class="col-md-10">${operator.fax!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">注册时间：</div>
    <div class="col-md-10">${operator.registerTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}</div>
  </div>
  <div class="row">
    <div class="col-md-2">最后登录时间：</div>
    <div class="col-md-10"><#if operator.lastLoginTime?exists>${operator.lastLoginTime?string('yyyy-MM-dd HH:mm:ss')}</#if></div>
  </div>
  <div class="row">
    <div class="col-md-2">最后登录IP：</div>
    <div class="col-md-10">${operator.lastLoginIpAddr!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">锁定状态：</div>
    <div class="col-md-10">${operator.lockStatus!}</div>
  </div>
  <div class="row">
    <div class="col-md-2">锁定时间：</div>
    <div class="col-md-10"><#if operator.lockTime?exists>${operator.lockTime?string('yyyy-MM-dd HH:mm:ss')}</#if></div>
  </div>
  <div class="row">
    <div class="col-md-2">授予角色：</div>
    <div class="col-md-10">
      <#list operator.roles as role>
        ${role.code!} ${role.name!}<br/>
      </#list>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
    <a href="${base}/security/operator/${operator.id}/edit" class="btn btn-primary">修改资料</a>
    </div>
  </div>
</div>
</#escape>