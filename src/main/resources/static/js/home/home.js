$(function init() {
    if ($('#auth').val() == "[ROLE_ADMIN]") {
        document.getElementById("username").innerHTML = "欢迎,管理员"
    } else {
        document.getElementById("username").innerHTML = "欢迎," + $('#name').val();
    }

    var con = $('[class*="material-container"]');
    var idi=0;
    var panel = con.find('[class^="panel bit-1"]');
    panel.each(function() {
        idi++;
        var $this = $(this);
        $this.attr('id', 'panel'+ idi);

        var shrunk = $('[class*="material-sidebar-left"]').width();
        // $this.addClass('shrunked').attr('data-shrunk', shrunk);
        $this.css({
            'width': 'calc(100% - '+ shrunk+'px)',
            'float': 'right'
        })
    });

    getFriendList();

    getFileInputList();

    $("#uploadFile").fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/file/upload", //上传的地址
        allowedFileExtensions: ['jpg','png','mp4','txt'],//接收的文件后缀
        uploadAsync: true, //默认异步上传
        showUpload: false, //是否显示上传按钮

        showRemove : false, //显示移除按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        dropZoneEnabled: true,//是否显示拖拽区域
        maxFileSize:0,//0表示不限制文件大小
        maxFileCount: 1, //表示允许同时上传的最大文件个数
        enctype: 'multipart/form-data',
        validateInitialCount:true,
        uploadExtraData: function () {
            var path = document.getElementById("path").value;
            return {"path": path};
        }
    }).on("fileuploaded", function (event, data, previewId, index) {
        if (data.response.res == "fail") {
            if(data.response.msg != null) {
                alert(data.response.msg );
            }
        }
    });
});

function getcong() {
    clickcong();
    if ($('#cong').attr("data-state") == "open") {
        $('#cong').attr("data-state", "closed")
    } else {
        $('#cong').attr("data-state", "open")
    }
}

function getusr() {
    clickusr();
    if ($('#usr').attr("data-state") == "open") {
        $('#usr').attr("data-state", "closed")
    } else {
        $('#usr').attr("data-state", "open")
    }
}

function clickcong() {
    var con = $('[class*="material-container"]');
    var idi=0;
    var panel = con.find('[class^="panel bit-1"]');
    function Panel() {
        panel.each(function(){
            idi++;
            var $this = $(this);
            $this.attr('id', 'panel'+ idi)

// When something is clicked, for example, the menu sidebar
                if($('#cong').attr('data-state') == 'closed'){
                    var shrunk = $('[class*="material-sidebar-right"]').width();
                    if ($('#usr').attr('data-state') == 'closed') {
                        $this.css({
                            'width': 'calc(100% - '+ shrunk+'px)',
                            'float': 'right'
                        })
                    } else if ($('#usr').attr('data-state') == 'open') {
                        $this.css({
                            'width': 'calc(100% - ' + shrunk +'px)',
                            'float': 'right'
                        })
                    }
                } else if($('#cong').attr('data-state') == 'open') {
                    if ($('#usr').attr('data-state') == 'closed') {
                        $this.css({
                            'width': '100%',
                            'float': 'right'
                        })
                    } else if ($('#usr').attr('data-state') == 'open') {
                        $this.css({
                            'width': '100%',
                            'float': 'right'
                        })
                    }
                }

        });
    }
    Panel();
}

function clickusr() {
    var con = $('[class*="material-container"]');
    var idi=0;
    var panel = con.find('[class^="panel bit-1"]');
    function Panel() {
        panel.each(function(){
            idi++;
            var $this = $(this);
            $this.attr('id', 'panel'+ idi)
                if($('#usr').attr('data-state') == 'open'){
                    var shrunk = $('[class*="material-sidebar-right"]').width();
                    if ($('#cong').attr('data-state') == 'closed') {
                        $this.css({
                            'width': '100%',
                            'float': 'right'
                        })
                    } else if ($('#cong').attr('data-state') == 'open') {
                        $this.css({
                            'width': 'calc(100% - '+ shrunk+'px)',
                            'float': 'right'
                        })
                    }
                } else if ($('#usr').attr('data-state') == 'closed') {
                    var shrunk = $('[class*="material-sidebar-right"]').width();
                    if ($('#cong').attr('data-state') == 'closed') {
                        $this.css({
                            'width': '100%',
                            'float': 'right'
                        })
                    } else if ($('#cong').attr('data-state') == 'open') {
                        $this.css({
                            'width': 'calc(100% - '+ shrunk +'px)',
                            'float': 'right'
                        })
                    }
                }
        });
    }
    Panel();
}

