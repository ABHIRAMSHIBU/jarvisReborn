<?php  require 'auth.php' ?>
<html>
<head>
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
.service{
    border-radius:0px;
}
.status{
    height:20px;
    width:20px;
    border-radius: 50%;
    background-color: green;
    color: green;
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
            <li><a href="index.php">Home</a></li>
            <li><a href="powerpanel.php">Power panel</a></li>
            <li><a href="status.php">Service status</a></li>
            <li><a href="plot.php">Live plot</a></li>
            <li><a href="setup.php" class="active">Dos Setup</a></li>
        </ul>
        </div>
    </nav>
    <br><br>
    <script type="text/javascript">
    function serviceHandler(serviceName,action){
      //console.log(serviceName);
      //console.log(action);
      var oReq = new XMLHttpRequest();
      oReq.onload=function(){};
      oReq.open("get", "serviceHandler.php?serviceName="+serviceName+"&action="+action, true);
      oReq.send();
    }
    </script>
    <?php
    $row= '<div align="center" id="%s">
        <span class="service badge badge-pill badge-primary" style="height:50px;width:95px;">%s</span>&nbsp;&nbsp;&nbsp;
        <span class="status badge badge-pill badge-warning" style="">&nbsp</span>&nbsp;<span class="badge badge-pill" style="background-color:white;color:black;width:80px;font-size:18;padding: 0px 3px 0 3px;">Running</span>&nbsp;&nbsp;
        <!--<button type="button" class="btn btn-primary btn-lg active">Device</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-warning btn-lg active">Status</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
        <button type="button" class="btn btn-info btn-lg active" onClick = "serviceHandler(\'%s\',1);">Start</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active" onClick = "serviceHandler(\'%s\',3);">Restart</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-info btn-lg active" onClick = "serviceHandler(\'%s\',2);">Stop</button>&nbsp;&nbsp; 
        <!--start = 1, stop = 2, restart = 3-->
        <br><br>
    </div>';
    foreach ($services as $s){
      echo sprintf($row,$s,$s,$s,$s,$s);
    }
    ?>
    <script type="text/javascript">
    var DATA="";
      function startRequest(){
        var data;
        var oReq = new XMLHttpRequest();
        oReq.onload = function() {
            data=this.responseText; 
            try{
            	DATA = JSON.parse(data);
              for (var service in DATA ){
                //console.log(service);
                //console.log(DATA[service]);
                if(DATA[service]=="0"){
                  //console.log("if");
                  document.getElementById(service).childNodes[3].style["background-color"]="red";
                  document.getElementById(service).childNodes[5].textContent=" Dead ";
                }
                else{
                  //console.log("else");
                  document.getElementById(service).childNodes[3].style["background-color"]="green";
                  document.getElementById(service).childNodes[5].textContent=" Running ";
                }
              }
            }
            catch(error){
            }
            //console.log("Json Data :"+DATA);
        };
        oReq.open("get", "serviceHandler.php?serviceName=all", true);
        oReq.send();
    }
    startRequest();
    setInterval(startRequest,500);
    </script>
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
