$(function init() {
    $("#volumeTable").bootstrapTable({
        url:"/admin/getTicketVolume",
        method: 'get',
        toolbar: '#toolbar',
        pageSize : 10, //每页的记录行数(*)
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
                    '<a role="button" type="button" class="btn btn-primary radius" onclick="Auth('+ row.id+')">通过</a>',
                    '<a role="button" type="button" class="btn btn-danger radius" onclick="unAuth('+ row.id+')">驳回</a>'
                ].join('')
            }
        },{
            field: 'id',
            title: '申请ID'
        }, {
            field: 'user',
            title: '申请用户'
        },{
            field: 'reason',
            title: '申请理由'
        }]
    });
});


function SearchForVolume() {
    $("#volumeTable").bootstrapTable('refresh',{
        silent:true
    });
}
function queryParams(params) {
    var temp = {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        username: $("#volumeName").val(),
        size: params.pageSize,
        page: params.pageNumber-1,
        sort:params.sortName==undefined ? '' : params.sortName+","+params.sortOrder
    };
    return temp;
}

//容量审核通过
function Auth(e) {
    if (confirm("确定通过？")) {
        $('#loading').slideDown('400');
        $.ajax({
            url:"/admin/volumeAuth?id=" + e,
            type:"GET",
            dataType:"json",
            success: function () {
                $('#loading').slideUp('400');
                alert("操作成功!");
                $("#volumeTable").bootstrapTable('refresh',{
                    silent:true
                });
            },
            error: function () {
                $('#loading').slideUp('400');
                alert("操作失败！");
            }
        })
    }
}

//申请容量不通过
function unAuth(e) {
    if (confirm("确定不通过？")) {
        $('#loading').slideDown('400');
        $.ajax({
            url:"/admin/unAuthVolume?id=" + e,
            type:"GET",
            dataType:"json",
            success: function () {
                $('#loading').slideUp('400');
                alert("操作成功!");
                $("#volumeTable").bootstrapTable('refresh',{
                    silent:true
                });
            },
            error: function () {
                $('#loading').slideUp('400');
                alert("操作失败！");
            }
        })
    }
}