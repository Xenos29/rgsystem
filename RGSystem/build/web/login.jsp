<%-- 
    Document   : login
    Created on : Feb 26, 2017, 9:40:05 AM
    Author     : Silver
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%
        String alert = (String)request.getAttribute("alert");
    %>
    <%if(alert!=null&&alert.equals("Invalid Acc")){%>
    <script type="text/javascript">
        var msg = "Invalid username or password.";
        alert(msg);
    </script>
    <%}%>
    <br>
    <div class="container-fluid">
        <div class="panel panel-warning">
            <div class="panel-heading" align="center">
            </div>
            <br>
            <div id="Logo">
            <center>
            <img src="Images/USTLOGO.png" alt="UST Miguel de Benavidez Library" style="width:15%;height:15%;">   
                <h2>UST Library Report Generation System</h2>
            </center>    
            </div>
            
            <div class="panel-body"align="center">
                <div class="container " style="margin-top: 2%; margin-bottom: 4%;">
                    <div class="panel panel-success" style="max-width: 35%;" align="left">

            <div class="panel-body">
            <div class="form-group">
                <form method="post" action="Login">
                <div class="form-group">
                    <label for="inputEmail">Username</label>
                    <input type="text" name="username" class="form-control" id="Username">
                </div>
                <div class="form-group">
                    <label for="inputPassword">Password</label>
                    <input type="password" name="password" class="form-control" id="Password">
                </div>
                <input type="hidden" name="dest" value="home"/>
                <button type="submit" class="btn btn-warning" style="width:100%">Login</button>
                </form>
                        </div> 
                    </div>
                </div>
           
        </div>
    </div>
    <div class="panel-footer" align="center">
                UST Miguel de Benavidez Library
                <br>
                Report Generation System
                <br>
                <small>by Team DELTA UST IICS</small>
            </div>
</body>
</html>