function toggleAddress() {
    var registeredAddress = document.getElementById('registered-address');
    var newAddress = document.getElementById('new-address');
    var addressType = document.querySelector('input[name="address"]:checked').value;

    if (addressType === 'registered') {
        registeredAddress.style.display = 'block';
        newAddress.style.display = 'none';
    } else {
        registeredAddress.style.display = 'none';
        newAddress.style.display = 'block';
    }
}

window.onload = function() {
    toggleAddress();
};
