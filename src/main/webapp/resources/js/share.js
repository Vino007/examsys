/**
 * Created by chucc on 2016/7/27.
 */
$(document).ready(function () {
    //checkbox select all
    $(document).on('change', '[name=check-all]', function () {
        if ($(this).prop('checked')) {
            $('[name=sub-checkbox]').prop('checked', true);
        } else {
            $('[name=sub-checkbox]').prop('checked', false);
        }
    });

    //sub checkbox
    $(document).on('change', '[name=sub-checkbox]', function () {
        var checkboxnum = $('[name=sub-checkbox]').length;
        var checked = $('[name=sub-checkbox]:checked').length;
        if (checkboxnum == checked) {
            $('[name=check-all]').prop('checked', true);
        } else {
            $('[name=check-all]').prop('checked', false);
        }
    });
});

function opADU(urlObj, submitData) {
    $.ajax({
        url: urlObj.url,
        async: true,
        type: urlObj.type,
        data: getFormJson(submitData)
    }).done(function (data) {
        alert(data.msg);
        if (data.success) {
            window.location.reload();
        }
    });
}

function init(initObj) {
    $.ajax({
        url: initObj.urlObj.url,
        async: true,
        type: initObj.urlObj.type,
        data: initObj.data
    }).done(function (data) {
        if (data.success) {
            initObj.resetTable(data);
            //page
            var totalPages = data.data.page.totalPages;
            var visiblePages = totalPages < 10 ? totalPages : 10;
            $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: visiblePages,
                onPageClick: function (event, page) {
                    $.ajax({
                        url: initObj.urlObj.url,
                        async: true,
                        type: initObj.urlObj.type,
                        data: {"pageNumber": page}
                    }).done(function (data) {
                        if (data.success) {
                            initObj.resetTable(data);
                        }
                    });
                }
            });
        }
    });
}

function getFormJson(formArray) {
    var jsonobj = {};
    $.each(formArray, function (key, value) {
        jsonobj[this.name] = this.value;
    })
    return jsonobj;
}
