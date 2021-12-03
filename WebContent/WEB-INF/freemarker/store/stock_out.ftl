<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-out">拣货</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">

  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stockin-toolbar" id="stockin-datagrid" 
      data-options="url:'${base}/store/stock-out/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:150"> 拣货单号</th>
        <th data-options="field:'outboundRegisterSerialNo',width:150">出库单号</th>
        <th data-options="field:'productName',width:100"> 商品名</th>
        <th data-options="field:'storeLocationCode',width:100"> 储位编码</th>
        <th data-options="field:'storeContainerLabel',width:100"> 托盘标签</th>
        <th data-options="field:'weight',width:70">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:70">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'storeAreaText',width:100">库区</th>
        <th data-options="field:'stockOutStatus',width:70">拣货单状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stockin-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-2">
        <div class="m-b-sm">
          <div class="btn-group">
          	<button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.picking();"><i class="fa fa-plus-square"></i>拣货</button>           	
          </div>
        </div>
      </div>
      <div class="col-md-10">
        <div style="text-align:right">
          <form class="form-inline" role="form">
          
                  <div class="form-group">
                <label class="sr-only" for="storeAreaId">选择库区</label>
                <select class="form-control" id="storeAreaId" name="storeAreaId" style="width:150px;text-align:left;margin-right:6px;">
                    <option value="">选择库区</option>
                    <#list storeAreas as storeArea>
                                        <#if !storeArea.parent?exists>
                                            <option value="${storeArea.id}" <#if storeArea.id == inboundRegisterItem.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                                            <#list storeArea.children as area>
                                                <option value="${area.id}" <#if area.id == inboundRegisterItem.storeArea.id>selected</#if>>&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                            </#list>
                                        </#if>
                     </#list>
                </select>
            </div>          
            
            <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="customerId" name="customerId" class="form-control"   onchange="selectedCustomer(this.value)"   style="width:180px;text-align: left;">
       		 <option value="">选择客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		
            <div class="form-group">
                <label class="sr-only" for="storeAreaId">选择商品</label>
                <select class="form-control" id="productId" name="productId" style="width:220px;text-align:left;margin-right:6px;">
                    <option value="">请选择商品</option>
                   
                </select>
            </div> 
            
          
   		  <div class="form-group">
      		 
      		 <select id="stockOutStatus" name="stockOutStatus" class="form-control"   style="width:120px;text-align: left;">
       		 <option value="">拣货状态</option>
  			 <#list stockOutStatus as stockOutStatu>
     		 <option value="${stockOutStatu}" >${stockOutStatu}</option>
	  		 </#list>
	  		 
     		 </select>
      		</div>
        	 
      		 <div class="form-group" >
              <label class="sr-only" for="a"> 出库单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  placeholder="出库单号" >
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
          stockOutStatus: $('#stockOutStatus').val(),
          customerId: $('#customerId').val(),
          product: $('#productId').val(),
          storeArea:$('#storeAreaId').val()
      });
    },
    
    
         picking: function(){
     	 var row = $('#stockin-datagrid').datagrid('getSelected');
     	 if(row.stockOutStatus=='已拣货'||row.stockOutStatus=='已作废'){
     	 alert('拣货单'+row.stockOutStatus);
     	 return ;
     	 }  
     	 if (row){
       	 location.href = "${base}/store/stock-out/" + row.id + "/add-picking";
     	 } else {
        	alert('请选择拣货单！');
      	}
    }
  }; 
  

   	function selectedCustomer(value) {
        $("#productId").empty();
        $("#productId").prepend('<option  value= >请选择商品</option>');
       
        if (value == '') {
            $("#productId").empty();
            return;
        }
        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" +value,
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 if(ar.length>0){
           		   	for (var i = 0; i < ar.length; i++) {
                	var option = '<option  value='+ar[i].id+'>'+ar[i].code+ar[i].name+'</option>';
                	$("#productId").append(option);
            		}
           		}
         	}
        })	
    }
  
</script>

</#escape>
