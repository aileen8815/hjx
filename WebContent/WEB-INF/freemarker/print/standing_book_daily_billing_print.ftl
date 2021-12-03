<html>
    <head>
      <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
    </head>
    <body>
      <div class="print-header">
        <div class="title">
            <h1>天津海吉星冷链储运中心</h1>
            <h2>客户对账单</h2>
        </div>
        <div class="clearfix"></div>
        <div>
	        <label class="billing-label">客户名称:</label> ${billing.customerName!}<br/>
	        <label class="billing-label">支付期间:</label>
	        <span>${startDate?string('yyyy-MM-dd')} 至 ${endDate?string('yyyy-MM-dd')}</span><br/>
	        <label class="billing-label">存储品种:</label> <br/>
        </div>
      </div>
    
    <div class="print-content">
        <table class="gridtable">
            <thead>
                <tr>
                    <th width="150"></th>
                    <th width="150"></th>
                    <th width="150"></th>
                    <th width="150"></th>
                    <th width="150"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>收费项目</td>
                    <td>截止日期</td>
                    <td>费用标准</td>
                    <td>当月发生费用</td>
                    <td>备注</td>
                </tr>
	                    <#if (billing.containerStorage > 0)>
                            <tr>
                                <td>仓储费（计托盘）</td>
                                <td>${endDate?string('yyyy-MM-dd')}</td>
                                <td>${billing.containerStorageRuleComment!}</td>
                                <td align="right">${billing.containerStorage}</td>
                                <td></td>
                            </tr>
                            </#if>
                         <#if (billing.rentalAreaStorage > 0)>
                            <tr>
                                <td>仓储费（计库间）</td>
                                <td>${endDate?string('yyyy-MM-dd')}</td>
                                <td>${billing.rentalAreaStorageRuleComment!}</td>
                                <td align="right">${billing.rentalAreaStorage}</td>
                                <td></td>
                            </tr>
                            </#if>
                            <#if (billing.weightStorage > 0)>
                            <tr>
                                <td>仓储费（计吨数）</td>
                                <td>${endDate?string('yyyy-MM-dd')}</td>
                                <td>${billing.weightStorageRuleComment!}</td>
                                <td align="right">${billing.weightStorage}</td>
                                <td></td>
                            </tr>
                            </#if>
                            <#if (billing.amountStorage > 0)>
                            <tr>
                                <td>仓储费（计件数）</td>
                                <td>${endDate?string('yyyy-MM-dd')}</td>
                                <td>${billing.amountStorageRuleComment!}</td>
                                <td align="right">${billing.amountStorage}</td>
                                <td></td>
                            </tr>
                            </#if>
                            <tr>
                            <td>转运费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.shipmentRuleComment!}</td>
                            <td align="right">${billing.shipment}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>分拣费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.sortingRuleComment!}</td>
                            <td align="right">${billing.sorting}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>倒货费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.unloadingRuleComment!}</td>
                            <td align="right">${billing.unloading}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>缠膜费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.shrinkwrapRuleComment!}</td>
                            <td align="right">${billing.shrinkwrap}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>酮体装卸费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.ketonehandlingRuleComment!}</td>
                            <td align="right">${billing.ketonehandling}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>抄码费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.writeCodeRuleComment!}</td>
                            <td align="right">${billing.writeCode}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>处置费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.disposalRuleComment!}</td>
                            <td align="right">${billing.disposal}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>降温费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.coldingRuleComment!}</td>
                            <td align="right">${billing.colding}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>库间倒货费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.sideUnloadingRuleComment!}</td>
                            <td align="right">${billing.sideUnloading}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>库内倒货费</td>
                            <td>${endDate?string('yyyy-MM-dd')}</td>
                            <td>${billing.inUnloadingRuleComment!}</td>
                            <td align="right">${billing.inUnloading}</td>
                            <td></td>
                        </tr>
                <tr>
                    <td>合计</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>${amountTotal}</td>
                    <td>&nbsp;</td>
                </tr>
            </tbody>
            
        </table>
        <div class="row">
	                    冷库确认:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    部门确认:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    客户确认:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    结算中心确认:
	       </div><div class="col-md-12">日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    日期:</div>     
            </div>
    </div>
    </body>
<html>