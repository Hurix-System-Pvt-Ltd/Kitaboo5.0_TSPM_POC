
angular.module('mobileKeyboard-holder',[])
    .config(function($stateProvider,$urlRouterProvider) {
        $stateProvider
            
            .state('startApp.mobileholder', {
                views: {
                    'mobile-holder@': {
                        controller: 'MobileKeyboardCtrl as mobilekeyboardCtrl',
                        templateUrl: 'scripts/mobile-keyboard-holder/mobile-keyboard.html'
                    }
                }
            });
            // .state('startApp.loginshell.loginsignup', {
            //     url:'/login',
            //     views: {
            //         'loginsignup': {
            //             controller: 'LoginSignupCtrl as loginsignupCtrl',
            //             templateUrl: 'commonshell/contents/login-signup/login-signup.html'
            //         }
            //     }
            // })
            // .state('startApp.loginshell.forgotpwd', {
            //     url:'/forgotpassword',
            //     views: {
            //         'forgotpwd': {
            //             controller: 'ForgotPwdCtrl as forgotpwdCtrl',
            //             templateUrl: 'commonshell/contents/forgot-password/forgotpwd.html'
            //         }
            //     }
            // });
    });
