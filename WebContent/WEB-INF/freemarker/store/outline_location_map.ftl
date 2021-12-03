<#list locationStats as areaStat> <#-- 库区 -->
    <#if (areaStat.maxRowCount > 0)>
        <#if totalMaxRowCount < areaStat.maxRowCount>
            <#assign totalMaxRowCount = areaStat.maxRowCount/>
        </#if>
    <div class="store-area">
        <div>
            <div class="store-area-title">${areaStat.areaCode!} ${areaStat.areaName!}</div>
            <div class="store-area-legend">
                <#if showStock>
                    <span class="area-legend-symbol legend-usable">&nbsp;</span>可选择 &nbsp;
                    <span class="area-legend-symbol legend-unbind">&nbsp;</span>不可选 &nbsp;
                <#else>
                    <span class="area-legend-symbol legend-usable">&nbsp;</span>可使用 ${areaStat.usable!} &nbsp;
                    <span class="area-legend-symbol legend-used">&nbsp;</span>使用中 ${areaStat.used!} &nbsp;
                    <span class="area-legend-symbol legend-maintain">&nbsp;</span>维修 ${areaStat.maintain!} &nbsp;
                    <span class="area-legend-symbol legend-unbind">&nbsp;</span>未绑定 ${areaStat.unbind!} &nbsp;
                </#if>
            </div>
        </div>
        <table class="table table-bordered table-area-map">
            <thead>
            <tr>
                <th colspan="2">&nbsp;</th>
                <#list 1..areaStat.maxRowCount as a>
                    <th>${a}</th>
                </#list>
            </tr>
            </thead>
            <tbody>
                <#list areaStat.columnList as column> <#-- 行 -->
                    <#if column_index == areaStat.layoutCorriderLine>
                    <tr>
                        <td colspan="2"><h4>过道</h4></td>
                        <td colspan="${areaStat.maxRowCount}">
                            <#if areaStat.turnoverList?has_content>
                                <div class="turnover-map">
                                    <ul>
                                        <#list areaStat.turnoverList as location>
                                            <#if location['PRODUCTNAME']?exists>
                                            <@showLocation location=location showStock=showStock locationCode=locationCode
                                            selectedLocationCode=selectedLocationCode allowSelectStatus=allowSelectStatus/>
                                        </#if>
                                        </#list>
                                    </ul>
                                </div>
                            </#if>
                        </td>
                    </tr>
                    </#if>
                <tr>
                    <td class="column-num">${column_index + 1}</td>
                    <td class="layer-num">
                        <ul>
                            <li>D</li>
                            <li>C</li>
                            <li>B</li>
                            <li>A</li>
                        </ul>
                    </td>
                    <#list column as row> <#-- 列 -->
                        <td>
                            <ul>
                                <#list row as location> <#-- 层 -->
                                <@showLocation location=location showStock=showStock locationCode=locationCode
                                selectedLocationCode=selectedLocationCode allowSelectStatus=allowSelectStatus/>
                            </#list>
                            </ul>
                        </td>
                    </#list>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    </#if>
</#list>

<#macro showLocation location showStock locationCode selectedLocationCode allowSelectStatus>
    <#if showStock> <#-- 显示某类库存 -->
    <li class="stat-<#if (customerId == location['CUSTOMERID'] && locationCode?default('')?contains(location['LOCATIONCODE']))>1<#else>0</#if> <#if selectedLocationCode?default('')?contains(location['LOCATIONCODE'])>stat-selected</#if>">
        <#if customerId == location['CUSTOMERID'] &&locationCode?has_content&&locationCode?contains(location['LOCATIONCODE'])&& allowSelectStatus?has_content && allowSelectStatus?contains(location['STORELOCATIONSTATUS']+"") >
            <a href="javascript:;" 
             data-placement="top"
                   data-html="true"
                   data-original-title="储位：${location['LOCATIONCODE']!}"
                   data-content="
                    客　　户：${location['CUSTOMERNAME']!} <br/>
                    商　　品：${location['PRODUCTNAME']!} <br/>
                    数　　量：${location['AMOUNT']!} <br/>
                    重　　量：${location['WEIGHT']!} <br/>
                    多品明细：${location['PRODUCTDETAIL']!} <br/>
                    入库单号：${location['INBOUNDREGISTERSERIALNO']!} <br/>
                    上架时间：${location['STOCKINTIME']?string}"
                   data-trigger="hover"
            class="popovers location-selector" ref="${location['STORECONTAINER']}" rev="${location['LOCATIONCODE']}">
            ${location['CUSTOMERNAME']!}&nbsp;${location['PRODUCTNAME']!}</a>
        <#else>
        ${location['CUSTOMERNAME']!}&nbsp;${location['PRODUCTNAME']!}
        </#if>
    </li>
    <#else>
    <li class="stat-${location['STORELOCATIONSTATUS']} <#if selectedLocationCode?default('')?contains(location['LOCATIONCODE'])>stat-selected</#if>">
        <#if (allowSelectStatus?contains(location['STORELOCATIONSTATUS']+""))><#-- 可选择则增加链接 -->
            <a href="javascript:;" class="location-selector" ref="${location['STORECONTAINER']}" title="${location['LOCATIONCODE']}">
            ${location['CUSTOMERNAME']!}&nbsp;${location['PRODUCTNAME']!}</a>
        <#else>
            <#if location["STORECONTAINER"]?exists>
                <a href="javascript:;"
                   data-placement="top"
                   data-html="true"
                   data-original-title="储位：${location['LOCATIONCODE']!}"
                   data-content="
                    客　　户：${location['CUSTOMERNAME']!} <br/>
                    商　　品：${location['PRODUCTNAME']!} <br/>
                    数　　量：${location['AMOUNT']!} <br/>
                    重　　量：${location['WEIGHT']!} <br/>
                    多品明细：${location['PRODUCTDETAIL']!} <br/>
                    入库单号：${location['INBOUNDREGISTERSERIALNO']!} <br/>
                    上架时间：${location['STOCKINTIME']?string}"
                   data-trigger="hover" class="popovers">${location['CUSTOMERNAME']!}&nbsp;${location['PRODUCTNAME']!}</a>
            <#else>
                &nbsp;
            </#if>
        </#if>
    </li>
    </#if>
</#macro>
 <script>
    
      $(function () { $('.popovers').popover();});
   
   </script>