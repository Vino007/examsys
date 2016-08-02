/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: questionAll,
        data: {},
        resetTable: resetTable
    };
    init(initObj);


    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(questionAdd, submitData);
    });

    /*add end*/

    /*set online/offline start*/
    $('#set-online').click(function () {
        toggleOnline(questionOnline);
    });

    $('#set-offline').click(function () {
        toggleOnline(questionOffline);
    });

    /*set online/offline end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(questionDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(questionDelete);
    });

    /*delete end*/


    /*edit start*/
    //edit modal
    $(document).on('click', '.edit', function () {
        var that = this;

        $.each($('.edit-form .form-group'), function (key, value) {
            $(value).children().eq(1).val($(that).parents().siblings('td').eq(key + 1).text());
        });

        $('#edit-submit').attr('data-id', $(that).parents().siblings('td').eq(0).children().val());
    });

    //submit edit user
    $('#edit-submit').click(function () {
        var submitData = $('.edit-form').serializeArray();
        var id = $(this).attr('data-id');
        var idObj = {
            name: 'id',
            value: id
        };
        submitData.push(idObj);

        opADU(questionUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var content = $(this).siblings('input').val();
        var type = $(this).siblings('.type-based-search').val();
        var isOnline = $(this).siblings('.is-online').val();
        var searchArgs = {
            'search_content': content,
            'search_type': type,
            'search_isOnline': isOnline
        };
        var initObj = {
            urlObj: questionSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    function toggleOnline(urlObj) {
        var ids = [];
        $.each($('[name=sub-checkbox]:checked'), function (key, value) {
            ids.push($(value).val());
        });
        var obj = {
            name: "id",
            value: ids
        };
        opADU(urlObj, obj);
    }
    
    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox"name="sub-checkbox"value="' + value.id + '"></td><td>' + value.content + '</td><td><img src="' + value.contentImageUrl + '"></td><td>' + value.choices + '</td><td>' + value.answer + '</td><td>' + (value.isOnline ? "是" : "否") + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#main-table').append(tr);
        });
    }
});