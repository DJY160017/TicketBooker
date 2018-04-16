function enlarge(id) {
    document.getElementById(id).style.webkitTransform = "scale(1.2)";
}

function recover(id) {
    document.getElementById(id).style.webkitTransform = "scale(1)";
}

function searchByTime() {
    var city = $('#province-active').text();
    var startDate = $('#start_date').val();
    var endDate = $('#end_date').val();
    var result = isOutRange(startDate, endDate);
    if (result === 0) {
        $('#end_date').val(startDate);
        endDate = startDate;
    }
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_search",
        data: {
            startDate: startDate,
            endDate: endDate,
            city: city
        },
        success: function (data) {
            $('#program-card-one').empty();
            $('#program-card-two').empty();
            var size = parseInt(data['programs_size']);
            for (var i = 0; i < 8; i++) {
                if (i >= size) {
                    break;
                }
                var li;
                if (i < 4) {
                    li = document.getElementById("program-card-one");
                } else {
                    li = document.getElementById("program-card-two");
                }
                var id = 'program_' + i.toString();
                var div_1 = $('<div class="panel panel-info col-md-2 col-sm-offset-1" style="min-height:200px;" onmouseover="enlarge(this.id)" onmouseout="recover(this.id)" onclick="showChoose(this)"></div>');
                $(div_1).attr('id', id);
                var div_2 = $('<div class="panel-body"></div>');
                var address = $('<p class="address"></p>');
                var name = $('<p class="name"></p>');
                var time = $('<p class="reserve_time"></p>');
                $(name).html(data['programs'][i]['name']);
                $(time).html('时间：' + data['programs'][i]['programID']['reserve_time']);
                $(address).html("地址：" + data['programs'][i]['venueAddress']);
                name.appendTo(div_2);
                time.appendTo(div_2);
                address.appendTo(div_2);
                div_2.appendTo(div_1);
                div_1.appendTo(li);
            }
            var max_page = parseInt(data['maxPage']);
            var page = 1;
            var pagination = document.getElementById('card-pagination-id');
            $('#card-pagination-id').empty();
            var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
            var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
            prev.appendTo(pagination);
            for (var j = 0; j < 5; j++) {
                var page_num = page + j;
                var l = $('<li></li>');
                var a = $('<a href="javascript:void(0)" onclick="getProgramByPage(this)">' + page_num + '</a>');
                if (page_num === max_page) {
                    $(a).attr('id', 'last');
                    a.appendTo(l);
                    l.appendTo(pagination);
                    if (max_page === 1) {
                        $(l).addClass('active');
                    }
                    break;
                }

                if (j === 0) {
                    $(l).addClass('active');
                    $(a).attr('id', 'first');
                }
                if (j === 4) {
                    $(a).attr('id', 'last');
                }
                a.appendTo(l);
                l.appendTo(pagination);
            }
            next.appendTo(pagination);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function searchByCity(province) {
    $('#province-active').css('background', '');
    $('#province-active').css('color', '');
    $('#province-active').removeAttr('id');
    $(province).attr('id', 'province-active');
    $(province).css('background', 'rgb(51,122,183)');
    $(province).css('color', 'rgb(255, 255, 255)');
    var city = $(province).text();
    var startDate = $('#start_date').val();
    var endDate = $('#end_date').val();
    var result = isOutRange(startDate, endDate);
    if (result === 0) {
        $('#end_date').val(startDate);
        endDate = startDate;
    }
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_search",
        data: {
            startDate: startDate,
            endDate: endDate,
            city: city
        },
        success: function (data) {
            $('#program-card-one').empty();
            $('#program-card-two').empty();
            var size = parseInt(data['programs_size']);
            for (var i = 0; i < 8; i++) {
                if (i >= size) {
                    break;
                }
                var li;
                if (i < 4) {
                    li = document.getElementById("program-card-one");
                } else {
                    li = document.getElementById("program-card-two");
                }
                var id = 'program_' + i.toString();
                var div_1 = $('<div class="panel panel-info col-md-2 col-sm-offset-1" style="min-height:200px;" onmouseover="enlarge(this.id)" onmouseout="recover(this.id)" onclick="showChoose(this)"></div>');
                $(div_1).attr('id', id);
                var div_2 = $('<div class="panel-body"></div>');
                var address = $('<p class="address"></p>');
                var name = $('<p class="name"></p>');
                var time = $('<p class="reserve_time"></p>');
                $(name).html(data['programs'][i]['name']);
                $(time).html('时间：' + data['programs'][i]['programID']['reserve_time']);
                $(address).html("地址：" + data['programs'][i]['venueAddress']);
                name.appendTo(div_2);
                time.appendTo(div_2);
                address.appendTo(div_2);
                div_2.appendTo(div_1);
                div_1.appendTo(li);
            }
            var max_page = parseInt(data['maxPage']);
            var page = 1;
            var pagination = document.getElementById('card-pagination-id');
            $('#card-pagination-id').empty();
            var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
            var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
            prev.appendTo(pagination);
            for (var j = 0; j < 5; j++) {
                var page_num = page + j;
                var l = $('<li></li>');
                var a = $('<a href="javascript:void(0)" onclick="getProgramByPage(this)">' + page_num + '</a>');
                if (page_num === max_page) {
                    $(a).attr('id', 'last');
                    a.appendTo(l);
                    l.appendTo(pagination);
                    if (max_page === 1) {
                        $(l).addClass('active');
                    }
                    break;
                }
                if (j === 0) {
                    $(l).addClass('active');
                    $(a).attr('id', 'first');
                }
                if (j === 4) {
                    $(a).attr('id', 'last');
                }
                a.appendTo(l);
                l.appendTo(pagination);
            }
            next.appendTo(pagination);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function getProgramByPage(page) {
    var pageNow = $(page).text();
    var startDate = $('#start_date').val();
    var endDate = $('#end_date').val();
    var result = isOutRange(startDate, endDate);
    if (result === 0) {
        $('#end_date').val(startDate);
        endDate = startDate;
    }
    var city = $('#province-active').text();
    $(page).parents('ul').find('.active').removeClass('active');
    $(page).parents('li').addClass('active');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_programInfoByPage",
        data: {
            pageNow: pageNow,
            startDate: startDate,
            endDate: endDate,
            city: city
        },
        success: function (data) {
            $('#program-card-one').empty();
            $('#program-card-two').empty();
            var size = parseInt(data['programs_size']);
            for (var i = 0; i < 8; i++) {
                if (i >= size) {
                    break;
                }
                var li;
                if (i < 4) {
                    li = document.getElementById("program-card-one");
                } else {
                    li = document.getElementById("program-card-two");
                }
                var id = 'program_' + i.toString();
                var div_1 = $('<div class="panel panel-info col-md-2 col-sm-offset-1" style="min-height:200px;" onmouseover="enlarge(this.id)" onmouseout="recover(this.id)" onclick="showChoose(this)"></div>');
                $(div_1).attr('id', id);
                var div_2 = $('<div class="panel-body"></div>');
                var address = $('<p class="address"></p>');
                var name = $('<p class="name"></p>');
                var time = $('<p class="reserve_time"></p>');
                $(name).html(data['programs'][i]['name']);
                $(time).html('时间：' + data['programs'][i]['programID']['reserve_time']);
                $(address).html("地址：" + data['programs'][i]['venueAddress']);
                name.appendTo(div_2);
                time.appendTo(div_2);
                address.appendTo(div_2);
                div_2.appendTo(div_1);
                div_1.appendTo(li);
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function prevPages() {
    var text = $('#first').text();
    if (text === '') {
        text = $('#last').text();
    }
    var page = parseInt(text.toString());
    if (page !== 1) {
        var pageNow = getPageNow();
        var pagination = document.getElementById('card-pagination-id');
        $('#card-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        for (var i = 1; i <= 5; i++) {
            var page_num = page - i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getProgramByPage(this)">' + page_num + '</a>');
            if (page_num === 1) {
                $(a).attr('id', 'first');
                if (page_num.toString() === pageNow) {
                    $(l).addClass("active");
                }
                a.appendTo(l);
                $(pagination).prepend(l);
                break;
            }
            if (i === 5) {
                $(a).attr('id', 'first');
            }
            if (i === 1) {
                $(a).attr('id', 'last');
            }
            a.appendTo(l);
            if (page_num.toString() === pageNow) {
                $(l).addClass("active");
            }
            $(pagination).prepend(l);
        }
        $(pagination).prepend(prev);
        next.appendTo(pagination);
    }
}

function nextPages() {
    var maxPage = parseInt(getMaxPage());
    var text = $('#last').text();
    var page = parseInt(text.toString());
    if (page !== maxPage) {
        var pageNow = getPageNow();
        var pagination = document.getElementById('card-pagination-id');
        $('#card-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        prev.appendTo(pagination);
        for (var i = 1; i <= 5; i++) {
            var page_num = page + i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getProgramByPage(this)">' + page_num + '</a>');
            if (page_num === maxPage) {
                $(a).attr('id', 'last');
                a.appendTo(l);
                l.appendTo(pagination);
                if (page_num.toString() === pageNow) {
                    $(l).addClass("active");
                }
                break;
            }

            if (i === 1) {
                $(a).attr('id', 'first');
            }
            if (i === 5) {
                $(a).attr('id', 'last');
            }
            a.appendTo(l);
            if (page_num.toString() === pageNow) {
                $(l).addClass("active");
            }
            l.appendTo(pagination);
        }
        next.appendTo(pagination);
    }
}

function getMaxPage() {
    var result;
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getMaxPage",
        async: false,
        success: function (data) {
            result = data['maxPage'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return result;
}

function getPageNow() {
    var result;
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getPageNow",
        async: false,
        success: function (data) {
            result = data['pageNow'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return result;
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

function isOutRange(start, end) {
    var startTime = new Date(start);
    var endTime = new Date(end);
    if (endTime < startTime) {
        return 0;
    }
    return 1;
}

function showChoose(program) {
    var address_tmp = $(program).find('.address').text();
    var time_tmp = $(program).find('.reserve_time').text();
    var address = address_tmp.split('：')[1];
    var time = time_tmp.split('：')[1];
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getOneProgram",
        data: {
            address: address,
            time: time
        },
        success: function (data) {
            $('#program-detail-body').empty();
            var body = document.getElementById('program-detail-body');
            var address = $('<p id="program-detail-address"></p>');
            var name = $('<p id="program-detail-name"></p>');
            var time = $('<p id="program-detail-reserve_time"></p>');
            var introduction = $('<p id="program-detail-introduction"></p>');
            $(name).html('名称：' + data['program']['name']);
            $(time).html('时间：' + data['program']['programID']['reserve_time']);
            $(address).html("地址：" + data['program']['venue']['address']);
            $(introduction).html("简介：" + data['program']['introduction']);
            name.appendTo(body);
            time.appendTo(body);
            address.appendTo(body);
            introduction.appendTo(body);
            $('#program-detail').modal('show');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function chooseTicket() {
    var address_tmp = $('#program-detail-address').text();
    var time_tmp = $('#program-detail-reserve_time').text();
    var address = address_tmp.split('：')[1];
    var time = time_tmp.split('：')[1];
    var url = '\\ticket\\choose?address=' + encodeURI(address) + '&time=' + encodeURI(time);
    window.open(url, '_self');
}