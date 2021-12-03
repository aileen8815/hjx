<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/store-area-assignee">库区分派</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#operator-toolbar" id="operator-datagrid" 
      data-options="
        url:'${base}/store/store-area-assignee/list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'username',width:100,fixed:'true'">仓管员用户名</th>
        <th data-options="field:'name',width:100,fixed:'true'">仓管员名称</th>
       	<th data-options="field:'storeAreaAssigneeText',width:824,fixed:'true'">库区</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="operator-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/store-area-assignee/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 分派</a>
           <button id="operator-edit" type="button" class="btn btn-primary" onclick="operatorjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <#-- 不支持搜索   <button id="operator-delete" type="button" class="btn btn-primary" onclick="operatorjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>-->
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <#-- 不支持搜索 
        <div style="text-align:right">
          <form class="form-inline" operator="form">
            <div class="form-group">
              <label class="sr-only" for="a">姓名</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="姓名">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="operatorjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul operator="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul>
            </div>
          </form>
        </div>
        -->
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var operatorjs = {
    search: function(){  
      $('#operator-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#operator-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/store-area-assignee/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#operator-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/store-area-assignee/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
