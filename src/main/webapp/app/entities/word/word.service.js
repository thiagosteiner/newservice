(function() {
    'use strict';
    angular
        .module('myserviceApp')
        .factory('Word', Word);

    Word.$inject = ['$resource'];

    function Word ($resource) {
        var resourceUrl =  'api/words/:id';

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
