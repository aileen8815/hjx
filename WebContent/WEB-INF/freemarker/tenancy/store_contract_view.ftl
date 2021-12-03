<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract/">租赁合同</a>
</header>

<input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
  
    <div class="col-md-12">
        <h3>
            单号：${storeContract.serialNo!}
            <div id="contractStatus" class="pull-right">状态 ${storeContract.status!}</div>
        </h3>
        <hr/>
    </div>

    <div class="col-md-2">合同编号：</div>
    <div class="col-md-10">${storeContract.contractNo!}</div>

    <div class="col-md-2">客户：</div>
    <div class="col-md-4">${storeContract.customer.text!}</div>
    
    <div class="col-md-2">库区名称：</div>
    <div class="col-md-4">${storeContract.storeArea.code!} ${storeContract.storeArea.name!}</div>
    
    <div class="col-md-2">使用面积：</div>
    <div class="col-md-4">${storeContract.rentalArea!} 平方米</div>

    <div class="col-md-2">开始日期：</div>
    <div class="col-md-4">${storeContract.startDate?string("yyyy-MM-dd")!}</div>
    
    <div class="col-md-2">结束日期：</div>
    <div class="col-md-4">${storeContract.endDate?string("yyyy-MM-dd")!}</div>

    <div class="col-md-2">计费日期：</div>
    <div class="col-md-4">${storeContract.chargeDate?string("yyyy-MM-dd")!}</div>
    
    <div class="col-md-2">签订日期：</div>
    <div class="col-md-4">${storeContract.signedDate?string("yyyy-MM-dd")!}</div>

    <div class="col-md-2">备注：</div>
    <div class="col-md-4">${storeContract.remark!}</div>
  </div>
  <br/><br/>
  <div class="row">
    <div class="col-md-12">
        <h4>周期费用明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
        		    <th data-options="width:50,fixed:true">#序号</th>
				   	<th data-options="width:50,fixed:true">收费项目</th>
				   	<th data-options="width:50,fixed:true">收费金额</th>
				   	<th data-options="width:50,fixed:true">收费周期</th>
				   	<th data-options="width:50,fixed:true">操作类型</th>
                </tr>
            </thead>
            <tbody>
            <#if storeContractFeeItems?has_content>
            <#list storeContractFeeItems as item>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.feeItem.name!}</td>
                    <td>${item.amount!}</td>
                    <td>${item.period!}</td>
                    <td>${item.operateType!}</td>                    
                	<#if storeContract.status == '未生效'>
                		<td><div id="editFeeItem" style="display:block">
                			<a href="${base}/tenancy/store-contract/${item.id}/edit-fee-item?serialNo=${storeContract.serialNo}">编辑</a></div>
                		</td>      
                		  
                		<td><div id="delFeeItem" style="display:block">
                			<a href="${base}/tenancy/store-contract/${item.id}/delete-fee-item?serialNo=${storeContract.serialNo}">删除</a></div>
                		</td>     
                	<#else>
                		<td><div id="editFeeItem" style="display:none">
                		<a href="${base}/tenancy/store-contract/${item.id}/edit-fee-item?serialNo=${storeContract.serialNo}">编辑</a></div></td>        
                		<td><div id="delFeeItem" style="display:none">
                		<a href="${base}/tenancy/store-contract/${item.id}/delete-fee-item?serialNo=${storeContract.serialNo}">删除</a></div></td>     
                	</#if>	                     	
                </tr>
            </#list>
            </#if>
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
  <!--  
  <div class="row">
    <div class="col-md-12">
        <h4>协议费用明细：</h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
        		    <th data-options="width:50,fixed:true">#序号</th>
				   	<th data-options="width:50,fixed:true">收费项目</th>
				   	<th data-options="width:50,fixed:true">单&nbsp;&nbsp;&nbsp;&nbsp;价</th>
				   	<th data-options="width:50,fixed:true">单&nbsp;&nbsp;&nbsp;&nbsp;位</th>
				   	<th data-options="width:50,fixed:true">使用限量</th>                      				   	                 
                </tr>
            </thead>
            <tbody>
            <#if storeContractPolicyItems?has_content>            
            <#list storeContractPolicyItems as item>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.feeItem.name!}</td>
                    <td>${item.amount!}</td>
                    <td>${item.measureUnit.name!}</td>
                    <td>${item.useLimited!}</td>

                	<#if storeContract.status == '未生效'>
                		<td><div id="editFeeItem" style="display:block">
                			<a href="${base}/tenancy/store-contract/${item.id}/edit-policy-item?serialNo=${storeContract.serialNo}">编辑</a></div>
                		</td>      
                		  
                		<td><div id="delFeeItem" style="display:block">
                			<a href="${base}/tenancy/store-contract/${item.id}/delete-policy-item?serialNo=${storeContract.serialNo}">删除</a></div>
                		</td>     
                	<#else>
                		<td><div id="editFeeItem" style="display:none">
                		<a href="${base}/tenancy/store-contract/${item.id}/edit-policy-item?serialNo=${storeContract.serialNo}">编辑</a></div></td>        
                		<td><div id="delFeeItem" style="display:none">
                		<a href="${base}/tenancy/store-contract/${item.id}/delete-policy-item?serialNo=${storeContract.serialNo}">删除</a></div></td>     
                	</#if>	                     	                                    
                </tr>
            </#list>
            </#if>
            </tbody>
        </table>    
    </div>
  </div>
  -->
  <div class="row">
    <div class="col-md-12">    
	   <button id="append-fee-item" type="button" class="btn btn-primary" onclick="storeContractjs.appendFeeItem();">新增周期费用</button>	 
	<!--   <button id="append-location-item" type="button" class="btn btn-primary" onclick="storeContractjs.appendLocationItem();">新增协议费用</button>-->	        
	   <button id="start-contract" type="button" class="btn btn-primary" onclick="storeContractjs.startContract();">合同启用</button>	 
    <!--   <button id="copy-contract" type="button" class="btn btn-primary" onclick="storeContractjs.copyContract();">合同续期</button> -->	   
       <button id="stop-contract" type="button" class="btn btn-primary" onclick="storeContractjs.stopContract();">合同停用</button>
    </div>
  </div>
  
