        var popE=false;
        function loadDoc() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
            document.getElementById("demo").innerHTML = this.responseText;
            }
        };
        xhttp.open("GET", "demo_get.asp", true);
        xhttp.send();
        }
        function popUP(){
           if(popE==false){
                popE=true;
                var pop=document.getElementById("popup");
                pop.style.display="block";
                var inpop=document.getElementById("inpopup")
                inpop.innerHTML="Add ROOM";
                inpop.innerHTML+="<form method=GET action='add.php'><br><br><label>Enter room name:<input type='text' style ='color:black;' name='room_name'></input></label><br><br><label>Enter room number:<input type='text' style ='color:black;' name='room_number'></input></label><br><br><center><input type='Submit' class='button' value='Add'></input></center></form>";
            }
            else{
                var pop=document.getElementById("popup");
                pop.style.display="none";
                var inpop=document.getElementById("inpopup")
                inpop.innerHTML="";
                popE=false;
            }
        }
        function submit(i) {
            var form=document.getElementById("hiddenForm");
            var hiddenEle=document.getElementsByName("devID")[0];
            hiddenEle.value=i;
            form.submit();
        }