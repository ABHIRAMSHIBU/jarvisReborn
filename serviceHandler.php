<?php require 'auth.php';
      require 'config.php'; ?>
<?php

    $serviceName = $_GET["serviceName"];
    $dumpAll=$_GET["DEBUG"];
    if($dumpAll){
        echo "serviceName: ";
        var_dump($serviceName);
        echo "<br>";
    }
    if(!(in_array($serviceName,$services) || $serviceName==="all")){
        echo json_encode("-1");
        return;
    }
    $action = $_GET["action"];
    if($dumpAll){
        echo "Action: ";
        var_dump($action);
        echo "<br>";
    }
    $action = intval($action);
    //var_dump($action);
    /* 
    0 - status
    1 - start
    2 - stop
    3 - restart
    */
    $command="";
    if($serviceName==="all"){
        $output=array();
        foreach ($services as $s){
            $result = exec("serviceControl --servicename ".$s);
            $output[$s]=$result;
        }
        echo json_encode($output);
    }
    else{
        switch($action){
            case 0:
                $command = "serviceControl --servicename ".$serviceName;
                break;
            case 1:
                $command = "serviceControl --servicename ".$serviceName." --action start";
                break; 
            case 2:
                $command = "serviceControl --servicename ".$serviceName." --action stop";
                break;
            case 3:
                $command = "serviceControl --servicename ".$serviceName." --action restart";
                break;
            default:
                $command = "";
        }
        if(empty($command)){

        }
        else{
            if($dumpAll){
                echo "command : ";
                var_dump($command);
                echo "<br>";
            }
            $result = exec($command);
            if($dumpAll){
                echo "result : ";
                var_dump($result);
                echo "<br>";
            }
            if($result==""){
                if($action==0){
                    echo json_encode("Error");
                }
            }
            else{
                echo json_encode($result);
            }
        }
    }
?>