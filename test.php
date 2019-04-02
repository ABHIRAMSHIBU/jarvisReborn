<?php
    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    $reply=getPin($socket, 1, 10,10);
    echo "<br>From test.php<br>";
    var_dump($socket);
    echo "<br>";
    echo $reply;
    echo "<br>";
    resetESP($socket, '1');
    endSocket($socket);
?>