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

function logout() {
    $.ajax({
        type: "post",
        async: true,
        url: "/venue/req_log_out",
        success: function (data) {
            if (data['result'] === "1") {
                window.location.href = '/venue/home';
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function login() {
    var venueID = $("#login_venueID").val();
    var password = $("#login_password").val();

    if (venueID === '') {
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
            url: "/venue/req_log_in",
            data: {
                venueID: venueID,
                password: password
            },
            success: function (data) {
                var array = data['result'].split(";");
                if (array[0] === '1') {
                    window.location.reload();
                } else if (array[0] === '-1') {
                    $("#errorMessageField").html("您的用户名或密码错误");
                } else {
                    $("#errorMessageField").html("您的用户名或密码错误");
                }
            },
            error: function (result) {
               console.log(result);
            }
        });
    }
}