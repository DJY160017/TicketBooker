<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/26
  Time: 21:42
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>场馆预订</title>

    <link href="../../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../../css/tool/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../../css/reset.css" rel="stylesheet">
    <link href="../../../css/venue/header.css" rel="stylesheet">
    <link href="../../../css/venue/venueBook.css" rel="stylesheet">
    <script src="../../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../../js/tool/bootstrap.js"></script>
    <script src="../../../js/tool/bootstrap-datetimepicker.js"></script>
    <script src="../../../js/tool/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../../../js/venue/venueLogIn.js"></script>
    <script src="../../../js/venue/venueBook.js"></script>
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
            $(".form_datetime").datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN',//中文，需要引用zh-CN.js包
                minView: 0,//日期时间选择器所能够提供的最精确的时间选择视图0
                startView: 2,//日视图
                pickerPosition: "top-left",
                startDate: new Date(2012 - 1 - 1)
            });
            var address = getRequestParam("address");
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/program/req_getVenuePlanInit",
                data: {
                    address: address
                },
                success: function (data) {
                    var size = parseInt(data['program_size'].toString());
                    if (size !== 0) {
                        $('#venue-book-table-content').show();
                        $('#miss-result-venue-plan-id').hide();
                        $('#venue-booked-table-body').empty();
                        var body = document.getElementById('venue-booked-table-body');
                        for (var i = 0; i < size; i++) {
                            var tr = $('<tr></tr>');
                            var start = $('<td></td>');
                            var end = $('<td></td>');
                            var state = $('<td>已预订</td>');
                            $(start).html(data['programs'][i]['start_time']);
                            $(end).html(data['programs'][i]['end_time']);
                            start.appendTo(tr);
                            end.appendTo(tr);
                            state.appendTo(tr);
                            tr.appendTo(body);
                        }
                        var max_page = parseInt(data['maxPage']);
                        var page = 1;
                        var pagination = document.getElementById('venue-book-table-pagination-id');
                        $('#venue-book-table-pagination-id').empty();
                        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                        prev.appendTo(pagination);
                        for (var j = 0; j < 5; j++) {
                            var page_num = page + j;
                            var l = $('<li></li>');
                            var a = $('<a href="javascript:void(0)" onclick="getPlanByPage(this)">' + page_num + '</a>');
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
                        $('#venue-book-table-content').hide();
                        $('#miss-result-venue-plan-id').show();
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
<div class="container venue-book-table-title">
    <p class="venue-book-table-p">场馆已预定计划</p>
</div>
<div class="container venue-book-table" id="venue-book-table-content" style="margin-top: 30px;width: 70%">
    <table class="table table-hover panel panel-default venue-book-table"
           style="vertical-align: middle;text-align: center;">
        <tbody id="venue-booked-table-body"></tbody>
    </table>
    <nav class="venue-table-pagination" style="text-align: center">
        <ul class="pagination" id="venue-book-table-pagination-id"></ul>
    </nav>
</div>
<div class="miss-result-venue-plan" id="miss-result-venue-plan-id" hidden>
    <p class="text-center">喔喔！ 未找到符合要求的计划</p>
</div>
<div class="container venue-book-title">
    <p class="venue-book-p">租用场馆 <span class="glyphicon glyphicon-info-sign" style="font-size: 15px"
                                       onclick="showRule()"></span></p>
</div>
<div class="container book panel panel-default">
    <div class="program-info">
        <form id="programForm" class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-md-3 control-label" style="padding-left: 0">场馆预定时间：</label>
                <div class="col-md-3">
                    <div class="input-group input-group-sm date form_date" data-date="" data-date-format="yyyy/mm/dd"
                         data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                        <input id="start_date" class="form-control" size="8" type="text" title="startDate"/>
                        <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group input-group-sm date form_date" data-date="" data-date-format="yyyy/mm/dd"
                         data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                        <input id="end_date" class="form-control" size="8" type="text" title="endDate"/>
                        <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="program_start_date" style="padding-left: 0">节目开始时间：</label>
                <div class="col-md-3">
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
                <label class="col-md-3 control-label" for="reg_program_name" style="padding-left: 0">节目名称：</label>
                <div class="col-md-3">
                    <input type="text" class="form-control" id="reg_program_name" name="reg_venue_name"
                           placeholder="请输入节目名称" required>
                </div>
                <div class="col-md-3">
                    <div class="dropdown">
                        <button type="button" class="btn btn-default dropdown-toggle" id="reg_program_type"
                                data-toggle="dropdown">
                            @节目类型
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                            <li role="presentation">
                                <a role="menuitem" tabindex="-1" href="javascript:void(0)"
                                   onclick="chooseProgramType(this)">音乐会</a>
                            </li>
                            <li role="presentation">
                                <a role="menuitem" tabindex="-1" href="javascript:void(0)"
                                   onclick="chooseProgramType(this)">话剧</a>
                            </li>
                            <li role="presentation">
                                <a role="menuitem" tabindex="-1" href="javascript:void(0)"
                                   onclick="chooseProgramType(this)">体育比赛</a>
                            </li>
                            <li role="presentation">
                                <a role="menuitem" tabindex="-1" href="javascript:void(0)"
                                   onclick="chooseProgramType(this)">动漫</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="ticket_low_price" style="padding-left: 0">票的底价：</label>
                <div class="col-md-3">
                    <input type="text" class="form-control" id="ticket_low_price" name="ticket_low_price"
                           placeholder="请输入票的最低价" required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="program_introduction"
                       style="padding-left: 0;">票价规则：</label>
                <div class="col-md-3">
                    <textarea class="form-control" rows="3" cols="7" id="price_raw_rule"
                              name="price_raw_rule" maxlength="60"
                              style="resize: none" placeholder="请输入行方向票价规则，如：1-2：20元；3-4：10元；" required></textarea>
                </div>
                <div class="col-md-3">
                    <textarea class="form-control" rows="3" cols="7" id="price_col_rule"
                              name="price_col_rule" maxlength="60"
                              style="resize: none" placeholder="请输入列方向票价规则，如：1-2：10元；3-4：20元；" required></textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="program_introduction"
                       style="padding-left: 0;">节目介绍：</label>
                <div class="col-md-5">
                    <textarea class="form-control" rows="3" cols="7" id="program_introduction"
                              name="program_introduction" maxlength="60"
                              style="resize: none" placeholder="请输入相关节目介绍" required></textarea>
                </div>
            </div>
            <strong class="col-md-offset-4" style="color:red;" id="errorMessageField"></strong>
        </form>
        <div class="book-button">
            <a href="javascript:void(0)" class="col-md-offset-4 col-md-3 btn btn-success" onclick="venueBook()">租用</a>
        </div>
    </div>
    <div class="useless" style="margin-top: 90px"></div>
    <div class="seat-price-statistics">
        <a href="javascript:void(0)" onclick="seatPriceDecideHelp()">座位定价帮助&gt;&gt;</a>
    </div>
</div>
<div class="modal fade" id="rule-explain" tabindex="-1" role="dialog" aria-labelledby="ruleLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="ruleLabel">票价规则</h4>
            </div>
            <div class="modal-body text-left">
                <h5>票的底价是该节目票的最低价，是每张票根据位置加价的底价</h5>
                <h5>行方向和列方向加价规则是有节目承办商自定义场馆多个区域的票价,例子如下：</h5>
                <h6>票的低价：100元</h6>
                <h6>行方向加价规则：1-2：20元；3-4：10元；</h6>
                <h6>列方向加价规则：1-3：10元；4-4：20元；</h6>
                <h6>1排1座价格为：130元</h6>
                <h6>3排1座价格为：120元</h6>
                <h6>1排4座价格为：140元</h6>
            </div>
            <div class="modal-footer">
                <div>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="seat-decide-tip" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body text-left">
                <h5>请选择节目类型</h5>
            </div>
            <div class="modal-footer">
                <div>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../venueLogin.jsp" %>
</body>
</html>
