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
    <!--<link href="${base}/assets/flatlab/css/navbar-fixed-top.css" rel="stylesheet">-->

    <!-- Custom styles for this template -->
    <link href="${base}/assets/flatlab/css/style.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/style-responsive.css" rel="stylesheet"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="${base}/assets/flatlab/js/html5shiv.js"></script>
    <script src="${base}/assets/flatlab/js/respond.min.js"></script>
    <![endif]-->

    <!-- js placed at the end of the document so the pages load faster -->
    <script src="${base}/assets/flatlab/js/jquery.js"></script>

    <script src="${base}/assets/flatlab/js/parsley/parsley.remote.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/parsley.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/i18n/zh_cn.js"></script>
    <script src="${base}/assets/easyui/jquery.easyui.min.js"></script>
    <script src="${base}/assets/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script src="${base}/assets/select2/select2.js"></script>

    <sitemesh:write property='head'/>
</head>

<body class="full-width">
<script>
        $.ajaxSetup({cache:false});
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
<section id="container" class="">
    <!--header start-->
    <header class="header white-bg">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="fa fa-bar"></span>
                <span class="fa fa-bar"></span>
                <span class="fa fa-bar"></span>
            </button>

            <!--logo start-->
            <a href="${base}/customer/customer-book-inventory" class="logo"><img src="${base}/assets/flatlab/img/logo.png"></a>
            <!--logo end-->
            <div class="horizontal-menu navbar-collapse collapse ">
                <ul class="nav navbar-nav">
                    <li><a href="${base}/customer/customer-book-inventory">首页</a></li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" data-hover="dropdown" class="dropdown-toggle" href="#">预约 <b class=" fa fa-angle-down"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="${base}/customer/customer-inbound-booking">入库预约</a></li>
                            <li><a href="${base}/customer/customer-outbound-booking">出库预约</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" data-hover="dropdown" class="dropdown-toggle" href="#">查询 <b class=" fa fa-angle-down"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="${base}/customer/customer-book-inventory">库存查询</a></li>
                            <li><a href="${base}/customer/inbound-receipt-query">入库台账</a></li>
                            <li><a href="${base}/customer/outbound-check-query">出库台账</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" data-hover="dropdown" class="dropdown-toggle" href="#">运力资源 <b class=" fa fa-angle-down"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="${base}/customer/c-resource-management">资源管理</a></li>
                             
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" data-hover="dropdown" class="dropdown-toggle" href="#">费用查询 <b class=" fa fa-angle-down"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="${base}/customer/payment-query">费用单查询</a></li>
                            <li><a href="${base}/customer/payment-query?paymentStatus=0">应付款查询</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div class="horizontal-menu pull-right">
                <ul class="nav navbar-nav">
                    <li><a href="javascript:;">欢迎您，${currentCustomer.name!}</a></li>
                    <li><a href="${base}/customer/login/logout">注销</a></li>
                </ul>
            </div>

        </div>

    </header>
    <!--header end-->
    <!--sidebar start-->

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
    <!--footer start-->
    <footer class="site-footer">
        <div class="text-center">
            2014 &copy; 天津海吉星.
            <a href="#" class="go-top">
                <i class="fa fa-angle-up"></i>
            </a>
        </div>
    </footer>
    <!--footer end-->
</section>

<!-- js placed at the end of the document so the pages load faster -->
<script src="${base}/assets/flatlab/js/bootstrap.min.js"></script>
<script src="${base}/assets/flatlab/js/hover-dropdown.js"></script>
<script src="${base}/assets/flatlab/js/jquery.dcjqaccordion.2.7.js"></script>
<script src="${base}/assets/flatlab/js/jquery.scrollTo.min.js"></script>
<script src="${base}/assets/flatlab/js/jquery.nicescroll.js"></script>
<script src="${base}/assets/flatlab/js/respond.min.js"></script>
<script src="${base}/assets/My97DatePicker/WdatePicker.js"></script>

<!--common script for all pages-->
<script src="${base}/assets/flatlab/js/common-scripts.js"></script>
<script>
    $(function () {
        $('select.form-control').select2();
        // 必填项目打上红边
        $('input[data-parsley-required="true"], select[data-parsley-required="true"]').css('border-left', '1px solid #f00');
    })
</script>
</body>
</html>
