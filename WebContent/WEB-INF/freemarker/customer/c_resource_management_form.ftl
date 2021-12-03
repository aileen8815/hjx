<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/customer/c-resource-management">资源管理</a> - 
  <#if vehicleType.id?exists>编辑<#else>新建</#if> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="person-form" action="${base}/customer/c-resource-management/${resourceManagement.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <div class="row">
      <div class="form-group">
        <label class="col-md-2 control-label">车牌号码:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="vehicleNo" id="vehicleNo" value="${resourceManagement.vehicleNo!}" data-parsley-required="true" data-parsley-validate>
        </div>
        <label class="col-md-2 control-label">车辆类型:</label>
        <div class="col-md-3">
          <select id="vehicleTypeId" name="vehicleType.id" class="form-control">
            <option value="">请选择车辆类型</option>
            <#list vehicleTypes as vehicle> <option value="${vehicle.id}" <#if vehicle.id == resourceManagement.vehicleType.id>selected</#if>>${vehicle.name}
            </option>
            </#list>
          </select>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-2 control-label">载重量:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="weight" id="weight" value="${resourceManagement.weight!}">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-2 control-label">开始日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" name="startDate" id="startDate"
                                       value="${resourceManagement.startDate?default(.now)?string('yyyy-MM-dd HH:mm:ss')}" placeholder="开始日期"
                                       data-parsley-required="true">
        </div>
        <label class="col-md-2 control-label">结束日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" name="endDate" id="endDate"
                                       value="${resourceManagement.endDate?default(.now)?string('yyyy-MM-dd HH:mm:ss')}" placeholder="结束日期"
                                       data-parsley-required="true">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-2 control-label">车主姓名:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="name" id="name" value="${resourceManagement.name!}" data-parsley-required="true" data-parsley-validate>
        </div>
        <label class="col-md-2 control-label">电话号码:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="telePhone" id="telePhone" value="${resourceManagement.telePhone!}" data-parsley-required="true" data-parsley-validate>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-2 control-label">备注:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="remark" id="remark" value="${resourceManagement.remark!}">
        </div>
      </div>
    </div>
     <div class="row">
      <div class="form-group">
    <div class="col-md-offset-2 col-md-3">
      <button type="submit" class="btn btn-primary">保存</button>
    </div>
        </div>
    </div>
  </form>
</div>
</#escape> 