<html>
<head>
<link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
</head>
<body>
<div class="print-content">
  <h1>天津海吉星冷链储运中心</h1>
  <h2>出库单</h2>
  <table class="gridtable">
    <thead>
      <tr>
        <th width="50"></th>
        <th width="30"></th>
        <th width="210"></th>
        <th width="70"></th>
        <th width="70"></th>
        <th width="60"></th>
        <th width="50"></th>
        <th width="100"></th>
        <th width="60"></th>
        <th width="50"></th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>登记<br>
          单号</td>
        <td colspan="2">${outboundRegister.serialNo!}</td>
        <td>装卸<br>平台</td>
        <td colspan="2"><#list outboundRegister.outboundTarrys as outboundTarry>
          ${outboundTarry.tallyArea.name!}  &nbsp;&nbsp;
          </#list> </td>
        <td>日期</td>
        <td colspan="5">${outboundRegister.outboundTime?string("yyyy-MM-dd")}</td>
      </tr>
      <tr>
        <td rowspan="2">客户<br>
          信息</td>
        <td colspan="2">公司名称</td>
        <td colspan="3">${outboundRegister.customer.gsmc!} &nbsp;</td>
        <td>客户<br>名称</td>
        <td colspan="5">${outboundRegister.customer.name!} &nbsp; </td>
      </tr>
      <tr>
        <td colspan="2">客户编号</td>
        <td colspan="3">${outboundRegister.customer.sKHDM!} &nbsp;</td>
        <td>联系<br>方式</td>
        <td colspan="5">${outboundRegister.customer.mobile!} &nbsp;</td>
      </tr>
      <tr>
        <td>车辆<br>
          信息</td>
        <td>车数</td>
        <td>${outboundRegister.vehicleAmount!} &nbsp;</td>
        <td>车辆<br>牌照</td>
        <td colspan="2">${outboundRegister.vehicleNumbers!}&nbsp;</td>
        <td>司机<br>信息</td>
        <td colspan="5">&nbsp;&nbsp;</td>
      </tr>
      <tr>
        <td rowspan="${rowCount!}"> 货物<br/>
          信息 </td>
        <td colspan="2">是否携带委托书</td>
        <td>${outboundRegister.haveProxy?string('是','否')}&nbsp;</td>
        <td colspan="2">装卸总重量</td>
        <td colspan="2">${weightAmount!}&nbsp;</td>
        <td colspan="3" style="text-align:left;">吨</td>
      </tr>
      <tr>
        <td>编号</td>
        <td>货物名称</td>
        <td>件数<br>(件)</td>
        <td>重量<br>(KG)</td>
        <td>规格<br>(kg/件)</td>
        <td>托数</td>
        <td>入库时间</td>
        <td>仓储<br>天数</td>
        <td>货物<br>位置</td>
      </tr>
    <#if outboundRegister.outboundCheckItems?has_content>
    
    <#list outboundCheckItems as out>
    <tr>
    	<td>${out['NUM']}</td>
    	<td>${out['PRODUCTNAME']}</td>
      	<td>${out['AMOUNT']}</td>
      	<td>${out['WEIGHTKG']}</td>
      	<td>${out['SPEC']}</td>
     	<td>${out['CONTAINERCOUNT']}</td>
     	<td>${out['STOCKINTIME']}</td>
     	<td colspan="0.5">${out['STOCKINDAYS']}</td>
        <td>${out['STOREAREA']}</td>
    </tr>
 	</#list>
 	</#if>
    <tr>
      <td colspan="2">合计</td>
      <td>&nbsp;${totalAmount}&nbsp;</td>
      <td>&nbsp;${weightAmountKG!}&nbsp;</td>
      <td>共</td>
      <td>&nbsp;${totalStoreContainerCount}&nbsp;</td>
      <td>托</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="12">客户：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
      						   装卸：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      						   调度：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      					   	   制表人：</td>
    </tr>
    </tbody>
    
  </table>
  <p>备注：首联（白色）冷库方保存，二联（红色）装卸队保存，三联（黄色）客户保存，尾联（蓝色）财务部保存。</p>
</div>
</body>
<html>
