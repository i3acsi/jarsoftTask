function getJsonOfFormdata() {
    let id = chkId($('#bId').val())
    let name = $('#bName').val()
    let price = $('#bPrice').val()
    let category = $('#bCategory option:selected').val()
    let content = $('#bContent').val()
    let json = JSON.stringify({'id': id, 'name': name, 'price': price, 'category' : category, 'content': content})
    if (chkName(name)&&chkPrice(price)&&chkContent(content))
        return json
}

function chkId(value) {
    if (value) {
        return value
    } else {
        return 0;
    }
}


function chkName(value) {
    if (value.length > 3 && /[A-zА-яЁё]/.test(value)) {
        return true
    } else {
        errorT("Имя должно быть длиннее 3 символов, содержать только буквы.")
        return false
    }
}

function chkPrice(value) {
    if (/^(0|[1-9]\d*)(\.[0-9])?([0-9])?$/.test(value)) {
        return true
    } else {
        errorT(" цена ложна быть дробным числом")
        return false
    }
}


function chkContent(value) {
    if (255> value.length && value.length > 3) {
        return true
    } else {
        errorT("Длинна содержания должна быть не более 255 и не менее 3.")
        return false
    }
}

