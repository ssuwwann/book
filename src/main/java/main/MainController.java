package main;

import domain.Main;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(value = {"/", ""}, loadOnStartup = 1)
public class MainController extends HttpServlet {

  private ServletContext context;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  @Override
  public void service(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {

    String param = req.getParameter("");
    HttpSession session = req.getSession();
    if (param != null && param.isEmpty()) {
      param = param.trim();
      switch (param) {
        case "search":
          search(req, res);
          break;
      }
    } else
      list(req, res);
  }

  private void list(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {
    MainService service = MainService.getInstance();

    ArrayList<Main> list = service.mainPageS();
    int count = service.count_academyS();

    req.setAttribute("list", list);
    req.setAttribute("count", count);

    String view = "index.jsp";
    req.getRequestDispatcher(view).forward(req, res);
  }

  private void search(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {
    String acd_name = req.getParameter("acd_name");

    MainService service = MainService.getInstance();
    ArrayList<Main> list = service.searchAcademyS(acd_name);
    int count = service.count_search_academy(acd_name);

    req.setAttribute("list", list);
    req.setAttribute("count", count);

    String view = "index.jsp";
    req.getRequestDispatcher(view).forward(req, res);
  }

}
