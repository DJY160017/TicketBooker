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
    <title>场馆租用</title>

    <link href="../../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../../css/reset.css" rel="stylesheet">
    <link href="../../../css/venue/header.css" rel="stylesheet">
    <link href="../../../css/venue/venueAdd.css" rel="stylesheet">
    <script src="../../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../../js/tool/bootstrap.js"></script>
    <script src="../../../js/venue/venueLogIn.js"></script>
    <script src="../../../js/venue/venueAdd.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/venue/req_venueSearchInit",
                success: function (data) {
                    var size = parseInt(data['venues_size'].toString());
                    if (size !== 0) {
                        $('#venue-table-content').show();
                        $('#miss-result-venue-id').hide();
                        $('#venue-book-table-body').empty();
                        var body = document.getElementById('venue-book-table-body');
                        for (var i = 0; i < size; i++) {
                            var tr = $('<tr onclick="showVenueDetail(this)"></tr>');
                            var name = $('<td class="name"></td>');
                            var address = $('<td class="address"></td>');
                            var price = $('<td class="price"></td>');
                            $(name).html(data['venues'][i]['name']);
                            $(address).html(data['venues'][i]['address']);
                            $(price).html(data['venues'][i]['price'] + '元/天');
                            name.appendTo(tr);
                            price.appendTo(tr);
                            address.appendTo(tr);
                            tr.appendTo(body);
                        }
                        var max_page = parseInt(data['maxPage']);
                        var page = 1;
                        var pagination = document.getElementById('venue-table-pagination-id');
                        $('#venue-table-pagination-id').empty();
                        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                        prev.appendTo(pagination);
                        for (var j = 0; j < 10; j++) {
                            var page_num = page + j;
                            var l = $('<li></li>');
                            var a = $('<a href="javascript:void(0)" onclick="getVenueByPage(this)">' + page_num + '</a>');
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
                            if (j === 9) {
                                $(a).attr('id', 'last');
                            }
                            a.appendTo(l);
                            l.appendTo(pagination);
                        }
                        next.appendTo(pagination);
                    } else {
                        $('#venue-table-content').hide();
                        $('#miss-result-venue-id').show();
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
        <label class="col-md-1 control-label" for="venue_key">关键字：</label>
        <div class="form-group input-group input-group-sm col-md-3" style="padding-left: 15px;">
            <input class="form-group form-control" type="text" id="venue_key" name="venue_key"
                   placeholder="请输入关键字进行查找" aria-describedby="sizing-addon1" required/>
            <span class="input-group-btn">
                        <button class="btn btn-default" id="key_search" type="button"
                                onclick="searchByKey()">搜索</button>
                </span>
        </div>
    </div>
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
</div>
<div class="container venue-table-result" style="margin-top: 40px;width: 80%" id="venue-table-content">
    <table class="table table-hover panel panel-default venue-book-table"
           style="vertical-align: middle;text-align: center;">
        <tbody id="venue-book-table-body"></tbody>
    </table>
    <nav class="venue-table-pagination" style="text-align: center">
        <ul class="pagination" id="venue-table-pagination-id"></ul>
    </nav>
</div>
<div class="container miss-result-venue" id="miss-result-venue-id" style="margin-top: 40px;" hidden>
    <p class="text-center">喔喔！ 未找到符合要求的场馆</p>
</div>
<div class="modal fade" id="venue-detail" tabindex="-1" role="dialog" aria-labelledby="detailLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="detailLabel">场馆详情</h4>
            </div>
            <div class="modal-body text-center" id="venue-detail-body"></div>
            <div class="modal-footer">
                <div class="program-btn-group">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="bookVenue()">预订</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../venueLogin.jsp" %>
</body>
</html>
