$(function init() {
    var E = window.wangEditor;
    var editor = new E('#editor');
    // 或者 var editor = new E( document.getElementById('editor') )
    editor.customConfig.menus = [
        'undo',  //撤销
        'redo'   //重复
    ];
    editor.create();
    //TODO 初始云笔记内容
    editor.txt.html('<p>用 JS 设置的内容</p>');
});

function sc() {
    //TODO 获取云笔记内容
    editor.txt.text();
}