<?php

    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    echo "<br>From test.php<br>";
    var_dump($socket);
    $devID=$_POST['devID'];
    //setPin($socket, $devID, $pinNo, $state, $retries)
    for($i=0;$i<10;$i++){
    	if($_POST[$i]=="on")
    	{
    	    setPin($socket, $devID,$i,'1','10');
    	    echo "Need wheelchair access.<br>";
    	}
    	else
    	{
            setPin($socket, $devID,$i,'0','10');
    	    echo "Do not Need wheelchair access.<br>";
        }
    }
    endSocket($socket);
    echo "Wait, your request is processing!";
?>
