<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/role">角色</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#role-toolbar" id="role-datagrid" 
      data-options="
        url:'${base}/security/role/list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'code',width:100,fixed:'true'">编码</th>
        <th data-options="field:'name',width:200,fixed:'true'">名称</th>
        <th data-options="field:'remark',width:200">说明</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="role-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/security/role/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="role-edit" type="button" class="btn btn-primary" onclick="rolejs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="role-delete" type="button" class="btn btn-primary" onclick="rolejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var rolejs = {
    edit: function(){
      var row = $('#role-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/security/role/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#role-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/security/role/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
