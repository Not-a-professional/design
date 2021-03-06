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
            field: 'path',
            title: '文件(夹)',
            formatter: function (value, row, index) {
                var index = row.path.lastIndexOf(".");
                var suffix = row.path.substring(index + 1);
                switch (suffix) {
                    case "jpg" :
                        return '<img src="http://localhost:8080/file/getView' + row.path + '"width="100"/>';
                        break;
                    case "png" :
                        return '<img src="http://localhost:8080/file/getView' + row.path + '"width="100"/>';
                        break;
                    case "mp4" :
                        return '<video src="http://localhost:8080/file/download?other&path=' + row.path
                            + '" controls="controls" width="50%"></video>';
                        break;
                    default :
                        return row.path;
                        break;
                }
            }
        }],
        detailView : true, //是否显示父子表
        onExpandRow : function (index, row, $detail) { //循环初始化子表
            subTable(index, row, $detail);
        }
    });
});

function Search() {
    $("#shareTable").bootstrapTable('refresh',{
        silent:true
    });
}

function queryParams(params) {
    var temp = {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        username: $("#ticketName").val(),
        size: params.pageSize,
        page: params.pageNumber-1,
        sort:params.sortName==undefined ? '' : params.sortName+","+params.sortOrder
    };
    return temp;
}

//分享审核通过
function Auth(e) {
    if (confirm("确定通过？")) {
        $('#loading').slideDown('400');
        $.ajax({
            url:"/admin/shareAuth?id=" + e,
            type:"GET",
            dataType:"json",
            success: function () {
                $('#loading').slideUp('400');
                alert("操作成功!");
                $("#shareTable").bootstrapTable('refresh',{
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

//分享不通过
function unAuth(e) {
    if (confirm("确定不通过？")) {
        $('#loading').slideDown('400');
        $.ajax({
            url:"/admin/shareUnauth?id=" + e,
            type:"GET",
            dataType:"json",
            success: function (data) {
                $('#loading').slideUp('400');
                alert("操作成功!");
                $("#shareTable").bootstrapTable('refresh',{
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

//递归生成子表
function subTable(index, row, $detail) {
    var parentPath = row.path;
    var cur_table = $detail.html('<table></table>').find('table');
    $(cur_table).bootstrapTable({
        url: "/admin/getList?path=" + parentPath,
        method: "get",
        detailView: true,//父子表
        pageSize: 10,
        sidePagination: "server",
        queryParamsType:"",
        dataType:"json",
        columns:[{
            field: 'path',
            title: '文件(夹)',
            formatter: function (value, row, index) {
                var index = row.path.lastIndexOf(".");
                var suffix = row.path.substring(index + 1);
                switch (suffix) {
                    case "jpg" :
                        return '<img src="http://localhost:8080/file/getView' + row.path.substring(27) + '"width="100"/>';
                        break;
                    case "png" :
                        return '<img src="http://localhost:8080/file/getView' + row.path.substring(27) + '"width="100"/>';
                        break;
                    case "mp4" :
                        return '<video src="http://localhost:8080/file/download?other&path=' + row.path.substring(27)
                            + '" controls="controls" width="50%"></video>';
                        break;
                    default :
                        return row.path.substring(27);
                        break;
                }
            }
        }],
        onExpandRow: function (index, row, $Subdetail) {
            subTable(index, row, $Subdetail);
        }
    });
}