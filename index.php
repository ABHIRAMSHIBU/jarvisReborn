<?php
    $selected=0;
    require 'header.php';
?>

    <form method="post" action="switches.php" id="hiddenForm" hidden=true>
		<input type="hidden" name="devID" value="" ></input>
	</form>
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
        //echo "<button type=\"button\" class=\"btn btn-danger\">Danger</button>";
        echo "<div class='row'><br><br><br></div>";
        echo "<div class='row'><br><br>";
        echo "<a href=\"logout.php\"><div class='row'><br><br><center><button onclick=\"logout.php\" type=\"button\" class=\"btn btn-danger\">Logout</button></center><br></div></a>";
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
    </div>
<?php require("footer.php")?>
