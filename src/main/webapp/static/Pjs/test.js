 function exportExcel() {
    axios({
        method: 'POST',
        url: "userServlet?action=test",
        timeout: 5000,
        responseType: 'blob'
    }).then(function (res) {
        var data = res.data;
        //告知浏览器这是一个字节流，浏览器就会以字节流的方式处理，默认的方式就是下载
        var blob = new Blob([data], {type: 'application/octet-stream'});
        var url = URL.createObjectURL(blob);
        var exportLink = document.createElement('a');
        exportLink.setAttribute("download","文件下载.xls");
        exportLink.href = url;
        document.body.appendChild(exportLink);
        exportLink.click();
    })
}