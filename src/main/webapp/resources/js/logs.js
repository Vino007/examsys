/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: logsAll,
        data: {},
        resetTable: resetTable
    };
    init(initObj);

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td>' + value.user.username + '</td><td>' + value.opType + '</td><td>' + value.content + '</td><td>' + value.timestamp + '</td></tr>';
            $('#main-table').append(tr);
        });
    }
});