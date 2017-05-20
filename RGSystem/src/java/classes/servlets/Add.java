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

public class Add extends HttpServlet {

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
        String username = request.getParameter("username");
        
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String position = "Section Head";
        String section = request.getParameter("section");
        if(!cmd.exists(username,null)){
        if (cmd.getUser(section) == null) {
            cmd.add(username, password, name, position, section);
        } else {
            error = true;
            session.setAttribute("error", "User for section " + section + " already exists.");
            response.sendRedirect("View_Add?action=Add");
        }
        }
        else{
            error = true;
            session.setAttribute("error", "Username already exists.");
            response.sendRedirect("View_Add?action=Add");
        }
        if (!error) {
            session.setAttribute("success", "Add Successful");
            cmd.log(user, "Added user " + username);
            response.sendRedirect("View_Main");
        }
        

    }

}
