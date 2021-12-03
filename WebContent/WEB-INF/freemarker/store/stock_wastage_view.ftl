<#escape x as x?html>
<header class="panel-heading">
	<#if operatorType==1>
	<a href="${base}/store/stock-wastage/index-check">报损单审核</a>
	<#elseif operatorType==2 >
	 <a href="${base}/store/stock-wastage/index-fee-check">报损单费用审核</a>
	<#else>
	<a href="${base}/store/stock-wastage">报损单</a>
	</#if>
   
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-12"><h3>单号：${stockWastage.serialNo!}</h3><hr/></div>

    <div class="col-md-2">报损单状态：</div>
    <div class="col-md-4">${stockWastage.stockWastageStatus!} &nbsp;</div>
    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${stockWastage.customer.name!}</div>

    <div class="col-md-2">登记时间：</div>
    <div class="col-md-4">${stockWastage.inputTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    <div class="col-md-2">登记人：</div>
    <div class="col-md-4">${stockWastage.inputOperator.name!}&nbsp;</div>

 
    </div>
  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>报损商品明细：</h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>重量</th>
                   <th>储位编码</th>
                   <th>托盘标签号</th>
                   <th>报损类型</th>
                   <#if stockWastage.stockWastageStatus=='待处理'>
                   <th>操作</th>
                   </#if>
                </tr>
            </thead>
            <tbody>
            <#if stockWastage.stockWastageItems?has_content>
            <#assign totalAmount = 0, totalWeight = 0>
            <#list stockWastage.stockWastageItems as item>
                <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.product.code!} ${item.product.name!}</td>
                    <td>${item.amount!} ${item.amountMeasureUnit.name!}</td>
                    <td>${item.weight!} ${item.weightMeasureUnit.name!}</td>
                    <td>${item.storeLocation.code!}</td>
                    <td>${item.storeContainer.label!} </td>
                    <td>${item.stockWastageType.name!}</td>
                    <#if stockWastage.stockWastageStatus=='待处理'>
                    <td>
                    <a href="${base}/store/stock-wastage/${item.id}/edit-item?serialNo=${stockWastage.serialNo}">编辑</a>
                    <a href="${base}/store/stock-wastage/${item.id}/delete-item">删除</a>
                   
                    </td>
                	</#if>
                </tr>
            </#list>
            <tr>
                <td colspan="10" align="right">总数量：${totalAmount}, 总重量 ${totalWeight}</td>
            </tr>
            </tbody>
            </#if>
        </table>
        <div>
        </div>

    </div>
  </div>
 
  <div class="row">
    <div class="col-md-12">
        <h4>报损协商费用：</h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>退费项</th>
                   <th>应退</th>
                   <th>实退</th>
                </tr>
            </thead>
            <tbody>
            <#if stockWastage.payment.paymentItems?has_content>
            <#assign totalAmount = 0, totalWeight = 0, actuallyAmount = 0>
            <#list stockWastage.payment.paymentItems as item>
            <#assign totalAmount = totalAmount + item.amount, actuallyAmount = actuallyAmount + item.actuallyAmount>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.feeItem.name!}</td>
                    <td>${item.amount?string.currency} </td>
                    <td>${item.actuallyAmount?string.currency} </td>
                </tr>
            </#list>
            <tr>
                <td colspan="10" align="right">应退金额：${totalAmount?string.currency}, 实退金额：${actuallyAmount?string.currency}</td>
            </tr>
            </tbody>
            </#if>
        </table>
        <div>
        </div>

    </div>
  </div>
  
  
    <!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">协商费用</h4>
      </div>
		 <form  action="${base}/store/stock-wastage/settlement" method="post" id="storeform" class="form-horizontal"  data-parsley-validate>
             </br> 
             
 			<div class="row">
            <div class="col-md-6">
             <div class="form-group">
      		 <label class="col-md-6 control-label">报损费用:</label>
      		 <div class="col-md-6">
      		 <input type="hidden" class="form-control" name="stockWastageId"   value="${stockWastage.id!}"  >
      		 <input type="hidden" class="form-control" name="paymentItemId"   value="${paymentItem.id!}"  >
       		 <input type="text" class="form-control" name="money"   value="${paymentItem.amount!}"   data-parsley-required="true" data-parsley-type="number">
     	 	</div>
     	 	</div>
     	 	</div>
     	 	</div>
     	 	</br>
     		<div class="modal-footer">
        <button type="submit" class="btn btn-primary" >保存</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal" >关闭</button>

      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

 
  
  <div class="row">
    <div class="col-md-12">
    	<#if operatorType==1||operatorType==2>
    	<form  id="stock-wastage-form" action="${base}/store/stock-wastage/${stockWastage.id}/check" method="post" class="form-horizontal" >
		 
 	 	<input type="hidden" name="type"  id="type"  value="0">
 	 	<input type="hidden" name="operatorType"  id="operatorType"  value="${operatorType}">
 	 	<div class="row">
 	 	<div class="form-group"  >
      	<label class="col-md-1 control-label">备注：</label>
      	<div class="col-md-6">
       	<textarea class="form-control" name="remark" id="remark" rows="2"></textarea>
     	</div>
     	</div>
     	<div class="form-group">
     	<label class="col-md-1 control-label">&nbsp;</label>
      	<div  col-md-6">
       &nbsp;&nbsp;&nbsp; <button type="submit" class="btn btn-primary"   onclick="check('1')">审核通过</button>  &nbsp;&nbsp;   <button type="submit" class="btn btn-primary"  onclick="check('0')">审核拒绝</button>
      	</div>
    	</div> 
    	</div>
 		</form>
    	
    	<#else>
    		<#if stockWastage.stockWastageStatus=='待处理'>
   		 		<a href="${base}/store/stock-wastage/new-item?stockWastageId=${stockWastage.id}" class="btn btn-primary">添加报损明细</a>&nbsp; 
    	 		<#if stockWastage.stockWastageItems?has_content>
    	 		<a href="${base}/store/stock-wastage/${stockWastage.id}/commitcheck" class="btn btn-primary">提交审核</a>&nbsp; 
    	 		</#if>
    	 	</#if>
    	 	<#if stockWastage.stockWastageStatus=='已同意报损'||stockWastage.stockWastageStatus=='被驳回报损赔偿'>
  	   	 	<a href="javascript:void(0)" class="btn btn-primary"  data-toggle="modal" data-target="#location-selector-modal" >协商报损单费用</a>&nbsp; 
  	   	 	</#if>
  	   	 	<#if stockWastage.stockWastageStatus=='已同意报损赔偿' >
  	   	 	<a href="${base}/store/stock-wastage/${stockWastage.id}/complete" class="btn btn-primary">完成</a>&nbsp; 
    	 	</#if>
    	</#if>
    	
      
    </div>
  </div>
  <script type="text/javascript">
  function  check(type){
  	  $("#type").val(type);
  	  if('${operatorType}'=='2'){
  	    $("#stock-wastage-form").attr("action", "${base}/store/stock-wastage/${stockWastage.id}/fee-check");
  	  }
  }
 </script >
</#escape>
