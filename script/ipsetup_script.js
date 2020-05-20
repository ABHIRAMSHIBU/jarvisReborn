// function delete_item(id){
//     console.log(id);
// }
function delete_item(id){
    f=document.createElement("form");
    f.action="ipsetup.php";
    f.method="post";
    var hidden=document.createElement("input");
    hidden.type="hidden";
    hidden.name="id";
    hidden.value=id;
    // console.log([relays[i],countOnes(relays[i])]);
    f.appendChild(hidden);
    document.body.appendChild(f);
    //console.log("Form will submit");
    f.submit();
    return 1;
}
function add_item(element){
    f=document.createElement("form")
    arr=element.parentNode.parentNode.childNodes;
    f.method="POST";
    for(var i=0;i<arr.length-1;i++){
        node=document.createElement("input")
        node.type="hidden"
        //console.log(arr[i]);
        txt = arr[i].childNodes[0].childNodes[0]
        console.log(txt);
        node.name=txt.name;
        node.value=txt.value;
        f.appendChild(node);
    }
    document.body.appendChild(f);
    f.submit()
}
function add_config(){
    var table_html = document.getElementById("table_config");
    $(table_html).find('tbody').append("<tr>"+
                                        "<td><center><input type=\"text\" name=\"room_id\"></input></center></td>"+
                                        "<td><center><input type=\"text\" name=\"ip_addr\"></input></center></td>"+
                                        "<td><center><input type=\"text\" name=\"pins\"></input></center></td>"+
                                        "<td><center><select name='dos_hardware'><option value='1'>Yes</option><option value='0' selected>No</option></select></center></td>"+
                                        "<td><input class=\"btn btn-info btn-lg\"  type=\"button\" value=\"Apply\" onclick=add_item(this);></input></td>"+
                                        "</tr>");
}