function enlarge(id){
    document.getElementById(id).style.webkitTransform = "scale(1.2)";
}

function recover(id){
    document.getElementById(id).style.webkitTransform = "scale(1)";
}

function apply() {
    window.open('/venue/application', '_self');
}

function add(){
    window.open('/venue/addPlan', '_self');
}

 function plan() {
     window.open('/venue/plan', '_self');
 }