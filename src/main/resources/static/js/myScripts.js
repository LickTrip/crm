function showMeeting(meetId) {
    var prev = document.getElementById("showPreviewMeet" + meetId);
    var note = document.getElementById("showNoteMeet" + meetId);
    if (prev.style.display == "none") {
        prev.style.display = "block"
        note.style.display = "none"
    } else {
        prev.style.display = "none"
        note.style.display = "block"
    }
}

function showTask(taskId) {
    var prev = document.getElementById("showPreviewTask" + taskId);
    var note = document.getElementById("showNoteTask" + taskId);
    if (prev.style.display == "none") {
        prev.style.display = "block"
        note.style.display = "none"
    } else {
        prev.style.display = "none"
        note.style.display = "block"
    }
}

function showErrorMatchToast() {
    var showErrorMatchToast = document.getElementById("showErrorMatchToast");
    if (showErrorMatchToast.value == "true") {
        toastr.error('Password has not been changed..', 'Passwords do not match!');
    }
}

function showErrorWPassToast() {
    var showErrorWPassToast = document.getElementById("showErrorWPassToast");
    if (showErrorWPassToast.value == "true") {
        toastr.error('Password has not been changed..', 'Old Passwords is wrong!');
    }
}

