<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/inbound-receipt-query">入库存信息查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#carrier-toolbar" id="carrier-datagrid" 
      data-options="url:'${base}/customer/inbound-receipt-query/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">入库单号</th>
      	<th data-options="field:'vehicleTypeName',width:100">来车类型</th>
        <th data-options="field:'vehicleAmount',width:100">车辆数</th>
   		<th data-options="field:'inboundTime',width:100,formatter:formatterDate">入库时间</th>
        <th data-options="field:'stockInStatus',width:100">入库单状态</th>
        <th data-options="field:'inboundType',width:100">入库类型</th>
     </tr>
    </thead>
  </table>
  
   
  <div class="col-md-12" id="carrier-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            
          
          </div>
        </div>
      </div> 
          <div style="text-align:right">
          <form class="form-inline" operator="form">
            
              
            <div class="form-group">
              <label class="sr-only" for="a">入库时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="startTime" id="startTime" value=""  placeholder="入库时间">
            	至
              <label class="sr-only" for="a">入库时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="endTime" id="endTime" value=""   placeholder="入库时间">
 
            </div>
    
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="carrierjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
 
          
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var carrierjs = {
    search: function(){
      $('#carrier-datagrid').datagrid('load',{
          startTime: $('#startTime').val(),
          endTime: $('#endTime').val()
      });
    },

 
  }; 
  
   
  function rowformater(value,row,index){
    var url="${base}/customer/inbound-receipt-query/"+row.id;
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
