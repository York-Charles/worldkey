<%--@elvariable id="info" type="com.worldkey.entity.InformationAll"--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>

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
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/js/jquery-3.1.1.min.js" type="text/javascript"></script>
    <script src="/js/webSocket/sockjs.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/webSocket/stomp.js" type="text/javascript" charset="utf-8"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
<script src="js/jquery-3.1.1.min.js"></script>
<style type="text/css">
button:focus, input[type=submit]:focus {
	outline: none;
}

body {
	overflow-y: hidden;
}

button::-moz-focus-inner {
	outline: 0;
}

a {
	outline: none;
}

.w-e-toolbar {
	flex-wrap: wrap;
	-webkit-box-lines: multiple;
}

.w-e-toolbar .w-e-menu:hover {
	z-index: 10002 !important;
}

.w-e-menu a {
	text-decoration: none;
}

.fullscreen-editor {
	position: fixed !important;
	width: 100% !important;
	height: 81% !important;
	left: 0 !important;
	top: 100px !important;
	background-color: white;
	z-index: 9999;
}

.fullscreen-editor .w-e-text-container {
	width: 100% !important;
	height: 95% !important;
}
</style>
</head>
<body style="margin-top: 5px;">
	<sf:form enctype="multipart/form-data" method="post"
		cssClass="form-inline text-center" action="users/information/update"
		modelAttribute="info">
		<%--@elvariable id="twoType" type="com.worldkey.entity.TwoType"--%>
		<%--@elvariable id="oneType" type="com.worldkey.entity.OneType"--%>
		<div class="form-group" style="height: 45px">
			<label for="title">标题:</label>
			<sf:input path="title" id="title" cssClass="form-control"
				style="width:300px;" />
			
			<!--  <span>类别:${oneType.typeName}-${twoType.typeName}</span>   -->
			<%-- <button type="button" class="btn btn-primary btn-sm" id="tImg">修改封面图</button>--%>
		</div>
		<div>
		<label for="abstracte">摘要:</label>
			<sf:input cssClass="form-control" path="abstracte" id="abstracte"
				style="width:800px;" />
		</div>

		<span id="saveState" style="position: absolute; right: 10px;"></span>

		<div id="editor" class="fullscreen-editor">${info.info}</div>
	</sf:form>


	<!--引入jquery和wangEditor.js-->
	<!--注意：javascript必须放在body最后，否则可能会出现问题-->
	<script type="text/javascript"
		src="https://unpkg.com/wangeditor@3.0.15/release/wangEditor.min.js"></script>
	<script type="text/javascript">

    function clearWordStyle(html) {
        /* var html = $('body').html();*/
        html = html.replace(/<\/?SPAN[^>]*>/gi, "");
        html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3");
        html = html.replace(/<(\w[^>]*) style="([^"]*)"([^>]*)/gi, "<$1$3");
        html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3");
        html = html.replace(/<\\?\?xml[^>]*>/gi, "");
        html = html.replace(/<\/?\w+:[^>]*>/gi, "");
        html = html.replace(/&nbsp;/, " ");
        var re = new RegExp("(<P)([^>]*>.*?)(<\/P>)", "gi");
        html = html.replace(re, "<div$2</div>");
        /*去除<pre>强制格式标签*/
        html = html.replace(/<pre>/g, "");
        html = html.replace(/&lt;pre&gt;/g, "");
        html = html.replace(/<pre>/g, "");
        html = html.replace(/&lt;\/pre&gt;/g, "");
        return html;
    }

    window.wangEditor.fullscreen = {
        // editor create之后调用
        init: function (editorSelector) {
            $(editorSelector + " .w-e-toolbar").append('<div class="w-e-menu"><a class="_wangEditor_btn_fullscreen"  onclick="window.wangEditor.fullscreen.toggleFullscreen(\'' + editorSelector + '\')">退出全屏</a></div>');
        },
        toggleFullscreen: function (editorSelector) {
            $(editorSelector).toggleClass('fullscreen-editor');
            if ($(editorSelector + ' ._wangEditor_btn_fullscreen').text() === '全屏') {
                $(editorSelector + ' ._wangEditor_btn_fullscreen').text('退出全屏');
            } else {
                $(editorSelector + ' ._wangEditor_btn_fullscreen').text('全屏');
            }
        }
    };
    //文章ID
    var id = "${info.id}";
    //更新地址
    var Url = '/users/information/ajaxUpdate';
    $(function () {
        var E = window.wangEditor;
        var editor = new E('#editor');
        editor.customConfig.uploadImgServer = '/upload2';//图片上传接口
        editor.customConfig.printLog = false;
        editor.customConfig.uploadFileName = 'file';
        editor.customConfig.debug = false;
        editor.customConfig.uploadImgMaxSize = 20 * 1024 * 1024;
        editor.customConfig.uploadImgMaxLength = 1;
        editor.customConfig.menus = [
            'head',  // 标题
            'bold',  // 粗体
            'italic',  // 斜体
            'underline',  // 下划线
            'strikeThrough',  // 删除线
            'foreColor',  // 文字颜色
            'backColor',  // 背景颜色
            'link',  // 插入链接
            'list',  // 列表
            'justify',  // 对齐方式
            'quote',  // 引用
            'emoticon',  // 表情
            'image',  // 插入图片
           /* 'table',  // 表格*/
           /* 'video',  // 插入视频*/
           /* 'code',  // 插入代码*/
            'undo',  // 撤销
            'redo'  // 重复
        ];
// 自定义 onchange 触发的延迟时间，默认为 200 ms
        editor.customConfig.onchangeTimeout = 1500;/// 单位 ms

//内容改变事件
        editor.customConfig.onchange = function (html) {
            infoChange(clearWordStyle(html));
        };
        editor.create();
        E.fullscreen.init('#editor');


        var oldTitleImg = getTitleImg();
        function infoChange(html) {




            //标题图片
            var titleImg = getTitleImg();
            $.ajax({
                url: Url,
                type: 'POST',
                data: {info: html, id: id, titleImg: titleImg, oldTitleImg: oldTitleImg},
                beforeSend: function () {
                    $('#saveState').text("正在保存...")
                },
                success: function () {
                    $('#saveState').text("已保存到草稿箱");
                    oldTitleImg = titleImg;
                },
                error: function () {
                    alert("保存失败");
                }
            })
        }

        function getTitleImg() {
            //标题图片
            var $imgs = $('#editor').find('img');
            var titleImg = [];
            for (var i = 0; i < $imgs.length; i++) {
                titleImg[i] = $imgs.eq(i).attr('src');
            }
            return titleImg.toString();
        }


//标题改变事件
        $('#title').change(function () {
            var title = $(this).val();
            $.ajax({
                url: Url,
                type: 'POST',
                data: {title: title, id: id},
                beforeSend: function () {
                    $('#saveState').text("正在保存...")
                },
                success: function (responseStr) {
                    $('#title' + id, parent.document).text(responseStr);
                    $('#saveState').text("已保存到草稿箱");
                },
                error: function () {
                    $('#saveState').text("保存失败");
                }
            })
        });


//摘要改变事件
        $('#abstracte').change(function () {
            var abstracte = $(this).val();
            var formData = new FormData();
            formData.append("abstracte", abstracte);
            formData.append("id", id);
            $.ajax({
                url: Url,
                type: 'POST',
                data: {abstracte: abstracte, id: id},
                beforeSend: function () {
                    $('#saveState').text("正在保存...")
                },
                success: function () {
                    $('#saveState').text("已保存到草稿箱");
                },
                error: function () {
                    $('#saveState').text("保存失败");
                }
            })
        });

//标题图片改变事件
        $('#file').change(function () {
            var formData = new FormData();
            formData.append("file", $('#file')[0].files[0]);
            formData.append("id", id);

            $.ajax({
                url: Url,
                type: 'POST',
                data: formData,
                // 告诉jQuery不要去处理发送的数据
                processData: false,
                // 告诉jQuery不要去设置Content-Type请求头
                contentType: false,
                beforeSend: function () {
                    $('#saveState').text("正在保存...");
                },
                success: function (responseStr) {
                    $('#titleImg' + id, parent.document).attr('src', responseStr);
                    $('#saveState').text("已保存到草稿箱");
                },
                error: function () {
                    $('#saveState').text("保存失败");
                }
            })
        });
    });
</script>
</body>
</html>