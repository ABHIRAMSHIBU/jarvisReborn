var relays=Array([0,0,0,0]);
function mastercalc(){
    var mutex=0;
    var relayobjects=[0,0,0,0];
    relayobjects[0] = document.getElementById("1").childNodes;
    relayobjects[1] = document.getElementById("2").childNodes;
    relayobjects[2] = document.getElementById("3").childNodes;
    relayobjects[3] = document.getElementById("4").childNodes;
    relays=[0,0,0,0];
    for(var j=0;j<4;j++){
        for(var i=0;i<8;i++){
            index=((7-i)*2)+3;
            if((mutex & relayobjects[j][index].childNodes[1].checked << i) == 1 << i ){
                relayobjects[j][index].childNodes[1].checked = false;
                alert("Conflict");
            }
            else{
                relays[j]=relays[j] | relayobjects[j][index].childNodes[1].checked << i;
            }
        }
        relays[j]=relayobjects[j][19].innerHTML=relays[j];
        mutex=mutex|relays[j];
    }
    // console.log(relays);
}
mastercalc();
function calculate(z){
    var relayobjects=[0,0,0,0];
    relayobjects[0] = document.getElementById("1").childNodes;
    relayobjects[1] = document.getElementById("2").childNodes;
    relayobjects[2] = document.getElementById("3").childNodes;
    relayobjects[3] = document.getElementById("4").childNodes;
    relays=[0,0,0,0];
    var j =z-1;
    var count=0;
    var flag=0;
    for(var i=0;i<8;i++){
        index=((7-i)*2)+3;
        if(count<2){
            relays[j]=relays[j] | relayobjects[j][index].childNodes[1].checked << i;
            if(relayobjects[j][index].childNodes[1].checked==true){
                count++;
                // console.log(["count",count])
            }
        }
        else{
            flag= flag| relayobjects[j][index].childNodes[1].checked;
            relayobjects[j][index].childNodes[1].checked=false;
            // console.log([j,index,relayobjects[j][index].childNodes[1]]);
        }
    }
    if(flag==1){
        // alert("More than two selected");
    }
    var mutex = relays[j];
    for(var j=0;j<4;j++){
        if(j==z-1){
            relayobjects[j][19].innerHTML=mutex;
            continue;
        }
        for(var i=0;i<8;i++){
            index=((7-i)*2)+3;
            if((mutex & relayobjects[j][index].childNodes[1].checked << i) == 1 << i ){
                relayobjects[j][index].childNodes[1].checked = false;
                // alert("Conflict");
            }
            else{
                relays[j]=relays[j] | relayobjects[j][index].childNodes[1].checked << i;
            }
        }
        
        mutex=mutex|relays[j];
    }
    mastercalc(); 
}
function countOnes(b){
    var val=1;
    var count=0;
    for(i=0;i<8;i++){
        if((val & b) == val){
            count++;
        }
        val=val<<1;
        if(count==2){
            break;
        }
    }
    // console.log(["Counted value=",count,b]);
    if(count==2){
        return 1;
    }
    else if(count==0){
        return 1;
    }
    else{
        return 0;
    }
}
function submitvalues(){
    f=document.createElement("form");
    f.action="relayHandler.php";
    f.method="get";
    hidden=[0,0,0,0];
    for(var i=0;i<4;i++){
        hidden[i]=document.createElement("input");
        hidden[i].type="hidden";
        hidden[i].name=i+1;
        hidden[i].value=relays[i];
        // console.log([relays[i],countOnes(relays[i])]);
        if(countOnes(relays[i])==0){
            alert("One more more relay pairs dont have a pair!");
            return 0;
        }
        f.appendChild(hidden[i]);
    }
    document.body.appendChild(f);
    //console.log("Form will submit");
    f.submit();
    return 1;
}