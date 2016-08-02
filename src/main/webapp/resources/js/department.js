/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: departmentSearch,
        data: {},
        resetTable: resetTable
    };
    init(initObj);


    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(departmentAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(departmentDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(departmentDelete);
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

        opADU(departmentUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var search = $(this).siblings('input').val();
        var searchArgs = {
            'search_deptName': search
        };
        var initObj = {
            urlObj: departmentSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.deptName + '</td><td>' + value.description + '</td><td>' + value.createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#main-table').append(tr);
        });
    }
});