<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/common/header.css"
/>
<header class="max-w-100 position-sticky top-0 z-1">
  <%--로고--%>
  <div class="w-100">
    <a href="/">보령엄마</a>
  </div>

  <%--검색--%>
  <nav class="search-form">
    <form>
      <div class="search-input">
        <input class="search-bar" type="text" placeholder="학원을 검색해주세요.">
        <span class="search-image--icon">
        <img src="${pageContext.request.contextPath}/resources/imgs/search_icon.png" alt="검색이미지">
        </span>
      </div>
    </form>
  </nav>
</header>
