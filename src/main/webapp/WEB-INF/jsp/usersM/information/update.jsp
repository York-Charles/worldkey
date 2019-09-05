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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>更新数据</title>
    <link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!--导入文件域控件JS-->
   <script src="js/fileupload/fileinput.min.js" type="text/javascript"></script>
    <!--导入文件域控件本地化JS-->
   <script src="js/fileupload/locales/zh.js" type="text/javascript"></script>
    <!--导入文件域控件css文件-->
    <link href="js/fileupload/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
    <style type="text/css">
    button:focus,input[type=submit]:focus{outline:none;}
button::-moz-focus-inner{outline:0;}
a{outline:none;}
    </style>
</head>
<body style="margin-top: 5px;">
<sf:form enctype="multipart/form-data" method="post" cssClass="form-inline text-center"
         action="users/information/update" modelAttribute="info">
    <sf:hidden path="id"/>
    <div class="form-group">
        <span for="title">标题：</span>
        <sf:input path="title" id="title" cssClass="form-control"/>
        <sf:errors path="title" cssClass="error"> </sf:errors>
    </div>
    <div class="form-group">
        <label for="abstracte">摘要：</label>
        <sf:input cssClass="form-control" path="abstracte" id="abstracte"/>
        <sf:errors path="abstracte" cssClass="error"> </sf:errors>
    </div>
    <div class="form-group">
        <label for="onetype">类别：</label>
        <select id="onetype"  name="onetype" class="form-control">
            <c:forEach items="${onetypes}" var="t">
              <c:if test="${t.id!=28 }"> <option value="${t.id}">
                        ${t.typeName}
                </option> 
                 </c:if>
            </c:forEach>
        </select>
    </div>
    <sf:select cssClass="form-control" path="type" id="type" items="${twotypes}" itemLabel="typeName"
               itemValue="id">
    </sf:select>
          <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">封面图</button>
<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">封面图</h4>
					</div>
					<div class="modal-body">
					<div class="form-group file">
					<img style="display: inline-block;" alt="" width="200" height="160" src="${info.titleImg}">
       					 <input type="file" style="display: inline-block;" class="form-control" id="file" name="file"/>
   					</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
						<!-- <button type="button" class="btn btn-primary">保存</button> -->
					</div>
				</div>
			</div>
		</div>
    <button type="submit" class="btn btn-warning">保存</button>
    <hr style="margin: 1.5px;padding: 0px;height: 1px;border: none; background-color: #ffffff;">
    <div id="WE">
        <sf:textarea path="info" id="textarea1" rows="60" cols="30"/>
    </div>
</sf:form>
<!--引入jquery和wangEditor.js-->
<!--注意：javascript必须放在body最后，否则可能会出现问题-->
<script type="text/javascript" src="js/wangEditor.min.js"></script>
<!--这里引用editor.js 创建富文本框，textarea的id必须为textarea1-->
<script type="text/javascript" src="js/editor.js"></script>
<script type="text/javascript">
   /* 文件上传控件样式和属性控制*/
    $("#file").fileinput({
        language: 'zh',
        showUpload: false,
        showCaption: false,
        browseClass: "btn btn-warning btn-sm",
        fileType: "any",
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
    });
    <!--文件上传控件样式和属性控制 end -->
    <!--分类二级联动-->
    $(function () {
        $("#onetype").change(function () {
            var one = $(this).val();
            $.post("<%=basePath%>type/two/" + $(this).val(), function (date) {
                var two = "";
                for (var i = 0; i < date.result.length; i++) {
                    two += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
                }
                $("#type").html(two);
            });
        })
    });
    <!--分类二级联动 end-->
</script>
</body>
</html>