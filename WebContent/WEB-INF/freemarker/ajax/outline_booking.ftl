<#escape x as x?html>
<div id="outline">
    <#if areaStats?has_content>
        <div class="row">
            <div class="col-md-4">
                <#assign inboundTotal = 0>
                <#assign outboundTotal = 0>
                <#if inboundStats?has_content>
                    <div>
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th width="50">#</th>
                                <th width="nowrap">商品</th>
                                <th width="100">预约托盘数</th>
                            </tr>
                            </thead>
                            <#list inboundStats as stat>
                                <#assign inboundTotal = inboundTotal + stat["CONTAINERCOUNT"]>
                                <tr>
                                    <td>${stat_index+1}</td>
                                    <td>${stat["PRODUCTCODE"]}${stat["PRODUCTNAME"]}</td>
                                    <td>${stat["CONTAINERCOUNT"]}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </#if>
                <#if outboundStats?has_content>
                    <div>
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th width="50">#</th>
                                <th width="nowrap">商品</th>
                                <th width="100">预约托盘数</th>
                            </tr>
                            </thead>
                            <#list outboundStats as stat>
                                <#assign outboundTotal = outboundTotal + stat["CONTAINERCOUNT"]>
                                <tr>
                                    <td>${stat_index+1}</td>
                                    <td>${stat["PRODUCTCODE"]}${stat["PRODUCTNAME"]}</td>
                                    <td>${stat["CONTAINERCOUNT"]}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </#if>
                <div>
                    <table class="table table-bordered">
                        <tr>
                            <td width="nowrap">入库预约合计：</td>
                            <td width="100">${inboundTotal}</td>
                        </tr>
                        <tr>
                            <td>出库预约合计：</td>
                            <td>${outboundTotal}</td>
                        </tr>
                        <tr>
                            <td>出入库盈亏合计：</td>
                            <td>${inboundTotal - outboundTotal}</td>
                        </tr>
                        <tr>
                            <td>预计可用托盘数：</td>
                            <td>
                                <#assign totalUsable = 0>
                                <#list areaStats as stat>
                                <#assign totalUsable = totalUsable + stat["USABLE"]/>
                            </#list>
                                ${totalUsable + inboundTotal + outboundTotal}
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="col-md-8">
                <script src="${base}/assets/echarts/echarts.js"></script>
                <script>
                    require.config({
                        packages: [
                            {
                                name: 'echarts',
                                location: '${base}/assets/echarts',
                                main: 'echarts'
                            }
                        ]
                    });

                    var colorArray = [
                        '#32cd32', '#da70d6', '#ff7f50', '#cccccc', '#6495ed',
                        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0',
                        '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700',
                        '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0'
                    ];
                </script>
                <#list areaStats as stat>
                    <div class="area-chart">
                        <div id="graph-${stat['ID']}" class="area-chart-graph"></div>
                        <script type="text/javascript">
                            require(
                                [
                                    'echarts',
                                    'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
                                ],
                                function (ec) {
                                    var myChart = ec.init(document.getElementById('graph-${stat['ID']}'));
                                    option = {
                                        title: {
                                            x: 'center',
                                            text: '${stat["CODE"]} ${stat["NAME"]}',
                                            link: '${base}/store/outline/locations?areaId=${stat['ID']}'
                                        },
                                        tooltip: {
                                            trigger: 'item',
                                            formatter: "{b}<br/>{c}"
                                        },
                                        //calculable : true,
                                        animation: false,
                                        color: colorArray,
                                        series: [
                                            {
                                                name: '访问来源',
                                                type: 'pie',
                                                radius: '80%',
                                                center: ['50%', '58%'],
                                                itemStyle: {
                                                    normal: {
                                                        label: {
                                                            show: false
                                                        },
                                                        labelLine: {
                                                            show: false
                                                        }
                                                    }
                                                },
                                                data: [
                                                    {value:${stat["USABLE"]}, name: '可使用'},
                                                    {value:${stat["USED"]}, name: '已使用'},
                                                    {value:${stat["MAINTAIN"]}, name: '维修'},
                                                    {value:${stat["UNBIND"]}, name: '未绑定'},
                                                    {value:${stat["RENTAREA"]}, name: '已包库'}
                                                ]
                                            }
                                        ]
                                    };
                                    // 为echarts对象加载数据
                                    myChart.setOption(option);
                                }
                            );
                        </script>
                    </div>
                </#list>
            </div>
        </div>
    <#else>
        <div class="alert alert-warning">商品未指定存放库区</div>
    </#if>
</div>
</#escape>