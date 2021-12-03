<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract">租赁合同</a>
</header>
<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-12"><h3>合同单号：<a href="${base}/tenancy/store-contract/${storeContract.id}">${storeContract.serialNo!}</a></h3><hr/></div>
    
 	<div class="col-md-2">合同编号：</div>
    <div class="col-md-4">${storeContract.contractNo!} &nbsp;</div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${storeContract.customer.name!}</div>

    <div class="col-md-2">开始日期：</div>
    <div class="col-md-4">${storeContract.startDate?default(.now)?string('yyyy-MM-dd')}&nbsp;</div>
    <div class="col-md-2">结束日期：</div>
    <div class="col-md-4">${storeContract.endDate?default(.now)?string('yyyy-MM-dd')}&nbsp;</div>

    <div class="col-md-2">计费日期：</div>
    <div class="col-md-4">${storeContract.feeDate?default(.now)?string('yyyy-MM-dd')}&nbsp;</div>
    <div class="col-md-2">签订日期：</div>
    <div class="col-md-4">${storeContract.signedDate?default(.now)?string('yyyy-MM-dd')}&nbsp;</div>
	
	<div class="col-md-2">合同状态：</div>
    <div class="col-md-10">${storeContract.status!} &nbsp;</div>
  </div> 
     
  <br/>

  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_contract_fee_item-toolbar" id="store_contract_fee_item-datagrid" 
      data-options="url:'${base}/tenancy/store-contract/fee-items?serialNo=${serialNo}',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">    
    <thead>
     <tr>
        <th data-options="field:'feeName',width:100">收费项目</th>
        <th data-options="field:'amount',width:100">收费金额</th>
        <th data-options="field:'period',width:100">收费周期</th>
        <th data-options="field:'operateType',width:100">操作类型</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store_contract_fee_item-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/tenancy/store-contract/new-fee-item?serialNo=${serialNo}" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="storecontractfeeitemjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="storecontractfeeitemjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
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
  var storecontractfeeitemjs = {
    search: function(){  
      $('#store_contract_fee_item-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#store_contract_fee_item-datagrid').datagrid('getSelected');  
      if (row){
     	var serialNo= $('#serialNo').val();
        location.href = "${base}/tenancy/store-contract/" + row.id + "/edit-fee-item?serialNo="+serialNo;
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#store_contract_fee_item-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          var serialNo= $('#serialNo').val();
          location.href = "${base}/tenancy/store-contract/" + row.id + "/delete-fee-item?serialNo="+serialNo;
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
