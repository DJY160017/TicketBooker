<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/5
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>场馆申请</title>


    <link href="../../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../../css/reset.css" rel="stylesheet">
    <link href="../../../css/venue/header.css" rel="stylesheet">
    <link href="../../../css/venue/venueApplication.css" rel="stylesheet">
    <script src="../../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../../js/tool/bootstrap.js"></script>
    <script src="../../../js/venue/venueLogIn.js"></script>
    <script src="../../../js/venue/venueApplication.js"></script>
</head>
<body>
<header>
    <%@ include file="../../venue/header.jsp" %>
</header>
<div class="venue-register panel panel-default" style="height: 120%;">
    <div class="venue-register-info">
        <form id="venueRegisterForm" class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-md-3 control-label" for="reg_venue_name" style="padding-left: 0">场馆名称：</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="reg_venue_name" name="reg_venue_name"
                           placeholder="请输入场馆名称" required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="reg_venue_password" style="padding-left: 0">场馆密码：</label>
                <div class="col-md-4">
                    <input type="password" class="form-control" id="reg_venue_password" name="reg_venue_password"
                           placeholder="请输入场馆密码" required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="reg_venue_password2" style="padding-left: 0">确认密码：</label>
                <div class="col-md-4">
                    <input type="password" class="form-control" id="reg_venue_password2" name="reg_venue_password2"
                           placeholder="请再次输入场馆密码" required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="venue_raw" style="padding-left: 0">场馆排数：</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="venue_raw" name="venue_raw" placeholder="请输入场馆的总排数"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="venue_col" style="padding-left: 0">场馆行数：</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="venue_col" name="venue_col" placeholder="请输入场馆的总行数"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="venue_price" style="padding-left: 0">租用价格：</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="venue_price" name="venue_price" placeholder="请输入场馆的租用价格"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="address" style="padding-left: 0;">场馆地址：</label>
                <div class="col-md-5">
                    <textarea class="form-control" rows="3" cols="5" id="address" name="address" maxlength="50"
                              style="resize: none" placeholder="请输入场馆地址" required></textarea>
                </div>
            </div>
            <strong class="col-md-offset-4" style="color:red;" id="errorMessageField"></strong>
        </form>
    </div>
    <div class="venue-register-button">
        <a href="javascript:void(0)" class="btn btn-success" onclick="cancelApplication()">取消</a>
        <a href="javascript:void(0)" class="btn btn-primary" onclick="apply()">申请</a>
    </div>
</div>
<div class="modal fade" id="applicationResult" tabindex="-1" role="dialog" aria-labelledby="applicationResultLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="applicationResultLabel">申请成功</h4>
            </div>
            <div class="modal-body" id="application-result-body"></div>
            <div class="modal-footer">
                <div class="login-btn-group">
                    <button type="button" class="btn btn-primary" onclick="resultConfirm()">确认</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../venueLogin.jsp" %>
</body>
</html>
