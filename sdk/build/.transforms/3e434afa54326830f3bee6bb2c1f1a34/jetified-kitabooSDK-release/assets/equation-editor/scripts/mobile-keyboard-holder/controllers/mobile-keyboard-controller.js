angular.module('mobileKeyboard-holder').controller('MobileKeyboardCtrl', function MobileKeyboardCtrl($state, $scope, $timeout) {
    $scope.mobileKeyboardData = mobileKeyboardData;
    $timeout(function () {
        $('.number').on('touchstart click', $scope.onNumberBtnClick);
        $('.capitalnumbers').on('touchstart click', $scope.oncapitalnumbersClick)
        $('.mathPanel').on('touchstart click', $scope.onMathBtnClick);
        $('.relativeDiv').on('touchstart click', $scope.onArrowClick);
        $('.alfabetDiv').on('touchstart click', $scope.onChooseClick);
        $('.symbolDiv').on('touchstart click', $scope.onChooseClick1);
        $('.numberPanelDiv').on('touchstart click', $scope.onChooseClick2);
        $('.hideKeyBoardDiv').on('touchstart click', $scope.onhideKeyBoardClick);
        $('.chooseDiv3').on('touchstart click', $scope.onChooseClick3);
        $('.chooseDiv4').on('touchstart click', $scope.onChooseClick4);
        $('.overlay-modal').on('click', $scope.onOverlayClick);
        $('.equation-holder').on('touchstart click', $scope.onMathquillTap);
        $('.equation-holder').on('keyup', $scope.onKeyup);
        $('.keys').on('touchstart click', $scope.onKeyTap);
        $('.showkeyboard').on('touchstart click', $scope.onshowkeyboardClick);
        $('.addBtn').on('touchstart click', $scope.onDoneBtnClick);
        $('.cancelBtn').on('touchstart click', $scope.onCancelBtnClick);
        $('.enterclick').on('touchstart click', $scope.onenterbuttonclick);

        var answerMathField = $('#math-equation-holder', '.haseqnEditor');

        $('.equation-holder').focus();
        $('#math-equation-holder').focus();
        $('.mq-textarea').focus();
        // $('.equation-holder').draggable({ 
        //     handle: ".drag-this",
        //     scroll : false,
        //     containment : [0,0,$('body').width() - $('.equation-holder').width(),containmentBottom]
        // });
        $('.done-btn').on('touchstart click', $scope.onDoneBtnClick);
        $('.abcIcon').on('touchstart click', $scope.onAbcBtnClick);
        var containmentBottom = $('body').height() - $(".mobile-keyboard").height() - $('#draggable-element').height();

        $('#draggable-element').draggable({
            handle: ".drag-this",
            scroll: false,
            containment: [0, 0, $('body').width() - $('#draggable-element').width(), containmentBottom]
        });

        window.addEventListener("resize", function () {
            var containmentBottom = $('body').height() - $(".mobile-keyboard").height() - $('#draggable-element').height();

            $('#draggable-element').draggable({
                handle: ".drag-this",
                scroll: false,
                containment: [0, 0, $('body').width() - $('#draggable-element').width(), containmentBottom]
            });

        }, false);

    });
    $(window).on("orientationchange", function (event) {
        $("#draggable-element").css({
            "position": "absolute", "top": "10%", "left": "auto"
        });
    });

    window.addEventListener("resize", function (event) {
        $("#draggable-element").css({
            "position": "absolute", "top": "10%", "left": "auto"
        });
    });



    $scope.onenterbuttonclick = function () {
        window.enterPress();
    }

    $scope.onNumberBtnClick = function () {
        $('.math').hide();
        $('.numberPanel').show();
    };
    $scope.oncapitalnumbersClick = function () {
        $('.alfabetDiv').hide();
        $('.choosePanelUpperCase').show();
    }
    $scope.onMathBtnClick = function () {
        $('.math').show();
        $('.numberPanel').hide();
    };
    $scope.onArrowClick = function () {
        if ($('.overlay-modal').css('display') == 'none') {
            if ($('.numberPanel').css('display') == 'none') {
                $('.navPanel').css('left', '16.5%');
            }
            else {
                $('.navPanel').css('left', '60.5%');
            }
            $('.overlay-modal').show();
            $('.navPanel').show();
            $('.relativeDiv').css('position', 'relative');
        }
        else {
            $('.overlay-modal').hide();
            $('.navPanel').hide();
            $('.relativeDiv').css('position', 'static');
        }
    };
    $scope.onChooseClick = function () {
        if ($('.overlay-modal').css('display') == 'none') {
            $('.overlay-modal').show();
            $('.choosePanel').show();
            //$('.choosePanel3').show();
            $('.alfabetDiv').css('position', 'relative');
        }
        else {
            $('.overlay-modal').hide();
            $('.choosePanel').hide();
            $('.choosePanel3').hide();
            $('.alfabetDiv').css('position', 'static');
        }
    };
    $scope.onChooseClick1 = function () {
        if ($('.overlay-modal').css('display') == 'none') {
            $('.overlay-modal').show();
            $('.symbolPanel').show();
            $('.symbolDiv').css('position', 'relative');
        }
        else {
            $('.overlay-modal').hide();
            $('.symbolPanel').hide();
            $('.symbolDiv').css('position', 'static');
        }
    };
    $scope.onChooseClick2 = function () {
        if ($('.overlay-modal').css('display') == 'none') {
            $('.overlay-modal').show();
            $('.choosePanel2').show();
            $('.numberPanelDiv').css('position', 'relative');
        }
        else {
            $('.overlay-modal').hide();
            $('.choosePanel2').hide();
            $('.numberPanelDiv').css('position', 'static');
        }
    };
    $scope.onhideKeyBoardClick = function () {
        //$scope.showkeboard = true;
        //$('.mobile-keyboard').hide();
        $('.math').hide();
        $('.showkeyboard').show();
        var containmentBottom = $('body').height() - $('#draggable-element').height();
        $('#draggable-element').draggable({
            handle: ".drag-this",
            scroll: false,
            containment: [0, 0, $('body').width() - $('#draggable-element').width(), containmentBottom]
        });
    };
    $scope.onshowkeyboardClick = function () {
        window.setTimeout(function () {
            $('.math').show();
            $('.showkeyboard').hide();
            var containmentBottom = $('body').height() - $(".mobile-keyboard").height() - $('#draggable-element').height();

            $('#draggable-element').draggable({
                handle: ".drag-this",
                scroll: false,
                containment: [0, 0, $('body').width() - $('#draggable-element').width(), containmentBottom]
            });
        }, 200);
    };
    // $scope.onshowkeboard =function(){
    //     $('.math').show();
    //     $('.showkeboard').hide();
    // };
    $scope.onChooseClick3 = function () {
        $('.overlay-modal').show();
        $('.choosePanel3').show();
        $('.choosePanel').hide();
        $('.chooseDiv3').css('position', 'relative');
    };
    $scope.onChooseClick4 = function () {
        $('.overlay-modal').show();
        $('.choosePanel3').hide();
        $('.choosePanel').show();
        //$('.math').show();
        $('.chooseDiv4').css('position', 'relative');
    };
    $scope.onOverlayClick = function () {
        $('.overlay-modal').hide();
        $('.choosePanel').hide();
        $('.symbolPanel').hide();
        $('.choosePanel2').hide();
        $('.choosePanel3').hide();
        $('.alfabetDiv').css('position', 'static');
        $('.symbolDiv').css('position', 'static');
        $('.numberPanelDiv').css('position', 'static');
        $('.chooseDiv3').css('position', 'static');
        $('.chooseDiv4').css('position', 'static');
        $('.navPanel').hide();
        $('.relativeDiv').css('position', 'static');
    };
    $('.mq-root-block').focus();
    $('.text-area').focus();
    $scope.onMathquillTap = function (event) {
        answerMathField.focus();
    };

    $scope.onKeyup = function (event) {
        if ($('.errormsgtext')[0].style.display == 'block') {
            $('.equation-holder').css("border-color", "#b0b0b0");
            $('.errormsgtext').css("display", "none");
            document.getElementsByClassName('addBtn').style.pointerEvents = 'block';
        }
    }

    $scope.onKeyTap = function (event) {
        event.preventDefault();
        var arrDataLatex = $(event.currentTarget).attr('data-latex-val').split('-'),
            split1 = arrDataLatex[0],
            split2 = arrDataLatex[1],
            split3 = arrDataLatex[2],
            latex = mobileKeyboardData[split1][split2][Number(split3) - 1].latex
        if (latex !== undefined) {
            $.each(latex, function (index, obj) {
                if (obj.indexOf('key') >= 0) {
                    answerMathField.keystroke(obj.slice(obj.indexOf('-') + 1, obj.length));
                }
                else {
                    answerMathField.typedText(obj);
                }
            });
            answerMathField.focus();
        }
    };
    
    $scope.onCancelBtnClick = function (event) {
        var metaString = {
            'cancel': true
        };

        var encodedString = encodeURIComponent(JSON.stringify(metaString));
        $scope.nativeCallback(encodedString);
    }

    $scope.onDoneBtnClick = function (event) {
        var metaString = {
            'latex': answerMathField.latex(),
            'height': $(answerSpan).height(),
            'width': $(answerSpan).width(),
            'fontSize': $(answerSpan).css('font-size')
        };

        var encodedString = encodeURIComponent(JSON.stringify(metaString));
        $scope.nativeCallback(encodedString);
    };

    $scope.onAbcBtnClick = function (event) {
        var metaString = {
            'changeKeyboard': true,
            'latex': answerMathField.latex()
        };
        var encodedString = encodeURIComponent(JSON.stringify(metaString));
        $scope.nativeCallback(encodedString);
    }

    $scope.nativeCallback = function (encodedString) {
        var userAgent = window.navigator.userAgent;
        if (userAgent.indexOf('Android') !== -1) {
            window.android.getEquationData(encodedString);
        } else if ((userAgent.indexOf('iPad') !== -1 || userAgent.indexOf('iPhone') !== -1)) {
            window.location = "getEquationData:" + encodedString;
        } else {
            window.external.notify(encodedString);
        }
    };
});
