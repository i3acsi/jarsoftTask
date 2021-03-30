function errorT(text) {
    let hint = $('#hint')
    hint.val(text)
    let header = $('#hintH')
    header.css("background-color", "crimson");
}