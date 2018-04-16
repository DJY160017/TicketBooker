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
    <title>我的场馆信息</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/venue/header.css" rel="stylesheet">
    <link href="../../css/venue/venueInfo.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/venue/venueLogIn.js"></script>
    <script src="../../js/venue/venueInfo.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/venue/req_getVenueInfo",
                success: function (data) {
                    $('#venueID').val(venueIDFormat(data['venue']['venueID']));
                    $('#venue_name').val(data['venue']['name']);
                    $('#venue_raw').val(data['venue']['raw_num']);
                    $('#venue_col').val(data['venue']['col_num']);
                    $('#venue_price').val(data['venue']['price']);
                    $('#address').val(data['venue']['address']);
                    if(data['account'] !== 'null'){
                        $('#venue-account-value').val(data['account']);
                    }
                    var state = data['venue']['venueState'];
                    if (state === 'Unapproved') {
                        $('#venue_state').html("尚未审核");
                        $('#venue_state').css('color', 'rgb(279,173,79)');
                    } else if (state === 'NotThrough') {
                        $('#venue_state').html("未通过审核，请仔细检查您的场馆信息");
                        $('#venue_state').css('color', 'rgb(217,84,79)');
                    } else {
                        $('#venue_state').html("审核通过");
                        $('#venue_state').css('color', 'rgb(91,184,93)');
                    }
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
    <%@ include file="../venue/header.jsp" %>
</header>
<div class="container" style="margin-top: 60px">
    <div class="row">
        <div class="col-xs-3 panel panel-default">
            <ul class="nav nav-pills nav-stacked">
                <li role="presentation" class="active">
                    <a href="javascript:void(0)" id="venueInfo-a" onclick="showVenueInfo()">场馆信息</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="venue-plan-a" onclick="showPlan()" style="color: #1b6d85">我的计划</a>
                </li>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default venue-info-panel">
            <div class="venueInfo" id="venueInfo">
                <div class="venueInfo-tile">
                    <p class="venueInfo-title-p">场馆信息（<b id="venue_state"></b>）</p>
                </div>
                <div class="venueInfo-input">
                    <form id="venueInputForm" class="form-horizontal" role="form">
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="venueID" style="padding-left: 0">场馆账号：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="venueID"
                                       name="venueID" value="" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="venue_name" style="padding-left: 0">场馆名称：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="venue_name" name="venue_name"
                                       placeholder="请输入场馆名称" required disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="venue_raw" style="padding-left: 0">场馆排数：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="venue_raw" name="venue_raw"
                                       placeholder="请输入场馆的总排数" required disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="venue_col" style="padding-left: 0">场馆行数：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="venue_col" name="venue_col"
                                       placeholder="请输入场馆的总行数" required disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="venue_price"
                                   style="padding-left: 0">租用价格：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="venue_price" name="venue_price"
                                       placeholder="请输入场馆的租用价格" required disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="address" style="padding-left: 0;">场馆地址：</label>
                            <div class="col-md-5">
                                <textarea class="form-control" rows="3" cols="5" id="address" name="address"
                                          maxlength="50"
                                          style="resize: none" placeholder="请输入场馆地址" required disabled></textarea>
                            </div>
                        </div>
                    </form>
                    <div class="venueInfo-button">
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="venue-info-modify"
                           onclick="venue_modify()">修改</a>
                        <a href="javascript:void(0)" class="btn btn-success btn-sm" id="venue-info-cancel"
                           onclick="venue_cancel()"
                           hidden>取消</a>
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="venue-info-save"
                           onclick="venue_save()"
                           hidden>保存</a>
                    </div>
                </div>
                <div class="venue-account-tile">
                    <p class="venue-account-title-p">预留场馆银行账号</p>
                </div>
                <div class="venue-account-panel" id="venue-=account">
                    <form method="POST">
                        <div class="venue-account-input-password">
                            <div class="input-group-vertical">
                                <input id="venue-account-value" class="form-control" type="text" name="account"
                                       placeholder="请输入您的银行账号" disabled='disabled' required/>
                            </div>
                        </div>
                        <strong class="col-md-offset-5" style="color:red;" id="accountErrorMessageField"></strong>
                    </form>
                    <div class="venue-account-button">
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="account-modify"
                           onclick="account_modify()">修改</a>
                        <a href="javascript:void(0)" class="btn btn-success btn-sm" id="account-cancel"
                           onclick="account_cancel()"
                           hidden>取消</a>
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="account-save"
                           onclick="account_save()" hidden>保存</a>
                    </div>
                </div>
                <div class="password-tile">
                    <p class="password-title-p">修改密码</p>
                </div>
                <div class="venue-password-panel" id="venue-password">
                    <form method="POST">
                        <div class="venue-input-modify-password">
                            <div class="input-group-vertical">
                                <input id="venue-password-new" class="form-control" type="password" name="password"
                                       placeholder="请输入密码" disabled='disabled' required/>
                                <input id="venue-password-again" class="form-control" type="password"
                                       name="password-again"
                                       placeholder="请再输入密码" disabled="disabled" required/>
                            </div>
                        </div>
                        <strong class="col-md-offset-5" style="color:red;" id="errorMessageField"></strong>
                    </form>
                    <div class="venue-password-button">
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="password-modify"
                           onclick="password_modify()">修改</a>
                        <a href="javascript:void(0)" class="btn btn-success btn-sm" id="password-cancel"
                           onclick="password_cancel()"
                           hidden>取消</a>
                        <a href="javascript:void(0)" class="btn btn-primary btn-sm" id="password-save"
                           onclick="password_save()" hidden>保存</a>
                    </div>
                </div>
            </div>
            <div class="venue-plan" id="venue-plan" hidden>
                <div class="plan-title" id="venue-plan-title">
                    <p class="plan-title-p">场馆计划</p>
                </div>
                <div class="plan-content">
                    <div class="table-responsive venue-plan-table">
                        <table class="table" style="vertical-align: middle;text-align: center;">
                            <tbody id="venue-plan-body"></tbody>
                        </table>
                    </div>
                    <div class="miss-plan-result" id="miss-plan-result-id" hidden>
                        <p class="text-center">喔喔！ 您的场馆尚未有租用计划</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../venue/venueLogin.jsp" %>
</body>
</html>
