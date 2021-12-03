<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-booking">添加预约出库商品</a>
</header>
<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">



  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-booking-toolbar" id="outbound-booking-datagrid" 
      data-options="url:'${base}/store/outbound-booking/item-list?serialNo=${serialNo}',method:'get',rownumbers:true,singleSelect:false,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
     	<th data-options="field:'storeProductID',checkbox:true"></th>
        <th data-options="field:'productName',width:100">商品</th>
        <th data-options="field:'weight',width:100">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:100">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'qualityGuaranteePeriod',width:100">保质期（天）</th>
        <th data-options="field:'storeDuration',width:100">预期保管时间（天）</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-booking-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
                <button id="person-add" type="button" class="btn btn-primary" onclick="storelocationtypejs.add();"><i class="fa fa-plus-square"></i> 添加</button>
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
      var row = $('#outbound-booking-datagrid').datagrid('getSelected');  
      if (row){
     	var serialNo= $('#serialNo').val();
     	var checkedItems = $('#outbound-booking-datagrid').datagrid('getChecked');
		var ids = [];
		$.each(checkedItems, function(index, item){
		ids.push(item.storeProductID);
	    });
        location.href = "${base}/store/outbound-booking/save-item?serialNo="+serialNo+"&ids="+ids;
      } else {
        alert('请选择要添加的数据！');
      }
    }

  };
  

</script>

</#escape>
