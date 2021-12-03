<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/finance/payment/extra-charge">提成费用查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#payment-toolbar" id="payment-datagrid" 
      data-options="
        url:'${base}/finance/payment/extra-charge-list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'serialNo',width:225,fixed:true">单据流水号</th>     
        <th data-options="field:'customerName',width:225,fixed:true">客户</th>   
        <th data-options="field:'amount',width:225,fixed:true">实收</th>
         <th data-options="field:'actuallyAmount',width:225,fixed:true">第三方费用</th>
 		<th data-options="field:'time',width:225,fixed:true,formatter:formatterDate">结算时间</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="payment-toolbar">
    <div class="row row-toolbar">
     <div class="col-md-4">
                <div class="m-b-sm">
         
                </div>
       </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" id="store-contract-form">
            <div class="form-group">
                <label class="sr-only" for="customerId">客户</label>
                <select class="form-control" id="customerId" name="customerId" style="width:180px;text-align:left;margin-right:6px;">
                    <option value="">客户</option>
                    <#list customers as c>
                    <option value="${c.id!}">${c.text!}</opton>
                    </#list>
                </select>
            </div>        
        <div class="form-group">
               <input type="text" class="form-control" style="width:160px;" name="serialNo" id="serialNo" value="" placeholder="流水单号">
         </div>     
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="extrachargejs.search();" type="button"><i class="fa fa-search"></i>查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
            </div>
          </form>
        </div>
         
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var extrachargejs = {
    search: function(){  
      $('#payment-datagrid').datagrid('load',{
       	  serialNo: $('#serialNo').val(),
          customerId: $('#customerId').val()                     
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
	 
	
</script>

</#escape>
