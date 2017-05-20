package classes.servlets;

import classes.Commands;
import classes.entity.Report;
import classes.entity.User;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Delete extends HttpServlet {

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
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (type.equals("report")) {
            int id = Integer.parseInt(request.getParameter("idtbd"));
            Report r = cmd.getReport(id);
            cmd.delete(r);        
            if (!error) {
                session.setAttribute("success", "Delete Successful");
                cmd.log(user, "Deleted file " + r.getFilename());
            }
        } else if (type.equals("user")) {
            String section = request.getParameter("sectiontbd");
            User u = cmd.getUser(section);
            cmd.delete(u);     
            if (!error) {
                session.setAttribute("success", "Delete Successful");
                cmd.log(user, "Deleted user account of " + u.getName());
            }
        }
        response.sendRedirect("View_Main");
    }

}
