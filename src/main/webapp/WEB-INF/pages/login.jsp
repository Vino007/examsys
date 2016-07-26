<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
   <title>登录</title>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/css/login.css">
    <!-- 若已经登录，直接跳转登录界面 -->
     <shiro:authenticated>
    <script type="text/javascript">
        window.location.href = 'pages/index.html';
    </script>
    </shiro:authenticated>
</head>
<body>
<div class="container">
    <div class="row">
        <form class="form-group col-lg-4 col-lg-offset-4 col-md-4 col-md-offset-4 col-sm-4 col-sm-offset-4" role="form">
            <fieldset>
                <legend>登录</legend>
                <div class="control-group">
                    <input name="username" class="input-xlarge form-control" placeholder="username" required autofocus>
                </div>
                <div class="control-group">
                    <input name="password" type="password" class="input-xlarge form-control" placeholder="password" required>
                </div>
                <div class="control-group row">
                    <div class="col-lg-6">
                        <button type="submit" class="btn btn-lg btn-block btn-primary">登录</button>
                    </div>
                    <div class="col-lg-6">
                        <button type="button" id="register-btn" class="btn btn-lg btn-block">注册</button>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
<script src="resources/js/jquery-2.2.4.min.js"></script>
<script src="resources/bootstrap/js/bootstrap.min.js"></script>
<script>
    (function () {
        $('form').submit(function (e) {
        	
            e.preventDefault();
            var usr = $('input[name=username]').val(),
                    pwd = $('input[name=password]').val();
            var data = {'username': usr, 'password': pwd};
            $.ajax({
                url: 'login',
                async: true,
                type: 'POST',
                data: data,
                dataType:'json'
            }).done(function (data) {
                if (data.success) {
                    var dataStr = JSON.stringify(data);
                    sessionStorage.setItem('username', usr);
                    sessionStorage.setItem('data', dataStr);
                    window.location.href = 'pages/index.html';
                } else {
                    alert(data.msg);
                }
            });
        });
        $('#register-btn').click(function () {
            window.location.href = 'register.html';
        });
    })();
</script>
</body>
</html>