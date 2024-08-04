<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="review-wrapper">
  <div>
    <span>
      리뷰 (${review.average})
    <strong>${review.count}개</strong>
    </span>
    <button class="review-btn btn btn-primary">리뷰 작성</button>
  </div>

  <div class="review-conainer">
    <ul>
      <c:forEach var="review" items="${review.dto}">
        <li>
          <a href="javascript:void(0)" data-writer='${review.memberSeq}'>
            <div class="review-card-${review.reviewSeq}">
              <div class="review-card--header">
                <div class="review-item">
                  <img src="${pageContext.request.contextPath}/resources/imgs/별.png" alt="">
                  <span>${review.rate}</span>
                </div>
                <div class="review-item">정성리뷰</div>
              </div>
              <p class="review-card--content">
                <span class="title">${review.title}</span>
              </p>
              <ul class="review-card--info">
                <li><span class="">${review.nickname}</span></li>
                <li><span>${review.grade}</span></li>
                <li><span>${review.cdate}</span></li>
              </ul>
            </div>
          </a>
        </li>
      </c:forEach>
      <li>
        <button id="allReviewBtn">모든 리뷰</button>
      </li>
    </ul>
  </div>
</div>
<jsp:include page="/WEB-INF/jsp/board/modal.jsp"/>
