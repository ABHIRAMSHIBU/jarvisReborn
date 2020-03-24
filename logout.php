<?php
require 'config.php';
if(!isset($_COOKIE["auth"])) {
  //redirect
  header("location:login.php");
  echo "<h1>You are already logged out</h1>";
}
else{
    $auth=$_COOKIE["auth"];
    $conn = new mysqli($servername, $username, $password, $dbname);
    $sql = "UPDATE `userinfo` SET `session` =NULL WHERE `userinfo`.`session` = \"".$auth."\";";
    header("location:login.php");
    var_dump($sql);
    var_dump($username);
    $conn->query($sql);
}

?>
