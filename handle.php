<?php
    require 'ssal_client.php';
    require 'config.php';
    $socket = startSocket($ip,$port);
    echo "<br>From test.php<br>";
    var_dump($socket);
    $devID=$_POST['devID'];
    //setPin($socket, $devID, $pinNo, $state, $retries)
    $pinState=[0=>0];
    for($i=1;$i<=10;$i++){
        $pinState[$i]=getPin($socket,$devID,$i,10);
    }
    echo "<br>";
    for($i=1;$i<=10;$i++){
        if($_POST[$i]=="on"){
            if($pinState[$i]==0){
                setPin($socket, $devID,$i,'1','10');
                echo "Device $devID, pin $i set to 1 <br>";
            }
        }
        else{
            if($pinState[$i]==1){
                setPin($socket, $devID,$i,'0','10');
                echo "Device $devID set to 0 <br>";
             }   
        }
    }
    endSocket($socket);
    echo "Wait, your request is processing!";
    //header("location: switches.php");
?>
<br>
<input type="button" value="Go Back" onclick="window.close();">
<script type="text/javascript">
    window.close();
</script>
