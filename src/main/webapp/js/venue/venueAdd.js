function searchByCity(province) {
    $('#province-active').css('background', '');
    $('#province-active').css('color', '');
    $('#province-active').removeAttr('id');
    $(province).attr('id', 'province-active');
    $(province).css('background', 'rgb(51,122,183)');
    $(province).css('color', 'rgb(255, 255, 255)');
    var city = $(province).text();
    var key = $('#venue_key').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_search",
        data: {
            area: city,
            key: key
        },
        success: function (data) {
            var size = parseInt(data['venues_size']);
            if (size !== 0) {
                $('#venue-table-content').show();
                $('#miss-result-venue-id').hide();
                $('#venue-book-table-body').empty();
                var body = document.getElementById('venue-book-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr onclick="showVenueDetail(this)"></tr>');
                    var name = $('<td class="name"></td>');
                    var address = $('<td class="address"></td>');
                    var price = $('<td class="price"></td>');
                    $(name).html(data['venues'][i]['name']);
                    $(address).html(data['venues'][i]['address']);
                    $(price).html(data['venues'][i]['price'] + '元/天');
                    name.appendTo(tr);
                    price.appendTo(tr);
                    address.appendTo(tr);
                    tr.appendTo(body);
                }
                var max_page = parseInt(data['maxPage']);
                var page = 1;
                var pagination = document.getElementById('venue-table-pagination-id');
                $('#venue-table-pagination-id').empty();
                var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                prev.appendTo(pagination);
                for (var j = 0; j < 10; j++) {
                    var page_num = page + j;
                    var l = $('<li></li>');
                    var a = $('<a href="javascript:void(0)" onclick="getVenueByPage(this)">' + page_num + '</a>');
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
                    if (j === 9) {
                        $(a).attr('id', 'last');
                    }
                    a.appendTo(l);
                    l.appendTo(pagination);
                }
                next.appendTo(pagination);
            } else {
                $('#venue-table-content').hide();
                $('#miss-result-venue-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function searchByKey() {
    var city = $('#province-active').text();
    var key = $('#venue_key').val();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_search",
        data: {
            area: city,
            key: key
        },
        success: function (data) {
            var size = parseInt(data['venues_size']);
            if (size !== 0) {
                $('#venue-table-content').show();
                $('#miss-result-venue-id').hide();
                $('#venue-book-table-body').empty();
                var body = document.getElementById('venue-book-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr onclick="showVenueDetail(this)"></tr>');
                    var name = $('<td class="name"></td>');
                    var address = $('<td class="address"></td>');
                    var price = $('<td class="price"></td>');
                    $(name).html(data['venues'][i]['name']);
                    $(address).html(data['venues'][i]['address']);
                    $(price).html(data['venues'][i]['price'] + '元/天');
                    name.appendTo(tr);
                    price.appendTo(tr);
                    address.appendTo(tr);
                    tr.appendTo(body);
                }
                var max_page = parseInt(data['maxPage']);
                var page = 1;
                var pagination = document.getElementById('venue-table-pagination-id');
                $('#venue-table-pagination-id').empty();
                var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
                var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
                prev.appendTo(pagination);
                for (var j = 0; j < 10; j++) {
                    var page_num = page + j;
                    var l = $('<li></li>');
                    var a = $('<a href="javascript:void(0)" onclick="getVenueByPage(this)">' + page_num + '</a>');
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
                    if (j === 9) {
                        $(a).attr('id', 'last');
                    }
                    a.appendTo(l);
                    l.appendTo(pagination);
                }
                next.appendTo(pagination);
            } else {
                $('#venue-table-content').hide();
                $('#miss-result-venue-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function getVenueByPage(page) {
    var pageNow = $(page).text();
    var city = $('#province-active').text();
    var key = $('#venue_key').text();
    $(page).parents('ul').find('.active').removeClass('active');
    $(page).parents('li').addClass('active');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_venueInfoByPage",
        data: {
            pageNow: pageNow,
            area: city,
            key: key
        },
        success: function (data) {
            var size = parseInt(data['venues_size']);
            if (size !== 0) {
                $('#venue-table-content').show();
                $('#miss-result-venue-id').hide();
                $('#venue-book-table-body').empty();
                var body = document.getElementById('venue-book-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr onclick="showVenueDetail(this)"></tr>');
                    var name = $('<td class="name"></td>');
                    var address = $('<td class="address"></td>');
                    var price = $('<td class="price"></td>');
                    $(name).html(data['venues'][i]['name']);
                    $(address).html(data['venues'][i]['address']);
                    $(price).html(data['venues'][i]['price'] + '元/天');
                    name.appendTo(tr);
                    price.appendTo(tr);
                    address.appendTo(tr);
                    tr.appendTo(body);
                }
            } else {
                $('#venue-table-content').hide();
                $('#miss-result-venue-id').show();
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
        var pagination = document.getElementById('venue-table-pagination-id');
        $('#venue-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        for (var i = 1; i <= 10; i++) {
            var page_num = page - i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getVenueByPage(this)">' + page_num + '</a>');
            if (page_num === 1) {
                $(a).attr('id', 'first');
                if (page_num.toString() === pageNow) {
                    $(l).addClass("active");
                }
                a.appendTo(l);
                $(pagination).prepend(l);
                break;
            }

            if (i === 10) {
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
        var pagination = document.getElementById('venue-table-pagination-id');
        $('#venue-table-pagination-id').empty();
        var prev = $('<li><a href="javascript:void(0)" id="prev" onclick="prevPages()">&laquo;</a></li>');
        var next = $('<li><a href="javascript:void(0)" id="next" onclick="nextPages()">&raquo;</a></li>');
        prev.appendTo(pagination);
        for (var i = 1; i <= 10; i++) {
            var page_num = page + i;
            var l = $('<li></li>');
            var a = $('<a href="javascript:void(0)" onclick="getVenueByPage(this)">' + page_num + '</a>');
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
            if (i === 10) {
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
        url: "/venue/req_getMaxPage",
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
        url: "/venue/req_getPageNow",
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

function showVenueDetail(venue) {
    var address = $(venue).find('.address').text();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/venue/req_getOneVenue",
        data: {
            address: address
        },
        success: function (data) {
            $('#venue-detail-body').empty();
            var body = document.getElementById('venue-detail-body');
            var name = $('<p></p>');
            var price = $('<p></p>');
            var address = $('<p class="address"></p>');
            var col = $('<p></p>');
            var raw = $('<p></p>');
            $(name).html('场馆名称：' + data['venue']['name']);
            $(price).html('租用价格：' + data['venue']['price'] + '元/天');
            $(raw).html('场馆排数：' + data['venue']['raw_num']);
            $(col).html('场馆列数：' + data['venue']['col_num']);
            $(address).html('场馆地址：' + data['venue']['address']);
            name.appendTo(body);
            price.appendTo(body);
            raw.appendTo(body);
            col.appendTo(body);
            address.appendTo(body);
            $('#venue-detail').modal('show');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function bookVenue() {
    var address_tmp = $('#venue-detail-body').find('.address').text().toString();
    var address = address_tmp.split('：')[1];
    var url = '\\venue\\book?address=' + encodeURI(address);
    window.open(url, '_self');
}