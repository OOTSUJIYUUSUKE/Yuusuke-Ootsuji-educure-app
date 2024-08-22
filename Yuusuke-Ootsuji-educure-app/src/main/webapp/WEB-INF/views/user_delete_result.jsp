<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. サービス退会完了画面</title>
    <link rel="stylesheet" href="css/user_delete_result.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <main>
           <p>退会が完了しました。3秒後にトップ画面に戻ります。</p>
        </main>
    </div>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="js/redirect.js"></script>
</body>
</html>