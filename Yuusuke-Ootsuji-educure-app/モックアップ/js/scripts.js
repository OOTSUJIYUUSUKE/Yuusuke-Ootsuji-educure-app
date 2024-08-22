document.addEventListener('DOMContentLoaded', function() {
    const productList = document.getElementById('product-list');
    const productItems = productList.getElementsByClassName('product-item');
    const totalProducts = productItems.length;
    const itemsPerRow = 5;
    const remainder = totalProducts % itemsPerRow;

    if (remainder !== 0) {
        const spacersNeeded = itemsPerRow - remainder;

        // スペーサーを追加する
        for (let i = 0; i < spacersNeeded; i++) {
            const spacer = document.createElement('div');
            spacer.className = 'spacer';
            productList.appendChild(spacer);
        }
    }
});
