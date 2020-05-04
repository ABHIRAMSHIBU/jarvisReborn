<?php  require 'auth.php' ?>
<html>
<head>
<?php  require 'config.php' ?> 
<meta name="viewport" content="width=device-width, initial-scale=1">
<title> SAL POWER PANEL </title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title> SAL REMOTE UI </title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <script src="script/BootstrapMenu.min.js"></script>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="script/script.js"></script>
    <link rel="stylesheet" href="css/styles.css">
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
    <body>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!--<div class="navbar-header">
                <a class="navbar-brand" href="#">WebSiteName</a>
            </div>-->
        <ul class="nav navbar-nav">
            <li><a href="index.php">Home</a></li>
            <li class="active"><a href="powerpanel.php">Power panel</a></li>
            <li><a href="status.php">Service status</a></li>
            <li><a href="plot.php">Live plot</a></li>
            <li><a href="setup.php">Dos Setup</a></li>
        </ul>
        </div>
    </nav>
    <div>
    <br><br>
        <div>
            <table class="table table-striped table-bordered table-hover" style="width:60%;margin-left:20%;">
            <thead >
                <tr class="">
                <th scope="col">MCU ID</th>
                <th scope="col">IP_ADDRESS</th>
                <th scope="col">PINS</th>
                <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>192.16.187.131</td>
                    <td>12</td>
                    <td><a>Edit</a></td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>192.16.187.131</td>
                    <td>13</td>
                    <td><a>Edit</a></td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>192.16.187.131</td>
                    <td>15</td>
                    <td><a>Edit</a></td>
                </tr>
            </tbody>
            </table>
            <button type="button" class="btn btn-success" style="margin-left:45%;">ADD</button>
        </div>
    </div>
    <footer class="footer" style="margin-top:10%;">
            <div class="row justify-content-center">             
                    <div class="col-auto " align="center">
                        <p> © Copyright 2019 BrainNet Technologies</p>
                    </div>
               </div>
    </footer>
</body>
</html> 
