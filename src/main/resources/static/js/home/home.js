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
                if (i == 0) {
                    optionNode.setAttribute("selected", "selected");
                }
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

// 下载并为下载的记录download属性加一
function OtherDownload(e) {
    window.location.href = "http://localhost:8080/share/download?path=" + e + "&other=other";
}

function OtherDownloadZip(e) {
    window.location.href = "http://localhost:8080/share/downloadZip?url=" + e + "&other=other";
}