(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorDetailController', ReviewVectorDetailController);

    ReviewVectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ReviewVector'];

    function ReviewVectorDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ReviewVector) {
        var vm = this;

        vm.reviewVector = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('myserviceApp:reviewVectorUpdate', function(event, result) {
            vm.reviewVector = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
