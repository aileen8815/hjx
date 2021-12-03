<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title>天津海吉星</title>
    <!-- Bootstrap core CSS -->
    <link href="${base}/assets/flatlab/css/bootstrap.min.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/bootstrap-reset.css" rel="stylesheet">
    <!--external css-->
    <link href="${base}/assets/flatlab/assets/font-awesome/css/font-awesome.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/assets/easyui/themes/bootstrap/easyui.css">
    <link href="${base}/assets/select2/select2.css" rel="stylesheet"/>
    <link href="${base}/assets/select2/select2-bootstrap.css" rel="stylesheet"/>
    <link href="${base}/assets/bootstrap-table/bootstrap-table.css" rel="stylesheet"/>
    <!-- Custom styles for this template -->
    <link href="${base}/assets/flatlab/css/style.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/style-responsive.css" rel="stylesheet"/>
    <link href="${base}/assets/flatlab/assets/gritter/css/jquery.gritter.css" rel="stylesheet"/>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="${base}/assets/flatlab/js/html5shiv.js"></script>
    <script src="${base}/assets/flatlab/js/respond.min.js"></script>
    <![endif]-->
    <script src="${base}/assets/flatlab/js/jquery.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/parsley.remote.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/parsley.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/i18n/zh_cn.js"></script>
    <script src="${base}/assets/easyui/jquery.easyui.min.js"></script>
    <script src="${base}/assets/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script src="${base}/assets/select2/select2.js"></script>
    <script src="${base}/assets/flatlab/assets/gritter/js/jquery.gritter.min.js"></script>
    <script>
        $.ajaxSetup({cache: false});
        window.ParsleyValidator
                .addValidator('gt', function (value, requirement) {
                    return value > requirement;
                }, 32)
                .addMessage('zh_cn', 'gt', '输入值必须大于%s')
                .addValidator('lt', function (value, requirement) {
                    return value < requirement;
                }, 32)
                .addMessage('zh_cn', 'lt', '输入值必须小于%s')
                .addValidator('lessto', function (value, otherField) {
                    var otherValue = parseInt($(otherField).val());
                    return (value <= otherValue);
                }, 32)
                .addMessage('zh_cn', 'lessto', '不正确的值');
    </script>
    <sitemesh:write property='head'/>
