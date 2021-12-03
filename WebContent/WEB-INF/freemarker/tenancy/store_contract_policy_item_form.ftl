<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract/index-policy-item?serialNo=${serialNo}">协议费用</a>
    <#if contractPolicyItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeContractPolicyItem-form" action="${base}/tenancy/store-contract/${storeContractPolicyItem.id!}/save-policy-item" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeContractPolicyItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeContractPolicyItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
    <input type="hidden" class="form-control" name="storeContract.id" id="storeContract.id" value="${storeContractPolicyItem.storeContract.id}" >

    <div class="form-group">
      <label class="col-md-1 control-label">收费项目:</label>
      <div class="col-md-3">
      <select id="feeItemId" name="feeItem.id"  class="form-control">       
        <#list feeItems as feeItem>
     	  <option value="${feeItem.id}" <#if feeItem.id == storeContractPolicyItem.feeItem.id>selected</#if>>${feeItem.name}</option>
	    </#list>
      </select>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">单价:</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="amount" id="amount" value="${storeContractPolicyItem.amount}" data-parsley-required="true" data-parsley-type="number" data-parsley-gt="0">
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">单位:</label>
      <div class="col-md-3">
      <select id="measureUnitId" name="measureUnit.id"  class="form-control" data-parsley-required="true">       
        <#list measureUnits as measureUnit>
     	  <option value="${measureUnit.id}" <#if measureUnit.id == storeContractPolicyItem.measureUnit.id>selected</#if>>${measureUnit.name}</option>
	    </#list>
      </select>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">使用限量:</label>
      <div class="col-md-3">
       <input type="text" class="form-control" name="useLimited" id="useLimited" value="${storeContractPolicyItem.useLimited}" data-parsley-required="true" data-parsley-type="integer">
      </div>
    </div>
      
    <div class="form-group">
      <div class="col-md-offset-1 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>

</#escape>
