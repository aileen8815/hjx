<#escape x as x?html>
<header class="panel-heading">

	 <a href="${base}/store/inbound-register?operationType=${operationType}">
	 <#if operationType==1>
         <script>
             var pageIndicator = "inbound-settle";
         </script>
	 	结算
	 <#elseif operationType==2>
		 延迟付款审核
		 <script>
             var pageIndicator = "inbound-check";
         </script>
	 <#elseif operationType==3>
	 	付款
	 	 <script>
             var pageIndicator = "inbound-payment";
         </script>
	 <#elseif operationType==4>
	 	预分配储位
	 <#else>
		 入库单
	 </#if>
	 </a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#inbound-register-toolbar" id="inbound-register-datagrid"
      data-options="url:'${base}/store/inbound-register/list?operationType=${operationType}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">入库单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
      	<th data-options="field:'vehicleTypeName',width:100">来车类型</th>
        <th data-options="field:'vehicleAmount',width:100">车辆数</th>
   		<th data-options="field:'inboundTime',width:100,formatter:formatterDate">入库时间</th>
        <th data-options="field:'stockInStatus',width:100">入库单状态</th>
        <th data-options="field:'inboundType',width:100">入库类型</th>
     </tr>
    </thead>
  </table>

  <#-- 表格工具条 -->
  <div class="col-md-12" id="inbound-register-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
          <#if !operationType?exists||operationType==0>
            <a href="${base}/store/inbound-register/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
           	<#--<button id="person-edit" type="button" class="btn btn-primary" onclick="storelocationtypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>-->
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 作废</button>
          </#if>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
           <form class="form-inline" operator="form">
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 入库类型</label>
      		 <select id="inboundType" name="inboundType" class="form-control" style="width:150px;text-align: left;">
       		 <option value="">选择入库类型</option>
      		 <#list inboundTypes as inboundType>
     		 <option value="${inboundType}" >&nbsp${inboundType}&nbsp</option>
	  		 </#list>
     		 </select>
      		</div>
      		 <#if !operationType?exists||operationType==0>
      		<div class="form-group">
      		 <label class="sr-only" for="a"> 入库单状态</label>
      		 <select id="stockInStatu" name="stockInStatu" class="form-control" style="width:150px;text-align: left;">
       		 <option value="">选择入库单状态</option>
      		 <#list stockInStatus as stockInStatu>
     		 <option value="${stockInStatu}" >&nbsp${stockInStatu}&nbsp</option>
	  		 </#list>
     		 </select>
      		</div>
      		</#if>
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="customerId" name="customerId" class="form-control" style="width:180px;text-align: left;">
       		 <option value="">选择客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		 <div class="form-group" >
              <label class="sr-only" for="a"> 单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  placeholder="单号" style="width:150px;">
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
      $('#inbound-register-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          inboundType: $('#inboundType').val(),
          customerId: $('#customerId').val(),
          stockInStatu: $('#stockInStatu').val()
      });
    },

    edit: function(){
      var row = $('#inbound-register-datagrid').datagrid('getSelected');
      if (row){
      	if(row.stockInStatus!='已派送'){
      	alert('不是可编辑状态！');
      	return false;
      	}
        location.href = "${base}/store/inbound-register/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },


    remove: function(){
      var row = $('#inbound-register-datagrid').datagrid('getSelected');
      if (row){
      	if(row.stockInStatus!='已派送'){
      	alert('该状态不可作废！');
      	return false;
      	}
        if(confirm('确定要作废入库单吗？')){
          location.href = "${base}/store/inbound-register/" + row.id + "/cancel-register";
        }
      }else{
        alert('请选择要作废的入库单！');
      }
    }
  }


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
    var url="${base}/store/inbound-register/"+row.id+"?operationType=${operationType}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
  $(function(){
	 if("${mesg}"!=""){
		 alert("${mesg}");
	 }
  });

</script>

</#escape>
