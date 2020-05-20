<?php  require 'auth.php' ?>
<?php  require 'config.php' ?> 
<?php $selected=5; require("header.php"); ?>
<link rel="stylesheet" href="css/ipsetup_style.css">
<script type="text/javascript" src="script/ipsetup_script.js"></script>

<?php 
    if(!empty($_POST)){
        //echo("Works!");
      // Do the request
        if(isset($_POST['id']) && !isset($_POST["ip"])){
            //Deletion code
            $conn = new mysqli($servername, $username, $password, $dbname);
            // Check connection
            if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
            }
            $sql = "DELETE FROM `ssal_ips` WHERE `ssal_ips`.`id` = ".$_POST['id'].";";
            $result = $conn->query($sql);
            //var_dump($sql);
        }
        if(isset($_POST['room_id']) && isset($_POST["ip_addr"]) && isset($_POST["pins"]) && isset($_POST["dos_hardware"])){
            // var_dump($_POST);
            $conn = new mysqli($servername, $username, $password, $dbname);
            if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
            }
            $sql = "INSERT INTO  `ssal_ips`(`id`, `IP`, `pins`, `DOS`) VALUES (".$_POST['room_id'].",\"".$_POST['ip_addr']."\",\"".$_POST['pins']."\",".$_POST["dos_hardware"].")";
            $result = $conn->query($sql);
            //var_dump($result);
            if($result){
                //Do something YAY success
            }
            else{
                //Do something XoX fail;;
            }
        }
    }


?>
    <div align="center" >
    <br><br>
<!--     <h3 style="margin-left: 10%"><span class="badge badge-info">Air Conditioner</span> &nbsp; </h3> -->
    <!-- <span class="badge badge-info"><bold>Air Conditioner</bold></span> &nbsp;&nbsp;-->
    <!-- <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-success btn-lg active">MAIN SUPPLY</button>&nbsp;&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="toggleOn()">
    <span class="slider round"></span>
    </label>
    <script>
        function toggleOn()
        {
                document.get
        }
    </script>
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <button type="button" class="btn btn-success btn-lg active">MAIN SUPPLY</button>&nbsp;&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="toggleOn()">
    <span class="slider round"></span>
    </label>
    <script>
        function toggleOn()
        {
                document.get
        }
    </script>
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-success btn-lg active">MAIN SUPPLY</button>&nbsp;&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="toggleOn()">
    <span class="slider round"></span>
    </label>
    <script>
        function toggleOn()
        {
                document.get
        }
    </script>
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-success btn-lg active">MAIN SUPPLY</button>&nbsp;&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="toggleOn()">
    <span class="slider round"></span>
    </label>
    <script>
        function toggleOn()
        {
                document.get
        }
    </script>
    <br><br>
    <br><br><br><br><br><br><br><br>
    </div> -->
    <?php
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $sql = "SELECT * from ssal_ips";
    $result = $conn->query($sql);
    ?>
    <div>
            <table class="table table-hover" style="width:60%;margin-left:0%;" id="table_config">
            <thead >
                <tr class="">
                <th scope="col"><center>Room ID</center></th>
                <th scope="col"><center>IP</center></th>
                <th scope="col"><center>PINS</center></th>
                <th scope="col"><center>DOS Hardware</center></th>
                <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <?php 
                while($row = $result->fetch_assoc()){
                 ?>
                <tr>
                    <td><center><?php echo($row['id']); ?></center></td>
                    <td><center><?php echo($row['IP']); ?></center></td>
                    <td><center><?php echo($row['pins']); ?></center></td>
                    <td><center><?php 
                        if($row['DOS']==1){
                            echo("Yes");
                        } 
                        else{
                            echo("No");
                        }
                    ?><center></td>
                    <td><button type="button" class="btn btn-danger" onclick="delete_item(<?php echo($row['id']); ?>);">Delete</button></td>
                </tr>
                <?php 
                } ?>
                <!-- <tr>
                    <td>Air Conditioner</td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr>
                <tr>
                    <td>Air Conditioner</td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr> -->
            </tbody>
            </table>
            <center>
            <button type="button" class="btn btn-success" onclick="add_config()">Add</button>
            </center>
        </div>
    </div>
    <br>
    <br>
    <br>
    <br>
   <?php require("footer.php")?>