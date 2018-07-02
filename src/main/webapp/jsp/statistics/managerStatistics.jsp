<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/6/18
  Time: 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>平台分析</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/statistics/managerStatistics.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=Yf4rwtQrpwSrBWZkmjDqVSE2Quahb3IA"></script>
    <script src="../../js/tool/echarts.min.js"></script>
    <script src="../../js/tool/echarts-liquidfill.js"></script>
    <script src="../../js/tool/bmap.js"></script>
    <script src="../../js/statistics/managerStatistics.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/statistics/manager/req_systemVenueProfit",
                data: {
                    year: 2018
                },
                success: function (data) {
                    createProfitChart('profit-venue-show-chart', data['month_profit'], data['quarter_profit'], '2018', '场馆');
                },
                error: function (result) {
                    console.log(result);
                }
            });
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/statistics/manager/req_systemProgramProfit",
                data: {
                    year: 2018
                },
                success: function (data) {
                    createProfitChart('profit-program-show-chart', data['month_profit'], data['quarter_profit'], '2018', '节目');
                },
                error: function (result) {
                    console.log(result);
                }
            });
        });
    </script>
</head>
<body>
<div class="container" style="margin-top: 25px">
    <div class="row">
        <div class="col-xs-3 panel panel-default">
            <ul class="nav nav-pills nav-stacked">
                <li role="presentation" class="active">
                    <a href="javascript:void(0)" id="profit-a" onclick="showProfit()">盈利情况分析</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="management-a" onclick="showManagement()" style="color: #1b6d85">经营情况分析</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="venue-a" onclick="showVenue()" style="color: #1b6d85">场馆情况分析</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="member-a" onclick="showMember()" style="color: #1b6d85">会员情况分析</a>
                </li>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default system-statistics-info-panel">
            <div class="profit-statistics" id="profit-statistics">
                <div class="profit-tile">
                    <p class="profit-title-p">盈利分析</p>
                </div>
                <div class="profit-venue-show-chart-type col-md-offset-10 col-md-2">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="profit-venue-show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showProfitVenueShowByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProfitVenueShowByYear(this)">2017</a>
                            </li>
                            <%--<li>--%>
                                <%--<a href="javascript:void(0)" onclick="showProfitVenueShowByYear(this)">2016</a>--%>
                            <%--</li>--%>
                        </ul>
                    </div>
                </div>
                <div class="profit-venue-show col-md-offset-1 col-md-10 col-md-offset-1" id="profit-venue-show">
                    <div class="profit-venue-show-chart" id="profit-venue-show-chart"></div>
                </div>
                <div class="profit-program-show-chart-type col-md-offset-10 col-md-2">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm"
                                id="profit-program-show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showProfitProgramShowByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProfitProgramShowByYear(this)">2017</a>
                            </li>
                            <%--<li>--%>
                                <%--<a href="javascript:void(0)" onclick="showProfitProgramShowByYear(this)">2016</a>--%>
                            <%--</li>--%>
                        </ul>
                    </div>
                </div>
                <div class="profit-program-show col-md-offset-1 col-md-10 col-md-offset-1" id="profit-program-show">
                    <div class="profit-program-show-chart" id="profit-program-show-chart"></div>
                </div>
            </div>
            <div class="management-statistics" id="management-statistics" style="min-width: 95%" hidden>
                <div class="management-tile">
                    <p class="management-title-p">平台经营分析</p>
                </div>
                <div class="turnover col-md-offset-1 col-md-10 col-md-offset-1" id="turnover-table"
                     style="margin-top: 20px;">
                    <table class="table table-hover table-responsive">
                        <caption>场馆平均周转天数</caption>
                        <thead id="turnover-table-head">
                        <tr>
                            <th>2018年度</th>
                            <th>2017年度</th>
                            <th>2016年度</th>
                        </tr>
                        </thead>
                        <tbody id="turnover-table-body"></tbody>
                    </table>
                </div>
                <div class="venue-income-show-chart-type col-md-offset-9 col-md-3" style="margin-top: 25px;">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="venue-income-show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByYear(this)">2017</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByYear(this)">2016</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByYear(this)">2015</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm"
                                id="venue-income-show-unit-time"
                                data-toggle="dropdown">
                            月份<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByUnitTime(this)">月份</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showVenueIncomeShowByUnitTime(this)">季度</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="venue-income-show col-md-offset-1 col-md-10 col-md-offset-1" id="venue-income-show">
                    <div class="venue-income-show-chart" id="venue-income-show-chart" style="height: 400px;"></div>
                </div>
                <div class="program-income-show-chart-type col-md-offset-9 col-md-3" style="margin-top: 25px;">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm"
                                id="program-income-show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByYear(this)">2017</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByYear(this)">2016</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByYear(this)">2015</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm"
                                id="program-income-show-unit-time"
                                data-toggle="dropdown">
                            月份<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByUnitTime(this)">月份</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showProgramIncomeShowByUnitTime(this)">季度</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="program-income-show col-md-offset-1 col-md-10 col-md-offset-1" id="program-income-show">
                    <div class="program-income-show-chart" id="program-income-show-chart" style="height: 400px"></div>
                </div>
            </div>
            <div class="venue-statistics" id="venue-statistics" style="min-width: 95%" hidden>
                <div class="venue-tile" style="border-bottom: 1px solid rgba(51, 51, 51, 0.3);">
                    <p class="venue-title-p">场馆分析</p>
                </div>
                <div class="venue-now-statistics col-md-12">
                    <div class="venue-num" id="venue-num">
                        <h3 class="col-md-4" id="venue-pass"></h3>
                        <h3 class="col-md-4" id="venue-unApproved"></h3>
                        <h3 class="col-md-4" id="venue-notThrough"></h3>
                    </div>
                    <div class="venue-now-year" id="venue-now-year">
                        <div id="venue-year-chart" style="height: 400px"></div>
                    </div>
                    <div class="venue-profit" id="venue-profit" style="margin-top: 50px;margin-left: 280px;">
                        <h3 id="venue-total-price"></h3>
                    </div>
                </div>
                <div class="venue-area-show col-md-offset-1 col-md-10 col-md-offset-1" id="venue-area-show"
                     style="margin-top: 15px;">
                    <div class="venue-area-show-chart " id="venue-area-show-chart" style="height: 400px;"></div>
                </div>
                <div class="venue-size-show col-md-offset-1 col-md-10 col-md-offset-1" id="venue-size-show"
                     style="margin-top: 15px;">
                    <div class="venue-size-show-chart" id="venue-size-show-chart" style=" height: 400px;"></div>
                </div>
                <div class="venue-program-show col-md-offset-1 col-md-10 col-md-offset-1" id="venue-program-show"
                     style="margin-top: 15px;">
                    <div class="venue-program-show-chart" id="venue-program-show-chart" style=" height: 400px;"></div>
                </div>
            </div>
            <div class="member-statistics" id="member-statistics" style="min-width: 95%" hidden>
                <div class="member-tile" style="border-bottom: 1px solid rgba(51, 51, 51, 0.3);">
                    <p class="member-title-p">会员分析</p>
                </div>
                <div class="member-area-show  col-md-12" id="member-area-show" style=" margin-top: 15px;">
                    <div class="member-area-show-chart" id="member-area-show-chart" style="height: 90%"></div>
                </div>
            </div>
        </div>
        <div class="return-manage-info">
            <a href="<c:url value="/ticketBookerManager/home"/>">&lt;&lt;返回经理管理中心</a>
        </div>
    </div>
</div>
</body>
</html>
