let bId
let bName
let bPrice
let bContent
let bannerUrl = origin + '/banner/'


function loadBannersB() {
    let banners = ''
    for (let banner of frontendDataBanners.values()) {
        banners += '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="bnr' + banner['id'] + '" value="' + banner['id'] + '" onclick="applyBannerToFormB(this.value)">' + banner['name'] + '</button>'
    }
    buttonsL.html(banners)
}

function loadCategoriesB() {
    let categories = ''
    for (let category of frontendDataCategories.values()) {
        categories += '<option id = "cat' + category['id'] + '" value="' + category['id'] + '">' + category['name'] + '</option>\n'
    }
    $('#bCategory').html(categories)
}

function applyBannerToFormB(id) {
    clearFormB()
    id = parseInt(id)
    let banner = frontendDataBanners.get(id)
    bId.val(banner['id'])
    bName.val(banner['name'])
    bPrice.val(banner['price'])
    let cId = "cat" + parseInt(banner['category'])
    document.getElementById(cId).selected = true;
    bContent.val(banner['content'])
    rAction.text("Edit Banner: #" + banner["id"])
}

function clearFormB() {
    bId.val(0)
    bName.val('')
    bPrice.val('')
    bContent.val('')
    rAction.text("Create new Banner:")
    clearHint()
}

function fillFormsForBanners() {
    clearHint()
    editForm.html('<input type="text" class="form-control" value="0" id="bId" hidden>\n' +
        '\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="bName" class="col-sm-2 col-form-label">Name</label>\n' +
        '                        <div class="col">\n' +
        '                            <input type="text" class="form-control" id="bName" name="name" maxlength="25" required>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="bPrice" class="col-sm-2 col-form-label">Price</label>\n' +
        '                        <div class="col">\n' +
        '                            <input type="number" step="0.01" min="0" class="form-control" id="bPrice" name="bPrice"\n' +
        '                                   required>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="bCategory" class="col-sm-2 col-form-label">Category</label>\n' +
        '                        <div class="col">\n' +
        '                            <select class="form-control" id="bCategory" name="bCategory" required>\n' +
        '                            </select>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="form-group row m-3">\n' +
        '                        <label for="bContent" class="col-sm-2 col-form-label">Content</label>\n' +
        '                        <div class="col">\n' +
        '                            <textarea class="form-control" rows="8" maxlength="250" aria-label="With textarea"\n' +
        '                                      id="bContent" name="bContent" required></textarea>\n' +
        '                        </div>\n' +
        '                    </div>')
    bId = $('#bId')
    bName = $('#bName')
    bPrice = $('#bPrice')
    bContent = $('#bContent')
    lHeader.text('Banners:')
    rAction.text('Create new Banner: ')

    createBtn.attr("onclick", "").unbind("click");
    createBtn.on("click", function () {
        clearFormB()
    });

    search.val('')
    search.attr("placeholder", "enter banner name");
    search.attr("onkeyup", "").unbind("keyup");
    search.on('keyup', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            searchBanners(search.val())
        }
    });

    saveBtn.attr("onclick", "").unbind("click");
    saveBtn.on("click", function () {
        saveBanner()
    });

    delBtn.attr("onclick", "").unbind("click");
    delBtn.on("click", function () {
        delBanner()
    });

    loadBannersB()
    loadCategoriesB()

}

function searchBanners(input) {
    clearHint()
    if (input && input.length > 0)
        $.get({
            url: bannerUrl + 'search/' + input
        }).done(function (listBanner) {
            let banners = ''
            for (let i in listBanner) {
                let id = listBanner[i]['id']
                let name = listBanner[i]['name']
                banners += '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="bnr' + id + '" value="' + id + '" onclick="applyBannerToFormB(this.value)">' + name + '</button>'
            }
            buttonsL.html(banners)
        }).fail(function (err) {
            let msg = 'error on search'
            if (err){
                msg += err
                console.log(err)
            }
            errorT(msg)
        });
    else
        loadBannersB()
}

function saveBanner() {
    let id = chkId(parseInt(bId.val()))
    if (id === 0)
        createB()
    else
        updateB(id)
}

function createB() {
    let json = getBannerJsonFromForm()
    if (json)
        $.post({
            contentType: 'application/json;charset=UTF-8',
            url: bannerUrl,
            data: json
        }).done(function (banner) {
            let id = banner["id"]
            frontendDataBanners.set(id, banner)
            let bannerBtn = '<button class="btn btn-dark btn-outline-success text text-white m-1"  id="bnr' + banner['id'] + '" value="' + banner['id'] + '" onclick="applyBannerToFormB(this.value)">' + banner['name'] + '</button>'
            buttonsL.append(bannerBtn)
            clearFormB()
        }).fail(function (err) {
            errorOnCreate(err)
        });
}

function updateB(id) {
    let json = getBannerJsonFromForm()
    if (json)
        $.ajax({
            type: 'PUT',
            contentType: 'application/json;charset=UTF-8',
            url: origin + '/banner/',
            data: json
        }).done(function () {
            let banner = frontendDataBanners.get(id)
            banner['name'] = bName.val()
            banner['price'] = bPrice.val()
            banner['category'] = $('#bCategory option:selected').val()
            banner['content'] = bContent.val()
            frontendDataBanners.set(id, banner)
            $('#bnr' + id).text(bName.val())
            clearFormB()
        }).fail(function (err) {
            errorOnUpdate(err)
        });
}


function delBanner() {
    let id = chkId(bId.val())
    if (id > 0) {
        let url = origin + '/banner/' + id
        $.ajax({
            type: 'DELETE',
            url: url
        }).done(function () {
            frontendDataBanners.delete(id)
            let bId = 'bnr' + id
            document.getElementById(bId).remove()
            clearFormB()
        }).fail(function (err) {
            errorOnDelete(err)
        });
    }
}

function getBannerJsonFromForm() {
    let id = chkId(bId.val())
    let name = bName.val()
    let price = bPrice.val()
    let category = $('#bCategory option:selected').val()
    let content = bContent.val()
    let json = JSON.stringify({'id': id, 'name': name, 'price': price, 'category': category, 'content': content})
    if (chkName(name) && chkPrice(price) && chkContent(content))
        return json
}