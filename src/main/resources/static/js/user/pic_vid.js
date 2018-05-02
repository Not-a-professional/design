$(function init() {
    var index = pvPath.lastIndexOf(".");
    var suffix = pvPath.substring(index + 1);
    var node;
    if (suffix == 'jpg' || suffix == 'png') {
        node = document.createElement("img");
        node.setAttribute("src","");// TODO
    } else if (suffix == 'mp4') {
        node = document.createElement("video");
        node.setAttribute("src", ""); //TODO
    }
    document.getElementById("pv").appendChild(node);
});