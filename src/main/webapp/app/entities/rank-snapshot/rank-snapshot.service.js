(function() {
    'use strict';
    angular
        .module('myserviceApp')
        .factory('RankSnapshot', RankSnapshot);

    RankSnapshot.$inject = ['$resource'];

    function RankSnapshot ($resource) {
        var resourceUrl =  'api/rank-snapshots/:id';

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
