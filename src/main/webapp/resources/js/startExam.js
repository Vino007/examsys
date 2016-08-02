/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    //queryString
    var queryString = location.search.substr(1);
    var queryStrArr = queryString.split('&');
    var examId = queryStrArr[0].split('=')[1];
    var isMock = queryStrArr[1].split('=')[1];
    var urlObj;

    if (isMock == 'true') {
        urlObj = examFindMockQuestions;
    } else {
        urlObj = examFindQuestions;
    }

    //get questions
    $.ajax({
        url: urlObj.url,
        type: urlObj.type,
        data: {'examId': examId}
    }).done(function (data) {
        if (data.success) {
            $('#exam-list').html('');
            $.each(data.data.questions, function (key, value) {
                var li = '', img = '', input = '', choices = [];
                switch (value.type) {
                    case 1:
                        if (value.contentImageUrl != '') {
                            img = '<img src="' + value.contentImageUrl + '">';
                        } else {
                            img = '';
                        }
                        choices = value.choices.split(';');
                        $.each(choices, function (k, v) {
                            input += '<label><input type="radio" name="' + (key + 1) + '" value="' + (k + 1) + '">' + v + '</label>';
                        });
                        li = '<li class="list-group-item"><p>' + value.content + '</p>' + img + '<div class="form-group">' + input + '</div></li>';
                        break;
                    case 2:
                        if (value.contentImageUrl != '') {
                            img = '<img src="' + value.contentImageUrl + '">';
                        } else {
                            img = '';
                        }
                        choices = value.choices.split(';');
                        $.each(choices, function (k, v) {
                            input += '<label><input type="checkbox" name="' + (key + 1) + '" value="' + (k + 1) + '">' + v + '</label>';
                        });
                        li = '<li class="list-group-item"><p>' + value.content + '</p>' + img + '<div class="form-group">' + input + '</div></li>';
                        break;
                    case 3:
                        li = '<li class="list-group-item"><p>' + value.content + '</p><div class="form-group"><textarea class="form-control"name="' + (key + 1) + '"></textarea></div></li>';
                        break;
                    case 4:
                        li = '<li class="list-group-item"><p>' + value.content + '</p><div class="form-group"><label><input type="radio"name="' + (key + 1) + '"value="true">是</label><label><input type="radio"name="' + (key + 1) + '"value="false">否</label></div></li>';
                        break;
                    default:
                        break;
                }
                $('#exam-list').append(li);
            });

            //set countdown
            var date = new Date(new Date().valueOf() + 60 * 60 * 1000);
            $('#countdown').countdown(date, function (event) {
                $(this).html(event.strftime('%H:%M:%S'));
            }).on('finish.countdown', function () {
                
            });

        } else {
            alert(data.msg);
        }
    });


    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.course.id + '</td><td>' + value.course.courseName + '</td><td data-id="' + (value.courseCategory == null ? '' : value.courseCategory.id) + '">' + (value.courseCategory == null ? "未分类" : value.courseCategory.coursecatName) + '</td><td>' + value.startTime + '</td><td>' + value.endTime + '</td><td>' + value.passLine + '</td><td>' + value.questionNumber + '</td><td><button class="btn btn-primary form-control start-exam" data-id="' + value.id + '"data-toggle="modal"data-target="#start-exam">开始考试</button></td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td><td><button class="btn btn-primary form-control bind"data-toggle="modal"data-target="#bind">绑定考题</button></td><td><button class="btn btn-primary form-control user">参考人员</button></td></tr>';
            $('#main-table').append(tr);
        });
    }

});