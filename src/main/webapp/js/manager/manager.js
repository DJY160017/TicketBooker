function showVenueApplication() {
    $('#venue-application-verify').show();
    $('#venue-application-verify-a').parent().addClass("active");
    $('#venue-application-verify-a').css("color", "");
    $('#manager-settlement').hide();
    $('#manager-settlement-a').parent().removeClass("active");
    $('#manager-settlement-a').css("color", "#1b6d85");
    // $('#platform-data-center').hide();
    // $('#platform-data-center-a').parent().removeClass("active");
    // $('#platform-data-center-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_getUnVerifyVenue",
        success: function (data) {
            var size = parseInt(data['venues_size']);
            if (size !== 0) {
                $('#venue-application-verify-table').show();
                $('#miss-result-verify-id').hide();
                $("#venue-application-verify-table-body").empty();
                var body = document.getElementById('venue-application-verify-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr></tr>');
                    var venue_id = $('<td class="venue_id"></td>');
                    var venue_name = $('<td></td>');
                    var venue_price = $('<td></td>');
                    var venue_col = $('<td></td>');
                    var venue_raw = $('<td></td>');
                    var venue_address = $('<td></td>');
                    var venue_pass = $('<td><a href="javascript:void(0)" class="btn btn-success btn-sm" onclick="pass(this)">通过</a></td>');
                    var venue_refuse = $('<td><a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="refuse(this)">拒绝</a></td>');
                    $(venue_id).html(venueIDFormat(data['venues'][i]['venueID']));
                    $(venue_name).html(data['venues'][i]['name']);
                    $(venue_price).html(data['venues'][i]['price'] + '元/天');
                    $(venue_raw).html(data['venues'][i]['raw_num'] + '排');
                    $(venue_col).html(data['venues'][i]['col_num'] + '列');
                    $(venue_address).html(data['venues'][i]['address']);
                    venue_id.appendTo(tr);
                    venue_name.appendTo(tr);
                    venue_price.appendTo(tr);
                    venue_raw.appendTo(tr);
                    venue_col.appendTo(tr);
                    venue_address.appendTo(tr);
                    venue_refuse.appendTo(tr);
                    venue_pass.appendTo(tr);
                    tr.appendTo(body);
                }
            } else {
                $('#venue-application-verify-table').hide();
                $('#miss-result-verify-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showManagerSettlement() {
    $('#venue-application-verify').hide();
    $('#venue-application-verify-a').parent().removeClass("active");
    $('#venue-application-verify-a').css("color", "#1b6d85");
    $('#manager-settlement').show();
    $('#manager-settlement-a').parent().addClass("active");
    $('#manager-settlement-a').css("color", "");
    // $('#platform-data-center').hide();
    // $('#platform-data-center-a').parent().removeClass("active");
    // $('#platform-data-center-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_getUnSettlementRecord",
        success: function (data) {
            var size = parseInt(data['settlements_size']);
            if (size !== 0) {
                $('#manager-settlement-table').show();
                $('#miss-result-settlement-id').hide();
                $("#manager-settlement-table-body").empty();
                var body = document.getElementById('manager-settlement-table-body');
                for (var i = 0; i < size; i++) {
                    var tr = $('<tr></tr>');
                    var time = $('<td class="settlement_time"></td>');
                    var store_account = $('<td class="store_account"></td>');
                    var venue_account = $('<td class="venue_account"></td>');
                    var store_price = $('<td></td>');
                    var venue_price = $('<td></td>');
                    var settlement = $('<td><a href="javascript:void(0)" class="btn btn-primary btn-sm" onclick="settle(this)">结算</a></td>');
                    $(time).html(data['settlements'][i]['settlementID']['settlement_time']);
                    $(venue_account).html(data['settlements'][i]['settlementID']['venueAccount']);
                    $(venue_price).html(data['settlements'][i]['venueTotalPrice'] + '元');
                    $(store_account).html(data['settlements'][i]['settlementID']['storeAccount']);
                    $(store_price).html(data['settlements'][i]['storeTotalPrice'] + '元');
                    time.appendTo(tr);
                    venue_account.appendTo(tr);
                    venue_price.appendTo(tr);
                    store_account.appendTo(tr);
                    store_price.appendTo(tr);
                    settlement.appendTo(tr);
                    tr.appendTo(body);
                }
            } else {
                $('#manager-settlement-table').hide();
                $('#miss-result-settlement-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showPlatformDataCenter() {
    $('#venue-application-verify').hide();
    $('#venue-application-verify-a').parent().removeClass("active");
    $('#venue-application-verify-a').css("color", "#1b6d85");
    $('#manager-settlement').hide();
    $('#manager-settlement-a').parent().removeClass("active");
    $('#manager-settlement-a').css("color", "#1b6d85");
    $('#platform-data-center').show();
    $('#platform-data-center-a').parent().addClass("active");
    $('#platform-data-center-a').css("color", "");
    userDataCenter();
}

function userDataCenter() {
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/system/req_userDataCenter",
        success: function (data) {
            var level = [];
            var consume = [];
            for (var key in data['level']) {
                var item = [];
                item.push(key, data['level'][key]);
                level.push(item);
            }
            for (var index in data['consume']) {
                var money = [];
                money.push(index, data['consume'][index]);
                consume.push(money);
            }
            $('#users').html('已注册人数：' + data['total_users'] + '人');
            createDoughnutChart('consume-chart', consume, '收益');
            createCustmizedChart('member-chart', level, '人数');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function venueDataCenter() {
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/system/req_venueDataCenter",
        success: function (data) {
            var profit = [];
            for (var key in data['profit']) {
                if (key !== 'total_price') {
                    profit.push(data['profit'][key]);
                }
            }
            $('#venue-pass').html('已注册场馆：' + data['state']['已注册场馆']);
            $('#venue-unApproved').html('待审核场馆：' + data['state']['待审核场馆']);
            $('#venue-notThrough').html('未通过场馆：' + data['state']['未通过场馆']);
            $('#venue-total-price').html('场馆总收益：' + data['profit']['total_price'] + '元');
            createBarChart('venue-year-chart', profit);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function refuse(refuse) {
    var venueID = $(refuse).parents('tr').find('.venue_id').text();
    var state = encodeURI('未通过');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_verify",
        data: {
            venueID: venueID,
            state: state
        },
        success: function (data) {
            $(refuse).parents('tr').remove();
            if ($('#venue-application-verify-table-body').find('tr').length === 0) {
                $('#venue-application-verify-table').hide();
                $('#miss-result-verify-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function pass(pass) {
    var venueID = $(pass).parents('tr').find('.venue_id').text();
    var state = encodeURI('已通过');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_verify",
        data: {
            venueID: venueID,
            state: state
        },
        success: function (data) {
            $(pass).parents('tr').remove();
            if ($('#venue-application-verify-table-body').find('tr').length === 0) {
                $('#venue-application-verify-table').hide();
                $('#miss-result-verify-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function allPass() {
    var list = [];
    $('#venue-application-verify-table-body').find('tr').each(function () {
        list.push(venueIDDeFormat($(this).find('.venue_id').text()));
        $(this).remove();
    });
    $('#venue-application-verify-table').hide();
    $('#miss-result-verify-id').show();
    var needDate = JSON.stringify(list);
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_allPass",
        data: {
            id: needDate
        },
        success: function (data) {
            console.log(data);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function settle(record) {
    var time = $(record).parents('tr').find('.settlement_time').text();
    var store_account = $(record).parents('tr').find('.store_account').text();
    var venue_account = $(record).parents('tr').find('.venue_account').text();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_settle",
        data: {
            store_account: store_account,
            venue_account: venue_account,
            settlement_time: time
        },
        success: function (data) {
            console.log(data);
            $(record).parents('tr').remove();
            if ($('#manager-settlement-table-body').find('tr').length === 0) {
                $('#manager-settlement-table').hide();
                $('#miss-result-settlement-id').show();
            }
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function allSettle() {
    $('#manager-settlement-table-body').find('tr').each(function () {
        $(this).remove();
    });
    $('#manager-settlement-table').hide();
    $('#miss-result-settlement-id').show();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_allSettle",
        success: function (data) {
            console.log(data);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function venueIDFormat(venueID) {
    var result = venueID.toString();
    var length = 7 - result.length;
    for (var i = 0; i < length; i++) {
        result = '0' + result;
    }
    return result;
}

function venueIDDeFormat(venueID) {
    return parseInt(venueID.toString());
}

//数据格式：[['name1','1'],['name2','2']]
function createDoughnutChart(id, data, seriesTitle) {
    function splitPieData(rawData) {
        var categoryData = [];
        var values = [];

        for (var i = 0; i < rawData.length; i++) {
            var name = rawData[i].splice(0, 1)[0];
            var value = rawData[i].splice(0, 1)[0];
            categoryData.push(name);
            values.push({
                value: value,
                name: name
            });
        }
        return {
            categoryData: categoryData,
            values: values
        };
    }

    var pieData = splitPieData(data);
    var pieChart = echarts.init(document.getElementById(id));
    pieChart.showLoading();
    var option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        series: [
            {
                name: seriesTitle,
                type: 'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data: pieData.values
            }
        ]
    };
    pieChart.setOption(option);
    pieChart.hideLoading();
    return pieChart;
}

//数据格式：[['name1','1'],['name2','2']]
function createCustmizedChart(id, data, seriesTitle) {
    function splitPieData(rawData) {
        var values = [];
        for (var i = 0; i < rawData.length; i++) {
            var name = rawData[i].splice(0, 1)[0];
            var value = rawData[i].splice(0, 1)[0];
            values.push({
                value: value,
                name: name
            });
        }
        return {
            values: values
        };
    }

    var pieData = splitPieData(data);
    var pieChart = echarts.init(document.getElementById(id));
    pieChart.showLoading();
    var option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        calculable: true,
        series: [{
            name: seriesTitle,
            type: 'pie',
            radius: [30, 110],
            roseType: 'area',
            data: pieData.values
        }]
    };
    pieChart.setOption(option);
    pieChart.hideLoading();
    return pieChart;
}

//数据格式：[12,13,234]
function createBarChart(id, data) {
    function splitPieData(rawData) {
        var values = [];
        for (var i = 0; i < rawData.length; i++) {
            values.push(rawData[i]);
        }
        return {
            values: values
        };
    }

    var barData = splitPieData(data);
    var barChart = echarts.init(document.getElementById(id));
    barChart.showLoading();
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        calculable: true,
        xAxis: [{
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        }],
        yAxis: [{
            type: 'value'
        }],
        series: [{
            name: '收益',
            type: 'bar',
            data: barData.values,
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }]
    };
    barChart.setOption(option);
    barChart.hideLoading();
    return barChart;
}

