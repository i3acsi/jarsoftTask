let cId
let cName
let cReq

function loadCategoriesC() {
    clearFormC()
    let categories = ''
    for (let category of frontendDataCategories.values()) {
        categories += '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="cat' + category['id'] + '" value="' + category['id'] + '" onclick="applyCategoryToFormC(this.value)">' + category['name'] + '</button>'
    }
    buttonsL.html(categories)
}

function applyCategoryToFormC(id) {
    clearFormC()
    id = parseInt(id)
    let category = frontendDataCategories.get(id)
    cId.val(category['id'])
    cName.val(category['name'])
    cReq.val(category['reqName'])
    rAction.text("Edit Category: #" + category["id"])
}

function clearFormC() {
    cId.val(0)
    cName.val('')
    cReq.val('')
    rAction.text("Create new Category:")
    clearHint()
}

function fillFormsForCategories() {
    clearHint()
    editForm.html(
        '<input type="text" class="form-control" value="0" id="cId" hidden>\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="cName" class="col-sm-2 col-form-label">Name</label>\n' +
        '                        <div class="col">\n' +
        '                            <input type="text" class="form-control" id="cName" name="cName" maxlength="255" required>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="cReq" class="col-sm-2 col-form-label">Request ID</label>\n' +
        '                        <div class="col">\n' +
        '                             <input type="text" class="form-control" id="cReq" name="cReq" maxlength="255" required>\n' +
        '                        </div>\n' +
        '                    </div>\n'
    )
    cId = $('#cId')
    cName = $('#cName')
    cReq = $('#cReq')
    lHeader.text('Categories:')
    rAction.text('Create new Category:')

    createBtn.attr("onclick", "").unbind("click");
    createBtn.on("click", function () {
        clearFormC()
    });

    search.val('')
    search.attr("placeholder", "enter category name");
    search.attr("onkeyup", "").unbind("keyup");
    search.on('keyup', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            searchCategories(search.val())
        }
    });

    saveBtn.attr("onclick", "").unbind("click");
    saveBtn.on("click", function () {
        saveCategory()
    });


    delBtn.attr("onclick", "").unbind("click");
    delBtn.on("click", function () {
        delCategory()
    });

    loadCategoriesC()
}

function searchCategories(input) {
    clearHint()
    if (input && input.length > 0)
        $.get({
            url: origin + '/category/search/' + input
        }).done(function (listCategory) {
            let categories = ''
            for (let i in listCategory) {
                let id = listCategory[i]['id']
                let name = listCategory[i]['name']
                categories += '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="cat' + id + '" value="' + id + '" onclick="applyCategoryToFormC(this.value)">' + name + '</button>'
            }
            buttonsL.html(categories)
        }).fail(function (err) {
            let msg = 'error on search'
            if (err) {
                msg += err
                console.log(err)
            }
            errorT(msg)
        });
    else
        loadCategoriesC()
}

function saveCategory() {
    let id = chkId(parseInt(cId.val()))
    if (id === 0)
        createC()
    else
        updateC(id)

}

function createC() {
    let json = getCategoryJsonFromForm()
    if (json)
        $.post({
            contentType: 'application/json;charset=UTF-8',
            url: origin + '/category/',
            data: json
        }).done(function (category) {
            let id = category["id"]
            frontendDataCategories.set(id, category)
            let categoryBtn = '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="cat' + category['id'] + '" value="' + category['id'] + '" onclick="applyCategoryToFormC(this.value)">' + category['name'] + '</button>'
            buttonsL.append(categoryBtn)
            clearFormC()
        }).fail(function (err) {
            errorOnCreate(err)
        });
}

function updateC(id) {
    let json = getCategoryJsonFromForm()
    if (json)
        $.ajax({
            type: 'PUT',
            contentType: 'application/json;charset=UTF-8',
            url: origin + '/category/',
            data: json
        }).done(function () {
            let category = frontendDataCategories.get(id)
            category['name'] = cName.val()
            category['reqName'] = cReq.val()
            frontendDataCategories.set(id, category)
            $('#cat' + id).text(cName.val())
            clearFormC()
        }).fail(function (err) {
            errorOnUpdate(err)
        });
}

function delCategory() {
    let id = chkId(cId.val())
    if (id > 0) {
        let url = origin + '/category/' + id
        $.ajax({
            type: 'DELETE',
            url: url
        }).done(function (data) {
            console.log(data.length)
            if (data && data.length > 0) {
                errorT('Category can\'t be deleted because banners with this id\'s still active:' + data)
            } else {
                frontendDataCategories.delete(id)
                let cId = 'cat' + id
                document.getElementById(cId).remove()
                clearFormC()
            }
        }).fail(function (err) {
            errorOnDelete(err)
        });
    }
}

function getCategoryJsonFromForm() {
    let id = chkId(cId.val())
    let name = cName.val()
    let reqName = cReq.val()
    let json = JSON.stringify({'id': id, 'name': name, 'reqName': reqName, 'deleted': false})
    if (chkCategoryName(name) && chkCategoryReq(reqName))
        return json
}