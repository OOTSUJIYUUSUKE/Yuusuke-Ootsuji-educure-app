document.addEventListener('DOMContentLoaded', () => {
    const dropArea = document.getElementById('drop-area');
    const fileInput = document.getElementById('product-images');
    const imagePreview = document.getElementById('image-preview');

    // Prevent default drag behaviors
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // Highlight drop area when dragging over it
    ['dragenter', 'dragover'].forEach(eventName => {
        dropArea.addEventListener(eventName, () => {
            dropArea.classList.add('highlight');
        }, false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, () => {
            dropArea.classList.remove('highlight');
        }, false);
    });

    // Handle dropped files
    dropArea.addEventListener('drop', handleDrop, false);
    dropArea.addEventListener('click', () => fileInput.click(), false);
    fileInput.addEventListener('change', handleFiles, false);

    function handleDrop(e) {
        let dt = e.dataTransfer;
        let files = dt.files;
        handleFiles({ target: { files } });
    }

    function handleFiles(e) {
        let files = e.target.files;
        if (files.length + imagePreview.children.length > 5) {
            alert('5枚までの画像を選択できます。');
            return;
        }
        Array.from(files).forEach(file => {
            if (!file.type.startsWith('image/')) return; // Skip non-image files
            let img = document.createElement('img');
            img.file = file;
            img.style.width = '100px'; // Thumbnail size
            img.style.height = '100px'; // Thumbnail size
            imagePreview.appendChild(img);
            let reader = new FileReader();
            reader.onload = ((aImg) => {
                return (e) => {
                    aImg.src = e.target.result;
                };
            })(img);
            reader.readAsDataURL(file);
        });
    }
});

