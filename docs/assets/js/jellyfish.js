// The SD keywords.  Used for home-made highlighting.
var sdKeywords = [
      'package',
      'import',
      'model',
      'enum',
      'data',
      'int',
      'float',
      'string',
      'boolean',
      'metadata',
      'input',
      'output',
      'parts',
      'requires',
      'links',
      'link',
      'scenario',
      'given',
      'when',
      'then',
      'properties',
      'many'];

function addClassesToSdLanguageExamples(rootElement) {
  // Only include code blocks that have no language specified.  This means they are
  // SD code examples.
  var sdElements = rootElement.find('div.highlighter-rouge').filter(function(i, element) {
  	return $(element)
      .attr('class')
      .split(' ')
      .findIndex(function(className) { return className.startsWith('language-'); }) < 0;
  });
  
  $(sdElements).find('code span').each(function(i, element) {
    if (sdKeywords.includes($(element).text())) {
      $(element).addClass('sd-keyword');
    }
  });
}

function renderLatestJellyfishRelease(latestReleaseUrl) {
  $.get(latestReleaseUrl, function (response) {
    var link = response.html_url;
    var version = response.name;
    $('#index #get-jellyfish').attr('href', link)
    $('#index #jellyfish-current-version').text(version)
  });
}