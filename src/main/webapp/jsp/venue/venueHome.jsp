<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>场馆</title>

    <link href="../../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../../css/reset.css" rel="stylesheet">
    <link href="../../css/venue/header.css" rel="stylesheet">
    <link href="../../css/venue/venueHome.css" rel="stylesheet">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.ttf">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff">
    <link href="../../css/fonts/bootstrap/glyphicons-halflings-regular.woff2">
    <script src="../../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../../js/tool/bootstrap.js"></script>
    <script src="../../js/venue/venueLogIn.js"></script>
    <script src="../../js/venue/venueHome.js"></script>
</head>
<body>
<header>
    <%@ include file="../venue/header.jsp" %>
    <div class="banner-holder">
        <div class="banner-image-holder">
            <img alt="TicketBooker" src="../../img/bg.jpg" draggable="false">
        </div>
        <div class="jumbotron banner-desc">
            <div class="container text-center">
                <h1>TicketBooker,这有您想要的</h1>
            </div>
        </div>
    </div>
</header>

<div class="content">
    <div class="card">
        <div class="application-card panel panel-default col-md-3 col-sm-offset-1 text-center"
             id="venueHome-application-card"
             onmouseover="enlarge('venueHome-application-card')" onmouseout="recover('venueHome-application-card')"
             onclick="apply()">
            <div class="panel-body">
                <span class="venue-application glyphicon glyphicon-send"></span>
            </div>
            <p>场馆申请</p>
        </div>
        <div class="add-card panel panel-default col-md-3 col-sm-offset-1 text-center" id="venueHome-add-card"
             onmouseover="enlarge('venueHome-add-card')" onmouseout="recover('venueHome-add-card')" onclick="add()">
            <div class="panel-body">
                <span class="venue-add glyphicon glyphicon-plus"></span>
            </div>
            <p>场馆租用</p>
        </div>
        <div class="plan-card panel panel-default col-md-3 col-sm-offset-1 text-center" id="venueHome-plan-card"
             onmouseover="enlarge('venueHome-plan-card')" onmouseout="recover('venueHome-plan-card')" onclick="plan()">
            <div class="panel-body">
                <span class="venue-plan glyphicon glyphicon-align-justify"></span>
            </div>
            <p>场馆计划</p>
        </div>
    </div>
</div>

<!-- 登录模态框（Modal） -->
<%@ include file="../venue/venueLogin.jsp" %>
</body>
</html>