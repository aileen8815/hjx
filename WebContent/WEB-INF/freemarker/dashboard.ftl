<#escape x as x?html>
<div class="panel-body main-content-wrapper site-min-height">
<div class="row">
    <#if currentOperator.hasPermission('1300')>
        <div class="col-md-6">
            <section class="panel">
                <header class="panel-heading">
                    <a href="${base}/task/index?taskMode=${'工作通知'?url('utf-8')}">工作通知</a>
                    <span class="pull-right">
                        <a href="${base}/task/index?taskMode=${'工作通知'?url('utf-8')}">更多 ></a>
                    </span>
                </header>
                <div class="panel-body main-content-wrapper">
                    <#if tasks?has_content>
                        <ul>
                            <#list tasks as task>
                                <#if task_index < 10>
                                    <li><a href="${base}/task/${task.id}"><span class="notify-type">${task.taskType}</span> ${task.subject!}</a></li>
                                </#if>
                            </#list>
                        </ul>
                    <#else>
                        没有工作通知
                    </#if>
                </div>
            </section>
        </div>
    </#if>
    <#if currentOperator.hasPermission('1400')>
        <div class="col-md-6">
            <section class="panel">
                <header class="panel-heading">
                    <a href="${base}/task/index?taskMode=${'预警提醒'?url('utf-8')}">预警提醒</a>
                    <span class="pull-right">
                        <a href="${base}/task/index?taskMode=${'预警提醒'?url('utf-8')}">更多 ></a>
                    </span>
                </header>
                <div class="panel-body main-content-wrapper">
                    <#if notices?has_content>
                        <ul>
                            <#list notices as task>
                                <#if task_index < 10>
                                    <li><a href="${base}/task/${task.id}"><span class="notify-type">${task.taskType}</span> ${task.subject!}</a></li>
                                </#if>
                            </#list>
                        </ul>
                    <#else>
                        没有预警提醒
                    </#if>
                </div>
            </section>
        </div>
    </div>
    </#if>
    <#if currentOperator.hasPermission('3300')>
        <section class="panel">
            <header class="panel-heading">
                <a href="${base}/store/outline/area">库区概览</a>
            </header>
            <div class="panel-body main-content-wrapper">
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

                <#list rootAreaStats.keySet() as key>
                    <#assign areaStats = rootAreaStats.get(key)>
                    <div class="row">
                        <div id="outline-${key}" class="col-md-12" style="height:400px"></div>
                        <script type="text/javascript">
                            require(
                                [
                                    'echarts',
                                    'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                                ],
                                function (ec) {
                                    var myChart = ec.init(document.getElementById('outline-${key}'));
                                    option = {
                                        title: {
                                            x: 'left',
                                            text: '${key}'
                                        },
                                        legend: {
                                            data: ['可使用', '已使用', '维修', '未绑定', '已包库']
                                        },
                                        tooltip: {
                                            trigger: 'axis',
                                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                                            }
                                        },
                                        calculable: true,
                                        color: colorArray,
                                        xAxis: [
                                            {
                                                type: 'category',
                                                data: [
                                                    <#list areaStats as stat>
                                                        '${stat['CODE']} ${stat['NAME']}'<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            }
                                        ],
                                        yAxis: [
                                            {
                                                type: 'value'
                                            }
                                        ],
                                        series: [
                                            {
                                                name: '已使用',
                                                type: 'bar',
                                                stack: '储位',
                                                barCategoryGap: '60%',
                                                data: [
                                                    <#list areaStats as stat>
                                                    ${stat['USED']}<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            },
                                            {
                                                name: '可使用',
                                                type: 'bar',
                                                stack: '储位',
                                                barCategoryGap: '60%',
                                                data: [
                                                    <#list areaStats as stat>
                                                    ${stat['USABLE']}<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            },
                                            {
                                                name: '维修',
                                                type: 'bar',
                                                stack: '储位',
                                                barCategoryGap: '60%',
                                                data: [
                                                    <#list areaStats as stat>
                                                    ${stat['MAINTAIN']}<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            },
                                            {
                                                name: '未绑定',
                                                type: 'bar',
                                                stack: '储位',
                                                barCategoryGap: '60%',
                                                data: [
                                                    <#list areaStats as stat>
                                                    ${stat['UNBIND']}<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            },
                                            {
                                                name: '已包库',
                                                type: 'bar',
                                                stack: '储位',
                                                barCategoryGap: '60%',
                                                data: [
                                                    <#list areaStats as stat>
                                                    ${stat['RENTAREA']}<#if (stat_index != areaStats?size -1)>,</#if>
                                                    </#list>
                                                ]
                                            }
                                        ]
                                    };

                                    myChart.setOption(option);
                                }
                            );
                        </script>
                    </div>
                </#list>
            </div>
        </section>
    </#if>
</div>
</#escape>
