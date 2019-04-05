<html>
<head>
    <title> SAL REMOTE UI </title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title> SAL REMOTE UI </title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
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
            <div class="addbutton">
                    <button class="button button1 btn-primary" onclick="popUP();">+</button>
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
     <form method="post" action="switches.php" id="hiddenForm" hidden>
		<input type="hidden" name="devID" value="" ></input>
</form>
	<footer class="footer">
            <div class="row justify-content-center">             
                    <div class="col-auto " align="center">
                        <p> © Copyright 2018 SSAL Automation</p>
                    </div>
               </div>
    </footer>
</body>
</html>
