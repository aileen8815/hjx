<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/c-resource-management">资源管理</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#resourcemanagement-toolbar" id="resourcemanagement-datagrid" 
      data-options="url:'${base}/customer/c-resource-management/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'vehicleNo',width:20">车牌号码</th>
        <th data-options="field:'vehicleTypeName',width:20">车辆类型</th>
        <th data-options="field:'weight',width:20">载重量</th> 
        <th data-options="field:'name',width:20">车主姓名</th>
        <th data-options="field:'telePhone',width:20">电话号码</th>
	    <th data-options="field:'startDate',width:140,fixed:true,formatter:formatterDate">开始日期</th>
        <th data-options="field:'endDate',width:140,fixed:true,formatter:formatterDate">结束日期</th>
     	<th data-options="field:'remark',width:50">备注</th>
      </tr>
    </thead>
  </table>
  <#-- 表格工具条 -->
  <div class="col-md-12" id="resourcemanagement-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
          <#--
            <a href="${base}/customer/c-resource-management/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="resourcemanagement-edit" type="button" class="btn btn-primary" onclick="tooljs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="resourcemanagement-delete" type="button" class="btn btn-primary" onclick="tooljs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          -->
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var tooljs = {
    edit: function(){
      var row = $('#resourcemanagement-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/customer/c-resource-management/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#resourcemanagement-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/customer/c-resource-management/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
  function formatterDate(value) {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
        dateFormat(date, 'yyyy-mm-dd HH:MM:SS');
        return date.format('yyyy-mm-dd HH:MM:ss');
    }
</script>

</#escape>
