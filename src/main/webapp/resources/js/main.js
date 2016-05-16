var useQueryExp = "";
var useWordNet = "";

$( document ).ready(function() {
    searchPublicationsInit();
});

function searchPublicationsInit() {
    $("#result-list").hide();
    $("#loader").show();
    var url = '/search/submitAsync';
    if(document.getElementById('qe-checkbox').checked) {
        useQueryExp = "on";
    } else {
        useQueryExp = "off";
    }

    if(document.getElementById('wn-checkbox').checked) {
            useWordNet = "on";
        } else {
            useWordNet = "off";
        }
    url = url + '?mainQuery=' + encodeURIComponent($('#mainQuery').val());
    url = url + '&titleQuery=' + encodeURIComponent($('#titleQuery').val());
    url = url + '&authorQuery=' + encodeURIComponent($('#authorQuery').val());
    url = url + '&keywordsQuery=' + encodeURIComponent($('#keywordsQuery').val());
    url = url + '&useQueryExp=' + encodeURIComponent(useQueryExp);
    url = url + '&useWordNet=' + encodeURIComponent(useWordNet);
    //var url_clean = encodeURIComponent(url);
    $("#result-list").load(url, function(){
        $("#loader").fadeOut("fast");
        $("#result-list").show();
    });
}


function searchPublications() {
    $("#result-list").hide();
    $("#loader").show();
    var url = '/search/submitAsync';

    url = url + '?mainQuery=' + encodeURIComponent($('#mainQuery').val());
    url = url + '&titleQuery=' + encodeURIComponent($('#titleQuery').val());
    url = url + '&authorQuery=' + encodeURIComponent($('#authorQuery').val());
    url = url + '&keywordsQuery=' + encodeURIComponent($('#keywordsQuery').val());
    url = url + '&useQueryExp=' + encodeURIComponent(useQueryExp);
    url = url + '&useWordNet=' + encodeURIComponent(useWordNet);
    //var url_clean = encodeURIComponent(url);
    $("#result-list").load(url, function(){
        $("#loader").fadeOut("fast");
        $("#result-list").show();
    });
}
 $('input[name="useQueryExp"]').on('switchChange.bootstrapSwitch', function(event, state) {
   console.log(this); // DOM element
   console.log(event); // jQuery event
   console.log(state); // true | false
   if(state){
    useQueryExp = "on";
   } else {
     useQueryExp = "off";
   }
 });

 $('input[name="useWordNet"]').on('switchChange.bootstrapSwitch', function(event, state) {
    console.log(this); // DOM element
    console.log(event); // jQuery event
    console.log(state); // true | false
    if(state){
        useWordNet = "on";
       } else {
         useWordNet = "off";
       }
  });




