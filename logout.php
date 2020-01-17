<?php
require 'config.php';
var_dump(setcookie("auth", "INV", time()+-10, "/"));
if(!isset($_COOKIE["auth"])) {
  //redirect
  echo "<h1>You are already logged out</h1>";
}
else{
    $auth=$_COOKIE["auth"];
    $conn = new mysqli($servername, $username, $password, $dbname);
    $sql = "UPDATE `userinfo` SET `session` =NULL WHERE `userinfo`.`session` = \"".$auth."\";";
    var_dump($sql);
    var_dump($username);
    $conn->query($sql);
}

header("location:login.php");
?>
