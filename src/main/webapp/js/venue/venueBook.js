function chooseProgramType(type) {
    var need_type = $(type).text();
    $('#reg_program_type').html(need_type + '<span class="caret"></span>');
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

function prevPages() {
    var text = $('#first').text();
    if (text === '') {
        text = $('#last').text();
    }
    var page = parseInt(text.toString());
    if (page !== 1) {
        var pageNow = getPageNow();
        var pagination = document.getElementById('venue-book-table-pagination-id');
        $('#venue-book-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        for (var i = 1; i <= 5; i++) {
            var page_num = page - i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getPlanByPage(this)">' + page_num + '</a>');
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
        var pagination = document.getElementById('venue-book-table-pagination-id');
        $('#venue-book-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        prev.appendTo(pagination);
        for (var i = 1; i <= 5; i++) {
            var page_num = page + i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getPlanByPage(this)">' + page_num + '</a>');
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

function getPlanByPage(page) {
    var pageNow = $(page).text();
    $(page).parents('ul').find('.active').removeClass('active');
    $(page).parents('li').addClass('active');
    var address = getRequestParam('address');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getVenuePlanByPage",
        data: {
            pageNow: pageNow,
            address: address
        },
        success: function (data) {
            var size = parseInt(data['program_size'].toString());
            $('#venue-book-table-content').show();
            $('#miss-result-venue-plan-id').hide();
            $('#venue-booked-table-body').empty();
            var body = document.getElementById('venue-booked-table-body');
            for (var i = 0; i < size; i++) {
                var tr = $('<tr></tr>');
                var start = $('<td></td>');
                var end = $('<td></td>');
                var state = $('<td>已预订</td>');
                $(start).html(data['programs'][i]['start_time']);
                $(end).html(data['programs'][i]['end_time']);
                start.appendTo(tr);
                end.appendTo(tr);
                state.appendTo(tr);
                tr.appendTo(body);
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function getMaxPage() {
    var result;
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getPlanMaxPage",
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
        url: "/program/req_getPlanPageNow",
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

function venueBook() {
    var startTime = $('#start_date').val();
    var endTime = $('#end_date').val();
    var name = $('#reg_program_name').val();
    var type = $('#reg_program_type').text();
    var reserve_time = $('#program_start_date').val();
    var introduction = $('#program_introduction').val();
    var low_price = $('#ticket_low_price').val();
    var raw_rule = $('#price_raw_rule').val();
    var col_rule = $('#price_col_rule').val();
    var address = getRequestParam('address');

    if (startTime === '' || endTime === '' || name === '' || type === '' || reserve_time === '' ||
        introduction === '' || low_price === '' || raw_rule === '' || col_rule === '') {
        $('#errorMessageField').html('信息不能为空');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/venue/req_book",
            data: {
                address: address,
                startTime: startTime,
                endTime: endTime,
                name: name,
                type: type,
                reserve_time: reserve_time,
                introduction: introduction,
                low_price: low_price,
                raw_rule: raw_rule,
                col_rule: col_rule
            },
            success: function (data) {
                var message = data['result'].split(';');
                if (message[0] === '1') {
                    var url = '\\external\\payment';
                    window.open(url, '_self');
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

function showRule() {
    $('#rule-explain').modal('show');
}

function seatPriceDecideHelp() {
    var address = getRequestParam('address');
    var type = $('#reg_program_type').text().trim();
    if (type === '@节目类型') {
        $('#seat-decide-tip').modal('show');
    } else {
        var url = '/statistics/publisher/?address=' + address + '&type=' + type;
        window.open(url, '_self');
    }
}

