
<?php
    if (!empty($_POST)){
        echo("Success!");
        var_dump($_POST);
        $name = $_POST["fullname"];
        $uname = $_POST["username"];
        $email = $_POST["email-address"];
        $password = $_POST["password"];
        $hashval=hash('sha256',$password);
        require_once "config.php";
        $conn = new mysqli($servername, $username, $password, $dbname);
        $sql = "insert into userinfo(fullname,name,email,password) values(\"$name\",\"$uname\",\"$email\",\"$hashval\");";
        $sql1 = "select name from userinfo where name = $name;";
        $y = $conn->query($sql1);
        $flag=false;
        $flag_uname_already_exists=false;
        if((mysqli_num_rows($y)==0))
        {
        echo "Meh!";
            $x = $conn->query($sql);
            if($x){
                $flag=false;
            }
            else{
                $flag=true;
                //Comment below if mysqli thingy works...
                $flag_uname_already_exists=true;
            }
            echo "Sucess";
        }
        else
        {
            $flag=true;
            $flag_uname_already_exists=true;
            var_dump($flag_uname_already_exists);
        }
        if($flag==true)
        {
            //echo "failed";
            ?>
             <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <!------ Include the above in your HEAD tag ---------->
    <!doctype html>
    <html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Fonts -->
        <link rel="dns-prefetch" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css?family=Raleway:300,400,600" rel="stylesheet" type="text/css">



        <link rel="icon" href="Favicon.png">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/register.css"/>
        <script src="script/register.js"></script> 
        <title>ssal registration</title>
    </head>
    <body>

    <nav class="navbar navbar-expand-lg navbar-light navbar-laravel">
        <div class="container">
        <a class="navbar-brand" href="#">SSAL</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="login.php">Login</a>
                </li>
                <!--<li class="nav-item">
                    <a class="nav-link" href="#">Register</a>
                </li> -->
            </ul>

        </div>
        </div>
    </nav>

    <main class="my-form">
        <div class="cotainer">
            <div class="row justify-content-center">
                <div class="col-md-8">
                        <div class="card">
                            <div class="card-header">Register</div>
                            <div class="card-body">
                                <?php if($flag_uname_already_exists==true){
                                    //echo "<center><br><h3><font color=red>Username already exists</font></h3><br></center>";
                                    //echo "<center><br><h3><font color=red>Some error occured, please check username.</font></h3><br></center>";
                                    echo "<div class=\"alert alert-danger\" role=\"alert\">Username already exist!</div>";
                                }
                                ?>
                                <form name="my-form" onsubmit="return validform()" action="register.php" method="POST">
                                    <div class="form-group row">
                                        <label for="full_name" class="col-md-4 col-form-label text-md-right">Full Name</label>
                                        <div class="col-md-6">
                                            <input type="text" id="full_name" class="form-control" name="fullname" value="<?php echo $name ?>">
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label for="user_name" class="col-md-4 col-form-label text-md-right">User Name</label>
                                        <div class="col-md-6">
                                            <input type="text" id="user_name" class="form-control" name="username"  value="<?php echo $uname ?>">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="email_address" class="col-md-4 col-form-label text-md-right">E-Mail Address</label>
                                        <div class="col-md-6">
                                            <input type="text" id="email_address" class="form-control" name="email-address" value="<?php echo $email ?>">
                                        </div>
                                    </div>


                                    <div class="form-group row">
                                        <label for="InputPassword1" class="col-md-4 col-form-label text-md-right">Password</label>
                                        <div class="col-md-6">
                                            <input type="password" class="form-control" id="InputPassword1" placeholder="Password" name="password" value="<?php echo $password ?>">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="InputPassword1" class="col-md-4 col-form-label text-md-right">Repeat Password</label>
                                        <div class="col-md-6">
                                            <input type="password" class="form-control" id="InputPassword2" placeholder="Password" value="<?php echo $password ?>">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="phone_number" class="col-md-4 col-form-label text-md-right">Profile Image</label>
                                        <div class="col-md-6">
                                            <input type="file" class="form-control-file" id="image1">
                                            <br>
                                            <input type="file" class="form-control-file" id="image2">
                                        </div>
                                    </div>

                                        <div class="col-md-6 offset-md-4">
                                            <button type="submit" class="btn btn-primary">
                                            Register
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                </div>
            </div>
        </div>
        </main>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    </body>
    </html>
            <?php
        }
        else{
            header("location: login.php");
        }
    }
    else{
        ?>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <!------ Include the above in your HEAD tag ---------->
    <!doctype html>
    <html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Fonts -->
        <link rel="dns-prefetch" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css?family=Raleway:300,400,600" rel="stylesheet" type="text/css">



        <link rel="icon" href="Favicon.png">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/register.css"/>
        <script src="script/register.js"></script> 
        <title>ssal registration</title>
    </head>
    <body>

    <nav class="navbar navbar-expand-lg navbar-light navbar-laravel">
        <div class="container">
        <a class="navbar-brand" href="#">SSAL</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="login.php">Login</a>
                </li>
                <!--<li class="nav-item">
                    <a class="nav-link" href="#">Register</a>
                </li> -->
            </ul>

        </div>
        </div>
    </nav>

    <main class="my-form">
        <div class="cotainer">
            <div class="row justify-content-center">
                <div class="col-md-8">
                        <div class="card">
                            <div class="card-header">Register</div>
                            <div class="card-body">
                                <form name="my-form" onsubmit="return validform()" action="register.php" method="POST">
                                    <div class="form-group row">
                                        <label for="full_name" class="col-md-4 col-form-label text-md-right">Full Name</label>
                                        <div class="col-md-6">
                                            <input type="text" id="full_name" class="form-control" name="fullname" >
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label for="user_name" class="col-md-4 col-form-label text-md-right">User Name</label>
                                        <div class="col-md-6">
                                            <input type="text" id="user_name" class="form-control" name="username">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="email_address" class="col-md-4 col-form-label text-md-right">E-Mail Address</label>
                                        <div class="col-md-6">
                                            <input type="text" id="email_address" class="form-control" name="email-address" >
                                        </div>
                                    </div>


                                    <div class="form-group row">
                                        <label for="InputPassword1" class="col-md-4 col-form-label text-md-right">Password</label>
                                        <div class="col-md-6">
                                            <input type="password" class="form-control" id="InputPassword1" placeholder="Password" name="password" >
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="InputPassword1" class="col-md-4 col-form-label text-md-right">Repeat Password</label>
                                        <div class="col-md-6">
                                            <input type="password" class="form-control" id="InputPassword2" placeholder="Password" >
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label for="phone_number" class="col-md-4 col-form-label text-md-right">Profile Image</label>
                                        <div class="col-md-6">
                                            <input type="file" class="form-control-file" id="image1">
                                            <br>
                                            <input type="file" class="form-control-file" id="image2">
                                        </div>
                                    </div>

                                        <div class="col-md-6 offset-md-4">
                                            <button type="submit" class="btn btn-primary">
                                            Register
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                </div>
            </div>
        </div>
        </main>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    </body>
    </html>
<?php
    }
    
?>

