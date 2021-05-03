/* Document selection listener */
var exactWidth = 0;
var exactHeight = 0;
var pagecount = 0;
var scalePercentage = 1;
var elementList = ["kitaboospan", "kitabooNote"];
var cfiElementList = [];

function onSelectionStart() {
    window.location = "onselectionstart:selectionstart";
}

/*window.onresize = function() {
    setTimeout(function() {}, 5000);
};*/
var currentTop;


function clearSelection() {
    if (window.getSelection) window.getSelection().removeAllRanges();
    else if (document.selection) document.selection.empty();
}

function addListner() {
    var body = document.body;
    body.setAttribute("onselectionchange", "onSelectionChange()");
}


function removeElementsByClass(className) {
    var elements = document.getElementsByClassName(className);
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }
}


function visibleTextNodes() {
    var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, function(node) {
            if (node.nodeType == node.TEXT_NODE) {
                return NodeFilter.FILTER_ACCEPT;
            } else {
                return NodeFilter.FILTER_REJECT;
            }
        },
        false
    );

    for (var nodes = []; walker.nextNode();) {
        nodes.push(walker.currentNode);
    }
    return nodes;
}


window.onload = function() {
        updateimages();
        updatetables();

        if(ReflowablePlayerView != undefined)
               ReflowablePlayerView.onDocLoaded();
}

function updateimages() {
    var images = document.getElementsByTagName('img');
    var pageHeight = document.documentElement.clientHeight
    var pageWidth = document.documentElement.clientWidth
    for (var i = 0; i < images.length; i++) {
    if(images[i].getBoundingClientRect().height > window.innerHeight)
        images[i].setAttribute('style', "height:" + (pageHeight * .9) + "px;" + "max-height:" + (pageHeight * .9) + "px;");
        if(images[i].getBoundingClientRect().width > window.innerWidth)
        images[i].setAttribute('style', "width:" + (pageWidth * .8) + "px !important;" + "max-width:" + (pageWidth * .8) + "px !important;"  + "min-width:" + (pageWidth * .8) + "px !important;");
    }
}

function updatetables() {
    var tables = document.getElementsByTagName('table');
    var pageHeight = document.documentElement.clientHeight
    for (var i = 0; i < tables.length; i++) {
    if(tables[i].getBoundingClientRect().height > window.innerHeight)
        tables[i].setAttribute('style', "height:" + (pageHeight * .9) + "px;" + "max-height:" + (pageHeight * .9) + "px;");

    }
}

function updateimageWidth() {
    var images = document.getElementsByTagName('img');
    var pageWidth = (document.documentElement.clientWidth)
    for (var i = 0; i < images.length; i++) {
        images[i].setAttribute('style', "width:" + (pageWidth * .82) + "px;" + "max-width:" + (pageWidth * .82) + "px;");

    }
}

function updateimageHeightMobilePort() {
    var images = document.getElementsByTagName('img');
    var pageHeight = document.documentElement.clientHeight
    for (var i = 0; i < images.length; i++) {
        images[i].setAttribute('style', "height:" + (pageHeight * .7) + "px;" + "max-height:" + (pageHeight * .7) + "px;");

    }
}

var pageno = 0;
var linkcolor = "color: #0000EE ";
document.addEventListener("DOMContentLoaded", function() {
    addListner();
    var links = document.getElementsByTagName("a");
    for (var i = 0; i < links.length; i++) {
        if (links[i].getAttribute('href')) {
            links[i].setAttribute("onclick", "onLinkClick(this,'" + links[i].href + "')");
            // Need to remove href for blocking chapter navigation of book package
            links[i].removeAttribute("href");
            links[i].setAttribute("style", linkcolor);

        }

    }

}, false);


function onImageLinkClick(event, data) {
   // console.log("onImageLinkClick called");
    if (event.getAttribute("data-dblclick") == null) {
        event.setAttribute("data-dblclick", 1);

        setTimeout(function() {
            if (event.getAttribute("data-dblclick") == 1) {

            }
            event.removeAttribute("data-dblclick");
        }, 500);
    } else {
        event.removeAttribute("data-dblclick");
        onLinkClick(event, data);
    }
}

