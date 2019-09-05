<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2017/9/25
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8"%>
<%--<html>--%>

<!-- 分页导航 -->
<nav aria-label="Page navigation" class="text-center">
    <ul class="pagination">
        <c:if test="${!pageinfo.isFirstPage}">
         <li>
                <a class="page " id="1" aria-label="Previous">
                    <span>首页</span> </a></li>
            <li>
                <a class="page " id="${pageinfo.prePage}" aria-label="Previous">
                    <span>上一页</span> </a></li>
        </c:if>

        <%--@elvariable id="pageinfo" type="com.github.pagehelper.PageInfo"--%>
        <c:forEach begin="${pageinfo.pages<=10?1:(pageinfo.pageNum-5<1?1:(pageinfo.pageNum-5>pageinfo.pages-10?pageinfo.pages-10:pageinfo.pageNum-5))}"
                   end="${pageinfo.pages<=10?pageinfo.pages:(pageinfo.pageNum+4>pageinfo.pages?pageinfo.pages:(pageinfo.pageNum+4<10?10:pageinfo.pageNum+4))}"
                   var="i">
            <c:if test="${pageinfo.pageNum==i }">
                <li class="active">
                    <span class="page" id="${i}">${i}</span></li>
            </c:if>
            <c:if test="${pageinfo.pageNum!=i }">
                <li>
                    <a class="page" id="${i}"><span> ${i}</span></a></li>
            </c:if>
        </c:forEach>

        <c:if test="${!pageinfo.isLastPage}">
            <li>
                <a class="page" id="${pageinfo.nextPage}" aria-label="Next">
                    <span>下一页</span>
                </a></li>
                 <li>
                <a class="page" id="${pageinfo.pages}" aria-label="Next">
                    <span>尾页</span>
                </a></li>
        </c:if>

    </ul>
</nav>
<script type="text/javascript">
        $('.page').css('cursor','pointer');
</script>
<!-- 分页导航结束 -->
<%--</html>--%>
