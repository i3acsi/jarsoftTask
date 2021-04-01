function loadFrontEndData() {
    loadFrontEndDataBanners().then(function () {
        loadFrontEndDataCategories().then(function () {
            setBannersToDoc()
        })
    })


}

function setBannersToDoc() {
    showBanners.val(true)
    showBanners.addClass('active')
    showCategories.val(false)
    showCategories.removeClass('active')

    fillFormsForBanners()
}

function setCategoriesToDoc() {
    showCategories.val(true)
    showCategories.addClass('active')
    showBanners.val(false)
    showBanners.removeClass('active')

    fillFormsForCategories()
}

function loadFrontEndDataBanners() {
    return $.get({
        url: location.origin + '/banner/',
    }).done(function (listBanner) {
        frontendDataBanners = new Map()
        for (let k in listBanner) {
            let id = listBanner[k]["id"]
            frontendDataBanners.set(id, listBanner[k])
        }
    }).fail(function (err) {
        console.log(err)
    });

}

function loadFrontEndDataCategories() {
    return $.get({
        url: location.origin + '/category/',
    }).done(function (listCat) {
        frontendDataCategories = new Map()
        for (let k in listCat) {
            let id = listCat[k]["id"]
            frontendDataCategories.set(id, listCat[k])
        }
    }).fail(function (err) {
        console.log(err)
    });
}

