<?php
require_once "config.php";
$cookie_name="auth";
$conn = new mysqli($servername, $username, $password, $dbname);
$auth=false;
error_log("auth.php");
if(!isset($_COOKIE["auth"])) {
  //redirect
  error_log("1");
  header("location: login.php");
  $auth=false;
}
else{
    $auth=$_COOKIE[$cookie_name];
    $sql="select * from userinfo where session = \"".$auth."\";";
    $result = $conn->query($sql);
    if($result->num_rows > 0){
       // header("location: login.php");
       //  error_log("2");
        $auth=true;
    }
    else{
        header("location: login.php");
        $auth=false;
    }
}
?>
