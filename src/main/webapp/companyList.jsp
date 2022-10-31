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
    <link rel="stylesheet"  href="static/css/public.css" media="all">
    <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <fieldset class="table-search-fieldset">
            <div style="margin: 10px 10px 10px 10px">
                <form class="layui-form layui-form-pane" action="">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">单位名称</label>
                            <div class="layui-input-inline">
                                <input type="text" name="companyName" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <button type="submit" class="layui-btn layui-btn-primary"  lay-submit lay-filter="data-search-btn"><i class="layui-icon"></i> 搜 索</button>
                        </div>
                    </div>
                </form>
            </div>
        </fieldset>

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加 </button>
                <button class="layui-btn layui-btn-sm layui-btn-danger data-delete-btn" lay-event="delete"> 删除 </button>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
        </script>

    </div>
</div>
<script src="static/lib/layui/layui.js" charset="utf-8"></script>
<script src="static/js/ajaxRequest.js"></script>
<script src="static/js/time.js"></script>
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
<script>
    layui.use(['form', 'table'], function () {
        const $ = layui.jquery,
            form = layui.form,
            table = layui.table;

        table.render({
            elem: '#currentTableId',
            url: 'companyServlet?action=getCompany',
            toolbar: '#toolbarDemo',
            defaultToolbar: [],
            response: {
                statusCode: 200 //规定成功的状态码，默认：0
            },
            parseData: function(res) {
                return {
                    "code": res.code,
                    "data": res.data, //解析数据列表
                    "count": res.count,
                };
            },
            done: (res, curr, count) => {
                if (curr > 1 && res.data.length === 0) {
                    curr = curr - 1;
                    table.reload('currentTableId', {
                        page: {
                            curr: curr
                        }
                    });
                }
            },
            cols: [
                [
                    {type: "checkbox"},
                    {type: 'numbers', title: 'ID', sort: true},
                    {field: 'companyName',  title: '单位名称', align: "center"},
                    {field: 'address',  title: '地址', align: "center"},
                    {field: 'phone',  title: '电话', align: "center"},
                    {field: 'fax',  title: '传真', align: "center"},
                    {
                        field: 'status',
                        title: '状态', align: "center",
                        templet:(row => {
                                let menuId = row.id;
                                if (row.status === 0) {
                                    return " <a class='layui-btn layui-btn-disabled layui-btn-xs'>禁用</a>"
                                } else if (row.status === 1) {
                                    return "<a class='layui-btn layui-btn-normal layui-btn-xs'>启用</a>"
                                }}
                        )
                    },
                    {title: '操作', toolbar: '#currentTableBar', align: "center"}
                ]
            ],
            page: true,
            skin: 'line'
        });

        // 监听搜索操作
        form.on('submit(data-search-btn)', function (data) {
            //执行搜索重载
            table.reload('currentTableId', {
                page: {
                    curr: 1
                },
                where: {
                    companyName: data.field.companyName,
                }
            });
            layer.msg("查询成功");
            return false;
        });


        /**
         * toolbar监听事件
         */
        table.on('toolbar(currentTableFilter)', function (obj) {
            if (obj.event === 'add') {  // 监听添加操作
                const index = layer.open({
                    title: '添加用户',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['55%', '55%'],
                    content: 'companyAdd.jsp',
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {  // 监听删除操作
                //获取选中的数据
                const checkStatus = table.checkStatus('currentTableId');
                const data = checkStatus.data.map((n) => {
                    return n.companyId;
                });
                if (data.length !== 0) {
                    layer.confirm('真的删除行么', function (index) {
                        jqPromiseAjax({
                            url:'companyServlet?action=deleteCompanyById',
                            contentType : "application/json;charset=utf-8",
                            dataType:'json',
                            data: {idStr: data.toString()}
                        }).then(res => {
                            if (res.code === 200) {
                                table.reload('currentTableId');
                                layer.msg("删除成功", {time : 1000});
                            }else{
                                layer.msg("存在下属部门，不能删除！",{time:1000,icon: 5})
                            }
                        });
                        layer.close(index);
                    });
                } else {
                    layer.msg("请先勾选删除项", {time : 1000});
                }
            }
        });

        table.on('tool(currentTableFilter)', function (obj) {
            const data = obj.data;
            if (obj.event === 'edit') {
                const index = layer.open({
                    title: '编辑标签',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['55%', '75%'],
                    content: 'companyEdit.jsp',
                    success: function(layero, index) {
                        const body = layer.getChildFrame('body', index);
                        body.contents().find("#companyId").val(data.companyId);
                    }
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
                return false;
            } else if (obj.event === 'delete') {
                var idStr=data.companyId; //companyId
                layer.confirm('真的删除行么', function (index) {
                    jqPromiseAjax({
                        url:'companyServlet?action=deleteCompanyById',
                        contentType : "application/json;charset=utf-8",
                        dataType:'json',
                        data: { idStr: idStr}
                    }).then(res => {
                        if (res.code === 200) {
                            table.reload('currentTableId');
                            layer.msg("删除成功", {time : 1000});
                        }else{
                            layer.msg("存在下属部门，不能删除！",{time:1000,icon: 5})
                        }
                    });
                    layer.close(index);
                });
            }
        });

    });
</script>

</body>
</html>
