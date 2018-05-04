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
        var html;
        $.ajax({
            url:"/file/getEditorHtml?path=" + pvPath,
            dataType:"json",
            success: function (data) {
                html = data['res'];
            }
        });
        temp.append(html);
        //创建一个新的canvas
        var canvas2 = document.createElement("canvas");
        var _canvas = document.getElementById("pv");
        var w = parseInt(window.getComputedStyle(_canvas).width);
        var h = parseInt(window.getComputedStyle(_canvas).height);
        //将canvas画布放大若干倍，然后盛放在较小的容器内，就显得不模糊了
        canvas2.width = w * 2;
        canvas2.height = h * 2;
        canvas2.style.width = w + "px";
        canvas2.style.height = h + "px";
        //可以按照自己的需求，对context的参数修改,translate指的是偏移量
        var context = canvas2.getContext("2d");
        context.scale(2,2);
        html2canvas(temp.get(0),{canvas:canvas2}).then(function (canvas) {
                var pngUrl = canvas.toDataURL("image/png");
                node = document.createElement("img");
                node.setAttribute("scr", pngUrl);
        })
    }
    document.getElementById("pv").appendChild(node);
});