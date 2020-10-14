<?php
    require 'ssal_client.php';
    require 'config.php';
    require 'auth.php';
    $relayID=$_GET["relayID"];
    $roomID=$_GET["roomID"];
    $socket = startSocket($ip,$port);
    $output=getrelay($socket,$relayID,$roomID);
    $output=substr($output,0,2);
    //var_dump($output[1]);
    $responce=json_encode("ERROR");
    if($output==">0"){
        $responce=json_encode("off");
    }
    elseif($output==">1"){
        $responce=json_encode("Grid");
    }
    elseif($output==">2"){
        $responce=json_encode("Solar");
    }
    else{
        $responce=json_encode("Error");
    }
    echo($responce);
?>