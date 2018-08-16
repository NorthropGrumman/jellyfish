/**
 *  UNCLASSIFIED
 *  Northrop Grumman Proprietary
 *  ____________________________
 *
 *   Copyright © 2018, Northrop Grumman Systems Corporation
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains the property of
 *  Northrop Grumman Systems Corporation. The intellectual and technical concepts
 *  contained herein are proprietary to Northrop Grumman Systems Corporation and
 *  may be covered by U.S. and Foreign Patents or patents in process, and are
 *  protected by trade secret or copyright law. Dissemination of this information
 *  or reproduction of this material is strictly forbidden unless prior written
 *  permission is obtained from Northrop Grumman.
 */

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
  'many',
  'extends'
];

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