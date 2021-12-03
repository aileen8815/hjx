<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/payment-query">客户费用查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#carrier-toolbar" id="carrier-datagrid" 
      data-options="url:'${base}/customer/payment-query/list?paymentStatus=${paymentStatus}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:150,fixed:true,formatter:rowformater">单据流水号</th>     
 
        <th data-options="field:'settledTime',width:250,fixed:true,formatter:formatterDate">结算时间</th>
        <th data-options="field:'settledByName',width:200,fixed:true">结算员</th>
        <th data-options="field:'chargeTypeName',width:150,fixed:true">计费类型</th>  
        <th data-options="field:'paymentType',width:150,fixed:true">单据类型</th>   
		<th data-options="field:'settleRange',width:200,fixed:true">结算周期</th>
		<th data-options="field:'paymentStatus',width:150,fixed:true">状态</th>
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
              <input type="hidden" id="paymentStatus" name="paymentStatus" value="${paymentStatus!}" data-parsley-required="true">
          
            <div class="form-group">
              <label class="sr-only" for="a">结算时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="startTime" id="startTime" value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>"  placeholder="结算时间">
 
            </div>至
             <div class="form-group">
              <label class="sr-only" for="a">结算时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="endTime" id="endTime" value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>"   placeholder="结算时间">
 
            </div>
    
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="paymentjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var paymentjs = {
    search: function(){
      $('#carrier-datagrid').datagrid('load',{
         
          startTime: $('#startTime').val(),
          endTime: $('#endTime').val()
      });
    },

 
  }; 
  	function formatterDate(value){
		var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
        dateFormat(date,'yyyy-mm-dd');
        return date.format('yyyy-mm-dd');
	}  
  function rowformater(value,row,index){
    var url="${base}/customer/payment-query/"+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
