<?php
require "auth.php";
require "ssal_client.php";


if(true){
    echo(json_encode("success"));
    $devId = $_GET["devId"];
    $socket = startSocket("localhost",9998);
    for($i=0;$i<count($_GET)-1;$i++){
        $relayNo = $i;
        $value = $_GET[$relayNo];
        $retVal = setrelaycfg($socket,$relayNo+1, $value,$devId);
    }

?>

<?php
}
else{
    echo(json_encode("failure"));
?>

<?php
}
?>