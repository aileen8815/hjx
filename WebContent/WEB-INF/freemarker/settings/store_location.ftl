<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-location">储位信息</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#person-toolbar" id="person-datagrid" 
      data-options="url:'${base}/settings/store-location/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
      <tr>
      <th data-options="field:'areaText',width:100,fixed:true">所属仓间</th>
        <th data-options="field:'code',width:120,fixed:true">编码</th>
        <th data-options="field:'label',width:120,fixed:true">标签号</th>
        <th data-options="field:'coordX',width:40,fixed:true">行</th>
        <th data-options="field:'coordY',width:40,fixed:true">列</th>
        <th data-options="field:'coordZ',width:40,fixed:true">层</th>
        <th data-options="field:'storeLocationTypeText',width:60,fixed:true">类型</th>
        <th data-options="field:'length',width:40,fixed:true">长</th>
        <th data-options="field:'width',width:40,fixed:true">宽</th>
        <th data-options="field:'height',width:40,fixed:true">高</th>
        <th data-options="field:'weight',width:40,fixed:true">承重</th>
        <th data-options="field:'remark',width:100">描述</th>
        <th data-options="field:'storeLocationStatus',width:60,fixed:true">状态</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="person-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/store-location/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            
          	<a href="${base}/settings/store-location/batch-add" class="btn btn-primary"><i class="fa fa-plus-square"></i>批量新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="storeLocation.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storeLocation.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
      
            
            <div class="form-group">
                    <label class="sr-only" for="a"> 储位类型</label>
                    <select id="storeLocationType" name="storeLocationType" class="form-control" style="width:150px;text-align: left;">
                        <option value="">选择类型</option>
                        <#list storeLocationTypes as storeLocationType>
                            <option value="${storeLocationType.id}">${storeLocationType.code} ${storeLocationType.name}</option>
                        </#list>
                    </select>
             </div>
             
             <div class="form-group">
                    <label class="sr-only" for="a"> 所属仓间</label>
                    <select id="storeArea" name="storeArea" class="form-control" style="width:150px;text-align: left;">
                        <option value="">选择仓间</option>
                        <#list storeAreas as storeArea>
                            <option value="${storeArea.id}">${storeArea.code} ${storeArea.name}</option>
                        </#list>
                    </select>
             </div>
                
             <div class="form-group">
                    <label class="sr-only" for="a"> 状态</label>
                    <select id="storeLocationStatus" name="storeLocationStatus" class="form-control" style="width:150px;text-align: left;">
                        <option value="">选择状态</option>
                        <#list storeLocationStatus as storeLocationStatu>
                            <option value="${storeLocationStatu}">${storeLocationStatu}</option>
                        </#list>
                    </select>
             </div>
             <div class="form-group">
              <label class="sr-only" for="a">编码</label>
              <input type="text" class="form-control" id="code" name="code" placeholder="编码">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storeLocation.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var storeLocation = {
    search: function(){  
      $('#person-datagrid').datagrid('load',{
          storeLocationStatus: $('#storeLocationStatus').val(),
          storeAreaId: $('#storeArea').val(),
          storeLocationTypeId: $('#storeLocationType').val(),
          code: $('#code').val()
      });
    },
    
    edit: function(){
      var row = $('#person-datagrid').datagrid('getSelected');  
      if (row){
      	if(row.storeLocationTypeText=='周转区'){
        	 alert("周转区不可维护！");
        	 return ;
        	}
        location.href = "${base}/settings/store-location/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#person-datagrid').datagrid('getSelected');  
      if (row){
      	if(row.storeLocationTypeText=='周转区'){
        	 alert("周转区不可维护！");
        	 return ;
        }
        if(confirm('确实要删除选中数据吗？')){
        	location.href = "${base}/settings/store-location/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>
</#escape>
