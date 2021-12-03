<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/carrier">承运商信息</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#carrier-toolbar" id="carrier-datagrid" 
      data-options="url:'${base}/settings/carrier/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'code',width:100">编码</th>
        <th data-options="field:'fullName',width:100">全称</th>
        <th data-options="field:'shortName',width:100">简称</th>
        <th data-options="field:'carrierTypeName',width:100">类别</th>
        <th data-options="field:'address',width:100">详细地址</th>
        <th data-options="field:'intro',width:100">车队资料</th>
        <th data-options="field:'linkman',width:100">联系人</th>
        <th data-options="field:'tel',width:100">电话</th>
        <th data-options="field:'fax',width:100">传真</th>
        <th data-options="field:'rank',width:100">承运人评级</th>
        
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="carrier-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/carrier/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="carrierjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="carrierjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">全称</label>
              <input type="text" class="form-control" id="fullName" name="fullName" placeholder="编码或简称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="carrierjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <#--<button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
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
  var carrierjs = {
    search: function(){  
      $('#carrier-datagrid').datagrid('load',{
          name: $('#fullName').val()
      });
    },
    
    edit: function(){
      var row = $('#carrier-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/carrier/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#carrier-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/carrier/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
