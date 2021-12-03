<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-booking">入库预约</a>
</header>
<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
   <div class="col-md-12"><h3>单号：<a href="${base}/store/inbound-booking/${inboundBooking.id}">${inboundBooking.serialNo!}</a></h3><hr/></div>
 	<div class="col-md-2">预约状态：</div>
    <div class="col-md-4">${inboundBooking.bookingStatus!} &nbsp;</div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${inboundBooking.customer.name!}</div>

    <div class="col-md-2">来车类型：</div>
    <div class="col-md-4">${inboundBooking.vehicleType.name!} &nbsp;</div>
    <div class="col-md-2">来车台数：</div>
    <div class="col-md-4">${inboundBooking.vehicleAmount!} &nbsp;</div>

    <div class="col-md-2">车牌号：</div>
    <div class="col-md-10">${inboundBooking.vehicleNumbers!} &nbsp; </div>

    <div class="col-md-2">预约时间：</div>
    <div class="col-md-4">${inboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">申请入库时间：</div>
    <div class="col-md-4">${inboundBooking.applyInboundTime!}&nbsp;</div>

    <div class="col-md-2">预约方式：</div>
    <div class="col-md-4">${inboundBooking.bookingMethod.name!} &nbsp;</div>
    <div class="col-md-2">预约仓间：</div>
    <div class="col-md-4">${inboundBooking.storeArea.name!} &nbsp;</div>
	
	<div class="col-md-2">客户留言：</div>
    <div class="col-md-10">${inboundBooking.note!} &nbsp;</div>
  <br/>
  </div>

</br>


  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_location_type-toolbar" id="store_location_type-datagrid" 
      data-options="url:'${base}/store/inbound-booking/items?serialNo=${serialNo}',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
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
  <div class="col-md-12" id="store_location_type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/inbound-booking/new-item?serialNo=${serialNo}" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="storelocationtypejs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
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
    search: function(){  
      $('#store_location_type-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
     	var serialNo= $('#serialNo').val();
        location.href = "${base}/store/inbound-booking/" + row.id + "/edit-item?serialNo="+serialNo;
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          var serialNo= $('#serialNo').val();
          location.href = "${base}/store/inbound-booking/" + row.id + "/delete-item?serialNo="+serialNo;
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
