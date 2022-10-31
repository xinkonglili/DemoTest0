<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>部门管理</title>
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
            <legend>添加部门</legend>
        </fieldset>

        <form class="layui-form" action="">
            <input type="hidden" name="id" id="fileId" class="layui-input">
            <div class="layui-form-item">
                <label class="layui-form-label">单位名称</label>
                <div class="layui-input-block">
                    <select id="company" name="companyId" lay-filter="company">
                        <option value="-1" selected>选择单位</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">部门名称</label>
                <div class="layui-input-block">
                    <input type="text" name="departmentName" id="departmentName" lay-verify="required" autocomplete="off" placeholder="部门名称" class="layui-input">
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
            <div class="layui-form-item" style="margin-top: 15px">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit="" lay-filter="*">立即提交</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="static/lib/layui/layui.js" charset="utf-8"></script>
<script src="static/js/ion.rangeSlider.js"></script>
<script src="static/js/jquery.min.js"></script>
<script>



    layui.use(['form', 'layedit','table'], function () {
        const form = layui.form,
            layer = layui.layer;


        //监听提交
        form.on('submit(*)', function (data) {
            if (data.field.companyId === "-1") {
                layer.msg('请选择所属单位', {icon: 5});
                return false;
            }
            data.field.departmentName = data.field.departmentName.trim();
            $.ajax({
                url: 'departmentServlet?action=addDepartment',
                type: 'POST',
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data: data.field,
                success: function (res) {
                    console.log(res.code);
                    if (res.code === 200) {
                        const index = layer.alert("添加成功", function () {
                            // 关闭弹出层
                            layer.close(index);
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            parent.layui.table.reload('currentTableId',{page:{curr:$(".layui-laypage-em").next().html()}});
                        });
                    }
                },
                error: function (error) {
                    layer.msg("删除失败", {time : 1000});
                }
            });
            return false;
        });
    });
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
