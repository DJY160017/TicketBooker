<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/3/7
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单预览</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/tool/flat/blue.css" rel="stylesheet">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/order/orderPreview.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/tool/icheck.js"></script>
    <script src="../../js/order/orderPreview.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/order/req_getCacheOrder",
                success: function (data) {
                    var isAutoTicket = data['order']['autoTicket'];
                    $('#order-preview-body').empty();
                    var body = document.getElementById('order-preview-body');
                    if (isAutoTicket === false) {
                        var detail = data['order']['detail'];
                        var need_detail = detail.split(';');
                        for (var i in need_detail) {
                            var item = need_detail[i].split('-');
                            var record = $('<h4></h4>');
                            $(record).html('座位：' + item[0] + '&nbsp&nbsp' + '价格：' + item[1] + '元');
                            record.appendTo(body);
                        }
                    } else {
                        var auto_detail = data['order']['detail'];
                        var auto_need_detail = auto_detail.split(';');
                        var auto_num = $('<h4></h4>');
                        var auto_price = $('<h4></h4>');
                        $(auto_num).html('总张数：' + auto_need_detail[0]);
                        $(auto_price).html('价格：' + auto_need_detail[1]);
                        auto_num.appendTo(body);
                        auto_price.appendTo(body);
                    }
                    var discount = $('<h4 class="discount"></h4>');
                    $(discount).html('会员折扣：' + (parseFloat(data['discount']) * 10).toFixed(2) + "折");
                    discount.appendTo(body);
                    var real_price = (data['order']['total_price'] * parseFloat(data['discount'])).toFixed(2);
                    var price = $('<h3 class="real_price" style="color: rgb(217,84,79)"></h3>');
                    $(price).html('实付：' + real_price + '元');
                    price.appendTo(body);
                },
                error: function (result) {
                    console.log(result);
                }
            });
        });
    </script>
</head>
<body>
<div class="order-preview panel panel-default" style="height: 90%;">
    <div class="order-preview-title text-center" id="order-preview-title">
        <h3>订单预览</h3>
    </div>
    <div class="order-preview-content" id="order-preview-content" style="margin-top: 50px;">
        <div class="order-preview-body col-md-offset-4 col-md-4" id="order-preview-body"></div>
        <div class="coupon-show-select col-md-3 col-md-offset-1">
            <a href="javascript:void(0)" class="btn btn-primary" id="use-coupon" onclick="selectCoupon()">使用优惠券</a>
            <a href="javascript:void(0)" class="btn btn-primary" id="unUse-coupon" onclick="refuseCoupon()" hidden>不使用优惠券</a>
        </div>
    </div>
    <div class="order-preview-footer" id="order-preview-footer">
        <div class="pay-button">
            <button class="btn btn-primary pay-cancel" id="cancel" onclick="cancelOrderPreview()">取消</button>
            <button class="btn btn-success pay-confirm" id="confirm" onclick="generateOrder()">支付</button>
        </div>
    </div>
    <div class="coupon-select-title text-center" id="coupon-select-title" hidden>
        <h3>优惠券</h3>
    </div>
    <div class="coupon-select-content" id="coupon-select-content" style="margin-top: 50px;" hidden>
        <table class="table table-hover" id="coupon-table-detail"
               style="vertical-align: middle;text-align: center;width: 90%;margin-left: 40px;">
            <thead>
            <tr>
                <th>时间</th>
                <th>抵扣金额</th>
                <th>使用</th>
            </tr>
            </thead>
            <tbody id="coupon-table-detail-body"></tbody>
        </table>
        <div class="miss-result-coupon" id="miss-result-coupon-id" style="margin-top: 40px;" hidden>
            <p class="text-center">喔喔！ 您尚未有优惠券</p>
        </div>
    </div>
    <div class="coupon-select-footer" id="coupon-select-footer" hidden>
        <div class="coupon-return-button">
            <button class="btn btn-primary coupon-return" id="coupon-return" onclick="couponReturn()">返回</button>
        </div>
    </div>
</div>
</body>
</html>
