function getRequestParam(name) {
    var url = location.search; //获取url中"?"符后的字串
    if (url.indexOf("?") !== -1) {
        var str = url.substr(1);
        var strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            var temp_str = strs[i].split('=');
            if (temp_str[0] === name) {
                return decodeURI(temp_str[1]);
            }
        }
    }
    return '';
}

function selectCoupon() {
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/member/req_getCoupon",
        success: function (data) {
            var size = data['coupons_size'];
            if (size !== 0) {
                $('#coupon-table-detail').show();
                $('#miss-result-coupon-id').hide();
                $('#coupon-table-detail-body').empty();
                var body = document.getElementById('coupon-table-detail-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr></tr>');
                    var time = $('<th class="coupon-time"></th>');
                    var price = $('<th class="coupon-price"></th>');
                    var checkbox = $('<th><input type="checkbox" title=""></th>');
                    $(time).html(data['coupons'][i]['couponID']['time']);
                    $(price).html(data['coupons'][i]['price']);
                    time.appendTo(tr);
                    price.appendTo(tr);
                    checkbox.appendTo(tr);
                    tr.appendTo(body);
                }
                $('input').iCheck({
                    checkboxClass: 'icheckbox_flat-blue',
                    increaseArea: '20%'
                });
                $('#order-preview-title').hide();
                $('#order-preview-content').hide();
                $('#order-preview-footer').hide();
                $('#coupon-select-title').show();
                $('#coupon-select-content').show();
                $('#coupon-select-footer').show();
            } else {
                $('#coupon-table-detail').hide();
                $('#miss-result-coupon-id').show();
                $('#order-preview-title').hide();
                $('#order-preview-content').hide();
                $('#order-preview-footer').hide();
                $('#coupon-select-title').show();
                $('#coupon-select-content').show();
                $('#coupon-select-footer').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function refuseCoupon() {
    $('#coupon-table-detail-body').find('tr').each(function () {
        $(this).find('.checked').removeClass('checked');
    });
    $('#use-coupon').show();
    $('#unUse-coupon').hide();
}

function couponReturn() {
    var isUseCoupon = 0;
    var coupons = [];
    $('#coupon-table-detail-body').find('tr').each(function () {
        var isCheck = $(this).find('.checked').length;
        if (isCheck === 1) {
            isUseCoupon = isCheck;
            var coupon = [];
            coupon.push($(this).find('.coupon-time').text());
            coupon.push(parseFloat($(this).find('.coupon-price').text().toString()));
            coupons.push(coupon);
        }
    });
    if (isUseCoupon !== 0) { //使用优惠券
        $('#use-coupon').hide();
        $('#unUse-coupon').show();
        var real_price = $('#order-preview-body').find('.real_price');
        var price = parseFloat($(real_price).text().toString().split('：')[1].replace('元', ''));
        for (var i in coupons) {
            price = price - coupons[i][1];
            var record = $('<h4 class="coupon"></h4>');
            $(record).html('优惠券：' + coupons[i][1] + '元');
            $(real_price).before(record);
        }
        $(real_price).html('实付：' + price + '元');
    } else { //不使用优惠券
        $('#use-coupon').show();
        $('#unUse-coupon').hide();
    }
    $('#order-preview-title').show();
    $('#order-preview-content').show();
    $('#order-preview-footer').show();
    $('#coupon-select-title').hide();
    $('#coupon-select-content').hide();
    $('#coupon-select-footer').hide();
}

function generateOrder() {
    var coupons = [];
    $('#coupon-table-detail-body').find('tr').each(function () {
        var isCheck = $(this).find('.checked').length;
        if (isCheck === 1) {
            var coupon = [];
            coupon.push($(this).find('.coupon-time').text());
            coupon.push($(this).find('.coupon-price').text().toString());
            coupons.push(coupon);
        }
    });
    var need_coupons = JSON.stringify(coupons);
    var price = $('#order-preview-body').find('.real_price').text().toString().split('：')[1].replace('元', '');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/order/req_generateOrder",
        data: {
            total_price: price,
            coupons: need_coupons
        },
        success: function (data) {
            var message = data['result'].split(';');
            if (message[0] === '1') {
                var time = data['orderTime'];
                var url = '\\external\\payment?time=' + encodeURI(time);
                window.open(url, '_self');
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function cancelOrderPreview(){
    window.location.href=document.referrer;
}