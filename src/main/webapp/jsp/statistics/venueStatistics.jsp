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
    <title>场馆分析</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/statistics/venueStatistics.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/echarts.min.js"></script>
    <script src="../../js/tool/echarts-liquidfill.js"></script>
    <script src="../../js/statistics/venueStatistics.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/statistics/venue/req_marketCompare",
                success: function (data) {
                    var body = $('#market-compare-table-body');
                    $(body).empty();
                    for (var name in data) {
                        var tr_average = $('<tr></tr>');
                        var name_td = $('<td></td>');
                        $(name_td).html(name);
                        var venue = $('<td></td>');
                        $(venue).html(data[name]['场馆']);
                        var market = $('<td></td>');
                        $(market).html(data[name]['细分市场']);
                        var index = $('<td></td>');
                        $(index).html(data[name]['指数']);
                        $(tr_average).append(name_td);
                        $(tr_average).append(venue);
                        $(tr_average).append(market);
                        $(tr_average).append(index);
                        $(body).append(tr_average);
                    }
                    createMarketChart('market-compare-chart', data);
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
                    <a href="javascript:void(0)" id="market-compare-a" onclick="showMarketCompare()">市场比较</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="venue-price-a" onclick="showVenuePrice()" style="color: #1b6d85">场馆定价</a>
                </li>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default venue-statistics-info-panel">
            <div class="market-compare-statistics" id="market-compare-statistics">
                <div class="market-compare-tile">
                    <p class="market-compare-title-p">市场比较分析</p>
                </div>
                <div class="market-compare-table col-md-offset-1 col-md-10 col-md-offset-1" id="market-compare-table">
                    <table class="table table-hover table-responsive">
                        <caption>市场比较指数</caption>
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>场馆</th>
                            <th>细分市场</th>
                            <th>指数</th>
                        </tr>
                        </thead>
                        <tbody id="market-compare-table-body"></tbody>
                    </table>
                </div>
                <div class="market-compare-chart col-md-offset-1 col-md-10 col-md-offset-1"
                     id="market-compare-chart" style="height: 400px;"></div>
            </div>
            <div class="venue-price-statistics" id="venue-price-statistics" style="min-width: 95%" hidden>
                <div class="venue-price-tile">
                    <p class="venue-price-title-p">场馆定价帮助</p>
                </div>
                <div class="average-price col-md-offset-1 col-md-10 col-md-offset-1" id="average-price-table">
                    <table class="table table-hover table-responsive">
                        <caption>价格比较</caption>
                        <thead>
                        <tr>
                            <th>相同地域平均价格</th>
                            <th>同等规模平均价格</th>
                            <th>维度组合平均价格</th>
                        </tr>
                        </thead>
                        <tbody id="average-price-table-body"></tbody>
                    </table>
                </div>
                <div class="book-index-condition col-md-offset-10 col-md-2">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="show-unit-time"
                                data-toggle="dropdown">
                            月份<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showByUnitTime(this)">月份</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showByUnitTime(this)">季度</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showByUnitTime(this)">年份</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="book-index-chart-show  col-md-offset-1 col-md-10 col-md-offset-1"
                     id="book-index-chart-show">
                    <div class="book-index-chart" id="book-index-chart" style="height: 400px;"></div>
                </div>
                <div class="top-book-time-condition col-md-offset-10 col-md-2">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="show-year-time"
                                data-toggle="dropdown">
                            2018<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="javascript:void(0)" onclick="showByYear(this)">2018</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showByYear(this)">2017</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showByYear(this)">2016</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="showByYear(this)">2015</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="top-book-time-range-chart-show col-md-offset-1 col-md-10 col-md-offset-1"
                     id="top-book-time-range-chart-show">
                    <div class="top-book-time-range-chart" id="top-book-time-range-chart" style="height: 400px;"></div>
                </div>
            </div>
        </div>
        <div class="return-venue-info">
            <a href="<c:url value="/venue/venueInfoManage"/>">&lt;&lt;返回场馆信息</a>
        </div>
    </div>
</div>
</body>
</html>
