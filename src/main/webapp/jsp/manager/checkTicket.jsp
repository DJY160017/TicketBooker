<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/3/8
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>检票登记</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/tool/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/manager/checkTicket.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/manager/checkTicket.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".form_datetime").datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN',//中文，需要引用zh-CN.js包
                minView: 0,//日期时间选择器所能够提供的最精确的时间选择视图0
                startView: 2,//日视图
                pickerPosition: "bottom-right"
            });
        });
    </script>
</head>
<body>
<header>
    <%@ include file="../manager/managerHeader.jsp" %>
</header>
<div class="check-ticket panel panel-default" style="height: 85%;">
    <div class="check-ticket-info">
        <form id="checkTicketForm" class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-md-3 control-label" for="program_start_date" style="padding-left: 0">开始时间：</label>
                <div class="col-md-4">
                    <div class="input-group input-group-sm date form_datetime" data-date=""
                         data-date-format="yyyy/mm/dd hh:ii"
                         data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                        <input id="program_start_date" class="form-control" size="8" type="text"
                               title="program_start_date"/>
                        <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="venue_id" style="padding-left: 0">场馆ID：</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="venue_id" name="venue_id" placeholder="请输入场馆的ID"
                           required>
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
            <strong class="col-md-offset-4" style="color:red;" id="errorMessageField"></strong>
        </form>
    </div>
    <div class="check-ticket-button">
        <a href="javascript:void(0)" class="btn btn-success check-ticket-button-a" onclick="ticketCheck()">检票</a>
    </div>
</div>
<div class="modal fade" id="checkResult" tabindex="-1" role="dialog" aria-labelledby="checkResultLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="checkResultLabel">检票结果</h4>
            </div>
            <div class="modal-body text-center" id="check-result-body">
                <h3>检票成功</h3>
            </div>
            <div class="modal-footer">
                <div class="check-ticket-btn-group">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
