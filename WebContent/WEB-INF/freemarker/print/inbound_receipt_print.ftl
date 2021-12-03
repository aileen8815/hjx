<html>
    <head>
     <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
    </head>
    <body>
      <div class="print-header">
        <div class="title">
            <h1>天津海吉星冷链储运中心</h1>
            <h2>入库收货单</h2>
        </div>
        <div class="barcode">
          
        </div>
    </div>
    <div class="clearfix"></div>
    <div class="print-content">
        <table class="gridtable">
            <thead>
                <tr>
                    <th width="30"></th>
                    <th width="30"></th>
                    <th width="210"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="50"></th>
                    <th width="50"></th>
                    <th width="40"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>登记<br>单号</td>
                    <td colspan="2">${inboundRegister.serialNo!}&nbsp</td>
                    <td colspan="2">装卸平台</td>
                    <td colspan="2"> <#list inboundRegister.inboundTarrys as inboundTarry>
        								${inboundTarry.tallyArea.name!}    &nbsp;&nbsp;
        							 </#list></td>
                    <td>日期</td>
                    <td colspan="3">${inboundRegister.inboundTime?string("yyyy-MM-dd")}&nbsp;</td>
                </tr>
                <tr>
                    <td rowspan="2">客户<br>信息</td>
                    <td colspan="2">公司名称</td>
                    <td colspan="4">${inboundRegister.customer.gsmc!}&nbsp;</td>
                    <td>客户名称</td>
                    <td colspan="3">${inboundRegister.customer.name!}&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">客户编号</td>
                    <td colspan="4">${inboundRegister.customer.sKHDM!}&nbsp;</td>
                    <td>联系方式</td>
                    <td colspan="3">${inboundRegister.customer.mobile!}&nbsp;</td>
                </tr>
                <tr>
                    <td>车辆<br>信息</td>
                    <td>车数</td>
                    <td>${inboundRegister.vehicleAmount!} &nbsp;</td>
                    <td colspan="2">车辆牌号</td>
                    <td colspan="2">${inboundRegister.vehicleNumbers!}&nbsp;</td>
                    <td>司机信息</td>
                    <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                    <td rowspan="${rowcount}">
                        货物<br/>信息
                    </td>
                    <td colspan="2">到货日期</td>
                    <td colspan="2">${inboundRegister.registerTime?default(.now)?string('yyyy-MM-dd')}&nbsp;</td>
                    <td colspan="2">收货总重量(吨)</td>
                    <td colspan="2">${sumWeight}</td>
                    <td colspan="2" style="text-align:left;">吨</td>
                </tr>
				 <tr>
                    <td rowspan="2">编号</td>
                    <td rowspan="2">货物名称</td>
                    <td colspan="2">件数（件）</td>
                    <td colspan="2">总重量（KG）</td>
                    <td rowspan="2">规格</td>
                    <td rowspan="2">托数</td>
                    <td rowspan="2">冷间</td>
                    <td rowspan="2">备注</td>
                </tr>
                <tr>
                    <td>清点前</td>
                    <td>清点后</td>
                    <td>清点前</td>
                    <td>清点后</td>
                </tr>
                <#assign index=0 >
                <#if inboundRegister.inboundReceiptItems?has_content> 
                <#list receiptPrintMap.keySet() as keyValue> 
                <#assign receiptPrintItem = receiptPrintMap.get(keyValue), index=index+1>    
                <tr>
                    <td>${index!}</td>
                    <td>${receiptPrintItem[0]}</td>
                    <td>${receiptPrintItem[7]}</td>
                    <td>${receiptPrintItem[1]}</td>
                    <td>${receiptPrintItem[8]}</td>
                    <td>${receiptPrintItem[2]}</td>
                    <td>${receiptPrintItem[4]}</td>
                    <td>${receiptPrintItem[3]}</td>
                    <td>${receiptPrintItem[5]}</td>
                    <td>${receiptPrintItem[6]}&nbsp;</td>
                </tr> 
               		</#list>
               	</#if>	
                <tr>
                    <td colspan="2">合计</td>
                    <td>${registerAmount}</td>
                    <td>${sumAmount}</td>
                    <td>${registerWeight}</td>
                    <td>${sumWeightKg}</td>
                    <td>合计托数</td>
                    <td>${sumStoreContainerCount}</td>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="12">客户：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    	      装卸：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    	      调度员：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    	      制表人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
            </tbody>
        </table>

       <p> 备注：首联（白色）冷库方保管，二联（红色）装卸队保存，尾联（黄色）客户保存。</p>
    </div>
    </body>
<html>