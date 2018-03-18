$(function() {
    $("#reg").validate({
        rules:{
            regname:{
                required:true,
                remote:{
                    url:"/checkRegName",
                    type:"POST"
                }
            },
            regpassword1:{
                required:true
            },
            regpassword2:{
                required:true,
                equalTo:"#regpassword1"
            }
        },
        messages:{
            regname:{
                required:"用户名不能为空！",
                remote:"用户名已存在！"
            },
            regpassword1:{
                required:"密码不能为空！"
            },
            regpassword2:{
                required:"密码不能为空！",
                equalTo:"密码不一致！"
            }
        },
        submitHandler:function () {
            var hobby = "";
            $("input:checkbox[name='hobby']:checked").each(function() {
                hobby += $(this).val() + ",";
            });
            $.ajax({
                url:"/register",
                type:"POST",
                data:{
                    name:$("#regname").val(),
                    password:$("#regpassword1").val(),
                    hobby:hobby
                },
                dataType:"json",
                success:function (data) {
                    alert("注册成功！");
                    $('.pop_box').slideUp('400');
                },
                error:function (data) {

                }
            })
        }
    })
});

$('.text-muted').click(function(){
    $('.pop_box').slideDown('400');
});
$('.closepop').click(function(){
    $('.pop_box').slideUp('400');
});