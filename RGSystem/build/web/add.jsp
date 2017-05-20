<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="classes.entity.User" %>
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
        String action = request.getParameter("action");
        User user = (User) request.getAttribute("user");
        User usertbd = (User) request.getAttribute("usertbd");
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
                                <div id="tables" class="panel panel-warning" style="margin-left: 2% ; margin-right: 10% " align="left">
                                    <div class="panel-heading" align="center">
                                        <%if (action.equals("Add")) { %>
                                        <h3>Add Section Head</h3>
                                        <%}%>
                                        <%if (action.equals("Update")) { %>
                                        <h3>Update User</h3>
                                        <%}%>               
                                    </div>

                                    <div id="reports_panel" class="panel-body" style="min-width: 550px ;min-height: 550px; max-height: 550px ; overflow: auto" align="center">
                                        <br>
                                        <div class="container-fluid">
                                            <div class="panel panel-primary" style="max-width: 80%;" align="left">
                                                <div class="panel-body">

                                                    <form method="post" action="<%=action%>">
                                                        <%if (action.equals("Add")) {
                                                        %>
                                                        <div class="form-group">
                                                            <label for="inputUsername">Username</label>
                                                            <input name="username"  type="text" class="form-control" id="Username" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputPassword">Password</label>
                                                            <input name="password" type="password" class="form-control" id="Password" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName">Full Name</label>
                                                            <input name="name" type="text" class="form-control" id="Name" required>
                                                        </div>
                                                        <div class="form-group">        
                                                            <label for="inputSection">Section</label>
                                                            <select class="form-control" id="Section" name="section" >
                                                                <option value="Circulation">Circulation</option>
                                                                <option value="Civil Law">Civil Law</option>
                                                                <option value="Ecclesiastical">Ecclesiastical</option>               
                                                                <option value="Filipiniana">Filipiniana</option>
                                                                <option value="Gen. Ref">Gen. Ref</option>
                                                                <option value="Graduate School">Graduate School</option>
                                                                <option value="Health Sciences">Health Sciences</option>
                                                                <option value="Heritage">Heritage</option>
                                                                <option value="EHS - Grade School">EHS - Grade School</option>
                                                                <option value="EHS - High School">EHS - High School</option>
                                                                <option value="Humanities">Humanities</option>
                                                                <option value="Internet">Internet</option>
                                                                <option value="Music">Music</option>
                                                                <option value="Old Books">Old Books</option>
                                                                <option value="Religion">Religion</option>
                                                                <option value="Science and Technology">Science and Technology</option>
                                                                <option value="Serials">Serials</option>
                                                                <option value="Social Science">Social Science</option>
                                                                <option value="Spanish">Spanish</option>
                                                                
                                                            </select>
                                                        </div>
                                                        <br>
                                                        <hr />
                                                        <div id="subbutton" align="center">
                                                            <button type="submit" class="btn btn-primary" style="width:40%"><%=action%> Account</button> 
                                                        </div>
                                                        <br>
                                                        <br>
                                                        <%}%>
                                                        <%if (action.equals("Update")) { %>
                                                        <input type="hidden" value="<%=usertbd.getSection()%>" name="sectiontbd">
                                                        <div class="form-group">
                                                            <label for="inputUsername">Username</label>
                                                            <input name="username" value="<%=usertbd.getUsername()%>" type="text" class="form-control" id="Username" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputPassword">Password</label>
                                                            <input name="password" value="<%=usertbd.getPassword()%>"  type="password" class="form-control" id="Password" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName">Name</label>
                                                            <input name="name" value="<%=usertbd.getName()%>" type="text" class="form-control" id="Name" required>
                                                        </div>
                                                        <div class="form-group">  
                                                            <%if(!usertbd.getPosition().equals("Clerk")){%>
                                                            <label for="inputSection">Section</label>
                                                            <select class="form-control" id="Section" name="section" >
                                                                <option <%if("Circulation".equals(usertbd.getSection())){%>selected<%}%> value="Circulation">Circulation</option>
                                                                <option <%if("Civil Law".equals(usertbd.getSection())){%>selected<%}%> value="Civil Law">Civil Law</option>
                                                                <option <%if("Ecclesiastical".equals(usertbd.getSection())){%>selected<%}%> value="Ecclesiastical">Ecclesiastical</option>               
                                                                <option <%if("Filipiniana".equals(usertbd.getSection())){%>selected<%}%> value="Filipiniana">Filipiniana</option>
                                                                <option <%if("Gen. Ref".equals(usertbd.getSection())){%>selected<%}%> value="Gen. Ref">Gen. Ref</option>
                                                                <option <%if("Graduate School".equals(usertbd.getSection())){%>selected<%}%> value="Graduate School">Graduate School</option>
                                                                <option <%if("Health Sciences".equals(usertbd.getSection())){%>selected<%}%> value="Health Sciences">Health Sciences</option>
                                                                <option <%if("Heritage".equals(usertbd.getSection())){%>selected<%}%> value="Heritage">Heritage</option>
                                                                <option <%if("EHS - Grade School".equals(usertbd.getSection())){%>selected<%}%> value="EHS - Grade School">EHS - Grade School</option>
                                                                <option <%if("EHS - High School".equals(usertbd.getSection())){%>selected<%}%> value="EHS - High School">EHS - High School</option>
                                                                <option <%if("Humanities".equals(usertbd.getSection())){%>selected<%}%> value="Humanities">Humanities</option>
                                                                <option <%if("Internet".equals(usertbd.getSection())){%>selected<%}%> value="Internet">Internet</option>
                                                                <option <%if("Music".equals(usertbd.getSection())){%>selected<%}%> value="Music">Music</option>
                                                                <option <%if("Old Books".equals(usertbd.getSection())){%>selected<%}%> value="Old Books">Old Books</option>
                                                                <option <%if("Religion".equals(usertbd.getSection())){%>selected<%}%> value="Religion">Religion</option>
                                                                <option <%if("Science and Technology".equals(usertbd.getSection())){%>selected<%}%> value="Science and Technology">Science and Technology</option>
                                                                <option <%if("Serials".equals(usertbd.getSection())){%>selected<%}%> value="Serials">Serials</option>
                                                                <option <%if("Social Science".equals(usertbd.getSection())){%>selected<%}%> value="Social Science">Social Science</option>
                                                                <option <%if("Spanish".equals(usertbd.getSection())){%>selected<%}%> value="Spanish">Spanish</option>
                                                            </select>
                                                            <%}%>

                                                        </div>
                                                        <br>
                                                        <hr />
                                                        <div id="subbutton" align="center">
                                                            <button type="submit" class="btn btn-primary" style="width:40%"><%=action%> Account</button> 
                                                        </div>
                                                        <br>
                                                        <br>
                                                        <%}%>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>                   
                            </td>

                            <td class="align-baseline">
                                <div class="panel panel-warning" style=" margin-left: -18%; margin-right: 5%" align="right">
                                    <div class="panel-heading" align="center" >
                                        <h2>Welcome</h2>
                                    </div>
                                    <div id="user_info" class="panel-body" align="center" style="min-height: 550px; max-height: 550px; overflow: auto">
                                        <div id="profile_info" align="center">
                                            <img src="Images/Avatar.png" width="22%" height="22%">
                                            <h3><%=user.getName()%></h3>
                                            <small>UST Library <%=user.getPosition()%></small>
                                        </div>  
                                        <br>
                                        <div id="profile_arrangement">
                                            <div id="modalLogOut">
                                                <button type="submit" class="btn btn-danger" data-toggle="modal" data-target="#logoutModal">
                                                    <span class="glyphicon glyphicon-log-out"></span> Log Out
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
                                                                        <button type="submit" value="Logout" class="btn btn-danger">
                                                                    </form>
                                                                    <span class="glyphicon glyphicon-log-out"></span> Log out
                                                                    </button>
                                                                </div>
                                                                <button type="button" class="btn btn-default">
                                                                    <span class="glyphicon glyphicon-ban-circle" data-dismiss="modal"></span> No
                                                                </button>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>


                                            <br>
                                            <br>

                                            <div class="panel-heading panel-warning"></div>
                                            <br>
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