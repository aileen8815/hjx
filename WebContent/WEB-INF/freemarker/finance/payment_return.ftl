<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/finance/payment">退款单据</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#payment-toolbar" id="payment-datagrid" 
      data-options="
        url:'${base}/finance/payment/returnsList',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'serialNo',width:120,fixed:true,formatter:rowformater">单据流水号</th>     
        <th data-options="field:'customerName',width:100,fixed:true">客户</th>   
        <th data-options="field:'settledTime',width:140,fixed:true,formatter:formatterDate">结算时间</th>
        <th data-options="field:'settledByName',width:100,fixed:true">结算员</th>
        <th data-options="field:'chargeTypeName',width:140,fixed:true">计费类型</th>  
        <th data-options="field:'paymentType',width:140,fixed:true">单据类型</th>   

		<th data-options="field:'paymentStatus',width:80,fixed:true">状态</th>
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="payment-toolbar">
    <div class="row row-toolbar">
     <div class="col-md-3">
                <div class="m-b-sm">
                    <div class="btn-group">
                        <a href="${base}/finance/payment/payment-return/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 退还杂费</a>
                 
                    </div>
                </div>
       </div>
      <div class="col-md-9">
        <div style="text-align:right">
          <form class="form-inline" id="store-contract-form">
            <div class="form-group">
                <label class="sr-only" for="customerId">客户</label>
                <select class="form-control" id="customerId" name="customerId" style="width:200px;text-align:left;margin-right:6px;">
                    <option value="">客户</option>
                    <#list customers as c>
                    <option value="${c.id!}">${c.text!!}</opton>
                    </#list>
                </select>
            </div>        
            
            <div class="form-group">
                <label class="sr-only" for="settledById">结算员</label>
                <select class="form-control" id="settledById" name="settledById" style="width:150px;text-align:left;margin-right:6px;">
                    <option value="">结算员</option>
                    <#list settledBys as sb>
                    <option value="${sb.id!}">${sb.name!}</opton> 
                    </#list>
                </select>
            </div>                 
            
            <div class="form-group">
                <label class="sr-only" for="paymentStatus">状态</label>
                <select class="form-control" id="paymentStatus" name="paymentStatus" style="width:150px;text-align:left;margin-right:6px;">
                    <option value="">状态</option>
                    <#list paymentStatus as s>
                    <option value="${s!}">${s!}</option> 
                    </#list>
                </select>
            </div>                          
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storecontractjs.search();" type="button"><i class="fa fa-search"></i>查询</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
            </div>
          </form>
        </div>
         
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storecontractjs = {
    search: function(){  
      $('#payment-datagrid').datagrid('load',{
          customerId: $('#customerId').val(),
          settledById: $('#settledById').val(),
          paymentStatus: $('#paymentStatus').val()                       
      });
    },
        
  }; 
  
    function rowformater(value,row,index){
      var url="${base}/finance/payment/"+row.id + "/view";
      var href="<a href='"+url+"' >"+value+"</a>"; 
 	  return href;
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
