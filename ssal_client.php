<?php
    //SSAL Java Client API Handler.
    require "auth.php";
    function startSocket($ip,$port){
        $service_port = $port;
        $address = $ip;
        $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);

        if ($socket === false) {
            echo "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
        } else {
//            echo "Socket created.\n";
        }
//         echo "Attempting to connect to '$address' on port '$service_port'...";
        $result = socket_connect($socket, $address, $service_port);
        if ($result === false) {
            echo "socket_connect() failed.\nReason: ($result) " . socket_strerror(socket_last_error($socket)) . "\n";
        } else {
//             echo "Socket connected.\n";
        }
        //Read SSAL Command line ready and trash it
        socket_read($socket, 500);
        return $socket;
    }
    function echoSocket($socket,$data){
        //Writing data into socket.
         socket_write($socket, $data."\r\n");
        //Reading the response from socket.
        return socket_read($socket, 500);
    }
    function endSocket($socket){
//         echo "Closing socket...";
        socket_close($socket);
//         echo "OK.\n\n";
    }
    function setPin($socket,$devID,$pinNo,$state,$retries){
        if($retries==-1){
            return "Error";
        }
        $expError=">Not allowed! ";
        $string='$set ';
        $string.=(string)$pinNo;
        $string.=" ";
        $string.=(string)$state;
        $string.=" ";
        $string.=(string)$devID;
        //var_dump($string);
        $expOut='>';
        $expOut.=$pinNo;
        if((int)$state == 1){
            $expOut.=" 1\n";
        }
        else{
            $expOut.=" 0\n";
        }
        //file_put_contents('php://stderr', print_r($string, TRUE));
        $reply=echoSocket($socket, $string);
        if(trim($reply)==trim($expError)){
            resetESP($socket,$devID);
            sleep(0.2);
            return setPin($socket,$devID,$pinNo,$state,$retries-1);
        }
        if($reply==$expOut){
            return $reply;
        }
        return setPin($socket,$devID,$pinNo,$state,$retries-1);
    }
    function getPin($socket,$devID,$pinNo,$retries){
        if($retries==-1){
            return "Error";
        }
        $string='$get ';
        $string.=$pinNo." ";
        $string.=$devID;
        //var_dump($string);
        $expOut='>';
        $expOut1=$expOut."true\n";
        $expOut2=$expOut."false\n";
        $reply=echoSocket($socket, $string);
        if($expOut1==$reply){
            return 1;
        }
        else if($expOut2==$reply){
            return 0;
        }
        return getPin($socket,$devID,$pinNo,$retries-1);
    }
    function resetESP($socket,$devID){
        $string='$reset ';
        $string=$string.$devID;
        //file_put_contents('php://stderr', print_r($string, TRUE));
        return echoSocket($socket, $string);
    }
    function setrelaycfg($socket,$relayNo,$value,$devID){
        // echo "devId=".$devID."<br>";
        $command='$relaycfg '.$relayNo." ".$value." ".$devID;
        $expOut=">OK\n";
        $reply=echoSocket($socket, $command);
        if($expOut==$reply){
            return true;
        }
        else{
            return false;
        }
    }
    function getrelaycfg($socket,$relayNo,$devID){
        $command='$relaycfg '.$relayNo." ".$devID;
        $reply=echoSocket($socket, $command);
        return $reply;
    }
    function getrelay($socket,$relayNo,$devID){
        $command='$getrelay '.$relayNo." ".$devID;
        $reply=echoSocket($socket, $command);
        return $reply;
    }
    function setrelay($socket,$relayNo,$value,$devID){
        $command='$setrelay '.$relayNo." ".$value." ".$devID;
        $expOut=">OK\n";
        $reply=echoSocket($socket, $command);
        if($expOut==$reply){
            return true;
        }
        else{
            return false;
        }
    }
?>
