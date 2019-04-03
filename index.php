<html>
<head>
    <title> SAL REMOTE UI </title>
    <style>
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
    </style>
    <script>
        var popE=false;
        function loadDoc() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
            document.getElementById("demo").innerHTML = this.responseText;
            }
        };
        xhttp.open("GET", "demo_get.asp", true);
        xhttp.send();
        }
        function popUP(){
           if(popE==false){
                popE=true;
                var pop=document.getElementById("popup");
                pop.style.display="block";
                var inpop=document.getElementById("inpopup")
                inpop.innerHTML="Add ROOM";
                inpop.innerHTML+="<form method=GET action='add.php'><br><br><label>Enter room name:<input type='text' name='room_name'></input></label><br><br><label>Enter room number:<input type='text' name='room_number'></input></label><br><br><center><input type='Submit' class='button' value='Add'></input></center></form>";
            }
            else{
                var pop=document.getElementById("popup");
                pop.style.display="none";
                var inpop=document.getElementById("inpopup")
                inpop.innerHTML="";
                popE=false;
            }
        }
    </script>
</head>
<body>
    <div class=popup id=popup>
        <div class=inpopup id=inpopup>
        </div>
    </div>
    <div class=header>
        <div class=headerEle>
        SSAL REMOTE UI
        </div>
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
        echo "<table  width=100% height=100%>";
        $startFlag=true;
        while($row = $result->fetch_assoc()) {
            //echo "id: " . $row["id"]. " - Name: " . $row["name"]. "<br>";
            if($i%4==0){
                echo " ";
                if(startFlag!=true){
                    echo "</tr>";
                }
                echo "<tr align=center>";
                $startFlag=false;
            }
            $i+=1;
            echo "<td>";
            echo "<button class='button medRoundbutton' name=".$row["id"].">";
            echo $row["name"];
            echo "<br><br>";
            echo $row["id"];
            echo '</button>';
            //echo '<br>';
            echo "</td>";
        }
    } else {
        echo "0 results";
    }
    $conn->close();
    ?>
    <div class="addbutton">
        <button class="button roundbutton" onclick="popUP();">+</button>
    </div>
</body>
</html>
