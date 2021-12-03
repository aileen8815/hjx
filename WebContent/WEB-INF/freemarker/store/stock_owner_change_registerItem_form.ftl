<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-owner-change">货权转移单</a> - 
    <#if stockOwnerChange.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  

 <form class="form-inline"  action="${base}/store/stock-owner-change/${stockOwnerChange.id}"    method="post"  class="form-horizontal" data-parsley-validate onsubmit="return getData()">
 <#--商品详细 -->
  <#-- JEasyUI DataGrid 显示数据 -->
   <div class=" col-md-12">
   <table class="easyui-datagrid"  id="stock-owner-change-datagrid"   toolbar="#stock-owner-change-toolbar"
      data-options="url:'${base}/store/stock-owner-change/bookinventory-list?customerId=${stockOwnerChange.seller.id}&&stockOwnerChangeId=${stockOwnerChange.id}',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false,selectOnCheck:true,checkOnSelect:false,onLoadSuccess:loadSuccess">
    <thead>
     <tr>
     	<th data-options="field:'batchProduct',checkbox:true"></th>
     	<th data-options="field:'inboundRegisterSerialNo',width:100">批次</th> 
		<th data-options="field:'productName',width:100">商品</th>
        <th data-options="field:'weight',width:90, formatter:fixedformater">库存重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:80">库存数量</th>
        <th data-options="field:'outboundAmount',width:100,formatter: rowformater">转让数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
     </tr>
    </thead>
  </table>
 
 </div>
 

	 <#-- 表格工具条 -->
  <div class="col-md-12" id="stock-owner-change-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-6">
        <div class="m-b-sm">
          <div class="btn-group">
          
 			<input type="hidden" class="form-control" name="id" id="id" value="${stockOwnerChange.id}"  >
        	 <textarea   style="display:none" class="form-control" name="productCheck" id="productCheck" rows="5"></textarea>
        	 
        	  <div class="row">
    		<div class="col-md-6">
      		 <label class="sr-only" for="a"> 卖方客户</label>
      			<select id="seller" name="seller.id" class="form-control" onchange="search(this.value)"  data-parsley-required="true"  style="width:180px;text-align: left;">
      			<option value="">请选择卖方客户</option>
       			<#list customers as customer>
     			<option value="${customer.id}" <#if customer.id == stockOwnerChange.seller.id>selected</#if>>${customer.text!}</option>
	  			</#list>
      			</select>
      		</div>
        	 
    		<div class="col-md-6">
      		 <label class="sr-only" for="a">买方客户</label>
      		 <select id="buyer" name="buyer.id" class="form-control" data-parsley-required="true"  style="width:180px;text-align: left;">
       		 <option value="">选择买方客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}"   <#if customer.id == stockOwnerChange.buyer.id>selected</#if>>${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		</div>
 
          </div>
        </div>
      </div>
      <div class="col-md-6">
      
      </div>
    </div>
  </div>
   <div class="col-md-6">
   <div class="btn-group">
   				  </br>
                  <button type="submit" class="btn btn-primary"  >保存</button>
            </div>
    </div>
          </form>
</div>
	<script type="text/javascript">
	 function getData(){
		var checkedItems = $('#stock-owner-change-datagrid').datagrid('getChecked');
		var ids = [];
		var result=true;
		var reg =  "^[1-9]*[1-9][0-9]*$";
		$.each(checkedItems, function(index, item){
		var outboundAmount=$('#val_'+item.batchProduct).val();
		if(outboundAmount.match(reg)==null||outboundAmount>item.amount){
			result= false;
			return result;
		}
		ids.push(item.batchProduct+"_"+outboundAmount);
	    });
	    if(!result){
	    alert("选择的商品数量需大于零且不能大于库存数量");
	    return   result;
	    }
	     $('#productCheck').val(ids);
	 	if(ids.length==0){
	 		alert("请选择商品");
	 		return false;
	 	}
	 	if($("#seller").val()==$("#buyer").val()){
	 	alert("买方客户和卖方客户不能相同");
	 	return false;
	 	}
	 	return true;
	 }
		
	function loadSuccess(data){
 
		if(data){
			$.each(data.rows, function(index, item){
		      <#list stockOwnerChange.stockOwnerChangeRegisterItems as stockOwnerChangeRegisterItem>
			      if(item.batchProduct=='${stockOwnerChangeRegisterItem.batchProduct}'){
					$('#stock-owner-change-datagrid').datagrid('checkRow', index);
				  }
        	  </#list>
		
			});
		}
	}
	   function    search(value){  
	   var 	id=value;
	   	if(id==''){
	   		id=-1;
	   	}
      $('#stock-owner-change-datagrid').datagrid('load',{
          selectCustomerId:id
 		})
 		 
      }
		
  	function rowformater(value,row,index){
  		return   '<input type="text"  name="outboundAmount" id=val_'+row.batchProduct+' style="color:red"  data-parsley-type="integer"  value='+value+' >'
  	}
  	
  	function fixedformater(value){
      var  v=value.toFixed(2);
     <!-- console.log(v); -->
      return v;
  	}
  	
    </script >
</#escape>
