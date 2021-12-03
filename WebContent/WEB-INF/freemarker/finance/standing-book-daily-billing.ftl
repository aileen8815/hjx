<#escape x as x?html>
<header class="panel-heading"><#include "/print/lodop.ftl">
    <a href="${base}/finance/standing-book-daily/billing">冷库客户应收费用对账单</a>
</header>
<div class="col-md-12">
    <div class="btn-group pull-right">
	  </div>
</div>
<div class="panel-body main-content-wrapper site-min-height">
    <div class="row">
        <div class="col-md-12">
            <form action="billing" class="form-horizontal" data-parsley-validate>
                <input type="hidden" id="userid" name="userid" value="${user}">
                <input type="hidden" id="password" name="password" value="${password}">
                <input type="hidden" id="output-target" name="output-target" value="pageable/pdf">
                <div class="form-group">
                    <label class="col-md-1 control-label">开始日期:</label>
                    <div class="col-md-2">
                        <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                               name="startDate" id="startDate" placeholder="开始日期"
                               value="${startDate?default(.now)?string('yyyy-MM-dd')}">
                    </div>
                    <label class="col-md-1 control-label">截止日期:</label>
                    <div class="col-md-2">
                        <input type="text" class="form-control Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                               name="endDate" id="endDate" placeholder="结束日期"
                               value="${endDate?default(.now)?string('yyyy-MM-dd')}">
                    </div>
                    <label class="col-md-1 control-label">客户:</label>
                    <div class="col-md-2">
                        <select id="customerid" name="customerId" class="form-control">
                            <#list customerlist as customer>
                                <option value="${customer.id}" <#if (customer.id == customerId)>selected</#if>>
                                ${customer.text!}
                                </option>
                            </#list>
                        </select>
                    </div>
                    <div class="col-md-1">
                        <button type="submit" class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                    <#if billing?exists>
	                    <div class="col-md-1">
	                        <a id="billingPrint" href="javascript:;" onclick="print(this)" ref="${base}/print/standing-book-daily-billing?startDate=${startDate?string('yyyy-MM-dd')}&endDate=${endDate?string('yyyy-MM-dd')}&customerId=${customerId}" class="btn btn-primary">打印对账单</a>&nbsp;
	                    </div>
                    </#if>
                </div>
            </form>
        </div>
        <div class="col-md-8">
            <#if billing?exists>
                <h1>
                    <center>对账单</center>
                </h1>
                <div>
                    <label class="billing-label">客户名称:</label> ${billing.customerName!}<br/>
                    <label class="billing-label">支付期间:</label>
                    <span>${startDate?string('yyyy-MM-dd')} 至 ${endDate?string('yyyy-MM-dd')}</span><br/>
                    <label class="billing-label">存储品种:</label> <br/>
                </div>
                <div>
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>收费项目</th>
                            <th>截止日期</th>
                            <th>费用标准</th>
                            <th>当月发生费用</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
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
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <div class="col-md-4">冷库确认:</div>
                    <div class="col-md-4">部门确认:</div>
                    <div class="col-md-4">客户确认:</div>
                    <div class="col-md-4">日期:</div>
                    <div class="col-md-4">日期:</div>
                    <div class="col-md-4">日期:</div>
                    <div class="col-md-12">结算中心确认:</div>
                    <div class="col-md-12">日期:</div>
                </div>
            </#if>
        </div>
    </div>
</div>
<script type="text/javascript">  
   function print(button) {
        var printurl = $(button).attr("ref");
        $.get(printurl, function (data) {
            LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            LODOP.ADD_PRINT_HTM(20, 0, 800, 457, data);
            LODOP.PRINT();
        });

    }
</script >
</#escape>
