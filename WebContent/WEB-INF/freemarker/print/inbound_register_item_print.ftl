<html>
<br>
<br>
<head>
    <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
</head>
<body>
<div class="print-content">
    <h1>天津海吉星冷链储运中心</h1>
    <h2>入库登记单</h2>
    <table class="gridtable">
        <thead>
        <tr>
        <div class="row">
            <th width="30"></th>
            <th width="50"></th>
            <th width="80"></th>
            <th width="90"></th>
            <th width="70"></th>
            <th width="80"></th>
            <th width="50"></th>
            <th width="120"></th>
            <th width="180"></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td colspan="2">登记单号</td>
            <td colspan="5">${inboundRegister.serialNo!}&nbsp</td>
            <td  >日期</td>
            <td colspan="2">${inboundRegister.inboundTime?string("yyyy-MM-dd")}&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2"  colspan="2">客户信息</td>
            <td colspan="2">公司名称</td>
            <td colspan="3">${inboundRegister.customer.gsmc!}&nbsp;</td>
            <td>客户名称</td>
            <td colspan="2">${inboundRegister.customer.name!}&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2">客户编号</td>
            <td colspan="3">${inboundRegister.customer.sKHDM!}&nbsp;</td>
            <td>联系方式</td>
            <td colspan="2">${inboundRegister.customer.mobile!}&nbsp;</td>
        </tr>

        <tr>
            <td rowspan="${inboundRegister.inboundRegisterItems.size}+2" colspan="2">
                货物信息
            </td>
            <td>编号</td>
            <td>货物名称</td>
            <td>件数</td>
            <td>重量（吨）</td>
            <td  colspan="2">规格</td>
            <td >备注</td>
        </tr>
   <#assign totalAmount = 0, totalWeight = 0>
                <#if inboundRegister.inboundRegisterItems?has_content>
                    <#list inboundRegister.inboundRegisterItems as item>
                        <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight>      
        <tr>
            <td>${item.product.code!}</td>
            <td>${item.product.name!}</td>
            <td>${item.amount!}</td>
            <td>${item.weight!}</td>
            <td colspan="2">${item.spec!}</td>
            <td colspan="2">&nbsp;</td>
        </tr>
         			</#list>
                </#if>
      
        <tr>
            <td colspan="2">合计</td>
            <td>${totalAmount}&nbsp;</td>
            <td>${totalWeight}&nbsp;</td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td colspan="11">
                <span class="spanstyle">客户：&nbsp;</span>
                <span class="spanstyle">装卸：&nbsp;</span>
                <span class="spanstyle">调度：&nbsp;</span>
            </td>
        </tr>
        </tbody>
    </table>
    <p>备注：首联（白色）冷库方保管，二联（红色）装卸队保存，尾联（黄色）客户保存。</p>
</div>
</body>
<html>