function getFileInputList() {
    $.ajax({
        url: "/file/getDir",
        dataType: "json",
        success: function (data) {
            for (var i = 0; i< data.length; ++i) {
                var optionNode = document.createElement("option");
                optionNode.setAttribute("value", data[i].substring(27));
                var text = document.createTextNode(data[i].substring(27));
                optionNode.appendChild(text);
                document.getElementById("path").appendChild(optionNode);
            }
        }
    });
}

function getFriendList() {
    $.ajax({
        url:"/user/getFriend",
        type:"GET",
        dataType:"json",
        success:function (data) {
            var usrlist = data;
            for (var i = 0;i < usrlist.length;++i) {
                var liNode = document.createElement("li");
                var anode = document.createElement("a");
                var spanNode = document.createElement("span");
                var text = document.createTextNode(usrlist[i]);
                anode.setAttribute('class','material-btn-flat-white');
                anode.setAttribute('onclick', 'otherShare(\"' + usrlist[i] + "\")");
                spanNode.setAttribute('class','glyphicon glyphicon-option-horizontal');
                anode.appendChild(spanNode);
                anode.appendChild(text);
                liNode.appendChild(anode);
                document.getElementById("userlist").appendChild(liNode);
            }
        },
        error:function (data) {
            alert("获取好友列表失败！" + data);
        }
    });
}

function otherShare(e) {
    window.location.href = "/share/otherShare?name=" + e;
}

function getHotShare() {
    $.ajax({
        url: "/user/getHotShare",
        type: "GET",
        dataType: "json",
        success: function (data) {

        },
        error: function (data) {
            alert("获取朋友圈热门失败！" + data);
        }
    })
}

function fillHotShareList(data) {
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
        var temp = data[i].url.split("/");
        var name = temp[temp.length - 1];
        inputNode1.setAttribute("type", "text");
        inputNode1.setAttribute("value", name);
        inputNode1.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode1.setAttribute("readonly", "readonly");
        var inputNode2 = document.createElement("input");
        inputNode2.setAttribute("type", "text");
        inputNode2.setAttribute("value", "来源：" + data[i].userId);
        inputNode2.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode2.setAttribute("readonly", "readonly");
        var inputNode3 = document.createElement("input");
        inputNode3.setAttribute("type", "text");
        inputNode3.setAttribute("value", "热度：" + data[i].hot);
        inputNode3.setAttribute("style", "border:none;outline:none;width:100%");
        inputNode3.setAttribute("readonly", "readonly");
        var pNode2 = document.createElement("h5");
        var aNode1 = document.createElement("a");
        aNode1.setAttribute("role", "button");
        aNode1.setAttribute("onclick", "OtherDownload(\"" + data[i].url + "\")");
        var aText1 = document.createTextNode("下载 ");
        aNode1.appendChild(aText1);
        pNode2.appendChild(aNode1);
        var index = data[i].url.lastIndexOf(".");
        var suffix = data[i].url.substring(index + 1);
        if (suffix == "jpg" || suffix == "png") {
            imgNode.setAttribute("src", "http://localhost:8080/file/getView" + data[i].url);
        } else if (suffix == "mp4") {
            imgNode.setAttribute("src", "http://localhost:8080/img/Youtube.png");
        } else {
            imgNode.setAttribute("src", "http://localhost:8080/img/folder-videos.png");
            aNode1.setAttribute("onclick", "OtherDownloadZip(\"" + data[i].url + "\")");
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

// 下载并为下载的记录download属性加一
function OtherDownload(e) {
    window.location.href = "http://localhost:8080/share/download?path=" + e + "&other=other";
}

function OtherDownloadZip(e) {
    window.location.href = "http://localhost:8080/share/downloadZip?url=" + e + "&other=other";
}