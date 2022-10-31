$(document).ready(function () {
    $.ajax({
        cache: false,
        async: false,
        type: 'POST',
        url: "/getCollege",
        dataType: 'json',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            $.each(data, function (index, item) {
                console.log(index, item)
                $('#college').append(new Option(item.college_name, item.college_id));

            });
        },
        error: function (err) {
            console.log("请求失败", err)
        }
    });
}
);









