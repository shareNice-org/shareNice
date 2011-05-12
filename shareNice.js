var shareNiceConfig = '{ "twitter" : { "url" : "http://twitter.com/home?status=[SHARE_URL]" , "icon" : "twitter.com.png" }}';


jQuery(document).ready(function() {
    var config = jQuery.parseJSON(shareNiceConfig) ;
//    alert(config.twitter.url);

    var shareBoxHTML = '<ul id="socialLinks">' +
                       '<li>' + 
                       '<a href="' + config.twitter.url + '">' +
                       '<img src="images/' + config.twitter.icon + '" style="border: 0" alt="email icon" />' +
                       '</a>' +
                       '</li>' +
                       '<div style="clear:both;"></div>'+
                       '</ul>';
    $('div#share-box-body').html(shareBoxHTML);

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
