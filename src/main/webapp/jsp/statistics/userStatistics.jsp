<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/6/18
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员分析</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/statistics/userStaistics.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/echarts.min.js"></script>
    <script src="../../js/tool/echarts-liquidfill.js"></script>
    <script src="../../js/statistics/userStatistics.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/statistics/user/req_memberConsumptionRange",
                success: function (data) {
                    $('#low-consumption-price').text(data['range'][1]);
                    $('#high-consumption-price').text(data['range'][0]);
                },
                error: function (result) {
                    console.log(result);
                }
            });
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/statistics/user/req_memberConsumptionByUnitTime",
                data: {
                    unit_time: '全部',
                    unit_time_year: '2018'
                },
                success: function (data) {
                    var myData = [];
                    for (var index in data['result']) {
                        var item = [];
                        item.push(data['result'][index]['tag']);
                        item.push(data['result'][index]['data']);
                        myData.push(item);
                    }
                    var chart = createConsumptionChart('user-consumption-show-chart', myData, '2018年月份花销统计');
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
                    <a href="javascript:void(0)" id="my-consumption-a" onclick="showConsumptionStatistics()">我的花销分析</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="my-favorite-a" onclick="showFavorite()" style="color: #1b6d85">喜爱节目统计</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="my-program-a" onclick="showProgram()" style="color: #1b6d85">我的节目收益分析</a>
                </li>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default user-statistics-info-panel">
            <div class="my-consumption-statistics" id="my-consumption-statistics">
                <div class="consumption-tile">
                    <p class="consumption-title-p">花销统计</p>
                </div>
                <div class="consumption-range">
                    <div class="low-consumption-price col-md-offset-1 col-md-5">
                        单笔消费的最低：<b id="low-consumption-price"></b>元
                    </div>
                    <div class="high-consumption-price col-md-5 col-md-offset-1">
                        单笔消费的最高：<b id="high-consumption-price"></b>元
                    </div>
                </div>
                <div class="chart-type col-md-offset-8 col-md-4">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYear(this)">2017</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYear(this)">2016</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYear(this)">2015</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYear(this)">全部</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="show-unit-time"
                                data-toggle="dropdown">
                            全部<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTime(this)">月份</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTime(this)">季度</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTime(this)">全部</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group" style="border-left: 1px solid rgba(51, 51, 51, 0.3);height: 30px;"></div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-sm btn-default" id="all-type-btn"
                                onclick="showChartByAll()">按年全部
                        </button>
                    </div>
                </div>
                <div class="user-consumption-show-chart col-md-offset-1 col-md-10 col-md-offset-1"
                     id="user-consumption-show-chart"></div>
            </div>
            <div class="my-favorite-statistics" id="my-favorite-statistics" style="min-width: 95%" hidden>
                <div class="favorite-program-type-tile">
                    <p class="favorite-program-type-title-p">喜爱节目类型统计</p>
                </div>
                <div class="program-type-show" id="program-type-show">
                    <div class="program-type-chart col-md-12" id="program-type-chart"></div>
                </div>
                <div class="favorite-area-tile">
                    <p class="favorite-area-title-p">常订区域统计</p>
                </div>
                <div class="area-show" id="area-show">
                    <div class="area-show-chart col-md-12" id="area-chart"></div>
                </div>
            </div>
            <div class="my-program-statistics" id="my-program-statistics" style="min-width: 95%" hidden>
                <div class="program-income-tile" style="border-bottom: 1px solid rgba(51, 51, 51, 0.3);">
                    <p class="program-income-title-p">节目收入统计</p>
                </div>
                <div class="chart-type col-md-offset-8 col-md-4">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="program-show-year"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYearForIncome(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYearForIncome(this)">2017</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYearForIncome(this)">2016</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYearForIncome(this)">2015</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByYearForIncome(this)">全部</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="program-show-unit-time"
                                data-toggle="dropdown">
                            全部<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTimeForIncome(this)">月份</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTimeForIncome(this)">季度</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showChartByUnitTimeForIncome(this)">全部</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group" style="border-left: 1px solid rgba(51, 51, 51, 0.3);height: 30px;"></div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-sm btn-default" id="program-all-type-btn"
                                onclick="showChartByAllForIncome()">按年全部
                        </button>
                    </div>
                </div>
                <div class="program-income-chart col-md-offset-1 col-md-10 col-md-offset-1" id="program-income-chart"
                     style=" margin-top: 15px;height: 400px;"></div>
            </div>
        </div>
        <div class="return-my-info">
            <a href="<c:url value="/userInfoManage"/>">&lt;&lt;返回我的信息</a>
        </div>
    </div>
</div>
</body>
</html>
