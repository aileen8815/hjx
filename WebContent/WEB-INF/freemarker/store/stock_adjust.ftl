<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-adjust">库存调整</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stock-adjust-toolbar" id="stock-adjust-datagrid" 
      data-options="url:'${base}/store/stock-adjust/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">业务单号</th>
       	<th data-options="field:'stockAdjustTypeName',width:100">业务类型</th>
        <th data-options="field:'stockAdjustStatus',width:100">审核状态</th>
        <th data-options="field:'remark',width:100">备注</th>

     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stock-adjust-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-6">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/stock-adjust/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="stockAdjustjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="stockAdjustjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>               	
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">业务类型</label>
              <select id="adjustType" name="adjustType" class="form-control">
      			 <option value="">业务类型</option>
      				 <#list adjustTypes as adjustType>
     	 			 <option value="${adjustType.id}"  >&nbsp;&nbsp;&nbsp;${adjustType.name}&nbsp;&nbsp;&nbsp;&nbsp;</option>
	  				 </#list>
     		  </select>&nbsp;&nbsp;&nbsp;
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="stockAdjustjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                 
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var stockAdjustjs = {
    search: function(){  
      $('#stock-adjust-datagrid').datagrid('load',{
          stockAdjustType: $('#adjustType').val()
      });
    },
    
    edit: function(){
      var row = $('#stock-adjust-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/stock-adjust/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    remove: function(){
      var row = $('#stock-adjust-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/stock-adjust/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
    function rowformater(value,row,index){
    var url="${base}/store/stock-adjust/index-item?stockAdjustId="+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
