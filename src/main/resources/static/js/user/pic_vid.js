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
        var temp = $("#hiddenForTxt");
        var h;
        $.ajax({
            url:"/file/getEditorHtml?path=" + pvPath,
            dataType:"json",
            success: function (data) {
                h = data['res'];
            }
        });
        temp.append(h);
        html2canvas([temp.get(0)], {
            onrendered: function (canvas) {
                var pngUrl = canvas.toDataURL("image/png");
                node = document.createElement("img");
                node.setAttribute("scr", pngUrl);
            }
        })
    }
    document.getElementById("pv").appendChild(node);
});