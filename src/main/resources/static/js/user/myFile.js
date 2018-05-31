var rootPath = "/" + $('#name').val();
var path = rootPath;
$(function init() {
    getList(path);
    $("input[name='radio']").on('change',function () {
        var value = $("input[name='radio']:checked").val();
        if (value == 0) {
            document.getElementById("gen").innerHTML = '申请公开分享'
        } else {
            document.getElementById("gen").innerHTML = '申请私密分享'
        }
    })
});

function getList(e) {
    $.ajax({
        url: "/file/getList?path=" + e,
        dataType: "json",
        success: function (data) {
            fillList(data);
        }
    })
}

function fillList(data) {
    var rowNode = document.createElement("div");
    rowNode.setAttribute("class", "row");
    var colNode = document.createElement("div");
    colNode.setAttribute("class", "col-md-3");
    var thNode = document.createElement("div");
    thNode.setAttribute("class", "thumbnail");
    var imgNode = document.createElement("img");
    imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
    var captionNode = document.createElement("div");
    captionNode.setAttribute("class", "caption");
    var inputNode1 = document.createElement("input");
    inputNode1.setAttribute("type", "text");
    inputNode1.setAttribute("style", "width:100%");
    inputNode1.setAttribute("id", "create");
    captionNode.appendChild(inputNode1);
    var pNode2 = document.createElement("h5");
    var aNode = document.createElement("a");
    var text = document.createTextNode("创建文件夹");
    aNode.setAttribute("onclick", "create()");
    aNode.setAttribute("role", "button");
    aNode.appendChild(text);
    pNode2.appendChild(aNode);
    captionNode.appendChild(pNode2);
    thNode.appendChild(imgNode);
    thNode.appendChild(captionNode);
    colNode.appendChild(thNode);
    rowNode.appendChild(colNode);
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
        inputNode1.setAttribute("style", "width:100%");
        inputNode1.setAttribute("readonly", "readonly");
        var pNode2 = document.createElement("h5");
        var aNode1 = document.createElement("a");
        aNode1.setAttribute("role", "button");
        aNode1.setAttribute("onclick", "Download(\"" + data[i].substring(27) + "\")");
        var aText1 = document.createTextNode("下载 ");
        aNode1.appendChild(aText1);
        var aNode2 = document.createElement("a");
        aNode2.setAttribute("role", "button");
        aNode2.setAttribute("onclick", "Share(\"" + data[i].substring(27) + "\")");
        var aText2 = document.createTextNode("分享 ");
        aNode2.appendChild(aText2);
        var aNode3 = document.createElement("a");
        aNode3.setAttribute("role", "button");
        aNode3.setAttribute("onclick", "Delete(\"" + data[i].substring(27) + "\")");
        var aText3 = document.createTextNode("删除 ");
        aNode3.appendChild(aText3);
        var index = data[i].lastIndexOf(".");
        var suffix = data[i].substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i].substring(27));
            imgNode.setAttribute("onclick", "Check(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
            pNode2.appendChild(aNode2);
            pNode2.appendChild(aNode3);
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
            imgNode.setAttribute("onclick", "Check(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
            pNode2.appendChild(aNode2);
            pNode2.appendChild(aNode3);
        } else if (suffix == "txt") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Notes.png");
            imgNode.setAttribute("onclick", "Edit(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
            pNode2.appendChild(aNode2);
            pNode2.appendChild(aNode3);
        } else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
            imgNode.setAttribute("onclick", "Enter(\"" + data[i].substring(27) + "\")");
            aNode1.setAttribute("onclick", "DownloadZip(\"" + data[i].substring(27) + "\")");
            pNode2.appendChild(aNode1);
            pNode2.appendChild(aNode2);
            pNode2.appendChild(aNode3);
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

function Download(e) {
    window.location.href = "http://localhost:8080/file/download?path=" + e + "&other";
}

function DownloadZip(e) {
    window.location.href = "http://localhost:8080/file/downloadZip?url=" + e + "&other";
}

function Edit(e) {
    window.location.href = "http://localhost:8080/user/editor?path=" + e;
}

function Delete(e) {
    if (confirm("确定删除？")) {
        $.ajax({
            url:"/file/delete?path=" + e,
            dataType:"json",
            success: function (data) {
                if (data['res'] == "success") {
                    $("#body2").empty();
                    getList(path);
                } else {
                    alert("删除失败");
                }
            }
        });
    }
}

function Enter(e) {
    $("#body2").empty();
    path = e;
    getList(e);
}

function Return() {
    if (path == rootPath) {

    } else {
        $("#body2").empty();
        var index = path.lastIndexOf("/");
        path = path.substring(0, index);
        getList(path);
    }
}

function Share(e) {
    var index = e.lastIndexOf("/");
    var name = e.substring(index + 1);
    document.getElementById("shareName").innerHTML = "分享文件(夹): " + name;
    $("#sharePath").attr("value", e);
    $("#share").attr("class", "m-card bit-3 on-screen");
    $("#sPath").empty();
}

function generateShare() {
    $("#sPath").empty();
    var value = $("input[name='radio']:checked").val();
    var expireTime = document.getElementById("expireTime").value;
    var sharePath = $("#sharePath").val();
    if (value == 0) {
        $.ajax({
            url:"/file/sharePath?url=" + sharePath + "&expireTime=" + expireTime,
            type:"GET",
            dataType:"json",
            success: function (data) {
                if (data['sPath'] == 'fail') {
                    $("#sPath").append('<ul><li>' + '您的前一次申请还在审核中，请耐心等待！' +'</li></ul>')
                } else {
                    $("#sPath").append('<ul><li>' + '公开分享申请已提交，请耐心等待管理员审核！' + '</li></ul>')
                }
            },
            error: function (data) {
                alert("申请失败！" + data)
            }
        })
    } else {
        $.ajax({
            url:"/file/sSharePath?url=" + sharePath + "&expireTime=" + expireTime,
            type:"GET",
            dataType:"json",
            success: function (data) {
                if (data['res'] == 'fail') {
                    $("#sPath").append('<ul><li>' + '您的前一次申请还在审核中，请耐心等待！' +'</li></ul>')
                } else {
                    $("#sPath").append('<ul><li>' + '私密链接：' + data['sPath']
                        + '</li><li>' + '提取码：' +  data['secret'] + '</li><li>'
                        + '请耐心等待管理员审核！' +'</li></ul>')
                }
            },
            error: function (data) {
                alert("申请失败！" + data)
            }
        })
    }
}

function create() {
    var dir = path + "/" + $("#create").val();
    $.ajax({
        url:"/file/createDir?url=" + dir,
        type:"GET",
        dateType:"json",
        success:function (data) {
            if (data['res'] == 'success') {
                alert(data['error']);
                $("#body2").empty();
                getList(path);
                $("#path").empty();
                getFileInputList();
            } else {
                alert(data['error']);
            }
        },
        error:function (data) {
            alert(data);
        }
    })
}