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
<style>
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
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
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
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
            <li class="active"><a href="powerpanel.php">Power panel</a></li>
            <li><a href="status.php">Service status</a></li>
            <li><a href="plot.php">Live plot</a></li>
            <!-- <li><a href="setup.php">Dos Setup</a></li> -->
            <li><a href="mastersetup.php">DOS</a></li>
        </ul>
        </div>
    </nav>
    <div align="center" >
    <br><br>
<!--     <h3 style="margin-left: 10%"><span class="badge badge-info">Air Conditioner</span> &nbsp; </h3> -->
    <!-- <span class="badge badge-info"><bold>Air Conditioner</bold></span> &nbsp;&nbsp;-->
    <!-- <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
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
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
    <br><br>
    <button type="button" class="btn btn-info btn-lg" disabled>Air Conditioner&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-danger btn-lg active">SOLAR SUPPLY</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
    <br><br>
    <br><br><br><br><br><br><br><br>
    </div> -->
    <div>
            <table class="table table-hover" style="width:60%;margin-left:0%;">
            <thead >
                <tr class="">
                <th scope="col">Device Name</th>
                <th scope="col">Power Source</th>
                <th scope="col">
                &nbsp;&nbsp;&nbsp;<svg class="bi bi-power" width="1.5em" height="1.5em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M5.578 4.437a5 5 0 104.922.044l.5-.866a6 6 0 11-5.908-.053l.486.875z" clip-rule="evenodd"/>
                <path fill-rule="evenodd" d="M7.5 8V1h1v7h-1z" clip-rule="evenodd"/>  
                </svg>
                </th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Air Conditioner</td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr>
                <tr>
                    <td>Air Conditioner</td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr>
                <tr>
                    <td>Air Conditioner</td>
                    <td>Grid</td>
                    <td>
                    <label class="switch">
                    <input type="checkbox" onclick="toggleOn()">
                    <span class="slider round"></span>
                    </label>
                    </td>
                </tr>
            </tbody>
            </table>
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
