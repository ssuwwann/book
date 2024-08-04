<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="book.util.BookConst" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate var="currentDate" value="${now}" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="lDate" value="${info.dto.ldate}" pattern="yyyy-MM-dd"/>

<body>
<div class="book-container">
    <form name='bookForm' method="get">
        <input type="hidden" id="member" value="${member}"/>
        <c:choose>
            <c:when test="${book.bookCnt == book.totalBookLimit || currentDate > lDate}">
                <button id="fullyBtn" class="btn btn-warning" disabled>예약마감</button>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${statusBook == BookConst.LOGIN_NO || statusBook == BookConst.BOOk_NO}">
                        <button id="bookBtn" class="btn btn-warning">예약하기
                            (${book.bookCnt}/${book.totalBookLimit})
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button id="cancelBtn" class="btn btn-warning">예약취소</button>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </form>
</div>
</body>