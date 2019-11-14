//向后台发送json数据，请求地址为 /comment
function post() {
    var questionId = $("#question_id").val();
    var content    = $("#comment_content").val();
    var user = $("#login").val();
    var secondComment = $("#secondComment").val();
    if(!user){
        alert("请先登录")
        return;
    }
    if(!content){
        alert("不能回复空内容")
        return;
    }
    $.ajax({
        type: "POST",//请求类型
        url: "/comment",//发送的请求
        contentType: 'application/json',//设置发送请求的格式
        //发送的数据（json格式）
        data: JSON.stringify({
            "parent_id":questionId,
            "content":content,
            "type":0
        }),
        //成功后返回的数据
        success: function (response) {
            if(response.code == 200){
                $("#comment_section").hide();
            }else{
                alert(response.msg)
            }
        },
        //数据类型
        dataType: "json"
    });
    history.go(0)
}
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse")
        e.classList.remove("active")
    }else{
        comments.addClass("in");
        e.setAttribute("data-collapse","in");
        e.classList.add("active")
    }
}