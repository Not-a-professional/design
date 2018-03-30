$(function init() {
    $("#volumeTable").bootstrapTable({
        url:"/admin/getTicketVolume",
        method: 'get',
        toolbar: '#toolbar',
        pagination:true,
        cache: false,
        sortable: false,
        sidePagination: "server",
        queryParams: queryParams,
        queryParamsType:"",
        dataType:"json",
        columns: [{
            title: '操作',
            formatter: function (value, row, index) {
                return [
                    '<a role="button" type="button" class="btn btn-primary" onclick="Auth('+ row.id+')">通过</a>',
                    '<a role="button" type="button" class="btn btn-danger" onclick="unAuth('+ row.id+')">驳回</a>'
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


function queryParams(params) {
    var temp = {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        username: $("#username").val(),
        size: params.pageSize,
        page: params.pageNumber-1,
        sort:params.sortName==undefined ? '' : params.sortName+","+params.sortOrder
    };
    return temp;
}

//容量审核通过
function Auth(e) {
    $.ajax({
        url:"/admin/volumeAuth?id=" + e,
        type:"GET",
        dataType:"json",
        success: function () {
            alert("操作成功!");
            $("#shareTable").bootstrapTable('refresh',{
                silent:true
            });
        },
        error: function () {
            alert("操作失败！");
        }
    })
}

//申请容量不通过
function unAuth(e) {
    $.ajax({
        url:"/admin/unAuthVolume?id=" + e,
        type:"GET",
        dataType:"json",
        success: function () {
            alert("操作成功!");
            $("#shareTable").bootstrapTable('refresh',{
                silent:true
            });
        },
        error: function () {
            alert("操作失败！");
        }
    })
}