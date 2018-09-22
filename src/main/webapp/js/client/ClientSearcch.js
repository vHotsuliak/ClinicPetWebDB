function submitCheck() {
    if (document.getElementsByName("clientName").length === 0 && document.getElementsByName("petName").length === 0 && document.getElementsByName("petName").length === 0) {
        document.body.innerHTML =
            '<div style="background-color: yellowgreen">Need to fill at least one field</div>';
    }
}