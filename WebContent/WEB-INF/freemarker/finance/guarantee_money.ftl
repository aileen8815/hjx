<#escape x as x?html>
<header class="panel-heading">
 	 <a href="${base}/finance/payment/query-guarantee-money">客户押金查询</a>
 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">

	 <div class="col-md-12" id="store_location_type-toolbar">
        <div class="row row-toolbar">
            <div class="col-md-4">
                <div class="m-b-sm">
                    
                </div>
            </div>
            <div class="col-md-8">
                <div style="text-align:right">
                    <form class="form-inline" operator="form">
                        
                	<div class="form-group">
                    <label class="sr-only" for="a"> 客户</label>
                    <select id="customerId" name="customerId" class="form-control" style="width:180px;text-align: left;"  onchange="selectcustomer()">
                        <option value="">选择客户</option>
                        <#list customers as customer>
                            <option value="${customer.id}"    <#if customer.id == customerId>selected</#if>>${customer.text!}</option>
                        </#list>
                    </select>
                	</div>
                 	</form>
            	</div>
        	</div>
    </div>
</div>
</div>
 
  <div class="row">
    <div class="col-md-12">
        <h4>客户押金明细： </h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>支付单号</th>
                   <th>费用类型</th>
                   <th>金额</th>
                </tr>
            </thead>
            <tbody>
             
             
            <#list paymentItems as item>
                
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.payment.serialNo!}</td>
                    <td>${item.payment.paymentType!} </td>
                    <td>${item.actuallyAmount?string.currency} </td>
 
                </tr>
            </#list>
            
            <tr>
                <td colspan="10" align="right">总押金：${totalMoney}</td>
            </tr>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
 </div>  
 
  <script type="text/javascript">
	 function selectcustomer(){
	   var customerid=$('#customerId').val();
	  
	 	if(customerid!=""){
	 		location.href="${base}/finance/payment/query-guarantee-money?customerId="+customerid;
	 	}
	  
	 }
 </script>
  
</#escape>
