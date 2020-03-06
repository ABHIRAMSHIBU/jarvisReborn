function validform() {

        var a = document.forms["my-form"]["fullname"].value;
        var b = document.forms["my-form"]["username"].value;
        var c = document.forms["my-form"]["email-address"].value;
        var d = document.forms["my-form"]["InputPassword1"].value;
        var e = document.forms["my-form"]["InputPassword2"].value;
        //var f = document.forms["my-form"]["image1"].value;
        if (a==null || a=="")
        {
            alert("Please Enter Your Full Name");
            return false;
        }else if (b==null || b=="")
        {
            alert("Please Enter Your Username");
            return false;
        }else if (c==null || c=="")
        {
            alert("Please Enter Your Email Address ");
            return false;
        }else if (d==null || d=="")
        {
            alert("Please Enter Your Password");
            return false;
        }
        else if(d.length < 8)
        {
            var s = d.toString();
            var len = s.length;
            console.log(""+len);
            alert("Password should be atleast 8 characters");
            return false;
        }
        else if (e==null || e!=d)
        {
            alert("Passwords Does not match");
            return false;
        }
    }
