<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<script src="/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="/js/webSocket/sockjs.min.js" type="text/javascript"
	charset="utf-8"></script>
<script src="/js/webSocket/stomp.js" type="text/javascript"
	charset="utf-8"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>
<script type="text/javascript">
<!-- 定义stomp客户端 -->
	var stompClient = null;
	function connect() {
		var socket = new SockJS('/endpointSang');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected:' + frame);
			stompClient.subscribe('/topic/getResponse', function(response) {
				var message = JSON.parse(response.body).message;
				//获得数据之后的处理
				alert(message);
			})
		});
	}
	connect();
	window.onclose = disconnect();
	function disconnect() {
		if (stompClient !== null) {
			stompClient.disconnect();
		}
		console.log('Disconnected');
	}
</script>
<title>写文章-世界钥匙</title>
<style type="text/css">
body {
	overflow-y: hidden;
}

input {
	outline: none;
}

* {
	padding: 0;
	margin: 0;
}

ul li {
	list-style: none;
}

.fullHeight {
	height: 100%;
}

#left {
	overflow-y: auto;
}

#right {
	color: rgb(47, 47, 47);
	margin: 0;
}

.back {
	color: rgb(236, 114, 89);
	border: 1px solid rgb(236, 114, 89);
	border-radius: 20px;
	line-height: 30px;
	margin: 10px 0;
}

.writing-item {
	position: relative;
	border-top: 1px solid rgb(220, 220, 220);
	border-right: 1px solid rgb(220, 220, 220);
	border-left: 4px solid #FFF;
	padding-top: 15px;
	padding-bottom: 15px;
	cursor: pointer;
	overflow: hidden;
}

.writing-item-click {
	border-left-color: rgb(236, 114, 89);
	background-color: rgb(236, 236, 236);
}

.writing-item:hover {
	border-left-color: rgb(236, 114, 89);
	background-color: rgb(236, 236, 236);
}

.writing-item-img {
	height: 60px;
}

.writing-item-setting {
	position: relative;
}

.writing-item-setting-item {
	position: absolute;
	z-index: 1000;
	/*top: -10px;*/
	width: 80px;
	display: none;
}

#rightUpdate {
	border: 0;
}
</style>
</head>

