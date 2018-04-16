function cancelApplication() {
    var url = '\\venue\\home';
    window.open(url, '_self');
}

function apply() {
    var name = $('#reg_venue_name').val();
    var password = $('#reg_venue_password').val();
    var password_again = $('#reg_venue_password2').val();
    var raw = $('#venue_raw').val();
    var col = $('#venue_col').val();
    var price = $('#venue_price').val();
    var address = $('#address').val();
    if (name === '' || password === '' || raw === '' || col === '' || price === '' || address === '') {
        $('#errorMessageField').html('信息不能为空');
    } else if(password !== password_again){
        $('#errorMessageField').html('两次密码输入不一致');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/venue/req_venueApply",
            data: {
                name: name,
                password: password,
                raw: raw,
                col: col,
                price: price,
                address: address
            },
            success: function (data) {
                var message = data['result'].split(';');
                if (message[0] === '1') {
                    $('#application-result-body').empty();
                    var body = document.getElementById('application-result-body');
                    var p1 = $('<p>您的场馆申请已提交，场馆的租用要待系统管理员审核通过后，才可以进行！</p>');
                    var p2 = $('<p></p>');
                    p2.html('这是您的场馆账号：' + data['venueID'] + ',你可以使用账号登录查看场馆的审核状态');
                    p1.appendTo(body);
                    p2.appendTo(body);
                    $('#applicationResult').modal('show');
                } else {
                    $('#errorMessageField').html(message[1]);
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}

function resultConfirm() {
    var url = '\\venue\\home';
    window.open(url, '_self');
}