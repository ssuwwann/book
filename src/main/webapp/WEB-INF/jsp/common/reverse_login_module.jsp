<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${!empty member}">
  <script>
    alert('가산땃쥐 잘못된 접근 같습니다만.. :0')
    location.href = '/'
  </script>
</c:if>