<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/tally-area">装卸口信息</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#tally-area-toolbar" id="tally-area-datagrid" 
      data-options="url:'${base}/settings/tally-area/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'code',width:100">编码</th>
        <th data-options="field:'name',width:100">名称</th>
        <th data-options="field:'tallyAreaTyreDesc',width:100">装卸口类型</th>
        <th data-options="field:'tallyAreaStatus',width:100">状态</th>
        <th data-options="field:'remark',width:100">备注</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="tally-area-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/tally-area/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="tallyareatypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="tallyareatypejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">名称</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="编码或名称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="tallyareatypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <#--
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul>
                -->
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var tallyareatypejs = {
    search: function(){  
      $('#tally-area-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#tally-area-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/tally-area/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#tally-area-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/tally-area/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
