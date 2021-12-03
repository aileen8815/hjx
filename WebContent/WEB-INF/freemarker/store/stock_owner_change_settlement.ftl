<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-owner-change/index-settlement?customerType=${customerType}">货权转移-<#if customerType==0>买方客户<#else>卖方客户</#if>结算</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stock-owner-change-toolbar" id="stock-owner-change-datagrid" 
      data-options="url:'${base}/store/stock-owner-change/list-settlement?customerType=${customerType}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     	<th data-options="field:'serialNo',width:100,formatter: rowformater">单号</th>
        <th data-options="field:'sellerName',width:100">卖方客户</th>
        <th data-options="field:'buyerName',width:100">买客户</th>
        <th data-options="field:'changeTime',width:100,formatter:formatterDate">转让时间</th>
	</tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stock-owner-change-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-6">
        <div class="m-b-sm">
          <div class="btn-group">

          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div style="text-align:right">
          <form class="form-inline" operator="form">
        	 
        	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="sellerId" name="sellerId" class="form-control">
       		 <option value="">选择买方客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" >${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
           	 <div class="form-group">
      		 <label class="sr-only" for="a"> 客户</label>
      		 <select id="buyerId" name="buyerId" class="form-control">
       		 <option value="">选择卖方客户</option>
      		 <#list customers as customer>
     		 <option value="${customer.id}" > ${customer.text!}</option>
	   		 </#list>
     		 </select>
      		</div>
            
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var storelocationtypejs = {
    search: function(){
      $('#stock-owner-change-datagrid').datagrid('load',{
          sellerId: $('#sellerId').val(),
          outboundType: $('#outboundtype').val(),
          customerId: $('#customerId').val()
      });
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
    var url="${base}/store/stock-owner-change/"+row.id+"?customerType=${customerType}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
