<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>用户管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="static/lib/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="static/css/public.css" media="all">
    <%--<script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>--%>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>编辑用户</legend>
        </fieldset>

        <form class="layui-form" action="">
            <input type="hidden" name="id" id="fileId" class="layui-input">
            <div class="layui-form-item">
                <label class="layui-form-label">用户名</label>
                <div class="layui-input-block">
                    <input type="text" name="username" id="username" lay-verify="username" autocomplete="off" placeholder="输入用户名" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">密码</label>
                <div class="layui-input-block">
                    <input type="password" name="password" id="password" lay-verify="password" autocomplete="off" placeholder="输入密码" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">电话</label>
                <div class="layui-input-block">
                    <input type="number" name="phone" id="phone" lay-verify="phone" autocomplete="off" placeholder="输入电话号码" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">学分</label>
                <div class="layui-input-block">
                    <input type="number" name="credit" id="credit" lay-verify="credit" autocomplete="off" value="0" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">角色权限</label>
                <div class="layui-input-inline">
                    <select name="roleId" id="selectId"></select>
                </div>
            </div>
            <div class="layui-form-item" style="margin-top: 15px">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit="" lay-filter="submit">立即提交</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="static/lib/layui/layui.js" charset="utf-8"></script>
<script src="static/js/ion.rangeSlider.js"></script>
<script src="static/js/jquery.min.js"></script>
<script>



    layui.use(['form', 'layedit'], function () {
        const form = layui.form,
            layer = layui.layer;

        setTimeout(function () {
            const id = $('#fileId').val();
            jqPromiseAjax({
                url:`/api/back/user/modify/${id}`,
                contentType : "application/json;charset=utf-8",
            }).then(res => {
                if (res.code === 200) {
                    $('#username').prop("value", res.data.username);
                    $('#password').prop("value", res.data.password);
                    $('#phone').prop("value", res.data.phone);
                    $('#credit').prop("value", res.data.credit);
                    $("#selectId ").val(res.data.roleId);
                    form.render("select");
                }
            });
        }, 100);

        jqPromiseAjax({
            url:'/api/back/role/option',
            type:'post',
            contentType : "application/json;charset=utf-8"
        }).then(res=>{
            if (res.code === 200) {
                const data = res.data;
                const $select = $('#selectId');
                //重置下拉框
                $select.empty();
                $select.append(`<option value="">请选择</option>`);
                for(let key in data) {
                    let optgroup = `<optgroup label="${key}">`;
                    for(let i = 0, len = data[key].length; i < len; i++) {
                        optgroup += `<option value="${data[key][i].id}">${data[key][i].role}</option>`;
                    }
                    optgroup += `</optgroup>`;
                    $select.append(optgroup);
                }
                form.render('select');
            }
        });

        form.verify({
            username: function (value) {
                if (value.length < 1 || value.length > 10) {
                    return '用户为1到10个之间';
                }
            },
            password: function (value) {
                if (value.length < 1 || value.length > 30) {
                    return '密码在30字以内';
                }
            },
            phone: function (value) {
                if (value.length > 11) {
                    return '号码在11字以内';
                }
            }
        });

        //监听提交
        form.on('submit(submit)', function (data) {
            if (data.field.roleId === "") {
                layer.msg('请选择角色权限', {icon: 5});
                return false;
            }
            data.field.username = data.field.username.trim();
            jqPromiseAjax({
                url:'/api/back/user/update',
                type:'post',
                contentType : "application/json;charset=utf-8",
                data: JSON.stringify(data.field)
            }).then(res=>{
                if (res.code === 200) {
                    const index = layer.alert("修改成功", function () {
                        // 关闭弹出层
                        layer.close(index);
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        parent.layui.table.reload('currentTableId',{page:{curr:$(".layui-laypage-em").next().html()}});
                    });
                }

            });
            return false;
        });
    });
</script>

</body>
</html>
