<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-owner-change">
	 货权转移
	<#if operationType==1&&customerType==0>
	 卖方结算
	 <script>
         var pageIndicator = "stock-owner-sellsettle";
     </script>
	<#elseif operationType==1&&customerType==1>
	买方结算
	 <script>
         var pageIndicator = "stock-owner-buysettle";
     </script>
	<#elseif operationType==2&&customerType==0>
	卖方延迟付款审核
	 <script>
         var pageIndicator = "stock-owner-sellcheck";
     </script>
	<#elseif operationType==2&&customerType==1>
	买方延迟付款审核
	 <script>
         var pageIndicator = "stock-owner-buycheck";
     </script>
	<#elseif operationType==3&&customerType==0>
	卖方付款
	 <script>
         var pageIndicator = "stock-owner-sellpayment";
     </script>
	<#elseif operationType==3&&customerType==1>
	买方付款
	 <script>
         var pageIndicator = "stock-owner-buypayment";
     </script>
	</#if>
   
    </a>
    
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stock-owner-change-toolbar" id="stock-owner-change-datagrid" 
      data-options="url:'${base}/store/stock-owner-change/list?customerType=${customerType}&&operationType=${operationType}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     	<th data-options="field:'serialNo',width:100,formatter: rowformater">单号</th>
        <th data-options="field:'sellerName',width:100">卖方客户</th>
        <th data-options="field:'buyerName',width:100">买客户</th>
        <th data-options="field:'changeTime',width:100,formatter:formatterDate">转让时间</th>
        <th data-options="field:'stockOwnerChangeStatus',width:100">状态</th>
	</tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stock-owner-change-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-6">
        <div class="m-b-sm">
          <div class="btn-group">
            <#if !operationType?exists>
            <a href="${base}/store/stock-owner-change/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
           <#-- <button id="person-edit" type="button" class="btn btn-primary" onclick="storelocationtypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-remove" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>-->
          	</#if>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div style="text-align:right">
          <form class="form-inline" operator="form">
        	 
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="sellerId" name="sellerId" class="form-control"   style="width:180px;text-align: left;">
       		 <option value="">选择买方客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}">${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
           	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="buyerId" name="buyerId" class="form-control"  style="width:180px;text-align: left;">
       		 <option value="">选择卖方客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" > ${customer.text!} </option>
	   		 </#list>
     		 </select>
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
      $('#stock-owner-change-datagrid').datagrid('load',{
          sellerId: $('#sellerId').val(),
          outboundType: $('#outboundtype').val(),
          customerId: $('#customerId').val()
      });
    },
    
    edit: function(){
      var row = $('#stock-owner-change-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/stock-owner-change/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },

 
    remove: function(){
      var row = $('#stock-owner-change-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/stock-owner-change/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
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
    var url="${base}/store/stock-owner-change/"+row.id+"?customerType=${customerType}&&operationType=${operationType}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
