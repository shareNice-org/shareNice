jQuery(document).ready(function() {
    $('a#shareBoxLink, #share-box-wrapper').mouseenter(function() {
    $('#share-box-wrapper').show();
    $('#share-box-wrapper').css({'top':$(this).offset().top});
    $('#share-box-wrapper').css({'left':$(this).offset().left});

    var value = window.location;
    $('a#share-twitter').attr('href', 'http://twitter.com/home?status=' + value);
    $('a#share-twitter').attr('target','_blank');
    $('a#share-email').attr('href', 'mailto:?body=' + value);
    $('a#share-fb').attr('href', 'http://www.facebook.com/sharer.php?u=' + value);
    $('a#share-fb').attr('target','_blank');
    });

    $('#share-box-wrapper').mouseleave(function () {
    $(this).hide();
    });
});

var shareNiceConfig = '
{ "twitter" : 
    {
        "url" : "http://twitter.com/home?status=[SHARE_URL]" ,
        "icon" : "twitter.com.png" 
    },
  "email" : 
    {
        "url" : "mailto:?subject=Look&body=[SHARE_URL]",
        "icon" : "email.png"
    },
  "facebook" : 
    {
        "url" : "http://www.facebook.com/sharer.php?u=[SHARE_URL]",
        "icon" : "facebook.com.png"
    }

}';


/* vi:set expandtab sts=4 sw=4: */
