<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/stevedore-port">装卸口管理</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stevedore-port-toolbar" id="stevedore-port-datagrid" 
      data-options="url:'${base}/settings/stevedore-port/list',method:'get',rownumbers:true,singleSelect:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'code',width:100">编码</th>
        <th data-options="field:'name',width:100">名称</th>
        <th data-options="field:'stevedorePortStatus',width:100">状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stevedore-port-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/stevedore-port/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="stevedoreportjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="stevedoreportjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
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
  var stevedoreportjs = {
    edit: function(){
      var row = $('#stevedore-port-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/stevedore-port/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#stevedore-port-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/stevedore-port/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
