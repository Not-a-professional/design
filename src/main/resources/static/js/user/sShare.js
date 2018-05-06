var path = '';
var rootPath = '';

function sCheck() {
    $.ajax({
        url: "/s/check",
        type: "POST",
        dataType: "json",
        data: {
            sPath: sPath,
            secret: $("#secret").val()
        },
        success: function (data) {
            if (data['res'] = "success") {
                $("#body2").empty();
                path = data['path'];
                rootPath = data['path'];
                getListForsPath()
            } else {
                alert("提取码错误！");
            }
        }
    })
}

function getListForsPath() {
    $.ajax({
        url: "/file/getList?path=" + path,
        dataType: "json",
        success: function (data) {
            fillList(data);
        }
    })
}

function fillList(data) {
    var rowNode = document.createElement("div");
    rowNode.setAttribute("class", "row");
    for (var i = 0;i < data.length; ++i) {
        var colNode = document.createElement("div");
        colNode.setAttribute("class", "col-md-3");
        var thNode = document.createElement("div");
        thNode.setAttribute("class", "thumbnail");
        var imgNode = document.createElement("img");
        imgNode.setAttribute("style", "height:128px;width:128px");
        var captionNode = document.createElement("div");
        captionNode.setAttribute("class", "caption");
        var inputNode1 = document.createElement("input");
        var temp = data[i].split("/");
        var name = temp[temp.length - 1];
        inputNode1.setAttribute("type", "text");
        inputNode1.setAttribute("value", name);
        inputNode1.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode1.setAttribute("readonly", "readonly");
        var pNode2 = document.createElement("h5");
        var aNode1 = document.createElement("a");
        aNode1.setAttribute("role", "button");
        aNode1.setAttribute("onclick", "Download(\"" + data[i].substring(27) + "\")");
        var aText1 = document.createTextNode("下载 ");
        aNode1.appendChild(aText1);
        var index = data[i].lastIndexOf(".");
        var suffix = data[i].substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i].substring(27));
            imgNode.setAttribute("onclick", "Check(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
            imgNode.setAttribute("onclick", "Check(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
        } else if (suffix == "txt") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Notes.png");
            imgNode.setAttribute("onclick", "Check(\"" + data[i] + "\")");
            pNode2.appendChild(aNode1);
        } else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
            imgNode.setAttribute("onclick", "Enter(\"" + data[i].substring(27) + "\")");
            aNode1.setAttribute("onclick", "DownloadZip(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
        }
        captionNode.appendChild(inputNode1);
        captionNode.appendChild(pNode2);
        thNode.appendChild(imgNode);
        thNode.appendChild(captionNode);
        colNode.appendChild(thNode);
        rowNode.appendChild(colNode);
    }
    $("#body2")[0].appendChild(rowNode);
    $("#body1").attr("style", "display: block");
}

function Download(e) {
    window.location.href = "http://localhost:8080/file/download?path=" + e + "&other=other";
}

function DownloadZip(e) {
    window.location.href = "http://localhost:8080/file/downloadZip?url=" + e + "&other=other";
}

function Enter(e) {
    $("#body2").empty();
    path = e;
    getListForsPath(e);
}

function Return() {
    if (path != rootPath) {
        $("#body2").empty();
        var index = path.lastIndexOf("/");
        path = path.substring(0, index);
        getListForsPath(path);
    }
}