function onLinkClick(event, data) {
    // if data have www or http or https then
    if ((data.indexOf("http:") != -1) || (data.indexOf("https:") != -1)) {

        ReflowablePageFragment.onLinkClick(true, data);
    } else if (window.location.pathname.indexOf(data) != -1) {

        ReflowablePageFragment.onLinkClick(false, data);
    } else {

        ReflowablePageFragment.onLinkClick(false, data);
    }

    return true;
}


function getRangeText(safeRanges) {
    var text = '';
    for (var i = 0; i < safeRanges.length; i++) {
        text += highlightRange(safeRanges[i], name);
    }
    return text;
}


function getOffset(el) {
    el = el.getBoundingClientRect();
    return {
        left: el.left + window.scrollX,
        top: el.top + window.scrollY
    }
}

function getAbsPosition(el) {
    var el2 = el;
    var curtop = 0;
    var curleft = 0;
    if (document.getElementById || document.all) {
        do {
            curleft += el.offsetLeft - el.scrollLeft;
            curtop += el.offsetTop - el.scrollTop;
            el = el.offsetParent;
            el2 = el2.parentNode;
            while (el2 != el) {
                curleft -= el2.scrollLeft;
                curtop -= el2.scrollTop;
                el2 = el2.parentNode;
            }
        } while (el.offsetParent);

    } else if (document.layers) {
        curtop += el.y;
        curleft += el.x;
    }
    return [curtop, curleft];
}

function remove(elem) {
    return elem.parentNode.removeChild(elem);
}

var elem;
var scrollWidth;
var horizontalInterval;
var horizontalIntervalPeriod = 1000;
var horizontalIntervalCounter = 0;
var horizontalIntervalLimit = 3000;
var needBlankPage;


function initHorizontalDirection(isPortrait, isMobile, isNeedBlankPage, isCoverPage) {
    needBlankPage = isNeedBlankPage;
    if (!isPortrait && isNeedBlankPage && isCoverPage && !isMobile) {
        updateimages()
        var pageWidth = (document.documentElement.clientWidth / 2)
        var pageHeight = document.documentElement.clientHeight
        var emptypage = document.createElement('kitaboospan');
        emptypage.setAttribute('id', 'kitabooemptypage');
        emptypage.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");

        var emptypage2 = document.createElement('kitaboospan');
        emptypage2.setAttribute('id', 'kitabooemptypage2');
        emptypage2.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
        document.body.insertBefore(emptypage, document.body.firstChild);

    } else if (isNeedBlankPage && isCoverPage && isMobile) {
        updateimages()
    }

    preInitHorizontalDirection(isPortrait, isMobile);
    postInitHorizontalDirection(isNeedBlankPage);

    horizontalInterval = setInterval(horizontalRecheck, horizontalIntervalPeriod);
}

function preInitHorizontalDirection(isPortrait, isMobile) {

    var htmlElement = document.getElementsByTagName('html')[0];
    var bodyElement = document.getElementsByTagName('body')[0];

    // Required when initHorizontalDirection() is called multiple times.
    // Currently it is called only once per page.
    htmlElement.style.width = null;
    bodyElement.style.width = null;
    htmlElement.style.height = null;
    bodyElement.style.height = null;

    var bodyStyle = bodyElement.currentStyle || window.getComputedStyle(bodyElement);
    var paddingTop = parseInt(bodyStyle.paddingTop, 10);
    var paddingRight = parseInt(bodyStyle.paddingRight, 10);
    var paddingBottom = parseInt(bodyStyle.paddingBottom, 10);
    var paddingLeft = parseInt(bodyStyle.paddingLeft, 10);
    //console.log("-> padding = " + paddingTop + ", " + paddingRight + ", " + paddingBottom + ", " + paddingLeft);

    //document.documentElement.clientWidth is window.innerWidth excluding x scrollbar width
    var pageWidth = 0;
    if (isPortrait || isMobile) {
        pageWidth = (window.innerWidth) - (paddingLeft + paddingRight);
    } else {
        pageWidth = (window.innerWidth / 2) - (paddingLeft + paddingRight);
    }

    //document.documentElement.clientHeight is window.innerHeight excluding y scrollbar height
     var pageHeight = window.innerHeight - (paddingTop + paddingBottom);

    bodyElement.style.webkitColumnGap = (paddingLeft + paddingRight) + 'px';
    bodyElement.style.webkitColumnWidth = pageWidth + 'px';
    bodyElement.style.columnFill = 'auto';
    bodyElement.style.overflowY = 'hidden';


    htmlElement.style.height = (window.innerHeight - (paddingTop + paddingBottom)) + 'px';
    bodyElement.style.height = (window.innerHeight - (paddingTop + paddingBottom)) + 'px';

   // console.log("pageHeight value" + pageHeight);
}


