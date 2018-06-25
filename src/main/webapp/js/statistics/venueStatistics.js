function showMarketCompare() {
    $('#market-compare-statistics').show();
    $('#market-compare-a').parent().addClass("active");
    $('#market-compare-a').css("color", "");
    $('#venue-price-statistics').hide();
    $('#venue-price-a').parent().removeClass("active");
    $('#venue-price-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_marketCompare",
        success: function (data) {
            var body = $('#market-compare-table-body');
            $(body).empty();
            for (var name in data) {
                var tr_average = $('<tr></tr>');
                var name_td = $('<td></td>');
                $(name_td).html(name);
                var venue = $('<td></td>');
                $(venue).html(data[name]['场馆']);
                var market = $('<td></td>');
                $(market).html(data[name]['细分市场']);
                var index = $('<td></td>');
                $(index).html(data[name]['指数']);
                $(tr_average).append(name_td);
                $(tr_average).append(venue);
                $(tr_average).append(market);
                $(tr_average).append(index);
                $(body).append(tr_average);
            }

            $('#market-compare-chart').remove();
            var new_chart_div = $('<div class="market-compare-chart col-md-offset-1 col-md-10 col-md-offset-1" id="market-compare-chart" style="height: 400px;"></div>');
            $('#market-compare-table').append(new_chart_div);
            createMarketChart('market-compare-chart', data);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showVenuePrice() {
    $('#market-compare-statistics').hide();
    $('#market-compare-a').parent().removeClass("active");
    $('#market-compare-a').css("color", "#1b6d85");
    $('#venue-price-statistics').show();
    $('#venue-price-a').parent().addClass("active");
    $('#venue-price-a').css("color", "");
    //取数据
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_venuePriceCompare",
        success: function (data) {
            var body = $('#average-price-table-body');
            $(body).empty();
            var tr_average = $('<tr></tr>');
            var averageAreaPrice = $('<td></td>');
            $(averageAreaPrice).html(data['相同地域平均价格']);
            var averageSizePrice = $('<td></td>');
            $(averageSizePrice).html(data['同等规模平均价格']);
            var averageSizeAreaPrice = $('<td></td>');
            $(averageSizeAreaPrice).html(data['维度组合平均价格']);
            $(tr_average).append(averageAreaPrice);
            $(tr_average).append(averageSizePrice);
            $(tr_average).append(averageSizeAreaPrice);
            $(body).append(tr_average);
        },
        error: function (result) {
            console.log(result);
        }
    });
    //预订率
    var unitTime = $('#show-unit-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_venueBookIndex",
        data: {
            unit_time: unitTime
        },
        success: function (data) {
            $('#book-index-chart-show').empty();
            var bookIndexChart = $('<div class="book-index-chart" id="book-index-chart" style="height: 400px;"></div>');
            $('#book-index-chart-show').append(bookIndexChart);
            createBookIndexChart('book-index-chart', data['result'], unitTime);
        },
        error: function (result) {
            console.log(result);
        }
    });
    //高峰时段数据
    var year = $('#show-year-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_venueTopBookIndex",
        data: {
            year: year
        },
        success: function (data) {
            console.log(data);
            $('#top-book-time-range-chart-show').empty();
            var topBookChart = $('<div class="top-book-time-range-chart" id="top-book-time-range-chart" style="height: 400px;"></div>');
            $('#top-book-time-range-chart-show').append(topBookChart);
            createScatterChart('top-book-time-range-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showByUnitTime(unit_time) {
    var unitTime = $(unit_time).text();
    $('#show-unit-time').html("");
    $('#show-unit-time').html(unitTime + '<span class="caret"></span>');
    //接下俩取数据更新chart
    //预订率
    var unitTime = $('#show-unit-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_venueBookIndex",
        data: {
            unit_time: unitTime
        },
        success: function (data) {
            $('#book-index-chart-show').empty();
            var bookIndexChart = $('<div class="book-index-chart" id="book-index-chart" style="height: 400px;"></div>');
            $('#book-index-chart-show').append(bookIndexChart);
            createBookIndexChart('book-index-chart', data['result'], unitTime);
        },
        error: function (result) {
            console.log(result);
        }
    });

}

function showByYear(year_show) {
    var unitTime = $(year_show).text();
    $('#show-year-time').html("");
    $('#show-year-time').html(unitTime + '<span class="caret"></span>');
    //接下俩取数据更新chart
    //高峰时段数据
    var year = $('#show-year-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/venue/req_venueTopBookIndex",
        data: {
            year: year
        },
        success: function (data) {
            $('#top-book-time-range-chart-show').empty();
            var topBookChart = $('<div class="top-book-time-range-chart" id="top-book-time-range-chart" style="height: 400px;"></div>');
            $('#top-book-time-range-chart-show').append(topBookChart);
            createScatterChart('top-book-time-range-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
}


function createMarketChart(id, data) {
    function splitData(rawData) {
        var values = [];
        var indicator = [];
        for (var name in rawData) {
            indicator.push({
                text: name,
                max: rawData[name]['细分市场']
            });
            values.push(rawData[name]['场馆']);
        }
        return {
            indicator: indicator,
            values: values
        };
    }

    var needData = splitData(data);
    console.log(needData);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        radar: [{
            indicator: needData.indicator,
            center: ['50%', '50%'],
            radius: 120
        }],
        series: [{
            tooltip: {
                trigger: 'item'
            },
            type: 'radar',
            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            data: [{
                value: needData.values,
                name: '场馆'
            }]
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function createBookIndexChart(id, data, unitTime) {
    function splitData(rawData) {
        var values = [];
        var categories = [];
        for (var index in rawData) {
            values.push(rawData[index]['data']);
            categories.push(rawData[index]['tag']);
        }
        return {
            categories: categories,
            values: values
        };
    }

    var needData = splitData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: [{
            text: '场馆预订率',
            left: 'center'
        }],
        tooltip: {
            trigger: 'axis',
            formatter: '{b}<br/>{a0}: {c0}%<br/>'
        },
        xAxis: {
            type: 'category',
            name: unitTime,
            data: needData.categories
        },
        yAxis: {
            type: 'value',
            name: '预订率',
            axisLabel: {
                formatter: '{value} %'
            }
        },
        series: [{
            name: unitTime + '预订率统计',
            type: 'line',
            data: needData.values,
            markPoint: {
                label: {
                    show: true,
                    formatter: '{c}%'
                },
                data: [{
                    type: 'max',
                    name: '最大值'
                }, {
                    type: 'min',
                    name: '最小值'
                }, {
                    coord: [41, 15],
                    name: '15',
                    value: 15
                }]
            },
            markLine: {
                data: [{
                    type: 'average',
                    name: '平均值'
                }]
            }
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function createScatterChart(id, data) {
    function splitData(rawData) {
        var values = [];
        var categoies = [];
        for (var index in rawData) {
            var value = [];
            categoies.push(rawData[index]['tag']);
            value.push(rawData[index]['tag'], rawData[index]['data']);
            values.push(value);
        }
        return {
            categoies: categoies,
            values: values
        };
    }

    var needData = splitData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: [{
            text: '预订高峰统计',
            left: 'center'
        }],
        xAxis: {
            type: 'category',
            scale: true,
            data: needData.categoies
        },
        yAxis: {
            type: 'value',
            scale: true
        },
        series: [{
            type: 'scatter',
            data: needData.values
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}