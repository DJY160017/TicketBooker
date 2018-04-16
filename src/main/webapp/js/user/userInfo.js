function showInfo() {
    $('#my-info').show();
    $('#my-info-a').parent().addClass("active");
    $('#my-info-a').css("color", "");
    $('#my-order').hide();
    $('#my-order-a').parent().removeClass("active");
    $('#my-order-a').css("color", "#1b6d85");
    $('#my-discount').hide();
    $('#my-discount-a').parent().removeClass("active");
    $('#my-discount-a').css("color", "#1b6d85");
    $('#my-venuebook').hide();
    $('#my-venuebook-a').parent().removeClass("active");
    $('#my-venuebook-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/req_getUserInfo",
        success: function (data) {
            $('#profile-username-input').val(data['user_name']);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showOrder() {
    $('#my-info').hide();
    $('#my-info-a').parent().removeClass("active");
    $('#my-info-a').css("color", "#1b6d85");
    $('#my-order').show();
    $('#my-order-a').parent().addClass("active");
    $('#my-order-a').css("color", "");
    $('#my-discount').hide();
    $('#my-discount-a').parent().removeClass("active");
    $('#my-discount-a').css("color", "#1b6d85");
    $('#my-venuebook').hide();
    $('#my-venuebook-a').parent().removeClass("active");
    $('#my-venuebook-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/order/req_getUserOrder",
        success: function (data) {
            if (data['paid_length'] === 0) {
                $('#alreadyPaid-table-id').hide();
                $('#miss-alreadyPaid-result-id').show();
            } else {
                $('#alreadyPaid-table-detail-body').empty();
                var table_1 = document.getElementById('alreadyPaid-table-detail-body');
                for (var i = 0; i < data['paid_length']; i++) {
                    var id_1 = 'order' + i.toString();
                    var need_name_1 = data['paid_programs'][i]['name'];
                    var body_1 = $('<tr></tr>');
                    var time_1 = $('<td id="' + id_1 + '">' + data['paid'][i]['orderID']['orderTime'] + '</td>');
                    var name_1 = $('<td>' + need_name_1 + '</td>');
                    var price_1 = $('<td>' + data['paid'][i]['total_price'] + '</td>');
                    var detail_1 = $('<td><a href="javascript:void(0)" onclick="checkDetail(' + id_1 + ')">' + '点击查看详情' + '</a></td>');
                    var unSubscribed_1 = $('<td onclick="checkUnSubscribed(' + id_1 + ')"><a href="javascript:void(0)">' + '点击退订' + '</a></td>');
                    time_1.appendTo(body_1);
                    name_1.appendTo(body_1);
                    price_1.appendTo(body_1);
                    detail_1.appendTo(body_1);
                    unSubscribed_1.appendTo(body_1);
                    body_1.appendTo(table_1);
                }
            }

            if (data['unPaid_length'] === 0) {
                $('#unPaid-table-id').hide();
                $('#miss-unPaid-result-id').show();
            } else {
                $('#unPaid-table-detail-body').empty();
                var table_2 = document.getElementById('unPaid-table-detail-body');
                for (var j = 0; j < data['unPaid_length']; j++) {
                    var id_2 = 'unPaidOrder' + j.toString();
                    var need_name_2 = data['unPaid_programs'][j]['name'];
                    var body_2 = $('<tr></tr>');
                    var time_2 = $('<td id="' + id_2 + '">' + data['unPaid'][j]['orderID']['orderTime'] + '</td>');
                    var name_2 = $('<td>' + need_name_2 + '</td>');
                    var price_2 = $('<td>' + data['unPaid'][j]['total_price'] + '</td>');
                    var detail_2 = $('<td><a href="javascript:void(0)" onclick="checkDetail(' + id_2 + ')">' + '点击查看详情' + '</a></td>');
                    var pay = $('<td><a href="javascript:void(0)" onclick="pay(' + id_2 + ')">' + '支付' + '</a></td>');
                    time_2.appendTo(body_2);
                    name_2.appendTo(body_2);
                    price_2.appendTo(body_2);
                    detail_2.appendTo(body_2);
                    pay.appendTo(body_2);
                    body_2.appendTo(table_2);
                }
            }

            if (data['unSubscribed_length'] === 0) {
                $('#unSubscribed-table-id').hide();
                $('#miss-unSubscribed-result-id').show();
            } else {
                $('#unSubscribed-table-detail-body').empty();
                var table_3 = document.getElementById('unSubscribed-table-detail-body');
                for (var k = 0; k < data['unSubscribed_length']; k++) {
                    var id_3 = 'unSubscribed' + k.toString();
                    var need_name_3 = data['unSubscribed_programs'][k]['name'];
                    var body_3 = $('<tr></tr>');
                    var time_3 = $('<td id="' + id_3 + '">' + data['unSubscribed'][k]['orderID']['orderTime'] + '</td>');
                    var name_3 = $('<td>' + need_name_3 + '</td>');
                    var price_3 = $('<td>' + data['unSubscribed'][k]['total_price'] + '</td>');
                    var detail_3 = $('<td><a href="javascript:void(0)" onclick="checkDetail(' + id_3 + ')">' + '点击查看详情' + '</a></td>');
                    time_3.appendTo(body_3);
                    name_3.appendTo(body_3);
                    price_3.appendTo(body_3);
                    detail_3.appendTo(body_3);
                    body_3.appendTo(table_3);
                }
            }

            if (data['invalidOrder_length'] === 0) {
                $('#invalidOrder-table-id').hide();
                $('#miss-invalidOrder-result-id').show();
            } else {
                $('#invalidOrder-table-detail-body').empty();
                var table_4 = document.getElementById('invalidOrder-table-detail-body');
                for (var m = 0; m < data['invalidOrder_length']; m++) {
                    var id_4 = 'invalidOrder' + m.toString();
                    var need_name_4 = data['invalidOrder_programs'][m]['name'];
                    var body_4 = $('<tr></tr>');
                    var time_4 = $('<td id="' + id_4 + '">' + data['invalidOrder'][m]['orderID']['orderTime'] + '</td>');
                    var name_4 = $('<td>' + need_name_4 + '</td>');
                    var price_4 = $('<td>' + data['invalidOrder'][m]['total_price'] + '</td>');
                    var detail_4 = $('<td><a href="javascript:void(0)" onclick="checkDetail(' + id_4 + ')">' + '点击查看详情' + '</a></td>');
                    time_4.appendTo(body_4);
                    name_4.appendTo(body_4);
                    price_4.appendTo(body_4);
                    detail_4.appendTo(body_4);
                    body_4.appendTo(table_4);
                }
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showDiscount() {
    $('#my-info').hide();
    $('#my-info-a').parent().removeClass("active");
    $('#my-info-a').css("color", "#1b6d85");
    $('#my-order').hide();
    $('#my-order-a').parent().removeClass("active");
    $('#my-order-a').css("color", "#1b6d85");
    $('#my-discount').show();
    $('#my-discount-a').parent().addClass("active");
    $('#my-discount-a').css("color", "");
    $('#my-venuebook').hide();
    $('#my-venuebook-a').parent().removeClass("active");
    $('#my-venuebook-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/member/req_getCoupon",
        success: function (data) {
            if (data['coupons_size'] === 0) {
                $('#discount-table-id').hide();
                $('#miss-discount-result-id').show();
            } else {
                $('#discount-table-id').show();
                $('#miss-discount-result-id').hide();
                $('#discount-table-detail-body').empty();
                var table = document.getElementById('discount-table-detail-body');
                for (var i = 0; i < data['coupons_size']; i++) {
                    var body = $('<tr></tr>');
                    var time = $('<td>' + data['coupons'][i]['couponID']['time'] + '</td>');
                    var price = $('<td>' + data['coupons'][i]['price'] + '元</td>');
                    time.appendTo(body);
                    price.appendTo(body);
                    body.appendTo(table);
                }
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showMyVenueBook() {
    $('#my-info').hide();
    $('#my-info-a').parent().removeClass("active");
    $('#my-info-a').css("color", "#1b6d85");
    $('#my-order').hide();
    $('#my-order-a').parent().removeClass("active");
    $('#my-order-a').css("color", "#1b6d85");
    $('#my-discount').hide();
    $('#my-discount-a').parent().removeClass("active");
    $('#my-discount-a').css("color", "#1b6d85");
    $('#my-venuebook').show();
    $('#my-venuebook-a').parent().addClass("active");
    $('#my-venuebook-a').css("color", "");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getCarterVenueBook",
        success: function (data) {
            if (data['programs_size'] === 0) {
                $('#venuebook-table-id').hide();
                $('#miss-venuebook-result-id').show();
            } else {
                $('#venuebook-table-detail-body').empty();
                var table = document.getElementById('venuebook-table-detail-body');
                for (var i = 0; i < data['programs_size']; i++) {
                    var body = $('<tr></tr>');
                    var venueName = $('<td>' + data['programs'][i]['venueName'] + '</td>');
                    var programName = $('<td>' + data['programs'][i]['name'] + '</td>');
                    var startTime = $('<td>' + data['programs'][i]['start_time'] + '</td>');
                    var endTime = $('<td>' + data['programs'][i]['end_time'] + '</td>');
                    var address = $('<td>' + data['programs'][i]['venueAddress'] + '</td>');
                    var day = calculateDays(data['programs'][i]['start_time'], data['programs'][i]['end_time']);
                    var price = day * parseFloat(data['programs'][i]['venuePrice'].toString());
                    var price_show = $('<td>' + price + '元</td>');
                    venueName.appendTo(body);
                    programName.appendTo(body);
                    startTime.appendTo(body);
                    endTime.appendTo(body);
                    price_show.appendTo(body);
                    address.appendTo(body);
                    body.appendTo(table);
                }
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function show_password_save() {
    document.getElementById('password-save').style.display = 'inline';
    document.getElementById('password-modify').style.display = 'none';
    $("#profile-password-new").removeAttr('disabled');
    $("#profile-password-again").removeAttr('disabled');
}

function show_password_modify() {
    document.getElementById('password-save').style.display = 'none';
    document.getElementById('password-modify').style.display = 'inline';
    $("#profile-password-new").attr('disabled', true);
    $("#profile-password-again").attr('disabled', true);
    var password = $('#profile-password-new').val();
    var password_again = $('#profile-password-again').val();
    if (password === '' || password_again === '') {
        $('#errorMessageField').html('密码不能为空');
    } else if (password !== password_again) {
        $('#errorMessageField').html('两次密码不一致');
    } else {
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: "/req_ModifyPassword",
            data: {
                password: password
            },
            success: function (data) {
                console.log(data);
                window.location.reload();
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}

function show_profile_save() {
    document.getElementById('profile-save').style.display = 'inline';
    document.getElementById('profile-modify').style.display = 'none';
    $("#profile-username-input").removeAttr('disabled');
}

function show_profile_modify() {
    document.getElementById('profile-save').style.display = 'none';
    document.getElementById('profile-modify').style.display = 'inline';
    $("#profile-username-input").attr('disabled', true);
    var user_name = $('#profile-username-input').val();
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: "/req_ModifyUserInfo",
        data: {
            user_name: user_name
        },
        success: function (data) {
            console.log(data);
            window.location.reload();
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function checkDetail(id) {
    var time = id.innerText;
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: "/order/req_getOrderDetail",
        data: {
            time: time
        },
        success: function (data) {
            console.log(data);
            var detail = data['detail'].split(';');
            $('#order-show-content').empty();
            var content = document.getElementById('order-show-content');
            for (var i in detail) {
                var p = $('<p></p>');
                if (detail[i].indexOf('折扣') !== -1) {
                    var discount = detail[i].split(':');
                    $(p).html('会员折扣：' + discount[1]);
                } else if (detail[i].indexOf('优惠券') !== -1) {
                    var coupon_content = detail[i].replace('优惠券:', '');
                    var coupons = [];
                    if (coupon_content.indexOf('|') !== -1) {
                        coupons = coupon_content.split('|');
                    } else {
                        coupons.push(coupon_content);
                    }
                    for (var index in coupons) {
                        var coupon = coupons[index].split('T')[1].split('-');
                        var coupon_p = $('<p></p>');
                        $(coupon_p).html('使用优惠券：' + coupon[1] + '元');
                        coupon_p.appendTo(content);
                    }
                } else if (detail[i].indexOf('配票成功') !== -1) {
                    $(p).html('配票状态：' + detail[i]);
                } else {
                    if (data['isAutoTicket'] === true && data['detail'].indexOf('配票成功') === -1) {
                        $(p).html(detail[i]);
                    } else {
                        var temp_str = detail[i].split('-');
                        $(p).html('座位：' + temp_str[0] + ' 价格：' + temp_str[1] + '元');
                    }
                }
                p.appendTo(content);
            }
            var last_p = $('<p>' + '总价：' + data['total_price'] + '元' + '</p>');
            last_p.appendTo(content);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $('#order-detail-show').modal('show');
}

function checkUnSubscribed(id) {
    var time = id.innerText;
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: "/order/req_getOrderDetail",
        data: {
            time: time
        },
        success: function (data) {
            console.log(data);
            var detail = data['detail'].split(';');
            $('#unSubscribed-show-content').empty();
            var fist_p = $('<p id="order-time"></p>');
            var content = document.getElementById('unSubscribed-show-content');
            fist_p.appendTo(content);
            $('#order-time').html('订单时间：' + time);
            for (var i in detail) {
                var p = $('<p></p>');
                if (detail[i].indexOf('折扣') !== -1) {
                    var discount = detail[i].split(':');
                    $(p).html('会员折扣：' + discount[1]);
                } else if (detail[i].indexOf('优惠券') !== -1) {
                    var coupon_content = detail[i].replace('优惠券:', '');
                    var coupons = [];
                    if (coupon_content.indexOf('|') !== -1) {
                        coupons = coupon_content.split('|');
                    } else {
                        coupons.push(coupon_content);
                    }
                    for (var index in coupons) {
                        var coupon = coupons[index].split('T')[1].split('-');
                        var coupon_p = $('<p></p>');
                        $(coupon_p).html('使用优惠券：' + coupon[1] + '元');
                        coupon_p.appendTo(content);
                    }
                } else if (detail[i].indexOf('配票成功') !== -1) {
                    $(p).html('配票状态：' + detail[i]);
                } else {
                    if (data['isAutoTicket'] === true && data['detail'].indexOf('配票成功') === -1) {
                        $(p).html(detail[i]);
                    } else {
                        var temp_str = detail[i].split('-');
                        $(p).html('座位：' + temp_str[0] + ' 价格：' + temp_str[1] + '元');
                    }
                }
                p.appendTo(content);
            }
            var last_p = $('<p>' + '总价：' + data['total_price'] + '元' + '</p>');
            last_p.appendTo(content);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $('#unSubscribed-show').modal('show');
}

function unSubscribe() {
    var text = $('#order-time').html();
    var time = text.split('：')[1];
    var account = $('#bank_account').val();
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: "/order/req_subscribe",
        data: {
            time: time,
            account: account
        },
        success: function (data) {
            console.log(data);
            $('#account-show').modal('hide');
            showOrder();
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function unSubscribe_show() {
    $('#unSubscribed-show').modal('hide');
    $('#account-show').modal('show');
}

function pay(id) {
    var time = id.innerText;
    var url = '\\external\\payment?time=' + encodeURI(time);
    window.open(url, '_self');
}

function calculateDays(start, end) {
    var startTime = new Date(start.toString().replace('-', '/'));
    var endTime = new Date(end.toString().replace('-', '/'));
    return (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60 / 24;
}