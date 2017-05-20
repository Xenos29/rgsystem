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

public class Login extends HttpServlet {

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
            if(con==null)
                System.out.println("CON IS NULL");
        } catch (SQLException sqle) {
        } catch (ClassNotFoundException nfe) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Commands cmd = new Commands(con);
        User user=cmd.getUser(request.getParameter("username"),request.getParameter("password"));
        if(user==null){
         request.setAttribute("alert","Invalid Acc");
         rd = request.getRequestDispatcher("login.jsp");
         rd.include(request, response);
        }else{
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        session.setAttribute("success", "false");
        session.setAttribute("error", "false");
        cmd.log(user,"Login");
        rd = request.getRequestDispatcher("View_Main");
        }
        rd.forward(request, response);
    }

}
