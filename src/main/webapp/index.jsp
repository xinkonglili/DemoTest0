<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>后台管理系统</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="stylesheet" href="static/css/font.css">
    <link rel="stylesheet" href="static/css/index.css">
    <link rel="stylesheet" href="static/css/iconfont.css">
    <script src="static/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="static/js/index.js"></script>

</head>
<body class="index">
<!-- 顶部开始 -->
<div class="container">
    <div class="logo">
        <a href="#">后台管理系统</a></div>
    <div class="left_open">
        <a><i title="展开左侧栏" class="iconfont">&#xe699;</i></a>
    </div>



    <ul class="layui-nav right" lay-filter="">
        <li class="new-nav" style="width: 200px">当前登录时间：${sessionScope.currentTime }</li>
        <li class="new-nav" style="width: 180px">上次登录时间：${sessionScope.lastAccessTime }</li>
        <li class="layui-nav-item">
            <a href="javascript:;" style="color: #fff9ec">${user.username}</a>
            <dl class="layui-nav-child">
                <!-- 二级菜单 -->
                <dd>
                    <a href="userServlet?action=logout">退出</a></dd>
            </dl>
        </li>
    </ul>



</div>
<!-- 顶部结束 -->
<!-- 中部开始 -->
<!-- 左侧菜单开始 -->
<div class="left-nav">
    <div id="side-nav">
        <ul id="nav">
            <li>
                <a href="javascript:;" onclick="xadmin.add_tab('单位列表','companyList.jsp')">
                    <i class="layui-icon layui-icon-face-smile" lay-tips="单位管理">&#xe6a2;</i>
                    <cite>单位管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
            </li>
            <li>
                <a href="javascript:;" onclick="xadmin.add_tab('部门列表','departmentList.jsp')">
                    <i class="layui-icon layui-icon-read" lay-tips="部门管理">&#xe6a2;</i>
                    <cite>部门管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
            </li>
            <li>
                <a href="javascript:;">
                    <i class="layui-icon layui-icon-username" lay-tips="用户管理">&#xe6b8;</i>
                    <cite>用户管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">

                    <li>
                        <a onclick="xadmin.add_tab('用户列表','userList.jsp')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>用户列表</cite></a>
                    </li>

                    <li>
                        <a onclick="xadmin.add_tab('登录日志','loginlogList.jsp')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>登录日志</cite></a>
                    </li>

                </ul>
            </li>
            <li>
                <a href="javascript:;">
                    <i class="layui-icon layui-icon-survey" lay-tips="统计管理"></i>
                    <cite>统计管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">
                    <li>
                        <a onclick="xadmin.add_tab('人员统计','staffList.jsp')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>人员统计</cite></a>
                    </li>
                    <li>
                        <a onclick="xadmin.add_tab('登录统计','loginlogCount.jsp')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>登录统计</cite></a>
                    </li>
                </ul>
            </li>
            <li>
                <a onclick="xadmin.add_tab('操作日志','operationinfoList.jsp')">
                    <i class="layui-icon layui-icon-file" lay-tips="操作日志"></i>
                    <cite>操作日志</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
            </li>

        </ul>
    </div>
</div>
<!-- <div class="x-slide_left"></div> -->
<!-- 左侧菜单结束 -->
<!-- 右侧主体开始 -->
<div class="page-content">
    <div class="layui-tab tab" lay-filter="xbs_tab" lay-allowclose="false">
        <ul class="layui-tab-title">
            <li class="home">
                <i class="layui-icon">&#xe68e;</i>单位列表</li></ul>
        <div class="layui-unselect layui-form-select layui-form-selected" id="tab_right">
            <dl>
                <dd data-type="this">关闭当前</dd>
                <dd data-type="other">关闭其它</dd>
                <dd data-type="all">关闭全部</dd></dl>
        </div>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe src='companyList.jsp' frameborder="0" scrolling="yes" class="x-iframe"></iframe>
            </div>
        </div>
        <div id="tab_show"></div>
    </div>
</div>
<div class="page-content-bg"></div>
<style id="theme_style"></style>
<!-- 右侧主体结束 -->
<!-- 中部结束 -->

</body>
<script src="static/js/jquery.min.js"></script>

</html>

<script>
    window.onload=function () {
        var click=document.getElementById('new-nav');
        var news=document.getElementById('news');
        click.addEventListener('mousemove',function () {
            news.style.display='block';
        });
        click.addEventListener('mouseout',function () {
            news.style.display='none';
        });

        var gonggao=document.getElementById('gonggao');
        var gonggaos=document.getElementById('gonggaos');
        gonggao.addEventListener('mousemove',function () {
            gonggaos.style.display='block';
        });
        gonggao.addEventListener('mouseout',function () {
            gonggaos.style.display='none';
        });
    }
</script>
<script type="text/javascript">
    $(function () {
        $('.myscroll').myScroll({
            speed: 60, //数值越大，速度越慢
            rowHeight: 38 //li的高度
        });

    });
    function getTime(){
        var myDate = new Date();
        var myYear = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
        var myMonth = myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
        var myToday = myDate.getDate(); //获取当前日(1-31)
        var myDay = myDate.getDay(); //获取当前星期X(0-6,0代表星期天)
        var myHour = myDate.getHours(); //获取当前小时数(0-23)
        var myMinute = myDate.getMinutes(); //获取当前分钟数(0-59)
        var mySecond = myDate.getSeconds(); //获取当前秒数(0-59)
        var week = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
        var nowTime;

        nowTime = myYear+'-'+fillZero(myMonth)+'-'+fillZero(myToday)+'&nbsp;&nbsp;'+fillZero(myHour)+':'+fillZero(myMinute)+':'+fillZero(mySecond);
        //console.log(nowTime);
        $('#time').html(nowTime);
    };
    function fillZero(str){
        var realNum;
        if(str<10){
            realNum	= '0'+str;
        }else{
            realNum	= str;
        }
        return realNum;
    }
    setInterval(getTime,1000);
</script>

