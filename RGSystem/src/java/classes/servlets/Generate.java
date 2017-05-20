package classes.servlets;

import classes.Commands;
import classes.entity.User;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Generate extends HttpServlet {

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
        String section[] = request.getParameterValues("section");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean error = false;
        if(section==null){
            error=true;
            session.setAttribute("error","Nothing Selected");
            rd = request.getRequestDispatcher("View_Main");
            rd.forward(request,response);
        }
        try{
        InputStream is = cmd.getReport(2).getFile().getBinaryStream();
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + "Summary.xlsx");
        cmd.generate(section,is,os);
        if (!error) {
            session.setAttribute("success", "Generate Successful");
            cmd.log(user, "Generated annual report");
        }
        }
        catch(SQLException e){}
    }

}
