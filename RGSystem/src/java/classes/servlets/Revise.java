package classes.servlets;

import classes.Commands;
import classes.entity.Report;
import classes.entity.User;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 268435456)
public class Revise extends HttpServlet {

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
        int id = Integer.parseInt(request.getParameter("idtbd"));
        String section = request.getParameter("section");
        Report reporttbd = cmd.getReport(id);
        boolean error = false;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Part report = request.getPart("report");
        InputStream isblob = report.getInputStream();
        InputStream isay = report.getInputStream();
        int size = (int) report.getSize();
        String fn = report.getName();
        try {

            String fn_split[] = fn.split("\\.");
            String ay = cmd.getAY(isay,fn_split[fn_split.length-1]);
            boolean uniqueay = true;
            List<Report> reports = cmd.getReports(user);


            if (uniqueay) {
                if (fn_split[fn_split.length-1].equals("xls") || fn_split[fn_split.length-1].equals("xlsx")) {
                    cmd.revise(reporttbd,user, isblob, ay, size, fn);
                } else {
                    session.setAttribute("error", "Unsupported file format");
                    error = true;
                }
            } else {
                session.setAttribute("error", "There exists a report for this A.Y.");
                error = true;
            }
        } catch (IllegalStateException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            session.setAttribute("error", "Please select a file.");
            error = true;
        }
        if (!error) {
            session.setAttribute("success", "Revision Successful");
            cmd.log(user, "Revised " + reporttbd.getFilename());
        }
        response.sendRedirect("View_Main");
    }
}
