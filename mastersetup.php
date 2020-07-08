<?php  require 'auth.php' ?>
<?php  require 'config.php' ?>
<?php $selected=6; require("header.php"); ?>

 <?php
//select ssal_rooms.id,ssal_rooms.name,ssal_ips.DOS from ssal_rooms,ssal_ips where ssal_rooms.id=ssal_ips.id
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "select ssal_rooms.id,ssal_rooms.name,ssal_ips.DOS from ssal_rooms,ssal_ips where ssal_rooms.id=ssal_ips.id;";
$result = $conn->query($sql);

?>

    <div>
    <br><br>
        <div>
            <table class="table table-striped table-hover" style="width:60%;margin-left:20%;">
            <tbody>
            <?php
                if ($result->num_rows > 0) {
                    while($row = $result->fetch_assoc()) {
                        if($row['DOS']=='1'){

            ?>
                <tr>
                    <td id='<?php echo($row['id']); ?>' ><?php echo($row['name']); ?></td>
                    <td><button type="button" class="btn btn-danger" style="margin-left:45%;" onclick="goToConfig(this);">Setup</button></td>
                    <td><button type="button" class="btn btn-success" style="margin-left:45%;" onclick="goToStatus(this);">Status</button></td>
                </tr>
                <?php
                        }
                }
            }
                ?>
            </tbody>
            </table>
        </div>
    </div>
    <script type="text/javascript" src="script/DOS.js"></script>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
<?php require("footer.php") ?>
