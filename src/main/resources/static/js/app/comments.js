var comments = {

    init : function() {
        var _this = this;
        $('#btn-save-comment').on('click', function() {
            _this.save();
        });
        $('.btn-edit-comment').on('click', function() {
            _this.edit($(this)); // 클릭된 버튼 객체를 전달
        });
        $('.btn-update-comment').on('click', function() {
            _this.update($(this)); // 클릭된 버튼 객체를 전달
        });
        $('.btn-reply-comment').on('click', function() {
            _this.reply($(this)); // 클릭된 버튼 객체를 전달
        });
        $('.btn-save-reply-comment').on('click', function() {
            _this.saveReply($(this)); // 클릭된 버튼 객체를 전달
        });
        $('.btn-delete-comment').on('click', function() {
            if(confirm('정말로 삭제하시겠습니까?')){
                _this.delete($(this)); // 클릭된 버튼 객체를 전달
            }
        });
    },
    save : function() {
        var content = $('#input-comment-content').val().trim();
        if(!content) {
            alert('내용을 입력해 주세요.');
            $('#input-comment-content').focus();
            return;
        }

        var data = {
            postId: $('#post-id').val(),
            content: $('#input-comment-content').val(),
            isSecret: $('#is-secret').is(":checked")
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert('댓글(id=' + result + ')이 등록되었습니다.');
            // .input-comment 초기화
//            $('#input-comment-content').val("");
//            $('#is-secret').prop("checked", false);
            // 댓글 갱신
            location.reload();

        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    edit : function(editButton) {
        var comment = editButton.closest('.comment');
        var commentContent = comment.find('.comment-content'); // pre
        var inputUpdateComment = comment.find('.input-update-comment'); // textarea

        function toggle() {
            commentContent.toggle(); // 댓글 내용 토글
            comment.find('.edit-comment').toggle(); // 댓글 편집 도구 토글
            editButton.toggleClass('active'); // 클릭 버튼 색상 변경
            editButton.next().toggle(); // 삭제 버튼 토글
        }

        if(editButton.text() == '수정') {
        //============== 댓글 편집 초기화 ================
            $('.comment-content').show();
            $('.edit-comment').hide();
            $('.reply-comment').hide();
            $('.btn-reply-comment').text('답글').removeClass('active');
            $('.btn-edit-comment').text('수정').removeClass('active');
            $('.btn-delete-comment').show();
        //==============================================
            toggle();
            editButton.text('취소');
            inputUpdateComment.val(commentContent.text()).focus();
        } else {
            toggle();
            editButton.text('수정');
        }
    },
    update : function(updateButton) {
        var comment = updateButton.closest('.comment');

        var id = comment.data('id');
        var inputUpdateComment = comment.find('.input-update-comment');
        var isSecret = comment.find('.update-is-secret');

        if(!inputUpdateComment.val().trim()) {
            alert('내용을 입력해 주세요.');
            inputUpdateComment.focus();
            return;
        }

        var data = {
            content: inputUpdateComment.val(),
            isSecret: isSecret.is(":checked")
        };

        $.ajax({
            type: 'PUT',
            url: '/api/v1/comments/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert(result + '번 댓글 수정 완료.');
            // 댓글 갱신
            location.reload();

        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    reply : function(replyButton) {
        var comment = replyButton.closest('.comment');
        var inputReplyComment = comment.find('.input-reply-comment'); // textarea

        if(replyButton.text() == '답글') {
        //============== 댓글 편집 초기화 ================
            $('.comment-content').show();
            $('.edit-comment').hide();
            $('.reply-comment').hide();
            $('.btn-reply-comment').text('답글').removeClass('active');
            $('.btn-edit-comment').text('수정').removeClass('active');
            $('.btn-delete-comment').show();
        //==============================================

            comment.find('.reply-comment').toggle(); // 답글 편집 도구 토글
            replyButton.text('취소').toggleClass('active');;
            inputReplyComment.val("").focus();
        } else {
            comment.find('.reply-comment').toggle(); // 답글 편집 도구 토글
            replyButton.text('답글').toggleClass('active');;
        }
    },
    saveReply : function(saveReplyButton) {
        var comment = saveReplyButton.closest('.comment');
        var parentId = comment.data('id');
        var inputReplyComment = comment.find('.input-reply-comment');

        if(!inputReplyComment.val().trim()) {
            alert('내용을 입력해 주세요.');
            inputReplyComment.focus();
            return;
        }

        var data = {
            postId: $('#post-id').val(),
            parentId: parentId,
            content: inputReplyComment.val(),
            isSecret: $('#reply-' + parentId + '-is-secret').is(":checked")
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
//            alert('댓글(id=' + result + ')이 등록되었습니다.');
            // 댓글 갱신
            location.reload();

        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function(deleteButton) {
        var id = deleteButton.closest('.comment').data('id');

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/comments/' + id,
            dataType: 'json',
        }).done(function(result) {
//            alert(result + '번 댓글이 삭제되었습니다.');
            // 댓글 갱신
            location.reload();

        }).fail(function(error) {
            alert(JSON.stringify(error))
        });
    },
    getList : function() {
        var id = $('#post-id').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/' + id + '/comments',
            dataType: 'json',
        }).done(function(comments) {
            console.log(comments.length);




        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }

};

comments.init();
