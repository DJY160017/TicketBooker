function showProfit() {
    $('#profit-statistics').show();
    $('#profit-a').parent().addClass("active");
    $('#profit-a').css("color", "");
    $('#management-statistics').hide();
    $('#management-a').parent().removeClass("active");
    $('#management-a').css("color", "#1b6d85");
    $('#venue-statistics').hide();
    $('#venue-a').parent().removeClass("active");
    $('#venue-a').css("color", "#1b6d85");
    $('#member-statistics').hide();
    $('#member-a').parent().removeClass("active");
    $('#member-a').css("color", "#1b6d85");
    //取数据
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemVenueProfit",
        data: {
            year: 2018
        },
        success: function (data) {
            $('#profit-venue-show-year').html('2018<span class="caret"></span>');
            $('#profit-venue-show').empty();
            var new_chart_venue = $('<div class="profit-venue-show-chart" id="profit-venue-show-chart"></div>');
            $('#profit-venue-show').append(new_chart_venue);
            createProfitChart('profit-venue-show-chart', data['month_profit'], data['quarter_profit'], '2018', '场馆');
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemProgramProfit",
        data: {
            year: 2018
        },
        success: function (data) {
            $('#profit-program-show-year').html('2018<span class="caret"></span>');
            $('#profit-program-show').empty();
            var new_chart_program = $('<div class="profit-program-show-chart" id="profit-program-show-chart"></div>');
            $('#profit-program-show').append(new_chart_program);
            createProfitChart('profit-program-show-chart', data['month_profit'], data['quarter_profit'], '2018', '节目');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showManagement() {
    $('#profit-statistics').hide();
    $('#profit-a').parent().removeClass("active");
    $('#profit-a').css("color", "#1b6d85");
    $('#management-statistics').show();
    $('#management-a').parent().addClass("active");
    $('#management-a').css("color", "");
    $('#venue-statistics').hide();
    $('#venue-a').parent().removeClass("active");
    $('#venue-a').css("color", "#1b6d85");
    $('#member-statistics').hide();
    $('#member-a').parent().removeClass("active");
    $('#member-a').css("color", "#1b6d85");
    //取数据
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemTurnover",
        success: function (data) {
            $('#turnover-table-body').empty();
            var body = $('#turnover-table-body');
            var tr = $('<tr></tr>');
            var td_2018 = $('<td></td>');
            $(td_2018).html(data['result'][0]);
            var td_2017 = $('<td></td>');
            $(td_2017).html(data['result'][1]);
            var td_2016 = $('<td></td>');
            $(td_2016).html(data['result'][2]);
            tr.append(td_2018);
            tr.append(td_2017);
            tr.append(td_2016);
            body.append(tr);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemVenueAverageIncome",
        data: {
            unit_time: '月份',
            year: '2018'
        },
        success: function (data) {
            $('#venue-income-show-year').html('2018<span class="caret"></span>');
            $('#venue-income-show-unit-time').html('月份<span class="caret"></span>');
            $('#venue-income-show').empty();
            var new_chart_venue = $('<div class="venue-income-show-chart" id="venue-income-show-chart" style="height: 400px;"></div>');
            $('#venue-income-show').append(new_chart_venue);
            createIncomeChart('venue-income-show-chart', data['profit'], '场馆');
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemProgramAverageIncome",
        data: {
            unit_time: '月份',
            year: '2018'
        },
        success: function (data) {
            $('#program-income-show-year').html('2018<span class="caret"></span>');
            $('#program-income-show-unit-time').html('月份<span class="caret"></span>');
            $('#program-income-show').empty();
            var new_chart_program = $('<div class="program-income-show-chart" id="program-income-show-chart" style="height: 400px"></div>');
            $('#program-income-show').append(new_chart_program);
            createIncomeChart('program-income-show-chart', data['profit'], '节目');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showVenue() {
    $('#profit-statistics').hide();
    $('#profit-a').parent().removeClass("active");
    $('#profit-a').css("color", "#1b6d85");
    $('#management-statistics').hide();
    $('#management-a').parent().removeClass("active");
    $('#management-a').css("color", "#1b6d85");
    $('#venue-statistics').show();
    $('#venue-a').parent().addClass("active");
    $('#venue-a').css("color", "");
    $('#member-statistics').hide();
    $('#member-a').parent().removeClass("active");
    $('#member-a').css("color", "#1b6d85");
    //取数据
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

            $('#venue-now-year').empty();
            var new_now_chart = $('<div id="venue-year-chart" style="height: 400px"></div>');
            $('#venue-now-year').append(new_now_chart);
            createBarChart('venue-year-chart', profit);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_venueProfitArea",
        success: function (data) {
            $('#venue-area-show').empty();
            var new_venue_area_chart = $('<div class="venue-area-show-chart " id="venue-area-show-chart" style="height: 400px;"></div>');
            $('#venue-area-show').append(new_venue_area_chart);
            createVenueAreaChart('venue-area-show-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_venueProfitSize",
        success: function (data) {
            $('#venue-size-show').empty();
            var new_venue_size_chart = $('<div class="venue-size-show-chart" id="venue-size-show-chart" style=" height: 400px;"></div>');
            $('#venue-size-show').append(new_venue_size_chart);
            createVenueSizeChart('venue-size-show-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_venueProfitProgram",
        success: function (data) {
            $('#venue-program-show').empty();
            var new_venue_size_chart = $('<div class="venue-program-show-chart" id="venue-program-show-chart" style=" height: 400px;"></div>');
            $('#venue-program-show').append(new_venue_size_chart);
            createVenueProgramChart('venue-program-show-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showMember() {
    $('#profit-statistics').hide();
    $('#profit-a').parent().removeClass("active");
    $('#profit-a').css("color", "#1b6d85");
    $('#management-statistics').hide();
    $('#management-a').parent().removeClass("active");
    $('#management-a').css("color", "#1b6d85");
    $('#venue-statistics').hide();
    $('#venue-a').parent().removeClass("active");
    $('#venue-a').css("color", "#1b6d85");
    $('#member-statistics').show();
    $('#member-a').parent().addClass("active");
    $('#member-a').css("color", "");
    //取数据
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_activeMemberArea",
        success: function (data) {
            $('#member-area-show').empty();
            var new_member_area_chart = $('<div class="member-area-show-chart" id="member-area-show-chart" style="height:90%;"></div>');
            $('#member-area-show').append(new_member_area_chart);
            createMemberArea('member-area-show-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

//------------------------------------------------------------------------------------------
function showProfitVenueShowByYear(year) {
    var real_year = $(year).text().trim();
    $('#profit-venue-show-year').html("");
    $('#profit-venue-show-year').html(real_year + '<span class="caret"></span>');
    //接下俩取数据更新chart
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemVenueProfit",
        data: {
            year: real_year
        },
        success: function (data) {
            $('#profit-venue-show').empty();
            var new_chart_venue = $('<div class="profit-venue-show-chart" id="profit-venue-show-chart"></div>');
            $('#profit-venue-show').append(new_chart_venue);
            createProfitChart('profit-venue-show-chart', data['month_profit'], data['quarter_profit'], real_year, '场馆');
        },
        error: function (result) {
            console.log(result);
        }
    });

}

function showProfitProgramShowByYear(year) {
    var real_year = $(year).text().trim();
    $('#profit-program-show-year').html("");
    $('#profit-program-show-year').html(real_year + '<span class="caret"></span>');
    //接下俩取数据更新chart
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemProgramProfit",
        data: {
            year: real_year
        },
        success: function (data) {
            $('#profit-program-show').empty();
            var new_chart_program = $('<div class="profit-program-show-chart" id="profit-program-show-chart"></div>');
            $('#profit-program-show').append(new_chart_program);
            createProfitChart('profit-program-show-chart', data['month_profit'], data['quarter_profit'], real_year, '节目');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

//数据格式：[['name1','1'],['name2','2']]
function createProfitChart(id, data1, data2, year, type) {
    function splitData(rawData) {
        var values = [];
        var categories = [];
        var max_value = 0;
        for (var index in rawData) {
            values.push(rawData[index]['data']);
            categories.push(rawData[index]['tag']);
            if (rawData[index]['data'] > max_value) {
                max_value = rawData[index]['data'];
            }
        }
        return {
            max_value: max_value,
            categories: categories,
            values: values
        };
    }

    var needData1 = splitData(data1);
    var needData2 = splitData(data2);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        visualMap: [{
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: needData1.max_value
        }, {
            show: false,
            type: 'continuous',
            seriesIndex: 1,
            min: 0,
            max: needData2.max_value
        }],


        title: [{
            left: 'left',
            text: type + year + '年月份同比增长率'
        }, {
            top: '47%',
            left: 'left',
            text: type + year + '年季度同比增长率'
        }],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: [{
            data: needData1.categories
        }, {
            data: needData2.categories,
            gridIndex: 1
        }],
        yAxis: [{
            splitLine: {show: false}
        }, {
            splitLine: {show: false},
            gridIndex: 1
        }],
        grid: [{
            bottom: '60%'
        }, {
            top: '60%'
        }],
        series: [{
            type: 'line',
            showSymbol: false,
            data: needData1.values
        }, {
            type: 'line',
            showSymbol: false,
            data: needData2.values,
            xAxisIndex: 1,
            yAxisIndex: 1
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

//--------------------------------------------------------------------------------------------
function showVenueIncomeShowByYear(year) {
    var real_year = $(year).text().trim();
    $('#venue-income-show-year').html("");
    $('#venue-income-show-year').html(real_year + '<span class="caret"></span>');
    //接下俩取数据更新chart
    var unit_time = $('#venue-income-show-unit-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemVenueAverageIncome",
        data: {
            unit_time: unit_time,
            year: real_year
        },
        success: function (data) {
            $('#venue-income-show').empty();
            var new_chart_venue = $('<div class="venue-income-show-chart" id="venue-income-show-chart" style="height: 400px;"></div>');
            $('#venue-income-show').append(new_chart_venue);
            createIncomeChart('venue-income-show-chart', data['profit'], '场馆');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showProgramIncomeShowByYear(year) {
    var real_year = $(year).text().trim();
    $('#program-income-show-year').html("");
    $('#program-income-show-year').html(real_year + '<span class="caret"></span>');
    //接下俩取数据更新chart
    var unit_time = $('#program-income-show-unit-time').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemProgramAverageIncome",
        data: {
            unit_time: unit_time,
            year: real_year
        },
        success: function (data) {
            $('#program-income-show').empty();
            var new_chart_program = $('<div class="program-income-show-chart" id="program-income-show-chart" style="height: 400px"></div>');
            $('#program-income-show').append(new_chart_program);
            createIncomeChart('program-income-show-chart', data['profit'], '节目');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showVenueIncomeShowByUnitTime(unit_time) {
    var real_unit_time = $(unit_time).text().trim();
    $('#venue-income-show-unit-time').html("");
    $('#venue-income-show-unit-time').html(real_unit_time + '<span class="caret"></span>');
    //接下俩取数据更新chart
    var year = $('#venue-income-show-year').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemVenueAverageIncome",
        data: {
            unit_time: real_unit_time,
            year: year
        },
        success: function (data) {
            $('#venue-income-show').empty();
            var new_chart_venue = $('<div class="venue-income-show-chart" id="venue-income-show-chart" style="height: 400px;"></div>');
            $('#venue-income-show').append(new_chart_venue);
            createIncomeChart('venue-income-show-chart', data['profit'], '场馆');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showProgramIncomeShowByUnitTime(unit_time) {
    var real_unit_time = $(unit_time).text().trim();
    $('#program-income-show-unit-time').html("");
    $('#program-income-show-unit-time').html(real_unit_time + '<span class="caret"></span>');
    //接下俩取数据更新chart
    var year = $('#program-income-show-year').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/manager/req_systemProgramAverageIncome",
        data: {
            unit_time: real_unit_time,
            year: year
        },
        success: function (data) {
            $('#program-income-show').empty();
            var new_chart_program = $('<div class="program-income-show-chart" id="program-income-show-chart" style="height: 400px"></div>');
            $('#program-income-show').append(new_chart_program);
            createIncomeChart('program-income-show-chart', data['profit'], '节目');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

//数据格式：[['name1','1'],['name2','2']]
function createIncomeChart(id, data, type) {
    function splitData(rawData) {
        var average_values = [];
        var max_values = [];
        var categories = [];
        for (var index in rawData) {
            categories.push(rawData[index]['tag']);
            average_values.push(rawData[index]['data1']);
            max_values.push(rawData[index]['data2']);
        }
        return {
            categories: categories,
            average_values: average_values,
            max_values: max_values
        };
    }

    var needData = splitData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            left: 'left',
            text: type + '平均收入'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        xAxis: [{
            type: 'category',
            data: needData.categories,
            axisPointer: {
                type: 'shadow'
            }
        }],
        yAxis: [{
            type: 'value',
            name: '平均收入',
            axisLabel: {
                formatter: '{value}元'
            }
        }, {
            type: 'value',
            name: '最高收入',
            axisLabel: {
                formatter: '{value}元'
            }
        }],
        series: [{
            name: '平均收入',
            type: 'bar',
            data: needData.average_values
        }, {
            name: '最高收入',
            type: 'line',
            yAxisIndex: 1,
            data: needData.max_values
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

//-----------------------------------------------------------------------------------------------

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

function createVenueAreaChart(id, data) {
    function splitPieData(rawData) {
        var values = [];
        var categories = [];
        for (var index in rawData) {
            categories.push(rawData[index]['tag']);
            values.push(rawData[index]['data']);
        }
        return {
            values: values,
            categories: categories
        };
    }

    var needData = splitPieData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            text: '场馆效益与地域关系分析'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: needData.categories
        },
        series: [{
            type: 'bar',
            itemStyle: {
                color: '#2f4554'
            },
            data: needData.values
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function createVenueSizeChart(id, data) {
    function splitPieData(rawData) {
        var values = [];
        var names = [];
        for (var index in rawData) {
            names.push(data[index]['tag']);
            values.push({
                value: data[index]['data'],
                name: data[index]['tag']
            });
        }
        return {
            names: names,
            values: values
        };
    }

    var needData = splitPieData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            text: '场馆效益与规模关系分析',
            left: 'left',
            top: 30
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            x: 'center',
            data: needData.names
        },
        calculable: true,
        series: [{
            type: 'pie',
            radius: [30, 110],
            center: ['50%', '50%'],
            data: needData.values.sort(function (a, b) {
                return a.value - b.value;
            }),
            roseType: 'radius',
            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
                return Math.random() * 200;
            }
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function createVenueProgramChart(id, data) {
    function splitPieData(rawData) {
        var venue_values = [];
        var program_values = [];
        var categories = [];
        for (var index in rawData) {
            categories.push(rawData[index]['tag']);
            venue_values.push(rawData[index]['data1']);
            program_values.push(rawData[index]['data2']);
        }
        return {
            categorise: categories,
            venue_values: venue_values,
            program_values: program_values
        };
    }

    var needData = splitPieData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            text: '场馆效益与节目效益关系分析'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['场馆', '节目'],
            itemGap: 5
        },
        grid: {
            top: '12%',
            left: '1%',
            right: '10%',
            containLabel: true
        },
        calculable: true,
        xAxis: [{
            type: 'category',
            data: needData.categorise,
            axisPointer: {
                type: 'shadow'
            }
        }],
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: function (a) {
                    a = +a;
                    return isFinite(a)
                        ? echarts.format.addCommas(+a / 1000)
                        : '';
                }
            }
        }],
        dataZoom: [{
            show: true,
            start: 94,
            end: 100
        }, {
            type: 'inside',
            start: 94,
            end: 100
        }, {
            show: true,
            yAxisIndex: 0,
            filterMode: 'empty',
            width: 30,
            height: '80%',
            showDataShadow: false,
            left: '93%'
        }],
        series: [{
            name: '场馆',
            type: 'bar',
            data: needData.venue_values,
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
            name: '节目',
            type: 'bar',
            data: needData.program_values,
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

//------------------------------------------------------------------------------
function createMemberArea(id, data) {
    function splitPieData(rawData) {
        var values = [];
        for (var index in rawData) {
            values.push({
                name: rawData[index]['tag'],
                value: rawData[index]['data']
            });
        }
        return {
            values: values
        };
    }

    var geoCoordMap = {
        '海门': [121.15, 31.89],
        '鄂尔多斯': [109.781327, 39.608266],
        '招远': [120.38, 37.35],
        '舟山': [122.207216, 29.985295],
        '齐齐哈尔': [123.97, 47.33],
        '盐城': [120.13, 33.38],
        '赤峰': [118.87, 42.28],
        '青岛': [120.33, 36.07],
        '乳山': [121.52, 36.89],
        '金昌': [102.188043, 38.520089],
        '泉州': [118.58, 24.93],
        '莱西': [120.53, 36.86],
        '日照': [119.46, 35.42],
        '胶南': [119.97, 35.88],
        '南通': [121.05, 32.08],
        '拉萨': [91.11, 29.97],
        '云浮': [112.02, 22.93],
        '梅州': [116.1, 24.55],
        '文登': [122.05, 37.2],
        '上海': [121.48, 31.22],
        '攀枝花': [101.718637, 26.582347],
        '威海': [122.1, 37.5],
        '承德': [117.93, 40.97],
        '厦门': [118.1, 24.46],
        '汕尾': [115.375279, 22.786211],
        '潮州': [116.63, 23.68],
        '丹东': [124.37, 40.13],
        '太仓': [121.1, 31.45],
        '曲靖': [103.79, 25.51],
        '烟台': [121.39, 37.52],
        '福州': [119.3, 26.08],
        '瓦房店': [121.979603, 39.627114],
        '即墨': [120.45, 36.38],
        '抚顺': [123.97, 41.97],
        '玉溪': [102.52, 24.35],
        '张家口': [114.87, 40.82],
        '阳泉': [113.57, 37.85],
        '莱州': [119.942327, 37.177017],
        '湖州': [120.1, 30.86],
        '汕头': [116.69, 23.39],
        '昆山': [120.95, 31.39],
        '宁波': [121.56, 29.86],
        '湛江': [110.359377, 21.270708],
        '揭阳': [116.35, 23.55],
        '荣成': [122.41, 37.16],
        '连云港': [119.16, 34.59],
        '葫芦岛': [120.836932, 40.711052],
        '常熟': [120.74, 31.64],
        '东莞': [113.75, 23.04],
        '河源': [114.68, 23.73],
        '淮安': [119.15, 33.5],
        '泰州': [119.9, 32.49],
        '南宁': [108.33, 22.84],
        '营口': [122.18, 40.65],
        '惠州': [114.4, 23.09],
        '江阴': [120.26, 31.91],
        '蓬莱': [120.75, 37.8],
        '韶关': [113.62, 24.84],
        '嘉峪关': [98.289152, 39.77313],
        '广州': [113.23, 23.16],
        '延安': [109.47, 36.6],
        '太原': [112.53, 37.87],
        '清远': [113.01, 23.7],
        '中山': [113.38, 22.52],
        '昆明': [102.73, 25.04],
        '寿光': [118.73, 36.86],
        '盘锦': [122.070714, 41.119997],
        '长治': [113.08, 36.18],
        '深圳': [114.07, 22.62],
        '珠海': [113.52, 22.3],
        '宿迁': [118.3, 33.96],
        '咸阳': [108.72, 34.36],
        '铜川': [109.11, 35.09],
        '平度': [119.97, 36.77],
        '佛山': [113.11, 23.05],
        '海口': [110.35, 20.02],
        '江门': [113.06, 22.61],
        '章丘': [117.53, 36.72],
        '肇庆': [112.44, 23.05],
        '大连': [121.62, 38.92],
        '临汾': [111.5, 36.08],
        '吴江': [120.63, 31.16],
        '石嘴山': [106.39, 39.04],
        '沈阳': [123.38, 41.8],
        '苏州': [120.62, 31.32],
        '茂名': [110.88, 21.68],
        '嘉兴': [120.76, 30.77],
        '长春': [125.35, 43.88],
        '胶州': [120.03336, 36.264622],
        '银川': [106.27, 38.47],
        '张家港': [120.555821, 31.875428],
        '三门峡': [111.19, 34.76],
        '锦州': [121.15, 41.13],
        '南昌': [115.89, 28.68],
        '柳州': [109.4, 24.33],
        '三亚': [109.511909, 18.252847],
        '自贡': [104.778442, 29.33903],
        '吉林': [126.57, 43.87],
        '阳江': [111.95, 21.85],
        '泸州': [105.39, 28.91],
        '西宁': [101.74, 36.56],
        '宜宾': [104.56, 29.77],
        '呼和浩特': [111.65, 40.82],
        '成都': [104.06, 30.67],
        '大同': [113.3, 40.12],
        '镇江': [119.44, 32.2],
        '桂林': [110.28, 25.29],
        '张家界': [110.479191, 29.117096],
        '宜兴': [119.82, 31.36],
        '北海': [109.12, 21.49],
        '西安': [108.95, 34.27],
        '金坛': [119.56, 31.74],
        '东营': [118.49, 37.46],
        '牡丹江': [129.58, 44.6],
        '遵义': [106.9, 27.7],
        '绍兴': [120.58, 30.01],
        '扬州': [119.42, 32.39],
        '常州': [119.95, 31.79],
        '潍坊': [119.1, 36.62],
        '重庆': [106.54, 29.59],
        '台州': [121.420757, 28.656386],
        '南京': [118.78, 32.04],
        '滨州': [118.03, 37.36],
        '贵阳': [106.71, 26.57],
        '无锡': [120.29, 31.59],
        '本溪': [123.73, 41.3],
        '克拉玛依': [84.77, 45.59],
        '渭南': [109.5, 34.52],
        '马鞍山': [118.48, 31.56],
        '宝鸡': [107.15, 34.38],
        '焦作': [113.21, 35.24],
        '句容': [119.16, 31.95],
        '北京': [116.46, 39.92],
        '徐州': [117.2, 34.26],
        '衡水': [115.72, 37.72],
        '包头': [110, 40.58],
        '绵阳': [104.73, 31.48],
        '乌鲁木齐': [87.68, 43.77],
        '枣庄': [117.57, 34.86],
        '杭州': [120.19, 30.26],
        '淄博': [118.05, 36.78],
        '鞍山': [122.85, 41.12],
        '溧阳': [119.48, 31.43],
        '库尔勒': [86.06, 41.68],
        '安阳': [114.35, 36.1],
        '开封': [114.35, 34.79],
        '济南': [117, 36.65],
        '德阳': [104.37, 31.13],
        '温州': [120.65, 28.01],
        '九江': [115.97, 29.71],
        '邯郸': [114.47, 36.6],
        '临安': [119.72, 30.23],
        '兰州': [103.73, 36.03],
        '沧州': [116.83, 38.33],
        '临沂': [118.35, 35.05],
        '南充': [106.110698, 30.837793],
        '天津': [117.2, 39.13],
        '富阳': [119.95, 30.07],
        '泰安': [117.13, 36.18],
        '诸暨': [120.23, 29.71],
        '郑州': [113.65, 34.76],
        '哈尔滨': [126.63, 45.75],
        '聊城': [115.97, 36.45],
        '芜湖': [118.38, 31.33],
        '唐山': [118.02, 39.63],
        '平顶山': [113.29, 33.75],
        '邢台': [114.48, 37.05],
        '德州': [116.29, 37.45],
        '济宁': [116.59, 35.38],
        '荆州': [112.239741, 30.335165],
        '宜昌': [111.3, 30.7],
        '义乌': [120.06, 29.32],
        '丽水': [119.92, 28.45],
        '洛阳': [112.44, 34.7],
        '秦皇岛': [119.57, 39.95],
        '株洲': [113.16, 27.83],
        '石家庄': [114.48, 38.03],
        '莱芜': [117.67, 36.19],
        '常德': [111.69, 29.05],
        '保定': [115.48, 38.85],
        '湘潭': [112.91, 27.87],
        '金华': [119.64, 29.12],
        '岳阳': [113.09, 29.37],
        '长沙': [113, 28.21],
        '衢州': [118.88, 28.97],
        '廊坊': [116.7, 39.53],
        '菏泽': [115.480656, 35.23375],
        '合肥': [117.27, 31.86],
        '武汉': [114.31, 30.52],
        '大庆': [125.03, 46.58]
    };
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var needData = splitPieData(data);
    var convertData = function (data) {
        var res = [];
        for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
                res.push({
                    name: data[i].name,
                    value: geoCoord.concat(data[i].value)
                });
            }
        }
        return res;
    };
    var option = {
        title: {
            text: '活跃会员与地域关系分析',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        bmap: {
            center: [104.114129, 37.550339],
            zoom: 5,
            roam: true,
            mapStyle: {
                styleJson: [{
                    'featureType': 'water',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#d1d1d1'
                    }
                }, {
                    'featureType': 'land',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#f3f3f3'
                    }
                }, {
                    'featureType': 'railway',
                    'elementType': 'all',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'highway',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#fdfdfd'
                    }
                }, {
                    'featureType': 'highway',
                    'elementType': 'labels',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'arterial',
                    'elementType': 'geometry',
                    'stylers': {
                        'color': '#fefefe'
                    }
                }, {
                    'featureType': 'arterial',
                    'elementType': 'geometry.fill',
                    'stylers': {
                        'color': '#fefefe'
                    }
                }, {
                    'featureType': 'poi',
                    'elementType': 'all',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'green',
                    'elementType': 'all',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'subway',
                    'elementType': 'all',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'manmade',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#d1d1d1'
                    }
                }, {
                    'featureType': 'local',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#d1d1d1'
                    }
                }, {
                    'featureType': 'arterial',
                    'elementType': 'labels',
                    'stylers': {
                        'visibility': 'off'
                    }
                }, {
                    'featureType': 'boundary',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#fefefe'
                    }
                }, {
                    'featureType': 'building',
                    'elementType': 'all',
                    'stylers': {
                        'color': '#d1d1d1'
                    }
                }, {
                    'featureType': 'label',
                    'elementType': 'labels.text.fill',
                    'stylers': {
                        'color': '#999999'
                    }
                }]
            }
        },
        series: [{
            name: '活跃人数',
            type: 'scatter',
            coordinateSystem: 'bmap',
            data: convertData(needData.values),
            symbolSize: function (val) {
                if (val[2] > 1000) {
                    return 50;
                } else {
                    return val[2] / 50;
                }
            },
            label: {
                normal: {
                    formatter: '{b}',
                    position: 'right',
                    show: false
                },
                emphasis: {
                    show: true
                }
            },
            itemStyle: {
                normal: {
                    color: '#ddb926'
                }
            }
        }, {
            name: 'Top',
            type: 'effectScatter',
            coordinateSystem: 'bmap',
            data: convertData(needData.values.sort(function (a, b) {
                return b.value - a.value;
            }).slice(0, 5)),
            symbolSize: function (val) {
                if (val[2] > 1000) {
                    return 50;
                } else {
                    return val[2] / 50;
                }
            },
            showEffectOn: 'render',
            rippleEffect: {
                brushType: 'stroke'
            },
            hoverAnimation: true,
            label: {
                normal: {
                    formatter: '{b}',
                    position: 'right',
                    show: true
                }
            },
            itemStyle: {
                normal: {
                    color: '#f4e925',
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            zlevel: 1
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}