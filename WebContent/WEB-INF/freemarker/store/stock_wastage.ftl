<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-wastage">
    	<#if operatorType==1>
    	报损审批
    	<script>
             var pageIndicator = "stock-wastage-check";
         </script>
    	<#elseif  operatorType==2>
    	报损费用审批
    	 <script>
             var pageIndicator = "stock-wastage-chargecheck";
         </script>
    	<#else>
    	报损登记
    	</#if>
    </a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_location_type-toolbar" id="store_location_type-datagrid" 
      data-options="url:'${base}/store/stock-wastage/list?operatorType=${operatorType}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
		<th data-options="field:'inputTime',width:100,formatter:formatterDate">登记时间</th>
		<th data-options="field:'inputOperatorName',width:100">登记人</th>
        <th data-options="field:'stockWastageStatus',width:100">状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store_location_type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
             <#if operatorType!=1&&operatorType!=2>
             
             <a href="${base}/store/stock-wastage/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a> 
             <button id="store-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.cancel();"><i class="fa fa-trash-o"></i> 作废</button>        	
          	</#if>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
         <form class="form-inline" operator="form">
        	 
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="customerId" name="customerId" class="form-control" style="width:180px;text-align: left;">
       		 <option value="">选择客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		 <#if operatorType!=1&&operatorType!=2>
        	 <div class="form-group"">
      		 <label class="sr-only" for="a"> 报损单状态</label>
      		 <select id="stockWastageStatus" name="stockWastageStatus" class="form-control" style="width:135px;text-align: left;">
       		 <option value="">报损单状态</option>
  			 <#list stockWastageStatus as stockWastageStatu>
     		 <option value="${stockWastageStatu}" >${stockWastageStatu}</option>
	  		 </#list>
     		 </select>
      		</div>
			</#if>
      		 <div class="form-group">
                <input type="text" class="form-control" style="width:120px;"  name="serialNo" id="serialNo" value=""  placeholder="报损单号" >
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
      $('#store_location_type-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          stockWastageStatus: $('#bookingStatus').val(),
          customerId: $('#customerId').val(),
      });
    },
    
    save: function(){
      var customerId = $('#customerId').val();  
      if (customerId!=''){
        location.href = "${base}/store/stock-wastage/save?customerId="+customerId;
      } else {
        alert('请先选择客户！');
      }
    },

    remove: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/stock-wastage/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    },
     cancel: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要作废选中的数据吗？')){
          if(row.stockWastageStatus!='待处理'){
          alert("该状态不可作废");
            return false
          }
          location.href = "${base}/store/stock-wastage/" + row.id + "/cancel";
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
                    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + hour + ':' + min + ':' + sec;
                    dateFormat(date,'yyyy-mm-dd HH:MM:SS');
                    return date.format('yyyy-mm-dd HH:MM:ss');
    }
        
    function rowformater(value,row,index){
    var url="${base}/store/stock-wastage/"+row.id+"?operatorType=${operatorType}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
