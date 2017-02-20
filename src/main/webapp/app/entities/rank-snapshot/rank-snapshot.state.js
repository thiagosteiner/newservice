(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rank-snapshot', {
            parent: 'entity',
            url: '/rank-snapshot',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RankSnapshots'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshots.html',
                    controller: 'RankSnapshotController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rank-snapshot-detail', {
            parent: 'rank-snapshot',
            url: '/rank-snapshot/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RankSnapshot'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshot-detail.html',
                    controller: 'RankSnapshotDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RankSnapshot', function($stateParams, RankSnapshot) {
                    return RankSnapshot.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rank-snapshot',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rank-snapshot-detail.edit', {
            parent: 'rank-snapshot-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshot-dialog.html',
                    controller: 'RankSnapshotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RankSnapshot', function(RankSnapshot) {
                            return RankSnapshot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rank-snapshot.new', {
            parent: 'rank-snapshot',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshot-dialog.html',
                    controller: 'RankSnapshotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                epoch: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rank-snapshot', null, { reload: 'rank-snapshot' });
                }, function() {
                    $state.go('rank-snapshot');
                });
            }]
        })
        .state('rank-snapshot.edit', {
            parent: 'rank-snapshot',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshot-dialog.html',
                    controller: 'RankSnapshotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RankSnapshot', function(RankSnapshot) {
                            return RankSnapshot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rank-snapshot', null, { reload: 'rank-snapshot' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rank-snapshot.delete', {
            parent: 'rank-snapshot',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rank-snapshot/rank-snapshot-delete-dialog.html',
                    controller: 'RankSnapshotDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RankSnapshot', function(RankSnapshot) {
                            return RankSnapshot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rank-snapshot', null, { reload: 'rank-snapshot' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
