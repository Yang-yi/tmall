<%--
  Created by IntelliJ IDEA.
  User: longy
  Date: 5/22/2018
  Time: 2:31 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>

<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap/3.3.6/bootstrap.min.css">
    <link rel="stylesheet" href="css/back/style.css">

    <script>
        function checkEmpty(id,name){
            var value = $("#"+id).val();
            if (value.length == 0){
                alert(name+"不能为空");
                $("#"+id)[0].focus();
                return false;
            }
            return true;
        }

        function checkNumber(id, name){
            var value = $("#"+id).val();
            if (value.length == 0){
                alert(name+"不能为空");
                $("#"+id)[0].focus();
                return false;
            }
            if (isNaN(value)){
                alert(name+"必须是数字");
                $("#"+id)[0].focus();
                return false;
            }
            return true;
        }

        function checkInt(id, name){
            var value = $("#"+id).val();
            if (value.length == 0){
                alert(name+"不能为空");
                $("#"+id)[0].focus();
                return false;
            }
            if (parseInt(value) != value){
                alert(name+"必须是整数");
                $("#"+id)[0].focus();
                return false;
            }
            return true;
        }

        $(function(){
            $("a").click(function(){
                var deleteLink = $("a").attr("deleteLink");
                if (deleteLink == "true"){
                    var confirm = confirm("确认要删除？");
                    if (confirm)
                            return true;
                    return false;
                }
            });
        });
    </script>
</head>
<body>


