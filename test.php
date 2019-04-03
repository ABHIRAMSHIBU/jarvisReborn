<?php
    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    //$reply=getPin($socket, 1, 2,10);
    echo "<br>From test.php<br>";
    var_dump($socket);
    //echo "<br>";
    //echo $reply;
    //echo "<br>";
    $i=2;
    echo setPin($socket, '1',$i,'1','10');
    endSocket($socket);
?>
