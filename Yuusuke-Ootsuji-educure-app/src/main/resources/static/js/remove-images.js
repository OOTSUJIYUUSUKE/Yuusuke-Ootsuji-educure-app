document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('product-images');
    const previewContainer = document.getElementById('image-preview');
    let filesArray = []; // 選択されたファイルを保持するための配列

    fileInput.addEventListener('change', function(event) {
        const newFiles = Array.from(event.target.files);
        filesArray = filesArray.concat(newFiles);

        previewContainer.innerHTML = '';

        filesArray.forEach((file, index) => {
            const reader = new FileReader();

            reader.onload = function(e) {
                const div = document.createElement('div');
                div.classList.add('image-preview-item');
                
                const img = document.createElement('img');
                img.src = e.target.result;
                img.classList.add('preview-image');

                const removeButton = document.createElement('button');
                removeButton.classList.add('remove-button');
                removeButton.innerText = '×';
                removeButton.addEventListener('click', function(event) {
                    event.stopPropagation();
                    filesArray.splice(index, 1);
                    updateFileInput();
                    div.remove();
                });

                div.appendChild(img);
                div.appendChild(removeButton);
                previewContainer.appendChild(div);
            }

            reader.readAsDataURL(file);
        });

        updateFileInput();
    });

    function updateFileInput() {
        const dt = new DataTransfer();

        filesArray.forEach(file => {
            dt.items.add(file);
        });

        fileInput.files = dt.files;
    }
});
