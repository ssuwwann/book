package board.review;

import board.BoardResponseDTO;
import board.BoardService;
import board.util.DateTimeUtil;
import book.BookService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/reviews/*")
public class ReviewController extends HttpServlet {
  private ReviewService reviewService;
  private BookService bookService;
  private BoardService boardService;

  public ReviewController() {
    reviewService = ReviewService.getInstance();
    bookService = BookService.getInstance();
    boardService = BoardService.getInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String uri = req.getRequestURI();
    String[] method = uri.split("/");
    System.out.println("uri: " + uri);
    try {
      if (method[2].startsWith("get")) {
        long reviewPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        getReviewByReviewPk(req, res, reviewPk);
      }
      if (method[2].startsWith("all")) {
        long boardPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        getReviewByBoardPk(req, res, boardPk);
      }

      if (method[2].startsWith("add")) {
        String boardPkParam = req.getParameter("seq");
        long boardPk = 0L;
        if (boardPkParam != null) boardPk = Long.parseLong(boardPkParam);
        moveToWrite(req, res, boardPk);
      }

      if (method[2].startsWith("update")) {
        long reviewPk = Long.parseLong(req.getParameter("review"));
        long boardPk = Long.parseLong(req.getParameter("board"));
        moveToUpdateReviewPage(req, res, boardPk, reviewPk);
      }

      if (method[2].startsWith("remove")) {
        long reviewPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        removeReviewByReviewPk(req, res, reviewPk);
      }

      if (method[2].startsWith("check")) {
        long writerPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        checkWriter(req, res, writerPk);
      }

      if (method[2].startsWith("valid")) {
        long boardPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        checkValid(req, res, boardPk);
      }

      if (method[2].startsWith("dup")) {
        long boardPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        System.out.println("boardPk: " + boardPk);
        checkDup(req, res, boardPk);
      }
    } catch (NumberFormatException nfe) {
      nfe.printStackTrace();
    }
  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String uri = req.getRequestURI();
    String[] method = uri.split("/");
    System.out.println("uri: " + uri);
    try {
      if (method[2].startsWith("all")) {
        long boardPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        getReviewByBoardPkWithPaging(req, res, boardPk);
      }
      if (method[2].startsWith("add")) {
        long boardPk = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        addReview(req, res, boardPk);
      }
      if (method[2].startsWith("update")) {
        long reviewPk = Long.parseLong(req.getParameter("review"));
        updateReviewByReviewPk(req, res, reviewPk);
      }
    } catch (NumberFormatException nfe) {
      nfe.printStackTrace();
    }
  }

  private void checkDup(HttpServletRequest req, HttpServletResponse res, long boardPk) throws IOException {
    PrintWriter out = res.getWriter();
    HttpSession session = req.getSession(false);
    Member member = (Member) session.getAttribute("member");
    int bookPk = bookService.getBookUserWriteBySuwan(member.getSeq(), boardPk);
    int result = 0;
    if (bookPk == -1) {
      result = bookPk;
    } else {
      int reviewPk = reviewService.getReviewPkByMemberPk(bookPk, member.getSeq());
      result = reviewPk;
    }
    out.print("{\"result\":\"" + result + "\"}");
  }


  private void checkValid(HttpServletRequest req, HttpServletResponse res, long boardPk) throws ServletException, IOException {
    PrintWriter out = res.getWriter();
    HttpSession session = req.getSession(false);
    Member member = (Member) session.getAttribute("member");
    int result = 0;
    if (member == null) {
      out.print("{\"result\":\"" + result + "\"}");
    } else {
      long memberPk = member.getSeq();
      int findResult = bookService.getBoardPkFromBookByMemberPk(memberPk, boardPk);
      if (findResult > 0) {
        BoardResponseDTO board = boardService.getBoardByBoardPk(boardPk);
        String today = DateTimeUtil.transfer(new java.util.Date());
        try {
          Date parse = DateTimeUtil.sdf.parse(today);
          int compared = parse.compareTo(board.getLdate());
          if (compared > 0) result = 1;
        } catch (ParseException e) {
          e.printStackTrace();
        }
      } else {
        result = -1;
      }
      out.print("{\"result\":\"" + result + "\"}");
    }
  }


  private void removeReviewByReviewPk(HttpServletRequest req, HttpServletResponse res, long reviewPk) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Member member = (Member) session.getAttribute("member");
    PrintWriter out = res.getWriter();
    int result = 0;
    if (member != null) {
      result = reviewService.removeReviewByReviewPk(reviewPk);
    }
    out.print("{\"result\":\"" + result + "\"}");
  }


  private void updateReviewByReviewPk(HttpServletRequest req, HttpServletResponse res, long reviewPk) throws ServletException, IOException {
    Gson gson = new Gson();
    HttpSession session = req.getSession(false);
    Member member = (Member) session.getAttribute("member");
    PrintWriter out = res.getWriter();

    BufferedReader reader = req.getReader();
    ReviewRequestDTO dto = gson.fromJson(reader, ReviewRequestDTO.class);
    try {
      dto.setMemberSeq(member.getSeq());
    } catch (NullPointerException npe) {
      System.out.println("로그인 아직 안됨: " + npe.getMessage());
    }
    int result = reviewService.updateReviewByReviewPk(dto, reviewPk);
    out.print("{\"result\":\"" + result + "\"}");
  }


  private void getReviewByReviewPk(HttpServletRequest req, HttpServletResponse res, long reviewPk) throws ServletException, IOException {
    Gson gson = new Gson();
    PrintWriter out = res.getWriter();
    ReviewResponseDTO dto = reviewService.getReviewByReviewPk(reviewPk);

    out.print(gson.toJson(dto));
  }

  private void moveToUpdateReviewPage(HttpServletRequest req, HttpServletResponse res, long boardPk, long reviewPk) throws ServletException, IOException {
    ReviewResponseDTO dto = reviewService.getReviewByReviewPk(reviewPk);
    req.setAttribute("dto", dto);
    BoardResponseDTO board = boardService.getBoardByBoardPk(boardPk);
    req.setAttribute("board", board);
    req.getRequestDispatcher("/WEB-INF/jsp/board/review_update.jsp").forward(req, res);
  }


  private void moveToWrite(HttpServletRequest req, HttpServletResponse res, long boardPk) throws ServletException, IOException {
    BoardResponseDTO board = boardService.getBoardByBoardPk(boardPk);
    req.setAttribute("board", board);
    req.getRequestDispatcher("/WEB-INF/jsp/board/review_write.jsp").forward(req, res);
  }

  private void checkWriter(HttpServletRequest req, HttpServletResponse res, long writerPk) throws ServletException, IOException {
    PrintWriter out = res.getWriter();
    HttpSession session = req.getSession(false);
    Member member = (Member) session.getAttribute("member");
    int result = 0;
    if (member == null) {
      out.print("{\"result\":\"" + result + "\"}");
    } else {
      result = writerPk == member.getSeq() ? 1 : 0;
      out.print("{\"result\":\"" + result + "\"}");
    }
  }

  private void getReviewByBoardPk(HttpServletRequest req, HttpServletResponse res, long boardPk) throws ServletException, IOException {
    Gson gson = new Gson();
    PrintWriter out = res.getWriter();
    List<ReviewResponseDTO> dto = new ArrayList();
    ResponseDTO<ReviewResponseDTO> reviewsByBoardPk = reviewService.getReviewsByBoardPk(boardPk);
    for (ReviewResponseDTO e : reviewsByBoardPk.getDto()) {
      dto.add(e);
    }
    out.print(gson.toJson(dto));
  }

  private void getReviewByBoardPkWithPaging(HttpServletRequest req, HttpServletResponse res, long boardPk) throws ServletException, IOException {
    Gson gson = new Gson();
    PrintWriter out = res.getWriter();

    BufferedReader reader = req.getReader();
    RequestDTO paging = gson.fromJson(reader, RequestDTO.class);
    int count = reviewService.getCountbyBoardPk(boardPk);
    List<ReviewResponseDTO> reviewsByBoardPkWithPaging = reviewService.getReviewsByBoardPkWithPaging(boardPk, paging);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("count", count);
    JsonArray jsonArray = gson.toJsonTree(reviewsByBoardPkWithPaging).getAsJsonArray();
    jsonObject.add("data", jsonArray);
    out.print(jsonObject.toString());
  }

  private void addReview(HttpServletRequest req, HttpServletResponse res, long boardPk) throws ServletException, IOException {
    Gson gson = new Gson();
    HttpSession session = req.getSession(false);
    Member member = (Member) session.getAttribute("member");
    PrintWriter out = res.getWriter();

    BufferedReader reader = req.getReader();
    ReviewRequestDTO dto = gson.fromJson(reader, ReviewRequestDTO.class);
    dto.setBoardSeq(boardPk);
    try {
      dto.setMemberSeq(member.getSeq());
    } catch (NullPointerException npe) {
      System.out.println("로그인 아직 안됨: " + npe.getMessage());
    }
    int result = reviewService.addReview(dto, member.getSeq());

    out.print("{\"result\":\"" + result + "\"}");
  }
}
