<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/3
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员中心</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <link href="../../css/fonts/fontawesome/fontawesome-webfont.ttf">
    <link href="../../css/fonts/fontawesome/fontawesome-webfont.woff">
    <link href="../../css/fonts/fontawesome/fontawesome-webfont.woff2">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/header.css" rel="stylesheet">
    <link href="../../css/myProgress.css" rel="stylesheet">
    <link href="../../css/user/memberCenter.css" rel="stylesheet">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/logIn.js"></script>
    <script src="../../js/user/memberCenter.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/member/req_memberInfo",
                success: function (data) {
                    $('#current_level').html('当前等级：' + data['current_level']);
                    $('#marks-bar').css('width', data['percent']);
                    $('#current_marks').html(data['accumulate_level_marks'] + "分");
                    $('#next_level').html(data['next_level']);
                    $('#need_next_marks').html('距离' + data['next_level'] + '还需' + data['need_next_level_marks'] + '分');
                    $('#level_discount').html(data['current_level_discount'] + '折优惠');
                    $('#current-mark-id').html('积分兑换（现有'+data['current_level_marks']+'分）：');
                },
                error: function (result) {
                    console.log(result);
                }
            });
        });
    </script>
</head>
<body>
<header>
    <%@ include file="../header.jsp" %>
</header>
<div class="member-center container panel panel-default">
    <div class="level-now">
        <div class="level">
            <h3 id="current_level"></h3>
        </div>
        <div class="progress-combination level-marks-progress">
            <div class="progress">
                <div class="progress-bar" id="marks-bar" style="background:#d9534f;">
                    <span id="current_marks"></span>
                </div>
            </div>
        </div>
        <div class="next-level">
            <h3 id="next_level"></h3>
        </div>
        <div class="level-next-marks">
            <h5 id="need_next_marks"></h5>
        </div>
        <div class="level-discount">
            <h3>当前等级优惠:</h3>
            <h4 id="level_discount" style="margin-left: 45px"></h4>
        </div>
    </div>
    <div class="exchange-marks">
        <h3 id="current-mark-id"></h3>
        <div class="input-group input-group-sm col-md-3" style="margin-top:50px;margin-left: 45px;">
            <input class="form-control" type="text" id="marks" name="marks"
                   placeholder="请输入您的积分"
                   aria-describedby="sizing-addon1" required/>
            <span class="input-group-btn">
                        <button class="btn btn-default" id="mark-change" type="button" onclick="exchange()">兑换</button>
            </span>
        </div>
        <h1 class="warn-info"><span class="label label-danger" id="error-info"></span></h1>
    </div>
    <div class="cancelMember" style="position: absolute;top: 355px; left: 700px;">
        <h3>会员资格取消:</h3>
        <button data-toggle="modal" data-target="#member-cancel-show" class="btn btn-lg btn-success" style="margin-top: 30px;margin-left: 120px">取消</button>
    </div>
</div>
<div class="modal fade" id="member-cancel-show" tabindex="-1" role="dialog" aria-labelledby="memberCancelShowModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="memberCancelShowModalLabel">会员取消</h4>
            </div>
            <div class="modal-body text-center" id="account-show-content">
                <p>亲爱的用户，会员取消就无法恢复喔！（只能重新注册才能恢复）</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="cancelMember()" >取消会员</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="member-cancel-result" tabindex="-1" role="dialog" aria-labelledby="memberCancelResultModalLabel"
     aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="memberCancelResultModalLabel">会员取消</h4>
            </div>
            <div class="modal-body text-center" id="account-result-content">
                <p>取消成功</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="logout()" >确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="exchange-result-show" tabindex="-1" role="dialog" aria-labelledby="exchangeShowModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="exchangeShowModalLabel">兑换结果</h4>
            </div>
            <div class="modal-body text-center" id="exchange-show-content"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="../logIn.jsp" %>
</body>
</html>
