<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-booking/check-index">入库预约审核</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_location_type-toolbar" id="store_location_type-datagrid" 
      data-options="url:'${base}/store/inbound-booking/list?bookingStatus=${"未审核"?url('UTF-8')}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">预约单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
        <th data-options="field:'storeAreaName',width:100">库区编码</th>
        <th data-options="field:'vehicleTypeName',width:100">来车类型</th>
        <th data-options="field:'vehicleAmount',width:100">车辆数</th>
        <th data-options="field:'bookingMethodName',width:100">预约方式</th>
        <th data-options="field:'bookingStatus',width:100">预约状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store_location_type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
           

          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
               <div class="form-group">
                    <label class="sr-only" for="a"> 客户</label>
                    <select id="customerId" name="customerId" class="form-control" style="width:180px;text-align: left;">
                        <option value="">选择客户</option>
                        <#list customers as customer>
                            <option value="${customer.id}">${customer.text!}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" style="width:160px;" name="serialNo" id="serialNo" value="" placeholder="预约单号">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" style="width:100px;" name="vehicleNumbers" id="vehicleNumbers" value="" placeholder="车牌号">
                </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storelocationtypejs = {
      search: function () {
            $('#store_location_type-datagrid').datagrid('load', {
                serialNo: $('#serialNo').val(),
                customerId: $('#customerId').val(),
                vehicleNumbers: $('#vehicleNumbers').val()
            });
        },
    
    edit: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/inbound-booking/" + row.id ;
      } else {
        alert('请选择要审核的数据！');
      }
    },
    
      
  }; 
  
  function rowformater(value, row, index) {
        var url = "${base}/store/inbound-booking/" + row.id;
        return "<a href='" + url + "' >" + value + "</a>";
    }
</script>

</#escape>
