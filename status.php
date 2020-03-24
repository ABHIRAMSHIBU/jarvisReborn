<html>
<head>
<?php  require 'auth.php' ?>
<?php  require 'config.php' ?> 
<meta name="viewport" content="width=device-width, initial-scale=1">
<title> SAL Service Status </title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title> SAL REMOTE UI </title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <script src="script/BootstrapMenu.min.js"></script>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="script/script.js"></script>
    <link rel="stylesheet" href="css/styles.css">
<style>
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
.badge{
    text-align:center;
    padding: 20px 3px 0 3px;
}
.status{
    height:29px;
    width:29px;
    border-radius: 50%;
    background-color: orchid;
    color: orchid;
    padding: 00px 3px 0 3px;
}
</style>
</head>
<body>
    <header class="jumbotron" style="margin-bottom:0px;" >
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-2"></div>
                    <div class="col-md-6">
                        <h1>SSAL Remote Dashboard</h1>
                    </div>
                    <div class="col-md-4">
                        <img src="img/ssal.png" class="img-fluid" style="height:20%">
                    </div>
                </div>
            </div>
    </header>  
    <body>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!--<div class="navbar-header">
                <a class="navbar-brand" href="#">WebSiteName</a>
            </div>-->
        <ul class="nav navbar-nav">
            <li class="active"><a href="index.php">Home</a></li>
            <li><a href="powerpanel.php">Power panel</a></li>
            <li><a href="status.php">Service status</a></li>
        </ul>
        </div>
    </nav>
    <br><br>
    <div align="center" >
        <span class="badge badge-pill badge-primary" style="height:50px;width:95px;">Device</span>&nbsp;&nbsp;&nbsp;
        <span class="status badge badge-pill badge-warning" style="">.</span>&nbsp;Running&nbsp;&nbsp;
        <!--<button type="button" class="btn btn-primary btn-lg active">Device</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-warning btn-lg active">Status</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
        <button type="button" class="btn btn-info btn-lg active">Start</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Restart</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Stop</button>&nbsp;&nbsp; 
        <br><br>
    </div>
    <div align="center" >
        <span class="badge badge-pill badge-primary" style="height:50px;width:95px;">Device</span>&nbsp;&nbsp;&nbsp;
        <span class="status badge badge-pill badge-warning" >.</span>&nbsp;Running&nbsp;&nbsp;
        <!--<button type="button" class="btn btn-primary btn-lg active">Device</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-warning btn-lg active">Status</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
        <button type="button" class="btn btn-info btn-lg active">Start</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Restart</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Stop</button>&nbsp;&nbsp; 
        <br><br>
    </div>
    <div align="center" >
        <span class="badge badge-pill badge-primary" style="height:50px;width:95px;">Device</span>&nbsp;&nbsp;&nbsp;
        <span class="status badge badge-pill badge-warning">.</span>&nbsp;Running&nbsp;&nbsp;
        <!--<button type="button" class="btn btn-primary btn-lg active">Device</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-warning btn-lg active">Status</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
        <button type="button" class="btn btn-info btn-lg active">Start</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Restart</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active">Stop</button>&nbsp;&nbsp; 
        <br><br>
    </div>
    <br><br><br><br><br><br>
    <footer class="footer">
            <div class="row justify-content-center">             
                    <div class="col-auto " align="center">
                        <p> © Copyright 2019 BrainNet Technologies</p>
                    </div>
               </div>
    </footer>
</body>
</html> 
