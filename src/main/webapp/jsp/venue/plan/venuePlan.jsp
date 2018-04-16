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
    <title>场馆计划</title>

    <link href="../../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../../css/tool/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../../css/reset.css" rel="stylesheet">
    <link href="../../../css/venue/header.css" rel="stylesheet">
    <link href="../../../css/venue/venuePlan.css" rel="stylesheet">
    <script src="../../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../../js/tool/bootstrap.js"></script>
    <script src="../../../js/tool/bootstrap-datetimepicker.js"></script>
    <script src="../../../js/tool/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../../../js/venue/venueLogIn.js"></script>
    <script src="../../../js/venue/venuePlan.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".form_date").datetimepicker({
                format: "yyyy-mm-dd",
                autoclose: true,
                todayBtn: true,
                language: 'zh-CN',//中文，需要引用zh-CN.js包
                startView: 2,//日视图
                minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图0
                pickerPosition: "bottom-left",
                startDate: new Date(2012 - 1 - 1)
            });
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/program/req_getSearchPlanInit",
                success: function (data) {
                    console.log(data);
                    var size = parseInt(data['program_size'].toString());
                    if (size !== 0) {
                        $('#plan-table-content').show();
                        $('#miss-result-plan-id').hide();
                        $('#plan-table-body').empty();
                        $('#plan_start_date').val(data['startTime']);
                        $('#plan_end_date').val(data['endTime']);
                        var body = document.getElementById('plan-table-body');
                        for (var i = 0; i < size; i++) {
                            var tr = $('<tr></tr>');
                            var start = $('<td></td>');
                            var end = $('<td></td>');
                            var programName = $('<td></td>');
                            var venueName = $('<td></td>');
                            var address = $('<td></td>');
                            $(start).html(data['programs'][i]['start_time']);
                            $(end).html(data['programs'][i]['end_time']);
                            $(programName).html(data['programs'][i]['name']);
                            $(venueName).html(data['programs'][i]['venueName']);
                            $(address).html(data['programs'][i]['venueAddress']);
                            start.appendTo(tr);
                            end.appendTo(tr);
                            programName.appendTo(tr);
                            venueName.appendTo(tr);
                            address.appendTo(tr);
                            tr.appendTo(body);
                        }
                        var max_page = parseInt(data['maxPage']);
                        var page = 1;
                        var pagination = document.getElementById('plan-table-pagination-id');
                        $('#plan-table-pagination-id').empty();
                        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                        prev.appendTo(pagination);
                        for (var j = 0; j < 5; j++) {
                            var page_num = page + j;
                            var l = $('<li></li>');
                            var a = $('<a href="javascript:void(0)" onclick="searchByPage(this)">' + page_num + '</a>');
                            if (page_num === max_page) {
                                $(a).attr('id', 'last');
                                a.appendTo(l);
                                l.appendTo(pagination);
                                if (max_page === 1) {
                                    $(l).addClass('active');
                                }
                                break;
                            }
                            if (j === 0) {
                                $(l).addClass('active');
                                $(a).attr('id', 'first');
                            }
                            if (j === 4) {
                                $(a).attr('id', 'last');
                            }
                            a.appendTo(l);
                            l.appendTo(pagination);
                        }
                        next.appendTo(pagination);
                    } else {
                        $('#plan-table-content').hide();
                        $('#miss-result-plan-id').show();
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
    <%@ include file="../../venue/header.jsp" %>
</header>
<div class="container search">
    <div class="search-key">
        <label class="col-md-1 control-label" for="plan_key">关键字：</label>
        <div class="form-group input-group input-group-sm col-md-3" style="padding-left: 15px;">
            <input class="form-group form-control" type="text" id="plan_key" name="plan_key"
                   placeholder="请输入关键字进行查找" aria-describedby="sizing-addon1" required/>
            <span class="input-group-btn">
                        <button class="btn btn-default" id="key_search" type="button" onclick="searchPlan()">搜索</button>
                </span>
        </div>
    </div>
    <div class="search-time">
        <label class="col-md-1 control-label">时间：</label>
        <div class="form-group col-md-3">
            <div class="input-group input-group-sm date form_date" data-date="" data-date-format="yyyy/mm/dd"
                 data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                <input id="plan_start_date" class="form-control" size="8" type="text" title="startDate"/>
                <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>
        <div class="form-group col-md-3">
            <div class="input-group input-group-sm date form_date" data-date="" data-date-format="yyyy/mm/dd"
                 data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                <input id="plan_end_date" class="form-control" size="8" type="text" title="endDate"/>
                <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>
        <div class="col-md-1">
            <a href="javascript:void(0)" class="btn btn-primary btn-sm" onclick="searchPlan()">查找</a>
        </div>
    </div>
</div>
<div class="container plan-table-result" id="plan-table-content" style="margin-top: 40px;width: 80%">
    <table class="table table-hover panel panel-default plan-table"
           style="vertical-align: middle;text-align: center;">
        <tbody id="plan-table-body"></tbody>
    </table>
    <nav class="plan-table-pagination" style="text-align: center">
        <ul class="pagination" id="plan-table-pagination-id"></ul>
    </nav>
</div>
<div class="miss-result-plan" id="miss-result-plan-id" style="margin-top: 40px;" hidden>
    <p class="text-center">喔喔！ 未找到符合要求的计划</p>
</div>
<%@ include file="../venueLogin.jsp" %>
</body>
</html>