<body style="width: 97%; margin: 0 auto;">

	<div class="row fullHeight">
		<div class="col-md-4 fullHeight" id="left">
			<div class="row">
				<a href="/users/manage"
					class="col-sm-5 btn btn-default text-center back"> 回首页 </a> <span
					class="col-sm-2"></span>
				<sf:form cssClass="form-inline text-center" id="addform"
					method="post" action="/users/information/emptyFrame"
					modelAttribute="information">
					<input type="hidden" name="checked" value="2">
					<input type="hidden" name="title" value="">
					<!-- <button type="submit" class="col-sm-5 btn btn-default text-center back" data-toggle="modal"
                    data-target="#myModal"> -->
					<!-- <button type="submit"
						class="col-sm-5 btn btn-default text-center back">新建文章</button>  -->
					<!-- </button> -->
				</sf:form>
			</div>
			<%--@elvariable id="informationList" type="java.util.List"--%>
			<c:forEach items="${informationList}" var="item" varStatus="vs">
				<ul>
					<li class=" row writing-item" data-id="${item.id}"
						id="item${item.id}"><img class="col-md-3 writing-item-img"
						id="titleImg${item.id}" src='${item.titleImg.split(",")[0]}' />
						<div class="col-md-7 writing-item-title">
							<span class="h4" id="title${item.id}">${item.title}</span><br />
						</div>
						<div class="col-md-2 writing-item-setting">
							<!-- 4.23关闭设置 留发布删除
                        <ul class="writing-item-setting-item">
                            <li data-id="${item.id}" class="release">   -->
							<!-- <button data-id="${item.id}"
								class="btn btn-default btn-sm issuance" data-toggle="modal"
								data-target="#myModal">发布</button> -->
							<!-- <button class="btn btn-default btn-sm issuance" data-id="${item.id}">发布</button> -->
							<!--  </li>
                            <li data-id="${item.id}" class="delete"> -->
							<button class="btn btn-sm btn-default del" data-id="${item.id}">删除</button>
							<!--   </li>
                        </ul> -->
						</div></li>
			</c:forEach>
			</ul>
		</div>
		<div class=" col-md-8 fullHeight" id="right">
			<c:if test="${informationList.size()!=0}">
				<iframe id="rightUpdate" width="100%" height="80%"
					src="/users/information/update/${informationList[0].id}"></iframe>
				<div class="row">
					<div class="text-left col-sm-6 col-lg-3">
						<br />
						<button class="btn btn-sm btn-info glyphicon glyphicon-search"
							data-toggle="modal" data-target="#myModal"
							value="${informationList[0].id}">选择分类</button>

						${oneType} ${twoType}
						<c:if test="${threeType!=null}">  ${threeType}</c:if>
					</div>
					<div class="text-right col-sm-6 col-lg-6">
						<br />
						<button class="btn btn-warning btn-sm issuance"
							data-id="${informationList[0].id}">发布</button>
					</div>
				</div>
			</c:if>

		</div>
	</div>
	<!-- 新建文章弹出层 -->
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
					<h4 class="modal-title" id="myModalLabel">Modal title</h4>
				</div>
				<div class="modal-body">
					<%--@elvariable id="information" type="com.worldkey.entity.InformationAll"--%>
					<sf:form cssClass="form-inline text-center" id="addform"
						method="post" action="/users/information/updateType"
						modelAttribute="information">
						<input type="hidden" name="id" value="${informationList[0].id}">
						<input type="hidden" name="title" value="aaa">
						<div class="form-group">
							<label for="oneType">类别：</label> <select class="form-control"
								id="oneType" title="一级分类">
								<%--@elvariable id="oneTypes" type="java.util.List"--%>
								<c:forEach items="${oneTypes}" var="item" varStatus="c">
									<c:if test="${item.id!=33&&item.id!=34&&item.id!=27 }">
										<option value="${item.id}">${item.typeName}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<%--@elvariable id="twoTypes" type="java.util.List"--%>
							<sf:select cssClass="form-control" path="" id="twoType"
								items="${twoTypes}" itemLabel="typeName" itemValue="id">
							</sf:select>
						</div>


						<!-- 4.13 薛秉臣添加 三级框  -->
						<div class="form-group">
							<%--@elvariable id="threeTypes" type="java.util.List"--%>
							<sf:select cssClass="form-control" path="type" id="threeType"
								items="${threeTypes}" itemLabel="typeName" itemValue="id">
							</sf:select>
						</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="submit" class="btn btn-primary">确定</button>
				</div>
			</div>
		</div>
	</div>
	</sf:form>


	<script type="text/javascript">
		var height = window.innerHeight;
		$('.fullHeight').css('height', height - 20);

		$(function() {
			//发布按钮点击事件
			$('.issuance').click(function() {
				if (window.confirm("确认发布？")) {
					var id = $(this).data("id");
					$.post('/users/information/issuance', {
						id : id
					}, function(data) {
						if (data.code === 200) {
							$('#item' + id).remove();
							location.reload();
						} else {
							alert(data.result);
						}
					});
				}
			});

			//删除文章事件
			$('.del').click(function() {
				if (window.confirm("删除？")) {
					var id = $(this).data("id");
					$.post("/users/information/del", {
						id : id
					}, function(data) {
						var code = data.code;
						if (code === 200) {
							$("#item" + id).remove();
							location.reload();
						} else {
							alert(data.result);
						}
					});
				}
			});

			$('.writing-item').click(
					function() {
						$('.writing-item-setting-item').slideUp();
						$('.writing-item').removeClass('writing-item-click');
						$(this).addClass('writing-item-click');
						var id = $(this).data('id');
						$('#rightUpdate').attr('src',
								'/users/information/update/' + id);
					});

			$('.writing-item-setting').mouseover(
					function() {
						/*event.stopPropagation();*/
						$(this).children('.writing-item-setting-item').css(
								'display', 'block')

					});

			$('.writing-item-setting').mouseout(
					function() {
						/*event.stopPropagation();*/
						$(this).children('.writing-item-setting-item').css(
								'display', 'none')
					});

			$(window).resize(function() {
				var height = window.innerHeight;
				$('.fullHeight').css('height', height - 20);
			});

			/*
			$("#oneType").change(function () {
			    $.post("/type/two/" + $(this).val(), function (date) {
			        var two = "";
			        for (var i = 0; i < date.result.length; i++) {
			            two += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
			        }
			        $("#type").html(two);
			    });
			})
			 */
			/*4.14薛秉臣 修改 三级联动  */
			$("#oneType")
					.change(
							function() {

								$
										.post(
												"/type/two/" + $(this).val(),
												function(date) {
													var two = "";
													for (var i = 0; i < date.result.length; i++) {
														two += "<option value=" + date.result[i].id + " class=twoType" + ">"
																+ date.result[i].typeName
																+ "</option>";
													}
													$("#twoType").html(two);
													$
															.post(
																	"/type/three/"
																			+ $(
																					".twoType")
																					.val(),
																	function(
																			date) {
																		var three = "";
																		for (var j = 0; j < date.result.length; j++) {
																			three += "<option value=" + date.result[j].id + " class=threeType" + ">"
																					+ date.result[j].typeName
																					+ "</option>";
																		}
																		$(
																				"#threeType")
																				.html(
																						three);
																	});

												});
							});
			$("#twoType")
					.change(
							function() {
								$
										.post(
												"/type/three/" + $(this).val(),
												function(date) {
													var three = "";
													for (var i = 0; i < date.result.length; i++) {
														three += "<option value=" + date.result[i].id + " class=threeType" + ">"
																+ date.result[i].typeName
																+ "</option>";
													}
													$("#threeType").html(three);
												});
							})

		})
	</script>
</body>
</html>
