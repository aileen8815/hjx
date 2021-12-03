<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-register?operationType=${operationType}">
	 <#if operationType==1>
	 	结算
	 	 <script>
             var pageIndicator = "outbound-settle";
         </script>
	 <#elseif operationType==2>
		 延迟付款审核
		 <script>
             var pageIndicator = "outbound-check";
         </script>
	 <#elseif operationType==3>
	 	付款
	 	 <script>
             var pageIndicator = "outbound-payment";
         </script>
	 <#else>
		 出库单
	 </#if>
	</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-register-toolbar" id="outbound-register-datagrid" 
      data-options="url:'${base}/store/outbound-register/list?operationType=${operationType}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">出库单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
      	<th data-options="field:'vehicleTypeName',width:100">来车类型</th>
        <th data-options="field:'vehicleAmount',width:100">车辆数</th>
        <th data-options="field:'outboundTime',width:100,formatter:formatterDate">出库时间</th>
        <th data-options="field:'stockOutStatus',width:100">出库单状态</th>
        <th data-options="field:'outboundType',width:100">出库类型</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-register-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
          <#if !operationType?exists>
            <a href="${base}/store/outbound-register/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
           <#-- <button id="person-edit" type="button" class="btn btn-primary" onclick="storelocationtypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>-->
            <button id="person-remove" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 作废</button>
 
          </#if>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" operator="form">
          	 <#if !operationType?exists||operationType==0>
          	 <div class="form-group">
      		 <label class="sr-only" for="a"> 出库单状态</label>
      		 <select id="stockOutStatus" name="stockOutStatu" class="form-control"  style="width:150px;text-align: left;">
       		 <option value="">选择出库单状态</option>
      		 <#list stockOutStatus as stockOutStatu>
     		 <option value="${stockOutStatu}" >&nbsp${stockOutStatu}&nbsp</option>
	  		 </#list>
     		 </select>
      		</div>
      		</#if>
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 出库类型</label>
      		 <select id="outboundtype" name="outboundType" class="form-control"  style="width:150px;text-align: left;">
       		 <option value="">选择出库类型</option>
      		 <#list outboundTypes as outboundType>
     		 <option value="${outboundType}" >&nbsp${outboundType}&nbsp</option>
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
              <label class="sr-only" for="a"> 单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  style="width:150px;" placeholder="单号" >
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
      $('#outbound-register-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          outboundType: $('#outboundtype').val(),
          customerId: $('#customerId').val(),
          stockOutStatu: $('#stockOutStatus').val()
      });
    },
    
    edit: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
      	if(row.stockOutStatus!='已派送'){
      	alert('该状态不可编辑！');
      	return false;
      	}
        location.href = "${base}/store/outbound-register/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
      stockOut: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/stock-out?outboundRegisterId="+row.id;
      } else {
        alert('请选择要拣货的登记单！');
      }
    },
      outboundcheck: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/outbound-check/new-item?outboundRegisterId="+row.id;
      } else {
        alert('请选择要检查的登记单！');
      }
    },
    remove: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
       	if(row.stockOutStatus!='已派送'){
      	alert('该状态不可作废！');
      	return false;
      	}
        if(confirm('确实要作废该出库单？')){
          location.href = "${base}/store/outbound-register/" + row.id + "/cancel-register";
        }
      }else{
        alert('请选择要作废的出库单！');
      }
    }
  }; 
  
         function	formatterDate(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                    var hour = date.getHours();
                    var min = date.getMinutes();
                    var sec = date.getSeconds();
                    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
                    dateFormat(date,'yyyy-mm-dd HH:MM:SS');
                    return date.format('yyyy-mm-dd HH:MM:ss');
        }
    function rowformater(value,row,index){
    var url="${base}/store/outbound-register/"+row.id+"?operationType=${operationType}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
  
  
  
  $(function(){
     if("${mesg}"!=""){
	 	alert("${mesg}");
	 }
  }); 
</script>

</#escape>
