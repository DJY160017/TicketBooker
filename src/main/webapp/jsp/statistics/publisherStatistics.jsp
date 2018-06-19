<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/6/18
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>座位定价分析</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/statistics/publisherStatistics.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/echarts.min.js"></script>
    <script src="../../js/tool/echarts-liquidfill.js"></script>
    <script src="../../js/statistics/publisherStatistics.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var venueKey = 1;
            var sizeKey = 0;
            var typeKey = 0;
            getSeatPriceData(venueKey, sizeKey, typeKey)
        });
    </script>
</head>
<body>
<div class="seat-price-decide panel panel-default">
    <div class="seat-price-decide-title text-center" id="seat-price-decide-title">
        <h3>座位定价分析</h3>
    </div>
    <div class="seat-price-decide-content" id="seat-price-decide-content">
        <div class="seat-price-decide-condition col-md-offset-9 col-md-3">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle btn-sm" id="select-condition"
                        data-toggle="dropdown">
                    场馆<span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li>
                        <a href="javascript:void(0)" onclick="conditionSelect(this, 1,0,0)">场馆</a>
                    </li>
                    <li>
                        <a href="javascript:void(0)" onclick="conditionSelect(this, 0,2,0)">场馆规模</a>
                    </li>
                    <li>
                        <a href="javascript:void(0)" onclick="conditionSelect(this, 1,0 ,3)">场馆和节目类型</a>
                    </li>
                    <li>
                        <a href="javascript:void(0)" onclick="conditionSelect(this, 0,2,3)">场馆规模和节目类型</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="seat-price-decide-show-chart col-md-offset-1 col-md-10 col-md-offset-1"
             id="seat-price-decide-chart"></div>
    </div>
    <div class="seat-price-decide-footer" id="seat-price-decide-footer">
        <div class="seat-price-decide-return-button">
            <button class="btn btn-success seat-price-decide-return" id="seat-price-decide-return"
                    onclick="returnVenueBook()">返回
            </button>
        </div>
    </div>
</div>
</body>
</html>
