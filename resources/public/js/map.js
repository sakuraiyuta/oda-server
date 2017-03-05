var map;
var service;
var infowindow;

var lat;
var lng;

$('#mapsearch').on('click', function(){
  var pyrmont = new google.maps.LatLng(34.6937378,135.5021651);

  map = new google.maps.Map(document.getElementById('map'), {
      center: pyrmont,
      zoom: 15
    });

  var request = {
    location: pyrmont,
    radius: '500',
    query: $('#place').val()
  };

  service = new google.maps.places.PlacesService(map);
  service.textSearch(request, callbackSearch);
});

function callbackSearch(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK && results.length > 0) {
    $('#address').val(results[0].formatted_address);
  }
}
