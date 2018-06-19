function conditionSelect(condition, venueKey, sizeKey, typeKey) {
    var text = $(condition).text();
    $('#select-condition').html("");
    $('#select-condition').html(text + '<span class="caret"></span>');
    getSeatPriceData(venueKey, sizeKey, typeKey);
}

function getSeatPriceData(venueKey, sizeKey, typeKey) {
    //取数据
    var address = getRequestParam('address');
    var type_button = getRequestParam('type');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/publisher/req_seatPriceDecide",
        data: {
            address: address,
            type: type_button,
            venue_key: venueKey,
            size_key: sizeKey,
            type_key: typeKey
        },
        success: function (data) {
            $('#seat-price-decide-chart').remove();
            var chart_show = $('<div class="seat-price-decide-show-chart col-md-offset-1 col-md-10 col-md-offset-1" id="seat-price-decide-chart"></div>');
            $('#seat-price-decide-content').append(chart_show);
            createSeatPriceChart('seat-price-decide-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
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

function createSeatPriceChart(id, data) {
    function splitData(rawData) {
        var values_low = [];
        var values_high = [];
        var categories = [];
        for (var index in rawData) {
            categories.push(index);
            values_low.push(rawData[index][0]);
            values_high.push(rawData[index][1]);
        }
        return {
            categories: categories,
            values_low: values_low,
            values_high: values_high
        };
    }

    var needData = splitData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            text: '座位各类型价格区间'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['最高价', '最低价']
        },
        calculable: true,
        xAxis: [{
            type: 'category',
            data: needData.categories
        }],
        yAxis: [{
            type: 'value'
        }],
        series: [{
            name: '最高价',
            type: 'bar',
            data: needData.values_high,
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
        }, {
            name: '最低价',
            type: 'bar',
            data: needData.values_low,
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
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function returnVenueBook(){
    window.location.href=document.referrer;
}