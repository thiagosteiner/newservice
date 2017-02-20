'use strict';

describe('Controller Tests', function() {

    describe('WordRank Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWordRank, MockRankSnapshot, MockWord;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWordRank = jasmine.createSpy('MockWordRank');
            MockRankSnapshot = jasmine.createSpy('MockRankSnapshot');
            MockWord = jasmine.createSpy('MockWord');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WordRank': MockWordRank,
                'RankSnapshot': MockRankSnapshot,
                'Word': MockWord
            };
            createController = function() {
                $injector.get('$controller')("WordRankDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myserviceApp:wordRankUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
