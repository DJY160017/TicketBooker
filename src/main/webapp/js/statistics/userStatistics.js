function showConsumptionStatistics() {
    $('#my-consumption-statistics').show();
    $('#my-consumption-a').parent().addClass("active");
    $('#my-consumption-a').css("color", "");
    $('#my-favorite-statistics').hide();
    $('#my-favorite-a').parent().removeClass("active");
    $('#my-favorite-a').css("color", "#1b6d85");
    $('#my-program-statistics').hide();
    $('#my-program-a').parent().removeClass("active");
    $('#my-program-a').css("color", "#1b6d85");

    $('#show-year').html("");
    $('#show-year').html('2018<span class="caret"></span>');
    $('#show-unit-time').html("");
    $('#show-unit-time').html('月份<span class="caret"></span>');
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/user/req_memberConsumptionRange",
        success: function (data) {
            $('#low-consumption-price').text(data['range'][1]);
            $('#high-consumption-price').text(data['range'][0]);
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/user/req_memberConsumptionByUnitTime",
        data: {
            unit_time: '月份',
            year: '2018'
        },
        success: function (data) {
            $('#user-consumption-show-chart').remove();
            var new_chart_div = $('<div class="user-consumption-show-chart col-md-offset-1 col-md-10 col-md-offset-1" id="user-consumption-show-chart"></div>');
            $("#my-consumption-statistics").append(new_chart_div);

            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }
            createConsumptionChart('user-consumption-show-chart', myData, '2018年月份花销统计');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showFavorite() {
    $('#my-consumption-statistics').hide();
    $('#my-consumption-a').parent().removeClass("active");
    $('#my-consumption-a').css("color", "#1b6d85");
    $('#my-favorite-statistics').show();
    $('#my-favorite-a').parent().addClass("active");
    $('#my-favorite-a').css("color", "");
    $('#my-program-statistics').hide();
    $('#my-program-a').parent().removeClass("active");
    $('#my-program-a').css("color", "#1b6d85");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/user/req_memberFavorite",
        success: function (data) {
            $('#program-type-chart').remove();
            var new_program_chart_div = $('<div class="program-type-chart col-md-12" id="program-type-chart"></div>');
            $('#program-type-show').append(new_program_chart_div);
            var pty = data['pty'];
            var pty_must_key = '';
            var pty_must_num = -1;
            var pty_all = 0;
            for (var key in pty) {
                pty_all = pty_all + pty[key];
                if (pty[key] > pty_must_num) {
                    pty_must_key = key;
                    pty_must_num = pty[key];
                }
            }
            var pty_json = {
                all: pty_all,
                data: pty
            };
            createFavoriteChart('program-type-chart', pty_json, '节目类型详细统计', '最喜爱节目类型：' + pty_must_key + ' 订购次数：' + pty_must_num, '节目类型比例统计');

            $('#area-chart').remove();
            var new_area_chart_div = $('<div class="area-show-chart col-md-12" id="area-chart"></div>');
            $('#area-show').append(new_area_chart_div);
            var area = data['area'];
            var area_must_city = '';
            var area_must_num = -1;
            var area_all = 0;
            for (var city in area) {
                area_all = area_all + area[city];
                if (area[city] > area_must_num) {
                    area_must_city = city;
                    area_must_num = area[city];
                }
            }

            var area_json = {
                all: area_all,
                data: area
            };
            createFavoriteChart('area-chart', area_json, '地域详细统计', '最常订购区域：' + area_must_city + ' 订购次数：' + area_must_num, '地域比例统计');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function showProgram() {
    $('#my-consumption-statistics').hide();
    $('#my-consumption-a').parent().removeClass("active");
    $('#my-consumption-a').css("color", "#1b6d85");
    $('#my-favorite-statistics').hide();
    $('#my-favorite-a').parent().removeClass("active");
    $('#my-favorite-a').css("color", "#1b6d85");
    $('#my-program-statistics').show();
    $('#my-program-a').parent().addClass("active");
    $('#my-program-a').css("color", "");
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/publisher/req_programIncomeByUnitTime",
        data: {
            unit_time: '全部',
            unit_time_year: '2018'
        },
        success: function (data) {
            $('#program-income-chart-show').empty();
            var new_chart_div = $('<div class="program-income-chart" id="program-income-chart" style="height: 400px;"></div>');
            $("#program-income-chart-show").append(new_chart_div);

            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }
            createIncomeChart('program-income-chart', myData, '您的2018年详细节目收入统计');
        },
        error: function (result) {
            console.log(result);
        }
    });
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/publisher/req_seatOrderRate",
        success: function (data) {
            $('#seat-order-chart-show').empty();
            var new_chart_div = $('<div class="seat-order-chart" id="seat-order-chart" style="height: 400px;"></div>');
            $("#seat-order-chart-show").append(new_chart_div);
            createSeatRateChart('seat-order-chart', data['result']);
        },
        error: function (result) {
            console.log(result);
        }
    });

}