</div>  

<script type="text/javascript">
	var storeContractjs = {

		appendFeeItem: function() {
	    	if ('${storeContract.status}' == '未生效') {
	    		location.href = "${base}/tenancy/store-contract/new-fee-item?serialNo=${storeContract.serialNo}";
	    	} else {
	      		alert("只能当合同未生效时才能新增固定收费项目。");
	    	}
		}, 
	
		appendLocationItem: function() {
	    	if ('${storeContract.status}' == '未生效') {
	    		location.href = "${base}/tenancy/store-contract/new-policy-item?serialNo=${storeContract.serialNo}";
	    	} else {
	      		alert("只能当合同未生效时才能新增协议收费项目。");
	    	}
		},
	
	    copyContract: function(){
	      if (confirm("确定要复制一张新合同吗？")) {
	          location.href = "${base}/tenancy/store-contract/${storeContract.id}/copy-contract";
	      }
	    },
	    
	    startContract: function(){
	    	if ('${storeContract.status}' == '未生效') {
	    		if (confirm("确定要启用此合同吗？")) {
	          		location.href = "${base}/tenancy/store-contract/${storeContract.id}/start-contract";        
	      		}
			} else {
	      		alert("只能启用未生效的合同。");
	      	}
	    },
	    
	    stopContract: function(){
	    	if ('${storeContract.status}' == '已生效') {    	    
		    	if (confirm("确定要停用此合同吗？")) {
	    	    	location.href = "${base}/tenancy/store-contract/${storeContract.id}/stop-contract";
	    		}        
	      	} else {
	      		alert("只能停用已生效的合同。");      	
	      	}
	    },    
    
	}
</script>    

</#escape>
