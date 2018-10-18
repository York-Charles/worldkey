<%--@elvariable id="createTime" type="java.lang.String"--%>
<%--@elvariable id="info" type="com.worldkey.entity.InformationAll"--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title></title>
    <style type="text/css">
	    div{
	    margin:10px 0px 0px 0px;
	    }

        img {
            width: 100%;
            height: auto;
        }

        p {
            /*  color: rgb(102, 102, 102);*/
            /* outline-color: rgb(102, 102, 102);*/
            /* font-weight: 500;*/
            line-height: 1.5;
            word-break: break-all;
            /*  font-size: 1.13rem;margin: 10px .3rem;*/
        }

        body {
            padding-left: 10px;
            padding-right: 10px;
            font-size: 1rem;
            /*margin: 0 auto;*/
            color: #000000;
        }

        #title {
            font-size: 1.3rem;
            color: #000000;
            outline-color: #000000;
            font-weight: 500;
            text-overflow: ellipsis;
            text-align: left;
            margin: .3rem 0.1rem;
        }

        #headImg {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            float: left;
        }

        #petName {
            padding-left: 15px;
            float: left;
            display: table-cell;
            line-height: 50px;
            color: #000;
            font-size: 14px;
        }
        #follow{
            float: right;
        }
        
    </style>
</head>
<body>
<div style=" height: 60px;">
    <img id="headImg" src="${info.users.headImg}">  
    <span id="petName"> ${info.users.petName}  
     ${createTime}
    </span>
   
   <%-- <button id="follow">关注</button>--%>
</div>
<div style=" height: 30px; font-size: 15px;line-height:1.5;display:inline-block;">${info.title}</div>
<br></br>
<div style=" height: 40px; font-size: 14px;line-height:2.2;display:inline-block;">${info.info}</div>



</body>
</html>