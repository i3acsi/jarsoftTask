function chkId(value) {
    if (value) {
        return value
    } else {
        return 0;
    }
}

function chkCategoryName(value) {
    if (value.length > 3) {
        return true
    } else {
        errorT("Имя должно быть длиннее 3 символов")
        return false
    }
}

function chkCategoryReq(value) {
    if (value.length > 3) {
        return true
    } else {
        errorT("Request ID должно быть длиннее 3 символов")
        return false
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

