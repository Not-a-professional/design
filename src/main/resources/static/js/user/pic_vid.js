$(function init() {
    var index = pvPath.lastIndexOf(".");
    var suffix = pvPath.substring(index + 1);
    var node;
    if (suffix == 'jpg' || suffix == 'png') {
        node = document.createElement("img");
        node.setAttribute("src","http://localhost:8080/file/getView" + pvPath);
    } else if (suffix == 'mp4') {
        node = document.createElement("video");
        node.setAttribute("src", "http://localhost:8080/file/download?other&path=" + pvPath);
        node.setAttribute("controls", "controls");
    } else if (suffix == 'txt') {
        node = document.createElement("img");
        node.setAttribute("src", "http://localhost:8080/user/htmlToImage?path=" + pvPath);
    }
    document.getElementById("pv").appendChild(node);
});