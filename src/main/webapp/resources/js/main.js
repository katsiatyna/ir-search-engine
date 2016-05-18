var useQueryExp = "";
var useWordNet = "";
var operator = "";

$( document ).ready(function() {
    if($("#result-list").length > 0){
        initParams();
        search();
    }
});

function search(){
    searchPublications();
    searchDBPedia();
    searchDBLP();
}

function searchDBLP() {
    $("#result-list-dblp").hide();
    $("#loader-dblp").show();
    var url = '/search/submitDBLPAsync';

    url = url + '?mainQuery=' + encodeURIComponent($('#mainQuery').val());
    url = url + '&titleQuery=' + encodeURIComponent($('#titleQuery').val());
    url = url + '&authorQuery=' + encodeURIComponent($('#authorQuery').val());
    url = url + '&keywordsQuery=' + encodeURIComponent($('#keywordsQuery').val());
    url = url + '&useQueryExp=' + encodeURIComponent(useQueryExp);
    url = url + '&useWordNet=' + encodeURIComponent(useWordNet);
    url = url + '&year=' + encodeURIComponent($('#year').val());
    url = url + '&operator=' + encodeURIComponent(operator);
    //var url_clean = encodeURIComponent(url);
    $("#result-list-dblp").load(url, function(){
        $("#loader-dblp").fadeOut("fast");
        $("#result-list-dblp").show();
    });
}


function searchDBPedia() {
    $("#result-list-dbpedia").hide();
    $("#loader-dbpedia").show();
    var url = '/search/submitDBPediaAsync';

    url = url + '?mainQuery=' + encodeURIComponent($('#mainQuery').val());
    url = url + '&titleQuery=' + encodeURIComponent($('#titleQuery').val());
    url = url + '&authorQuery=' + encodeURIComponent($('#authorQuery').val());
    url = url + '&keywordsQuery=' + encodeURIComponent($('#keywordsQuery').val());
    url = url + '&useQueryExp=' + encodeURIComponent(useQueryExp);
    url = url + '&useWordNet=' + encodeURIComponent(useWordNet);
    url = url + '&year=' + encodeURIComponent($('#year').val());
    url = url + '&operator=' + encodeURIComponent(operator);
    //var url_clean = encodeURIComponent(url);
    $("#result-list-dbpedia").load(url, function(){
        $("#loader-dbpedia").fadeOut("fast");
        $("#result-list-dbpedia").show();
    });
}

function initParams(){
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

    if(document.getElementById('eq').checked){
        operator = "eq";
        $('#eq').parent().addClass('active');
    }
    if(document.getElementById('le').checked){
        operator = "le";
        $('#le').parent().addClass('active');
    }
    if(document.getElementById('lt').checked){
        operator = "lt";
        $('#lt').parent().addClass('active');
    }
    if(document.getElementById('ge').checked){
        operator = "ge";
        $('#ge').parent().addClass('active');
    }
    if(document.getElementById('gt').checked){
        operator = "gt";
        $('#gt').parent().addClass('active');
    }
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
    url = url + '&year=' + encodeURIComponent($('#year').val());
    url = url + '&operator=' + encodeURIComponent(operator);
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


  $(function() {

      $('#eq').bind('change', function (v) {

          if($(this).is(':checked')) {
             operator = "eq";
          } else {

          }
      });

  });

  $(function() {

        $('#lt').bind('change', function (v) {

            if($(this).is(':checked')) {
               operator = "lt";
            } else {

            }
        });

    });

    $(function() {

          $('#gt').bind('change', function (v) {

              if($(this).is(':checked')) {
                 operator = "gt";
              } else {

              }
          });

      });

      $(function() {

            $('#le').bind('change', function (v) {

                if($(this).is(':checked')) {
                   operator = "le";
                } else {

                }
            });

        });

        $(function() {

              $('#ge').bind('change', function (v) {

                  if($(this).is(':checked')) {
                     operator = "ge";
                  } else {

                  }
              });

          });




