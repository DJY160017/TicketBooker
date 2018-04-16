<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/3
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的信息</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/user/userInfo.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/user/userInfo.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/req_getUserInfo",
                success: function (data) {
                    $('#profile-username-input').val(data['user_name']);
                },
                error: function (result) {
                    console.log(result);
                }
            });
        });
    </script>
</head>
<body>
<header>
    <%@ include file="../header.jsp" %>
</header>
<div class="container" style="margin-top: 70px">
    <div class="row">
        <div class="col-xs-3 panel panel-default">
            <ul class="nav nav-pills nav-stacked">
                <li role="presentation" class="active">
                    <a href="javascript:void(0)" id="my-info-a" onclick="showInfo()">我的信息</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="my-order-a" onclick="showOrder()" style="color: #1b6d85">我的订单</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="my-discount-a" onclick="showDiscount()" style="color: #1b6d85">我的优惠券</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="my-venuebook-a" onclick="showMyVenueBook()" style="color: #1b6d85">我的场馆租用</a>
                </li>
            </ul>
        </div>

        <div class="col-md-9 panel panel-default user-info-panel">
            <div class="my-info" id="my-info">
                <div class="profile-tile">
                    <p class="profile-title-p">个人信息</p>
                </div>
                <div class="profile-input">
                    <div class="input-group">
                        <span class="input-group-addon" id="sizing-addon1">用户名:</span>
                        <input class="form-control" type="text" name="introduction" placeholder="请输入您的用户名"
                               id="profile-username-input" aria-describedby="sizing-addon1" disabled="disabled"
                               required/>
                    </div>
                </div>
                <div class="profile-button">
                    <div class="profile-modify" id="profile-modify" onclick="show_profile_save()">
                        <button class="btn btn-info btn-sm">修改</button>
                    </div>
                    <div class="profile-save" id="profile-save" onclick="show_profile_modify()" hidden="hidden">
                        <button class="btn btn-info btn-sm">保存</button>
                    </div>
                </div>
                <div class="password-tile">
                    <p class="password-title-p">修改密码</p>
                </div>
                <div class="profile-password-panel" id="profile-password">
                    <form method="POST">
                        <div class="profile-input-modify-password">
                            <div class="input-group-vertical">
                                <input id="profile-password-new" class="form-control" type="password" name="password"
                                       placeholder="请输入密码" disabled='disabled' required/>
                                <input id="profile-password-again" class="form-control" type="password"
                                       name="password-again"
                                       placeholder="请再输入密码" disabled="disabled" required/>
                            </div>
                        </div>
                    </form>
                    <div class="profile-password-button">
                        <div class="profile-password-modify" id="password-modify" onclick="show_password_save()">
                            <button class="btn btn-info btn-sm">修改</button>
                        </div>
                        <div class="profile-password-save" id="password-save" onclick="show_password_modify()"
                             hidden="hidden">
                            <button class="btn btn-info btn-sm">保存</button>
                        </div>
                    </div>
                </div>
                <div style="margin-top: 30px;margin-left: 375px;">
                    <strong style="color:red;" id="errorMessageField"></strong>
                </div>
            </div>
            <div class="my-order" id="my-order" style="width: 90%" hidden>
                <ul id="myOrderTab" class="nav nav-tabs">
                    <li class="active">
                        <a href="#alreadyPaid" data-toggle="tab">已支付</a>
                    </li>
                    <li>
                        <a href="#unPaid" data-toggle="tab">未支付</a>
                    </li>
                    <li>
                        <a href="#unSubscribed" data-toggle="tab">已退订</a>
                    </li>
                    <li>
                        <a href="#invalidOrder" data-toggle="tab">已失效</a>
                    </li>
                </ul>
                <div id="myOrderTabContent" class="tab-content" style="margin-top: 20px">
                    <div class="tab-pane fade in active" id="alreadyPaid">
                        <div class="alreadyPaid-table" id="alreadyPaid-table-id">
                            <table class="table table-hover table-striped" id="alreadyPaid-table-detail">
                                <thead>
                                <tr>
                                    <th>订单时间</th>
                                    <th>名称</th>
                                    <th>总价</th>
                                    <th>订单详情</th>
                                    <th>订单退订</th>
                                </tr>
                                </thead>
                                <tbody id="alreadyPaid-table-detail-body"></tbody>
                            </table>
                        </div>
                        <div class="miss-alreadyPaid-result" id="miss-alreadyPaid-result-id" hidden>
                            <p class="text-center">喔喔！ 您尚未有已支付的订单</p>
                        </div>
                    </div>
                    <div class="tab-pane fade in" id="unPaid">
                        <div class="unPaid-table" id="unPaid-table-id">
                            <table class="table table-hover table-striped" id="unPaid-table-detail">
                                <thead>
                                <tr>
                                    <th>订单时间</th>
                                    <th>名称</th>
                                    <th>总价</th>
                                    <th>订单详情</th>
                                    <th>支付订单</th>
                                </tr>
                                </thead>
                                <tbody id="unPaid-table-detail-body"></tbody>
                            </table>
                        </div>
                        <div class="miss-unPaid-result" id="miss-unPaid-result-id" hidden>
                            <p class="text-center">喔喔！ 您尚未有未支付的订单</p>
                        </div>
                    </div>
                    <div class="tab-pane fade in" id="unSubscribed">
                        <div class="unSubscribed-table" id="unSubscribed-table-id">
                            <table class="table table-hover table-striped" id="unSubscribed-table-detail">
                                <thead>
                                <tr>
                                    <th>订单时间</th>
                                    <th>名称</th>
                                    <th>总价</th>
                                    <th>订单详情</th>
                                </tr>
                                </thead>
                                <tbody id="unSubscribed-table-detail-body"></tbody>
                            </table>
                        </div>
                        <div class="miss-unSubscribed-result" id="miss-unSubscribed-result-id" hidden>
                            <p class="text-center">喔喔！ 您尚未有已退订的订单</p>
                        </div>
                    </div>
                    <div class="tab-pane fade in" id="invalidOrder">
                        <div class="invalidOrder-table" id="invalidOrder-table-id">
                            <table class="table table-hover table-striped" id="invalidOrder-table-detail">
                                <thead>
                                <tr>
                                    <th>订单时间</th>
                                    <th>名称</th>
                                    <th>总价</th>
                                    <th>订单详情</th>
                                </tr>
                                </thead>
                                <tbody id="invalidOrder-table-detail-body"></tbody>
                            </table>
                        </div>
                        <div class="miss-invalidOrder-result" id="miss-invalidOrder-result-id" hidden>
                            <p class="text-center">喔喔！ 您尚未有已失效的订单</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="my-discount" id="my-discount" hidden>
                <div class="discount-title" style="border-bottom: 1px solid rgba(51, 51, 51, 0.3);">
                    <p class="discount-title-p">优惠券</p>
                </div>
                <div class="discount-table" id="discount-table-id" style="margin-top: 20px;">
                    <table class="table table-hover table-striped" id="discount-table-detail">
                        <thead>
                        <tr>
                            <th>时间</th>
                            <th>抵用价格</th>
                        </tr>
                        </thead>
                        <tbody id="discount-table-detail-body"></tbody>
                    </table>
                </div>
                <div class="miss-discount-result" id="miss-discount-result-id" style="margin-top: 70px;" hidden>
                    <p class="text-center">喔喔！ 您尚未有优惠券</p>
                </div>
            </div>
            <div class="my-venuebook" id="my-venuebook" hidden>
                <div class="venuebook-title" style="border-bottom: 1px solid rgba(51, 51, 51, 0.3);">
                    <p class="venuebook-title-p">场馆租用信息</p>
                </div>
                <div class="venuebook-table" id="venuebook-table-id" style="margin-top: 20px;">
                    <table class="table table-hover table-striped" id="venuebook-table-detail">
                        <thead>
                        <tr>
                            <th>场馆名称</th>
                            <th>节目名称</th>
                            <th>开始时间</th>
                            <th>结束时间</th>
                            <th>总价</th>
                            <th>场馆地址</th>
                        </tr>
                        </thead>
                        <tbody id="venuebook-table-detail-body"></tbody>
                    </table>
                </div>
                <div class="miss-venuebook-result" id="miss-venuebook-result-id" style="margin-top: 70px;" hidden>
                    <p class="text-center">喔喔！ 您尚未租用过场馆</p>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="order-detail-show" tabindex="-1" role="dialog" aria-labelledby="order-show-modal-label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="order-show-modal-label">订单详情</h4>
            </div>
            <div class="modal-body text-center" id="order-show-content"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="unSubscribed-show" tabindex="-1" role="dialog" aria-labelledby="unSubscribedModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="unSubscribedModalLabel">订单退订</h4>
            </div>
            <div class="modal-body text-center" id="unSubscribed-show-content"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="unSubscribe_show()">退订</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="account-show" tabindex="-1" role="dialog" aria-labelledby="accountModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="accountModalLabel">账号输入</h4>
            </div>
            <div class="modal-body text-center" id="account-show-content">
                <form id="accountForm" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-md-3 control-label" for="bank_account">银行卡号：</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="bank_account" name="bank_account"
                                   placeholder="请输入您的银行卡号">
                        </div>
                    </div>
                    <strong class="col-md-offset-4" style="color:red;" id="accountErrorMessageField"></strong>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="unSubscribe()">退订</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
