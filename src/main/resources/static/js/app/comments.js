var comments = {

    init : function() {
        var _this = this;
        $('#btn-save-comment').on('click', function() {
            _this.save();
        });
        $('.comment-list').on('click', '.btn-edit-comment', function() {
            _this.edit($(this));
        });
        $('.comment-list').on('click', '.btn-update-comment', function() {
            _this.update($(this));
        });
        $('.comment-list').on('click', '.btn-reply-comment', function() {
            _this.reply($(this));
        });
        $('.comment-list').on('click', '.btn-save-reply-comment', function() {
            _this.saveReply($(this));
        });
        $('.comment-list').on('click', '.btn-delete-comment', function() {
            if(confirm('정말로 삭제하시겠습니까?')){
                _this.delete($(this));
            }
        });
    },
    save : function() {
        var _this = this;
        var content = $('#input-comment-content').val().trim();
        if(!content) {
            alert('내용을 입력해 주세요.');
            $('#input-comment-content').focus();
            return;
        }

        var data = {
            postId: $('#post-id').val(),
            content: $('#input-comment-content').val(),
            isSecret: $('#is-secret').is(':checked')
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
            $('#input-comment-content').val("");
            $('#is-secret').prop("checked", false);
            _this.getList();
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    edit : function(editButton) {
        var comment = editButton.closest('.comment'); // 편집(수정) 대상 댓글
        var id = comment.data('id');
        var content = comment.find('.comment-content'); // 내용 부분
        var isSecret = comment.find('img[alt="locked"]').length > 0; // 비밀 여부

        var editComment = '<div class="edit-comment">'
                        + '    <textarea class="form-control input-update-comment" rows="3" placeholder="내용을 입력하세요"></textarea>'
                        + '    <div class="d-flex py-2">'
                        + '        <div class="custom-control custom-checkbox ml-auto mr-1 pt-1">'
                        + '            <input class="custom-control-input" type="checkbox" id="' + id + '-is-secret"' + (isSecret ? ' checked>' : '>')
                        + '            <label class="custom-control-label text-muted-comment" for="' + id + '-is-secret">비밀로 하기</label>'
                        + '        </div>'
                        + '        <button type="button" class="btn btn-success btn-sm btn-update-comment">완료</button>'
                        + '    </div>'
                        + '</div>';

        if(editButton.text() == '수정') {
        //============== 댓글 편집 초기화 ================
            $('.reply-comment').remove();
            $('.edit-comment').remove();
            $('.btn-reply-comment').text('답글').removeClass('active');
            $('.btn-edit-comment').text('수정').removeClass('active');
            $('.comment-content').show();
        //==============================================
            content.toggle();
            editButton.text('취소').toggleClass('active');
            comment.find('.comment-body').append(editComment);
            $('.input-update-comment').val(content.text()).focus();
        } else {
            $('.edit-comment').remove();
            content.toggle();
            editButton.text('수정').toggleClass('active');
        }
    },
    update : function(updateButton) {
        var _this = this;
        var id = updateButton.closest('.comment').data('id');
        var inputUpdateComment = $('.input-update-comment');

        if(!inputUpdateComment.val().trim()) {
            alert('내용을 입력해 주세요.');
            inputUpdateComment.focus();
            return;
        }

        var data = {
            content: inputUpdateComment.val(),
            isSecret: $('#' + id + '-is-secret').is(':checked')
        };

        $.ajax({
            type: 'PUT',
            url: '/api/v1/comments/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
            _this.getList();
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    reply : function(replyButton) {
        var comment = replyButton.closest('.comment'); // 답글 대상(부모가 될 댓글)
        var id = comment.data('id');
        var groupId = comment.data('group-id');
        var hasParent = id != groupId; // 대상의 답글(대댓글) 여부
        var isSecret = comment.find('img[alt="locked"]').length > 0; // 비밀 여부
        var lastGroupComment = $('.comment[data-group-id="' + groupId + '"]:last'); // 그룹의 마지막 댓글

        var replyComment = '<div class="reply-comment border border-dark rounded-lg px-2 pt-2 mb-1" style="margin-left: 58px;">';
        if(hasParent) { // 답글의 답글이면 수신자 표기
            var replyTo = comment.find('.comment-writer').text();
            replyComment += '   <div class="text-primary">@' + replyTo + '</div>';
        }
            replyComment += '   <textarea class="form-control input-reply-comment" rows="3" placeholder="내용을 입력하세요"></textarea>'
                          + '   <div class="d-flex py-2">'
                          + '       <div class="custom-control custom-checkbox ml-auto mr-1 pt-1">'
                          + '           <input class="custom-control-input reply-is-secret" type="checkbox" id="reply-' + id + '-is-secret"' + (isSecret ? ' checked>' : '>')
                          + '           <label class="custom-control-label text-muted-comment" for="reply-' + id + '-is-secret">비밀로 하기</label>'
                          + '       </div>'
                          + '       <button type="button" class="btn btn-success btn-sm btn-save-reply-comment" data-parent-id="' + id + '">등록</button>'
                          + '   </div>'
                          + '</div>';

        if(replyButton.text() == '답글') {
        //============== 댓글 편집 초기화 ================
            $('.reply-comment').remove();
            $('.edit-comment').remove();
            $('.btn-reply-comment').text('답글').removeClass('active');
            $('.btn-edit-comment').text('수정').removeClass('active');
            $('.comment-content').show();
        //==============================================
            replyButton.text('취소').toggleClass('active');;
            lastGroupComment.after(replyComment);
            $('.input-reply-comment').focus();
        } else {
            $('.reply-comment').remove();
            replyButton.text('답글').toggleClass('active');;
        }
    },
    saveReply : function(saveReplyButton) {
        var _this = this;
        var parentId = saveReplyButton.data('parent-id');
        var inputReplyComment = $('.input-reply-comment');

        if(!inputReplyComment.val().trim()) {
            alert('내용을 입력해 주세요.');
            inputReplyComment.focus();
            return;
        }

        var data = {
            postId: $('#post-id').val(),
            parentId: parentId,
            content: inputReplyComment.val(),
            isSecret: $('#reply-' + parentId + '-is-secret').is(':checked')
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(result) {
            _this.getList();
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function(deleteButton) {
        var _this = this;
        var id = deleteButton.closest('.comment').data('id');

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/comments/' + id,
            dataType: 'json',
        }).done(function(result) {
            _this.getList();
        }).fail(function(error) {
            alert(JSON.stringify(error))
        });
    },
    getList : function() {
        var _this = this;
        var id = $('#post-id').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/' + id + '/comments',
            dataType: 'json',
        }).done(function(result) {
            _this.showList(result);
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    showList : function(comments) {
        var commentList = '';

        $.each(comments, function(i, dto) {
            if(!dto.parentId) { // 답글이 아닌(부모가 없는) 댓글
                commentList += '<div class="comment d-flex px-1 pt-1 mb-1' + (dto.isMyComment ? ' my-comment"' : '"')
                             + ' data-id="' + dto.id + '" data-group-id="' + dto.groupId + '">';
            } else {
                commentList += '<div class="comment d-flex px-1 pt-1 mb-1 border-bottom' + (dto.isMyComment ? ' my-comment"' : '"')
                             + ' data-id="' + dto.id + '" data-group-id="' + dto.groupId + '" style="margin-left: 58px;">';
            }

            if(dto.isDeleted) {
                commentList += '<div class="col text-muted py-2 mb-2">삭제된 댓글입니다.</div>';
            } else {
                if(!dto.isVisible) { // 비밀 댓글 처리
                    commentList += '<div class="col text-muted py-2 mb-2">'
                                 + '    <img src="/images/lock.svg" height="20" alt="locked">'
                                 + '    <span>비밀 댓글입니다.</span>'
                                 + '    <span class="text-muted-comment">' + dto.modifiedDate + '</span>'
                                 + (dto.isModified ? ' <span class="text-muted-comment">수정됨</span>' : '')
                                 + '</div>';
                } else {
                    commentList += '<div class="comment-profile">';
                    if(!dto.profile) {
                        commentList += '<img src="/images/blank_profile_picture.png" class="rounded-circle mr-2" height="50" width="50" alt="blank_profile">';
                    } else {
                        commentList += '<img src="' + dto.profile + '" class="rounded-circle mr-2" height="50" width="50" alt="profile_picture">';
                    }
                    commentList += '</div>'
                                 + '<div class="flex-grow-1">'
                                 + '    <div class="comment-info row mx-0">'
                                 + '        <div class="col-sm-auto px-0 mr-1">'
                                 + '            <span class="font-weight-bold comment-writer">' + dto.writer + '</span>'
                                 + (dto.equalsPostWriter ? ' <span class="badge badge-pill badge-danger">작성자</span>' : '')
                                 + '            <span class="text-muted-comment">' + dto.modifiedDate + '</span>'
                                 + (dto.isModified ? ' <span class="text-muted-comment">수정됨</span>' : '')
                                 + '        </div>'
                                 + '        <div class="comment-buttons col px-0">'
                                 + '            <div class="d-flex">'
                                 + '                    <button type="button" class="btn btn-outline-dark btn-xs btn-reply-comment">답글</button>'
                                 + (dto.isMyComment ? ' <button type="button" class="btn btn-outline-primary btn-xs btn-edit-comment ml-auto mr-1">수정</button>'
                                                    + ' <button type="button" class="btn btn-outline-danger btn-xs btn-delete-comment">삭제</button>'
                                                    : '')
                                 + '            </div>'
                                 + '        </div>'
                                 + '    </div>'
                                 + '    <div class="comment-body' + (!dto.parentId ? ' border-bottom">' : '">')
                                 + '        <pre class="mb-2">' + (dto.isSecret ? '<img src="/images/lock.svg" height="20" alt="locked" class="mr-1">' : '')
                                 +          (dto.replyTo ? '<a class="reply-to mr-1">@' + dto.replyTo + '</a>' : '') + '<span class="comment-content">' + dto.content + '</span></pre>'
                                 + '    </div>'
                                 + '</div>';
                } // isVisible-end
            } // isNotDeleted-end
            commentList += '</div>';
        }); // each-end
        $('.comment-list').empty().append(commentList);
        $('.comment-count').text(comments.length);
    }

};

comments.init();
