<?php

    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    echo "<br>From test.php<br>";
    var_dump($socket);
for($i=0;$i<10;$i++){
	if(isset($_POST[$i]) &&$_POST[$i] == 'Yes')
	{
	    setPin($socket, '1',$i,'1','10');
	    echo "Need wheelchair access.<br>";
	}
	else
	{
        setPin($socket, '1',$i,'0','10');
	    echo "Do not Need wheelchair access.<br>";
    }
}
endSocket($socket);
echo "Wait, your request is processing!";

?>
