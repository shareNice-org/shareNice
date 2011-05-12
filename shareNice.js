var shareNiceConfig = '{ "digg.com" : { "url" : "http://digg.com/submit?phase=2&url=%SHARE_URL%", "icon" : "digg.com.png" }, "email" : { "url" : "mailto:?subject=Look&body=%SHARE_URL%", "icon" : "icon-email.png" }, "facebook.com" : { "url" : "http://www.facebook.com/sharer.php?u=%SHARE_URL%", "icon" : "facebook.com.png" }, "google.com" : { "url" : "http://www.google.com/bookmarks/mark?op=add&bkmk=", "icon" : "google.com.png" }, "linkedin.com" : { "url" : "http://www.linkedin.com/shareArticle?mini=true&url=%SHARE_URL%", "icon" : "linkedin.com.png" }, "myspace.com" : { "url" : "http://www.myspace.com/Modules/PostTo/Pages/?u=%SHARE_URL%", "icon" : "myspace.com.png" }, "reddit.com" : { "url" : "http://reddit.com/submit?url=%SHARED_URL%", "icon" : "reddit.com.png" }, "stumbleupon.com" : { "url" : "http://www.stumbleupon.com/submit?url=%SHARE_URL%", "icon" : "stumbleupon.com.png" }, "twitter.com" : { "url" : "http://twitter.com/home?status=%SHARE_URL%", "icon" : "twitter.com.png" } }';

jQuery(document).ready(function() {
    var config = jQuery.parseJSON(shareNiceConfig);
    var shareBoxHTML = '';
    if (config != null) {
      var currentPage = window.location;
      for ( site in config ) {
        shareBoxHTML = shareBoxHTML + '<li><a href="' + config[site].url.replace("%SHARE_URL%", currentPage) + '" target="_blank">' +
                       '<img src="images/' + config[site].icon + '" style="border: 0" alt="'+site+'_icon" />' +
                       '</a></li>';
      }
      shareBoxHTML = '<ul id="socialLinks">' + shareBoxHTML + '<div style="clear:both;"></div></ul>';
    }
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
