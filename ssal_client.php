<?php
    function startSocket($ip,$port){
        $service_port = $port;
        $address = $ip;
        $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);

        if ($socket === false) {
            echo "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
        } else {
            echo "Socket created.\n";
        }
        echo "Attempting to connect to '$address' on port '$service_port'...";
        $result = socket_connect($socket, $address, $service_port);
        if ($result === false) {
            echo "socket_connect() failed.\nReason: ($result) " . socket_strerror(socket_last_error($socket)) . "\n";
        } else {
            echo "Socket connected.\n";
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
            $expOut.=" on\n";
        }
        else{
            $expOut.=" off\n";
        }
        //file_put_contents('php://stderr', print_r($string, TRUE));
        $reply=echoSocket($socket, $string);
        if($reply==$expOut){
            return $reply;
        }
        return setPin($socket,$devID,$pinNo,$state,$retries-1);
    }
    function getPin($socket,$devID,$pinNo,$retries){
        if($retries==-1){
            return "Error";
        }
        $string='$test ';
        $string.=$pinNo." ";
        $string.=$devID;
        //var_dump($string);
        $expOut='>';
        $expOut1=$expOut."1\n";
        $expOut2=$expOut."0\n";
        $reply=echoSocket($socket, $string);
        if($expOut1==$reply || $expOut2==$reply){
            return $reply;
        }
        return getPin($socket,$devID,$pinNo,$retries-1);
    }
    function resetESP($socket,$devID){
        $string='$reset ';
        $string=$string.$devID;
        //file_put_contents('php://stderr', print_r($string, TRUE));
        return echoSocket($socket, $string);
    }
?>
