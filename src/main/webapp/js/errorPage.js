function enlarge(id) {
    document.getElementById(id).style.webkitTransform = "scale(1.2)";
}

function recover(id) {
    document.getElementById(id).style.webkitTransform = "scale(1)";
}

function returnLastDocument() {
    var info = $('#error-info').text();
    if (info === '由于您长时间未活动，你已退出，请返回首页重新登陆') {
        var url = '\\';
        window.open(url, '_self');
    } else {
        window.location.href = document.referrer;
    }
}