function postInitHorizontalDirection(isNeedBlankPage) {

    var htmlElement = document.getElementsByTagName('html')[0];
    var bodyElement = document.getElementsByTagName('body')[0];
    var bodyStyle = bodyElement.currentStyle || window.getComputedStyle(bodyElement);
    var paddingTop = parseInt(bodyStyle.paddingTop, 10);
    var paddingRight = parseInt(bodyStyle.paddingRight, 10);
    var paddingBottom = parseInt(bodyStyle.paddingBottom, 10);
    var paddingLeft = parseInt(bodyStyle.paddingLeft, 10);
    var clientWidth = window.innerWidth;
    var scrollWidth = document.documentElement.scrollWidth;

    if (scrollWidth > clientWidth &&
        scrollWidth > window.offsetWidth) {
        scrollWidth += paddingRight;
    }
    var newBodyWidth = clientWidth - (paddingLeft + paddingRight);

    window.scrollWidth = scrollWidth;

   if (isNeedBlankPage) {
       htmlElement.style.width = clientWidth + 'px';
       bodyElement.style.width = clientWidth + 'px';
   } else {
       htmlElement.style.width = newBodyWidth + 'px';
       bodyElement.style.width = newBodyWidth + 'px';
   }

    // pageCount deliberately rounded instead of ceiling to avoid any unexpected error
  //  console.log("scrollWidth value" + scrollWidth + "clientWidth" + clientWidth);

    var pageCount = Math.round(scrollWidth / clientWidth);
    var pageCountFloat = scrollWidth / clientWidth;

    if (pageCount != pageCountFloat) {
        /*console.warn("-> pageCount = " + pageCount + ", pageCountFloat = " + pageCountFloat +
            ", Something wrong in pageCount calculation");*/
    }
    ReflowablePageFragment.setHorizontalPageCount(pageCount);
}



function horizontalRecheck() {

    horizontalIntervalCounter += horizontalIntervalPeriod;

    if (window.scrollWidth != document.documentElement.scrollWidth) {
        // Rare condition
        // This might happen when document.documentElement.scrollWidth gives incorrect value
        // when the webview is busy re-drawing contents.

       /* console.warn("-> scrollWidth changed from " + window.scrollWidth + " to " +
            document.documentElement.scrollWidth);*/
        postInitHorizontalDirection(needBlankPage);
    }

    if (horizontalIntervalCounter >= horizontalIntervalLimit)
        clearInterval(horizontalInterval);
}

function bodyOrHtml() {

    if ('scrollingElement' in document) {
        return document.scrollingElement;
    }
    // Fallback for legacy browsers
    if (navigator.userAgent.indexOf('WebKit') != -1) {
        return document.body;
    }
    return document.documentElement;
}

function scrollToLast(direction) {

    var scrollingElement = bodyOrHtml();

    if (direction == "VERTICAL") {
        scrollingElement.scrollTop =
            scrollingElement.scrollHeight - document.documentElement.clientHeight;

    } else if (direction == "HORIZONTAL") {
        scrollingElement.scrollLeft =
            scrollingElement.scrollWidth - document.documentElement.clientWidth;
        ReflowableViewPager.setPageToLast();
    }
}

