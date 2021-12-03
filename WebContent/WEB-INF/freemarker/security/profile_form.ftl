<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/profile">个人资料</a> - 编辑
</header>

<div class="panel-body main-content-wrapper site-min-height">
  <form id="operator-form" action="${base}/security/profile/update" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="username" value="${operator.username!}">
    <input type="hidden" name="createdBy" value="${operator.createdBy!}">
    <input type="hidden" name="createdTime" value="${operator.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-2 control-label">姓名:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="name" id="name" value="${operator.name!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">性别:</label>
      <div class="col-md-4">
        <p>
        <#list sexes as sex>
          <input type="radio" class="radio-inline" name="sex" value="${sex}" <#if sex == operator.sex>checked</#if> <#if sex_index == 0>data-parsley-required="true"</#if>> ${sex}
        </#list>
        </p> 
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">生日:</label>
      <div class="col-md-4">
        <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="birthday" id="birthday" value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">所属部门:</label>
      <div class="col-md-4">
        <input class="easyui-combotree" id="combo-operator" value="${operator.department.id!}"
          data-options="
            url:'${base}/settings/department/tree',
            onShowPanel: resizeCombo,
            onChange: changeDepartment,
            method:'get'" 
          style="width:200px; height:34px;">
        <input type="hidden" id="departmentId" name="department.id" value="${operator.department.id!}" data-parsley-required="true">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">手机号码:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="mobile" id="mobile" value="${operator.mobile!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">电子邮箱:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="email" id="email" value="${operator.email!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">联系地址:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="address" id="address" value="${operator.address!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">邮编:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="zip" id="zip" value="${operator.zip!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">电话:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="tel" id="tel" value="${operator.tel!}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">传真:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="fax" id="fax" value="${operator.fax!}">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-4">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>  
</div>

<script>
  <#-- 令combotree与input等宽  -->
  function resizeCombo(){
    var width = $('#name').width() + 27;
    $('.easyui-combotree').combotree('resize', width);
  }
  
  var hiidenDepartment = $('#departmentId');
  function changeDepartment(newValue, oldValue){
    $(hiidenDepartment).val(newValue);
  }
  
  $(function(){
    resizeCombo();
  });
</script>
</#escape>

