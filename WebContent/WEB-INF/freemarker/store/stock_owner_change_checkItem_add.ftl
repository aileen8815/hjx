<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-owner-change/${stockOwnerChangeId}">货权转移单</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">

  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-register-toolbar" id="outbound-register-datagrid" 
      data-options="url:'${base}/store/stock-owner-change/item-list?stockOwnerChangeId=${stockOwnerChangeId}',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     	<th data-options="field:'storeProductID',checkbox:true"></th>
     	<th data-options="field:'inboundRegisterSerialNo',width:150">批次号</th>
     	<th data-options="field:'customeName',width:100">客户名称</th>
        <th data-options="field:'productName',width:130">商品名称</th>
        <th data-options="field:'storeContainerLabel',width:100">托盘标签</th>
     	<th data-options="field:'storeLocationCode',width:120">储位编码</th>
        <th data-options="field:'weight',width:80">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:80">重量单位</th>
        <th data-options="field:'amount',width:80">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:80">数量单位</th>
        <th data-options="field:'qualityGuaranteePeriod',width:80">保质期（天）</th>
        <th data-options="field:'storeDuration',width:80">预期保管时间（天）</th>
        <th data-options="field:'stockInTime',formatter:formatterDate,width:150">上架时间</th>
        
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-register-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
                <button id="person-add" type="button" class="btn btn-primary" onclick="storelocationtypejs.add();"><i class="fa fa-plus-square"></i> 保存选中托盘</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
       
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storelocationtypejs = {
     add: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
     	var checkedItems = $('#outbound-register-datagrid').datagrid('getChecked');
		var ids = [];
		var weight=0;
		var amount=0;
		$.each(checkedItems, function(index, item){
		ids.push(item.storeProductID);
			weight=weight+item.weight;
			amount=amount+item.amount;
	    });
	 
	  var result=  confirm("货权转移登记总重量${weight}公斤,总数量${amount}件;\r本次选中托盘总重量"+weight+"公斤,总数量"+amount+"件");
	  if(!result){
	  	return ;
	  }
        location.href = "${base}/store/stock-owner-change/save-item?stockOwnerChangeId=${stockOwnerChangeId}&&ids="+ids;
      } else {
        alert('请选择要添加的数据！');
      }
    }

  };
  
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
