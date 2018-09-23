function submitCheck() {
    if(document.getElementById('clientName').value === '' && document.getElementById('petName').value === '' && document.getElementById('kindOfPet').value === '') {
        document.getElementById("forResult").innerHTML =
            'Need to fill at least one field';
        event.preventDefault();
    }
}