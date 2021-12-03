<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-adjust">库存调整</a>
</header>
<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-2">业务单号：</div>
    <div class="col-md-10">${stockAdjust.serialNo!}</div>
     <div class="col-md-2">业务类型：</div>
    <div class="col-md-10">${stockAdjust.stockAdjustType.name!}</div>
  </div>
  <div class="row">
 	 <div class="col-md-2">审核状态：</div>
     <div class="col-md-10">${stockAdjust.stockAdjustStatus!}</div> 
     <div class="col-md-2">备注：</div>
     <div class="col-md-10">${stockAdjust.remark!}</div> 
  </div>

</br>


  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_location_type-toolbar" id="store_location_type-datagrid" 
      data-options="url:'${base}/store/stock-adjust/items?stockAdjustId=${stockAdjustId}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'productName',width:100">商品</th>
        <th data-options="field:'weight',width:100">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:100">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'storeLocationCode',width:100">储位</th>
        <th data-options="field:'qualityGuaranteePeriod',width:100">保质期（天）</th>
        <th data-options="field:'storeDuration',width:100">预期保管时间（天）</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store_location_type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/stock-adjust/new-item?stockAdjustId=${stockAdjustId}" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="stockaAdjustjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="stockaAdjustjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-10">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">名称</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="名称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="stockaAdjustjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var stockaAdjustjs = {
    search: function(){  
      $('#store_location_type-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
     	var serialNo= $('#serialNo').val();
        location.href = "${base}/store/stock-adjust/" + row.id + "/edit-item?stockAdjustId=${stockAdjust.id}";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          var serialNo= $('#serialNo').val();
          location.href = "${base}/store/stock-adjust/" + row.id + "/delete-item?stockAdjustId=${stockAdjust.id}";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