function jumpToPage(rangeOffset, ishrMode,isOreientationPortrait,isMobile) {

    rangeOffset = JSON.parse(rangeOffset);
    var range = restoreSelection(rangeOffset);

    var name = rangeOffset.start + "_" + rangeOffset.end;
    var text = "";

    var safeRanges = getSafeRanges(range);

    for (var i = 0; i < safeRanges.length; i++) {
        text += highlightRange(safeRanges[i], name);
    }

    var highlight = document.getElementsByName(name)[0];
    var anchorId = "ugc_" + name;
    var anchor = document.getElementById(anchorId);

    if (anchor == null) {
        anchor = document.createElement("kitaboospan");
        anchor.setAttribute("id", anchorId);
        anchor.setAttribute("name", anchorId);
        highlight.parentNode.insertBefore(anchor, highlight);
    }
    if (ishrMode) {

        var highlightTop = getAbsPosition(highlight)[0];
        var scrollingElement = bodyOrHtml();
        var clientWidth = window.innerWidth;


        if (isOreientationPortrait || isMobile) {

            var pageIndex = Math.floor(highlightTop / window.innerHeight);
            var newScrollLeft = clientWidth * (pageIndex);
            scrollingElement.scrollLeft = newScrollLeft;
            ReflowableViewPager.setCurrentPage(pageIndex);

        }else {
                var pageIndex = Math.floor(highlightTop / clientWidth);
                var newScrollLeft = clientWidth * (pageIndex - 1);
                scrollingElement.scrollLeft = newScrollLeft;
                ReflowableViewPager.setCurrentPage(pageIndex - 1);
        }
         ReflowablePageFragment.heightToMoved(highlightTop, clientWidth);
    } else {
        highlight.scrollIntoView();
    }
}

function renderAllHighlight(jsonarray, hrmode, isMobile, isOreientationPortrait) {
    var data = JSON.parse(jsonarray);
    for (var i = 0; i < data.length; i++) {
        var strtWord = data[i].mStartWordId;
        var endWord = data[i].mEndWordId;
        var range = {};
        range.start = strtWord;
        range.end = endWord;
        var noteData = data[i].mNoteData === '' ? false : true;
        var currHighlightColor = data[i].backgroundColor;

        renderHighlight(JSON.stringify(range), noteData, currHighlightColor, hrmode, isMobile, isOreientationPortrait);
    }

}

function renderHighlight(rangeOffset, hasNote, color, scrollEnabledValue, isMobile, isOreientationPortrait) {
    rangeOffset = JSON.parse(rangeOffset);
    var range = restoreSelection(rangeOffset);
    var name = rangeOffset.start + "_" + rangeOffset.end;
    var safeRanges = getSafeRanges(range);
    var txt;
    for (var i = 0; i < safeRanges.length; i++) {
        txt += highlightRange(safeRanges[i], name);
    }
    addHighlightAnchor(name);
    editHighlight(name, hasNote, color, scrollEnabledValue, isMobile, isOreientationPortrait);
}

var searchResults = [];
var lastSearchQuery = null;
var testCounter = 0;
var searchResultsInvisible = true;
var loadedUrl = "";
var mElasticTmpBackgroundColor = "";
var mSearchHighlightedColor = "";
var mSearchPosition = 0;


var searchResult;

function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}


function stopAudioVideo() {
    var audioPlayer = document.getElementsByTagName('audio')[0];
    if (audioPlayer != undefined) {
        audioPlayer.pause();
    }
    var videoPlayer = document.getElementsByTagName('video')[0];
    if (videoPlayer != undefined) {
        videoPlayer.pause();
    }
}


function restoreSelection(savedSel) {

    var charIndex = 0,
        range = document.createRange();
    range.setStart(document.body, 0);
    range.collapse(true);
    var nodeStack = [document.body],
        node, foundStart = false,
        stop = false;
    savedSel.end = savedSel.end;
    while (!stop && (node = nodeStack.pop())) {
        if (node.nodeType == 3) {
            var nextCharIndex = charIndex + node.length;
            if (!foundStart && savedSel.start >= charIndex && savedSel.start <= nextCharIndex) {
                range.setStart(node, savedSel.start - charIndex);
                foundStart = true;
            }
            if (foundStart && savedSel.end >= charIndex && savedSel.end <= nextCharIndex) {
                range.setEnd(node, savedSel.end - charIndex);
                stop = true;
            }
            charIndex = nextCharIndex;
        } else {
            var i = node.childNodes.length;
            while (i--) {
                nodeStack.push(node.childNodes[i]);
            }
        }
    }
    return range;
}

