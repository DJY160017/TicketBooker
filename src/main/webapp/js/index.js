function enlarge(id){
    document.getElementById(id).style.webkitTransform = "scale(1.2)";
}

function recover(id){
    document.getElementById(id).style.webkitTransform = "scale(1)";
}

function chooseProgram(program){
    var url = '\\program\\show?program='+encodeURI(program)+'&pageNow=1';
    window.open(url,'_self');
}