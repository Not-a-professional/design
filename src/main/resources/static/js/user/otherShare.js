$(function () {
    getOtherShare()
});

function getOtherShare() {
    $.ajax({
        url:"/share/getOtherShare?name=" + name,
        type:"GET",
        dataType:"json",
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
        aNode1.setAttribute("onclick", "OtherDownload(\"" + data[i] + "\")");
        var aText1 = document.createTextNode("下载 ");
        aNode1.appendChild(aText1);
        pNode2.appendChild(aNode1);
        var index = data[i].lastIndexOf(".");
        var suffix = data[i].substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i]);
            imgNode.setAttribute("onclick", "Check(\"" + data[i] + "\")");
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
            imgNode.setAttribute("onclick", "Check(\"" + data[i] + "\")");
        } else if (suffix == "txt") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Notes.png");
        }else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
            aNode1.setAttribute("onclick", "OtherDownloadZip(\"" + data[i] + "\")");
        }

        captionNode.appendChild(inputNode1);
        captionNode.appendChild(pNode2);
        thNode.appendChild(imgNode);
        thNode.appendChild(captionNode);
        colNode.appendChild(thNode);
        rowNode.appendChild(colNode);
    }
    $("#body2")[0].appendChild(rowNode);
}

// 下载并为下载的记录download属性加一
function OtherDownload(e) {
    window.location.href = "http://localhost:8080/share/download?path=" + e + "&other=other";
}

function OtherDownloadZip(e) {
    window.location.href = "http://localhost:8080/share/downloadZip?url=" + e + "&other=other";
}