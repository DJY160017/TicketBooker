<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/8
  Time: 21:24
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>付款</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/external_balance/payment.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/external_balance/payment.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        });
    </script>
</head>
<body>
<div class="container main">
    <div class="head">
        <span class="glyphicon glyphicon-credit-card pay-logo" aria-hidden="true"></span>
        <p class="pay-introduction">付款</p>
    </div>
    <div class="pay-input">
        <form id="payForm" class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-md-3 control-label" for="account">卡号：</label>
                <div class="col-md-6">
                    <input type="text" class="form-control" id="account" name="account" placeholder="请输入您的银行卡号"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="password">密码：</label>
                <div class="col-md-6">
                    <input type="password" class="form-control" id="password" name="password" placeholder="请输入您的银行卡密码"
                           required>
                </div>
            </div>
            <strong class="col-md-offset-5" style="color:red;" id="errorMessageField"></strong>
        </form>
    </div>
    <div class="pay-button">
        <button class="btn btn-success pay-cancel" id="cancel" onclick="cancelPay()">取消</button>
        <button class="btn btn-primary pay-confirm" id="confirm" onclick="pay()">支付</button>
    </div>
</div>
<div class="modal fade" id="paySuccess" tabindex="-1" role="dialog" aria-labelledby="paySuccessLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="paySuccessLabel">支付成功</h4>
            </div>
            <div class="modal-body text-center" id="paySuccess-body">
                <p>支付已成功，你可去用户中心进行查看或者返回首页继续购票</p>
            </div>
            <div class="modal-footer">
                <div class="login-btn-group">
                    <button type="button" class="btn btn-primary" onclick="showUserCenter()">前去查看</button>
                    <button type="button" class="btn btn-success" onclick="showHome()">返回首页</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="cancelPay" tabindex="-1" role="dialog" aria-labelledby="cancelPayLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="cancelPayLabel">支付取消</h4>
            </div>
            <div class="modal-body" id="cancel-pay-body"></div>
            <div class="modal-footer">
                <div class="login-btn-group">
                    <button type="button" class="btn btn-success" data-dismiss="modal">继续支付</button>
                    <button type="button" class="btn btn-primary" onclick="cancelUserPay()">取消支付</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="backTooltip" tabindex="-1" role="dialog" aria-labelledby="backTooltipLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="backTooltipLabel">系统提示</h4>
            </div>
            <div class="modal-body" id="back-tooltip-body">
                <p>你当前处于支付页面，无法直接通过浏览器后退，你可以选择取消，但取消后需要在15分钟内完成支付，不然就会自动取消订单喔</p>
            </div>
            <div class="modal-footer">
                <div class="login-btn-group">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>
