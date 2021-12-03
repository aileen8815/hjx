<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract">租赁合同</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store-contract-toolbar" id="store-contract-datagrid" 
      data-options="
        url:'${base}/tenancy/store-contract/list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'serialNo',width:140,formatter:rowformater">合同流水号</th>
		<th data-options="field:'contractNo',width:130">合同编号</th>        
        <th data-options="field:'customerText',width:140">客户</th>
        <th data-options="field:'storeAreaText',width:100">库区名称</th>     
        <th data-options="field:'rentalArea',width:70">使用面积</th>     
        <th data-options="field:'startDate',width:140,formatter:formatterDate">开始日期</th>
        <th data-options="field:'endDate',width:140,formatter:formatterDate">结束日期</th>
        <th data-options="field:'signedDate',width:140,formatter:formatterDate">签订日期</th>           
		<th data-options="field:'chargeDate',width:140,formatter:formatterDate">计费日期</th>
		<th data-options="field:'status',width:90">合同状态</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store-contract-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/tenancy/store-contract/new" class="btn btn-primary"><i class="fa fa-plus-square"></i>新建</a>
             <button id="store-contract-edit" type="button" class="btn btn-primary" onclick="storecontractjs.edit();"><i class="fa fa-edit"></i>编辑</button>
            <button id="store-contract-delete" type="button" class="btn btn-primary" onclick="storecontractjs.remove();"><i class="fa fa-trash-o"></i>删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" id="store-contract-form">
            <div class="form-group">
                <label class="sr-only" for="customerId">客户</label>
                <select class="form-control" id="customerId" name="customerId" style="width:100px;text-align:left;margin-right:10px;">
                    <option value="">客户</option>
                    <#list customers as c>
                    <option value="${c.id!}">${c.name!}</option> 
                    </#list>
                </select>
            </div>     
            
            <div class="form-group">
                <label class="sr-only" for="storeAreaId">库区</label>
                <select class="form-control" id="storeAreaId" name="storeAreaId" style="width:100px;text-align:left;margin-right:10px;">
                    <option value="">库区</option>
                    <#list storeAreas as t>
                    <option value="${t.id!}">${t.name!}</option> 
                    </#list>
                </select>
            </div>       
            
            <div class="form-group">
                <label class="sr-only" for="status">状态</label>
                <select class="form-control" id="status" name="status" style="width:100px;text-align:left;margin-right:10px;">
                    <option value="">状态</option>
                    <#list status as s>
                    <option value="${s}">${s}</option> 
                    </#list>
                </select>
            </div>        

            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storecontractjs.search();" type="button"><i class="fa fa-search"></i>查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
            </div>
          </form>
        </div>
         
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storecontractjs = {
    search: function(){  
      $('#store-contract-datagrid').datagrid('load',{
          customerId: $('#customerId').val(),
          status: $('#status').val(),
          storeAreaId: $('#storeAreaId').val()
      });
    },
    
    edit: function(){
      var row = $('#store-contract-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/tenancy/store-contract/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#store-contract-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/tenancy/store-contract/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
    function rowformater(value,row,index){
    var url="${base}/tenancy/store-contract/"+row.id + "/view";
    var href="<a href='"+url+"' >"+value+"</a>"; 
 	 return href;
  };
  
	function formatterDate(value){
		var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
        dateFormat(date,'yyyy-mm-dd');
        return date.format('yyyy-mm-dd');
	}  
</script>

</#escape>
