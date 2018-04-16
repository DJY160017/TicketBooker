<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/3/8
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>现场缴费</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/tool/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="../../css/tool/jquery.seat-charts.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/ticket/seat.css" rel="stylesheet">
    <link href="../../css/manager/onSitePayment.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../../js/tool/jquery.seat-charts.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/manager/onSitePayment.js"></script>
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
<div class="container panel panel-default ticket-choose-panel">
    <div class="program-info-choose" id="program-info-choose">
        <div class="program-info-input col-md-6">
            <form id="checkTicketForm" class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="col-md-3 control-label" for="program_start_date" style="padding-left: 0">开始时间：</label>
                    <div class="col-md-5">
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
                    <label class="col-md-3 control-label" for="search_key" style="padding-left: 0">关键字：</label>
                    <div class="col-md-5">
                        <input type="text" class="form-control" id="search_key" name="search_key"
                               placeholder="请输入地址/节目名关键字"
                               required>
                    </div>
                </div>
                <strong class="col-md-offset-4" style="color:red;" id="errorMessageField"></strong>
            </form>
            <div class="search-now">
                <a href="javascript:void(0)" class="btn btn-success search-now-button" onclick="onSiteSearchProgram()">查找</a>
            </div>
        </div>
        <div class="program-show-content col-md-6">
            <div class="program-show-info-body" id="program-show-info-body">
                <table class="table table-hover table-striped" id="program-info-table" style="vertical-align: middle;text-align: center;">
                    <tbody id="program-info-table-body"></tbody>
                </table>
            </div>
            <div class="miss-result-program" id="miss-result-program-id" style="margin-top: 160px;" hidden>
                <p class="text-center">喔喔！ 未找到相关节目信息</p>
            </div>
        </div>
    </div>
    <div class="choose-seat" id="choose-seat" hidden>
    </div>
</div>
<div class="modal fade" id="payResult" tabindex="-1" role="dialog" aria-labelledby="payResultLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="payResultLabel">支付结果</h4>
            </div>
            <div class="modal-body text-center" id="pay-result-body"></div>
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
