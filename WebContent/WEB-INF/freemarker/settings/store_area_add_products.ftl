<#escape x as x?html>
<header class="panel-heading">
 
    <a href="${base}/settings/store-area/${storeArea.id}">仓间</a>
    
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
 
    <table class="easyui-datagrid" toolbar="#product-toolbar" id="product-treegrid" 
       data-options="url: '${base}/settings/customer/customer-products',
        method:'get',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false,onLoadSuccess:loadSuccess">
    <thead>
      <tr>
      	<th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'code', fixed:'true'" width="200">编码</th>
        <th data-options="field:'name',fixed:'true'" width="200">名称</th>
        <th data-options="field:'commonUnitName',width:100,fixed:true">常用计量单位</th>
        <th data-options="field:'commonPackingName',width:100,fixed:true">常用包装</th>
		<th data-options="field:'priceRange',width:100,fixed:true">参考价格区间</th>
        <th data-options="field:'remark',width:200">备注</th>
      </tr>
    </thead>
  </table>
  

  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="product-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-12">
        <div class="m-b-sm"  style="text-align:right">
           <form class="form-inline"    action="${base}/settings/store-area/save-products"   method="post"  onsubmit="return getData()">
           <input  type="hidden"    name="storeAreaId"   id="storeAreaId" value="${storeArea.id}">
           <input  type="hidden"    name="products"   id="products" value="${products}">
  		   
  		   <div  style="text-align:left" class="col-md-8">
  				<select id="customerId" name="customerId" class="form-control"   onchange="search(this.value)"  style="width:200px;text-align: left;" >
       				<option value="">请选择客户</option>
       				<#list customers as customer>
     				 <option value="${customer.id}" >${customer.text!}</option>
	  			 </#list>
       			</select>&nbsp;
       			<button type="submit" class="btn btn-primary">保存</button>
      	  </div>
     
       		
          
      
   
          </form>
        </div>
      </div>
 
    </div>
  </div>
</div>

<script type="text/javascript">
 
 
 	function    search(value){  
  
      $('#product-treegrid').datagrid('load',{
         id:value
 		})
 		 
      }
  	function loadSuccess(data){
		if(data){
			$.each(data.rows, function(index, item){
		      <#list storeArea.products as product>
			      if(item.id=='${product.id}'){ 
					$('#product-treegrid').datagrid('checkRow', index);
				  }
        	  </#list>
		
			});
		}
	}
	
	function getData(){
		var checkedItems = $('#product-treegrid').datagrid('getChecked');
		var ids = [];
		var result=true;
		 
		$.each(checkedItems, function(index, item){
			ids.push(item.id); 
	    });
	     $('#products').val(ids);
	     
	     if(ids.length==0){
	    	alert("未选中商品");
	    	result=false;
	     }
	 
	     return result;
	 }
</script>

</#escape>