function getDocumentDetails() {
    ReflowablePageFragment.setDocDetails(document.body.scrollHeight, document.scrollingElement.scrollTop, window.innerHeight);
}



//cfi related functions

//use "getCFINodeString" for get cfinode.
//use "jumpToCFI" for jump to cfinode.
//use "isCFIContextBookmarked" for check if bookmarked on every page change

function getCFINodeString(isHrmode) {
    var currentrange;
    currentrange = document.caretRangeFromPoint(0, 0);
    var cfi = "";
    // if(currentrange != null) {
    if (currentrange.startContainer.nodeType == 3 && currentrange.startOffset != 0) {

        cfi = EPUBcfi.Generator.generateCharOffsetRangeComponent(currentrange.startContainer, currentrange.startOffset, currentrange.endContainer, currentrange.endOffset, null, null, null);

      //  console.log("Dhananjay" + 2);

    } else {
      //  console.log("Dhananjay" + 3);
        var node = getFirstVisibleNode(document.body) || document.body;

        if (node.nodeType === Node.TEXT_NODE) {
          //  console.log("Dhananjay" + 4);
            cfi = EPUBcfi.Generator.generateCharacterOffsetCFIComponent(node, 0);

        } else {

            cfi = EPUBcfi.Generator.generateElementCFIComponent(node);

        }

    }
   // console.log("Dhananjay" + 5);
    cfi = EPUBcfi.Generator.generateCompleteCFI("/0!", cfi);
    //console.log("DhananjayCuurentCFi" + cfi);
    return cfi;
    //  }

    // ReflowablePageFragment.onCFIBookMarkPageData(cfi);

}

function getViewportRect() {

    var viewportRectJSon = '{"x":0,"y":0,"width":' + window.innerWidth + ',"height":' + window.innerHeight + '}'

 /*   console.log("+window.innerWidth+" + window.innerWidth);
    console.log("+window.innerHeight+" + window.innerHeight);
    console.log("+document.documentElement.clientHeight+" + document.documentElement.clientHeight);
    console.log("+document.documentElement.clientWidth+" + document.documentElement.clientWidth);
    console.log("document.body.clientHeight" + document.body.clientHeight);
    console.log("document.body.clientWidth" + document.body.clientWidth);*/


    var viewportRect = constructDOMRect(viewportRectJSon)

    return viewportRect;

}



function constructDOMRect(rectJsonString) {

    var rectJson = JSON.parse(rectJsonString);

    return new DOMRect(rectJson.x, rectJson.y, rectJson.width, rectJson.height);

}

function getFirstVisibleNode(node) {

    var range = document.createRange();

    range.selectNode(node);

    var rect = RangeFix.getBoundingClientRect(range);

    if (rect == null)

        return null;

    var viewportRect = getViewportRect();

    var intersects = rectIntersects(viewportRect, rect);

    var contains = rectContains(viewportRect, rect);

    if (contains) {

        return node;

    } else if (intersects) {
        var childNodes = node.childNodes;

        for (var i = 0; i < childNodes.length; i++) {

            if (childNodes[i].nodeType === Node.ELEMENT_NODE || childNodes[i].nodeType === Node.TEXT_NODE) {

                var childNode = getFirstVisibleNode(childNodes[i]);

                if (childNode) {

                    return childNode;

                }

            }

        }

        return node;

    }

    return null;

}


function rectIntersects(a, b) {

    return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;

}

function rectContains(a, b) {

    return a.left < a.right && a.top < a.bottom

        &&
        a.left <= b.left && a.top <= b.top && a.right >= b.right && a.bottom >= b.bottom;

}


