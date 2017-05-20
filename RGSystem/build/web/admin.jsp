
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="classes.entity.Log" %>
<%@page import="classes.entity.User" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Home Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
    </head>
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
        <%
            List<User> users = (List) request.getAttribute("users");
            List<Log> logs = (List) request.getAttribute("logs");
            User user = (User) request.getAttribute("user");
            Iterator<User> itru = users.iterator();
            Iterator<Log> itrl = logs.iterator();
        %>

        <div id="body_container"class="container-fluid">
            <div class="panel panel-warning">

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tbody>

                        <tr>
                            <td class="align-baseline">
                                <div id="search_bar" class="panel panel-warning" style="margin-left: 2% ; margin-right: 10% " align="left">
                                    <div class="panel-heading">
                                        <form class="navbar-form" role="search" method="post" action="View_Main">
                                            <input type="hidden" name="dest" value="home"/>
                                            <div class="input-group col-xs-12">
                                                <input type="text" class="form-control" placeholder="Search" name="ukey" id="srch-term">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">
                                                        <i class="glyphicon glyphicon-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>


                                    <div id="reports_panel" class="panel-body" style="min-width: 550px ;min-height: 230px; max-height:230px ; overflow: auto">
                                        <br>
                                        <table class="table table-hover table-responsive table-bordered table-striped">
                                            <thead>
                                                <tr class="warning">
                                                    <th>Section</th>
                                                    <th>Username</th>
                                                    <th>Password</th>
                                                    <th>Name</th>
                                                    <th>Position</th>
                                                    <th></th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    while (itru.hasNext()) {
                                                        User u = (User) itru.next();
                                                %> <tr class = "default">
                                                    <td><%=u.getSection()%></td>
                                                    <td><%=u.getUsername()%></td>
                                                    <td><%=u.getPassword()%></td>
                                                    <td><%=u.getName()%></td>
                                                    <td><%=u.getPosition()%></td>
                                                    <td>
                                                        <form method="post" action="View_Add">
                                                            <input name="action" type="hidden" value="Update">
                                                            <input name="sectiontbd" type="hidden" value='<%=u.getSection()%>'>
                                                            <button  type="submit" class="btn btn-primary">Update</button>
                                                        </form>
                                                            
                                                    </td>
                                                    <td>
                                                        <div id= "<%=u.getSection()%>">
                                                            <button type="button" class="btn btn-danger" onclick="setSection('<%=u.getSection()%>')" data-toggle="modal" data-target="#deleteModal">
                                                                <span class="glyphicon glyphicon-ban-circle"></span> Delete
                                                            </button>
                                                        </div>
                                                    </td>

                                                </tr>	
                                                <%	}
                                                %>
                                            </tbody>
                                        </table>
                                        <script>
                                            function setSection(section) {
                                                var sectiontbd = document.getElementById("sectiontbd");
                                                sectiontbd.value = section;
                                            }
                                        </script>
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
                                                                <input type="hidden" name="type" value="user"/>
                                                                <input type="hidden" name="sectiontbd" id="sectiontbd"/>
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
                                    </div>

                                    <br>
                                    <br>
                                    
                                    <form method="post" action="View_Add">
                                        <input name="action" type="hidden" value="Add">
                                        &nbsp;&nbsp;<button class="btn btn-link" type="submit">Add Section Head</button>
                                    </form>
                                    <br>
                                    <br>    
                                </div>
                                <div id="tables" class="panel panel-warning" style="margin-left: 2% ; margin-right: 10% " align="left">
                                    <div class="panel-heading">
                                        <form method="post" action="View_Main" class="navbar-form" role="search">
                                            <div class="input-group col-xs-12">
                                                <input type="text" class="form-control" placeholder="Search" name="lkey" id="srch-term">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">
                                                        <i class="glyphicon glyphicon-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>

                                    <div id="log_panel" class="panel-body" style="min-width: 550px ;min-height: 230px; max-height:230px ; overflow: auto">

                                        <br>
                                        <table class="table table-hover table-responsive table-bordered table-striped">
                                            <thead>
                                                <tr class="warning">
                                                    <th>Log ID</th>
                                                    <th>Action</th>
                                                    <th>Timestamp</th>
                                                    <th>Section</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%while (itrl.hasNext()) {
                                                        Log l = itrl.next();
                                                %>
                                                <tr>
                                                    <td><%=l.getId()%></td>
                                                    <td><%=l.getAction()%></td>
                                                    <td><%=l.getTimestamp()%></td>
                                                    <td><%=l.getSection()%></td>
                                                </tr>   
                                                <%}%>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>    
                            </td>

                            <td class="align-baseline">
                                <div class="panel panel-warning" style=" margin-left: -24%; margin-right: 5%; margin-top: -5%;" align="right">
                                    <div class="panel-heading" align="center" >
                                        <h2>Welcome</h2>
                                    </div>
                                    <div id="user_info" class="panel-body" align="center" style="min-height: 550px; max-height: 550px">
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