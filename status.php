<?php  require 'auth.php' ?>
<?php  require 'config.php' ?> 
<?php $selected =2; require('header.php'); ?>
<link rel="stylesheet" href="css/status_style.css">
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
    <br><br><br>
    <?php require("footer.php")?>
