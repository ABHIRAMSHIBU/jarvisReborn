function goToConfig(buttonElement){
   var devId = buttonElement.parentNode.parentNode.childNodes[1].id;
   var f = document.createElement("form");
   f.action="setup.php";
   f.method="get";
   input = document.createElement("input");
   input.type="hidden";
   input.name = "devId";
   input.value=devId;
   f.appendChild(input);
   document.body.appendChild(f);
   f.submit();
}
