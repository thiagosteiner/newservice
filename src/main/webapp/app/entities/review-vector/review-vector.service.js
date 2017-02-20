(function() {
    'use strict';
    angular
        .module('myserviceApp')
        .factory('ReviewVector', ReviewVector);

    ReviewVector.$inject = ['$resource'];

    function ReviewVector ($resource) {
        var resourceUrl =  'api/review-vectors/:id';

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
