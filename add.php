<?php
    $room_name=($_GET["room_name"]);
    $room_number=($_GET["room_number"]);
    $servername = "localhost";
    $username = "ssal";
    $password = "PZR9qiAxzOEYmZTu";
    $dbname = "ssal";

    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 

    $sql = "insert into ssal_rooms values ($room_number,'$room_name')";
    $result = $conn->query($sql);
    $i=0;
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
