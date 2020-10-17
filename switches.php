<?php require 'auth.php' ?>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
.popup{
    width:100%;
    height:100%;
    opacity:.95;
    display:none;
    position:fixed;
    background-color:#313131;
    overflow:auto;
    font-color:white;
}
.inpopup{
    font-size:40px;
    text-align:center;
    color:white;
}
.button {
  display: inline-block;
  border-radius: 4px;
  background-color: #1C2833;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 28px;
  padding: 20px;
  width: 200px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
}

.button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.button:hover span {
  padding-right: 25px;
}
.button:hover{
    color:green;
}
.button:hover span:after {
  opacity: 1;
  right: 0;
}
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input {display:none;}

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
.center {
    text-align:center;
}
.addbutton{
    position:fixed;
    right:5%;
    bottom:5%;
}
.rembutton{
    position:fixed;
    left:5%;
    bottom:5%;
}
.roundbutton {
    border-radius: 50%;
    width: 70px;
    height: 70px;
}

.footer {
    background-color: #F1C40F;
    margin: 0px;
    padding: 20px 0px 20px 0px;
}

h2{
    text-align:center;   
    font-size:40px;
}
hr{
    color:lightgreen;
    background: lightgreen;
    height: 2px;
}
body{
    font-family: Avantgarde, TeX Gyre Adventor, URW Gothic L, sans-serif;
}
</style>
	<script>
        if(typeof(String.prototype.trim) === "undefined")
        {
            String.prototype.trim = function() 
            {
                return String(this).replace(/^\s+|\s+$/g, '');
            };
        }
		var popE=false;
    	function popUP(){
            if(popE==false){
                 popE=true;
                 var slider=document.getElementsByClassName("slider")
                 var pop=document.getElementById("popup");
                 for(var i=0;i<slider.length;i++){ 
                     slider[i].hidden=true; 
                 }
                 pop.style.display="block";
                 var inpop=document.getElementById("inpopup")
                 inpop.innerHTML="Add PIN";
                 var tempStr="";
                 tempStr+="<input type='hidden' name='type' value=1>";
                 tempStr+="<input name='devID' type='hidden' value=<?php $devID=$_POST['devID']; echo $devID;?>>";
                 tempStr+="<label>Enter pin name:<input type='text' name='pin_name'></input></label><br><br>";
                 tempStr+="<label>Enter pin number:<input type='text' name='pin_number'></input></label><br><br>";
                 tempStr+="<center><input type='Submit' class='button' value='Add'></input></center>";
                 inpop.innerHTML+="<form method=GET action='add.php'><br><br>"+tempStr+"</form>";
           }
             else{
                 var pop=document.getElementById("popup");
                 var slider=document.getElementsByClassName("slider")
                 for(var i=0;i<slider.length;i++){ 
                     slider[i].hidden=false; 
                 }
                 pop.style.display="none";
                 var inpop=document.getElementById("inpopup")
                 inpop.innerHTML="";
                 popE=false;
             }
        }
    	function popUP2(){
            if(popE==false){
                 popE=true;
                 var slider=document.getElementsByClassName("slider")
                 var pop=document.getElementById("popup");
                 for(var i=0;i<slider.length;i++){ 
                     slider[i].hidden=true; 
                 }
                 pop.style.display="block";
                 var inpop=document.getElementById("inpopup");
                 var checkBoxes=document.getElementsByClassName("chkbox");
                 var sldrs=document.getElementsByClassName("sldr")
                 inpop.innerHTML="Remove PIN";
                 var tempStr2="";
                 tempStr2+="<input type='hidden' name='type' value=3>";
                 tempStr2+="<input name='devID' type='hidden' value=<?php $devID=$_POST['devID']; echo $devID;?>>";
                 //inpop.innerHTML+="<label>Enter pin name:<input type='text' name='pin_name'></input></label><br><br>";
                 //inpop.innerHTML+="<label>Enter pin number:<input type='text' name='pin_number'></input></label><br><br>";;
                 var tempStr="";
                 for(var i=0;i<checkBoxes.length;i++){ 
                	 tempStr+="<option value='"+checkBoxes[i].name+"'>"+sldrs[i].innerHTML.split(":")[0].trim()+" "+checkBoxes[i].name+"</option>";
                 } 
                 tempStr2+="<select name='pin_number'>"+tempStr+"</select><br><br>";
                 tempStr2+="<center><input type='Submit' class='button' value='Remove'></input></center>";
                 inpop.innerHTML+="<form method=GET action='add.php'><br><br>"+tempStr2+"</form>";
            }
             else{
                 var pop=document.getElementById("popup");
                 var slider=document.getElementsByClassName("slider")
                 for(var i=0;i<slider.length;i++){ 
                     slider[i].hidden=false; 
                 }
                 pop.style.display="none";
                 var inpop=document.getElementById("inpopup")
                 inpop.innerHTML="";
                 popE=false;
             }
        }
    function reqListener () {
      console.log(this.responseText);
    }
    var update=true;
    function setUpdate(){
        update=document.getElementById('update').checked;
        if(update==true){
            startRequest();
        }
    }
    var date=Date.now()-5000;
    function sleep(milliseconds) {
        let currentDate = null;
        do {
            currentDate = Date.now();
        } while (currentDate - date < milliseconds);
    } 
    function delay_refresh(id){
        x = document.getElementById('form_switches')
        x.submit()
        date = Date.now()-4500;
    }
    function check_timer(){
        currentDate = Date.now()
        if(currentDate-date< 5000){
            update=false;
        }
        else{
            update=true;
        }
    }
    function startRequest(){
        var data;
        var oReq = new XMLHttpRequest();
        oReq.onload = function() {
            check_timer();
            data=this.responseText; 
            try{
            	var DATA = JSON.parse(data);
            }
            catch(error){
                document.write("<head><title>SSAL System Error</title></head>");
                document.write("<h1 style='text-align:center;'>SSAL JSON Error</h1><br>")
            	document.write("<center>There seems to be multiple errors logged. Please check SSAL Core and reload the page</center><br>");
            	document.write('<center><img src="warning.png" width="10%"></center>');
				document.write("<div style='position:absolute; bottom:0;display:block;'>");
				document.write("<h2 style='text-align:center;'><font color='red'>SSAL core seems to be offline!</font></h2><br>")
				document.write("Possible fixes<br>")
				document.write("<ol>")
				document.write("<li>Try rebooting the SSAL System Server</li>")
				document.write("<li>Try logging on to the SSAL System Server and restart SSAL.service</li>")
				document.write("<li>Try updating the system </li>");
				document.write("</div>");
            	window.stop();
            	window.location.reload();
            }
            console.log("Json Data :"+DATA);
            for(var i=0;i<=10;i++){
                if(document.getElementById(i)!=null && update){
                    document.getElementById(i).checked=DATA[i];
                }
            }
        };
        oReq.open("get", "get.php?devID="+"<?php echo $devID; ?>", true);
        oReq.send();
    }
    startRequest();
    setInterval(startRequest,500);
	</script>
