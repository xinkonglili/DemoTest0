<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>操作日志</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="static/lib/layui/css/layui.css" media="all">
    <link rel="stylesheet"  href="static/css/public.css" media="all">
    <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <fieldset class="table-search-fieldset">
            <div style="margin: 10px 10px 10px 10px">
                <form class="layui-form layui-form-pane" action="">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">表名</label>
                            <div class="layui-input-inline">
                                <select id="tableName" name="tableName" lay-filter="company">
                                    <option value="" selected>请选择</option>
                                    <option value="user_t" >用户表</option>
                                    <option value="company_t" >单位表</option>
                                    <option value="department_t" >部门表</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">操作类型</label>
                            <div class="layui-input-inline">
                                <select id="type" name="type" lay-filter="company">
                                    <option value="" selected>请选择</option>
                                    <option value="新增" >新增</option>
                                    <option value="修改" >修改</option>
                                    <option value="删除" >删除</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">操作人</label>
                            <div class="layui-input-inline">
                                <input type="text" name="operator" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input class="layui-input"  autocomplete="off" placeholder="开始日" name="start" id="start">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input class="layui-input"  autocomplete="off" placeholder="截止日" name="end" id="end">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <%--lay-submit lay-filter监听标致--%>
                            <button type="submit" class="layui-btn layui-btn-primary"  lay-submit lay-filter="data-search-btn"><i class="layui-icon"></i> 搜 索</button>
                        </div>
                    </div>
                </form>
            </div>
        </fieldset>

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button type="button"  class="layui-btn layui-btn-warm layui-btn-sm" lay-event="download">
                    <i class="layui-icon"></i>导出Excel
                </button>
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
    layui.use(['form', 'table','laydate'], function () {
        const $ = layui.jquery,
            form = layui.form,
            laydate=layui.laydate,
            table = layui.table;

        //执行一个laydate实例
        laydate.render({
            elem: '#start' //指定元素
        });
        //执行一个laydate实例
        laydate.render({
            elem: '#end' //指定元素
        });

        table.render({
            elem: '#currentTableId',
            url: 'userServlet?action=getOperationInfo',
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
                    {field: 'tableName',  title: '操作表', align: "center"},
                    {field: 'contentBefore',  title: 'Before', align: "center"},
                    {field: 'contentAfter',  title: 'After', align: "center"},
                    {field: 'time',  title: '时间', align: "center"},
                    {field: 'operator',  title: '操作人', align: "center"},
                    {field: 'type',  title: '类型', align: "center"},
                    {field: 'result',  title: '操作结果', align: "center"},
                ]
            ],
            page: true,
            skin: 'line'
        });
        table.on('toolbar(currentTableFilter)', function (obj) {
            if(obj.event === 'download'){
                const checkStatus = table.checkStatus('currentTableId');
                axios({
                    method: 'POST',
                    url: "userServlet?action=exportOperationInfo",
                    timeout: 5000,
                    responseType: 'blob'
                }).then(function (res) {
                    var data = res.data;
                    var blob = new Blob([data], {type: 'application/octet-stream'});
                    var url = URL.createObjectURL(blob);
                    var exportLink = document.createElement('a');
                    exportLink.setAttribute("download","操作日志.xls");
                    exportLink.href = url;
                    document.body.appendChild(exportLink);
                    exportLink.click();
                })

            }
        });

        // 监听搜索操作
        form.on('submit(data-search-btn)', function (data) {
            //执行搜索重载
            table.reload('currentTableId', {
                page: {
                    curr: 1
                },
                where: {
                    tableName:data.field.tableName,
                    type:data.field.type,
                    operator:data.field.operator,
                    start:data.field.start,
                    end:data.field.end,

                }
            });
            layer.msg("查询成功");
            return false;
        });
    });
</script>

</body>
</html>
