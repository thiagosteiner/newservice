(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('review-vector', {
            parent: 'entity',
            url: '/review-vector',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ReviewVectors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/review-vector/review-vectors.html',
                    controller: 'ReviewVectorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('review-vector-detail', {
            parent: 'review-vector',
            url: '/review-vector/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ReviewVector'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/review-vector/review-vector-detail.html',
                    controller: 'ReviewVectorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ReviewVector', function($stateParams, ReviewVector) {
                    return ReviewVector.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'review-vector',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('review-vector-detail.edit', {
            parent: 'review-vector-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/review-vector/review-vector-dialog.html',
                    controller: 'ReviewVectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReviewVector', function(ReviewVector) {
                            return ReviewVector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('review-vector.new', {
            parent: 'review-vector',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/review-vector/review-vector-dialog.html',
                    controller: 'ReviewVectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                vector: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('review-vector', null, { reload: 'review-vector' });
                }, function() {
                    $state.go('review-vector');
                });
            }]
        })
        .state('review-vector.edit', {
            parent: 'review-vector',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/review-vector/review-vector-dialog.html',
                    controller: 'ReviewVectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReviewVector', function(ReviewVector) {
                            return ReviewVector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('review-vector', null, { reload: 'review-vector' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('review-vector.delete', {
            parent: 'review-vector',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/review-vector/review-vector-delete-dialog.html',
                    controller: 'ReviewVectorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReviewVector', function(ReviewVector) {
                            return ReviewVector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('review-vector', null, { reload: 'review-vector' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
