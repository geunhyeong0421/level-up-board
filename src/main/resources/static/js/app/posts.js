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
        $('a.posts').on("click", function(e) {
            e.preventDefault();
            _this.submitForm($(this));
        });
        $('select[name="size"]').on('change', function() {
            if (!$('input[name="keyword"]').val()) {
                $('input[name="type"]').remove();
                $('input[name="keyword"]').remove();
            }
            $('input[name="page"]').val(1);
            $('#form-criteria').attr("action", "./posts").submit();
        });
        $('#search-keyword').on("keydown", function(e) {
            if(e.keyCode === 13) {
                e.preventDefault();
            }
        });
        $('#search-keyword').on("keyup", function(e) {
            if(e.keyCode === 13) {
                $('.btn-search').trigger("click");
            }
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
            $('#form-criteria').attr("action", "../" + result).submit();
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
            $('#form-criteria').attr("action", "../posts").submit();
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
            $('#form-criteria').attr("action", "../" + result).submit();
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    submitForm : function(trigger) {
        if (trigger.hasClass("page-link")) {
            $('input[name="page"]').val(trigger.data("page"));
        }
        if (trigger.hasClass("btn-search")) {
            var keyword = $('#search-keyword').val().trim();
            if (!keyword) {
                alert("검색어를 입력해주세요.");
                $('#search-keyword').val("").focus();
                return;
            }
            $('input[name="type"]').val($('#search-type').val());
            $('input[name="keyword"]').val(keyword);
            $('input[name="page"]').val(1);
        }

        if (!$('input[name="keyword"]').val()) {
            $('input[name="type"]').remove();
            $('input[name="keyword"]').remove();
        }
        $('#form-criteria').attr("action", trigger.attr("href")).submit();
    }


};

posts.init();
