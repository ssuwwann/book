package filter;

import domain.Member;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.MemberService;

import java.io.IOException;

@WebFilter("/*")
public class CookieFilter implements Filter {
  private MemberService memberService;
  private String mySeqCookie;
  private boolean flag;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    memberService = MemberService.getInstance();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse res = (HttpServletResponse) servletResponse;
    String requestURI = req.getRequestURI();
    Cookie[] cookies = req.getCookies();
    HttpSession session = req.getSession(false);  // 세션이 없으면 새로 생성하지 않음

    boolean hasCookie = false;
    String mySeqCookie = null;

    // 쿠키 확인
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("mySeq")) {
          hasCookie = true;
          mySeqCookie = cookie.getValue();
          break;
        }
      }
    }

    // 1. 쿠키가 있고 세션이 없을 때
    if (hasCookie && session == null) {
      Member member = memberService.isMember(Integer.parseInt(mySeqCookie));
      if (member != null) {
        session = req.getSession();  // 새 세션 생성
        session.setAttribute("member", member);
      }
    } else if (!hasCookie && session != null) {
      session.removeAttribute("member");
      //session.invalidate();
    } else if (hasCookie && session != null) {
      Member sessionMember = (Member) session.getAttribute("member");
      if (sessionMember == null) {
        Member cookieMember = memberService.isMember(Integer.parseInt(mySeqCookie));
        if (cookieMember != null) {
          session.setAttribute("member", cookieMember);
        }
      }
    }

    // "/reviews/auth" 엔드포인트 처리
    if (requestURI.startsWith("/reviews/auth")) {
      res.getWriter().print("{\"result\":\"" + (hasCookie ? 1 : 0) + "\"}");
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }
}