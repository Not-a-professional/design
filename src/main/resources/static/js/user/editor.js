var editorHtml = '';
var editor = new wangEditor('#editor');
$(function init() {
    editor.customConfig.menus = [
        'undo',  // 撤销
        'redo',  // 重复
        'image' //上传图片
    ];
    editor.customConfig.uploadImgShowBase64 = true; // 使用 base64 保存图片
    editor.customConfig.showLinkImg = false; // 隐藏“网络图片”tab
    editor.customConfig.uploadImgMaxSize = 1024 * 1024; // 将图片大小限制为 1M
    editor.customConfig.uploadImgMaxLength = 1; // 限制一次最多上传 1 张图片
    getEditorHtml();
    editor.create();
});

function getEditorHtml() {
    $.ajax({
        url:"/file/getEditorHtml?path=" + editorPath,
        dataType:"json",
        success: function (data) {
            editorHtml = data['res'];
            editor.txt.html(editorHtml);
        }
    })
}

function Save() {
    $.ajax({
        url:"/file/saveEditorHtml",
        type:"POST",
        data: {
            path:editorPath,
            content:editor.txt.html()
        },
        dataType:"json",
        success: function (data) {
            if (data['res'] == "success") {
                alert("保存成功")
            } else {
                alert("保存失败")
            }
        }
    })
}