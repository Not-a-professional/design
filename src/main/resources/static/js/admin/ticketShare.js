$(function init() {
    $("#shareTable").bootstrapTable({
        url:"/admin/getTicketShare",
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
            field: 'path',
            title: '文件(夹)',
            formatter: function (value, row, index) {
                var index = row.path.lastIndexOf(".");
                var suffix = row.path.substring(index + 1);
                switch (suffix) {
                    case "jpg" :
                        return '<img src="http://localhost:8080/file/getView"' + row.path.substring(27) + 'width="100"/>';
                        break;
                    case "png" :
                        return '<img src="http://localhost:8080/file/getView"' + row.path.substring(27) + 'width="100"/>';
                        break;
                    case "video" :
                        return '<video src="http://localhost:8080/file/download?other&path=' + row.path.substring(27)
                            + '" poster="http://localhost:8080/img/Youtube.png" width="100"></video>';
                        break;
                    default :
                        return row.path;
                        break;
                }
            }
        }],
        detailView : true, //是否显示父子表
        onExpandRow : function (index, row, $detail) {

        }
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

//分享不通过
function unAuth(e) {
    $.ajax({
        url:"/admin/shareUnauth?id=" + e,
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