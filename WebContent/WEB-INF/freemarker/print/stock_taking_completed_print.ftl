<html>
    <head>
      <link href="${base}/assets/flatlab/css/print-lodop.css" rel="stylesheet">
    </head>
    <body>
      <div class="print-header">
        <div class="title">
            <h1>天津海吉星冷链储运中心</h1>
            <h2>冷库盘点汇总表</h2>
        </div>
      </div>
    <div class="clearfix"></div>
    <div class="print-content">
    <h3>冷库名称：${storeArea}</h3>
        <table class="gridtable">
            <thead>
                <tr>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                    <th width="100"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>客户名称</td>
                    <td>期初库存（件）</td>
                    <td>期初库存（托）</td>
                    <td>期初库存（吨）</td>
                    <td>实盘库存（件）</td>
                    <td>实盘库存（托）</td>
                    <td>实盘库存（吨）</td>
                    <td>盘盈+-（件）</td>
                    <td>盘盈+-（托）</td>
                    <td>盘盈+-（吨）</td>
                    <td>备注</td>
                </tr>
                <#list stockTakingTotals as stockTakingTotal>
	                <tr>
	                    <td>${stockTakingTotal['CUSTOMERNAME']}</td>
	                    <td>${stockTakingTotal['AMOUNT']}</td>
	                    <td>${stockTakingTotal['STORECONTAINERCOUNT']}</td>
	                    <td>${stockTakingTotal['WEIGHT']}</td>
	                    <td>${stockTakingTotal['STOCKTAKINGAMOUNT']}</td>
	                    <td>${stockTakingTotal['STORETAKINGCONTAINERCOUNT']}</td>
	                    <td>${stockTakingTotal['STOCKTAKINGWEIGHT']}</td>
	                    <td>${stockTakingTotal['AMOUNTNEW']}</td>
	                    <td>${stockTakingTotal['CONTAINERCOUNTNEW']}</td>
	                    <td>${stockTakingTotal['WEIGHTNEW']}</td>
	                    <td>${stockTakingTotal['REMARK']}</td>
	                </tr>
               	</#list>
                <tr>
                    <td>合计</td>
                    <td>${amountTotal}</td>
                    <td>${storeContainerCountTotal}</td>
                    <td>${weightTotal}</td>
                    <td>${stockTakingAmountTotal}</td>
                    <td>${storeTakingContainerCountTotal}</td>
                    <td>${stockTakingWeightTotal}</td>
                    <td>${amountDifferentTotal}</td>
                    <td>${containerCountDifferentTotal}</td>
                    <td>${weightDifferentTotal}</td>
                    <td>&nbsp;</td>
                </tr>
            </tbody>
        </table>
    </div>
    </body>
<html>