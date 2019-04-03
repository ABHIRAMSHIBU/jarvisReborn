
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
.button {
  display: inline-block;
  border-radius: 4px;
  background-color: #f4511e;
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
</head>
<body bgcolor="black">
    <font color="white">
    <h2>Toggle Switch</h2>
    </font>
    <form action="handle.php" method="post">
	    <?php
	    echo "<center><hr>";
	    $devID=$_POST["devID"];
	    echo "<input type='hidden' name='devID' value='$devID'>";
	    echo "<table cellpadding=20%>";
	    $start=true;
	    for($i=0;$i<10;$i++){
	        if($i%2==0){
	            if(!$start){
	                echo "</tr>";
	            }
	            echo "<tr>";
	        }
	        echo "<td>";
            echo "<span style='color: white; font-size: 45px;'>Pin $i\t:\t</span>";
            echo '<label class="switch">';
            echo "<input type='checkbox' name='$i' />";
            echo '<span class="slider round"></span>';
            echo '</label>';
            echo "</td>";
        }
        echo "</tr>";
        echo "</table>";
        echo "</center>"
	    ?>
	    <div class="center">
    	    <span class="button">
    	    <input type="Submit" class = "button" style="vertical-align:middle" value="Submit" ></input></span></center>
		</div>
</form>
</body>
</html> 
