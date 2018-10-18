<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <!--这里引用wangEditor.min.css 设置富文本的布局-->
    <link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!--导入文件域控件JS-->
    <script src="js/fileupload/fileinput.min.js" type="text/javascript"></script>
    <!--导入文件域控件本地化JS-->
    <script src="js/fileupload/locales/zh.js" type="text/javascript"></script>
    <!--导入文件域控件css文件-->
    <link href="js/fileupload/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
    <title>资讯添加</title>
</head>
<body style="padding-top: 5px;">
<%--@elvariable id="information" type="com.worldkey.entity.InformationAll"--%>
<sf:form cssClass="form-inline text-center" enctype="multipart/form-data" id="addform" method="post" action="users/information/add"
         modelAttribute="information">
    <div class="form-group">
        <label for="title">标题：</label>
        <sf:input path="title" id="title" cssClass="form-control"/>
        <sf:errors path="title" cssStyle="color:red"/>
    </div>
    <div class="form-group">
        <label for="onetype">类别：</label>
        <sf:select cssClass="form-control" path="onetype" id="onetype">
            <sf:options items="${onetypes}" itemLabel="typeName" itemValue="id"/>
        </sf:select>
    </div>
    <div class="form-group">
        <sf:select cssClass="form-control" path="type" id="type" items="${twotypes}" itemLabel="typeName"
                   itemValue="id"> </sf:select>
    </div>
    <div class="form-group">
        <label>摘要：</label><sf:input path="abstracte" cssClass="form-control"/>
        <sf:errors path="abstracte" cssStyle="color:red"/>
    </div>
    <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">
 封面图
</button>
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
       					 <input type="file" class="form-control" id="file" name="file"/>
   					</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">发布</button>
						<!-- <button type="button" class="btn btn-primary">保存</button> -->
					</div>
				</div>
			</div>
		</div>
   <div style="margin: 0;padding: 0;display: inline" id="saveCGH">
   </div>
    <button type="submit" class="glyphicon btn btn-sm glyphicon-floppy-disk btn-warning">发布</button>
    <a type="button" id="saveCG" class="glyphicon btn btn-sm glyphicon-floppy-disk btn-info">保存到草稿箱</a>
    <hr style="margin: 1.5px;padding: 0;height: 1px;border: none; background-color: #ffffff;">
    <sf:textarea path="info" id="textarea1" rows="20" cols="20"/>
</sf:form>





<!--引入wangEditor.js-->
<!--注意：javascript必须放在body最后，否则可能会出现问题-->
<script type="text/javascript" src="js/wangEditor.js"></script>
<!--这里引用editor.js 创建富文本框，textarea的id必须为textarea1-->
<script type="text/javascript" src="js/editor.js"></script>
<script type="text/javascript">
    $("#file").fileinput({
        language: 'zh',
        showUpload: false,
        showCaption: false,
        browseClass: "btn btn-warning btn-sm",
        fileType: "any",
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
    });
    $(function () {

        $("#saveCG").click(function(){
            $("#saveCGH").html("<input type='hidden' name='checked' value='2'>");
            $("form").submit();
        });


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
</script>
</body>
</html>