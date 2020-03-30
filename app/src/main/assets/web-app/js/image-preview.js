$(document).ready(function() {

    var element = $('.image-preview');

    element.each(function() {
        $(this).css({'cursor':'pointer'});
    });
    element.mouseenter(function() {
        $(this).css({'opacity':'0.9'});
    });
    element.mouseleave(function() {
        $(this).css({'opacity':'1.0'});
    });

    $(document).on("click", '.image-preview', function(e) {

        e.stopPropagation();

        var imageCssUrl = null;

        if ($(this).is('img')) {

            var url = $(this).attr('src');
            imageCssUrl = 'url("'+url+'")';

        } else if ($(this).is('div')) {

            imageCssUrl = $(this).css('background-image');

        }

        imageCssUrl = imageCssUrl.replaceAll('"','');
        var partList = imageCssUrl.split(", ");
        if (partList.length > 1) {
            imageCssUrl = partList[0];
        }

        var imageUrl = imageCssUrl.replaceAll('url(','');
        imageUrl = imageUrl.replaceAll(')','');
        imageUrl = imageUrl.replaceAll("'","");
        var partList = imageUrl.split(", ");
        if (partList.length > 1) {
            imageUrl = partList[0];
        }

        //var x = $(this).offset().left;
        //var y = $(this).offset().top - $(document).scrollTop();
        //var startWidth = $(this).width();
        //var startHeight = $(this).height();

        getImgSize(imageUrl, function(width, height) {

            var outerContainer = $('<div class="image-preview-container select-no" style="z-index: 201; width: 100%; height: 100%; position: fixed; display: table; background-color: rgba(0,0,0,0.85); opacity: 1"></div>');
            var closeButtonContainer = $('<div style="position: absolute; top: 18px; right: 20px; color: #fff; font-size: 32px; cursor: pointer"><i class="fa fa-times"></i></div>');
            var centerContainer = $('<div style="display: table-cell; vertical-align: middle; text-align: center; width: 100%; height: 100%"></div>');
            var imageContainer = $('<div style="max-width: '+width+'px; max-height:'+height+'px; width: 90%; height: 90%; display: inline-block; background: '+imageCssUrl+' no-repeat center; background-size: contain"></div>');

            outerContainer.append(closeButtonContainer);
            centerContainer.append(imageContainer);
            outerContainer.append(centerContainer);

            $('body').prepend(outerContainer);

            $('.image-preview-container').click(function() {
                $(this).remove();
            });

        });

    });

    function getImgSize(imgSrc, callback) {
        var newImg = new Image();
        newImg.onload = function() {
            var height = newImg.height;
            var width = newImg.width;
            callback(width, height);
        };
        newImg.src = imgSrc;
    }

    String.prototype.replaceAll = function(str1, str2, ignore)
    {
        return this.replace(new RegExp(str1.replace(/([\/\,\!\\\^\$\{\}\[\]\(\)\.\*\+\?\|\<\>\-\&])/g,"\\$&"),(ignore?"gi":"g")),(typeof(str2)=="string")?str2.replace(/\$/g,"$$$$"):str2);
    }

});