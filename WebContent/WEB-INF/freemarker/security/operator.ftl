<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/operator">操作员</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#operator-toolbar" id="operator-datagrid" 
      data-options="
        url:'${base}/security/operator/list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'username',width:100,fixed:'true',formatter:formatUsername">登录名</th>
        <th data-options="field:'name',width:200,fixed:'true'">姓名</th>
        <th data-options="field:'departmentName',width:200">部门</th>
        <th data-options="field:'sex',width:200">性别</th>
        <th data-options="field:'mobile',width:200">手机</th>
        <th data-options="field:'email',width:200">电子邮箱</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="operator-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/security/operator/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="operator-edit" type="button" class="btn btn-primary" onclick="operatorjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="operator-delete" type="button" class="btn btn-primary" onclick="operatorjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" operator="form">
            <div class="form-group">
              <label class="sr-only" for="a">姓名</label>
              <input type="text" class="form-control" id="username" name="username" placeholder="用户名">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="operatorjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  function formatUsername(val, row){
    return '<a href="${base}/security/operator/' + row.id + '/view">' + val + '</a>';
  }
        
  var operatorjs = {
    search: function(){  
      $('#operator-datagrid').datagrid('load',{
          username: $('#username').val()
      });
    },
    
    edit: function(){
      var row = $('#operator-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/security/operator/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#operator-datagrid').datagrid('getSelected');  
      if (row){
        if(row.id == 1){
          alert('不允许删除超级管理员');
          return;
        }
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/security/operator/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
