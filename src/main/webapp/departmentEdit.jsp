<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>单位管理</title>
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
            <legend>编辑单位</legend>
        </fieldset>

        <form class="layui-form" action="" lay-filter="formDepartment">
            <input type="hidden" name="departmentId" id="departmentId" class="layui-input">
            <input type="hidden" name="companyId" id="companyId" class="layui-input">
            <div class="layui-form-item">
                <label class="layui-form-label">所属单位</label>
                <div class="layui-input-block">
                    <input type="text" name="companyName" id="companyName" lay-verify="required" autocomplete="off" placeholder="地址" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">部门名称</label>
                <div class="layui-input-block">
                    <input type="text" name="departmentName" id="departmentName" lay-verify="required" autocomplete="off" placeholder="输入单位名" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">状态</label>
                <div class="layui-input-inline">
                    <select name="status" id="selectId">
                        <option value="0">禁用</option>
                        <option value="1">启用</option>
                    </select>
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
<script src="static/js/ajaxRequest.js"></script>
<script>



    layui.use(['form', 'layedit'], function () {
        const form = layui.form,
            layer = layui.layer;
        setTimeout(function () {
            form.render("select")
        }, 100);

        //监听提交
        form.on('submit(*)', function (data) {
            data.field.departmentName = data.field.departmentName.trim();
            jqPromiseAjax({
                url:'departmentServlet?action=modifyDepartment',
                type:'POST',
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data: data.field
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
