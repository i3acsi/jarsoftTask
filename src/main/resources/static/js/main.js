function loadFrontEndData() {
    loadFrontEndDataBanners().then(function () {
        loadBanners()
    })

    loadFrontEndDataCategories().then(function () {
        loadCategories()
    })
}

function loadFrontEndDataBanners() {
   return  $.get({
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
   return  $.get({
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

function loadBanners() {
    let banners = ''
    for (let i = 1; i <= frontendDataBanners.size; i++) {
        let banner = frontendDataBanners.get(i)
        banners += '<button class="btn btn-dark btn-outline-success text text-white m-1"  value="' + banner['id'] + '" onclick="applyBannerToForm(this.value)">' + banner['name'] + '</button>'
    }
    $('#bannerB').html(banners)
}

function loadCategories() {
    let categories = ''
    for (let i = 1; i <= frontendDataCategories.size; i++) {
        let category = frontendDataCategories.get(i)
        categories += '<option id = "cat' + category['id'] + '" value="' + category['id'] + '">' + category['name'] + '</option>\n'
    }
    $('#bCategory').html(categories)
}

function applyBannerToForm(id) {
    id = parseInt(id)
    let banner = frontendDataBanners.get(id)
    $('#bId').val(banner['id'])
    $('#bName').val(banner['name'])
    $('#bPrice').val(banner['price'])
    let cId = "cat"+parseInt(banner['category'])
    document.getElementById(cId).selected=true;
    $('#bContent').val(banner['content'])
    $('#rAction').text("Edit Banner: #" +banner["id"])
    globalAction = 'editBanner'

}