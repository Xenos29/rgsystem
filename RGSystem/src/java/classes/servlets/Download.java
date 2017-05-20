package classes.servlets;

import classes.Commands;
import classes.entity.Report;
import classes.entity.User;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Download extends HttpServlet {

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

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Commands cmd = new Commands(con);
        int id = Integer.parseInt(request.getParameter("id"));
        boolean error = false;
        String section = request.getParameter("section");
        Report report = cmd.getReport(id);
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + report.getFilename());
        cmd.download(os, report);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(!error){
            cmd.log(user, "Downloaded "+report.getFilename());
        }
    }
}
