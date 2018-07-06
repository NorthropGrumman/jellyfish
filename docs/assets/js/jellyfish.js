function renderLatestJellyfishRelease(latestReleaseUrl) {
  $.get(latestReleaseUrl, function (response) {
    var link = response.html_url;
    var version = response.name;
    $('#index #get-jellyfish').attr('href', link)
    $('#index #jellyfish-current-version').text(version)
  });
}