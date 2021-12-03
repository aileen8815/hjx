<#escape x as x?html>
<header class="panel-heading">
  <div class="row">
    <div class="col-md-6">
      <h4> <a href="${base}/settings/customer/grade-approve">客户等级审批</a> -
        ${customer.text!} </h4>
    </div>
    <form  action="${base}/settings/customer/${customer.id}/save-grade-approve" method="get" class="form-horizontal" >
	<input type="hidden" name="customerGradeStatus"  id="customerGradeStatus"  value="">
    <div class="form-group">
	<div class="col-md-2">
	<button type="submit" class="btn btn-primary" onclick="setval('1')">审批通过</button>
    </div>
    </div>
  </div>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-12">
      <div class="row">
        <div class="col-md-2">会员卡号：</div>
        <div class="col-md-4">${customer.sMemberCode!} &nbsp;</div>
        <div class="col-md-2">客户名称：</div>
        <div class="col-md-4">${customer.name!}&nbsp;</div>
        <div class="col-md-2">客户编号：</div>
        <div class="col-md-4">${customer.sKHDM!} &nbsp;</div>
        <div class="col-md-2">简称：</div>
        <div class="col-md-4">${customer.shortName!} &nbsp;</div>
        <div class="col-md-2">公司名称：</div>
        <div class="col-md-4">${customer.gsmc!} &nbsp;</div>
        <div class="col-md-2">客户ID：</div>
        <div class="col-md-4">${customer.custemerId!} &nbsp;</div>
        <div class="col-md-2">磁道号：</div>
        <div class="col-md-4">${customer.cdnr!} &nbsp;</div>
        <div class="col-md-2">账号：</div>
        <div class="col-md-4">${customer.sAccount_NO!} &nbsp;</div>
        <div class="col-md-2">主卡名称：</div>
        <div class="col-md-4">${customer.zkmc!} &nbsp;</div>
        <div class="col-md-2">子卡ID：</div>
        <div class="col-md-4">${customer.iChildID!} &nbsp;</div>
        <div class="col-md-2">证件号码：</div>
        <div class="col-md-4">${customer.credentialsNo!} &nbsp;</div>
        <div class="col-md-2">客户生效等级：</div>
        <div class="col-md-4">${customer.customerGrade.name!} &nbsp;</div>
        <div class="col-md-2">客户待审批等级：</div>
        <div class="col-md-4">${customer.customerGradeNew.name!} &nbsp;</div>
        <div class="col-md-2">等级启用时间：</div>
        <div class="col-md-4"><#if customer.customerGradeStartTime?exists>${customer.customerGradeStartTime?string("yyyy-MM-dd")}</#if> &nbsp;</div>
		<div class="col-md-2">手机：</div>
        <div class="col-md-4">${customer.mobile!} &nbsp;</div>
        <div class="col-md-2">电子邮箱：</div>
        <div class="col-md-4">${customer.email!} &nbsp;</div>
        <div class="col-md-2">邮编：</div>
        <div class="col-md-4">${customer.zip!} &nbsp;</div>
        <div class="col-md-2">地址：</div>
        <div class="col-md-4">${customer.address!} &nbsp;</div>
        <div class="col-md-2">计费方式：</div>
        <div class="col-md-4">${customer.chargeType.name!} &nbsp;</div>
        <#if customer.chargeType.name = '月结'>
        <div class="col-md-2">对账日期：</div>
        <div class="col-md-4">${customer.billDate!} &nbsp;</div>
        <div class="col-md-2">付款日期：</div>
        <div class="col-md-4">${customer.payDate!} &nbsp;</div>
        </#if>
        <div class="col-md-2">客户等级审批状态：</div>
        <div class="col-md-4">${customer.customerGradeStatusName!} &nbsp;</div>
        <div class="col-md-2">审批人员：</div>
        <div class="col-md-4">${customer.approver.name!} &nbsp;</div>
        <div class="col-md-2">审批时间：</div>
        <div class="col-md-4"><#if customer.approveTime?exists>${customer.approveTime?string("yyyy-MM-dd HH:mm:ss")}</#if> &nbsp;</div>
		</div>
      <br/>
      <div class="row">
        <div class="col-md-12">
          <h4> 客户常用联系人：  </h4>
        </div>
        <div class="col-md-12">
          <table class="table table-striped table-advance table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>联系人</th>
                <th>手机</th>
                <th>电话</th>
                <th>传真</th>
                <th>电子邮箱</th>
                <th>邮编</th>
                <th>地址</th>
              </tr>
            </thead>
            <tbody>
            <#list customer.contacts as contact>
            <tr>
              <td>${item_index + 1}</td>
              <td>${contact.linkman!} </td>
              <td>${contact.mobile!}</td>
              <td>${contact.tel!}</td>
              <td>${contact.fax!}</td>
              <td>${contact.email!}</td>
              <td>${contact.zip!}</td>
              <td>${contact.address!}</td>
            </tr>
            </#list>
            </tbody>            
          </table>
        </div>
      </div>
      <br/>
      <div class="row">
        <div class="col-md-12">
          <h4> 专属承运商：  </h4>
        </div>
        <div class="col-md-12">
          <table class="table table-striped table-advance table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>承运商编号</th>
                <th>简称</th>
                <th>全称</th>
                <th>类别</th>
                <th>车队资料</th>
                <th>联系人</th>
                <th>电话</th>
                <th>传真</th>
                <th>承运人评级</th>
                <th>地址</th>
              </tr>
            </thead>
            <tbody>
            <#list  customer.carriers as carrier>
            <tr>
              <td>${item_index + 1}</td>
              <td>${carrier.code!} </td>
              <td>${carrier.shortName!}</td>
              <td>${carrier.fullName!}</td>
              <td>${carrier.carrierTypeName!}</td>
              <td>${carrier.intro!}</td>
              <td>${carrier.linkman!}</td>
              <td>${carrier.tel!}</td>
              <td>${carrier.fax!}</td>
              <td>${carrier.rank!}</td>
              <td>${carrier.address!}</td>
            </tr>
            </#list>
            </tbody>            
          </table>
        </div>
      </div>
      </br>
 	
 	  <div class="row">
        <div class="col-md-12">
          <h4>经营商品： </h4>
        </div>
        <div class="col-md-12">
          <table class="table table-striped table-advance table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>商品编码</th>
                <th>商品</th>
                <th>参考重量</th>
                <th>托盘承载件数</th>
                <th>参考价格区间</th>
                <th>常用包装</th>
                <th>产地</th>
              </tr>
            </thead>
            <tbody>
            <#list  customer.products as product>
            <tr>
              <td>${item_index + 1}</td>
              <td>${product.code!} </td>
              <td>${product.name!}</td>
              <td>${product.weight!}${product.commonUnit.name!}</td>
              <td>${product.bearingCapacity!}</td>
              <td>${product.priceRange!}</td>
              <td>${product.commonPacking.name!}</td>
              <td>${product.productionPlace!}</td>
            </tr>
            </#list>
            </tbody>            
          </table>
        </div>
      </div>
 	
    </div>
  </div>
</div>

<script type="text/javascript">	
	function  setval(value){
		$("#customerGradeStatus").val(value);
  	}
</script>
</#escape> 