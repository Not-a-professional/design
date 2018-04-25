$(function init() {
    getList();
    getsList();
});

function getList() {
    $.ajax({
        url: "/file/getShareList",
        dataType: "json",
        success: function (data) {
            fillList(data);
        }
    })
}

function getsList() {
    $.ajax({
        url: "/file/getsShareList",
        dataType: "json",
        success: function (data) {
            fillsList(data);
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
        aNode1.setAttribute("onclick", "cancelShare(\"" + data[i] + "\")");
        var aText1 = document.createTextNode("取消分享 ");
        aNode1.appendChild(aText1);
        pNode2.appendChild(aNode1);
        var index = data[i].lastIndexOf(".");
        var suffix = data[i].substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i]);
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
        } else if (suffix == "txt") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Notes.png");
        } else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
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

function fillsList(data) {
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
        var temp = data[i].path.split("/");
        var name = temp[temp.length - 1];
        inputNode1.setAttribute("type", "text");
        inputNode1.setAttribute("value", name);
        inputNode1.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode1.setAttribute("readonly", "readonly");
        var inputNode2 = document.createElement("input");
        inputNode2.setAttribute("type", "text");
        inputNode2.setAttribute("value", "提取路径：http://localhost:8080/s/" + data[i].spath);
        inputNode2.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode2.setAttribute("readonly", "readonly");
        var inputNode3 = document.createElement("input");
        inputNode3.setAttribute("type", "text");
        inputNode3.setAttribute("value", "提取码：" + data[i].secret);
        inputNode3.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode3.setAttribute("readonly", "readonly");
        var pNode2 = document.createElement("h5");
        var aNode1 = document.createElement("a");
        aNode1.setAttribute("role", "button");
        aNode1.setAttribute("onclick", "cancelShare(\"" + data[i].path + "\")");
        var aText1 = document.createTextNode("取消分享 ");
        aNode1.appendChild(aText1);
        pNode2.appendChild(aNode1);
        var index = data[i].path.lastIndexOf(".");
        var suffix = data[i].path.substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i].path);
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
        } else if (suffix == "txt") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Notes.png");
        } else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
        }
        captionNode.appendChild(inputNode1);
        captionNode.appendChild(inputNode2);
        captionNode.appendChild(inputNode3);
        captionNode.appendChild(pNode2);
        thNode.appendChild(imgNode);
        thNode.appendChild(captionNode);
        colNode.appendChild(thNode);
        rowNode.appendChild(colNode);
    }
    $("#body2")[0].appendChild(rowNode);
}

function cancelShare(e) {
    $.ajax({
        url: "/file/cancelShare?path=" + e,
        dataType: "json",
        success: function (data) {
            alert("取消成功");
            $("#body2").empty();
            getList();
            getsList();
        },
        error: function () {
            alert("取消失败");
        }
    })
}