<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/store/outbound-freight">出库发货</a> - 
  <#if outboundFreight.id?exists>编辑<#else>新建</#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="outboundFreight-form" action="${base}/store/outbound-freight/${outboundFreight.id!}" method="post" class="form-horizontal" data-parsley-validate   onsubmit="return getData()">
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${outboundFreight.createdBy!}">
    <input type="hidden" name="createdTime" value="${outboundFreight.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="customer.id" id="customer.id" value="${outboundFreight.customer.id}">
    <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${outboundFreight.serialNo}"  >
    <input type="hidden" class="form-control" name="outboundRegister.id" id="outboundCheckId" value="${outboundFreight.outboundRegister.id}" >
    <input type="hidden" class="form-control" name="productCheck" id="productCheck" value="${productCheck}" >
    <input type="hidden" class="form-control" name="outboundRegisterId"   value="${outboundRegisterId}" >
    <div class="row">
    <div class="col-md-11 row">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="col-md-4 control-label">装卸口:</label>
          <div class="col-md-8">
            <select id="tallyAreaId" name="tallyArea.id" class="form-control">
              <option value="">请选择装卸口</option>
              <#list tallyAreas as tallyArea> <option value="${tallyArea.id}" <#if tallyArea.id == outboundFreight.tallyArea.id>selected</#if>>${tallyArea.name}
              
              </option>
              </#list>
            </select>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="col-md-4 control-label">承运商:</label>
          <div class="col-md-8">
            <select id="carrierId" name="carrier.id" class="form-control">
              <option value="">请选择承运商</option>
              <#list carriers as carrier> <option value="${carrier.id}"  <#if carrier.id == outboundFreight.carrier.id>selected</#if>>${carrier.shortName}
              
              </option>
              </#list>
            </select>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">车型:</label>
        <div class="col-md-8">
          <select id="vehicleTypeId" name="vehicleType.id" class="form-control">
            <option value="">请选车型</option>
            <#list vehicleTypes as vehicleType> <option value="${vehicleType.id}" <#if vehicleType.id == outboundFreight.vehicleType.id>selected</#if>>${vehicleType.name}
            
            </option>
            </#list>
          </select>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">车牌号:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="vehicleNo" id="vehicleNo" value="${outboundFreight.vehicleNo}">
        </div>
      </div>
    </div>
    <div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="col-md-4 control-label">司机:</label>
          <div class="col-md-8">
            <input type="text" class="form-control" name="driver" id="driver" value="${outboundFreight.driver}">
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="col-md-4 control-label">司机证件号:</label>
          <div class="col-md-8">
            <input type="text" class="form-control" name="driverIdNo" id="driverIdNo" value="${outboundFreight.driverIdNo}">
          </div>
        </div>
      </div>
    </div>
    <#--商品详细 -->
    <#-- JEasyUI DataGrid 显示数据 -->
    <div class="col-md-12">
      <div class="form-group">
        <label class="col-md-2 control-label">出库明细:</label>
        <div  class="col-md-10">
          <table class="easyui-datagrid"  id="outbound-freight-datagrid" 
      data-options="url:'${base}/store/outbound-freight/outboundCheckItem-list?outboundFreightId=${outboundFreight.id}&&outboundRegisterId=${outboundRegisterId}',method:'get',rownumbers:true,singleSelect:false,selectOnCheck:true,checkOnSelect:false,pagination:false,fitColumns:true,collapsible:false,onLoadSuccess:loadSuccess">
            <thead>
              <tr>
                <th data-options="field:'productId',checkbox:true"></th>
                <th data-options="field:'productName',width:100">商品</th>
                <th data-options="field:'weight',width:80">检验数量</th>
                <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
                <th data-options="field:'amount',width:80">检验数量</th>
                <th data-options="field:'freighAmountt',width:100,formatter: rowformater">发货数量</th>
                <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
              </tr>
            </thead>
          </table>
        </div>
      </div>
    </div>
    <div class="col-md-12">
      <div class="form-group">
        <div class="col-md-offset-2 col-md-3">
          <button type="submit" class="btn btn-primary">保存</button>
              </div>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">
	function getData(){
		var checkedItems = $('#outbound-freight-datagrid').datagrid('getChecked');
		var ids = [];
		var result=true;
		var reg =  "^[1-9]*[1-9][0-9]*$";
		$.each(checkedItems, function(index, item){
			var freighAmountt=$('#val_'+item.productId).val();
			if(freighAmountt.match(reg)==null||freighAmountt>item.amount){
				result=false;
				return result;
			}
			ids.push(item.productId+":"+freighAmountt);
	    });
	     $('#productCheck').val(ids);
	     if(!result){
	    	alert("选择的商品数量需大于零并小于检验数量");
	     }
	     return result;
	 }

	function loadSuccess(data){
		if(data){
			$.each(data.rows, function(index, item){
		      <#list outboundFreight.outboundFreightItems as outboundFreightItem>
			      if(item.productId=='${outboundFreightItem.product.id}'){
					$('#outbound-freight-datagrid').datagrid('checkRow', index);
				  }
        	  </#list>
		
			});
		}
	}
  	function rowformater(value,row,index){
  		return   '<input type="text"  name="freighAmountt" id=val_'+row.productId+' style="color:red"  data-parsley-type="integer"  value='+value+' >'
  	}
  	</script>
</#escape> 