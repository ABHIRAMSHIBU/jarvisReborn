<html>
<head>
    <title> SAL REMOTE UI </title>
    <style>
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
        cursor: pointer;
        }

        .roundbutton {border-radius: 50%;}
        .addbutton{
            position:absolute;
            right:5%;
            bottom:5%;
        }
    </style>
    <script>
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
    </script>
</head>
<body>
    <?php
    $servername = "localhost";
    $username = "ssal";
    $password = "PqOnei4xt973wToR";
    $dbname = "ssal";

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
        echo "<table border=1>";
        $startFlag=true;
        while($row = $result->fetch_assoc()) {
            //echo "id: " . $row["id"]. " - Name: " . $row["name"]. "<br>";
            if($i%3==0){
                echo " ";
                if(startFlag!=true){
                    echo "</tr>";
                }
                echo "<tr>";
                $startFlag=false;
            }
            $i+=1;
            echo "<td>";
            echo "<button name=".$row["id"].">";
            echo $row["name"];
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
        <button class="button roundbutton">+</button>
    </div>
</body>
</html>
