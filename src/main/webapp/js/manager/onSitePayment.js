function calculateSeat(seatID) {
    var id = seatID.split('_');
    var raw = id[0];
    var col = id[1];
    return raw.toString() + '排' + col.toString() + '座';
}

function onSiteSearchProgram() {
    var time = $('#program_start_date').val();
    var key = $('#search_key').val();
    if (time === '' || key === '') {
        $('#errorMessageField').html('输入信息不能为空');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/ticketBookerManager/req_getPrograms",
            data: {
                time: time,
                key: key
            },
            success: function (data) {
                var size = data['programs_size'];
                if (size !== 0) {
                    $('#program-show-info-body').show();
                    $('#miss-result-program-id').hide();
                    $('#program-info-table-body').empty();
                    var body = document.getElementById('program-info-table-body');
                    for (var i = 0; i < size; i++) {
                        var tr = $('<tr></tr>');
                        var time = $('<td class="programTime"></td>');
                        var programName = $('<td></td>');
                        var type = $('<td class="programType"></td>');
                        var address = $('<td class="programAddress"></td>');
                        var order = $('<td><a href="javascript:void(0)" class="btn btn-primary" onclick="readyChooseSeat(this)">购买</a></td>');
                        $(time).html(data['programs'][i]['programID']['reserve_time']);
                        $(type).html(data['programs'][i]['programType']);
                        $(programName).html(data['programs'][i]['name']);
                        $(address).html(data['programs'][i]['venueAddress']);
                        time.appendTo(tr);
                        programName.appendTo(tr);
                        type.appendTo(tr);
                        address.appendTo(tr);
                        order.appendTo(tr);
                        tr.appendTo(body);
                    }
                } else {
                    $('#program-show-info-body').hide();
                    $('#miss-result-program-id').show();
                }
            },
            error: function (result) {
                console.log(result)
            }
        });
    }
}

