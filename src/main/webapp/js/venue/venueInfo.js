function showVenueInfo() {
    $('#venueInfo').show();
    $('#venueInfo-a').parent().addClass("active");
    $('#venueInfo-a').css("color", "");
    $('#venue-plan').hide();
    $('#venue-plan-a').parent().removeClass("active");
    $('#venue-plan-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_getVenueInfo",
        success: function (data) {
            $('#venueID').val(venueIDFormat(data['venue']['venueID']));
            $('#venue_name').val(data['venue']['name']);
            $('#venue_raw').val(data['venue']['raw_num']);
            $('#venue_col').val(data['venue']['col_num']);
            $('#venue_price').val(data['venue']['price']);
            $('#address').val(data['venue']['address']);
            var state = data['venue']['venueState'];
            if (state === 'Unapproved') {
                $('#venue_state').html("尚未审核");
                $('#venue_state').css('color', 'rgb(279,173,79)');
            } else if (state === 'NotThrough') {
                $('#venue_state').html("未通过审核，请仔细检查您的场馆信息");
                $('#venue_state').css('color', 'rgb(217,84,79)');
            } else {
                $('#venue_state').html("审核通过");
                $('#venue_state').css('color', 'rgb(91,184,93)');
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showPlan() {
    $('#venueInfo').hide();
    $('#venueInfo-a').parent().removeClass("active");
    $('#venueInfo-a').css("color", "#1b6d85");
    $('#venue-plan').show();
    $('#venue-plan-a').parent().addClass("active");
    $('#venue-plan-a').css("color", "");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getMyVenuePlan",
        success: function (data) {
            var size = data['length'];
            if (size !== 0) {
                $('#venue-plan-title').css('border-bottom', '');
                $('#miss-plan-result-id').hide();
                $('#venue-plan-body').empty();
                var body = document.getElementById('venue-plan-body');
                for (var i in data['programs']) {
                    var plan = $('<tr></tr>');
                    var startTime = $('<td></td>');
                    var endTime = $('<td></td>');
                    var type = $('<td></td>');
                    var name = $('<td></td>');
                    $(startTime).html(data['programs'][i]['programID']['reserve_time']);
                    $(endTime).html(data['programs'][i]['end_time']);
                    $(type).html(data['programs'][i]['programType']);
                    $(name).html(data['programs'][i]['name']);
                    startTime.appendTo(plan);
                    endTime.appendTo(plan);
                    type.appendTo(plan);
                    name.appendTo(plan);
                    plan.appendTo(body);
                }
            } else {
                $('#venue-plan-title').css('border-bottom', '1px solid rgba(51, 51, 51, 0.3)');
                $('#miss-plan-result-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function password_save() {
    var password = $('#venue-password-new').val();
    var password_again = $('#venue-password-again').val();
    if (password === '' || password_again === '') {
        $('#errorMessageField').html('密码不能为空');
    } else if (password !== password_again) {
        $('#errorMessageField').html('两次密码不一致');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/venue/req_venueModifyPassword",
            data: {
                password: password
            },
            success: function (data) {
                $('#password-save').hide();
                $('#password-modify').show();
                $('#password-cancel').hide();
                $("#venue-password-new").attr('disabled', true);
                $("#venue-password-again").attr('disabled', true);
                $('#venue-password-new').val('');
                $('#venue-password-again').val('');
                $('#errorMessageField').html('');
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}

function password_modify() {
    $('#password-save').show();
    $('#password-modify').hide();
    $('#password-cancel').show();
    $("#venue-password-new").removeAttr('disabled');
    $("#venue-password-again").removeAttr('disabled');
}

function password_cancel() {
    $('#password-save').hide();
    $('#password-modify').show();
    $('#password-cancel').hide();
    $("#venue-password-new").attr('disabled', true);
    $("#venue-password-again").attr('disabled', true);
}

function venue_save() {
    var name = $('#venue_name').val();
    var raw = $('#venue_raw').val();
    var col = $('#venue_col').val();
    var price = $('#venue_price').val();
    var address = $('#address').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_venueInfoModify",
        data: {
            name: name,
            raw: raw,
            col: col,
            price: price,
            address: address
        },
        success: function (data) {
            console.log(data);
            $('#venue_state').html("尚未审核");
            $('#venue_state').css('color', 'rgb(279,173,79)');
            $('#venue-info-modify').show();
            $('#venue-info-save').hide();
            $('#venue-info-cancel').hide();
            $('#venue_name').attr('disabled', true);
            $("#venue_raw").attr('disabled', true);
            $("#venue_col").attr('disabled', true);
            $("#venue_price").attr('disabled', true);
            $("#address").attr('disabled', true);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function venue_modify() {
    var state = getVenueState();
    if (state !== 'Unapproved') {
        $('#venue-info-modify').hide();
        $('#venue-info-save').show();
        $('#venue-info-cancel').show();
        $('#venue_name').removeAttr('disabled');
        $("#venue_raw").removeAttr('disabled');
        $("#venue_col").removeAttr('disabled');
        $("#venue_price").removeAttr('disabled');
        $("#address").removeAttr('disabled');
    }
}

function venue_cancel() {
    $('#venue-info-modify').show();
    $('#venue-info-save').hide();
    $('#venue-info-cancel').hide();
    $('#venue_name').attr('disabled', true);
    $("#venue_raw").attr('disabled', true);
    $("#venue_col").attr('disabled', true);
    $("#venue_price").attr('disabled', true);
    $("#address").attr('disabled', true);
}

function venueIDFormat(venueID) {
    var result = venueID.toString();
    var length = 7 - result.length;
    for (var i = 0; i < length; i++) {
        result = '0' + result;
    }
    return result;
}

function getVenueState() {
    var state;
    $.ajax({
        type: "post",
        dataType: 'json',
        async: false,
        url: "/venue/req_getVenueState",
        success: function (data) {
            state = data['venueState'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return state;
}

function account_modify() {
    $('#account-save').show();
    $('#account-modify').hide();
    $('#account-cancel').show();
    $("#venue-account-value").removeAttr('disabled');
}

function account_cancel() {
    $('#account-save').hide();
    $('#account-modify').show();
    $('#account-cancel').hide();
    $("#venue-account-value").attr('disabled', true);
}

function account_save() {
    var account = $('#venue-account-value').val();
    if (account === '') {
        $('#accountErrorMessageField').html('银行卡号不能为空');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/venue/req_addVenueAccount",
            data: {
                account: account
            },
            success: function (data) {
                $('#account-save').hide();
                $('#account-modify').show();
                $('#account-cancel').hide();
                $("#venue-account-value").attr('disabled', true);
                $('#accountErrorMessageField').html('');
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}