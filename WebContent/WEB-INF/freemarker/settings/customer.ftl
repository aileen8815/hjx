<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/customer">仓储客户</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#customer-toolbar" id="customer-datagrid" 
      data-options="url:'${base}/settings/customer/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'sKHDM',width:100,formatter: rowformater">客户编号</th>
        <th data-options="field:'sMemberCode',width:100">卡号</th>
       	<th data-options="field:'name',width:100">客户名称</th>
       	<th data-options="field:'shortName',width:100">客户简称</th>
        <th data-options="field:'mobile',width:150">手机</th>
        <th data-options="field:'credentialsNo',width:150">证件证号</th>
        <th data-options="field:'customerGradeName',width:100">客户等级</th>
        <th data-options="field:'chargeTypeName',width:100">计费方式</th>
        <th data-options="field:'billDate',width:100">对账日</th>
        <th data-options="field:'payDate',width:100">付款日</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="customer-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/customer/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="customerjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="customerjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">名称</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="名称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="customerjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var customerjs = {
    search: function(){  
      $('#customer-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#customer-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/customer/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#customer-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/customer/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
      function rowformater(value,row,index){
    var url="${base}/settings/customer/"+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
