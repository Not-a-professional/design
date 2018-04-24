$(function init() {
    $("#trashTable").bootstrapTable({
        url:"/user/getTrashList",
        method: 'get',
        toolbar: '#toolbar',
        pageSize : 10,
        pagination:true,
        cache: false,
        sortable: true,
        sidePagination: "server",
        queryParams: queryParams,
        queryParamsType:"",
        dataType:"json",
        columns: [{
            title: '操作',
            formatter: function (value, row, index) {
                return [
                    '<a role="button" type="button" class="btn btn-primary radius" onclick="rollBack('+ row.id+')">恢复</a>',
                ].join('')
            }
        },{
            field: 'id',
            title: 'ID'
        }, {
            field: 'path',
            title: '文件(夹)'
        },{
            field: 'date',
            title: '删除日期'
        }]
    });
});

function queryParams(params) {
    var temp = {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.pageSize,
        page: params.pageNumber-1,
        sort:params.sortName==undefined ? '' : params.sortName+","+params.sortOrder
    };
    return temp;
}

function rollBack(e) {
    if ("确定恢复？") {
        $.ajax({
            url:"/file/rollBack?id=" + e,
            type:"GET",
            dataType:"json",
            success: function () {
                alert("操作成功!");
                $("#trashTable").bootstrapTable('refresh',{
                    silent:true
                });
            },
            error: function () {
                alert("操作失败！");
            }
        })
    }
}