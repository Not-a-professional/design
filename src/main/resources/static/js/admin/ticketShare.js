$(function init() {
    $("#shareTable").bootstrapTable({
        url:"/admin/getTicketShare",
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
                    '<a role="button" role="button" class="btn btn-primary" onclick="Auth('+ row.id+')">通过</a>',
                    '<a role="button" role="button" class="btn btn-danger" onclick="unAuth('+ row.id+')">驳回</a>'
                ].join('')
            }
        },{
            field: 'id',
            title: '申请ID'
        }, {
            field: 'user',
            title: '申请用户'
        },{
            field: 'path',
            title: '文件(夹)路径'
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

//分享审核通过
function Auth(e) {
    $.ajax({
        url:"/admin/shareAuth?id=" + e,
        type:"GET",
        dataType:"json",
        success: function () {
            $("#shareTable").bootstrapTable('refresh',{
                silent:true
            });
        }
    })
}

//分享不通过
function unAuth(e) {
    $.ajax({
        url:"/admin/shareUnauth?id=" + e,
        type:"GET",
        dataType:"json",
        success: function () {
            $("#shareTable").bootstrapTable('refresh',{
                silent:true
            });
        }
    })
}