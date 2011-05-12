var shareNiceConfig = ' { "email" : { "url" : "mailto:?subject=Look&body=%SHARE_URL%", "icon" : "icon-email.png" }, "facebook.com" : { "url" : "http://www.facebook.com/sharer.php?u=%SHARE_URL%", "icon" : "facebook.com.png" }, "twitter.com" : { "url" : "http://twitter.com/home?status=%SHARE_URL%", "icon" : "twitter.com.png" } }';

jQuery(document).ready(function() {
    var config = jQuery.parseJSON(shareNiceConfig) ;

    for(var k in config){
        alert(k);

        var shareBoxHTML = '<ul id="socialLinks">' +
                       '<li>' + 
                       '<a href="' + config[k].url + '">' +
                       '<img src="images/' + config[k].icon + '" style="border: 0" alt="email icon" />' +
                       '</a>' +
                       '</li>' +
                       '<div style="clear:both;"></div>'+
                       '</ul>';
        $('div#share-box-body').html(shareBoxHTML);
    }

    $('a#shareBoxLink, #share-box-wrapper').mouseenter(function() {
    $('#share-box-wrapper').show();
    $('#share-box-wrapper').css({'top':$(this).offset().top});
    $('#share-box-wrapper').css({'left':$(this).offset().left});


    });

    $('#share-box-wrapper').mouseleave(function () {
    $(this).hide();
    });

});

/* vi:set expandtab sts=4 sw=4: */
