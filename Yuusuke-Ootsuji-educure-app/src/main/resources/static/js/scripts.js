document.addEventListener('DOMContentLoaded', function() {
    const productList = document.getElementById('product-list');
    if (productList) {
        const productItems = productList.getElementsByClassName('product-item');
        const totalProducts = productItems.length;
        const itemsPerRow = 5;
        const remainder = totalProducts % itemsPerRow;

        if (remainder !== 0) {
            const spacersNeeded = itemsPerRow - remainder;

            for (let i = 0; i < spacersNeeded; i++) {
                const spacer = document.createElement('div');
                spacer.className = 'spacer';
                productList.appendChild(spacer);
            }
        }
    }
});

window.onload = function() {
    const classNames = ['price', 'total-amount'];
    classNames.forEach(function(className) {
        document.querySelectorAll(`.${className}`).forEach(function(element) {
            let textContent = element.textContent.trim();
            let formattedText = textContent.replace(/(\.\d+)?$/, '');
            element.textContent = formattedText;
        });
    });
};
