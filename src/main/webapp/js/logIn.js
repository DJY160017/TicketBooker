$(document).ready(function () {
    //显示出白色下划线的效果
    //切换白色下划线
    $(".navbar-nav li a").on("click", function () {
        $(".navbar-nav li a[class~='act']").css("color", "#BDBDBD");
        $(".navbar-nav li a[class~='act']").removeClass("act");
        $(this).addClass("act");
        $(this).css("color", "#ffffff");
    });
});

function sendIDCode() {
    var email = $("#e_mail").val();
    if (email === '') {
        $('#errorMessageField2').html("邮箱不能为空");
        setTimeout(function () {
            $("#errorMessageField2").html('');
        }, 6000);
    } else {
        $('#id-code-button').attr('disabled', true);
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/req_sendIDCode",
            data: {
                email: email
            },
            success: function (data) {
                var array = data['result'].split(";");
                if (array[0] === '1') {
                    setTimeout(function () {
                        $('#id-code-button').removeAttr('disabled');
                    }, 60000);
                } else if (array[0] === '-1') {
                    $("#errorMessageField2").html(array[1]);
                } else {
                    $("#errorMessageField2").html("请再次确定输入信息");
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}

function logout() {
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/req_log_out",
        success: function (data) {
            if (data['result'] === "1") {
                window.open('/', '_self');
            }
        },
        error: function (result) {
            console.log(result)
        }
    });
}

function login() {
    var email = $("#login_mail").val();
    var password = $("#login_password").val();

    if (email === '') {
        $('#errorMessageField2').html("邮箱不能为空");
        setTimeout(function () {
            $("#errorMessageField2").html('');
        }, 6000);
    } else if (password === '') {
        $('#errorMessageField2').html("密码不能为空");
        setTimeout(function () {
            $("#errorMessageField2").html('');
        }, 6000);
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/req_log_in",
            data: {
                email: email,
                password: password
            },
            success: function (data) {
                var array = data['result'].split(";");
                if (array[0] === '1') {
                    if (data['user_type'] === 'admin') {
                        window.open('/ticketBookerManager/home', '_self')
                    } else {
                        window.location.reload();
                    }
                } else {
                    $("#errorMessageField").html(array[1]);
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}

function register() {
    var userName = $("#reg_username").val();
    var password = $("#reg_password").val();
    var email = $("#e_mail").val();
    var idCode = $('#idCode').val();

    if (userName === '' || password === '' || email === '' || idCode === '') {
        $("#errorMessageField2").html("信息不能为空");
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/req_register",
            data: {
                userName: userName,
                password: password,
                email: email,
                idCode: idCode
            },
            success: function (data) {
                var array = data['result'].split(";");
                if (array[0] === '1') {
                    location.reload();
                } else if (array[0] === '-1') {
                    // 提示错误信息
                    $("#errorMessageField2").html(array[1]);
                } else {
                    $("#errorMessageField2").html("请再次确定输入信息");
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
}