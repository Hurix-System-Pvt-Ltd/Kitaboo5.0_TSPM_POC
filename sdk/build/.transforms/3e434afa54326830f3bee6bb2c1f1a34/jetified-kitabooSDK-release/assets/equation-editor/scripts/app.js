var app = angular.module('virtual-keyboard', ['ui.router', 'mobileKeyboard-holder']);

app.controller('MainController', function MainController($scope, $state, $timeout) {
    $timeout(function () {
        answerSpan = document.getElementById('math-equation-holder');
        MQ = MathQuill.getInterface(2); // keeps the API stable
        answerMathField = MQ.MathField(answerSpan, {
            spaceBehavesLikeTab: true,
            supSubsRequireOperand: true,
            substituteTextarea: function () {
                return document.createElement('textarea');
            },
            handlers: {
                edit: function () {
                }
            }
        });

        window.enterPress = function () {
            answerMathField.write('\\textcolor{}{\\text{ }}');
            $('.mq-root-block').focus();
            answerMathField.focus();
        }

        dataCallback = function (metaString) {
            var decodedLatex = JSON.parse(atob(metaString)).latex,
                positionY = JSON.parse(atob(metaString)).posY;

            answerMathField.latex(decodedLatex);
            if (Number(positionY.substr(0, positionY.length - 2)) + $('.math-holder').height() <= $('.mobile-keyboard').offset().top) {
                $('.math-holder').css('margin-top', positionY);
            }
            else {
                $('.math-holder').css('margin-top', $('.mobile-keyboard').offset().top - $('.math-holder').height());
            }
            answerMathField.focus();
        }

        // if( $('.forMobile').css('display')=='none') {
        //     $state.go('startApp.desktopholder');
        // }
        // else
        // {
        //$state.go('startApp.mobileholder');            
        // }

        $state.go('startApp.mobileholder');
    });

});

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('startApp', {
            abstract: true,
            template: '<ui-view/>'
        });
});

// app.run( function ($rootScope,$location) {
//     $rootScope.$on('$stateChangeStart',
//     function (event, toState, toParams, fromState, fromParams) {
//         //save location.search so we can add it back after transition is done
//         this.locationSearch = $location.search();
//     });

// $rootScope.$on('$stateChangeSuccess',
//     function (event, toState, toParams, fromState, fromParams) {
//         //restore all query string parameters back to $location.search
//         $location.search(this.locationSearch);
//     });
// });
