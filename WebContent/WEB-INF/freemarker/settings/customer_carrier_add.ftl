<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/customer">客户信息</a>
</header>
<input type="hidden" class="form-control" name="customerid" id="customerid" value="${customer.id}" >
<div class="panel-body main-content-wrapper site-min-height">



  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#customer-carrier-toolbar" id="carrier-datagrid" 
      data-options="url:'${base}/settings/carrier/list',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     	<th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'code',width:100">编码</th>        
        <th data-options="field:'shortName',width:100">简称</th>
        <th data-options="field:'fullName',width:100">全称</th>
        <th data-options="field:'carrierTypeName',width:100">类别</th>        
        <th data-options="field:'intro',width:100">车队资料</th>
        <th data-options="field:'linkman',width:100">联系人</th>
        <th data-options="field:'tel',width:100">电话</th>
        <th data-options="field:'fax',width:100">传真</th>
        <th data-options="field:'rank',width:100">承运人评级</th>
        <th data-options="field:'address',width:200">详细地址</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="customer-carrier-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
                <button id="person-add" type="button" class="btn btn-primary" onclick="customercarrierjs.add();"><i class="fa fa-plus-square"></i> 保存</button>
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
  var customercarrierjs = {
     add: function(){
      var row = $('#carrier-datagrid').datagrid('getSelected');  
      var ids = [];
      if (row){
     	var checkedItems = $('#carrier-datagrid').datagrid('getChecked');
		$.each(checkedItems, function(index, item){
			ids.push(item.id);
	    });
      }
      location.href = "${base}/settings/customer/create-customercarrier?customerid=${customer.id}&carrierids="+ids;
    }  
  };
  
	$('#carrier-datagrid').datagrid({
		onLoadSuccess:function(data){  
		   <#list carriers as carrier>
		   	  var carrierid = '${carrier.id}'; 
		      if(data){
			     $.each(data.rows, function(index,rows){
	 			 if(rows.id==carrierid){
					$('#carrier-datagrid').datagrid('checkRow', index);
					}
			    });
		      }
		   </#list>  
	   }                
	});

</script>

</#escape>
