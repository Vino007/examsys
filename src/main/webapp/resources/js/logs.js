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

    //type
    $('.op-type').change(function () {
        var type = $(this).val();
        if (type == '') {
            window.location.reload();
        } else {
            var obj = {
                urlObj: logsSearch,
                data: {'search_opType': type},
                resetTable: resetTable
            };
            init(obj);
        }
    });

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var timestamp = toDateStr(value.timestamp);
            var type = '';
            switch (value.opType) {
                case 1:
                    type = '课程设置日志';
                    break;
                case 2:
                    type = '考试日志';
                    break;
                case 3:
                    type = '管理员日志';
                    break;
                case 4:
                    type = '学员日志';
                    break;
                default:
                    break;
            }
            var tr = '<tr><td>' + value.user.username + '</td><td>' + type + '</td><td>' + value.content + '</td><td>' + timestamp + '</td></tr>';
            $('#main-table').append(tr);
        });
    }
});