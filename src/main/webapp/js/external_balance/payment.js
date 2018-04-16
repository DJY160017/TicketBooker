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

function pay() {
    var time = getRequestParam('time');
    if (time === '') {
        venuePay()
    } else {
        userPay(time);
    }
}

function userPay(time) {
    var account = $('#account').val();
    var password = $('#password').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/external/req_userPay",
        data: {
            time: time,
            account: account,
            password: password
        },
        success: function (data) {
            var message = data['result'].split(';');
            if (message[0] === '1') {
                $('#paySuccess').modal('show');
            } else {
                $('#errorMessageField').html(message[1]);
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function venuePay() {
    var account = $('#account').val();
    var password = $('#password').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/external/req_venuePay",
        data: {
            account: account,
            password: password
        },
        success: function (data) {
            var message = data['result'].split(';');
            if (message[0] === '1') {
                $('#paySuccess').modal('show');
            } else {
                $('#errorMessageField').html(message[1]);
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function cancelPay() {
    var time = getRequestParam('time');
    if (time === '') {
        cancelVenuePay();
    } else {
        $('#cancel-pay-body').empty();
        var body = document.getElementById('cancel-pay-body');
        var p = $('<p>取消支付，您的订单将会在15分钟后取消，请您尽快去用户中心进行支付或者继续支付</p>');
        p.appendTo(body);
        $('#cancelPay').modal('show');
    }
}

function cancelUserPay() {
    var time = getRequestParam('time');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/external/req_cancelUserPay",
        data: {
            time: time
        },
        success: function (data) {
            console.log(data);
            showUserCenter();
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function cancelVenuePay() {
    var url = '\\venue\\home';
    window.open(url, '_self');
}

function showUserCenter() {
    var url = '\\userInfoManage';
    window.open(url, '_self');
}

function showHome() {
    var url = '\\';
    window.open(url, '_self');
}