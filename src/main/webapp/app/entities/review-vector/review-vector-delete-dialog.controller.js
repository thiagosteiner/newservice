(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorDeleteController',ReviewVectorDeleteController);

    ReviewVectorDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReviewVector'];

    function ReviewVectorDeleteController($uibModalInstance, entity, ReviewVector) {
        var vm = this;

        vm.reviewVector = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReviewVector.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
