(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('word', {
            parent: 'entity',
            url: '/word',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Words'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word/words.html',
                    controller: 'WordController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('word-detail', {
            parent: 'word',
            url: '/word/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Word'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word/word-detail.html',
                    controller: 'WordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Word', function($stateParams, Word) {
                    return Word.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'word',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('word-detail.edit', {
            parent: 'word-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word/word-dialog.html',
                    controller: 'WordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Word', function(Word) {
                            return Word.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word.new', {
            parent: 'word',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word/word-dialog.html',
                    controller: 'WordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                wordstring: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('word', null, { reload: 'word' });
                }, function() {
                    $state.go('word');
                });
            }]
        })
        .state('word.edit', {
            parent: 'word',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word/word-dialog.html',
                    controller: 'WordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Word', function(Word) {
                            return Word.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word', null, { reload: 'word' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word.delete', {
            parent: 'word',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word/word-delete-dialog.html',
                    controller: 'WordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Word', function(Word) {
                            return Word.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word', null, { reload: 'word' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
