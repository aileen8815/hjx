<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outbound-register">出库单</a>
</header>
<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-12">
        <h3>
            单号：${outboundRegister.serialNo!}
            <#--<div class="pull-right">状态 ${outboundRegister.stockOutStatus}</div>-->
        </h3>
        <hr/>
    </div>

    <#if outboundRegister.outboundBooking?has_content>
    <div class="col-md-2">预约单号：</div>
    <div class="col-md-10">${outboundRegister.outboundBooking.serialNo!}</div>
    </#if>

    <div class="col-md-2">出库类型：</div>
    <div class="col-md-4">${outboundRegister.outboundType!} &nbsp; </div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${outboundRegister.customer.name!} &nbsp; </div>
    
    <div class="col-md-2">出库时间：</div>
    <div class="col-md-4"><#if outboundRegister.outboundTime?exists>${outboundRegister.outboundTime?string("yyyy-MM-dd HH:mm")}</#if> &nbsp;</div>
    <div class="col-md-2">来车类型：</div>
    <div class="col-md-4">${outboundRegister.vehicleType.name!} &nbsp; </div>
    
    <div class="col-md-2">来车台数：</div>
    <div class="col-md-4">${outboundRegister.vehicleAmount!} &nbsp; </div>
    <div class="col-md-2">车牌号：</div>
    <div class="col-md-4">${outboundRegister.vehicleNumbers!} &nbsp; </div>

    <div class="col-md-2">登记时间：</div>
    <div class="col-md-4">${outboundRegister.registerTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">登记人：</div>
    <div class="col-md-4">${outboundRegister.registerOperator.name!}&nbsp;</div>

    <div class="col-md-2">出库理货区：</div>
    <div class="col-md-10">${outboundRegister.tallyArea.name!} &nbsp;</div>    
  </div>

  </br>


  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#outbound-register-toolbar" id="outbound-register-datagrid" 
      data-options="url:'${base}/store/outbound-register/items?serialNo=${serialNo}',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
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
  <div class="col-md-12" id="outbound-register-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/outbound-register/index-new-item?serialNo=${serialNo}" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
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

    
    remove: function(){
      var row = $('#outbound-register-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          var serialNo= $('#serialNo').val();
          location.href = "${base}/store/outbound-register/" + row.id + "/delete-item?serialNo="+serialNo;
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
