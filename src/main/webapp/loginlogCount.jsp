<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>登录日志信息</title>
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
                            <label class="layui-form-label">单位名称</label>
                            <div class="layui-input-inline">
                                <select id="company" name="companyId" lay-filter="company">
                                    <option value="-1" selected>选择单位</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">部门名称</label>
                            <div class="layui-input-inline">
                                <select id="department" name="departmentId" autocomplete="off">
                                    <option value="-1" selected>选择部门</option>
                                </select>
                            </div>
                        </div>

                        <div class="layui-inline">
                            <label class="layui-form-label">姓名</label>
                            <div class="layui-input-inline">
                                <input type="text" name="name" autocomplete="off" class="layui-input">
                            </div>
                        </div>

                        <div class="layui-inline">
                            <label class="layui-form-label">登录名</label>
                            <div class="layui-input-inline">
                                <input type="text" name="username" autocomplete="off" class="layui-input">
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
            url: 'userServlet?action=getLoginLogCount',
            toolbar: '#toolbarDemo',
            defaultToolbar: [],
           /* defaultToolbar: ['filter', 'exports', 'print', {
                title: '提示',
                layEvent: 'LAYTABLE_TIPS',
                icon: 'layui-icon-tips'
            }],*/
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
                    {field: 'name',  title: '姓名', align: "center"},
                    {field: 'username',  title: '登录名', align: "center"},
                    {field: 'count',  title: '登陆次数', align: "center"},
                    {field: 'departmentName',  title: '部门', align: "center"},
                    {field: 'companyName',  title: '单位', align: "center"},
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
                    url: "userServlet?action=exportLoginLogCount",
                    timeout: 5000,
                    responseType: 'blob'
                }).then(function (res) {
                    var data = res.data;
                    var blob = new Blob([data], {type: 'application/octet-stream'});
                    var url = URL.createObjectURL(blob);
                    var exportLink = document.createElement('a');
                    exportLink.setAttribute("download","登录统计.xls");
                    exportLink.href = url;
                    document.body.appendChild(exportLink);
                    exportLink.click();
                })

            }
        });
        // 监听搜索操作
        form.on('submit(data-search-btn)', function (data) {
            //执行搜索重载
            if (data.field.end.length != 0) {
                if (data.field.start > data.field.end) {
                    layer.msg("'开始日期'大于'结束日期'", {icon: 7});
                    return false;
                }
            }
            table.reload('currentTableId', {
                page: {
                    curr: 1
                },
                where: {
                    companyId: data.field.companyId,
                    departmentId:data.field.departmentId,
                    name:data.field.name,
                    username:data.field.username,
                    start: data.field.start,
                    end: data.field.end
                }
            });
            layer.msg("查询成功");
            return false;
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
