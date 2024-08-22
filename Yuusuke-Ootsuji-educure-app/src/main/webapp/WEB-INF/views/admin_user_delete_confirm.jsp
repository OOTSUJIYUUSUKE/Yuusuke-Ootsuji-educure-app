<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー削除確認画面</title>
    <link rel="stylesheet" href="css/admin_user_delete_confirm.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <a href="${pageContext.request.contextPath}/admin_user_detail?userId=${user.userId}" class="back-link">&lt; 戻る</a>
       <main>
		   <p>このユーザーを削除します。</p>
		   <form:form action="${pageContext.request.contextPath}/admin_user_delete_confirm"  method="post" modelAttribute="user" class="user-form">
               <form:input type="hidden" path="userId"/>
               <button type="submit" class="btn">削除</button>
           </form:form>
       </main>
    </div>
</body>
</html>