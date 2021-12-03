<#escape x as x?html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>天津海吉星</title>
    <link href="${base}/assets/flatlab/css/bootstrap.min.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/bootstrap-reset.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/style.css" rel="stylesheet">
    <script src="${base}/assets/flatlab/js/html5shiv.js"></script>
    <script src="${base}/assets/flatlab/js/respond.min.js"></script>
    <script src="${base}/assets/flatlab/js/jquery.js"></script>
    <style>
        body {
            -webkit-print-color-adjust: exact;
        }

        .location-map table.table-area-map td ul li {
            width: 32px;
            margin: 1px;
            font-size: 11px;
            height: 11px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .location-map table.table-area-map td ul li.stat-0, .location-map table.table-area-map td ul li.stat-1, .location-map table.table-area-map td ul li.stat-3, .location-map table
        .table-area-map td ul li.stat-4 {
            background: #fff;
            border: solid 1px windowtext;
        }

        .location-map table.table-area-map td ul li.stat-2 {
            background: #136c3c;
            border: solid 6px windowtext;
        }

        .location-map table.table-area-map th {
            padding: 0px;
            font-size: 1.2em;
        }

        .location-map table.table-area-map td.column-num {
            font-size: 1.2em;
        }

        .table-bordered > thead > tr > th, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > tbody > tr > td, .table-bordered > tfoot > tr > td {
            border: 1px solid windowtext;
        }

        @media print {
            table, .table-bordered > thead > tr > th, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > tbody > tr > td, .table-bordered > tfoot > tr > td {
                border: solid windowtext !important;
                border-width: 1px !important;
            }
        }
    </style>
</head>
<body style="background: #fff;">
<div class="location-map" style="width:780px;">
    <#list locationStats as areaStat> <#-- 库区 -->
        <#if (areaStat.maxRowCount > 0)>
            <#if totalMaxRowCount < areaStat.maxRowCount>
                <#assign totalMaxRowCount = areaStat.maxRowCount/>
            </#if>
            <div class="store-area">
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
                                ${areaStat.areaCode!} ${areaStat.areaName!}
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
            <li class="stat-<#if (customerId == location['CUSTOMERID'] && locationCode?default('')?contains(location['LOCATIONCODE']))>1<#else>0</#if>">
                &nbsp;
            </li>
        <#else>
            <li class="stat-${location['STORELOCATIONSTATUS']}">
                &nbsp;
            </li>
        </#if>
    </#macro>
</div>
</body>
</html>
</#escape>
