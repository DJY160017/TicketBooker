<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>TicketBooker</title>

    <link href="../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../css/reset.css" rel="stylesheet">
    <link href="../css/index.css" rel="stylesheet">
    <link href="../css/header.css" rel="stylesheet">
    <script src="../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../js/tool/bootstrap.js"></script>
    <script src="../js/logIn.js"></script>
    <script src="../js/index.js"></script>
</head>
<body>
<header>
    <%@ include file="header.jsp" %>
    <div class="banner-holder">
        <div class="banner-image-holder">
            <img alt="TicketBooker" src="../img/bg.jpg" draggable="false">
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
        <div class="music-card panel panel-default col-md-2 col-sm-offset-1 text-center" id="index-music-card"
             onmouseover="enlarge('index-music-card')" onmouseout="recover('index-music-card')" onclick="chooseProgram('音乐会')">
            <div class="panel-body">
                <img src="../img/music.jpg" class="img-circle" style="width: 80px;height: 80px">
            </div>
            <p>音乐会</p>
        </div>
        <div class="drama-card panel panel-default col-md-2 col-sm-offset-1 text-center" id="index-drama-card"
             onmouseover="enlarge('index-drama-card')" onmouseout="recover('index-drama-card')" onclick="chooseProgram('话剧')">
            <div class="panel-body">
                <img src="../img/drama.jpg" class="img-circle" style="width: 80px;height: 80px">
            </div>
            <p>话剧</p>
        </div>
        <div class="match-card panel panel-default col-md-2 col-sm-offset-1 text-center" id="index-match-card"
             onmouseover="enlarge('index-match-card')" onmouseout="recover('index-match-card')" onclick="chooseProgram('体育比赛')">
            <div class="panel-body">
                <img src="../img/match.jpg" class="img-circle" style="width: 80px;height: 80px">
            </div>
            <p>体育比赛</p>
        </div>
        <div class="comic-card panel panel-default col-md-2 col-sm-offset-1 text-center" id="index-comic-card"
             onmouseover="enlarge('index-comic-card')" onmouseout="recover('index-comic-card')" onclick="chooseProgram('动漫')">
            <div class="panel-body">
                <img src="../img/comic.jpg" class="img-circle" style="width: 80px;height: 80px">
            </div>
            <p>动漫</p>
        </div>
    </div>
</div>

<!-- 登录模态框（Modal） -->
<%@ include file="logIn.jsp" %>
</body>
</html>