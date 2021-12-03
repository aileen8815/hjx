<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract/index-location-item?serialNo=${serialNo}">合同储位</a> - 
    <#if contractLocationItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeContractFeeItem-form" action="${base}/tenancy/store-contract/${storeContractLocationItem.id!}/save-location-item" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeContractLocationItem.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeContractLocationItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${serialNo}" >
    <input type="hidden" class="form-control" name="storeContract.id" id="storeContract.id" value="${storeContractLocationItem.storeContract.id}" >

    <div class="form-group">
      <label class="col-md-1 control-label">租赁储位:</label>
      <div class="col-md-3">
      <select id="storeLocationId" name="storeLocation.id"  class="form-control">       
        <#list storeLocations as storeLocation>
     	  <option value="${storeLocation.id}" <#if storeLocation.id == storeContractLocationItem.storeLocation.id>selected</#if>>${storeLocation.label}</option>
	    </#list>
      </select>
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
