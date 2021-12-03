<html>
    <head>
    <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
    <style>
	 
	table.gridtable td {
    text-align: center;
    height: 22px;
    padding: 2px;
    border: 1pt solid windowtext;
	}
	</style>
    </head>
    <body>
    
    <div class="print-content">
        <h1>天津海吉星冷链储运中心</h1>
        <h2>入库缴费单</h2>
        <span>入库登记单号：${payment.paymentObjectSerialNo!}</span>
        <table class="gridtable">
            <thead>
                <tr>
                    <th width="40"></th>
                    <th width="100"></th>
                    <th width="110"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="70"></th>
                    <th width="55"></th>
                    <th width="55"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>表单<br>信息</td>
                    <td>结算方式</td>
                   	<td colspan="3">${payment.chargeType.name!}</td>
                    <td>日期</td>
                    <td colspan="3">${payment.settledTime?default(.now)?string('yyyy-MM-dd HH:mm')}&nbsp;</td>
             <td rowspan="${rightCount}" style="text-align:right">
                                               备注：<br>
                                           尾 三 二 首<br>
                                           联 联 联 联<br>
                                           ︵ ︵ ︵ ︵<br>
                                           黄 蓝 红 白<br>
                                           色 色 色 色<br>
                                           ︶ ︶ ︶ ︶<br>
                                           客 结 财 冷<br>
                                           户 算 务 库<br>
                                           保 保 部 方<br>
                                           存 存 保 保<br>
             &nbsp;&nbsp;&nbsp;存 存
              </td>
                </tr>
                <tr>
                    <td rowspan="2">客户<br>信息</td>
                    <td >公司名称</td>
                    <td colspan="3">${payment.customer.gsmc!}</td>
                    <td>客户名称</td>
                    <td colspan="3">${payment.customer.name!}</td>
                </tr>
                <tr>
                    <td >客户编号</td>
                    <td colspan="3">${payment.customer.sKHDM!}</td>
                    <td>联系方式</td>
                    <td colspan="3">${payment.customer.mobile!}</td>
                </tr>
                <tr>
                    <td rowspan="${rowCount}">缴费<br>信息</td>
                    <td>项目</td>
                    <td colspan="2">缴费金额（元）</td>
                    <td colspan="5">缴费说明</td>
                </tr>
                
                 <#assign actuallyAmount = 0>
             
            <#list payment.paymentItems as item>
	            <#if item.actuallyAmount != 0>
	                <#assign actuallyAmount = actuallyAmount + item.actuallyAmount>
	                <tr>
	                    <td>${item.feeItem.name!}</td>
	                    <td colspan="2">${item.actuallyAmount!}</td>
	                    <#if "${item.ruleComment}"?contains("托盘")>
	                    	<#if item.storeContainerCount != 0>
	                    		<td colspan="5">托盘数为： ${item.storeContainerCount!} 托&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                    	<#else>
	                    		<td colspan="5">托盘数为： ${totalStoreContainerCount} 托&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                    	</#if>
	                    <#elseif "${item.ruleComment}"?contains("重量")>
	                    	<#if item.weight != 0>
	                    		<td colspan="5">重量为： ${item.weight!/1000} 吨&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                    	<#else>
	                    		<td colspan="5">重量为： ${sumWeight} 吨&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                    	</#if>
	                    <#elseif "${item.ruleComment}"?contains("件数")>
	                   		<#if item.amoutPiece != 0>
	                   			<td colspan="5">数量为： ${item.amountPiece!} 件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                   		<#else>
	                   			<td colspan="5">数量为： ${amountPiece} 件 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.ruleComment!}</td>
	                   		</#if>
	                    </#if>
	                </tr>
	             </#if>
             </#list>
            
                 <tr>
                    <td>缴费总额</td>
                    <td colspan="3">${actuallyAmount}</td>
                    <td colspan="4"  style="text-align:left">元</td>
                </tr>                
            </tbody>
        </table>
                <span>客户确认：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                	      冷库确认：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                	      收款人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                	      制表人：&nbsp;</span>
    </div>
    </body>
<html>