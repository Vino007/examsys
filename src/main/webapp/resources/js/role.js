/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: roleAll,
        data: {},
        resetTable: resetTable
    };
    init(initObj);


    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(roleAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(roleDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(roleDelete);
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

        opADU(roleUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var search = $(this).siblings('input').val();
        var searchArgs = {
            'search_name': search
        };
        var initObj = {
            urlObj: roleSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var coursecat = '';
            if (value.categories != null) {
                $.each(value.categories, function (k, v) {
                    coursecat += (v.coursecatName + ';');
                });
            }
            var createTime = toDateStr(value.createTime);
           // var createTime = "2014";
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.name + '</td><td>' + value.description + '</td><td>' + coursecat + '</td><td>' + createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td><td><button class="btn btn-primary form-control bind-resource"data-toggle="modal"data-target="#bind-resource">绑定资源</button></td><td><button class="btn btn-primary form-control bind-coursecat"data-toggle="modal"data-target="#bind-coursecat">绑定课程分类</button></td></tr>';
            $('#main-table').append(tr);
        });
    }

    /*ztree start*/
    /*获取选中的resourceid*/
    function getCheckedResourceIds() {
        var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
        var nodes = treeObj.getCheckedNodes(true);

        var resourceIds = [];
        for (var i = 0; i < nodes.length; i++) {
            resourceIds[i] = nodes[i].id;
        }
        return resourceIds;
    }

    /* 提交表单 */
    $("#bindSubmitBtn").click(function () {
        var resourceIds = getCheckedResourceIds();
        var roleId = $('#bind-role-id').val();
        $.ajax({
            async: true,
            cache: false,
            type: roleBind.type,
            data: $.param({
                resourceIds: resourceIds,
                roleId: roleId
            }),
            url: roleBind.url,//请求的action路径
            dataType: 'json'
        }).done(function (data) {
            alert(data.msg);
            $('#bind-resource').modal('hide');
        });
    });

    /* ztree */
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId"
            },

            view: {
                showIcon: true
            }
        },
        check: {
            enable: true
        }
    };

    var zNodes;
    $(document).on('click', '.bind-resource', function () {
        $('#resourceTree').html('');
        var roleId = $(this).parents().siblings('td').eq(0).children('input').val();
        var roleName = $(this).parents().siblings('td').eq(1).text();
        $('#bind-role-id').val(roleId);
        $('#bind-role-name').text(roleName);
        $.ajax({
            async: true,
            cache: false,
            type: roleGetResourceTree.type,
            dataType: 'json',
            data: {'id': roleId},
            url: roleGetResourceTree.url//请求的action路径
        }).done(function (data) {
            if (data.success) {
                zNodes = data.data.tree;   //把后台封装好的简单Json格式赋给treeNodes
                $.fn.zTree.init($("#resourceTree"), setting, zNodes);
            }
        });
    });

    /*ztree end*/

    //绑定课程分类
    $(document).on('click', '.bind-coursecat', function () {
        $('#bind-coursecat-checkbox').html('');
        $('#bind-coursecat-submit').attr('data-id', $(this).parents().siblings('td').eq(0).children().val());
        $.ajax({
            url: roleGetAllCat.url,
            type: roleGetAllCat.type,
            dataType: 'json',
            async: true
        }).done(function (data) {
            if (data.success) {
                $.each(data.data.availableCategories, function (key, value) {
                    var checkbox = '<div><label class="control-label"><input name="bind-coursecat"type="checkbox"value="' + value.id + '">' + value.coursecatName + '</label></div>';
                    $('#bind-coursecat-checkbox').append(checkbox);
                })
            }
        });
    });

    $('#bind-coursecat-submit').click(function () {
        var roleId = $(this).attr('data-id');
        var categoryIds = [];
        $.each($('[name=bind-coursecat]:checked'), function (key, value) {
            categoryIds.push($(value).val());
        });
        var data = {
            'roleId': roleId,
            'categoryIds': categoryIds
        };

        $.ajax({
            url: roleBindCats.url,
            type: roleBindCats.type,
            dataType: 'json',
            data: data
        }).done(function (data) {
            alert(data.msg);
            $('#bind-coursecat').modal('hide');
        });
    });

});