function isInViewRect(elem) {
   var bounding = elem.getBoundingClientRect();
   if(bounding.bottom == 0) {
     return false;
   }
   return ( bounding.top >= 0 && bounding.left >= 0 && bounding.bottom <= (window.innerHeight ||
   document.documentElement.clientHeight) && bounding.right <= (window.innerWidth || document.documentElement.clientWidth));

}
document.addEventListener('click', function(e) {
    e = e || window.event;
    var target = e.target || e.srcElement,
        text = target.textContent || target.innerText;
  //  console.log("CLICKED_CONTENT  " + text);

}, false);


var timeout;


var needBlankPageNew;

function initHorizontalDirectionUpdate(isPortrait, isMobile, isNeedBlankPage, isCoverPage,spaceRequired,fileName) {
    var marginValue=spaceRequired;
	needBlankPageNew = isNeedBlankPage;
    if (!isPortrait && isNeedBlankPage && isCoverPage && !isMobile) {

        var pageWidth = (document.documentElement.clientWidth / 2)
		pageWidth=pageWidth-spaceRequired;
        var pageHeight = document.documentElement.clientHeight
        var emptypage = document.createElement('kitaboospan');
        emptypage.setAttribute('id', 'kitabooemptypage');
        emptypage.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");

        var emptypage2 = document.createElement('kitaboospan');
        emptypage2.setAttribute('id', 'kitabooemptypage2');
        emptypage2.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
        document.body.insertBefore(emptypage, document.body.firstChild);

    } else if (isNeedBlankPage && isCoverPage && isMobile) {
        updateimages()
    }

   // preInitHorizontalDirectionUpdate(isPortrait, isMobile,marginValue);
    //postInitHorizontalDirectionUpdate(false,marginValue,isPortrait,isMobile);

    //horizontalInterval = setInterval(horizontalRecheck, horizontalIntervalPeriod);
    if (isPortrait || isMobile) {
             var requiredHeight = window.innerHeight;
             var requiredWidth = (window.innerWidth);

        } else {
           var requiredHeight = window.innerHeight;
           var requiredWidth = (window.innerWidth/2);
        }

       addCSSRuleStyle('html', 'height:' + requiredHeight + 'px; column-gap: 0px; column-width:' + requiredWidth + 'px;',fileName);
}

function preInitHorizontalDirectionUpdate(isPortrait, isMobile,marginValue) {
    var defaultSpace=40;
    var calCulatedSpace=defaultSpace+marginValue;
    var pageWidthReq=window.innerWidth / 2;
    pageWidthReq=pageWidthReq-calCulatedSpace;

    var htmlElement = document.getElementsByTagName('html')[0];
    var bodyElement = document.getElementsByTagName('body')[0];

    // Required when initHorizontalDirection() is called multiple times.
    // Currently it is called only once per page.
    htmlElement.style.width = null;
    bodyElement.style.width = null;
    htmlElement.style.height = null;
    bodyElement.style.height = null;

    var bodyStyle = bodyElement.currentStyle || window.getComputedStyle(bodyElement);
    var paddingTop = parseInt(bodyStyle.paddingTop, 10);
    var paddingRight = parseInt(bodyStyle.paddingRight, 20);
    var paddingBottom = parseInt(bodyStyle.paddingBottom, 10);
    var paddingLeft = parseInt(bodyStyle.paddingLeft, 20);
    //console.log("-> padding = " + paddingTop + ", " + paddingRight + ", " + paddingBottom + ", " + paddingLeft);

    //document.documentElement.clientWidth is window.innerWidth excluding x scrollbar width
    var pageWidth = 0;
    if (isPortrait || isMobile) {
        pageWidth = (window.innerWidth) - (calCulatedSpace);
    } else {
        pageWidth = pageWidthReq;
    }

    //document.documentElement.clientHeight is window.innerHeight excluding y scrollbar height
     var pageHeight = window.innerHeight - (paddingTop + paddingBottom);

    bodyElement.style.webkitColumnGap = (calCulatedSpace) + 'px';
    bodyElement.style.webkitColumnWidth = pageWidth + 'px';
    bodyElement.style.columnFill = 'auto';
    bodyElement.style.overflowY = 'hidden';


    htmlElement.style.height = (window.innerHeight - (paddingTop + paddingBottom)) + 'px';
    bodyElement.style.height = (window.innerHeight - (paddingTop + paddingBottom)) + 'px';

   // console.log("pageHeight value" + pageHeight);
}


