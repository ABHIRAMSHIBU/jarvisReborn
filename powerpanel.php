<?php $selected=6;require 'header.php'; ?>
<?php  require 'config.php' ?> 

<link rel="stylesheet" href="css/powerpanel_style.css">
<?php
    require 'config.php';
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    if(!isset($_GET["devId"])){
        echo("<h1 style='text-align:center; color:red;'>Oops... Something happened try again..</h1>");
        exit();
    }
    $devId=$_GET["devId"];
    $sql = "SELECT relayPairID,name,id FROM `ssal_dos_devices` where id=".$devId.";";
    $result = $conn->query($sql);
    //var_dump($result);
    echo("<input type=hidden id=devID value=".$devId.">");
    ?>
    <div align="center" >
    <br><br>
    <div>
            <table class="table table-hover" style="width:60%;margin-left:0%;">
            <thead >
                <tr class="">
                <th scope="col">Device Name</th>
                <th scope="col">Power Source</th>
                <th scope="col">
                &nbsp;&nbsp;&nbsp;<svg class="bi bi-power" width="1.5em" height="1.5em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M5.578 4.437a5 5 0 104.922.044l.5-.866a6 6 0 11-5.908-.053l.486.875z" clip-rule="evenodd"/>
                <path fill-rule="evenodd" d="M7.5 8V1h1v7h-1z" clip-rule="evenodd"/>  
                </svg>
                </th>
                </tr>
            </thead>
            <tbody>

                <?php
                if ($result->num_rows > 0) {
                    while($row = $result->fetch_assoc()) {
                ?>
                <tr id=<?php echo($row["relayPairID"]); ?>>
                    <td><?php echo($row["name"]); ?></td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr>
                <?php
                    }
                }
                ?>
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
        </div>
    </div>
    <br>
    <br>
    <br>
<?php require("footer.php")?>
