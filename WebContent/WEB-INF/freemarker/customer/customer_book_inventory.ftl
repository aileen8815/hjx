<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/customer-book-inventory">库存信息查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#carrier-toolbar" id="carrier-datagrid" 
      data-options="url:'${base}/customer/customer-book-inventory/list',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>

        <th data-options="field:'productName',width:100">商品名称</th>
        <th data-options="field:'amount',width:100">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'weight',width:100">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
		<th data-options="field:'spec',width:100">规格</th>
        <th data-options="field:'packingName',width:60,fixed:true">包装</th>  
        <th data-options="field:'stockInTime',width:100,formatter:formatterDate">上架时间</th>
        <th data-options="field:'inboundRegisterSerialNo',width:150">批次</th> 
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
      		  <div class="col-md-3" >
       		 <select id="productId" name="productId"  style="width:180px;text-align:left;margin-right:6px;"  class="form-control">
       		 <option value="">选择商品</option>
      		 <#list products as product>
     		 <option value="${product.id}" >${product.name}</option>
	   		 </#list>
     		 </select>
      		</div>
      		
            </div>
            
            <div class="form-group">
              <label class="sr-only" for="a">上架时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="startTime" id="startTime" value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>"  placeholder="上架时间">
 
            </div>至
             <div class="form-group">
              <label class="sr-only" for="a">上架时间</label>
                <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="endTime" id="endTime" value="<#if operator.birthday?exists>${operator.birthday?string('yyyy-MM-dd')}</#if>"   placeholder="上架时间">
 
            </div>
    
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="carrierjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var carrierjs = {
    search: function(){
      $('#carrier-datagrid').datagrid('load',{
          productCategory: $('#productId').val(),
          startTime: $('#startTime').val(),
          endTime: $('#endTime').val()
      });
    }, 
  }; 
  
  $('#productId').select2();
    function formatterDate(value) {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
        dateFormat(date, 'yyyy-mm-dd HH:MM:SS');
        return date.format('yyyy-mm-dd HH:MM:ss');
    }
</script>

</#escape>
