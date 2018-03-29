$("#forget").click(function () {
   //TODO 跳出输入用户名窗口
});

function sendForgetPasswordEmail() {
    $.ajax({
        url: "/forgetPassword",
        data: {
            name:
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                //TODO 跳出输入验证码窗口
            }
        },
        error: function (data) {
            alert("发送邮件失败!" + data);
        }
    });
}

function vertifyCheckCode() {
    $.ajax({
        url: "/vertifyCheckCode",
        data: {
            checkCode:
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                findPassword();
            } else if (data.fail) {
                //TODO 清空
            }
        },
        error: function (data) {
            alert(data);
        }
    })
}

function findPassword() {
    $.ajax({
        url: "/findPassword",
        data: {
            name:
        },
        dataType: "json",
        success: function (data) {
            if (data.password) {
                //TODO 显示密码;
            }
        },
        error: function (data) {
            alert(data);
        }
    })
}