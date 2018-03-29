$("#forget").click(function () {
    getForgetPop();
});

$('#closeForget').click(function () {
    $('#forgettextmain').empty();
    $("#forgetPassword").slideUp('400');
});

function sendForgetPasswordEmail() {
    $.ajax({
        url: "/forgetPassword",
        data: {
            name: $('#forgetName').val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                $('#forgettextmain').empty();
                getCheckCodePop();
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
            checkCode:$('#checkCode').val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                findPassword();
            } else if (data.fail) {
                alert("验证失败！");
            }
        },
        error: function (data) {
            alert("验证失败！" + data);
        }
    })
}

function findPassword() {
    $.ajax({
        url: "/findPassword",
        dataType: "json",
        success: function (data) {
            if (data.password) {
                confirm("您的密码是：" + data.password);
            } else {
                alert("找回密码失败！");
            }
        },
        error: function (data) {
            confirm("找回密码失败！" + data);
        }
    })
}

function getForgetPop() {
    var h3Node = document.createElement('h3');
    var textNode = document.createTextNode("请填入需要找回密码的用户名");
    h3Node.appendChild(textNode);
    var formNode = document.createElement('form');
    var tableNode = document.createElement("table");
    tableNode.setAttribute("class", "normTbe");
    tableNode.setAttribute("cellspacing", "0");
    tableNode.setAttribute("cellpadding", "0");
    tableNode.setAttribute("border", "0");
    var trNode = document.createElement("tr");
    var tdNode1 = document.createElement("td");
    var pNode = document.createElement("p");
    var textNode1 = document.createElement("用户名");
    pNode.appendChild(textNode1);
    tdNode1.appendChild(pNode);
    var tdNode2 = document.createElement("td");
    var inputNode = document.createElement("input");
    inputNode.setAttribute("type", "text");
    inputNode.setAttribute("id", "forgetName");
    inputNode.setAttribute("name", "forgetName");
    tdNode2.appendChild(inputNode);
    trNode.appendChild(tdNode1);
    trNode.appendChild(tdNode2);
    tableNode.appendChild(trNode);
    formNode.appendChild(tableNode);
    var divNode = document.createElement("div");
    divNode.setAttribute("class", "btn_a1");
    var aNode = document.createElement("input");
    aNode.setAttribute("type", "button");
    aNode.setAttribute("class", "dtadd");
    aNode.setAttribute("onclick", "sendForgetPasswordEmail()");
    var textNode2 = document.createTextNode("提交");
    aNode.appendChild(textNode2);
    formNode.appendChild(aNode);
    $('#forgettextmain')[0].appendChild(h3Node).appendChild(formNode);
    $('#forgetPassword').slideDown('400');
}

function getCheckCodePop() {
    var h3Node = document.createElement('h3');
    var textNode = document.createTextNode("请填入您收到的邮件中给出的验证码");
    h3Node.appendChild(textNode);
    var formNode = document.createElement('form');
    var tableNode = document.createElement("table");
    tableNode.setAttribute("class", "normTbe");
    tableNode.setAttribute("cellspacing", "0");
    tableNode.setAttribute("cellpadding", "0");
    tableNode.setAttribute("border", "0");
    var trNode = document.createElement("tr");
    var tdNode1 = document.createElement("td");
    var pNode = document.createElement("p");
    var textNode1 = document.createElement("验证码");
    pNode.appendChild(textNode1);
    tdNode1.appendChild(pNode);
    var tdNode2 = document.createElement("td");
    var inputNode = document.createElement("input");
    inputNode.setAttribute("type", "text");
    inputNode.setAttribute("id", "checkCode");
    inputNode.setAttribute("name", "checkCode");
    tdNode2.appendChild(inputNode);
    trNode.appendChild(tdNode1);
    trNode.appendChild(tdNode2);
    tableNode.appendChild(trNode);
    formNode.appendChild(tableNode);
    var divNode = document.createElement("div");
    divNode.setAttribute("class", "btn_a1");
    var aNode = document.createElement("input");
    aNode.setAttribute("type", "button");
    aNode.setAttribute("class", "dtadd");
    aNode.setAttribute("onclick", "vertifyCheckCode()");
    var textNode2 = document.createTextNode("提交");
    aNode.appendChild(textNode2);
    formNode.appendChild(aNode);
    $('#forgettextmain')[0].appendChild(h3Node).appendChild(formNode);
}