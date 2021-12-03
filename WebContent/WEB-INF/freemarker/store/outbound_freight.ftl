<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-freight">出库发货</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">


  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-freight-toolbar" id="outbound-freight-datagrid" 
      data-options="url:'${base}/store/outbound-freight/list',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
      	<th data-options="field:'serialNo',width:100,formatter: rowformater">发货单号</th>
      	<th data-options="field:'outboundSerialNo',width:100">出库单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
        <th data-options="field:'tallyAreaName',width:100">理货区</th>
        <th data-options="field:'carrierName',width:100">承运商</th>
        <th data-options="field:'vehicleTypeName',width:100">车型</th>
     	<th data-options="field:'vehicleNo',width:100">车牌号</th>
     	<th data-options="field:'freightTime',width:100,formatter:formatterDate">发货时间</th>
     </tr>
    </thead>
  </table>
  
    <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-freight-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            
          <button id="person-edit" type="button" class="btn btn-primary" onclick="outboundfreightjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="outboundfreightjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>  
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
             <form class="form-inline" operator="form">
        	 <div class="form-group">
      		 <select id="customerId" name="customerId" class="form-control"   style="width:180px;text-align: left;">
       		 <option value="">选择客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
      		 <div class="form-group" >
              <label class="sr-only" for="a"> 单号</label>
                <input type="text" class="form-control"  name="serialNo" id="serialNo" value=""  placeholder="单号" >
  			</div>
            
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="outboundfreightjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
 
                
            </div>
       
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
 

<script type="text/javascript">
 var outboundfreightjs = {
 
    search: function(){
      $('#outbound-freight-datagrid').datagrid('load',{
          customerId: $('#customerId').val(),
          serialNo: $('#serialNo').val(),
      });
     }, 
 
    
    edit: function(){
      var row = $('#outbound-freight-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/outbound-freight/" + row.id + "/edit?outboundCheckId=${outboundCheck.id}";
      } else {
        alert('请选择要编辑的数据！');
      }
    },

    remove: function(){
      var row = $('#outbound-freight-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/outbound-freight/" + row.id + "/delete?outboundCheckId=${outboundCheck.id}";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  };  
  
    function rowformater(value,row,index){
    var url="${base}/store/outbound-freight/"+row.id;
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
