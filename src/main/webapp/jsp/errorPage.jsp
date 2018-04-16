<%--
  Created by IntelliJ IDEA.
  User: Byron Dong
  Date: 2018/2/10
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>error</title>

    <link href="../css/tool/bootstrap.css" rel="stylesheet">
    <link href="../css/reset.css" rel="stylesheet">
    <link href="../css/error.css" rel="stylesheet">
    <script src="../js/tool/jquery-3.2.1.min.js"></script>
    <script src="../js/tool/bootstrap.js"></script>
    <script src="../js/errorPage.js"></script>
</head>
<body>
<div class="main">
    <div class="sad-chicken">
        <img src="../img/sad.png" class="img-responsive" style="width: 260px;height: 280px">
    </div>
    <div class="return-button">
        <img src="../img/return.png" class="img-responsive" id="return-button-id" style="width: 200px;height: 90px"
             onmouseover="enlarge('return-button-id')" onmouseout="recover('return-button-id')" onclick="returnLastDocument()">
    </div>
</div>
<div class="error-message">
    <h3 id="error-info">${requestScope.message}</h3>
</div>
</body>
</html>
