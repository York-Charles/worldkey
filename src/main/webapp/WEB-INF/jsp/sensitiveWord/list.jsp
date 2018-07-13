<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: bubai_li
  Date: 2017/11/14
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>敏感词管理</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/js/jquery-3.1.1.min.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <style type="text/css">
        .page {
            cursor: pointer;
        }
    </style>
</head>
<body>
<button type="button"
        style="border-radius:50%;position: absolute;z-index:100;right: 50px;top: 30px;font-weight:800;font-size: 50px;"
        class="btn btn-info glyphicon glyphicon-plus" data-toggle="modal"
        data-target=".bs-example-modal-sm"></button>
<div style="margin: 0 auto;position: relative;">
    <table class="table table-hover table-condensed">
        <tr>
            <td>###</td>
            <td>word</td>
            <td>
                <form action="/sensitiveWord/search" method="get">
                    <input class="form-control" placeholder="查询" value="${search}" type="text" id="search" name="search"
                           title="">
                </form>
            </td>
            <td>###</td>
            <td>word</td>
            <td>##</td>
        </tr>
        <%--@elvariable id="words" type="java.util.List"--%>
        <c:forEach items="${words.list}" varStatus="c" var="item">
            <c:if test="${c.count%2!=0}">
                <tr>
            </c:if>
            <td>${c.count}</td>
            <td>${item.word}</td>
            <td>
                <button class="btn btn-danger glyphicon glyphicon-trash del" data-id="${item.id}"></button>
            </td>
            <c:if test="${c.count%2==0}">
                </tr>
            </c:if>
        </c:forEach>
    </table>
    <nav aria-label="Page navigation" class="text-center">
        <ul class="pagination">
            <%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo"--%>
            <c:if test="${!words.isFirstPage}">
                <li>
                    <span class="page " data-id="${words.prePage}" aria-label="Previous">上一页</span></li>
            </c:if>
            <c:forEach items="${words.navigatepageNums}" var="item">
                <c:if test="${item==words.pageNum}">
                    <li class="active">
                        <span class="page" data-id="${item}">${item}</span></li>
                </c:if>
                <c:if test="${item!=words.pageNum }">
                    <li>
                        <span class="page" data-id="${item}">${item}</span></li>
                </c:if>
            </c:forEach>

            <c:if test="${!words.isLastPage}">
                <li>
                    <span class="page" data-id="${words.nextPage}" aria-label="Next">下一页</span>
                </li>
            </c:if>

        </ul>
    </nav>
</div>
<%--添加拟态框--%>
<!-- Small modal -->
<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">敏感词添加</h4>
            </div>
            <form action="/sensitiveWord/add" method="post">
            <div class="modal-body">
                敏感词:<input type="radio" name="isWord" checked="checked"  value="true" title="isWord">
                分隔符:<input type="radio"  name="isWord" value="false" title="isWord"><br>
                <input class="form-control" type="text" name="word" title="word">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="submit" class="btn btn-primary">保存</button>

            </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var list = '/sensitiveWord/showWord';
        //分页点击事件
        $('.page').click(function () {
            var pageNum = $(this).data('id');
            var search = $('#search').val();
            location.href = list + '?pageNum=' + pageNum + '&search=' + search;
        });

        /*//添加方法
        $('.add').click(function () {
            var word = $('#word').val();
            var isWord = $('#isWord').val();
            var isWord2 = $('#isWord2').val();
            alert('isWord:'+isWord+';isWord2:'+isWord2)
           /!* $.post('/sensitiveWord/add', {word: word, isWord: isWord}, function (data) {
                if (data === 1) {
                    location.href = list;
                } else {
                    alert("添加失败,请重试");
                }
            });*!/
        });*/

        //删除方法
        $('.del').click(function () {
            if (window.confirm("删除？")) {
                var $id = $(this).data('id');
                $.post('/sensitiveWord/del', {id: $id}, function (data) {
                    if (data === 1) {
                        window.location.href = list;
                    } else {
                        alert("刪除失败,请重试," + data);
                    }
                })
            }
        });
    })

</script>
</body>
</html>
