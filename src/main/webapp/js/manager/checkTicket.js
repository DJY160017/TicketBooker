function ticketCheck() {
    var time = $('#program_start_date').val();
    var venueID = $('#venue_id').val();
    var raw = $('#venue_raw').val();
    var col = $('#venue_col').val();
    if (time === '' || venueID === '' || raw === '' || col === '') {
        $('#errorMessageField').html('输入信息不能为空');
    } else {
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/ticketBookerManager/req_checkTicket",
            data: {
                time: time,
                venueID: venueID,
                raw: raw,
                col: col
            },
            success: function (data) {
                var message = data['result'].split(';');
                if (message[0] === '1') {
                    $('#venue_raw').val('');
                    $('#venue_col').val('');
                    $('#checkResult').modal('show');
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