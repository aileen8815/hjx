<#escape x as x?html>
<header class="panel-heading"><#include "/print/lodop.ftl">
	</a>
	<div class="row">
    <div class="col-md-6">
     
    <h4>	
    <a href="${base}/store/stock-taking?operate=${operate}">
	<#if operate==1>
	盘点单审核
	<#else>
	盘点单
	</#if>
	</a>
 	-单号：${stockTaking.serialNo!}
     <span><strong>[${stockTaking.stockTakingStatus!}]</strong></span>
	</h4>
    </div>
    <div class="col-md-6">
    <div class="btn-group pull-right">
 	  		<#if operate!=1>
	 		 	<#if stockTaking.stockTakingStatus!='已复盘'&&stockTaking.stockTakingStatus!='已完成'&&stockTaking.stockTakingStatus!='已作废'>
		 		    <#if stockTaking.stockTakingStatus=='已批准'>
		 		    	<a href="${base}/store/stock-taking/${stockTaking.id}/complete" class="btn btn-primary">完成</a>&nbsp;
		  	   	 	</#if>
		  	   	 	<#if stockTaking.stockTakingStatus=='已批准'||stockTaking.stockTakingStatus=='待复盘'>
		  	   	 		<a href="javascript:void(0)"  onclick="setid(${item.id})" data-toggle="modal" data-target="#location-selector-modal2"   class="btn btn-primary">复盘</a>
		  	   	 	</#if>
		  	   	 	<#if stockTaking.stockTakingStatus=='盘点中'>
		  	   	 		<a href="${base}/store/stock-taking/${stockTaking.id}/commit-check" class="btn btn-primary">提交审核</a>&nbsp; 
		  	   	 	</#if>
		  	   	 	<#if stockTaking.stockTakingStatus=='盘点中'||stockTaking.stockTakingStatus=='待盘点'>
		  	   	 		<a href="${base}/store/stock-taking/${stockTaking.id}/new-result" class="btn btn-primary">添加盘点结果</a>&nbsp;
		  	   	 	</#if>
	  	   	 	</#if>
	  	   	 	<#if stockTaking.stockTakingStatus=='已完成'>
	  	   	 		<a id="stockTakingCompletedPrint" href="javascript:;" onclick="print(this)" ref="${base}/print/stock-taking-completed?id=${stockTaking.id!}" class="btn btn-primary">打印盘点汇总表</a>&nbsp;
	  	   	 	</#if>
  	   	 		<a id="stockTaking" href="javascript:;" onclick="print(this)" ref="${base}/store/outline/quickview?areaId=${stockTaking.stockTakingObjectId}" class="btn btn-primary">盘点货位图打印</a>&nbsp;
  	   	 	</#if>
 	  	 
    </div>
    </div>
    </div> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
     

    <div class="col-md-2">盘点人：</div>
    <div class="col-md-4">${stockTaking.stockTakingOperator.name!}&nbsp;</div>
	<div class="col-md-2">盘点开始时间：</div>
    <div class="col-md-4">${stockTaking.startTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    
    <div class="col-md-2">盘点结束时间：</div>
    <div class="col-md-4"><#if stockTaking.endTime?exists>${stockTaking.endTime?default(.now)?string('yyyy-MM-dd HH:mm')}</#if>&nbsp;</div>
    <div class="col-md-2">盘点方式：</div>
    <div class="col-md-4">${stockTaking.stockTakingMode!}&nbsp;</div>
    
    <div class="col-md-2">盘点类型：</div>
    <div class="col-md-4">${stockTaking.stockTakingType!}&nbsp;</div>
 <!-- 2  
    <div class="col-md-2">盘点库区：</div>
    <div class="col-md-4">${stockTaking.stockTakingObjectId!}&nbsp;</div>
  -->  
    <div class="col-md-2">登记时间：</div>
    <div class="col-md-4">${stockTaking.registerTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</div>
    
    <div class="col-md-2">登记人：</div>
    <div class="col-md-4">${stockTaking.registerOperator.name!}&nbsp;</div>
    
    <div class="col-md-2">盘点备注：</div>
    <div class="col-md-4">${stockTaking.remark!}&nbsp;</div>
   <#if stockTaking.stockTakingOld?exists>
    <div class="col-md-2">上次盘点计划单号：</div>
    <div class="col-md-4">${stockTaking.stockTakingOld.serialNo!}&nbsp;</div>
   </#if>
 
    </div>
  <br/><br/>
  <!--
  <div class="row">
    <div class="col-md-12">
        <h4><#if stockTaking.stockTakingType=='复盘'>初盘结果<#else>库存列表：</#if></h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>储位编码</th>
                   <th>托盘标签号</th>
                   <th>客户</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>重量</th>
                   <#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
                   <th>操作</th>
                   </#if>
                </tr>
            </thead>
            <tbody>
            <#if stockTaking.stockTakingItems?has_content>
        
            <#list stockTaking.stockTakingItems as item>
               
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.storeLocation.code!}</td>
                    <td>${item.storeContainer.label!} </td>
                    <td>${item.customer.text!}</td>
                    <td>${item.product.code!} ${item.product.name!} </td>
                    <td><#if item.amountMeasureUnit?exists>${item.amount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.weight!} ${item.weightMeasureUnit.name!}</#if></td>
           	 		<#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
           			<td>
           			<a href="${base}/store/stock-taking/${item.id}/new-result">记录盘点结果</a>
           			<#if item.storeContainer?exists>
           			
           			<#if item.amount!=0&&item.weight!=0>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="setid(${item.id})" data-toggle="modal" data-target="#location-selector-modal">移动托盘</a></#if>
					&nbsp;&nbsp;<a href="${base}/store/stock-taking/${item.id}/setempty">置为空</a>&nbsp;
					</#if>
					</td>
					</#if>
                </tr>
            </#list>
            <tr>
 
            </tr>
            </tbody>
            </#if>
        </table>
        <div>
        </div>

    </div>
  </div>
 	-->
 	
 	
   <div class="row">
    <div class="col-md-12">
        <h4>全盘记录：</h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>储位编码</th>
                   <th>目标储位编码</th>
                   <th>托盘标签号</th>
                   <th>客户</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>盘点数量</th>
           		   <th>重量</th>
                   <th>盘点重量</th>
                </tr>
            </thead>
            <tbody>
            
            <#if stockTaking.stockTakingType=='复盘'>
            	<#if stockTaking.stockTakingOld.stockTakingResults?has_content>
          		<#list stockTaking.stockTakingOld.stockTakingResults as item>
          		<#if item.stockTakeResultStatus!='差异'>
          		<tr>
                    <td>${item_index + 1}</td>
                    <td>${item.storeLocation.code!}</td>
                    <td>${item.toStoreLocation.code!}</td>
                    <td>${item.storeContainer.label!} </td>
                    <td>${item.customer.text!}</td>
                    <td>${item.product.code!} ${item.product.name!} </td>
                    <td><#if item.amountMeasureUnit?exists>${item.amount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.amountMeasureUnit?exists>${item.stockTakingAmount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.weight!} ${item.weightMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.stockTakingWeight!} ${item.weightMeasureUnit.name!}</#if></td>
                </tr>
                </#if>
            </#list>
            <tr>
 
            </tr>
            </tbody>
            </#if>
            <#else>
            	<#if stockTaking.stockTakingResults?has_content>
            	<#list stockTaking.stockTakingResults as item>
            	<#if item.stockTakeResultStatus!='差异'>
            	<tr>
                    <td>${item_index + 1}</td>
                    <td>${item.storeLocation.code!}</td>
                    <td>${item.toStoreLocation.code!}</td>
                    <td>${item.storeContainer.label!} </td>
                    <td>${item.customer.text!}</td>
                    <td>${item.product.code!} ${item.product.name!} </td>
                    <td><#if item.amountMeasureUnit?exists>${item.amount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.amountMeasureUnit?exists>${item.stockTakingAmount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.weight!} ${item.weightMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.stockTakingWeight!} ${item.weightMeasureUnit.name!}</#if></td>
                </tr>
                </#if>
            </#list>
            <tr>
 
            </tr>
            </tbody>
            </#if>
       		</#if>
        </table>
        <div>
        </div>

    </div>
  </div>
  	
   <#if stockTaking.stockTakingType=='复盘'>
   <div class="row">
    <div class="col-md-12">
        <h4>上次盘点差异结果：</h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>储位编码</th>
                   <th>目标储位编码</th>
                   <th>托盘标签号</th>
                   <th>客户</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>盘点数量</th>
           		   <th>重量</th>
                   <th>盘点重量</th>
                   <#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
                   <th>操作</th>
                   </#if>
                </tr>
            </thead>
            <tbody>
            <#if stockTaking.stockTakingOld.stockTakingResults?has_content>
        
            <#list stockTaking.stockTakingOld.stockTakingResults as item>
               <#if item.stockTakeResultStatus=='差异'>
                <tr>
                    <td>${item.storeLocation.code!}</td>
                    <td>${item.toStoreLocation.code!}</td>
                    <td>${item.storeContainer.label!} </td>
                    <td>${item.customer.text!}</td>
                    <td>${item.product.code!} ${item.product.name!} </td>
                    <td><#if item.amountMeasureUnit?exists>${item.amount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.amountMeasureUnit?exists>${item.stockTakingAmount!} ${item.amountMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.weight!} ${item.weightMeasureUnit.name!}</#if></td>
                    <td><#if item.weightMeasureUnit?exists>${item.stockTakingWeight!} ${item.weightMeasureUnit.name!}</#if></td>
                	<#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
           			<td>
           			<a href="${base}/store/stock-taking/${stockTaking.id}/new-result?resultId=${item.id}">记录结果</a>
           			<a href="${base}/store/stock-taking/${stockTaking.id}/add-result?resultId=${item.id}">确认结果</a>
           			</td>
					</#if>
                </tr>
                </#if>
            </#list>
            <tr>
 
            </tr>
            </tbody>
            </#if>
        </table>
        <div>
        </div>
  
 	</#if>
   <div class="row">
    <div class="col-md-12">
        <h4>本次盘点差异结果：</h4>
    </div>
    <div class="col-md-12">
    
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>储位编码</th>
                   <th>目标储位编码</th>
                   <th>托盘标签号</th>
                   <th>客户</th>
                   <th>商品</th>
                   <th>数量</th>
                   <th>盘点数量</th>
           		   <th>重量</th>
                   <th>盘点重量</th>
                   <#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
                   <th>操作</th>
                   </#if>
                </tr>
            </thead>
            <tbody>
            <#if stockTaking.stockTakingResults?has_content>
        
            <#list stockTaking.stockTakingResults as item>
               <#if item.stockTakeResultStatus=='差异'>
	               <tr>
	                    <td>${item.storeLocation.code!}</td>
	                    <td>${item.toStoreLocation.code!}</td>
	                    <td>${item.storeContainer.label!} </td>
	                    <td>${item.customer.text!}</td>
	                    <td>${item.product.code!} ${item.product.name!} </td>
	                    <td><#if item.amountMeasureUnit?exists>${item.amount!} ${item.amountMeasureUnit.name!}</#if></td>
	                    <td><#if item.amountMeasureUnit?exists>${item.stockTakingAmount!} ${item.amountMeasureUnit.name!}</#if></td>
	                    <td><#if item.weightMeasureUnit?exists>${item.weight!} ${item.weightMeasureUnit.name!}</#if></td>
	                    <td><#if item.weightMeasureUnit?exists>${item.stockTakingWeight!} ${item.weightMeasureUnit.name!}</#if></td>
	                	<#if stockTaking.stockTakingStatus=='待盘点'||stockTaking.stockTakingStatus=='盘点中'>
	           			<td>
	           			<a href="${base}/store/stock-taking/${stockTaking.id}/edit-result?resultId=${item.id}">编辑结果</a>
	           			<a href="${base}/store/stock-taking/${item.id}/delete-rsult">删除结果</a>
	           			</td>
						</#if>
	                </tr>
                </#if>
            </#list>
            <tr>
 
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
        <h4 class="modal-title" id="myModalLabel">移动托盘</h4>
      </div>
		 <form  action="${base}/store/stock-taking/relocation" method="post" id="storeform" class="form-horizontal"  data-parsley-validate>
             </br> 
             
 			<div class="row">
            <div class="col-md-6">
             <div class="form-group">
      		 <label class="col-md-6 control-label">储位编码:</label>
      		 
       		 <input type="text" class="form-control" name="tostockLacation"   value=""  style="width:50%" data-parsley-required="true" >
       		  <input type="hidden" class="form-control" name="id"  id="itemId"  value=""  style="width:50%"    >
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
  
<!-- Modal -->
<div class="modal fade" id="location-selector-modal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel2">复盘</h4>
      </div>
		 <form  action="${base}/store/stock-taking/${stockTaking.id}/retaking" method="get" id="retaking" class="form-horizontal"  data-parsley-validate>
             </br> 
             
 			<div class="row">
            <div class="col-md-6">
             <div class="form-group">
      		 
                    <label class="col-md-6 control-label">复盘时间:</label>
                    <input type="text" class="form-control Wdate" style="width:50%"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00',minDate:'(.now)'})"
                           name="retakingTime" id="retakingTime"  value=""
                           placeholder="复盘时间" data-parsley-required="true">
             
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
 		 <#if operate==1>
 		 <form  action="${base}/store/stock-taking/${stockTaking.id}/check" method="post" class="form-horizontal" >
 	 	<input type="hidden" name="operate"  id="operate"  value="${operate}">
 	 	<input type="hidden" name="result"  id="result"  value="">
 	 	<div class="form-group"  >
      	<div  class="col-md-12">
        <button type="submit" class="btn btn-primary"   onclick="setchecked(true)">同意</button>     <button type="submit" class="btn btn-primary"  onclick="setchecked(false)">复盘</button>
      	</div>
      	</div> 
 		</form>
  	   	 </#if>
 </div>
  </div>
  <script type="text/javascript">
  function setid(itemId){
  $("#itemId").val(itemId);
  }
  function setchecked(result){
  $("#result").val(result);
  }
  
   function print(button) {
        var printurl = $(button).attr("ref");
        $.get(printurl, function (data) {
            LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            LODOP.SET_PRINT_PAGESIZE(2, 0, 457, 'A4');
            LODOP.ADD_PRINT_HTM(20, 0, 800, 457, data);
            LODOP.PREVIEW();
        });

    }
 </script >
</#escape>
