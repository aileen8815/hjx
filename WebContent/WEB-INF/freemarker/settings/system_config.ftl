<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/system-config">系统参数</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#system-config-toolbar" id="system-config-datagrid" 
      data-options="url:'${base}/settings/system-config/list',method:'get',rownumbers:true,singleSelect:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'attribute',width:100">属性</th>
        <th data-options="field:'value',width:100,formatter: rowformater">值</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="system-config-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <!--  <a href="${base}/settings/system-config/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>-->
            <button id="person-edit" type="button" class="btn btn-primary" onclick="systemconfigjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
           <!-- <button id="person-delete" type="button" class="btn btn-primary" onclick="systemconfigjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>-->
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
  var systemconfigjs = {
    edit: function(){
      var row = $('#system-config-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/system-config/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#system-config-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/system-config/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
   	function rowformater(value,row,index){
  		if(row.attribute=='是否收取违约金'){
  			if(value=='1'){
  				return "收取";
  			}else{
  				return "不收取";
  			}
  		}else{
  			return	value;
  		}	 
  	}
</script>

</#escape>
