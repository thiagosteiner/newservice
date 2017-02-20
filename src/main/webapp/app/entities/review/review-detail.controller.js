(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewDetailController', ReviewDetailController);

    ReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Review', 'WordOccurrences', 'Book', 'ReviewVector'];

    function ReviewDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Review, WordOccurrences, Book, ReviewVector) {
        var vm = this;

        vm.review = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('myserviceApp:reviewUpdate', function(event, result) {
            vm.review = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
