<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-03-06
  Time: 上午 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>

<html>

<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta charset="UTF-8">
    <title>上传</title>
    <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.js"></script>
    <style>
        .container {
            width: 100%;
            height: 5rem;
            border: 1px #000 dotted;
            border-radius: 10px;
            text-align: center;
            vertical-align: middle;
        }

        .container1 {
            text-align: center;
            vertical-align: middle;
        }

        .container input {
            width: 15rem;
            height: 2.5rem;
            line-height: 2.5rem;
            background: lightskyblue;
            margin-top: 0.5rem;
            display: none;
        }

        .container div {
            border-radius: 10px;
            margin: 0 auto;
            width: 5rem;
            height: 2.5rem;
            line-height: 2.5rem;
            background: lightskyblue;
            margin-top: 0.5rem;
            border: 1px solid #000;
        }

        .submit {
            background: red;
            width: 5rem;
            height: 2.5rem;
            line-height: 2.5rem;
            margin-top: 1rem;
            border-radius: 10px;
        }

        .addition {
            width: 100%;
            height: auto;
            border-top: 1px solid #000;
        }

        .addition text {
            padding-left: 0.2rem;
            display: block;
        }
    </style>
    <script>
        $(document).ready(function () {
            $(".upload").click(function () {
                $(".file").click()
            })
            var orderCode = getCode("order");
            if (orderCode != "") {
                $("#order").val(orderCode)
            }
        })

        // --js 获取url参数值
        function getCode(orderCode) {
            new RegExp("(^|&)" + orderCode + "=([^&]*)").exec(window.location.search.substr(1));
            return RegExp.$2
        }
    </script>
</head>
<body>
<form id="form1" action="<%=path%>/upload/file.do" method="post" enctype="multipart/form-data">
    <div class="container">
        <div class="upload">选择</div>
        <input name="order" id="order" style="display: none">
        <input type="file" name="file" class="file" value="选择">
    </div>
    <div class="container1">
        <input type="button" value="提交" onclick="upload()" class="submit">
    </div>
</form>
<div class="addition">
    <h1 style="text-align: center">上传说明</h1>
    <text>1.单次上传仅支持5MB以内的文件</text>
    <text>2.箱单、发票、报关单以及合同可放置在一个Excel内上传</text>
    <text>3.其他资质类文件可以放在文件夹内解压后上传</text>
    <text>4.请将文件命名备注好再上传</text>
</div>
<%--    <iframe name="frame1" frameborder="0" height="40"></iframe>--%>
<!-- 其实我们可以把iframe标签隐藏掉 -->
<script type="text/javascript">
    function upload() {
        $("#form1").submit();
//         var t = setInterval(function() {
//             //获取iframe标签里body元素里的文字。即服务器响应过来的"上传成功"或"上传失败"
//             var word = $("iframe[name='frame1']").contents().find("body").text();
//             if(word != "") {
// 						alert(word); //弹窗提示是否上传成功
// //						clearInterval(t); //清除定时器
//             }
//         }, 1000);
    }

    $(document).ready(function () {
        var result = "<%=request.getAttribute("upload_result")%>";
        if (result != "null") {
            alert(result)
        }
    })
</script>
</body>

</html>