function getSeatPrice(col, raw) {
    var price;
    $.ajax({
        type: "post",
        dataType: 'json',
        async: false,
        url: "/ticket/req_getOneTicket",
        data: {
            col: col,
            raw: raw
        },
        success: function (data) {
            price = data['ticket']['price'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    return price;
}

function readyChooseSeat(program){
    $('#choose-seat').empty();
    var seat = document.getElementById('choose-seat');
    var code = $('<div class="seat-map col-md-6" id="seat-map"></div>\n' +
        '        <div class="order col-md-6" id="order-preview" hidden>\n' +
        '            <div class="order-price">\n' +
        '                <h1 class="order-price-p" id="order-price-detail">您的订单总价：0元</h1>\n' +
        '            </div>\n' +
        '            <div class="your-order">\n' +
        '                <table class="table table-hover" id="order-table" style="vertical-align: middle;text-align: center;">\n' +
        '                    <tbody id="order-table-body"></tbody>\n' +
        '                </table>\n' +
        '            </div>\n' +
        '            <div class="checkout-pay">\n' +
        '                <a href="javascript:void(0)" class="btn btn-primary" style=" width: 180px;height: 33px;" onclick="cancelPay()">取消</a>\n' +
        '                <a href="javascript:void(0)" class="btn btn-success" style=" width: 180px;height: 33px;" onclick="pay()">支付</a>\n' +
        '            </div>\n' +
        '        </div>');
    code.appendTo(seat);
    chooseSeat(program);
}

function chooseSeat(program) {
    var time = $(program).parents('tr').find('.programTime').text();
    var address = $(program).parents('tr').find('.programAddress').text();
    var type = $(program).parents('tr').find('.programType').text();
    var seat_map = [];
    $.ajax({
        type: "post",
        dataType: 'json',
        async: false,
        url: "/ticketBookerManager/req_getChooseInfo",
        data: {
            address: address,
            time: time,
            type: type
        },
        success: function (data) {
            seat_map = data['seatMap'];
        },
        error: function (result) {
            console.log(result);
        }
    });
    var firstLabel = 1;
    var sc = $('#seat-map').seatCharts({
        map: seat_map,
        seats: {
            a: {
                price: 40,
                classes: 'common-seat',
                category: '普通'
            }
        },
        naming: {
            top: false,
            getLabel: function (character, row, column) {
                return firstLabel++;
            }
        },
        click: function () {
            if (this.status() === 'available') {
                $('#order-preview').show();
                var detail_price = $('#order-price-detail').text().split('：');
                var total_price = parseFloat(detail_price[1].toString().replace('元', ''));
                var id = this.settings.id.split('_');
                var price = getSeatPrice(id[1], id[0]);
                total_price = total_price + price;
                $('#order-price-detail').html('您的订单总价：' + total_price + '元');
                var location = calculateSeat(this.settings.id);
                var seat_body_id = 'seat_body_' + this.settings.id;
                var seat_location_id = 'seat_location_' + this.settings.id;
                var seat_price_id = 'seat_price_' + this.settings.id;
                var seat_cancel_id = 'seat_cancel_' + this.settings.id;
                var seat_record = document.getElementById('order-table-body');
                var seat_tr = $('<tr></tr>');
                $(seat_tr).attr('id', seat_body_id).data('seatID', this.settings.id);
                var seat_location = $('<td></td>');
                $(seat_location).attr('id', seat_location_id);
                $(seat_location).html(location);
                var seat_price = $('<td></td>');
                $(seat_price).attr('id', seat_price_id);
                $(seat_price).html(price + '元');
                var seat_cancel = $('<td><a href="javascript:void(0)" class="cancel-cart-item">取消</a></td>');
                $(seat_cancel).attr('id', seat_cancel_id);
                seat_location.appendTo(seat_tr);
                seat_price.appendTo(seat_tr);
                seat_cancel.appendTo(seat_tr);
                seat_tr.appendTo(seat_record);
                return 'selected';
            } else if (this.status() === 'selected') {
                if (($('#order-table-body').find('tr').length - 1) === 0) {
                    $('#order-preview').hide();
                }
                $('#seat_body_' + this.settings.id).remove();
                var detail_price_1 = $('#order-price-detail').text().split('：');
                var total_price_1 = parseFloat(detail_price_1[1].toString().replace('元', ''));
                var id_1 = this.settings.id.split('_');
                var price_1 = getSeatPrice(id_1[1], id_1[0]);
                total_price_1 = total_price_1 - price_1;
                $('#order-price-detail').html('您的订单总价：' + total_price_1 + '元');
                return 'available';
            } else if (this.status() === 'unavailable') {
                return 'unavailable';
            } else {
                return this.style();
            }
        }
    });
    sc.find('u.available').status('unavailable');
    $('#order-table-body').on('click', '.cancel-cart-item', function () {
        sc.get($(this).parents('tr:first').data('seatID')).click();
    });
    $('#program-info-choose').hide();
    $('#choose-seat').show();
}

function cancelPay() {
    $('#program-info-choose').show();
    $('#choose-seat').hide();
}

function pay() {
    var seats = [];
    var tr_list = $('#order-table-body').find('tr');
    for (var i = 0; i < tr_list.length; i++) {
        var td_attr = tr_list.eq(i).children('td');
        var seat = td_attr.eq(0).html();
        seats.push(seat);
    }
    var total_price = $('#order-price-detail').html().toString().split('：')[1].replace('元', '');
    var need_seats = JSON.stringify(seats);
    $.ajax({
        type: "post",
        dataType: 'json',
        url: "/ticketBookerManager/req_pay",
        data: {
            total_price: total_price,
            seats: need_seats
        },
        success: function (data) {
            var message = data['result'].split(";");
            $('#pay-result-body').empty();
            var body = document.getElementById('pay-result-body');
            if (message[0] === '1') {
                var info = $('<h4>支付成功</h4>');
                info.appendTo(body);
                $('#program-info-choose').show();
                $('#choose-seat').hide();
            } else {
                var error = $('<h4></h4>');
                $(error).html('支付失败，' + message[1]);
                error.appendTo(body);
            }
            $('#payResult').modal('show');
        },
        error: function (result) {
            console.log(result);
        }
    });
}