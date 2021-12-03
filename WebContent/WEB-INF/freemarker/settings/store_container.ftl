<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-container">容器管理</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store-container-type-toolbar" id="store-container-datagrid" 
      data-options="url:'${base}/settings/store-container/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'label',width:150,fixed:true">标签号</th>
        <th data-options="field:'storeContainerTypeName',width:100,fixed:true">类型</th>
        <th data-options="field:'storeContainerStatus',width:100,fixed:true">状态</th>
        <th data-options="field:'length',width:100,fixed:true">长</th>
        <th data-options="field:'width',width:100,fixed:true">宽</th>
        <th data-options="field:'height',width:100,fixed:true">高</th>
        <th data-options="field:'weight',width:100">承重</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store-container-type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-6">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/store-container/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <a href="${base}/settings/store-container/batch-add" class="btn btn-primary"><i class="fa fa-plus"></i> 批量新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="storecontainerjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storecontainerjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div style="text-align:right">
          <form class="form-inline" role="form">
              <div class="form-group">
                    <label class="sr-only" for="a">容器类型</label>
                    <select id="storeContainerType" name="storeContainerType" class="form-control" style="width:150px;text-align: left;">
                        <option value="">选择类型</option>
                        <#list storeContainerTypes as storeContainerType>
                            <option value="${storeContainerType.id}">${storeContainerType.code} ${storeContainerType.name}</option>
                        </#list>
                    </select>
             </div>
            <div class="form-group">
              <input type="text" class="form-control" id="label" name="label" placeholder="标签号">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storecontainerjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var storecontainerjs = {
    search: function(){  
      $('#store-container-datagrid').datagrid('load',{
          label: $('#label').val(),
          storeContainerTypeId: $('#storeContainerType').val()
      });
    },
    
    edit: function(){
      var row = $('#store-container-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/store-container/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#store-container-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/store-container/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
