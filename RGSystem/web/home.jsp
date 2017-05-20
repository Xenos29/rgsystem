<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="classes.entity.Report" %>
<%@ page import="classes.entity.User" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Home Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="js/bootstrap.js"></script>

    </head>
    <%
        List<List<Report>> yearreports = (List) request.getAttribute("yearreports");
        List<String> aysl = (List) request.getAttribute("aysl");
        Set<String> sections = (Set) request.getAttribute("sections");
        User user = (User) request.getAttribute("user");
        Iterator itryr = yearreports.iterator();
        Iterator itry = aysl.iterator();
        Iterator<String> itrs = sections.iterator();
    %>
    <body>
        <%
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
        %>
        <%if (error != null) {%>
        <script type="text/javascript">
            var msg = "<%=error%>";
            alert(msg);
        </script>
        <%}%>
        <%if (success != null) {%>
        <script type="text/javascript">
            var msg = "<%=success%>";
            alert(msg);
        </script>
        <%}%>
        <br>
        <div id="body_container"class="container-fluid">
            <div class="panel panel-warning">

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tbody>

                        <tr>
                            <td class="align-baseline">
                                <div id="tables" class="panel panel-warning" style="margin-left: 2% ; margin-right: 5%" align="left">
                                    <div class="panel-heading">
                                        <form class="navbar-form" role="search" action="View_Main" method="post">
                                            <div class="input-group col-xs-12">
                                                <input type="text" class="form-control" placeholder="Search" name="rkey" id="srch-term">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">
                                                        <i class="glyphicon glyphicon-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>

                                    <div id="reports_panel" class="panel-body" style="min-width: 550px ;min-height: 550px; max-height: 550px ; overflow: auto">
                                        <div class="panel-group">
                                            <%
                                                int j = 0;
                                                while (itry.hasNext()) {
                                                    j++;
                                                    String href = "#collapse" + j;
                                                    String id = "collapse" + j;
                                                    String ay = (String) itry.next();
                                            %>
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title">
                                                        <a data-toggle="collapse" href=<%=href%>><%=ay%></a>
                                                    </h4>
                                                </div>
                                                <div id="<%=id%>" class="panel-collapse collapse">
                                                    <ul class="list-group">
                                                        <li class="list-group-item">
                                                            <br>
                                                            <table class="table table-hover table-responsive table-bordered table-striped">
                                                                <thead>
                                                                    <tr class="warning">
                                                                        <th>Report ID</th>
                                                                        <th>File Name</th>
                                                                        <th style="width: 20%">Date Uploaded</th>
                                                                        <th>A.Y.</th>
                                                                        <th>Section</th>
                                                                        <th>Link</th>
                                                                            <%if (user.getPosition().equals("Clerk") && !ay.equals("Templates")) {%>
                                                                        <th></th>
                                                                        <th></th>
                                                                            <%}%>
                                                                            <%if (user.getPosition().equals("Section Head") && !ay.equals("Templates")) {%>
                                                                        <th></th>
                                                                            <%}%>
                                                                            <%if (user.getPosition().equals("Clerk") && ay.equals("Templates")) {%>
                                                                        <th></th>
                                                                            <%}%>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        List<Report> reports = (List) itryr.next();
                                                                        Iterator<Report> itrr = reports.iterator();
                                                                        while (itrr.hasNext()) {
                                                                            Report report = itrr.next();
                                                                    %>
                                                                    <tr>
                                                                        <td><%=report.getId()%></td>
                                                                        <td><%=report.getFilename()%></td>
                                                                        <td><%=report.getTimestamp()%></td>
                                                                        <td><%=report.getAY()%></td>
                                                                        <td><%=report.getSection()%></td>
                                                                        <td>
                                                                            <form method="post" action="Download">
                                                                                <input name="id" type="hidden" value="<%=report.getId()%>"
                                                                                       <input name="section" type="hidden" value=<%=report.getSection()%>>
                                                                                <button class ="btn btn-info" type="submit">Download</button>
                                                                            </form>
                                                                        </td>
                                                                        <%if (user.getPosition().equals("Clerk") && !ay.equals("Templates")) {%>

                                                                        <td>
                                                                            <div id= "<%=report.getId()%>">
                                                                                <%if (!report.getRevise()) {%>
                                                                                <form method="post" action="View_Main">
                                                                                    <input type="hidden" name="revise" value=<%=report.getId()%>>
                                                                                    <button class ="btn btn-success" type="submit">Request Revision</button>
                                                                                </form>
                                                                                <%}%>    
                                                                                <%if (report.getRevise()) {%>
                                                                                <button class ="btn btn-success" type="submit" disabled>Request Revision</button>
                                                                                <%}%>
                                                                            </div> 
                                                                        </td> 
                                                                        <td>
                                                                            <div id= "<%=report.getId()%>">
                                                                                <button type="button" class="btn btn-danger" onclick="setId_del(<%=report.getId()%>)" data-toggle="modal" data-target="#deleteModal">
                                                                                    <span class="glyphicon glyphicon-ban-circle"></span> Delete
                                                                                </button>
                                                                            </div> 
                                                                        </td>
                                                                        <%}%>
                                                                        <%if (user.getPosition().equals("Section Head") && !ay.equals("Templates")) {%>

                                                                        <td>
                                                                            <div id= "<%=report.getId()%>">
                                                                                <%if (!report.getRevise()) {%>
                                                                                <button type="button" class="btn btn-success" disabled>Revise</button>

                                                                                <%}%>
                                                                                <%if (report.getRevise()) {%>
                                                                                <button type="button" class="btn btn-success" onclick="setId_rev(<%=report.getId()%>)" data-toggle="modal" data-target="#revisModal">
                                                                                    <span class="glyphicon glyphicon-check"></span> Revise
                                                                                </button>
                                                                                <%}%>
                                                                            </div> 
                                                                        </td>    
                                                                        <%}%>

                                                                        <%if (user.getPosition().equals("Clerk") && ay.equals("Templates")) {%>
                                                                        <td>
                                                                            <div id= "<%=report.getId()%>">
                                                                                <button type="button" class="btn btn-success" onclick="setId_rev(<%=report.getId()%>)" data-toggle="modal" data-target="#revisModal">
                                                                                    <span class="glyphicon glyphicon-check"></span> Revise
                                                                                </button>
                                                                            </div> 
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>
                                                                    <%}%>
                                                                </tbody>
                                                            </table>
                                                            <script>
                                                                function setId_rev(id) {
                                                                    var idtbd_rev = document.getElementById("idtbd_rev");
                                                                    idtbd_rev.value = id;
                                                                }
                                                                function setId_del(id) {
                                                                    var idtbd_del = document.getElementById("idtbd_del");
                                                                    idtbd_del.value = id;
                                                                }
                                                            </script>
                                                            <!-- Modal -->
                                                            <div class="modal fade" id="revisModal" role="dialog">
                                                                <div class="modal-dialog modal-sm">
                                                                    <div class="modal-content">
                                                                        <div class="modal-header">
                                                                            <button type="button" class="close" data-dismiss="modal">&times;
                                                                            </button>
                                                                            <h4 class="modal-title">Upload Revision</h4>
                                                                        </div>


                                                                        <form method="post" action="Revise" enctype="multipart/form-data">
                                                                            <div class="modal-body">
                                                                                <p>Do you really want to upload this revision?</p>
                                                                                <input type="file" name="report" size="100">
                                                                            </div>
                                                                            <div class="modal-footer">
                                                                                <div class="pull-left">
                                                                                    <input type="hidden" name="idtbd" id="idtbd_rev"/>
                                                                                    <button type="submit" class="btn btn-success">
                                                                                        <span class="glyphicon glyphicon-check"></span> Yes
                                                                                    </button>
                                                                                </div>
                                                                                <button type="button" class="btn btn-danger" data-dismiss="modal">
                                                                                    <span class="glyphicon glyphicon-ban-circle"></span> No
                                                                                </button>

                                                                            </div>
                                                                        </form>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <!-- Modal -->
                                                            <div class="modal fade" id="deleteModal" role="dialog">
                                                                <div class="modal-dialog modal-sm">
                                                                    <div class="modal-content">
                                                                        <div class="modal-header">
                                                                            <button type="button" class="close" data-dismiss="modal">&times;
                                                                            </button>
                                                                            <h4 class="modal-title">Delete</h4>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <p>Do you really want to delete this?</p>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <div class="pull-left">
                                                                                <form method="post" action="Delete">
                                                                                    <input type="hidden" name="type" value="report"/>
                                                                                    <input type="hidden" name="idtbd" id="idtbd_del"/>
                                                                                    <button type="submit" class="btn btn-danger">
                                                                                        <span class="glyphicon glyphicon-check"></span> Yes
                                                                                    </button>
                                                                                </form>
                                                                            </div>
                                                                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                                                                <span class="glyphicon glyphicon-ban-circle"></span> No
                                                                            </button>

                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>

                                                    </ul>
                                                </div>
                                            </div>
                                            <%}%>
                                        </div>


                                    </div>
                                </div>

                            </td>

                            <td class="align-baseline">
                                <div class="panel panel-warning" style=" margin-left: 5%; margin-right: 5%" align="right">
                                    <div class="panel-heading" align="center" >
                                        <h2>Welcome</h2>
                                    </div>
                                    <div id="user_info" class="panel-body" align="center" style="min-height:     550px; max-height: 550px; overflow: auto">
                                        <div id="profile_info" align="center">
                                            <img src="Images/Avatar.png" width="22%" height="22%">
                                            <h3><%=user.getName()%></h3>
                                            <small>UST Library <%=user.getPosition()%></small>
                                        </div>  
                                        <br>
                                        <div id="profile_arrangement">
                                            <div id="modalLogOut">
                                                <button type="submit" class="btn btn-danger" data-toggle="modal" data-target="#logoutModal">
                                                    <span class="glyphicon glyphicon-off"></span> Log Out
                                                </button>
                                                <!-- Modal -->

                                                <div class="modal fade" id="logoutModal" role="dialog">
                                                    <div class="modal-dialog modal-sm">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <button type="button" class="close" data-dismiss="modal">&times;
                                                                </button>
                                                                <h4 class="modal-title">Log Out</h4>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p>Do you really want to log out?</p>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <div class="pull-left">
                                                                    <form method="post" action="Logout">
                                                                        <button type="submit" class="btn btn-danger">
                                                                            <span class="glyphicon glyphicon-log-out"></span> Yes
                                                                        </button>
                                                                </div>
                                                                <button type="button" class="btn btn-default" data-dismiss="modal">
                                                                    <span class="glyphicon glyphicon-ban-circle"></span> No
                                                                </button>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                            <br>
                                            <div id="modalPassword">
                                                <button type="submit" class="btn btn-default" data-toggle="modal" data-target="#changeModal">
                                                    <span class="glyphicon glyphicon-off"></span> Change Password
                                                </button>
                                                <!-- Modal -->

                                                <div class="modal fade" id="changeModal" role="dialog">
                                                    <div class="modal-dialog modal-sm">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <button type="button" class="close" data-dismiss="modal">&times;
                                                                </button>
                                                                <h4 class="modal-title">Change Password</h4>
                                                            </div>
                                                            <form method="post" action="Change">
                                                                <div class="modal-body">
                                                                    Current  <input type="password" name="currpw" required/>
                                                                    <br>
                                                                    <br>
                                                                    New                 <input type="password" name="newpw" required/>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <div class="pull-left">

                                                                        <button type="submit" class="btn btn-danger">
                                                                            <span class="glyphicon glyphicon-log-out"></span> Confirm
                                                                        </button>
                                                                    </div>
                                                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                                                        <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                                                                    </button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <br>
                                            <br>

                                            <div class="panel-heading panel-warning"></div>
                                            <br>

                                            <div id="upload" text-align="center"></div>
                                            <%if (user.getPosition().equals("Section Head")) {%>
                                            <h4>Upload a file</h4>
                                            <form method="post" action="Upload" enctype="multipart/form-data">
                                                <input type="file" name="report" size="100">
                                                <br>
                                                <button type="submit" value="Upload" class="btn btn-success" style="width: 40%">Upload</button>
                                            </form>
                                            <%}%>
                                            <%if (user.getPosition().equals("Clerk")) {%>
                                            <button type="submit" value="Generate Annual Report" class="btn btn-primary" data-toggle="modal" data-target="#genrepModal" style="width: 80%">Generate</button>
                                            <%}%>
                                            <div id="genrepModal" class="modal fade" role="dialog">
                                                <div class="modal-dialog">

                                                    <!-- Modal content-->
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                            <h3 class="modal-title">Generate Report</h3>
                                                        </div>
                                                        <form method="post" action="Generate">
                                                            <div class="modal-body" text->
                                                                <h4>Make an annual report on the following sections?</h4>
                                                                <%while (itrs.hasNext()) {
                                                                        String section = itrs.next();
                                                                %>
                                                                <div class="checkbox">
                                                                    <label><input type="checkbox" name="section" value="<%=section%>"/><%=section%></label>
                                                                </div>
                                                                <%}%>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary">
                                                                    <span class="glyphicon glyphicon-ok"></span> Generate
                                                                </button>

                                                                <button type="button" class="btn btn-danger" data-dismiss="modal">
                                                                    <span class="glyphicon glyphicon-remove"></span> Cancel
                                                                </button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                </table>
            </div>
        </div>

        <br>


        <div class="panel-warning panel-footer" align="center">
            UST Miguel de Benavidez Library
            <br>
            Report Generation System
            <br>
            <small>by Team DELTA UST IICS</small>
        </div>


    </body>
</html>