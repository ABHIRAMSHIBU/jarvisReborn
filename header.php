<?php
require "auth.php";
//echo("<!DOCTYPE html>");  // RIP html5
//$selected is already defined...
//$selected is 0 then home
//$selected is 1 then powerpanel
//$selected is 2 then statis
//$selected is 3 then plot
//$selected is 4 then profile
//$selected is 5 then dos


$pages = array(
    "0"=>array("index.php","Home"),
    "2"=>array("status.php","Service status"),
    "3"=>array("plot.php","Live plot"),
    "4"=>array("editprofile.php","Profile"),
    "5"=>array("ipsetup.php","IP Setup"),
    "6"=>array("mastersetup.php","DOS")
);
?>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
    <title> SAL REMOTE UI </title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title> SAL REMOTE UI </title>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <script src="css/jquery.min.js"></script>
    <script src="css/bootstrap.min.js"></script>
    <script src="script/BootstrapMenu.min.js"></script>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="script/script.js"></script>
</head>
<body>
    <header class="jumbotron" style="margin-bottom:0px;" >
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-2"></div>
                    <div class="col-md-6">
                        <h1>SSAL Remote Dashboard</h1>
                    </div>
                    <div class="col-md-4">
                        <img src="img/ssal.png" class="img-fluid" style="height:20%">
                    </div>
                </div>
            </div>
    </header>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!--<div class="navbar-header">
                <a class="navbar-brand" href="#">WebSiteName</a>
            </div>-->
        <ul class="nav navbar-nav">
        <?php
        $counter=0;
        $i=0;
            while($counter < count($pages)){
                //var_dump($counter);
                //echo(" ");
                //var_dump($i);
                //echo("<br>");
                if(isset($pages[$i.""])){
                    if($i==$selected){
                        echo("<li class='active'><a href='".$pages[$i.""][0]."'>".$pages[$i.""][1]."</a></li>");

                    }
                    else{
                        echo("<li><a href='".$pages[$i.""][0]."'>".$pages[$i.""][1]."</a></li>");
                    }   
                    $counter++;
                }
                $i++;
            }
            ?>


        </ul>
        </div>
    </nav>
 