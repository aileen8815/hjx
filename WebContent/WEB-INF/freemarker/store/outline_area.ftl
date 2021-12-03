<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outline/area">库区概览</a>

    <div class="area-chart-legend">
       <span class="area-chart-legend pull-right">
            <span class="area-legend-symbol legend-usable">&nbsp;</span>可使用
            <span class="area-legend-symbol legend-used">&nbsp;</span>使用中
            <#--<span class="area-legend-symbol legend-engage">&nbsp;</span>预留-->
            <span class="area-legend-symbol legend-maintain">&nbsp;</span>维修
            <span class="area-legend-symbol legend-unbind">&nbsp;</span>未绑定
            <span class="area-legend-symbol legend-unbind">&nbsp;</span>已包库
       </span>
    </div>
</header>
<div class="panel-body main-content-wrapper">
    <#if areaStats?has_content>
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
                                title : {
                                    x: 'center',
                                    text: '${stat["CODE"]} ${stat["NAME"]}',
                                    link: '${base}/store/outline/locations?areaId=${stat['ID']}'
                                },
                                tooltip : {
                                    trigger: 'item',
                                    formatter: "{b}<br/>{c}"
                                },
                                //calculable : true,
                                animation: false,
                                color: colorArray,
                                series : [
                                    {
                                        name:'访问来源',
                                        type:'pie',
                                        radius : '80%',
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
                                        data:[
                                            {value:${stat['USABLE']}, name:'可使用'},
                                            {value:${stat['USED']}, name:'已使用'},
                                            {value:${stat['MAINTAIN']}, name:'维修'},
                                            {value:${stat['UNBIND']}, name:'未绑定'},
                                            {value:${stat['RENTAREA']}, name:'已包库'}
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
    </#if>
</div>
</#escape>

