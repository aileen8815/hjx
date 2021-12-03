<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stockin">上架单</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stockin-toolbar" id="stockin-datagrid" 
      data-options="url:'${base}/store/stockin/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     
        <th data-options="field:'serialNo',width:150"> 上架单号</th>
        <th data-options="field:'inboundReceiptSerialNo',width:150"> 入库单号</th>
        <th data-options="field:'productName',width:100"> 商品名</th>
        <th data-options="field:'storeContainerLabel',width:100"> 托盘标签</th>
        <th data-options="field:'weight',width:70">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:70">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'productDetail',width:100"> 多品明细</th>
       <#-- <th data-options="field:'storeAreainfo',width:100">库区</th>-->
        <th data-options="field:'stockInStatus',width:70">上架单状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stockin-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-5">
        <div class="m-b-sm">
          <div class="btn-group">
          	<button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.pickup();"><i class="fa fa-plus-square"></i>取货上架</button>           	
          </div>
        </div>
      </div>
      <div class="col-md-7">
        <div style="text-align:right">
          <form class="form-inline" role="form">
      		<div class="form-group">
      		 
      		 <select id="stockInStatus" name="stockInStatus" class="form-control"  style="width:120px;text-align: left;">
       		 <option value="">上架状态</option>
  		   	 <#list stockInStatus as stockInStatu>
     		 <option value="${stockInStatu}" >${stockInStatu}</option>
	  		 </#list>
	 		 </select>
      		</div>
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="customerId" name="customerId" class="form-control"  style="width:180px;text-align: left;">
       		 <option value="">选择客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		 <div class="form-group" >
              <label class="sr-only" for="a"> 入库单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  placeholder="入库单号" >
  			</div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
           
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storelocationtypejs = {
     search: function(){  
      $('#stockin-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          stockInStatus: $('#stockInStatus').val(),
          customerId: $('#customerId').val(),
          
      });
    },
    
         pickup: function(){
     	 var row = $('#stockin-datagrid').datagrid('getSelected');  
     	 if(row.stockInStatus=='已上架'||row.stockInStatus=='已作废'){
     	 	alert('上架单'+row.stockInStatus);
     	 	return ;
     	 }
     	 if (row){
       	 location.href = "${base}/store/stockin/" + row.id + "/add-pickup";
     	 } else {
        	alert('请选择要上架的上架单！');
      	}
    }
  }; 
  

</script>

</#escape>
