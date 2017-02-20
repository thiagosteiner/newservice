(function() {
    'use strict';
    angular
        .module('myserviceApp')
        .factory('WordRank', WordRank);

    WordRank.$inject = ['$resource'];

    function WordRank ($resource) {
        var resourceUrl =  'api/word-ranks/:id';

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
