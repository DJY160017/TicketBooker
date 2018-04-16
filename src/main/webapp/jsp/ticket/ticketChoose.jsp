<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/3
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>选票</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/tool/jquery.seat-charts.css" rel="stylesheet">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/ticket/seat.css" rel="stylesheet">
    <link href="../../css/ticket/ticketChoose.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/jquery.seat-charts.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/ticket/ticketChoose.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var order_now_counter = 0;
            var address = getRequestParam('address');
            var time = getRequestParam('time');
            var seat_map = [];
            $.ajax({
                type: "post",
                dataType: 'json',
                async: false,
                url: "/ticket/req_getChooseInfo",
                data: {
                    address: address,
                    time: time
                },
                success: function (data) {
                    seat_map = data['seatMap'];

                },
                error: function (result) {
                    console.log(result);
                }
            });
            var firstLabel = 1;
            var sc = $('#seat-map').seatCharts({
                map: seat_map,
                seats: {
                    a: {
                        price: 40,
                        classes: 'common-seat',
                        category: '普通'
                    }
                },
                naming: {
                    top: false,
                    getLabel: function (character, row, column) {
                        return firstLabel++;
                    }
                },
                click: function () {
                    if (this.status() === 'available') {
                        $('#order-preview').show();
                        if (order_now_counter === 6) {
                            return 'available';
                        }
                        order_now_counter = order_now_counter + 1;
                        var detail_price = $('#order-price-detail').text().split('：');
                        var total_price = parseFloat(detail_price[1].toString().replace('元', ''));
                        var id = this.settings.id.split('_');
                        var price = getSeatPrice(id[1], id[0]);
                        total_price = total_price + price;
                        $('#order-price-detail').html('您的订单总价：' + total_price + '元');
                        var location = calculateSeat(this.settings.id);
                        var seat_body_id = 'seat_body_' + this.settings.id;
                        var seat_location_id = 'seat_location_' + this.settings.id;
                        var seat_price_id = 'seat_price_' + this.settings.id;
                        var seat_cancel_id = 'seat_cancel_' + this.settings.id;
                        var seat_record = document.getElementById('order-table-body');
                        var seat_tr = $('<tr></tr>');
                        $(seat_tr).attr('id', seat_body_id).data('seatID', this.settings.id);
                        var seat_location = $('<td></td>');
                        $(seat_location).attr('id', seat_location_id);
                        $(seat_location).html(location);
                        var seat_price = $('<td></td>');
                        $(seat_price).attr('id', seat_price_id);
                        $(seat_price).html(price + '元');
                        var seat_cancel = $('<td><a href="javascript:void(0)" class="cancel-cart-item">取消</a></td>');
                        $(seat_cancel).attr('id', seat_cancel_id);
                        seat_location.appendTo(seat_tr);
                        seat_price.appendTo(seat_tr);
                        seat_cancel.appendTo(seat_tr);
                        seat_tr.appendTo(seat_record);
                        return 'selected';
                    } else if (this.status() === 'selected') {
                        if (order_now_counter !== 0) {
                            order_now_counter = order_now_counter - 1;
                            if (order_now_counter === 0) {
                                $('#order-preview').hide();
                            }
                        }
                        $('#seat_body_' + this.settings.id).remove();
                        var detail_price_1 = $('#order-price-detail').text().split('：');
                        var total_price_1 = parseFloat(detail_price_1[1].toString().replace('元', ''));
                        var id_1 = this.settings.id.split('_');
                        var price_1 = getSeatPrice(id_1[1], id_1[0]);
                        total_price_1 = total_price_1 - price_1;
                        $('#order-price-detail').html('您的订单总价：' + total_price_1 + '元');
                        return 'available';
                    } else if (this.status() === 'unavailable') {
                        return 'unavailable';
                    } else {
                        return this.style();
                    }
                }
            });
            sc.find('u.available').status('unavailable');
            $('#order-table-body').on('click', '.cancel-cart-item', function () {
                sc.get($(this).parents('tr:first').data('seatID')).click();
            });
        });
    </script>
</head>
<body>
<header>
    <%@ include file="../header.jsp" %>
</header>
<div class="container" style="margin-top: 70px">
    <div class="row">
        <div class="col-xs-3 panel panel-default">
            <ul class="nav nav-pills nav-stacked">
                <li role="presentation" class="active">
                    <a href="javascript:void(0)" id="choose-seat-a" onclick="showChooseSeat()">选座购买</a>
                </li>
                <li role="presentation">
                    <a href="javascript:void(0)" id="orderNow-a" onclick="showOrderNow()"
                       style="color: #1b6d85">立即购买</a>
                </li>
            </ul>
        </div>
        <div class="col-md-9 panel panel-default ticket-choose-panel">
            <div class="choose-seat" id="choose-seat">
                <div class="choose-seat-title">
                    <p class="choose-seat-title-p">选座购买(您最多选购6张)</p>
                </div>
                <div class="seat-map" id="seat-map"></div>
                <div class="order" id="order-preview" hidden>
                    <div class="order-price">
                        <h1 class="order-price-p" id="order-price-detail">您的订单总价：0元</h1>
                    </div>
                    <div class="your-order">
                        <table class="table" id="order-table" style="vertical-align: middle;text-align: center;">
                            <tbody id="order-table-body"></tbody>
                        </table>
                    </div>
                    <div class="checkout-pay">
                        <a href="javascript:void(0)" class="btn btn-lg btn-success" onclick="chooseCheckOut()">支付</a>
                    </div>
                </div>
            </div>
            <div class="orderNow" id="orderNow" hidden>
                <div class="orderNow-title">
                    <p class="orderNow-title-p">立即购买</p>
                </div>
                <div class="orderNow-input">
                    <form id="orderNowForm" class="form-horizontal" role="form">
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="ticket_num" style="padding-left: 0">票数：</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="ticket_num" name="ticket_num"
                                       placeholder="请再次输入您需要的票数" required>
                            </div>
                            <a href="javascript:void(0)" class="col-md-3 btn btn-success" id="orderNow-pay"
                               onclick="orderNowCheckOut()">立即支付</a>
                        </div>
                    </form>
                </div>
                <div class="rule-explain">
                    <span class="col-sm-offset-1 col-sm-1 glyphicon glyphicon-info-sign"></span>
                    <p class="col-sm-8">您将以该演出的票价的最低价进行支付，每单最多20张，演出前2周系统自动配票，如果配票不成功，全额退款</p>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="orderNow-preview-error" tabindex="-1" role="dialog" aria-labelledby="orderNowErrorLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="orderNowErrorLabel">购票提示</h4>
            </div>
            <div class="modal-body">
                <p id="orderNow-preview-error-detail"></p>
            </div>
            <div class="modal-footer">
                <div class="orderNow-btn-group">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
