<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/8
  Time: 20:21
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>TicketBookerManager</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/manager/manager.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/echarts.min.js"></script>
    <script src="../../js/tool/echarts-liquidfill.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/manager/manager.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/ticketBookerManager/req_getUnVerifyVenue",
                success: function (data) {
                    var size = parseInt(data['venues_size']);
                    if (size !== 0) {
                        $('#venue-application-verify-table').show();
                        $('#miss-result-verify-id').hide();
                        $("#venue-application-verify-table-body").empty();
                        var body = document.getElementById('venue-application-verify-table-body');
                        for (var i = 0; i < size; i++) {
                            var tr = $('<tr></tr>');
                            var venue_id = $('<td class="venue_id"></td>');
                            var venue_name = $('<td></td>');
                            var venue_price = $('<td></td>');
                            var venue_col = $('<td></td>');
                            var venue_raw = $('<td></td>');
                            var venue_address = $('<td></td>');
                            var venue_pass = $('<td><a href="javascript:void(0)" class="btn btn-success btn-sm" onclick="pass(this)">通过</a></td>');
                            var venue_refuse = $('<td><a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="refuse(this)">拒绝</a></td>');
                            $(venue_id).html(venueIDFormat(data['venues'][i]['venueID']));
                            $(venue_name).html(data['venues'][i]['name']);
                            $(venue_price).html(data['venues'][i]['price'] + '元/天');
                            $(venue_raw).html(data['venues'][i]['raw_num'] + '排');
                            $(venue_col).html(data['venues'][i]['col_num'] + '列');
                            $(venue_address).html(data['venues'][i]['address']);
                            venue_id.appendTo(tr);
                            venue_name.appendTo(tr);
                            venue_price.appendTo(tr);
                            venue_raw.appendTo(tr);
                            venue_col.appendTo(tr);
                            venue_address.appendTo(tr);
                            venue_refuse.appendTo(tr);
                            venue_pass.appendTo(tr);
                            tr.appendTo(body);
                        }
                    } else {
                        $('#venue-application-verify-table').hide();
                        $('#miss-result-verify-id').show();
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
    <%@ include file="../manager/managerHeader.jsp" %>
</header>
<div class="container" style="margin-top: 70px">
    <div class="row">
        <div class="col-xs-3 panel panel-default">
            <ul class="nav nav-pills nav-stacked">
                <li role="presentation" class="active">
                    <a href="javascript:void(0)" id="venue-application-verify-a"
                       onclick="showVenueApplication()">场馆申请审核</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="manager-settlement-a" onclick="showManagerSettlement()"
                       style="color: #1b6d85">结算</a>
                </li>
                <%--<li role="presentation">--%>
                    <%--<a href="javascript:void(0)" id="platform-data-center-a" onclick="showPlatformDataCenter()"--%>
                       <%--style="color: #1b6d85">平台数据中心</a>--%>
                <%--</li>--%>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default manager-panel">
            <div class="venue-application-verify" id="venue-application-verify">
                <div class="venue-application-verify-tile">
                    <p class="venue-application-verify-title-p">场馆申请审核</p>
                </div>
                <div class="venue-application-verify-content">
                    <div class="venue-application-verify-table" id="venue-application-verify-table">
                        <a href="javascript:void(0)" class="btn btn-success btn-lg"
                           style="margin-left: 87%;margin-top: 10px;" onclick="allPass()">全部通过</a>
                        <table class="table table-hover"
                               style="vertical-align: middle;text-align: center;margin-top: 5px;">
                            <tbody id="venue-application-verify-table-body"></tbody>
                        </table>
                    </div>
                    <div class="miss-result-verify" id="miss-result-verify-id" style="margin-top: 40px;" hidden>
                        <p class="text-center">喔喔！ 尚未有未审核的场馆</p>
                    </div>
                </div>
            </div>
            <div class="manager-settlement" id="manager-settlement" hidden>
                <div class="manager-settlement-tile">
                    <p class="manager-settlement-title-p">结算</p>
                </div>
                <div class="manager-settlement-content">
                    <div class="table-responsive manager-settlement-table" id="manager-settlement-table">
                        <a href="javascript:void(0)" class="btn btn-success btn-lg"
                           style="margin-left: 87%;margin-top: 10px;" onclick="allSettle()">一键结算</a>
                        <table class="table table-hover"
                               style="vertical-align: middle;text-align: left;margin-top: 5px;">
                            <thead>
                            <tr>
                                <th>记录时间</th>
                                <th>场馆账号</th>
                                <th>场馆金额</th>
                                <th>商家账号</th>
                                <th>商家金额</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="manager-settlement-table-body"></tbody>
                        </table>
                    </div>
                    <div class="miss-result-settlement" id="miss-result-settlement-id" style="margin-top: 40px;" hidden>
                        <p class="text-center">喔喔！ 尚未有未结算记录</p>
                    </div>
                </div>
            </div>
            <%--<div class="platform-data-center" id="platform-data-center" hidden>--%>
                <%--<ul id="platform-data-center-tab" class="nav nav-tabs">--%>
                    <%--<li class="active" onclick="userDataCenter()"><a href="#user" data-toggle="tab">用户</a></li>--%>
                    <%--<li onclick="venueDataCenter()"><a href="#venue" data-toggle="tab">场馆</a></li>--%>
                <%--</ul>--%>
                <%--<div id="platform-data-center-tab-content" class="tab-content">--%>
                    <%--<div class="tab-pane fade in active" id="user">--%>
                        <%--<div class="user-num" id="user-num" style="margin-top: 30px;">--%>
                            <%--<h3 id="users"></h3>--%>
                        <%--</div>--%>
                        <%--<div class="user-consume" id="user-consume" style="margin-top: 70px;margin-left: 30px">--%>
                            <%--<div id="consume-chart" style="width: 280px;height: 280px;"></div>--%>
                        <%--</div>--%>
                        <%--<div class="member-percent" id="member-percent"--%>
                             <%--style="position: absolute; top: 180px;left: 450px;">--%>
                            <%--<div id="member-chart" style="width: 350px;height: 300px;"></div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="tab-pane fade in" id="venue">--%>
                       <%----%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
        <div class="check-system-statistics">
            <a href="<c:url value="/statistics/manager/"/>">平台数据分析&gt;&gt;</a>
        </div>
    </div>
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