</head>
<body bgcolor="white">
	<div class=popup id=popup hidden=true>
        <div class=inpopup id=inpopup>
        </div>
    </div>
    <font color="black">
    <h2>Toggle Switch</h2>
    </font>
    <form target="_blank" action="handle.php" method="post" id="form_switches">
	    <?php
	    echo "<center><hr>";
	    $devID=$_POST["devID"];
	    echo "<input type='hidden' name='devID' value='$devID'>";
	    echo "<table cellpadding=20%>";
	    
	    require 'config.php';
	    // Create connection
	    $conn = new mysqli($servername, $username, $password, $dbname);
	    // Check connection
	    if ($conn->connect_error) {
	        die("Connection failed: " . $conn->connect_error);
	    }
	    
	    $sql = "SELECT * from ssal_switches where id=$devID";
	    $result = $conn->query($sql);
	    $sql_name = "SELECT name from ssal_rooms where id=$devID";
	    $result_name = $conn->query($sql);
	    file_put_contents('php://stderr', print_r($result, TRUE));
	    $i=0;
	    $start=true;
	    while($row = $result->fetch_assoc())
	    {file_put_contents('php://stderr', print_r($row, TRUE));
	        if($i%2==0){
	            if(!$start){
	                echo "</tr>";
	            }
	            echo "<tr>";
	        }
	        $name=$row['name'];
	        $id=$row['id_switch'];
	        echo "<td>";
	        echo "<span class='sldr' style='color: black; font-size: 45px;'>$name\t:\t</span>";
	        echo '<label class="switch">';
	        echo "<input type='checkbox' name='$id' id='$id' class='chkbox' onclick=\"delay_refresh($id)\"/>";
	        echo '<span class="slider round"></span>';
	        echo '</label>';
	        echo "</td>";
	        $i++;
	    }
	    
	    
// 	    for($i=0;$i<10;$i++){
// 	        if($i%2==0){
// 	            if(!$start){
// 	                echo "</tr>";
// 	            }
// 	            echo "<tr>";
// 	        }
// 	        echo "<td>";
//             echo "<span style='color: white; font-size: 45px;'>Pin $i\t:\t</span>";
//             echo '<label class="switch">';
//             echo "<input type='checkbox' name='$i' />";
//             echo '<span class="slider round"></span>';
//             echo '</label>';
//             echo "</td>";
//         }
        echo "</tr>";
        echo "</table>";
        echo "</center>"
	    ?>
	   <!-- <div class="center">
    	    <span class="button">
    	    <input type="submit" class = "button" style="vertical-align:middle" value="Submit" ></input>
    	    </span>
    	    <br>
		</div>-->
	</form>
	<div class="addbutton">
    	<button class="button roundbutton" onclick="popUP();">+</button>
	</div>
	<div class="rembutton">
    	<button class="button roundbutton" onclick="popUP2();">-</button>
	</div>
<!--	<footer class="footer" style="height:5%;">

            <div class="row justify-content-center">             
                    <div class="col-auto" align="center">
                        <p> © Copyright 2020 BrainNet Technologies</p>
                    </div>
               </div>
    </footer>-->
</body>
</html> 
