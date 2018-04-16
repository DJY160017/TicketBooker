<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/3
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>节目单</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/tool/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/ticket/program.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.js"></script>
    <script src="../../js/tool/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/ticket/program.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var programType = getRequestParam('program');
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/program/req_programInitInfo",
                data: {
                    programType: programType
                },
                success: function (data) {
                    var size = data['programs_size'];
                    var time = data['time'];
                    $('#start_date').val(time);
                    $('#end_date').val(time);
                    if (size !== 0) {
                        $('#program-content').show();
                        $('#miss-program-result').hide();
                        $('#program-card-one').empty();
                        $('#program-card-two').empty();
                        for (var i = 0; i < 8; i++) {
                            if (i >= size) {
                                break;
                            }
                            var li;
                            if (i < 4) {
                                li = document.getElementById("program-card-one");
                            } else {
                                li = document.getElementById("program-card-two");
                            }
                            var id = 'program_' + i.toString();
                            var div_1 = $('<div class="panel panel-info col-md-2 col-sm-offset-1" style="min-height:200px;" onmouseover="enlarge(this.id)" onmouseout="recover(this.id)" onclick="showChoose(this)"></div>');
                            $(div_1).attr('id', id);
                            var div_2 = $('<div class="panel-body"></div>');
                            var address = $('<p class="address"></p>');
                            var name = $('<p class="name"></p>');
                            var reserve_time = $('<p class="reserve_time"></p>');
                            $(name).html(data['programs'][i]['name']);
                            $(reserve_time).html('时间：' + data['programs'][i]['programID']['reserve_time']);
                            $(address).html("地址：" + data['programs'][i]['venueAddress']);
                            name.appendTo(div_2);
                            reserve_time.appendTo(div_2);
                            address.appendTo(div_2);
                            div_2.appendTo(div_1);
                            div_1.appendTo(li);
                        }
                        var max_page = parseInt(data['maxPage']);
                        var page = 1;
                        var pagination = document.getElementById('card-pagination-id');
                        $('#card-pagination-id').empty();
                        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                        prev.appendTo(pagination);
                        for (var j = 0; j < 5; j++) {
                            var page_num = page + j;
                            var l = $('<li></li>');
                            var a = $('<a href="javascript:void(0)" onclick="getProgramByPage(this)">' + page_num + '</a>');
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
                        $('#program-content').hide();
                        $('#miss-program-result').show();
                    }
                },
                error: function (result) {
                    console.log(result);
                }
            });
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
        });
    </script>
</head>
<body>
<header>
    <%@ include file="../header.jsp" %>
</header>
<div class="container search">
    <div class="search-city">
        <label class="col-md-1 control-label">城市：</label>
        <div class="col-md-11" id="search-city-content">
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)" id="province-active"
               style="background: rgb(51,122,183); color: rgb(255, 255, 255)">全部</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">深圳</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">北京</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">吉林</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">江苏</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">辽宁</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">广东</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">浙江</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">湖南</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">河北</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">新疆</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">山东</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">河南</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">山西</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">江西</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">安徽</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">湖北</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">内蒙</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">海南</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">四川</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">重庆</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">陕西</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">广西</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">福建</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">天津</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">云南</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">贵州</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">甘肃</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">黑龙江</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">宁夏</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">青海</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">上海</a>
            <a href="javascript:void(0)" class="province" onclick="searchByCity(this)">西藏</a>
        </div>
    </div>
    <div class="search-time">
        <label class="col-md-1 control-label">时间：</label>
        <div class="form-group col-md-3">
            <div class="input-group date form_date" data-date="" data-date-format="yyyy/mm/dd"
                 data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                <input id="start_date" class="form-control" size="8" type="text" title="startDate"/>
                <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>
        <div class="form-group col-md-3">
            <div class="input-group date form_date" data-date="" data-date-format="yyyy/mm/dd"
                 data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                <input id="end_date" class="form-control" size="8" type="text" title="endDate"/>
                <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>
        <div class="col-md-1">
            <a href="javascript:void(0)" class="btn btn-primary btn-sm" onclick="searchByTime()">查找</a>
        </div>
    </div>
</div>
<div class="content" id="program-content">
    <nav class="card-pagination">
        <ul class="pagination" id="card-pagination-id"></ul>
    </nav>
    <ul class="card-program">
        <li id="program-card-one"></li>
        <li id="program-card-two"></li>
    </ul>
</div>
<div class="miss-program-result-card" id="miss-program-result" style="margin-top: 100px;margin-left: 520px;" hidden>
    <p>喔喔！未找到符合条件的信息</p>
</div>
<div class="modal fade" id="program-detail" tabindex="-1" role="dialog" aria-labelledby="detailLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="detailLabel">节目详情</h4>
            </div>
            <div class="modal-body text-center" id="program-detail-body"></div>
            <div class="modal-footer">
                <div class="program-btn-group">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="chooseTicket()">购买</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
