<#assign debug=true>
<#escape x as x?html>
<header class="panel-heading"> <a href="${base}/settings/customer">仓储客户</a> - 
  <#if customer.id?exists>编辑<#else>新建</#if> </header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="customer-form" action="${base}/settings/customer/${customer.id!}" method="post" onSubmit ="return CheckData();" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${customer.createdBy!}">
    <input type="hidden" name="createdTime" value="${customer.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="contact.id" value="${customer.contact.id}">
    <input type="hidden" name="maxNumber" value="${customer.maxNumber!}">
    <#if customer.approver.id?exists>
	    <input type="hidden" name="approver.id" value="${customer.approver.id!}">
	    <input type="hidden" name="approveTime" value="${customer.approveTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    </#if>
    <input type="hidden" name="accountOID" id="accountOID" value="${customer.accountOID!}">
    <input type="hidden" name="sKHBH" id="sKHBH" value="${customer.sKHBH!}">
    <input type="hidden" name="customerOID" id="customerOID" value="${customer.customerOID!}">
 
    <#--客户一体化平台对接字段 -->
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">物理卡号:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="aCardMac" id="aCardMac" value="${customer.aCardMac}" <#if !debug> readonly </#if> >
        </div>
        <div class="col-md-1"> <button type="button" class="btn btn-info" onClick="getIDCardInfoFunc();"><i class="fa fa-ellipsis-v"></i>读卡</button> </div>
        <label class="col-md-1 control-label">密码:</label>
        <div class="col-md-4"> <input type="password" class="form-control" name="password" id="password" value="${customer.password}"> </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <div class="col-md-offset-1 col-md-4">
          <button type="button" class="btn btn-primary" onClick="return getCustomerInfo();">从一体化平台取数据</button>
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">审批状态:</label>
     	 <div class="col-md-4">
        <select id="customerGradeStatus" name="customerGradeStatus" data-parsley-required="true" class="form-control">
            <#list customerGradeStatuss as customerGradeStatus>
            <option value="${customerGradeStatus}"  readonly=readonly <#if customerGradeStatus== customer.customerGradeStatus>selected</#if>>${customerGradeStatus}</option>
            </#list>
        </select>
        </div>
        </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">客户ID:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="custemerId" id="custemerId" value="${customer.custemerId!}"  <#if !debug> readonly </#if> >
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">客户名称:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="name" id="name" value="${customer.name!}"  <#if !debug> readonly </#if> >
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">会员卡号:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="sMemberCode" id="sMemberCode" value="${customer.sMemberCode!}"  <#if !debug> readonly </#if> >
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">公司名称:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="gsmc" id="gsmc" value="${customer.gsmc!}"  <#if !debug> readonly </#if> >
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">账号:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="sAccount_NO" id="sAccount_NO" value="${customer.sAccount_NO!}"  <#if !debug> readonly </#if> >
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">客户代码:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="sKHDM" id="sKHDM" value="${customer.sKHDM!}"  <#if !debug> readonly </#if> >
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">主卡名称:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="zkmc" id="zkmc" value="${customer.zkmc!}"  <#if !debug> readonly </#if> >
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">子卡ID:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="iChildID" id="iChildID" value="${customer.iChildID!}"  <#if !debug> readonly </#if> >
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">客户简称:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="shortName" id="shortName" value="${customer.shortName!}" data-parsley-required="true">
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">证件号码:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="credentialsNo" id="credentialsNo" value="${customer.credentialsNo!}">
        </div>
      </div>
    </div>
    
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">当前等级:</label>
        <div class="col-md-4">
          <select id="customerGrade" name="customerGrade.id" class="form-control" data-parsley-required="true">
          <#if customer.id?exists>
        	
            <#list customerGrades as customerGrade> <option value="${customerGrade.id}" 
            <#if customerGrade.id == customer.customerGrade.id>selected</#if>>${customerGrade.name}
            </option>
            </#list>
          <#else>
          <option value="">请选择客户等级</option>
            <#list customerGradeDefaults as customerGradeDefault> <option value="${customerGradeDefault.id}" 
            <#if customerGradeDefault.id == customer.customerGrade.id>selected</#if>>${customerGradeDefault.name}
            </option>
            </#list>
          </#if>
            
          </select>
        </div>
