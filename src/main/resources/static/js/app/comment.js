var comment = {

    init : function() {
        var _this = this;
        $('#btn-save-comment').on('click', function() {
            _this.save();
        });
        $('#btn-update-comment').on('click', function() {
//            _this.update();
        });
        $('#btn-delete-comment').on('click', function() {
            if(confirm('정말로 삭제하시겠습니까?')){
                targetId = $(this).parent().data("id") // this.parentNode.dataset.id;
                _this.delete(targetId);
            }
        });
    },
    save : function() {
        var content = $('#comment-content').val().trim();
        if(!content) {
            alert('내용을 입력해 주세요.');
            $('#comment-content').focus();
            return;
        }

        var data = {
            postId: $('#post-id').val(),
            content: $('#comment-content').val(),
            isSecret: $('#is-secret').is(":checked")
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
            alert('댓글(id=' + result + ')이 등록되었습니다.');
            // .input-comment 초기화
            $('#comment-content').val("");
            $('#is-secret').prop("checked", false);
            // 댓글 갱신

        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        var title = $('#title').val().trim();
        if(!title) {
            alert('제목을 입력해 주세요.');
            $('#title').val("").focus();
            return;
        }
        var content = $('#content').val().trim();
        if(!content) {
            alert('내용을 입력해 주세요.');
            $('#content').focus();
            return;
        }

        var data = {
            title: title,
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
            alert('글이 수정되었습니다.');
            window.location.href = '/posts/' + result;
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function(id) {
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/comments/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function(result) {
            alert(result + '번 댓글이 삭제되었습니다.');
            // 댓글 갱신

        }).fail(function(error) {
            alert(JSON.stringify(error))
        });
    }


};

comment.init();
