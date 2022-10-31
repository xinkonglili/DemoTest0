<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title>添加用户</title>
    <link rel="stylesheet" href="static/lib/layui/css/layui.css">
    <style>
        *{ margin: 0; padding: 0;}
        #formdata{max-width: 852px; margin:0 auto}

        .btn { border-radius: 0px; font-weight: 100; cursor: pointer; display: inline-block; padding: 5px; font-size: 14px;
            font-family: '微软雅黑' }

        .btn-primary { color: #fff; text-shadow: 0 1px rgba(0, 0, 0, .1);
            background-image: -webkit-linear-gradient(top, #4d90fe 0, #4787ed 100%);
            background-image: -o-linear-gradient(top, #4d90fe 0, #4787ed 100%);
            background-image: -webkit-gradient(linear, left top, left bottom, from(#4d90fe), to(#4787ed));
            background-image: linear-gradient(to bottom, #4d90fe 0, #4787ed 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff4d90fe', endColorstr='#ff4787ed', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
            background-repeat: repeat-x;
            border: 1px solid #3079ed;
        }

        .btn-success { color: #fff; text-shadow: 0 1px rgba(0, 0, 0, .1);
            background-image: -webkit-linear-gradient(top, #35aa47 0, #35aa47 100%);
            background-image: -o-linear-gradient(top, #35aa47 0, #35aa47 100%);
            background-image: -webkit-gradient(linear, left top, left bottom, from(#35aa47),
            to(#35aa47));
            background-image: linear-gradient(to bottom, #35aa47 0, #35aa47 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff35aa47',
            endColorstr='#ff35aa47', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
            background-repeat: repeat-x;
            border: 1px solid #359947;
        }

        .gallery .img-item { position: relative; cursor: pointer; }

        .gallery .img-item .delete { position: absolute; display: block; width: 20px; height: 20px;
            color: #fff; background: rgba(232, 0, 0, 0.7);  line-height: 20px; text-align: center;
            border-radius: 50%; top: 0px; right: 0px; cursor: pointer; }

        .img { width: 100px; height: 100px; margin-right: 0px; cursor: pointer;}

        .btn-upload{ margin-left: 0px; float: left; display: block; width: 100px; height: 100px;
            border: 1px solid #ddd; background: #ebebeb; line-height: 100px; font-size: 14px;
            text-align: center; color: #808080; cursor: pointer; }
        .box {
            width: 100%;
            height: 100%;
            background: #333;
            position: absolute;
            top:0px;
            left:0px;
            margin: 0 auto;
            align-items: center; /*定义body的元素垂直居中*/
            justify-content: center; /*定义body的里的元素水平居中*/
            display: none;
            overflow: hidden
        }
        .box img{
            width:100%;
            position: absolute;
        }

    </style>

</head>
<body>
<form class="layui-form layui-form-pane" id="formdata" method="post" enctype="multipart/form-data">
    <input name="userId" id="userId" type="hidden">
    <div class="layui-form-item">
        <label class="layui-form-label">*单位名称</label>
        <div class="layui-input-block">
            <select id="company" name="companyId" lay-filter="company">
                <option value="-1" selected>选择单位</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">*部门名称</label>
        <div class="layui-input-block">
            <select id="department" name="departmentId" autocomplete="off">
                <option value="-1" selected>选择部门</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">
            <span class='x-red'>*</span>姓名
        </label>
        <div class="layui-input-block">
            <input type="text" name="name" id="name" autocomplete="off" placeholder="姓名"
                   class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">
            <span class='x-red'>*</span>登录名
        </label>
        <div class="layui-input-block">
            <input type="text" name="username" id="username" autocomplete="off" placeholder="登录名"
                   class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">
            <span class='x-red'>*</span>密码
        </label>
        <div class="layui-input-block">
            <input type="text" name="password" id="password" autocomplete="off" placeholder="密码"
                   class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">
            <span class='x-red'>*</span>邮箱
        </label>
        <div class="layui-input-block">
            <input type="text" name="email" id="email" autocomplete="off" placeholder="邮箱"
                   class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
            <input type="radio" name="status" value="1" title="启用" checked>
            <div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon layui-anim-scaleSpring"></i>
                <div>启用</div>
            </div>
            <input type="radio" name="status" value="0" title="禁用" >
            <div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i>
                <div>禁用</div>
            </div>
        </div>
    </div>
    <label class="layui-form-label">图片</label>
    <div class="gallery" id="gallery">
        <div class="img-item" style="display: inline-block;" id="first-btn-upload">
            <label for="btn-upload" class="btn-upload" id="btn-upload">上传图片</label>
            <div style="clear: both;"></div>
        </div>
        <div class="layui-input-block">
            <input id="file" type="file" multiple style="display: none">
            <%--<div class="box">
                <img src="" />
            </div>--%>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 15px">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="uploadpic" id="uploadpic">立即提交</button>
        </div>
    </div>
</form>


<script src="static/js/jquery.js"></script> <!-- 你必须先引入jQuery1.8或以上版本 -->
<!-- 必须先引入2.0.8版本的hammer.min.js -->
<script src="https://cdn.bootcdn.net/ajax/libs/hammer.js/2.0.8/hammer.min.js"></script>
<script src="static/lib/layui/layui.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/exif-js/2.3.0/exif.js"></script>
<script>
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            var layer = layui.layer;
            //监听提交
            form.on('submit(uploadpic)', function (data) {
                var companyId=$('#company').val();
                var departmentId=$('#department').val();
                console.log(companyId)
                if(companyId == -1 || departmentId == -1){
                    layer.msg("请选择单位部门",{icon:5});
                    return false;
                }
                layui.use('jquery', function () {
                    var $ = layui.$;
                    // 创建FormData根据form
                    var uploaddata = new FormData($("#formdata")[0]);
                    // 遍历图片数组把图片添加到FormData中
                    // var files = document.getElementById("file").files;
                    var maxsize = 0;
                    for (var i = 0; i < files.length; i++) {
                        uploaddata.append("file", files[i]);
                        maxsize = maxsize + files[i].size;
                    }
                    if (maxsize > 52428800) {
                        layer.msg('上传的文件总大小不能超过50MB，请重新上传！');
                        return;
                    }
                    $.ajax({
                        type: 'post',
                        url: 'userServlet?action=upload', // ajax请求路径
                        data: uploaddata,
                        async: false,
                        dataType:"json",
                        contentType: false,
                        processData: false,
                        success: function (res) {
                            if (res.code === 200) {
                                const index = layer.alert("添加成功", function () {
                                    // 关闭弹出层
                                    layer.close(index);
                                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                                    parent.layui.table.reload('currentTableId',{page:{curr:$(".layui-laypage-em").next().html()}});
                                });
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            layer.msg("发生错误!");
                        }
                    });
                });
                return false;//禁止跳转，否则会提交两次，且页面会刷新

            });
            form.on('select(company)',function (data) {
                console.log("监听")
                $("#department").html("");
                var companyId  = data.value;
                if(companyId==-1){
                    $(" #department").html("");
                    $('#department').append(new Option("选择部门", "-1"));
                    layui.form.render("select");
                }else{
                    $.ajax({
                        type: "POST",
                        url: "departmentServlet?action=getDepartmentByCompanyId",
                        dataType: "json",
                        data: {'companyId':companyId},
                        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                        async: false,
                        cache: false,
                        success: function (res) {
                            $('#department').append(new Option("选择部门", "-1"));
                            $.each(res, function (index, item) {
                                $('#department').append(new Option(item.departmentName, item.departmentId));
                            });
                            layui.form.render("select");
                        }
                    });
                }

            })
        });
    })

    // 预览
    function preView(obj) {
        var pimg = obj;
        // var pimg = document.querySelector("img");
        var oImg = document.querySelector(".box img");
        var oBox = document.querySelector(".box");
        // pimg.onclick=function(){
        oBox.style.display = "flex"
        oImg.src = pimg.src
        // }
        oBox.onclick = function () {
            oBox.style.display = "none"
            oImg.src = ''
        }
        var hammer = new Hammer(oImg);//hammer实例化
        hammer.get('pan').set({direction: Hammer.DIRECTION_ALL});//激活pan(移动)功能
        hammer.get('pinch').set({enable: true});//激活pinch(双指缩放)功能
        hammer.on("panstart", function (event) {
            var left = oImg.offsetLeft;
            var tp = oImg.offsetTop;
            hammer.on("panmove", function (event) {
                oImg.style.left = left + event.deltaX + 'px';
                oImg.style.top = tp + event.deltaY + 'px';
            });
        })

        hammer.on("pinchstart", function (e) {
            hammer.on("pinchout", function (e) {
                oImg.style.transition = "-webkit-transform 300ms ease-out";
                oImg.style.webkitTransform = "scale(2.5)";
            });
            hammer.on("pinchin", function (e) {
                oImg.style.transition = "-webkit-transform 300ms ease-out";
                oImg.style.webkitTransform = "scale(1)";
            });
        })
    }

    // 创建数组保存图片
    var files = new Array();
    var id = 0;
    // 选择图片按钮隐藏input[file]
    $("#btn-upload").click(function () {
        $("#file").trigger("click");
    });
    // 选择图片
    $("#file").change(function () {
        // 获取所有图片
        var img = document.getElementById("file").files;
        // 遍历
        for (var i = 0; i < img.length; i++) {
            // 得到图片
            var file = img[i];
            // 判断是否是图片

            var flag = judgeImgSuffix(file.name);
            if (flag) {

            } else {
                alert("要求图片格式为png,jpg,jpeg,bmp");
                return;
            }

            // 把图片存到数组中
            files[id] = file;
            id++;
            // 获取图片路径
            var url = URL.createObjectURL(file);

            // 创建img
            var box = document.createElement("img");
            box.setAttribute("src", url);
            box.className = 'img';
            box.onclick = function () {
                preView(this);
            };

            // 创建div
            var imgBox = document.createElement("div");
            imgBox.style.float = 'left';
            imgBox.className = 'img-item';

            // 创建span
            var deleteIcon = document.createElement("span");
            deleteIcon.className = 'delete';
            deleteIcon.innerText = 'x';
            // 把图片名绑定到data里面
            deleteIcon.id = img[i].name;
            // 把img和span加入到div中
            imgBox.appendChild(deleteIcon);
            imgBox.appendChild(box);
            // 获取id=gallery的div
            var body = document.getElementsByClassName("gallery")[0];
            // body.appendChild(imgBox);
            var showPlace = document.getElementsByClassName("img-item")[0];
            body.insertBefore(imgBox, showPlace);
            // 点击span事件
            $(deleteIcon).click(function () {
                // 获取data中的图片名
                var filename = $(this).attr('id');
                // 删除父节点
                $(this).parent().remove();
                var fileList = Array.from(files);
                // 遍历数组
                for (var j = 0; j < fileList.length; j++) {
                    // 通过图片名判断图片在数组中的位置然后删除
                    if (fileList[j].name == filename) {
                        fileList.splice(j, 1);
                        id--;
                        break;
                    }
                }
                files = fileList;
            });
        }
    });

    // 判断是否是图片类型
    function judgeImgSuffix(path) {
        var index = path.lastIndexOf('.');
        var suffix = "";
        if (index > 0) {
            suffix = path.substring(index + 1);
        }
        if ("png" == suffix || "jpg" == suffix || "jpeg" == suffix || "bmp" == suffix || "PNG" == suffix || "JPG" == suffix || "JPEG" == suffix || "BMP" == suffix) {
            return true;
        } else {
            return false;
        }
    }
</script>
<script>
    $(document).ready(function () {
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: "companyServlet?action=getAllCompany",
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (data) {
                    $.each(data, function (index, item) {
                        console.log(index, item)
                        $('#company').append(new Option(item.companyName, item.companyId));

                    });
                },
                error: function (err) {
                    console.log("请求失败", err)
                }
            });
        }
    );
</script>
</body>
</html>