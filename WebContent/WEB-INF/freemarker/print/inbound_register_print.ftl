<html>
    <head>
      <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
    </head>
    <body>
      <div class="print-header">
        <div class="title">
            <h1>天津海吉星冷链储运中心</h1>
            <h2>入库登记单</h2>
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
                    <th width="50"></th>
                    <th width="100"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="60"></th>
                    <th width="100"></th>
                    <th width="80"></th>
                    <th width="95"></th>
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
                    <td>车辆数</td>
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
                    <td colspan="2">装卸总重量</td>
                    <td colspan="2">${sumWeight}</td>
                    <td colspan="2" style="text-align:left;">吨</td>
                </tr>
				 <tr>
                    <td rowspan="2">编号</td>
                    <td rowspan="2">货物名称</td>
                    <td colspan="2">件数（件）</td>
                    <td colspan="2">重量（KG）</td>
                    <td rowspan="2">规格</td>
                    <td rowspan="2">托盘数</td>
                    <td rowspan="2">冷间</td>
                    <td rowspan="2">备注</td>
                </tr>
                <tr>
                    <td>清点前</td>
                    <td>清点后</td>
                    <td>清点前</td>
                    <td>清点后</td>
                </tr>
               <#assign totalAmount = 0, totalWeight = 0 , index=0 , totalStoreContainerCount =0 >
                <#if inboundRegister.inboundRegisterItems?has_content>
                    <#list inboundRegister.inboundRegisterItems as item>
                        <#assign totalAmount = totalAmount + item.amount, totalWeight = totalWeight + item.weight,index=index+1,totalStoreContainerCount = totalStoreContainerCount + item.storeContainerCount>      
                <tr>
                    <td>${index!}</td>
                    <td>${item.product.name!}</td>
                    <td>${item.amount!}</td>
                    <td>&nbsp;</td>
                    <td>#{(item.weight); M2}</td>
                    <td>&nbsp;</td>
                     <td>${item.spec!}</td>
                    <td>${item.storeContainerCount!}</td>
                    <td>${item.storeArea.name!}</td>
                    <td>${item.productDetail!}&nbsp;</td>
                </tr>
               		</#list>
                </#if>
                <tr>
                    <td colspan="2">合计</td>
                    <td>${totalAmount}</td>
                    <td>&nbsp;</td>
                    <td>${sumWeightKg}</td>
                    <td>&nbsp;</td>
                    <td>合计托数</td>
                    <td> ${totalStoreContainerCount}</td>
                    <td colspan="4"></td>
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

       <p> 备注：首联（白色）冷库方保管，二联（红色）装卸队保存，三联（黄色）客户保存；尾联（蓝色）财务部保存</p>
    </div>
    </body>
<html>