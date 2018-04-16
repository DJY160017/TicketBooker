<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default nav-wrapper navbar-fixed-top">
    <div class="container">
        <div class="navbar-header" style="margin-top: -40px">
            <a class="navbar-brand brand" href="#">
                <figure style="width: 200px;height:60px;z-index: 3">
                    <img class="img-responsive " src="../../img/logo.png">
                </figure>
            </a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a id="homePage" href="<c:url value="/"/>">首页</a></li>
            <li><a id="member" href="<c:url value="/member/center"/>" style="cursor: pointer">会员中心</a></li>
            <li><a id="venue" href="${pageContext.request.contextPath}/venue/home" style="cursor: pointer">场馆</a></li>
            <c:choose>
                <c:when test="${sessionScope.user_mail != null}">
                    <li><a href="<c:url value="/userInfoManage"/>">用户管理</a></li>
                    <li><a href="#" onclick="logout()">退出</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="#" data-toggle="modal" data-target="#login">登录</a></li>
                    <li><a href="#" data-toggle="modal" data-target="#register">注册</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
