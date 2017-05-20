package classes.servlets;

import classes.Commands;
import classes.entity.Log;
import classes.entity.Report;
import classes.entity.User;
import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class View_Main extends HttpServlet {

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
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Commands cmd = new Commands(con);
        String rkey = request.getParameter("rkey");
        String ukey = request.getParameter("ukey");
        String lkey = request.getParameter("lkey");
        List<List<Report>> yearreports = new ArrayList();
        Set<String> sections = new HashSet();

        List<User> users = new ArrayList();
        List<Log> logs = new ArrayList();
        Set<String> ays = new HashSet();
        List<String> aysl = null;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);
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
        
        String revise = request.getParameter("revise");
        if(revise!=null){
            Report report =cmd.getReport(Integer.parseInt(revise));
            cmd.requestRevise(report);
        }
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String ay = (year - 1) + "-" + (year);
        if (!user.getPosition().equals("Administrator")) {
            if (user.getPosition().equals("Section Head")) {
                List<Report> reports = new ArrayList();
                if (rkey == null) {
                    reports = cmd.getReports(user);
                }
                if (rkey != null) {
                    reports = cmd.getReports(rkey, user);
                }
                boolean added = false;
                for (int i = 0; i < reports.size(); i++) {
                    if (!ays.contains(reports.get(i).getAY()) && reports.get(i).getId() != 2 && reports.get(i).getId() != 1) {
                        ays.add(reports.get(i).getAY());
                        yearreports.add(new ArrayList());
                    }
                    if (reports.get(i).getId() == 2 || reports.get(i).getId() == 1) {
                        ays.add("Templates");
                        added = true;
                    }
                }
                if (added) {
                    yearreports.add(new ArrayList());
                }

                aysl = new ArrayList(ays);
                Collections.sort(aysl);
                for (int i = 0; i < reports.size(); i++) {
                    if (reports.get(i).getId() == 1) {
                        yearreports.get(aysl.size() - 1).add(reports.get(i));
                    }
                    for (int j = 0; j < aysl.size(); j++) {
                        if (reports.get(i).getAY().equals(aysl.get(j)) && reports.get(i).getId() != 2 && reports.get(i).getId() != 1) {
                            yearreports.get(j).add(reports.get(i));
                        }

                    }
                }

                rd = request.getRequestDispatcher("home.jsp");
            } else {
                List<Report> reports = new ArrayList();
                if (rkey == null) {
                    reports = cmd.getReports(user);
                }
                if (rkey != null) {
                    reports = cmd.getReports(rkey, user);
                }
                boolean added = false;
                for (int i = 0; i < reports.size(); i++) {
                    if (!reports.get(i).getSection().equals("N/A") && reports.get(i).getAY().equals(ay)) {
                        sections.add(reports.get(i).getSection());
                    }
                    if (!ays.contains(reports.get(i).getAY()) && reports.get(i).getId() != 2 && reports.get(i).getId() != 1) {
                        ays.add(reports.get(i).getAY());
                        yearreports.add(new ArrayList());
                    }
                    if (reports.get(i).getId() == 2 || reports.get(i).getId() == 1) {
                        ays.add("Templates");
                        added = true;
                    }
                }
                if (added) {
                    yearreports.add(new ArrayList());
                }

                aysl = new ArrayList(ays);
                Collections.sort(aysl);
                for (int i = 0; i < reports.size(); i++) {
                    if (reports.get(i).getId() == 1 || reports.get(i).getId() == 2) {
                        yearreports.get(aysl.size() - 1).add(reports.get(i));
                    }
                    for (int j = 0; j < aysl.size(); j++) {
                        if (reports.get(i).getAY().equals(aysl.get(j)) && reports.get(i).getId() != 2 && reports.get(i).getId() != 1) {
                            yearreports.get(j).add(reports.get(i));
                        }

                    }
                }
            }
            request.setAttribute("sections", sections);
            request.setAttribute("yearreports", yearreports);
            request.setAttribute("aysl", aysl);
            rd = request.getRequestDispatcher("home.jsp");
        } else if (user.getPosition().equals("Administrator")) {
            if (ukey == null) {
                users = cmd.getUsers(user.getSection());
            } else if (ukey != null) {
                users = cmd.getUsers(ukey, user.getSection());
            }
            if (lkey == null) {
                logs = cmd.getLogs();
            } else if (lkey != null) {
                logs = cmd.getLogs(lkey);
            }
            request.setAttribute("users", users);
            request.setAttribute("logs", logs);
            rd = request.getRequestDispatcher("admin.jsp");
        }
        rd.forward(request, response);
    }
}
