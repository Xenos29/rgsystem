package classes.servlets;

import classes.Commands;
import classes.entity.User;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class View_Add extends HttpServlet {

    private Connection con;
    private RequestDispatcher rd;

    public void init() throws ServletException {
        super.init();
        try {
            Class.forName(getServletContext().getInitParameter("jdbcClassname"));
            String username = getServletContext().getInitParameter("dbUsername");
            String password = getServletContext().getInitParameter("dbPassword");
            String url = getServletContext().getInitParameter("jdbcDriverURL");
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException sqle) {
        } catch (ClassNotFoundException nfe) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request,response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Commands cmd = new Commands(con);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        
        String error = (String) session.getAttribute("error");
        if (!error.equals("false")) {
            request.setAttribute("error", error);
            session.setAttribute("error", "false");
        }
        String success = (String) session.getAttribute("success");
        if (!success.equals("false")) {
            request.setAttribute("success", success);
            session.setAttribute("success", "false");
        }
        
        
        String action = request.getParameter("action");
        if(action==null)
            action = (String)request.getAttribute("action");
        request.setAttribute("action",action);
        if(action.equals("Update")){
            String sec = request.getParameter("sectiontbd");
            if(sec ==null)
                sec = (String)request.getAttribute("sectiontbd");
            User u = cmd.getUser(sec);
            
            request.setAttribute("usertbd", u);
        }
        request.setAttribute("user",user);
        rd = request.getRequestDispatcher("add.jsp");
        rd.forward(request, response);
    }
}
