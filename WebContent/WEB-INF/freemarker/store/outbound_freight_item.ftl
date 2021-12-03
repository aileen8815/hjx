<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-freight">出库发货</a>
</header>

<div class="panel-body main-content-wrapper site-min-height">
 <div class="row">

    <div class="col-md-2">发货单号：</div>
    <div class="col-md-4"><a href="${base}/store/outbound-freight/${outboundFreight.id}">${outboundFreight.serialNo!}</a> &nbsp;</div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${outboundFreight.customer.name!} &nbsp; </div>

    <div class="col-md-2">发货理货区：</div>
    <div class="col-md-4">${outboundFreight.tallyArea.name!} &nbsp;</div>
    <div class="col-md-2">承运商：</div>
    <div class="col-md-4">${outboundFreight.carrier.fullName!} &nbsp;</div>

	<div class="col-md-2">发货时间：</div>
    <div class="col-md-4">${outboundFreight.freightTime?string("yyyy-MM-dd HH:mm")!} &nbsp; </div>
    <div class="col-md-2">发货人：</div>
    <div class="col-md-4">${outboundFreight.operator.name!} &nbsp; </div>
    
    <div class="col-md-2">车型：</div>
    <div class="col-md-4">${outboundFreight.vehicleType.name!} &nbsp; </div>
    <div class="col-md-2">车牌号：</div>
    <div class="col-md-4">${outboundFreight.vehicleNo!} &nbsp; </div>    
    
    <div class="col-md-2">司机：</div>
    <div class="col-md-4">${outboundFreight.driver!} &nbsp; </div>
    <div class="col-md-2">司机身份证：</div>
    <div class="col-md-4">${outboundFreight.driverIdNo!} &nbsp; </div>
  </div>

  <br/>


  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-freight-item-toolbar" id="outbound-freight-item-datagrid" 
      data-options="url:'${base}/store/outbound-freight/items?outboundFreightId=${outboundFreight.id}',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'productName',width:100">商品</th>
        <th data-options="field:'weight',width:100">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'amount',width:100">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="outbound-freight-item-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/outbound-freight/new-item?outboundFreightId=${outboundFreight.id}&outboundCheckId=${outboundCheckId}" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="outboundfreightjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="outboundfreightjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
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
  var outboundfreightjs = {
    search: function(){  
      $('#outbound-freight-item-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#outbound-freight-item-datagrid').datagrid('getSelected');  
      if (row){
 
        location.href = "${base}/store/outbound-freight/" + row.id + "/edit-item?outboundFreightId=${outboundFreight.id}&outboundCheckId=${outboundCheckId}";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#outbound-freight-item-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
 
          location.href = "${base}/store/outbound-freight/" + row.id + "/delete-item?outboundFreightId=${outboundFreight.id}&outboundCheckId=${outboundCheckId}";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
