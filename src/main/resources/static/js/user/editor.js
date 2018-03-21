var editorHtml;
var E = window.wangEditor;
var editor = new E('#editor');
$(function init() {
    editor.customConfig.menus = [
        'undo',  // 撤销
        'redo'  // 重复
    ];
    getEditorHtml();
    editor.txt.text(editorHtml);
    editor.create()
});

function getEditorHtml() {
    $.ajax({
        url:"/file/getEditorHtml?path=" + editorPath,
        dataType:"json",
        success: function (data) {
            editorHtml = data['res'];
        }
    })
}

function Save() {
    $.ajax({
        url:"/file/saveEditorHtml",
        type:"POST",
        data: {
            path:editorPath,
            content:editor.txt.text()
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