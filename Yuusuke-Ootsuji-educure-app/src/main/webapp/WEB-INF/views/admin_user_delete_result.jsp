<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー削除完了画面</title>
    <link rel="stylesheet" href="css/admin_user_delete_result.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
       <main>
		   <p>ユーザーを削除しました。</p>
           <a href="${pageContext.request.contextPath}/admin_dashboard">管理者ダッシュボードに戻る</a>
       </main>
    </div>
</body>
</html>