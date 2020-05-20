<?php  require 'auth.php' ?>
<html>
<head>
<?php  require 'config.php' ?> 
<?php 
    if(!empty($_POST)){
        //echo("Works!");
      // Do the request
        if(isset($_POST['id']) && !isset($_POST["ip"])){
            //Deletion code
            $conn = new mysqli($servername, $username, $password, $dbname);
            // Check connection
            if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
            }
            $sql = "DELETE FROM `ssal_ips` WHERE `ssal_ips`.`id` = ".$_POST['id'].";";
            $result = $conn->query($sql);
            //var_dump($sql);
        }
        if(isset($_POST['room_id']) && isset($_POST["ip_addr"]) && isset($_POST["pins"]) && isset($_POST["dos_hardware"])){
            // var_dump($_POST);
            $conn = new mysqli($servername, $username, $password, $dbname);
            if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
            }
            $sql = "INSERT INTO  `ssal_ips`(`id`, `IP`, `pins`, `DOS`) VALUES (".$_POST['room_id'].",\"".$_POST['ip_addr']."\",\"".$_POST['pins']."\",".$_POST["dos_hardware"].")";
            $result = $conn->query($sql);
            //var_dump($result);
            if($result){
                //Do something YAY success
            }
            else{
                //Do something XoX fail;;
            }
        }
    }


?>
<script type="text/javascript">
    // function delete_item(id){
    //     console.log(id);
    // }
    function delete_item(id){
        f=document.createElement("form");
        f.action="ipsetup.php";
        f.method="post";
        var hidden=document.createElement("input");
        hidden.type="hidden";
        hidden.name="id";
        hidden.value=id;
        // console.log([relays[i],countOnes(relays[i])]);
        f.appendChild(hidden);
        document.body.appendChild(f);
        //console.log("Form will submit");
        f.submit();
        return 1;
    }
    function add_item(element){
        f=document.createElement("form")
        arr=element.parentNode.parentNode.childNodes;
        f.method="POST";
        for(var i=0;i<arr.length-1;i++){
            node=document.createElement("input")
            node.type="hidden"
            //console.log(arr[i]);
            txt = arr[i].childNodes[0].childNodes[0]
            console.log(txt);
            node.name=txt.name;
            node.value=txt.value;
            f.appendChild(node);
        }
        document.body.appendChild(f);
        f.submit()
    }
    function add_config(){
        var table_html = document.getElementById("table_config");
        $(table_html).find('tbody').append("<tr>"+
                                            "<td><center><input type=\"text\" name=\"room_id\"></input></center></td>"+
                                            "<td><center><input type=\"text\" name=\"ip_addr\"></input></center></td>"+
                                            "<td><center><input type=\"text\" name=\"pins\"></input></center></td>"+
                                            "<td><center><select name='dos_hardware'><option value='1'>Yes</option><option value='0' selected>No</option></select></center></td>"+
                                            "<td><input class=\"btn btn-info btn-lg\"  type=\"button\" value=\"Apply\" onclick=add_item(this);></input></td>"+
                                            "</tr>");
    }
</script>
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
            <li><a href="setup.php">Dos Setup</a></li>
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
    <?php
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $sql = "SELECT * from ssal_ips";
    $result = $conn->query($sql);
    ?>
    <div>
            <table class="table table-hover" style="width:60%;margin-left:0%;" id="table_config">
            <thead >
                <tr class="">
                <th scope="col"><center>Room ID</center></th>
                <th scope="col"><center>IP</center></th>
                <th scope="col"><center>PINS</center></th>
                <th scope="col"><center>DOS Hardware</center></th>
                <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <?php 
                while($row = $result->fetch_assoc()){
                 ?>
                <tr>
                    <td><center><?php echo($row['id']); ?></center></td>
                    <td><center><?php echo($row['IP']); ?></center></td>
                    <td><center><?php echo($row['pins']); ?></center></td>
                    <td><center><?php 
                        if($row['DOS']==1){
                            echo("Yes");
                        } 
                        else{
                            echo("No");
                        }
                    ?><center></td>
                    <td><button type="button" class="btn btn-danger" onclick="delete_item(<?php echo($row['id']); ?>);">Delete</button></td>
                </tr>
                <?php 
                } ?>
                <!-- <tr>
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
                </tr> -->
            </tbody>
            </table>
            <center>
            <button type="button" class="btn btn-success" onclick="add_config()">Add</button>
            </center>
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
