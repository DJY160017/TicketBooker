function searchPlan() {
    var key = $('#plan_key').val();
    var start = $('#plan_start_date').val();
    var end = $('#plan_end_date').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_searchPlan",
        data: {
            key: key,
            startTime: start,
            endTime: end
        },
        success: function (data) {
            var size = parseInt(data['program_size'].toString());
            if (size !== 0) {
                $('#plan-table-content').show();
                $('#miss-result-plan-id').hide();
                $('#plan-table-body').empty();
                var body = document.getElementById('plan-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr></tr>');
                    var start = $('<td></td>');
                    var end = $('<td></td>');
                    var programName = $('<td></td>');
                    var venueName = $('<td></td>');
                    var address = $('<td></td>');
                    $(start).html(data['programs'][i]['start_time']);
                    $(end).html(data['programs'][i]['end_time']);
                    $(programName).html(data['programs'][i]['name']);
                    $(venueName).html(data['programs'][i]['venueName']);
                    $(address).html(data['programs'][i]['venueAddress']);
                    start.appendTo(tr);
                    end.appendTo(tr);
                    programName.appendTo(tr);
                    venueName.appendTo(tr);
                    address.appendTo(tr);
                    tr.appendTo(body);
                }
                var max_page = parseInt(data['maxPage']);
                var page = 1;
                var pagination = document.getElementById('plan-table-pagination-id');
                $('#plan-table-pagination-id').empty();
                var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                prev.appendTo(pagination);
                for (var j = 0; j < 5; j++) {
                    var page_num = page + j;
                    var l = $('<li></li>');
                    var a = $('<a href="javascript:void(0)" onclick="searchByPage(this)">' + page_num + '</a>');
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
            } else {
                $('#plan-table-content').hide();
                $('#miss-result-plan-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function searchByPage(page) {
    var pageNow = $(page).text();
    $(page).parents('ul').find('.active').removeClass('active');
    $(page).parents('li').addClass('active');
    var key = $('#plan_key').val();
    var start = $('#plan_start_date').val();
    var end = $('#plan_end_date').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/program/req_getSearchPlaByPage",
        data: {
            key: key,
            startTime: start,
            endTime: end,
            pageNow: pageNow
        },
        success: function (data) {
            $('#plan-table-body').empty();
            var body = document.getElementById('plan-table-body');
            for (var i in data['programs']) {
                var tr = $('<tr></tr>');
                var start = $('<td></td>');
                var end = $('<td></td>');
                var programName = $('<td></td>');
                var venueName = $('<td></td>');
                var address = $('<td></td>');
                $(start).html(data['programs'][i]['start_time']);
                $(end).html(data['programs'][i]['end_time']);
                $(programName).html(data['programs'][i]['name']);
                $(venueName).html(data['programs'][i]['venueName']);
                $(address).html(data['programs'][i]['venueAddress']);
                start.appendTo(tr);
                end.appendTo(tr);
                programName.appendTo(tr);
                venueName.appendTo(tr);
                address.appendTo(tr);
                tr.appendTo(body);
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
        var pagination = document.getElementById('plan-table-pagination-id');
        $('#plan-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        for (var i = 1; i <= 5; i++) {
            var page_num = page - i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="searchByPage(this)">' + page_num + '</a>');
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
        var pagination = document.getElementById('plan-table-pagination-id');
        $('#plan-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        prev.appendTo(pagination);
        for (var i = 1; i <= 5; i++) {
            var page_num = page + i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="searchByPage(this)">' + page_num + '</a>');
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
        url: "/program/req_getSearchPlanMaxPage",
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
        url: "/program/req_getSearchPlanPageNow",
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