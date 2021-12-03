<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/tenancy/store-contract">租赁合同</a> 
  <#if storeContract.id?exists>编辑<#else>新建</#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeContract-form" action="${base}/tenancy/store-contract/${storeContract.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="serialNo" value="${storeContract.serialNo!}">
    <input type="hidden" name="createdBy" value="${storeContract.createdBy!}">
    <input type="hidden" name="createdTime" value="${storeContract.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="status" value="${storeContract.status!}">
    <div class="row">
      <div class="form-group">
        <label class="col-md-1 control-label">合同编号:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="contractNo" id="contractNo" value="${storeContract.contractNo}"    
          data-parsley-remote="${base}/tenancy/store-contract/exists?oldCode=${storeContract.contractNo!}" data-parsley-required="true"
          data-parsley-error-message="合同编号必须填写并不能重复">
        </div>
        <label class="col-md-1 control-label">客户:</label>
        <div class="col-md-3">
          <select id="customerId" name="customer.id" class="form-control" data-parsley-required="true">
            <option value="">请选择客户</option>
            <#list customers as customer> <option value="${customer.id}" <#if customer.id == storeContract.customer.id>selected</#if>>${customer.text!}
          
            </option>
            </#list>
          </select>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-1 control-label">库区:</label>
        <div class="col-md-3">
          <select id="storeAreaId" name="storeArea.id" class="form-control" onChange="selectedstoreArea()" data-parsley-required="true">
            <option value="">请选择库区</option>
            <#list storeAreas as storeArea> <option value="${storeArea.id}" <#if storeArea.id == storeContract.storeArea.id>selected</#if>>${storeArea.text!}
            
            </option>
            </#list>
          </select>
        </div>
        <label class="col-md-1 control-label">使用面积:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="rentalArea" id="rentalArea" data-parsley-required="true" value="${storeContract.rentalArea}">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-1 control-label">开始日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onClick="WdatePicker()" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="startDate" id="startDate" value="<#if storeContract.startDate?exists>${storeContract.startDate?string('yyyy-MM-dd')}</#if>"    placeholder="开始日期"  data-parsley-required="true">
        </div>
        <label class="col-md-1 control-label">结束日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onClick="WdatePicker()" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'(.now)'})" name="endDate" id="endDate" value="<#if storeContract.endDate?exists>${storeContract.endDate?string('yyyy-MM-dd')}</#if>"    placeholder="结束日期"  data-parsley-required="true">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <label class="col-md-1 control-label">计费日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onClick="WdatePicker()" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="chargeDate" id="chargeDate" value="<#if storeContract.chargeDate?exists>${storeContract.chargeDate?string('yyyy-MM-dd')}</#if>"    placeholder="计费日期"  data-parsley-required="true">
        </div>
        <label class="col-md-1 control-label">签订日期:</label>
        <div class="col-md-3">
          <input type="text" class="form-control Wdate" onClick="WdatePicker()" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="signedDate" id="signedDate" value="<#if storeContract.signedDate?exists>${storeContract.signedDate?string('yyyy-MM-dd')}</#if>"    placeholder="签订日期"  data-parsley-required="true">
        </div>
      </div>
    </div>
    <div class="row"> 
      <div class="form-group"  >
	  
	  <label class="col-md-1 control-label">备注:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="remark" id="remark" value="${storeContract.remark}">
        </div>
		<#if storeContract.id?exists>
        <label class="col-md-1 control-label">合同状态:</label>
        <div class="col-md-3">
          <select id="status" name="status" class="form-control">
            <option value="签订" <#if '签订'== storeContract.status>selected</#if>>签订
            </option>
          </select>
        </div>
        </#if>
        
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-1 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>
<script>
 function selectedstoreArea(id, value) {
        var storeAreaId = $("#storeAreaId").val();
        if (storeAreaId == '') {
            return;
        }
        $.ajax({
            url: "${base}/tenancy/store-contract/getstoreArea?id=" + storeAreaId,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');
					$("#rentalArea").val(ar.useArea);
            }
        })
    }
</script>
</#escape> 