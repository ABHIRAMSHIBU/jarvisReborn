<!DOCTYPE link PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<?php 
if (empty($_SERVER['HTTPS'])) {
    echo "<font color='red'><center><h1> HTTP Access Denied! </h1></center></font>";
    $host=$_SERVER['HTTP_HOST'];
    $uri=$_SERVER['REQUEST_URI'];
    $index=strpos($host,":");
    if($index!=FALSE){
        $host=substr($host, 0,$index);
    }
    $link="https://".$host.$uri;
    echo "<center><a href='$link'>Try HTTPS</a></center>";
    exit;
}
?>
<HTML>
    <HEAD>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    </HEAD>
<link rel="stylesheet" href="login.css">
<?php 
require 'config.php';
$bytes = openssl_random_pseudo_bytes(64, $cstrong);
var_dump(bin2hex($bytes));
$auth=FALSE;
if(isset($_COOKIE['uuid'])){
    $conn = new mysqli($servername, $username, $password, $dbname);
    $result=$conn->query("select * from userinfo where session==".$_COOKIE['uuid'].";");
    if($result->num_rows>0){
        $auth=TRUE;
    }
}
else{
    setcookie("uuid",bin2hex($bytes), time()+60*60*24,"/","abhiramshibu.tk",TRUE,TRUE);
}
?>
<BODY>
	<script type="text/javascript" src="script/sha512.js"></script>
    <div class="wrapper fadeInDown">
      <div id="formContent">
        <!-- Tabs Titles -->
    
        <!-- Icon -->
        <div class="fadeIn first">
          <img src="icon.svg" id="icon" alt="User Login" />
        </div>
    
        <!-- Login Form -->
        <form action="login.php">
          <input type="text" id="login" class="fadeIn second" name="login" placeholder="login">
          <input type="text" id="password" class="fadeIn third" name="pass" placeholder="password">
          <input type="submit" class="fadeIn fourth" value="Log In">
        </form>
    
        <!-- Remind Passowrd -->
        <div id="formFooter">
          <a class="underlineHover" href="#">Forgot Password?</a>
        </div>
    
      </div>
    </div>
</BODY>
</HTML>