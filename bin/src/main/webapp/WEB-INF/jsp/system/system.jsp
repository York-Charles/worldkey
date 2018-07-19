<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>系统管理</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet"
          href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body>
<%--@elvariable id="system" type="com.worldkey.entity.SystemConfig"--%>
<sf:form modelAttribute="system" action="system/update" method="post"
         enctype="multipart/form-data" class="form-control" target="_top">
    <div class="form-group">
        <label>文件路径</label>
        <sf:input class="form-control" path="filesrc"/>
        <sf:errors path="filesrc"> </sf:errors>
    </div>
    <div class="form-group">
        <label for="defaultHeadimg">默认头像</label>
        <sf:input class="form-control" path="defaultHeadimg"/>
        <sf:errors path="defaultHeadimg"> </sf:errors>
    </div>

    <div class="form-group">
        <button type="button" class="btn btn-primary btn-sm"
                data-toggle="modal" data-target="#myModal">默认头像
        </button>
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
             aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">默认头像</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group file">
                            <input type="file" class="form-control" id="file" name="file"/>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary"
                                data-dismiss="modal">保存
                        </button>
                        <!-- <button type="button" class="btn btn-primary">保存</button> -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="submit" value="保存">
</sf:form>
</body>
</html>