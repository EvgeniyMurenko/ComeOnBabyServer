"use strict";
$(document).ready(function () {


    // Left menu block toggle
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });

    // Show nice tables
    $('#dataTable').DataTable({
        responsive: true,
        language: {
            url: "/resources/js/dataTables/dataTables.korean.lg"
        }
    });

    // Recipes open link
    $('.recipesOpenLink').on('click', function() {
        var url = $("input[name=url]").val();
        var win = window.open(url, '_blank');
        win.focus();
    });

    // Tooltipe
    $('[data-toggle="tooltip"]').tooltip();

    // Delete confirm
    function sebSweetConfirm(originLink){
        swal({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3b9c96',
            cancelButtonColor: '#aaa',
            confirmButtonText: 'Yes, delete it!'
        }).then(function(isConfirm){
            if (isConfirm) {
                window.location.href = originLink;
            }
        })
    }

    $('.deleteConfirm').click(function(event){
        event.preventDefault();
        var originLink = $(this).attr("href");
        sebSweetConfirm(originLink);
    });

    // pushNotificationsForm from validation
    $('#pushNotificationsForm').formValidation({
        framework: 'bootstrap',
        excluded: ':disabled',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            phoneNumber: {
                validators: {
                    notEmpty: {
                        message: 'The notice id is required'
                    },
                    regexp: {
                        message: 'The notice id can only contain the digits',
                        regexp: /^[0-9  ]+$/
                    }
                }
            },
            notification: {
                validators: {
                    notEmpty: {
                        message: 'The notification is required'
                    }
                }
            }
        }
    });
});

/*$("#tenFilesInput").fileinput({
    allowedFileExtensions : ['jpg', 'jpeg','png'],
    overwriteInitial: false,
    /!*maxFileSize: 1024,*!/
    maxFilesNum: 10,
});*/

// CKEDITOR show
var editor = CKEDITOR.replace('editor', {
    toolbar : 'Basic',
    width : '100%',
    height : '250',
});
editor.add;
