<?php
    require 'config.php';
    $type=($_GET["type"]);
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    $result=null;
    if($type=="0"){
        $room_name=($_GET["room_name"]);
        $room_number=($_GET["room_number"]);
        $sql = "insert into ssal_rooms values ($room_number,'$room_name')";
        $result = $conn->query($sql);;
    }
    else if($type=="1"){
        $devID=$_GET['devID'];
        $pin_name=($_GET["pin_name"]);
        $pin_number=($_GET["pin_number"]);
        $sql = "insert into ssal_switches values ($devID,$pin_number,'$pin_name')";
        $result = $conn->query($sql);
    }
    else{
        echo "Type error, GOT ".$type;
    }
    if($result)
    {
    echo "Success";
    echo "<script> window.location='.'; </script>";

    }
    else
    {
    echo "Error";
    echo "<script> alert('Error add room'); window.location='.';</script>";

    }

    $conn->close();
?>
