function showImage(imageElement) {
    // メイン画像の要素を取得
    var mainImage = document.getElementById('main-image');
    // クリックされたサムネイル画像のURLを取得
    var newSrc = imageElement.src;
    // メイン画像のsrcを更新
    mainImage.src = newSrc;
}

// 初期状態で1枚目の画像を表示
window.onload = function() {
    var thumbnails = document.querySelectorAll('.thumbnail');
    if (thumbnails.length > 0) {
        document.getElementById('main-image').src = thumbnails[0].src;
    }
};