function postInitHorizontalDirectionUpdate(isNeedBlankPage,marginValue,isPortrait,isMobile) {
    var calCulatedSpace=40+marginValue;
    if(isPortrait || isMobile)
         var pageWidthReq= parseInt((window.innerWidth)-calCulatedSpace);
     else
          var pageWidthReq= parseInt((window.innerWidth/2)-calCulatedSpace);

    var htmlElement = document.getElementsByTagName('html')[0];
    var bodyElement = document.getElementsByTagName('body')[0];
    var bodyStyle = bodyElement.currentStyle || window.getComputedStyle(bodyElement);
    var paddingTop = parseInt(bodyStyle.paddingTop, 10);
    var paddingRight = parseInt(bodyStyle.paddingRight, 20);
    var paddingBottom = parseInt(bodyStyle.paddingBottom, 10);
    var paddingLeft = parseInt(bodyStyle.paddingLeft, 20);
    var clientWidth = window.innerWidth;
    var scrollWidth = document.documentElement.scrollWidth;

    /*if (scrollWidth > clientWidth &&
        scrollWidth > window.offsetWidth) {
        scrollWidth += paddingRight;
    }*/
    //var newBodyWidth = pageWidthReq;

    //window.scrollWidth = scrollWidth;

   if (isNeedBlankPage) {
       htmlElement.style.width = pageWidthReq + 'px';
       bodyElement.style.width = pageWidthReq + 'px';
   }else {
      htmlElement.style.width = pageWidthReq + 'px';
      bodyElement.style.width = pageWidthReq + 'px';
   }

    var pageCount = Math.round(scrollWidth / (pageWidthReq));
    var pageCountFloat = scrollWidth / pageWidthReq;

    ReflowablePageFragment.setHorizontalPageCount(pageCount);
}


function addCSSRuleStyle(selector, newRule,fileName)
{

   var stylesheetLength = document.styleSheets.length;
   var stylesheetLastEle = stylesheetLength-1;
   var mySheet = document.styleSheets[stylesheetLastEle];
   if (mySheet != undefined)
   {
       if (mySheet.addRule)
       {
           if(mySheet.cssRules)
           {
               for (var i = 0; i < mySheet.cssRules.length; i++)
               {
                   if (mySheet.cssRules[i].selectorText === 'html')
                   {
                       mySheet.removeRule(i);
                   }
               }
           }
           mySheet.addRule(selector, newRule);
       } else
       {
           ruleIndex = mySheet.cssRules.length;
           mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
       }
   } else
   {
       console.log('stylesheet not available');
   }

       var clientWidth = window.innerWidth;
              var scrollWidth = document.documentElement.scrollWidth;

           var pageCount = Math.round(scrollWidth / clientWidth);
              var pageCountFloat = scrollWidth / clientWidth;

        updateimages();
        updatetables();

        var width_ = window.scrollX;

        if(ReflowablePlayerView != undefined)
                       ReflowablePlayerView.loadNextChapter(width_);

}


function getPages(fileName)
{

       var clientWidth = window.innerWidth;
              var scrollWidth = document.documentElement.scrollWidth;

           var pageCount = Math.round(scrollWidth / clientWidth);
              var pageCountFloat = scrollWidth / clientWidth;

       ReflowablePageFragment.setPageCount(pageCount,fileName);
       clearSearch();
}


var cumulativeOffset = function(element) {
    var top = 0,
        left = 5;
    return {
        top: element.getBoundingClientRect().top,
        left: element.getBoundingClientRect().x
    };
};


function setFontSizeonDummyWebview(cls) {
    var elm = document.documentElement;
    removeClass(elm, "textSizeOne");
    removeClass(elm, "textSizeTwo");
    removeClass(elm, "textSizeThree");
    removeClass(elm, "textSizeFour");
    removeClass(elm, "textSizeFive");
    addClass(elm, cls);

}







