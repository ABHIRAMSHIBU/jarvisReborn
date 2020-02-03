<html>
<head>
<?php require 'auth.php' ?>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
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
    function startRequest(){
        var data;
        var oReq = new XMLHttpRequest();
        oReq.onload = function() {
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
            for(var i=0;i<10;i++){
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
	<link rel="stylesheet" href="css/styles.css">
</head>
<body bgcolor="black">
	<div class=popup id=popup hidden=true>
        <div class=inpopup id=inpopup>
        </div>
    </div>
    <font color="white">
    <h2>Toggle Switch</h2>
    </font>
    <form target="_blank" action="handle.php" method="post">
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
	        echo "<span class='sldr' style='color: white; font-size: 45px;'>$name\t:\t</span>";
	        echo '<label class="switch">';
	        echo "<input type='checkbox' name='$id' id='$id' class='chkbox' />";
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
	    <div class="center">
    	    <span class="button">
    	    <input type="submit" class = "button" style="vertical-align:middle" value="Submit" ></input>
    	    </span>
    	    <br>
    	    <?php 
    	    echo "<span class='sldr' style='color: white; font-size: 45px;'>Auto Update\t:\t</span>";
	        echo '<label class="switch">';
	        echo "<input type='checkbox' id='update' class='chkbox' onclick='setUpdate();' checked=true/>";
	        echo '<span class="slider round"></span>';
	        echo '</label>';
	        ?>
		</div>
	</form>
	<div class="addbutton">
    	<button class="button roundbutton" onclick="popUP();">+</button>
	</div>
	<div class="rembutton">
    	<button class="button roundbutton" onclick="popUP2();">-</button>
	</div>
</body>
</html> 
