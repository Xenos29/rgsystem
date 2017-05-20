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

public class Change extends HttpServlet {

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
        Commands cmd = new Commands(con);
        boolean error = false;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String currpw = request.getParameter("currpw");
        String newpw = request.getParameter("newpw");
        if(!currpw.equals(user.getPassword())){
            error=true;
            session.setAttribute("error","Password does not match");
        }
        else{
            cmd.updatePassword(user, newpw);
        }
        if (!error) {
            session.setAttribute("success", "Change password successful");
            cmd.log(user, "Changed password");
        }
        response.sendRedirect("View_Main");
    }

}
