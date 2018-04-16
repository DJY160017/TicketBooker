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
            <li><a href="<c:url value="/ticketBookerManager/ticket/check"/>">检票登记</a></li>
            <li><a href="<c:url value="/ticketBookerManager/on-site/pay"/>">现场缴费</a></li>
            <li><a href="<c:url value="/ticketBookerManager/home"/>">管理中心</a></li>
            <li><a href="#" onclick="logout()">退出</a></li>
        </ul>
    </div>
</nav>
