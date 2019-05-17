<?php

    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    $devID=$_GET['devID'];
    $pinState=[0=>0];
    for($i=1;$i<=10;$i++){
        $pinState[$i]=getPin($socket,$devID,$i,10);
    }
    #var_dump($pinState);
    echo json_encode($pinState);
?>
