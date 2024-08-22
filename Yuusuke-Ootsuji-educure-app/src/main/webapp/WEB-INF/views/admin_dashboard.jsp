<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 管理者ダッシュボード画面</title>
    <link rel="stylesheet" href="css/admin_dashboard.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/logout">ログアウト</a>
        </header>
        <main>
            <h1 class="title">管理者ダッシュボード</h1>
            <nav class="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin_user_management" class="menu-link">ユーザー管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin_product_management" class="menu-link">商品管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin_order_management" class="menu-link">注文管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin_contact_management" class="menu-link">サポート管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin_register" class="menu-link">管理者新規作成</a></li>
                </ul>
            </nav>
        </main>
    </div>
<body>

</body>
</html>