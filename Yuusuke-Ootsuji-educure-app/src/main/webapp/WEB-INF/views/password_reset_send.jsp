<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. パスワードリセット確認画面</title>
    <link rel="stylesheet" href="css/password_reset_send.css">
</head>
<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <main>
            <h1>パスワードリセットの確認</h1>
            <p>パスワードリセットのリンクを登録されたメールアドレスに送信しました。<br>
            メールに記載されたリンクからパスワードをリセットしてください。</p>
            <a href="${pageContext.request.contextPath}/user_login" class="btn">ログイン画面に戻る</a>
        </main>
    </div>
</body>

</html>
