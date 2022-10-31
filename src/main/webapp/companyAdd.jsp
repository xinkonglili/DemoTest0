<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>
        添加单位
    </title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">

    <link rel="stylesheet" href="static/css/font.css">
    <link rel="stylesheet" href="static/css/index.css">
    <script src="static/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="static/js/index.js"></script>
</head>
<body>

<div class="layui-card">
    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
        <div class="layui-tab-content" >
            <div class="layui-tab-item layui-show">
                <form class="layui-form layui-form-pane" action="" accept-charset="UTF-8">
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            <span class='x-red'>*</span>单位名称
                        </label>
                        <div class="layui-input-block">
                            <input type="text" name="companyName" autocomplete="off" placeholder="单位名称"
                                   class="layui-input" lay-verify="required">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            <span class='x-red'>*</span>地址
                        </label>
                        <div class="layui-input-block">
                            <input type="text" name="address" autocomplete="off" placeholder="请输入地址"
                                   class="layui-input" lay-verify="required">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" >
                            <span class='x-red'>*</span>电话
                        </label>
                        <div class="layui-input-block">
                            <input type="text" name="phone" autocomplete="off" placeholder="电话"
                                   class="layui-input" lay-verify="required">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            <span class='x-red'>*</span>传真
                        </label>
                        <div class="layui-input-block">
                            <input type="text" name="fax" autocomplete="off" placeholder="传真"
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
                    <div class="layui-form-item" style="margin-top: 15px">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit="" lay-filter="*">立即提交</button>
                        </div>
                    </div>
                </form>
                <div style="height:100px;"></div>
            </div>

        </div>
    </div>
</div>

<script>
    layui.use(['layer','form','table'], function(){
        const form = layui.form,
            layer = layui.layer;


        //监听提交
        form.on('submit(*)', function(data){
            $.ajax({
                type: "POST",
                url: "companyServlet?action=addCompany",
                dataType: "json",
                data:data.field,
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                async: false,
                cache: false,
                success: function (res) {
                    if (res.code === 200) {
                        const index = layer.alert("添加成功", function () {
                            // 关闭弹出层
                            layer.close(index);
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            parent.layui.table.reload('currentTableId',{page:{curr:$(".layui-laypage-em").next().html()}});
                            top.location.reload();
                        });
                    }
                }
            });
            return false;
        });

    })
</script>

</body>
</html>