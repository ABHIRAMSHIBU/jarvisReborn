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
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="script/script.js"></script>
    <link rel="stylesheet" href="css/styles.css">
<style>
.switch {
  position: relative;
  display: inline-block;
  width: 35px;
  height: 28px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: red;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 20px;
  width: 10px;
  left: 3px;
  bottom: 3px;
  background-color: ;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: green;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(20px);
  -ms-transform: translateX(20px);
  transform: translateX(20px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
</style>
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
            <li><a href="powerpanel.php">Power panel</a></li>
            <li><a href="status.php">Service status</a></li>
            <li><a href="plot.php">Live plot</a></li>
            <li class="active"><a href="setup.php">Dos Setup</a></li>
        </ul>
        </div>
    </nav>
    <br>
    <!-- <div class="card" style="width:100%;"> 
        <div class="card-body">
          Sucess 
        </div>
    </div> -->
    <b id="status"></b>
    <div align="center" >
    <br><br>
<!--     <h3 style="margin-left: 10%"><span class="badge badge-info">Air Conditioner</span> &nbsp; </h3> -->
    <!--<span class="badge badge-info"><bold>Air Conditioner</bold></span> &nbsp;&nbsp;<button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;
    <button type="button" class="btn btn-success btn-lg active">MAIN SUPPLY</button>&nbsp;&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="toggleOn()">
    <span class="slider round"></span>
    </label>
    <script>
        function toggleOn()
        {
                document.get
        }
    </script>
    <br><br>-->
    <form method="get" action="relayhandler.php">
    <?php
    for($i=0;$i<4;$i++){ 
    echo('
    <div id="'. ($i+1) .'">
    <button type="button" class="btn btn-warning btn-lg" disabled>Relay Pair&nbsp;'.($i+1).'</button>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;
    <label class="switch">
    <input type="checkbox" onclick="calculate('.($i+1).')">
    <span class="slider"></span>
    </label>&nbsp;&nbsp;
    <button type="button" class="btn btn-primary btn-lg" name="'.$i.'" style="Width:60px;" Value="0" disabled>0</button>
    </div>
    <br><br>');
    }
    ?>
    <button type="button" class="btn btn-success" onclick="createRequest()">Submit</button>
    </form>
    <br><br><br><br><br><br><br><br>
    </div>
    <footer class="footer">
            <div class="row justify-content-center">             
                    <div class="col-auto " align="center">
                        <p> © Copyright 2020 BrainNet Technologies</p>
                    </div>
               </div>
    </footer>
</body>
<script type="text/javascript" src="script/relay.js"></script>
</html> 
