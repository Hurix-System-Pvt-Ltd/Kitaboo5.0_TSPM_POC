document.addEventListener("DOMContentLoaded", function() {
    //    setTimeout(function(){
    //        checkStyleNode();
    //        var istwopage = isLandscape();
    //        console.log('orientation',istwopage);
    //        applyCSSRule(istwopage == true ? 2: 1);
    //    },500);
    checkStyleNode();
}, false);



function doublemode(istwopage) {
    // var istwopage = isLandscape();
    console.log('orientation', istwopage);
    applyCSSRule(istwopage == true ? 2 : 1);
}

function isLandscape() {
    var height = window.outerHeight;
    var width = window.outerWidth;
    var landscape = width > height;
    return landscape;
}

function Dual() {
    setTimeout(function() {
        checkStyleNode();
        var istwopage = isLandscape();
        console.log('orientation', istwopage);
        applyCSSRule(istwopage == true ? 2 : 1);
    }, 500);
}

function checkStyleNode() {
    if (true) {
        var empttext = document.createTextNode('');
        var emptystyle = document.createElement('style');
        emptystyle.appendChild(empttext);
        document.head.insertBefore(emptystyle, document.head.firstElementChild);
    }
}

function addCSSRuleStyle(selector, newRule) {
    var mySheet = document.styleSheets[0];
    console.log(mySheet)
    if (mySheet != undefined) {
        if (mySheet.addRule) {
            for (var i = 0; i < mySheet.rules.length; i++) {
                if (mySheet.rules[i].selectorText === 'html')
                    mySheet.removeRule(i);
            }
            mySheet.addRule(selector, newRule);
        } else {
            console.log(mySheet.cssRules);
            ruleIndex = mySheet.cssRules.length;

            mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
        }
    } else {
        console.log('stylesheet not available');
    }
}

function onNextDual() {
    clearSelectionD();
  var pageCont = Math.ceil(parseInt(document.body.scrollWidth) / window.innerWidth);
    console.log('pageCont: ' + pageCont);
    var CurrCont = Math.ceil(parseInt(window.scrollX + window.innerWidth) / window.innerWidth);
    console.log('CurrCont: ' + CurrCont);
    if (CurrCont == pageCont) {
        console.log('CurrCont: ' + CurrCont);
        console.log('CurrCont: ' + pageCont);
        ptr.NextChapter();
    } else {
        window.scrollBy(window.outerWidth, 0);
    }
    return 'Next';
}

function onPreviousDual() {
    clearSelectionD();
    console.log('PreviousscrollX: ' + window.scrollX);
    if (window.scrollX == 0) {
        ptr.PrevChapter();
    } else {
        window.scrollBy(-window.outerWidth, 0);
    }
    return 'Previous';
}

function applyCSSRule(pagesize) {
    var height = window.outerHeight;
    var width = window.outerWidth / pagesize;
    addCSSRuleStyle('html', 'height: ' + height + 'px; -webkit-column-gap: 0px; -webkit-column-width: ' + width + 'px;width: ' + width + 'px;-webkit-column-rule: 1px outset #999999;');
}

function removeCSSRule() {
    console.log("removed...............");
    var height = window.outerHeight;
    var width = window.outerWidth;
    removeRuleStyle('html', 'height: ' + height + 'px;width: ' + width + 'px;');
}

function removeRuleStyle(selector, newRule) {
    var mySheet = document.styleSheets[0];
    console.log(mySheet)
    if (mySheet != undefined) {
        if (mySheet.addRule) {
            for (var i = 0; i < mySheet.rules.length; i++) {
                if (mySheet.rules[i].selectorText === 'html')
                    mySheet.removeRule(i);
            }
            mySheet.removeRule(selector, newRule);
        } else {
            console.log(mySheet.cssRules);
            ruleIndex = mySheet.cssRules.length;

            mySheet.removeRule(selector + '{' + newRule + ';}', ruleIndex);
        }
    } else {
        console.log('stylesheet not available');
    }
    console.log("removed finaly...............");
}

function clearSelectionD() {
    if (window.getSelection) window.getSelection().removeAllRanges();
    else if (document.selection) document.selection.empty();
}
