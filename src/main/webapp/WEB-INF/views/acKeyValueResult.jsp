<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
<c:forEach items="${result}" var="item">
<li>${item.value}<span class="informal"> <spring:message code="value.usage" arguments="${item.used}"></spring:message></span></li>
</c:forEach>
</ul>