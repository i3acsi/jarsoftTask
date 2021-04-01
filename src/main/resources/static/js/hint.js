function errorT(text) {
    hint.text(text)
    header.css("background-color", "crimson");
}

function clearHint() {
    hint.text('')
    header.css("background-color", "");
}

function errorOnCreate(err) {
    errorOnAction('create', err)
}

function errorOnUpdate(err) {
    errorOnAction('update', err)
}

function errorOnDelete(err) {
    errorOnAction('delete', err)
}

function errorOnAction(action, err) {
    if (err)
        errorT('error on ' + action + '\n' + err['responseText'])
    else
        errorT('error on ' + action)
}