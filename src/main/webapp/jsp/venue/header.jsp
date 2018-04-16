<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default nav-wrapper navbar-fixed-top">
    <div class="container">
        <div class="navbar-header" style="margin-top: -40px">
            <a class="navbar-brand brand" href="#">
                <figure  style="width: 200px;height:60px;z-index: 3">
                    <img class="img-responsive " src="../../img/logo.png">
                </figure>
            </a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a id="homePage" href="<c:url value="/"/>">订票首页</a></li>
            <li><a id="venueHomePage"  style="cursor: pointer" href="<c:url value="/venue/home"/>">场馆首页</a></li>
            <c:choose>
                <c:when test="${sessionScope.venueID!=null}">
                    <li><a href="<c:url value="/venue/venueInfoManage"/>">场馆管理</a></li>
                    <li><a onclick="logout()">退出</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="#" data-toggle="modal" data-target="#login">场馆登录</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
