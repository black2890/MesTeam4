document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file before uploading.");
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    fetch('/upload-orders', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Error uploading file: ' + response.statusText);
            }
        })
        .then(data => {
            alert(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
});
