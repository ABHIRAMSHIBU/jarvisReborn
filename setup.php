<?php  require 'auth.php';
       require 'ssal_client.php'; ?>
<?php  require 'config.php' ?> 
<?php $selected=6; require("header.php"); ?>
<link rel="stylesheet" href="css/setup_style.css">
    <br>
    <b id="status"></b>
    <div align="center" >
    <br><br>
    <form method="get" action="relayhandler.php">
    <?php
    if(isset($_GET["devId"])==FALSE){
      echo("Oops.. Something went wrong...");
      exit();
    }
    $devId=$_GET["devId"];
    $socket = startSocket("localhost",9998);
    
    for($i=0;$i<4;$i++){ 
      $value =  getrelaycfg($socket,($i+1),$devId);
      $value =  substr($value,1);
      $value = intval($value);
      //var_dump($value);
      $valuebkp=$value;
    echo('
          <div id="'. ($i+1) .'">
          <button type="button" class="btn btn-warning btn-lg" disabled>Relay Pair&nbsp;'.($i+1).'</button>&nbsp;');
      for($j=0;$j<8;$j++){
        if($value!=0){
          $boolVal = $value & 128;
          $value = $value << 1;
        }
        else{
          $boolVal=FALSE;
        }
        if($boolVal){
          echo('
                <label class="switch">
                <input type="checkbox" checked=true onclick="calculate('.($i+1).')">
                <span class="slider"></span>
                </label>&nbsp;');
        }
        else{
          echo('
                <label class="switch">
                <input type="checkbox" onclick="calculate('.($i+1).')">
                <span class="slider"></span>
                </label>&nbsp;');
        }
      }
          echo('&nbsp;
          <button type="button" class="btn btn-primary btn-lg" name="'.$i.'" style="Width:60px;" value="'.$valuebkp.'" disabled>0</button>
          </div>
          <br><br>');
    }
    ?>
    <button type="button" class="btn btn-success" onclick="createRequest()">Submit</button>
    </form>
    <br><br><br><br><br>
    </div>
    <input type="hidden" id="devId" value="<?php echo($devId); ?>" >
    <script type="text/javascript" src="script/relay.js"></script>
    <?php require("footer.php"); ?>