(function() {
    'use strict';
    angular
        .module('myserviceApp')
        .factory('WordOccurrences', WordOccurrences);

    WordOccurrences.$inject = ['$resource'];

    function WordOccurrences ($resource) {
        var resourceUrl =  'api/word-occurrences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