function showChartByUnitTime(unit_time) {
    var unitTime = $(unit_time).text();
    $('#show-unit-time').html("");
    $('#show-unit-time').html(unitTime + '<span class="caret"></span>');

    $('#all-type-btn').removeClass('btn-primary');
    $('#all-type-btn').addClass('btn-default');
    //接下俩取数据更新chart
    getConsumptionChartData();
}

function showChartByYear(year) {
    var year_time = $(year).text();
    $('#show-year').html("");
    $('#show-year').html(year_time + '<span class="caret"></span>');

    var unit_time = $('#show-unit-time').text();
    if (unit_time === '年份') {
        $('#show-unit-time').html("");
        $('#show-unit-time').html('全部<span class="caret"></span>');
    }
    $('#all-type-btn').removeClass('btn-primary');
    $('#all-type-btn').addClass('btn-default');
    //接下俩取数据更新chart
    getConsumptionChartData();
}

function showChartByAll() {
    $('#show-unit-time').html("");
    $('#show-unit-time').html('年份<span class="caret"></span>');
    $('#show-year').html("");
    $('#show-year').html('全部<span class="caret"></span>');

    $('#all-type-btn').removeClass('btn-default');
    $('#all-type-btn').addClass('btn-primary');
    var need_unitTime = "年份";
    var need_year = '全部';
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/user/req_memberConsumptionByUnitTime",
        data: {
            unit_time: need_unitTime,
            unit_time_year: need_year
        },
        success: function (data) {
            $('#user-consumption-show-chart').remove();
            var new_chart_div = $('<div class="user-consumption-show-chart col-md-offset-1 col-md-10 col-md-offset-1" id="user-consumption-show-chart"></div>');
            $("#my-consumption-statistics").append(new_chart_div);
            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }
            createConsumptionChart('user-consumption-show-chart', myData, '您的详细年份花销统计');
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function getConsumptionChartData() {
    var need_unitTime = $('#show-unit-time').text().trim();
    var need_year = $('#show-year').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/user/req_memberConsumptionByUnitTime",
        data: {
            unit_time: need_unitTime,
            unit_time_year: need_year
        },
        success: function (data) {
            $('#user-consumption-show-chart').remove();
            var new_chart_div = $('<div class="user-consumption-show-chart col-md-offset-1 col-md-10 col-md-offset-1" id="user-consumption-show-chart"></div>');
            $("#my-consumption-statistics").append(new_chart_div);
            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }

            var mine = '您的';
            var unit_year = '';
            var unit_time_unit = '';
            var end = '花销统计';
            if (need_unitTime === '全部') {
                unit_time_unit = '详细';
            } else {
                unit_time_unit = need_unitTime;
            }
            if (need_year === '全部') {
                unit_year = '详细';
            } else {
                unit_year = need_year + "年";
            }
            createConsumptionChart('user-consumption-show-chart', myData, mine + unit_year + unit_time_unit + end);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

//数据格式：[['name1','1'],['name2','2']]
function createConsumptionChart(id, data, seriesTitle) {
    function splitData(rawData) {
        var values = [];
        var categories = [];
        for (var i = 0; i < rawData.length; i++) {
            var name = rawData[i].splice(0, 1)[0];
            var value = rawData[i].splice(0, 1)[0];
            values.push(value);
            categories.push(name);
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
        title: {
            text: seriesTitle
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            boundaryGap: true,
            data: needData.categories,
            axisLine: {
                show: true
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: true,
            axisLabel: {
                formatter: '{value} 元'
            },
            min: function (value) {
                return value.min * 0.7;
            },
            max: function (value) {
                return value.max * 1.3;
            },
            axisLine: {
                show: true
            }
        },
        series: [
            {
                name: '消费总计',
                type: 'line',
                smooth: true,
                data: needData.values,
                markPoint: {
                    data: [
                        {type: 'max', name: '最大消费'},
                        {type: 'min', name: '最小消费'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

//数据格式：{"all":data1, "data":{"name1":data2,"name2":data3}}
function createFavoriteChart(id, json_data, title1, subTitle1, title2) {
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        tooltip: {},
        title: [{
            text: title1,
            subtext: subTitle1,
            x: '25%',
            textAlign: 'center'
        }, {
            text: title2,
            x: '80%',
            textAlign: 'center'
        }],
        grid: [{
            top: 60,
            width: '50%',
            bottom: '5%',
            left: 10,
            containLabel: true
        }],
        xAxis: [{
            type: 'value',
            max: json_data.all,
            splitLine: {
                show: false
            }
        }],
        yAxis: [{
            type: 'category',
            data: Object.keys(json_data.data),
            axisLabel: {
                interval: 0,
                rotate: 30
            },
            splitLine: {
                show: false
            }
        }],
        series: [{
            type: 'bar',
            stack: 'chart',
            z: 3,
            label: {
                normal: {
                    position: 'right',
                    show: true
                }
            },
            data: Object.keys(json_data.data).map(function (key) {
                return json_data.data[key];
            })
        }, {
            type: 'bar',
            stack: 'chart',
            silent: true,
            itemStyle: {
                normal: {
                    color: '#eee'
                }
            },
            data: Object.keys(json_data.data).map(function (key) {
                return json_data.all - json_data.data[key];
            })
        }, {
            type: 'pie',
            radius: [0, '40%'],
            center: ['80%', '45%'],
            data: Object.keys(json_data.data).map(function (key) {
                return {
                    name: key,
                    value: json_data.data[key]
                }
            })
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

//---------------------------------------------------------------------------------------------------
function showChartByUnitTimeForIncome(unit_time) {
    var unitTime = $(unit_time).text();
    $('#program-show-unit-time').html("");
    $('#program-show-unit-time').html(unitTime + '<span class="caret"></span>');

    $('#program-all-type-btn').removeClass('btn-primary');
    $('#program-all-type-btn').addClass('btn-default');
    //接下俩取数据更新chart
    getIncomeChartData();
}

function showChartByYearForIncome(year) {
    var year_time = $(year).text();
    $('#program-show-year').html("");
    $('#program-show-year').html(year_time + '<span class="caret"></span>');

    var unit_time = $('#program-show-unit-time').text();
    if (unit_time === '年份') {
        $('#program-show-unit-time').html("");
        $('#program-show-unit-time').html('全部<span class="caret"></span>');
    }
    $('#program-all-type-btn').removeClass('btn-primary');
    $('#program-all-type-btn').addClass('btn-default');
    //接下俩取数据更新chart
    getIncomeChartData();
}

function showChartByAllForIncome() {
    $('#program-show-unit-time').html("");
    $('#program-show-unit-time').html('年份<span class="caret"></span>');
    $('#program-show-year').html("");
    $('#program-show-year').html('全部<span class="caret"></span>');

    $('#program-all-type-btn').removeClass('btn-default');
    $('#program-all-type-btn').addClass('btn-primary');
    var need_unitTime = "年份";
    var need_year = '全部';
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/publisher/req_programIncomeByUnitTime",
        data: {
            unit_time: need_unitTime,
            unit_time_year: need_year
        },
        success: function (data) {
            $('#program-income-chart').remove();
            var new_chart_div = $('<div class="program-chart col-md-offset-1 col-md-10 col-md-offset-1" id="program-income-chart" style=" margin-top: 15px;height: 400px;"></div>');
            $("#my-program-statistics").append(new_chart_div);
            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }

            var mine = '您的节目';
            var unit_year = '';
            var unit_time_unit = '';
            var end = '收入统计';
            if (need_unitTime === '全部') {
                unit_time_unit = '详细';
            } else {
                unit_time_unit = need_unitTime;
            }
            if (need_year === '全部') {
                unit_year = '详细';
            } else {
                unit_year = need_year + "年";
            }
            createIncomeChart('program-income-chart', myData, mine + unit_year + unit_time_unit + end);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

function getIncomeChartData() {
    var need_unitTime = $('#program-show-unit-time').text().trim();
    var need_year = $('#program-show-year').text().trim();
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/statistics/publisher/req_programIncomeByUnitTime",
        data: {
            unit_time: need_unitTime,
            unit_time_year: need_year
        },
        success: function (data) {
            $('#program-income-chart').remove();
            var new_chart_div = $('<div class="program-chart col-md-offset-1 col-md-10 col-md-offset-1" id="program-income-chart" style=" margin-top: 15px;height: 400px;"></div>');
            $("#my-program-statistics").append(new_chart_div);
            var myData = [];
            for (var index in data['result']) {
                var item = [];
                item.push(data['result'][index]['tag']);
                item.push(data['result'][index]['data']);
                myData.push(item);
            }

            var mine = '您的节目';
            var unit_year = '';
            var unit_time_unit = '';
            var end = '收入统计';
            if (need_unitTime === '全部') {
                unit_time_unit = '详细';
            } else {
                unit_time_unit = need_unitTime;
            }
            if (need_year === '全部') {
                unit_year = '详细';
            } else {
                unit_year = need_year + "年";
            }
            createIncomeChart('program-income-chart', myData, mine + unit_year + unit_time_unit + end);
        },
        error: function (result) {
            console.log(result);
        }
    });
}

//数据格式：[['name1','1'],['name2','2']]
function createIncomeChart(id, data, seriesTitle) {
    function splitData(rawData) {
        var values = [];
        var categories = [];
        for (var i = 0; i < rawData.length; i++) {
            var name = rawData[i].splice(0, 1)[0];
            var value = rawData[i].splice(0, 1)[0];
            values.push(value);
            categories.push(name);
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
        title: {
            text: seriesTitle
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            boundaryGap: true,
            data: needData.categories,
            axisLine: {
                show: true
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: true,
            axisLabel: {
                formatter: '{value} 元'
            },
            min: function (value) {
                return value.min * 0.7;
            },
            max: function (value) {
                return value.max * 1.3;
            },
            axisLine: {
                show: true
            }
        },
        series: [
            {
                name: '收入总计',
                type: 'line',
                smooth: true,
                data: needData.values,
                markPoint: {
                    data: [
                        {type: 'max', name: '最大收入'},
                        {type: 'min', name: '最小收入'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}

function createSeatRateChart(id, data) {
    function splitData(rawData) {
        var values = [];
        var reverse_values = [];
        var categories = [];
        for (var index in rawData) {
            var name = rawData[index]['tag'];
            var value = rawData[index]['data'];
            var reverse_value = 1 - value;
            reverse_values.push(reverse_value.toFixed(3));
            values.push(value);
            categories.push(name.split('T')[0]);
        }
        return {
            categories: categories,
            values: values,
            reverse_values: reverse_values
        };
    }

    var needData = splitData(data);
    var chart = echarts.init(document.getElementById(id));
    chart.showLoading();
    var option = {
        title: {
            text: "节目上座率统计",
            x: "4%"
        },
        tooltip: {
            trigger: "axis",
            axisPointer: {
                type: "shadow",
                textStyle: {
                    color: "#fff"
                }

            }
        },
        grid: {
            borderWidth: 0,
            top: 110,
            bottom: 95,
            textStyle: {
                color: "#fff"
            }
        },
        legend: {
            x: '4%',
            top: '11%',
            textStyle: {
                color: '#90979c'
            },
            data: ['上座率', '缺失率']
        },
        calculable: true,
        xAxis: [{
            type: "category",
            splitLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            splitArea: {
                show: false
            },
            axisLabel: {
                interval: 0
            },
            data: needData.categories
        }],
        yAxis: [{
            type: "value",
            splitLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                interval: 0

            },
            splitArea: {
                show: false
            }
        }],
        dataZoom: [{
            show: true,
            height: 30,
            xAxisIndex: [
                0
            ],
            bottom: 30,
            start: 70,
            end: 80,
            handleSize: '110%',
            handleStyle: {
                color: "#d3dee5"
            },
            borderColor: "#90979c"
        }, {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35
        }],
        series: [{
            name: "上座率",
            type: "bar",
            stack: "总量",
            barMaxWidth: 35,
            barGap: "10%",
            itemStyle: {
                normal: {
                    color: "rgba(255,144,128,1)",
                    label: {
                        show: true,
                        textStyle: {
                            color: "#fff"
                        },
                        position: "insideTop",
                        formatter: function (p) {
                            return p.value > 0 ? (p.value) : '';
                        }
                    }
                }
            },
            data: needData.values
        }, {
            name: "缺失率",
            type: "bar",
            stack: "总量",
            itemStyle: {
                normal: {
                    color: "rgba(0,191,183,1)",
                    barBorderRadius: 0,
                    label: {
                        show: true,
                        position: "top",
                        formatter: function (p) {
                            return p.value > 0 ? (p.value) : '';
                        }
                    }
                }
            },
            data: needData.reverse_values
        }, {
            name: "上座率展示",
            type: "line",
            stack: "总量",
            symbolSize: 10,
            symbol: 'circle',
            itemStyle: {
                normal: {
                    color: "rgba(252,230,48,1)",
                    barBorderRadius: 0,
                    label: {
                        show: true,
                        position: "top",
                        formatter: function (p) {
                            return p.value > 0 ? (p.value) : '';
                        }
                    }
                }
            },
            data: needData.values
        }]
    };
    chart.setOption(option);
    chart.hideLoading();
    return chart;
}