</head>
<body>
<section id="container" class="">
    <!--header start-->
    <header class="header white-bg">
        <div class="sidebar-toggle-box">
            <div data-original-title="Toggle Navigation" data-placement="right" class="fa fa-bars tooltips"></div>
        </div>
        <!--logo start-->
        <a href="${base}/dashboard" class="logo"><img src="${base}/assets/flatlab/img/logo.png"></a>
        <!--logo end-->
        <div class="nav notify-row" id="top_menu">
            <ul class="nav top-menu">
                <!-- task dropdown start -->
                <li id="header_task_bar" class="dropdown"></li>
                <!-- task dropdown end -->
                <!-- notification dropdown start-->
                <li id="header_notification_bar" class="dropdown"></li>
                <!-- notification dropdown end -->
            </ul>
        </div>
        <div class="top-nav ">
            <ul class="nav pull-right top-menu">
                <li> <!--
          <input type="text" class="form-control search" placeholder="Search">
          --> </li>
                <!-- user login dropdown start-->
                <li class="dropdown"><a data-toggle="dropdown" class="dropdown-toggle" href="#"> <img alt=""
                                                                                                      src="${base}/assets/flatlab/img/profile-small.png">
                    <span class="username">${currentOperator.username}</span> <b class="caret"></b> </a>
                    <ul class="dropdown-menu logout extended">

                        <div class="log-arrow-up"></div>
                    <#if currentOperator.hasPermission('1500')>
                        <li><a href="${base}/security/profile"><i class=" fa fa-user"></i> 个人资料</a></li>
                    </#if>
                    <#if currentOperator.hasPermission('1600')>
                        <li><a href="${base}/security/profile/change-password"><i class="fa fa-key"></i> 修改密码</a></li>
                    </#if>
                        <li><a href="${base}/signout"><i class="fa fa-power-off"></i> 注销登录</a></li>
                    </ul>
                </li>
                <!-- user login dropdown end -->
            </ul>
        </div>
    </header>
    <!--header end-->
    <!--sidebar start-->
    <aside>
        <div id="sidebar" class="nav-collapse ">
            <!-- sidebar menu start-->
            <ul class="sidebar-menu" id="nav-accordion">
            <#if currentOperator.hasPermission('1000')>
                <li><a href="${base}/dashboard" ref="dashboard"> <i class="fa fa-dashboard"></i> <span>我的工作台</span> </a></li>
            </#if>
            <#if currentOperator.hasPermission('2000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-trello"></i> <span>仓储作业</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('2100')>
                            <li class="sub-menu"><a href="javascript:;">入库</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('2101')>
                                        <li><a href="${base}/store/inbound-booking">入库预约</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2102')>
                                        <li><a href="${base}/store/inbound-booking/check-index">入库预约审核</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2103')>
                                        <li><a href="${base}/store/inbound-register">入库单</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2104')>
                                        <li><a href="${base}/store/inbound-register?operationType=1" ref="inbound-settle">入库结算</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2105')>
                                        <li><a href="${base}/store/inbound-register?operationType=3" ref="inbound-payment">入库付款</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2106')>
                                        <li><a href="${base}/store/inbound-register?operationType=2" ref="inbound-check">延迟付款审核</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2107')>
                                        <li><a href="${base}/store/stockin">上架单</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>

                        <#if currentOperator.hasPermission('2200')>
                            <li class="sub-menu"><a href="javascript:;">出库</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('2201')>
                                        <li><a href="${base}/store/outbound-booking">出库预约</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2202')>
                                        <li><a href="${base}/store/outbound-booking/check-index">出库预约审核</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2203')>
                                        <li><a href="${base}/store/outbound-register">出库单</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2204')>
                                        <li><a href="${base}/store/stock-out">拣货单</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2205')>
                                        <li><a href="${base}/store/outbound-register?operationType=1" ref="outbound-settle">出库结算</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2206')>
                                        <li><a href="${base}/store/outbound-register?operationType=3" ref="outbound-payment">出库付款</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2207')>
                                        <li><a href="${base}/store/outbound-register?operationType=2" ref="outbound-check">延迟付款审核</a>
                                        </li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2208')>
                                        <li><a href="${base}/store/outbound-freight">发货单</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <!--
            <li class="sub-menu"> <a href="javascript:;">库存调整</a>
              <ul class="sub">
                <li><a href="${base}/store/stock-adjust/new-item">库存调整</a></li>
              </ul>
            </li>
            -->
                        <#if currentOperator.hasPermission('2400')>
                            <li class="sub-menu"><a href="javascript:;">库存盘点</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('2401')>
                                        <li><a href="${base}/store/stock-taking">盘点计划</a></li>
                                    </#if>
                                    <!--<li><a href="">初盘记录</a></li>
                                                    <li><a href="">复盘记录</a></li>
                                                    <li><a href="">终盘记录</a></li>-->
                                    <#if currentOperator.hasPermission('2402')>
                                        <li><a href="${base}/store/stock-taking?operate=1" ref="stock-taking-check">盘点审批</a>
                                        </li>
                                    </#if>
                                    <!-- <li><a href="">盘点调整</a></li>-->
                                </ul>
                            </li>
                        </#if>
                        <!--  <li class="sub-menu">
                        <a href="">库内移位</a>
                        <ul class="sub">
                            <li><a href="${base}/store/Stock-relocation">移位计划</a></li>
                            <li><a href="">移位作业</a></li>
                        </ul>
                    </li> -->
                        <#if currentOperator.hasPermission('2500')>
                            <li class="sub-menu"><a href="">货物报损</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('2501')>
                                        <li><a href="${base}/store/stock-wastage">报损登记</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2502')>
                                        <li><a href="${base}/store/stock-wastage?operatorType=1" ref="stock-wastage-check">报损审批</a>
                                        </li>
                                    </#if>
                                    <!-- <li><a href="">损失费用登记</a></li> -->
                                    <#if currentOperator.hasPermission('2503')>
                                        <li><a href="${base}/store/stock-wastage?operatorType=2"
                                               ref="stock-wastage-chargecheck">损失费用审批</a></li>
                                    </#if>
                                    <!-- <li><a href="">报损拣货</a></li>
                                                    <li><a href="">报损出库</a></li>-->
                                </ul>
                            </li>
                        </#if>
                        <#if currentOperator.hasPermission('2600')>
                            <li class="sub-menu"><a href="">货权转移</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('2601')>
                                        <li><a href="${base}/store/stock-owner-change">货权转移登记</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2602')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=0&&operationType=1"
                                               ref="stock-owner-sellsettle">货权转移卖方结算</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2603')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=1&&operationType=1"
                                               ref="stock-owner-buysettle">货权转移买方结算</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2604')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=0&&operationType=2"
                                               ref="stock-owner-sellcheck">货权转移卖方延付审核</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2605')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=1&&operationType=2"
                                               ref="stock-owner-buycheck">货权转移买方延付审核</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2606')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=0&&operationType=3"
                                               ref="stock-owner-sellpayment">货权转移卖方付款</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('2607')>
                                        <li><a href="${base}/store/stock-owner-change?customerType=1&&operationType=3"
                                               ref="stock-owner-buypayment">货权转移买方付款</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <!--  <li><a href="">转仓计划</a></li>
                                     <li><a href="">库存初始化</a></li> -->
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('3000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-laptop"></i> <span>库存浏览</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('3100')>
                            <li><a href="${base}/store/book-inventory">库存查询</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('3200')>
                            <li><a href="${base}/store/book-inventory/expire">库存预警</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('3300')>
                            <li><a href="${base}/store/outline/area">库存平面图</a></li>
                        </#if>
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('4000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-dropbox"></i> <span>仓库租赁</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('4100')>
                            <li><a href="${base}/tenancy/store-contract">租赁合同</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('4200')>
                            <li><a href="${base}/tenancy/store-contract/carryover">合同结转</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('4300')>
                            <li><a href="${base}/tenancy/store-contract/eviction">合同退租</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('4400')>
                            <li><a href="${base}/tenancy/store-contract/cancelEviction">撤销退租</a></li>
                        </#if>
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('5000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-truck"></i> <span>运力资源</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('5100')>
                            <li><a href="${base}/truck/resource-management">资源管理</a></li>
                        </#if>
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('6000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-money"></i> <span>费用管理</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('6100')>
                            <li><a href="${base}/finance/payment/receivable">应收费用</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6200')>
                            <li><a href="${base}/finance/payment">收款单据</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6201')>
                            <li><a href="${base}/finance/payment/return-fee">退款单据</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6300')>
                            <li><a href="${base}/finance/payment/payable">应付费用</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6400')>
                            <li><a href="${base}/finance/payment/query-guarantee-money">客户押金查询</a></li>
                        </#if>
                        <!--    <#if currentOperator.hasPermission('6500')>
            <li><a href="${base}/finance/payment/extra-charge">提成费用查询</a></li>
            </#if>
         -->
                        <#if currentOperator.hasPermission('6600')>
                            <li><a href="${base}/finance/fee-item">收费项目</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6700')>
                            <li><a href="${base}/settings/charge-type">计费类型</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6800')>
                            <li><a href="${base}/ce/rule">计费规则</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6801')>
                            <li><a href="${base}/ce/simple-rule">简单计费规则</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('6802')>
                            <li><a href="${base}/ce/rule/rule-detail">计费标准查询</a></li>
                        </#if>
                        <!--
            <#if currentOperator.hasPermission('6900')>
            <li><a href="${base}/ce/discount-rule">折扣规则</a></li>
            </#if>
           -->
                        <#if currentOperator.hasPermission('7000')>
                            <li><a href="${base}/finance/payment/account-checking">对账单</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('7100')>
                            <li><a href="${base}/finance/standing-book-daily/create-again">重新生成客户台帐</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('7200')>
                            <li><a href="${base}/finance/standing-book-daily/create-all-customer">重新生成所有客户台帐</a></li>
                        </#if>
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('8000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-bar-chart-o"></i> <span>统计分析</span> </a>
                    <ul class="sub">
                        <!-- 2017-08-11 新增 -->
                        <#if currentOperator.hasPermission('8100')>
                            <li><a href="${base}/report/book-inventory-detail">库存明细报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8200')>
                            <li><a href="${base}/report/book-inventory-summary">库存汇总报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8300')>
                            <li><a href="${base}/report/book-inventory-daily">库存日报表</a></li>
                        </#if>
                        <!-- 2017-08-11 END -->
                        <#if currentOperator.hasPermission('8100')>
                            <li><a href="${base}/report/report-bookinventory-index?type=2">库存报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8200')>
                            <li><a href="${base}/report/report-bookinventory-index?type=1">库存汇总报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8300')>
                            <li><a href="${base}/report/report-warehouse-daily">仓库日报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8700')>
                            <li><a href="${base}/report/report-warehouse">仓库汇总报表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8400')>
                            <li><a href="${base}/report/report-payment">收款明细表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8500')>
                            <li><a href="${base}/report/report-arrearagefee">应收款明细表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8600')>
                            <li><a href="${base}/report/report-charge-collect">营业收入汇总表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8601')>
                            <li><a href="${base}/finance/standing-book-daily/accounting">结算收费台账</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8602')>
                            <li><a href="${base}/finance/standing-book-daily/summary">客户结算收费汇总表</a></li>
                        </#if>
                        <#if currentOperator.hasPermission('8603')>
                            <li><a href="${base}/finance/standing-book-daily/billing">客户结算收费对账单</a></li>
                        </#if>
                    </ul>
                </li>
            </#if>
            <#if currentOperator.hasPermission('9000')>
                <li class="sub-menu"><a href="javascript:;"> <i class="fa fa-gear"></i> <span>系统设置</span> </a>
                    <ul class="sub">
                        <#if currentOperator.hasPermission('9100')>
                            <li class="sub-menu"><a href="javascript:;">仓储设置</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('9101')>
                                        <li><a href="${base}/settings/store-area">仓间库区</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9105')>
                                        <li><a href="${base}/store/store-area-assignee">库区分派</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9102')>
                                        <li><a href="${base}/settings/store-location">储位管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9103')>
                                        <li><a href="${base}/settings/store-container">容器管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9106')>
                                        <li><a href="${base}/settings/store-location-type">储位类型</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9107')>
                                        <li><a href="${base}/settings/store-container-type">容器类型</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9109')>
                                        <li><a href="${base}/settings/stock-adjust-type">库存调整类型</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9108')>
                                        <li><a href="${base}/settings/tally-area-type">装卸口类型</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9104')>
                                        <li><a href="${base}/settings/tally-area">装卸口管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9110')>
                                        <li><a href="${base}/settings/hand-set">手持机管理</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <#if currentOperator.hasPermission('9200')>
                            <li class="sub-menu"><a href="javascript:;">通用设置</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('9213')>
                                        <li><a href="${base}/settings/system-config">系统参数配置</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9201')>
                                        <li><a href="${base}/settings/customer">客户资料</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9211')>
                                        <li><a href="${base}/settings/customer-grade">客户等级</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9214')>
                                        <li><a href="${base}/settings/customer/grade-approve">等级审批</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9202')>
                                        <li><a href="${base}/settings/department">组织机构</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9203')>
                                        <li><a href="${base}/settings/product-category">商品类别</a></li>
                                    </#if>
                                <#-- <#if currentOperator.hasPermission('9204')>
                                 <li><a href="${base}/settings/product">商品管理</a></li>
                                 </#if>-->
                                    <#if currentOperator.hasPermission('9205')>
                                        <li><a href="${base}/settings/product-status">商品状态</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9206')>
                                        <li><a href="${base}/settings/area">区域管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9207')>
                                        <li><a href="${base}/settings/measure-unit">计量单位</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9208')>
                                        <li><a href="${base}/settings/tax-type">税种设置</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9209')>
                                        <li><a href="${base}/settings/booking-method">预约方式</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9210')>
                                        <li><a href="${base}/settings/packing">包装方式</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9212')>
                                        <li><a href="${base}/settings/stock-wastage-type">报损类型</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <#if currentOperator.hasPermission('9300')>
                            <li class="sub-menu"><a href="javascript:;">运输设置</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('9301')>
                                        <li><a href="${base}/settings/carrier">承运商管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9302')>
                                        <li><a href="${base}/settings/carrier-type">承运商类型</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9303')>
                                        <li><a href="${base}/settings/vehicle-type">车辆类型</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <#if currentOperator.hasPermission('9400')>
                            <li class="sub-menu"><a href="javascript:;">权限管理</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('9401')>
                                        <li><a href="${base}/security/operator">操作员管理</a></li>
                                    </#if>

                                    <#if currentOperator.hasPermission('9402')>
                                        <li><a href="${base}/security/role">角色管理</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9403')>
                                        <li><a href="${base}/security/permission">系统权限</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9404')>
                                        <li><a href="${base}/security/operator/log-index">操作日志</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                        <#if currentOperator.hasPermission('9500')>
                            <li class="sub-menu"><a href="javascript:;">系统支持</a>
                                <ul class="sub">
                                    <#if currentOperator.hasPermission('9501')>
                                        <li><a href="">系统日志</a></li>
                                    </#if>
                                    <#if currentOperator.hasPermission('9502')>
                                        <li><a href="">导入导出</a></li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                    </ul>
                </li>
            </#if>
            </ul>
            <!-- sidebar menu end-->
        </div>
    </aside>
    <!--sidebar end-->
    <!--main content start-->
    <section id="main-content">
        <section class="wrapper site-min-height">
            <!-- page start-->
            <section class="panel">
                <sitemesh:write property='body'/>
            </section>
            <!-- page end-->
        </section>
    </section>
    <!--main content end-->
</section>
<!-- js placed at the end of the document so the pages load faster -->
<script src="${base}/assets/flatlab/js/bootstrap.min.js"></script>
<script src="${base}/assets/flatlab/js/jquery.dcjqaccordion.2.7.js"></script>
<script src="${base}/assets/flatlab/js/jquery.scrollTo.min.js"></script>
<script src="${base}/assets/flatlab/js/jquery.nicescroll.js"></script>
<script src="${base}/assets/flatlab/js/respond.min.js"></script>
<script src="${base}/assets/My97DatePicker/WdatePicker.js"></script>
<!--common script for all pages-->
<script src="${base}/assets/flatlab/js/common-scripts.js"></script>
<script>
    function loadHeaderTaskAndNotice() {
        // 加载待办任务
        $.post('${base}/ajax/task', function (data) {
            $('#header_task_bar').html(data);
        });

        // 加载提醒通知
        $.post('${base}/ajax/notice', function (data) {
            $('#header_notification_bar').html(data);
        });
    }

    function populateSubmenu() {
        var url = "${servletPath}";
        var submenu = $("ul.sidebar-menu a[href^='" + url + "']")[0];// 选择第一个，排列菜单式将长的放后面

        if (typeof pageIndicator != 'undefined') {
            var submenu = $("ul.sidebar-menu a[ref='" + pageIndicator + "']")[0];
        }

        if (submenu == undefined) {
            var stop = url.lastIndexOf("/");
            var parentUrl = url.substring(0, stop);
            submenu = $("ul.sidebar-menu a[href^='" + parentUrl + "']")[0];
            if (submenu == undefined) {
                stop = parentUrl.lastIndexOf("/");
                var grandUrl = url.substring(0, stop);
                submenu = $("ul.sidebar-menu a[href^='" + grandUrl + "']")[0];
            }
        }
        if (submenu != undefined) {
            var uppermenu = $(submenu).parent().parent().prev();
            var supermenu = $(submenu).parent().parent().parent().parent().prev();
            $(supermenu).addClass("active").trigger('click');
            $(uppermenu).addClass("active").trigger('click');
            $(submenu).addClass("active");
            $(submenu).parent().addClass("active");
        }
    }

    $(function () {
        populateSubmenu();

        // 必填项目打上红边
        $('input[data-parsley-required="true"], select[data-parsley-required="true"]').css('border-left', '1px solid #f00');
        $('select.form-control').select2();

        loadHeaderTaskAndNotice();
        // 每分钟刷新任务
        // setInterval("loadHeaderTaskAndNotice()", 60000);
    });
</script>
</body>
</html>
