var posts = {

    init : function() {
        var _this = this;
        $('#btn-save').on('click', function() {
            _this.save();
        });
        $('#btn-update').on('click', function() {
            _this.update();
        });
        $('#btn-delete').on('click', function() {
            if(confirm('정말로 삭제하시겠습니까?')){
                _this.delete();
            }
        });
        $('#btn-save-reply').on('click', function() {
            _this.saveReply();
        });
        $('.posts-pagination').on('click', 'a.page-link', function(e) {
            e.preventDefault();
            $('input[name="page"]').val($(this).data('page'));
            $('form').submit();
        });
    },
    save : function() {
        var selectBoard = $('select');
        if(!selectBoard.val()) {
            alert('게시판을 선택해주세요.');
            selectBoard.focus();
            return;
        }
        if(!$('#title').val().trim()) {
            alert('제목을 입력해 주세요.');
            $('#title').val("").focus();
            return;
        }
        if(!$('#content').val().trim()) {
            alert('내용을 입력해 주세요.');
            $('#content').focus();
            return;
        }

        var data = {
            boardId: selectBoard.val(),
            title: $('#title').val().trim(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert('글이 등록되었습니다.');
            window.location.href = './' + result;
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        if(!$('#title').val().trim()) {
            alert('제목을 입력해 주세요.');
            $('#title').val("").focus();
            return;
        }
        if(!$('#content').val().trim()) {
            alert('내용을 입력해 주세요.');
            $('#content').focus();
            return;
        }

        var data = {
            title: $('#title').val().trim(),
            content: $('#content').val()
        };

        var id = $('#post-id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert('글이 수정되었습니다.');
            window.location.href = '../' + result;
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function() {
        var id = $('#post-id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
//            alert('글이 삭제되었습니다.');
            window.location.href = '../posts';
        }).fail(function(error) {
            alert(JSON.stringify(error))
        });
    },
    saveReply : function() {
        if(!$('#title').val().trim()) {
            alert('제목을 입력해 주세요.');
            $('#title').val("").focus();
            return;
        }
        if(!$('#content').val().trim()) {
            alert('내용을 입력해 주세요.');
            $('#content').focus();
            return;
        }

        var data = {
            boardId: $('select').val(),
            parentId: $('#parent-id').val(),
            title: $('#title').val().trim(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert('글이 등록되었습니다.');
            window.location.href = '../' + result;
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }


};

posts.init();