<div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">计费方式:</label>
        <div class="col-md-4">
          <select id="chargeTypeId" name="chargeType.id" class="form-control" onchange="chargeType()" data-parsley-required="true">
            <option value="">请选择计费方式</option>
            <#list chargeTypes as chargeType> <option value="${chargeType.id}"<#if chargeType.id == customer.chargeType.id>selected</#if>>${chargeType.name}
            </option>
            </#list>
          </select>
        </div>
      </div>
    </div>
    
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">修改等级:</label>
        <div class="col-md-4">
          <select id="customerGradeNew" name="customerGradeNew.id" class="form-control" data-parsley-required="true">
            <option value="">请选择客户等级</option>
            <#list customerGradeNew as customerGrade> <option value="${customerGrade.id}" 
            <#if customerGrade.id == customer.customerGradeNew.id>selected</#if>>${customerGrade.name}
            </option>
            </#list>
          </select>
        </div>
        <div class="col-md-1">&nbsp; </div>
	    <label class="col-md-1 control-label">生效时间:</label>
	    <div class="col-md-4">
	        <input type="text" class="form-control Wdate" onClick="WdatePicker()" name="customerGradeStartTime" id="customerGradeStartTime"
	               value="${customer.customerGradeStartTime?default(.now)?string('yyyy-MM-dd')}" placeholder="生效时间"
	               data-parsley-required="true">
	    </div>
		</div>
    </div>    
    
    <div class="col-md-11 row"   id="chargeTypeDiv"   <#if customer.chargeType.id==1> style="display:none" </#if>>
      <div class="form-group">
        <label id="billDateId" class="col-md-1 control-label">对账日:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="billDate" id="billDate" value="${customer.billDate!}">
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label id="payDateId" class="col-md-1 control-label">付款日:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="payDate" id="payDate" value="${customer.payDate!}">
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">手机:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="mobile" id="mobile" value="${customer.mobile!}" data-parsley-required="true">
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">邮编:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="zip" id="zip" value="${customer.zip!}" 	>
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <label class="col-md-1 control-label">电子信箱:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="email" id="email" value="${customer.email!}"  data-parsley-type="email">
        </div>
        <div class="col-md-1">&nbsp; </div>
        <label class="col-md-1 control-label">地址:</label>
        <div class="col-md-4">
          <input type="text" class="form-control" name="address" id="address" value="${customer.address!}" >
        </div>
      </div>
    </div>
    <div class="col-md-11 row">
      <div class="form-group">
        <div class="col-md-offset-1 col-md-1">
          <button type="submit" class="btn btn-primary" onClick="">保存</button>
        </div>
      </div>
    </div>    
  </form>
</div>
<OBJECT id="ICCard" classid="clsid:186E79AE-FA06-4DFA-B43D-FD1EB3E7DD1C" width=0 height=0>
</OBJECT>
<script language="javascript">
function getIDCardInfoFunc()
{		
	var i = ICCard.readCardNo(); 
	var j = ICCard.readCardMac(); 
	if(i==-1){
	   alert("读卡会员卡号失败");
	}else{
	   $("#sMemberCode").val(i);
	}
	if(j==-1){
	   alert("读卡物理卡号失败");
	}else{
		$("#aCardMac").val(j);
	}
}

function CheckData()
{
	var aCardMac =$("#aCardMac").val().trim();
	var pwd = $("#password").val().trim();
	var customerid = $("#custemerId").val().trim();
	var billDate = $("#billDate").val();
	var payDate = $("#payDate").val();
	var mobile = $("#mobile").val().trim();
	var chargeTypeId = $("#chargeTypeId").val().trim();
	
	if (aCardMac == '')
	{
		alert("物理卡号不能为空，请刷卡！")
		return false;
	}
	
	if(pwd == '')
	{
		alert("密码不能为空，请输入密码！");
		return false;
	}
	
	if (customerid == '')
	{
	    alert("客户Id不能为空，请重新获取客户信息！");
		return false;
	}
	
	if (chargeTypeId == 2) 
	{
		if (billDate =='')
		{
		    alert("对账日不能为空，请输入对账日！");
			return false;
		}	
		
		if (payDate =='')
		{
		    alert("付款日不能为空，请输入付款日！");
			return false;
		}	
		
		if (billDate <1 || billDate >28)
		{
		    alert("对账日输入错误，请输入对账日为1-28日！");
			return false;
		}
			
		if (payDate <1 || payDate >28)
		{
		    alert("付款日输入错误，请输入付款日为1-28日！");
			return false;
		}	
	}
	
	if (mobile =='')
	{
	    alert("手机不能为空，请输入手机号！");
		return false;
	}	
	
	if (chargeTypeId =='')
	{
	    alert("计费方式不能为空，请选择计费方式！");
		return false;
	}
	
	return true;
}

function getCustomerInfo()
{
	var cardMac = $("#aCardMac").val();
	var pwd = $("#password").val();
	var sMemberCode = $("#sMemberCode").val();
	if(sMemberCode.length != 10)
	{
	    $("#sMemberCode").val('');
	}
	
	if((cardMac.trim() ==''))
	{
		alert("物理卡号不能为空，请检查");
		return false;
	}
	
	$.ajax({
        url: "${base}/settings/customer/get-customer-cardMac?cardMac=" + cardMac+"&sMemberCode="+sMemberCode+"&passWord="+pwd,
        data: '',
        type: "get",
        success: function (data) {
            if (data == '')
            {
            	alert("从一体化平台获取数据失败，请检查");
		        return false;
            }
            else
            {
                var ar = eval('(' + data + ')');  
                $("#custemerId").val(ar.custemerId);              
                $("#name").val(ar.name);
                $("#sKHBH").val(ar.sKHBH);
                $("#customerOID").val(ar.customerOID);
                $("#accountOID").val(ar.accountOID);
                $("#sKHDM").val(ar.sKHDM);
                $("#zkmc").val(ar.name);
                $("#shortName").val(ar.name);
                $("#password").val(ar.password);
                if((sMemberCode.length != 10))
				{
	  				$("#sMemberCode").val(ar.custemerId);
	  				$("#sAccount_NO").val(ar.custemerId);
				}else
				{
					$("#sAccount_NO").val(sMemberCode);
				}
            }
        }
       })
       return true;
}

function chargeType() {
        var chargeTypeId = $("#chargeTypeId").val();
        if (chargeTypeId == 2) {
         	$("#chargeTypeDiv").show();
    
        }
        else
        {	
        	$("#chargeTypeDiv").hide();
 
        }
}
        
</script>
</#escape>
