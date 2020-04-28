function mastercalc(){
    var mutex=0;
    var relayobjects=[0,0,0,0];
    relayobjects[0] = document.getElementById("1").childNodes;
    relayobjects[1] = document.getElementById("2").childNodes;
    relayobjects[2] = document.getElementById("3").childNodes;
    relayobjects[3] = document.getElementById("4").childNodes;
    var relays=Array([0,0,0,0]);
    for(var j=0;j<4;j++){
        for(var i=0;i<8;i++){
            index=(i*2)+3;
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
    console.log(relays);
}
mastercalc();
function calculate(z){
    var relayobjects=[0,0,0,0];
    relayobjects[0] = document.getElementById("1").childNodes;
    relayobjects[1] = document.getElementById("2").childNodes;
    relayobjects[2] = document.getElementById("3").childNodes;
    relayobjects[3] = document.getElementById("4").childNodes;
    var relays=Array([0,0,0,0]);
    var j =z-1;
    var count=0;
    for(var i=0;i<8;i++){
        index=(i*2)+3;
        if(count<2){
            relays[j]=relays[j] | relayobjects[j][index].childNodes[1].checked << i;
            if(relayobjects[j][index].childNodes[1].checked==true){
                count++;
            }
        }
        else{
            relayobjects[j][index].childNodes[1].checked=false;
            console.log([j,index,relayobjects[j][index].childNodes[1]]);
        }
    }
    if(count>2){
        alert("More than two selected");
    }
    var mutex = relays[j];
    for(var j=0;j<4;j++){
        if(j==z-1){
            relayobjects[j][19].innerHTML=mutex;
            continue;
        }
        for(var i=0;i<8;i++){
            index=(i*2)+3;
            if((mutex & relayobjects[j][index].childNodes[1].checked << i) == 1 << i ){
                relayobjects[j][index].childNodes[1].checked = false;
                alert("Conflict");
            }
            else{
                relays[j]=relays[j] | relayobjects[j][index].childNodes[1].checked << i;
            }
        }
        
        mutex=mutex|relays[j];
    }
    mastercalc(); 
}