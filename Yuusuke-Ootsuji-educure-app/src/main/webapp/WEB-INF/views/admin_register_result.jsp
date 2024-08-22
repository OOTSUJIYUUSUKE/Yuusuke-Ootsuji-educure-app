<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 管理者登録完了画面</title>
    <link rel="stylesheet" href="css/admin_register_result.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
       <main>
		   <p>管理者を新規登録しました。</p>
           <a href="${pageContext.request.contextPath}/admin_dashboard">管理者ダッシュボードに戻る</a>
       </main>
    </div>
</body>
</html>