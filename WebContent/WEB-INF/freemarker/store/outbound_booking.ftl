<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-booking">出库预约</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-booking-toolbar" id="outbound-booking-datagrid" 
      data-options="url:'${base}/store/outbound-booking/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">预约单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
        <th data-options="field:'storeAreaName',width:100">库区</th>
        <th data-options="field:'vehicleTypeName',width:100">来车类型</th>
        <th data-options="field:'vehicleAmount',width:100">车辆数</th>
        <th data-options="field:'bookingMethodName',width:100">预约方式</th>
        <th data-options="field:'applyOutboundTime',width:100,formatter:formatterDate">申请出库时间</th>
        <th data-options="field:'bookingStatus',width:100">预约状态</th>
 
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-booking-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-3">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/outbound-booking/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
             <#--  <button id="person-edit" type="button" class="btn btn-primary" onclick="storelocationtypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>
         
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>          
          	<button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.register();"><i class="fa fa-plus-square"></i> 出库登记</button> -->          	
          </div>
        </div>
      </div>
      <div class="col-md-9">
        <div style="text-align:right">
         <form class="form-inline" operator="form">
        	 <div class="form-group">
      		 
      		 <select id="bookingStatus" name="bookingStatus" class="form-control"   style="width:120px;text-align: left;">
       		 <option value="">预约状态</option>
  			<#list bookingStatus as bookingStatu>
     		 <option value="${bookingStatu}" >${bookingStatu}</option>
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
              <label class="sr-only" for="a"> 预约单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  placeholder="预约单号" >
  			</div>
            <div class="form-group" >
             	 <input type="text" class="form-control"  name="vehicleNumbers" id="vehicleNumbers" value=""  placeholder="车牌号" >
  			</div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                 <#-- <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul> -->
               
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
      $('#outbound-booking-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          bookingStatus: $('#bookingStatus').val(),
          customerId: $('#customerId').val(),
          vehicleNumbers: $('#vehicleNumbers').val()
         
      });
    },
    
    edit: function(){
      var row = $('#outbound-booking-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/outbound-booking/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
        register: function(){
      var row = $('#outbound-booking-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/outbound-register/" + row.id + "/register";
      } else {
        alert('请选择要入库登记的预约单！');
      }
    },
    remove: function(){
      var row = $('#outbound-booking-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/outbound-booking/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
    function rowformater(value,row,index){
    var url="${base}/store/outbound-booking/"+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
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
</script>

</#escape>
