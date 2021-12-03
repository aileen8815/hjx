<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/operator">操作员</a> -
    <#if operator.id?exists>编辑<#else>新建</#if>
</header>

<div class="panel-body main-content-wrapper site-min-height">
    <form id="operator-form" action="${base}/security/operator/${operator.id!}" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${operator.createdBy!}">
        <input type="hidden" name="createdTime" value="${operator.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">

        <div class="form-group">
            <label class="col-md-2 control-label">登录名:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="username" id="username" value="${operator.username!}" <#if operator.id?exists>readonly</#if>
                       data-parsley-required="true"
                       data-parsley-remote="${base}/security/operator/valid-username?oldUsername=${operator.username!}"
                       data-parsley-error-message="登录名必须填写并不能重复">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">登录密码:</label>

            <div class="col-md-4">
                <input type="password" class="form-control" name="password" id="password" value="" <#if !operator.id?exists>data-parsley-required="true"</#if>>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">确认密码:</label>

            <div class="col-md-4">
                <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" value=""
                       <#if !operator.id?exists>data-parsley-required="true"</#if>  data-parsley-equalto="#password">
            </div>
        </div>
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
                        <input type="radio" class="radio-inline" name="sex" value="${sex}" <#if sex == operator.sex>checked</#if>
                               <#if sex_index == 0>data-parsley-required="true"</#if>> ${sex}
                    </#list>
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">生日:</label>

            <div class="col-md-4">
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="birthday" id="birthday"
                       value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">所属部门:</label>

            <div class="col-md-4">
                <select class="form-control" name="department.id" id="departmentId">
                    <#list departments as d>
                        <option value="${d.id}" <#if d.id == operator.department.id>selected</#if>>${d.text}</option>
                    </#list>
                </select>
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
                <input type="text" class="form-control" name="email" id="email" value="${operator.email!}" data-parsley-type="email">
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
                <input type="text" class="form-control" name="zip" id="zip" value="${operator.zip!}" data-parsley-type="integer">
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
    <#-- 暂时不维护描述 --
    <div class="form-group">
      <label class="col-md-2 control-label">描述:</label>
      <div class="col-md-4">
        <input type="text" class="form-control" name="name" id="name" value="${operator.name!}">
      </div>
    </div>
    -->
        <div class="form-group">
            <label class="col-md-2 control-label">授予角色:</label>

            <div class="col-md-4">
                <div>
                    <#list roles as role>
                        <input type="checkbox" class="radio-inline" name="assignRoles" value="${role.id!}"
                               <#if operator.roles?contains(role)>checked</#if>
                               <#--<#if role_index == roles?size - 1>data-parsley-required="true"</#if>-->> ${role.name!} <br/>
                    </#list>
                </div>
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

