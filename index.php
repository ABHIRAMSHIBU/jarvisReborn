<html>
<head>
    <title> SAL REMOTE UI </title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title> SAL REMOTE UI </title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <script src="script/BootstrapMenu.min.js"></script>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="script/script.js"></script>
    <!--<style>
        body{
            background:url("space.png");
            background-repeat: initial;
        }
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
        background-color: #4CAF50; /* Green */
        border: none;
        color: white;
        padding: 20px;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        font-size: 16px;
        margin: 4px 2px;
        display: block;
        cursor: pointer;
        font-family: Avantgarde, TeX Gyre Adventor, URW Gothic L, sans-serif;
        }
        .medRoundbutton{
        border-radius: 25%;
        background-image: linear-gradient(to bottom right,black, green);
        color:white;
        width:150px;
        height:100px;
        }
        .medRoundbutton:hover{
        border-radius: 25%;
        background-image: linear-gradient(to bottom right,red, green);
        color:white;
        width:150px;
        height:100px;
        }
        .roundbutton {
        border-radius: 50%;
        }
        .addbutton{
            position:fixed;
            right:5%;
            bottom:5%;
        }
        .header {
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        background-color: #333;
        }
        .headerEle {
        float: left;
        }
        .headerEle {
        font-size:25px;
        display: block;
        color: white;
        text-align: center;
        padding: 20px 16px;
        text-decoration: none;
        font-family: Avantgarde, TeX Gyre Adventor, URW Gothic L, sans-serif;
        }
    </style>-->
</head>
<body>
	<form method="post" action="switches.php" id="hiddenForm" hidden=true>
		<input type="hidden" name="devID" value="" ></input>
	</form>
    <header class="jumbotron">
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
    <div class="container">
        <div class="popup" id="popup" align="right">
                <div class="inpopup" id="inpopup">
                </div>
        </div>
        <div class="row row-content align-content-center">
            <div class="addbutton" id="addbutton">
                    <button class="button button1 btn-primary" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">+</button>
            </div>
            <div class="remButton" id="remButton">
                    <button class="button button1 btn-primary" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModa2">-</button>
            </div>
    <?php
    require 'config.php';
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 

    $sql = "SELECT * from ssal_rooms";
    $result = $conn->query($sql);
    $i=0;
    if ($result->num_rows > 0) {
        // output data of each row
        //echo "<table  width=100% height=100%>";
        while($row = $result->fetch_assoc()) {
            //echo "id: " . $row["id"]. " - Name: " . $row["name"]. "<br>";
            if($i%4==0){
                echo " ";
                if($i!=0){
                    echo "</div>";
                }
                echo "<div class='row'><br><br><br></div><div class='row'>";
            }
            $i+=1;
            //echo "<td>";
            //<div class="col-md-3 col-sm-6"><button class="button medRoundbutton" name="1">Bed Room 1</button></div>
            echo "<div class='col-md-3 col-sm-6'><button class='button medRoundbutton' name=".$row["id"]." onclick=submit(".$row["id"].");".">";
            echo $row["name"]." ";
            echo $row["id"];
            echo '</button>';
            //echo '<br>';
            echo "</div>";
        }
        echo "<div class='row'><br><br><br></div>";
        echo "<div class='row'><br><br><br></div>";
        echo "</div>";
        echo "</div>";
        echo "</div>";
    } else {
        echo "0 results";
    }
    $conn->close();
    ?>
	<!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog">
        
          <!-- Modal content-->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">Add Room Dialogue</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                  <form style="text-align: center;" method=GET action='add.php'>
                  	<br><br>
                  	<input type="hidden" name="type" value="0">
                  	<label>Room Name:<input class="form-control" type='text' name='room_name' placeholder="Enter room name."></input></label><br>
                  	<small id="emailHelp" class="form-text text-muted">Example : Bed Room</small>
                  	<br><br>
                  	<label>Room Number:<input class="form-control" type='text' name='room_number' placeholder="Enter room number."></input></label><br>
                  	<small id="emailHelp" class="form-text text-muted">Example : 0</small>
                  	<br><br><input type='Submit' class='button' value='Add'></input>
                  </form>
                </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>
          
        </div>
    </div>
    <div class="modal fade" id="myModa2" role="dialog">
        <div class="modal-dialog">
        
          <!-- Modal content-->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">Remove Room Dialogue</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                  <form style="text-align: center;" method=GET action='add.php'>
                  	<br><br>
                  	<input type="hidden" name="type" value="2">
                  	<select class="form-control form-control-lg" id="sel1" name="devID">
                  		<?php 
                      		$conn = new mysqli($servername, $username, $password, $dbname);
                      		// Check connection
                      		if ($conn->connect_error) {
                      		    die("Connection failed: " . $conn->connect_error);
                      		} 
                  		    $result = $conn->query($sql);
                  		    if ($result->num_rows > 0) {
                  		        while($row = $result->fetch_assoc()) {
                  		            echo "<option value=";
                  		            echo $row['id'];
                  		            echo ">";
                  		            echo $row['name'];
                  		            echo " ";
                  		            echo $row['id'];
                  		            echo "</option>";
                  		        }
                  		    }
                  		    $conn->close();
                  		?>
                  	
                  	</select>
                  	
                  	<br><br><input type='Submit' class='button' value='Remove'></input>
                  </form>
                </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>
          
        </div>
    </div>
    
    <footer class="footer">
            <div class="row justify-content-center">             
                    <div class="col-auto " align="center">
                        <p> © Copyright 2019 BrainNet Technologies</p>
                    </div>
               </div>
    </footer>
</body>
</html>
