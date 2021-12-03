<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>天津海吉星</title>
    <link href="${base}/assets/flatlab/css/login.css" rel="stylesheet" type="text/css"/>
    <script src="${base}/assets/flatlab/js/jquery.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/parsley.remote.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/parsley.min.js"></script>
    <script src="${base}/assets/flatlab/js/parsley/i18n/zh_cn.js"></script>
</head>

<body class="login-bg">
<div class="lg-top"></div>
<div class="login">
    <form id="loginForm">
        <div class="login-t"></div>
        <div class="login-c">
            <div class="login-i"><input type="text" id="username" name="username" placeholder="用户名" data-parsley-required="true">
            </div>
            <div class="login-i"><input type="password" id="password" name="password" placeholder="密码" data-parsley-required="true">
            </div>
        </div>
        <div class="alert-warning"></div>
        <div class="login-c lg-pad">
            <input type="button" id="loginBtn" class="lg-btn" value="登 录"/>
        </div>
        <div class="login-b"></div>
    </form>
</div>

<!-- js placed at the end of the document so the pages load faster -->
<script src="${base}/assets/flatlab/js/jquery.js"></script>
<script src="${base}/assets/flatlab/js/parsley/parsley.remote.min.js"></script>
<script src="${base}/assets/flatlab/js/parsley/parsley.min.js"></script>
<script src="${base}/assets/flatlab/js/parsley/i18n/zh_cn.js"></script>
<script>
    $('#loginBtn').click(function(){
        if ($('#loginForm').parsley().validate()) {
            $.ajax({
                url: "${base}/signin",
                data: $("#loginForm").serialize(),
                type: "POST",
                error: function (request) {
                    $('.alert-warning').html("表单提交出错，请稍候再试");
                    $('.alert-warning').removeClass('hide');
                },
                success: function (data) {
                    var result = eval('(' + data + ')');
                    if (result.error == 0) {
                        location.href = '${base}/dashboard';
                    } else {
                        $('.alert-warning').html(result.message);
                        $('.alert-warning').removeClass('hide');
                    }
                }
            });
        }
    });
</script>
</body>
</html>