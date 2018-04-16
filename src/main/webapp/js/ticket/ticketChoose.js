function showChooseSeat() {
    $('#choose-seat').show();
    $('#choose-seat-a').parent().addClass("active");
    $('#choose-seat-a').css("color", "");
    $('#orderNow').hide();
    $('#orderNow-a').parent().removeClass("active");
    $('#orderNow-a').css("color", "#1b6d85");
}

function showOrderNow() {
    $('#choose-seat').hide();
    $('#choose-seat-a').parent().removeClass("active");
    $('#choose-seat-a').css("color", "#1b6d85");
    $('#orderNow').show();
    $('#orderNow-a').parent().addClass("active");
    $('#orderNow-a').css("color", "");
}

function calculateSeat(seatID) {
    var id = seatID.split('_');
    var raw = id[0];
    var col = id[1];
    return raw.toString() + '排' + col.toString() + '座';
}

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

function getSeatPrice(col, raw) {
    var price;
    $.ajax({
        type: "post",
        dataType: 'json',
        async: false,
        url: "/ticket/req_getOneTicket",
        data: {
            col: col,
            raw: raw
        },
        success: function (data) {
            price = data['ticket']['price'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return price;
}

function getSeatLowPrice() {
    var price;
    $.ajax({
        type: "post",
        dataType: 'json',
        async: false,
        url: "/ticket/req_getLowPrice",
        success: function (data) {
            price = data['low_price'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return price;
}

function chooseCheckOut() {
    var text = $('#order-price-detail').text();
    var total_price = text.split('：')[1].replace('元', '');
    var detail = '';
    var tr_list = $('#order-table-body').find('tr');
    for (var i = 0; i < tr_list.length; i++) {
        var td_attr = tr_list.eq(i).children('td');
        var seat = td_attr.eq(0).html();
        var price = td_attr.eq(1).html();
        detail = detail + seat + '-' + parseFloat(price.toString().replace('元', '')) + ';';
    }
    detail = detail.substring(0, detail.length - 1);
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/order/req_generateOrderToCache",
        data: {
            total_price: total_price,
            detail: detail
        },
        success: function (data) {
            var url = '\\order\\preview';
            window.open(url, '_self');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function orderNowCheckOut() {
    var ticketNum = parseInt($('#ticket_num').val().toString());
    if (ticketNum <= 20) {
        var price = parseFloat(getSeatLowPrice());
        var totalPrice = ticketNum * price;
        var detail = '张数：' + ticketNum + ';' + '单价：' + price;
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/order/req_generateAutoOrderToCache",
            data: {
                total_price: totalPrice,
                detail: detail
            },
            success: function (data) {
                var url = '\\order\\preview';
                window.open(url, '_self');
            },
            error: function (result) {
                console.log(result);
            }
        });
    } else {
        $('#ticket_num').val('');
        $('#orderNow-preview-error-detail').html('您的购票数不能超过20张');
        $('#orderNow-preview-error').modal('show');
    }
}
