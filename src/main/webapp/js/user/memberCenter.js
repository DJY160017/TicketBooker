function cancelMember() {
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/member/req_cancelMember",
        success: function (data) {
            $('#member-cancel-result').modal('show');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function exchange() {
    var mark = $('#marks').val();
    if (mark === '') {
        $('#error-info').html("积分不能为空");
        setTimeout(function () {
            $('#error-info').html("");
        }, 6000)
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/member/req_exchange",
            data: {
                mark: mark
            },
            success: function (data) {
                $('#exchange-show-content').empty();
                var content = document.getElementById('exchange-show-content');
                if (data['result'] === '1') {
                    var p_1 = $('<p></p>');
                    $(p_1).html('亲爱的用户，您已兑换' + data['price'].toString() + '元');
                    p_1.appendTo(content);
                    $('#current-mark-id').html('积分兑换（现有' + data['new_mark'].toString() + '分）：');
                } else {
                    var p_2 = $('<p></p>');
                    $(p_2).html(data['result']);
                    p_2.appendTo(content);
                }
                $('#exchange-result-show').modal('show');
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}