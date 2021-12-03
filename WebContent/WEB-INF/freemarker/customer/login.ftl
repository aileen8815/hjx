
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="fyunli">
    <link rel="shortcut icon" href="${base}/assets/flatlab/img/favicon.png">

    <title>HIGREEN</title>

    <!-- Bootstrap core CSS -->
    <link href="${base}/assets/flatlab/css/bootstrap.min.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/bootstrap-reset.css" rel="stylesheet">
    <!--external css-->
    <link href="${base}/assets/flatlab/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <!-- Custom styles for this template -->
    <link href="${base}/assets/flatlab/css/style.css" rel="stylesheet">
    <link href="${base}/assets/flatlab/css/style-responsive.css" rel="stylesheet" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="${base}/assets/flatlab/js/html5shiv.js"></script>
    <script src="${base}/assets/flatlab/js/respond.min.js"></script>
    <![endif]-->
</head>

  <body class="login-body">

    <div class="container">

      <form class="form-signin" id="loginForm" action="${base}/customer/signin" method="post">
        <h2 class="form-signin-heading">天津海吉星冷库管理系统</h2>
        <div class="login-wrap">
          <div class="alert alert-warning hide"></div>
          <input type="text" name="custemerId" class="form-control" placeholder="用户名" autofocus>
          <input type="password" name="passWord" class="form-control" placeholder="密码">
          <button class="btn btn-lg btn-login btn-block" type="button" onclick="login()">登录</button>
        </div>
      </form>
    </div>
    <!-- js placed at the end of the document so the pages load faster -->
    <script src="${base}/assets/flatlab/js/jquery.js"></script>
    <script src="${base}/assets/flatlab/js/bootstrap.min.js"></script>
    <script>
    function login() {
      $.ajax({
        url: "${base}/customer/login/signin",
        data:$("#loginForm").serialize(),
        type: "POST",
        error: function(request) {
          $('.alert-warning').html("表单提交出错，请稍候再试");
          $('.alert-warning').removeClass('hide');
        },
        success: function(data) {
          var result = eval('(' + data + ')');
          if(result.error == 0){
            location.href = '${base}/customer/customer-book-inventory';
          }else{
            $('.alert-warning').html(result.message);
            $('.alert-warning').removeClass('hide');
          }
        }
      });
      return false;
    }
    </script>
  </body>
</html>
