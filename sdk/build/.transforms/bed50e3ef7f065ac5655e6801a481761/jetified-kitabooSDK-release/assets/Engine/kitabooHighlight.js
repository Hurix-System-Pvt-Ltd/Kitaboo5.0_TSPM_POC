/* Document selection listener */
var exactWidth = 0;
var exactHeight = 0;
var pagecount = 0;
var scalePercentage =1;

/** The last point touched by the user. { 'x': xPoint, 'y': yPoint } */
lastTouchPoint = null;

function onSelectionStart() {
    window.location = "onselectionstart:selectionstart";
}

//Change reader background color
function changeBackgroundColor(backgroundColor) {
    addCSSRule('html', "background-color: " + backgroundColor);
    addCSSRule('body', "background-color: " + backgroundColor);
    var body = document.body;
    body.setAttribute("style", "background-color: " + backgroundColor);
}

//Change reader font color
function changeTextColor(color) {
    addCSSRule('html', 'color: ' + color);
    addCSSRule('body', 'color: ' + color);
}

window.onresize = function() {
    setTimeout(function() {}, 5000);
};
var currentTop;
window.onscroll = function() {
    clearSelection();
    ptr.onFlingCalled();
    currentTop = window.scrollY;
    //console.log("scrollY value" + currentTop);
};


//Add text selection listener
function addListner() {
    var body = document.body;
    body.setAttribute("onselectionchange", "onSelectionChange()");
}

//Remove text selection listener
function removeListener() {
    var body = document.body;
    body.setAttribute("onselectionchange", "");
}

function hrViewmode() {
    width = window.innerWidth;
    height = window.innerHeight;
    //addCSSRule('html', 'height: ' + height + 'px; -webkit-column-gap: 0px;-moz-column-fill: auto; column-fill: auto; -webkit-column-width: ' + width + 'px;-moz-column-width: '+width+'px; -epub-writing-mode:horizontal-tb;-webkit-wrting-mode:horizontal-tb; -webkit-column-rule: 0px outset #f1f1f1; -moz-column-rule: 0px outset #f1f1f1; column-rule: 0px outset #f1f1f1;');

    getPageCount();
}

function addhrmode() {
    width = window.innerWidth;
    height = window.innerHeight;
    window.scrollTo(0, 0);
    removeallstickynotes();
    pagecount = Math.ceil(document.body.scrollHeight / window.innerHeight);
    //addCSSRule('html', 'height: '+height+'px;-webkit-column-count :'+pagecount+'');
    updatesticksHr();
}


function verticleViewMode(width) {
    removeallstickynotes();
    //addCSSRule('html', 'height: 0px; width:'+window.innerWidth+'px;overflowX:hidden; -webkit-column-width: '+width +'px;');
    updatesticksVr();

}

function getPageCountForVertical() {
    var newwidth = document.body.clientWidth;
    ptr.pageCount(0);
}

function getPageCount() {
    var newwidth = window.innerWidth;
    //console.log("new width" + newwidth);
    var count = (newwidth / clicntwidth);
    ptr.jsError(pagecount);
    ptr.pageCount(pagecount);
}

function getPages() {
    var newwidth = document.body.scrollWidth;
   // console.log("new width" + newwidth);
    var count = (newwidth / clicntwidth);

}

function stringRet(isHrmode) {
    var top = window.scrollY;
    var left = window.scrollX;
    var lastvistedpageobject = currentPosition(isHrmode);
    ptr.lastVisitedPageData(lastvistedpageobject, top, left);
}

function currentPositionForBookMark() {
    var path = "";
    var lastvistedpageobject = currentPositionBookMark();
    ptr.bookmarkPageData(lastvistedpageobject);

}

function restorePagePosition() {
    var top = window.pageYOffset;
    var left = document.body.offsetLeft;
   /* console.log("Y position:" + top);
    console.log("X position:" + left);*/
    ptr.restorePagePosition(top, left);
}


function getPagePosition(pagelocation) {
    var rangeobj = JSON.parse(pagelocation);
   // console.log("restorePage called" + rangeobj.Y);
    setTimeout(function() {
        window.scrollTo(0, rangeobj.Y);;
    }, 500);

}

function restorePage(oldjson, isHrmode) {
    //console.log("restorePage called" + oldjson);
    var obj = oldjson;
   // console.log("oldjson" + obj);
    restorePosition(obj, isHrmode);
}


function addTwoPages() {
    var clientheight = document.body.clientHeight;
    clicntwidth = (clicntwidth / 2);
    // addCSSRule('html', 'height: '+clientheight+'px; -webkit-column-gap: 0px; -webkit-column-width: '+clicntwidth+'px; -epub-writing-mode:horizontal-tb; -webkit-wrting-mode:horizontal-tb;overflow: auto;overflow-y: hidden;');
    setTimeout(function() {
        getPageCount();
    }, 100);

}

window.onload = function() {
    exactWidth = document.body.offsetWidth;
    exactHeight = document.body.offsetHeight;
    clicntwidth = document.body.clientWidth;
    pagecount = Math.ceil(document.body.scrollHeight / window.innerHeight);
    ptr.onPageLoaded();
    addListner();



}

window.onresize = function() {
    exactWidth = document.body.offsetWidth;
    exactHeight = document.body.offsetHeight;
    clicntwidth = document.body.clientWidth;
    pagecount = Math.ceil(document.body.scrollHeight / window.innerHeight);
    ptr.onPageLoaded();
    // ptr.pageFinishAfterConfiguration();
    // addListner();
}

function updateimages(path) {
    var images = document.getElementsByTagName('img');
    for (var i = 0; i < images.length; i++) {
        var oldpath = images[i].getAttribute('src');
        images[i].setAttribute("src", path + oldpath);
       // console.log("image path " + oldpath + " new path " + path + oldpath)
    }
}


function visibleTextNodes() {
    var walker = document.createTreeWalker(document.body,NodeFilter.SHOW_TEXT ,function(node) {
            if (node.nodeType == node.TEXT_NODE) {
                return NodeFilter.FILTER_ACCEPT;
            }  else {
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

function doSearch(needle, keyword, colorObject) {

    var bolean = removeAllSearchElelemt(document.body);
    needle = needle.replace(/\s/g, '').toLowerCase();

    var textNodes = visibleTextNodes();

    for (var i = 0, texts = []; i < textNodes.length; i++) {
        texts.push(textNodes[i].nodeValue.replace(/\s/g, '').toLowerCase());
    }

    var matchStart = texts.join('').indexOf(needle);
    if (matchStart < 0) {
        return false;
    }

    var nodeAndOffsetAtPosition = function(position) {
        for (var i = 0, consumed = 0; consumed + texts[i].length < position; i++) {
            consumed += texts[i].length;
        }
        var whitespacePrefix = textNodes[i].nodeValue.match(/^\s*/)[0];
        return [textNodes[i], position - consumed + whitespacePrefix.length];
    };

    var range = document.createRange();
    range.setStart.apply(range, nodeAndOffsetAtPosition(matchStart));
    range.setEnd.apply(range, nodeAndOffsetAtPosition(matchStart + needle.length));
    window.getSelection().removeAllRanges();


    var safeRanges = getSafeRanges(range);
    for (var i = 0; i < safeRanges.length; i++) {
        var element = document.createElement('kitaboospan');
        element.setAttribute('class', 'tempsearch');
        safeRanges[i].surroundContents(element);
    }


    var getAll = document.getElementsByClassName('tempsearch');

    for (var i = 0; i < getAll.length; i++) {
        // console.log('Search text '+ getAll[i].innerHTML);
        var text = getAll[i].innerHTML;

        var searchMask = keyword;
        var regEx = new RegExp(searchMask, "ig");
        var replaceMask = '<kitaboospan class="searchhighlight" style="background:'+colorObject+' !important;color: white !important;">' + keyword + '</kitaboospan>';

        var result = text.replace(regEx, replaceMask);


        getAll[i].innerHTML = result;
    }

    var searchele = getAll[0];
   // console.log("searchele" + searchele);
    searchele.scrollIntoView();

    setTimeout(function() {
        removeAllSearchElelemt(document.body);
    }, 10000 * 10);
    cleareSelection();

}

function doElasticSearch(needle, keyword, colorObject,highlightedNodeIndex) {

    var bolean = removeAllElasticSearchElelemt(document.body);
    needle = needle.replace(/\s/g, '').toLowerCase();

    var textNodes = visibleTextNodes();

    for (var i = 0, texts = []; i < textNodes.length; i++) {
        texts.push(textNodes[i].nodeValue.replace(/\s/g, '').toLowerCase());
    }

    var matchStart = texts.join('').indexOf(needle);
    if (matchStart < 0) {
        return false;
    }

    var nodeAndOffsetAtPosition = function(position) {
        for (var i = 0, consumed = 0; consumed + texts[i].length < position; i++) {
            consumed += texts[i].length;
        }
        var whitespacePrefix = textNodes[i].nodeValue.match(/^\s*/)[0];
        return [textNodes[i], position - consumed + whitespacePrefix.length];
    };

    var range = document.createRange();
    range.setStart.apply(range, nodeAndOffsetAtPosition(matchStart));
    range.setEnd.apply(range, nodeAndOffsetAtPosition(matchStart + needle.length));
    window.getSelection().removeAllRanges();


    var safeRanges = getSafeRanges(range);
   // console.log("SafeRanges " + safeRanges.length);
    for (var i = 0; i < safeRanges.length; i++) {
        var element = document.createElement('kitaboospanAmit');
        element.setAttribute('class', 'tempsearchAmit');
        safeRanges[i].surroundContents(element);
    }


    var getAll = document.getElementsByClassName('tempsearchAmit');
//console.log("getAll.length " + getAll.length);
    for (var i = 0; i < getAll.length; i++) {
        // console.log('Search text '+ getAll[i].innerHTML);
        var text = getAll[i].innerHTML;

        var searchMask = keyword;
        var regEx = new RegExp(searchMask, "ig");
         // console.log("searchhighlightedindex " + highlightedNodeIndex+" "+i);
        if(highlightedNodeIndex==i){
               var replaceMask = '<kitaboospan class="searchhighlight" style="background: #00ff00 !important;color: white !important;">' + keyword + '</kitaboospan>';
        }else{
        var replaceMask = '<kitaboospan class="searchhighlight" style="background: #0066ff !important;color: white !important;">' + keyword + '</kitaboospan>';
        }

        var result = text.replace(regEx, replaceMask);


        getAll[i].innerHTML = result;
    }

    var searchele = getAll[0];
   // console.log("searchele" + searchele);
    searchele.scrollIntoView();

    setTimeout(function() {
        removeAllSearchElelemt(document.body);
    }, 10000 * 10);
    cleareSelection();

}

function clearSearch() {
    var bolean = removeAllSearchElelemt(document.body);
}
function clearElasticSearch() {
    var bolean = removeAllElasticSearchElelemt(document.body);
}

function getVRPageNo() {
    var pageno = document.body.scrollHeight / document.body.clientHeight;
    var newpae = pageno * window.pageYOffset;
   // console.log('page no : ' + pageno + " new " + newpae + " " + document.body.scrollHeight + ":" + window.pageYOffset);
}


function getHRPageNo() {
    var pageno = document.body.scrollWidth / document.body.clientWidth;
    var newpae = pageno * window.pageXOffset;
    //console.log('page no : ' + pageno + " newpae " + newpae + " " + document.body.scrollWidth + ":" + window.pageXOffset);
}
var pageno = 0;

function next(pageno, isfixed) {
    if (isfixed == 1) {
        ptr.NextChapter();
    } else {
        clearSelection();
        var pageCont = Math.ceil(parseInt(document.body.scrollHeight) / window.innerHeight);
       // console.log('pageCont: ' + pageCont);
        var CurrCont = Math.ceil(parseInt(window.scrollY + window.innerHeight) / window.innerHeight);
       // console.log('CurrCont: ' + CurrCont);
        if (CurrCont == pageCont) {
            /*console.log('CurrCont: ' + CurrCont);
            console.log('CurrCont: ' + pageCont);*/
            ptr.NextChapter();
        } else {
            window.scrollBy(0, window.innerHeight);
        }
    }
}

function PrevPageScroll(PrevChapter) {
    if (PrevChapter) {
        //var pageCont = Math.ceil(parseInt(document.body.scrollHeight)/window.innerHeight);
        window.scrollTo(0, document.body.scrollHeight);
    }

}

function NextPageScroll() {
    window.scrollTo(0, 0);
}

function previous(pageno, isfixed) {
    if (isfixed == 1) {
        ptr.PrevChapter();
    } else {
        clearSelection();
        if (window.scrollY == 0) {
            ptr.PrevChapter();
        } else {
            window.scrollBy(0, -window.innerHeight);
        }
    }
}

function setPageno(no) {
    pageno = no;
}

function jumptopage(pageno) {
   // console.log("scrollwidth value" + document.body.offsetWidth);
    var scrollwidth = (document.body.offsetWidth * pageno);
    window.scrollTo(scrollwidth, 0);
}

function jumptopagelast(pageno) {
   // console.log("scrollwidth val" + pageno);
    var scrollwidth = (document.body.offsetWidth * pageno);
    window.scrollTo(scrollwidth, 0);
}

function cleareSearch() {
    removeAllSearchElelemt(document.body);
}

function clearSelection() {
   if (window.getSelection) window.getSelection().removeAllRanges();
       else if (document.selection) document.selection.empty();
}

document.addEventListener("DOMContentLoaded", function() {
    var links = document.getElementsByTagName("a");

    for (var i = 0; i < links.length; i++) {
        links[i].setAttribute("onclick", "onLinkClick(this,'" + links[i].getAttribute("href") + "')");
        // links[i].removeAttribute("href");
    }

    var images = document.getElementsByTagName("img");
    for (var i = 0; i < images.length; i++) {
        images[i].setAttribute("onclick", "onImageLinkClick(this,'" + images[i].getAttribute("src") + "')");
    }

}, false);

function onImageLinkClick(event, data) {
  //  console.log("onImageLinkClick called" + data);

    if (event.getAttribute("data-dblclick") == null) {
      //  console.log("if : onImageLinkClick called");
        event.setAttribute("data-dblclick", 1);
        //alert('double click');
        setTimeout(function() {
            if (event.getAttribute("data-dblclick") == 1) {

            }
            event.removeAttribute("data-dblclick");
        }, 500);
    } else {
     //   console.log("else : onImageLinkClick called");
        event.removeAttribute("data-dblclick");
        onLinkClick(event, data);
    }
}


function onLinkClick(event, data) {
    if ((data.indexOf("tel:") != -1) || (data.indexOf("mailto:") != -1)) {
        ptr.onLinkClick(true, data);
    } else {
       // console.log("image links called" + data);
        ptr.onLinkClick(false, data);
    }
    // if data have www or http or https then
    /* if((data.indexOf("http:") != -1 )|| (data.indexOf("https:") != -1)){
        console.log("weblinks called"+data);
        ptr.onLinkClick(false,data);
     }
     else if (window.location.pathname.indexOf(data) != -1){
        console.log("TOC links called"+data);
        ptr.onLinkClick(false,data);
     }
     else{
        console.log("image links called"+data);
        ptr.onLinkClick(false,data);
     }*/
    //ptr.onLinkClick(true,data);
    //else if have window.location.pathname.includes(data)
    //them ptr.onLinkClick(false,data);
    //else
    //them ptr.onLinkClick(false,data);
    //event.stopImmediatePropagation();
    return true;
}


function onSelectionChange() {
   // console.log("onSelectionChange_ called");
    var selection = window.getSelection();
    if (selection) {
      //  console.log("rangecount" + selection.rangeCount);
        if (selection.rangeCount != 0)

        {
            var range = selection.getRangeAt(0);
            if (!(range.startContainer == range.endContainer && range.startOffset == range.endOffset)) {
                var rect = range.getBoundingClientRect();
                if (rect.width != 0 && rect.height != 0) {
                    var clonedSelection = range.cloneContents();
                    ptr.selectionRect(clonedSelection.textContent.replace(/[\n\t\r]/g, ""), rect.left, rect.top, rect.right, rect.bottom, rect.width, rect.height);
                    //window.TextHighlight.selectionChanged(clonedSelection.textContent.replace(/[\n\t\r]/g, ""), rect.left, rect.top, rect.right, rect.bottom, rect.width, rect.height)
                   // console.log("selectionRect:-" + rect.left + rect.top + rect.right + rect.bottom + rect.width + rect.height);
                }
            }
        }
    }
}
/*function onSelectionChange() {
        var sel = window.getSelection();
        if (!sel) {
            return;
        }
        var range = sel.getRangeAt(0);

        var rect = range.getBoundingClientRect();

        // Add spans to the selection to get page offsets
        var selectionStart = $("<span id=\"selectionStart\">&#xfeff;</span>");
        var selectionEnd = $("<span id=\"selectionEnd\"></span>");

        var startRange = document.createRange();
        startRange.setStart(range.startContainer, range.startOffset);
        startRange.insertNode(selectionStart[0]);

        var endRange = document.createRange();
        endRange.setStart(range.endContainer, range.endOffset);
        endRange.insertNode(selectionEnd[0]);

        */
/*var handleBounds = "{'left': " + (selectionStart.offset().left) + ", ";
        handleBounds += "'top': " + (selectionStart.offset().top + selectionStart.height()) + ", ";
        handleBounds += "'right': " + (selectionEnd.offset().left) + ", ";
        handleBounds += "'bottom': " + (selectionEnd.offset().top + selectionEnd.height()) + "}";*/
/*



        var clonedSelection = range.cloneContents();
        window.TextHighlight.selectionChanged(clonedSelection.textContent.replace(/[\n\t\r]/g, ""),
         selectionStart.offset().left,
         selectionStart.offset().top,
         selectionEnd.offset().left,
         selectionEnd.offset().top, rect.width, rect.height)

         // Pull the spans
         selectionStart.remove();
         selectionEnd.remove();

         // Reset range
         sel.removeAllRanges();
         sel.addRange(range);

}*/

//@@@@@@@@@@@@
//    Returns selection range object
//    Exp: {start: 10, end: 20}
//    start: start offset of range
//    end: end offset of range
//@@@@@@@@@@@@

function getSelectionRange() {
   // console.log("getSelectionRange called")
    var range = window.getSelection().getRangeAt(0);
    var preSelectionRange = range.cloneRange();
    preSelectionRange.selectNodeContents(document.body);
    preSelectionRange.setEnd(range.startContainer, range.startOffset);
    var start = preSelectionRange.toString().length;
    var string = JSON.stringify({
        start: start,
        end: start + range.toString().length
    });
    ptr.selectionRange(string);
}

function getSelectionText() {
    var range = window.getSelection().getRangeAt(0);
    var preSelectionRange = range.cloneRange();
    var text = range.cloneContents().textContent;
    ptr.selectionText(text);
    //return text;
    ptr.onSelectionText(text);
}

function getSelectionRangeForNote() {
    var range = window.getSelection().getRangeAt(0);
    var preSelectionRange = range.cloneRange();
    preSelectionRange.selectNodeContents(document.body);
    preSelectionRange.setEnd(range.startContainer, range.startOffset);
    var start = preSelectionRange.toString().length;
    var end = start + range.toString().length;
    var highlightText = range.cloneContents().textContent;
    /*console.log("Start :" + start);
    console.log("End :" + end);*/
    var string = JSON.stringify({
        start: start,
        end: end
    });
    ptr.selectionRangeForNote(string, highlightText);
}

function getRangeText(safeRanges) {
    var text = '';
    for (var i = 0; i < safeRanges.length; i++) {
        text += highlightRange(safeRanges[i], name);
    }
    return text;
}


function getHighlightedText(rangeOffset) {
    rangeOffset = JSON.parse(rangeOffset);
    var range = restoreSelection(rangeOffset);
    var highlightText = range.cloneContents().textContent;
    ptr.highlightedTextForNote(highlightText);
}

//@@@@@@@@@@@@
//    Returns range object based on provided start offset and end offset
//@@@@@@@@@@@@

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
    var sel = window.getSelection();
    sel.removeAllRanges();
    //    sel.addRange(range);
}


//@@@@@@@@@@@@
//    Add highlight at specified range
//@@@@@@@@@@@@

function addHighlight(rangeOffset) {
    rangeOffset = JSON.parse(rangeOffset);
    var range = restoreSelection(rangeOffset);
    var name = rangeOffset.start + "_" + rangeOffset.end;
    var text = "";

    var safeRanges = getSafeRanges(range);
    for (var i = 0; i < safeRanges.length; i++) {
        text += highlightRange(safeRanges[i], name);
    }
    addHighlightAnchor(name);
    ptr.textHighlighted(text);
    cleareSelection();
}

//@@@@@@@@@@@@
//    Change Highlight Color
//@@@@@@@@@@@@

function editHighlight(name, hasNote, color, scrollEnabledValue, isfixedlayout) {
    var highlights = document.getElementsByName(name);

    for (var i = 0; i < highlights.length; i++) {
        var highlight = highlights[i];
        removeClass(highlight, "normalHighlight");
        removeClass(highlight, "importantHighlight");
        removeClass(highlight, "sharedHighlight");
        addClass(highlight, color);
    }


    if (hasNote) {
        addNoteOnHighlight(name, color, scrollEnabledValue, isfixedlayout);
    }
}
var impColor = "";
function editCustomHighlightHr(object, hasNote, color, scrollEnabledValue, isfixedlayout) {
   /* console.log("editCustomHighlight");
    console.log(JSON.stringify(object));*/
    var highlights = document.getElementsByName(object.name);
    var styleText;
    if (object.font_style == undefined) {
        styleText = "background-color:" + object.backgroundColor + "!important;";
    } else if (object.font_style == "BOLD") {
        styleText = "font-weight:bold";
    } else if (object.font_style == "ITALIC") {
        styleText = "font-style: italic";
    } else if (object.font_style == "UNDERLINE") {
        styleText = "border-bottom: 3px solid black";
    }
    for (var i = 0; i < highlights.length; i++) {
        var highlight = highlights[i];
        highlight.setAttribute('style', styleText);
        /*removeClass(highlight, "normalHighlight");
               removeClass(highlight, "importantHighlight");
               removeClass(highlight, "sharedHighlight");
               addClass(highlight, color);*/
    }
    if (hasNote) {
       // console.log("editCustomHighlighthasNote ");
        addNoteOnHighlightHr(object.name, color, scrollEnabledValue, isfixedlayout, object.backgroundColor);
    }
    ptr.highlightDrawCompleted();
}
function editCustomHighlight(object, hasNote, color, scrollEnabledValue, isfixedlayout) {
   /* console.log("editCustomHighlight");
    console.log(JSON.stringify(object));*/
    var highlights = document.getElementsByName(object.name);
    var styleText;
    if (object.font_style == undefined) {
        styleText = "background-color:" + object.backgroundColor + "!important; opacity : 0.5;";
    } else if (object.font_style == "BOLD") {
        styleText = "font-weight:bold";
    } else if (object.font_style == "ITALIC") {
        styleText = "font-style: italic";
    } else if (object.font_style == "UNDERLINE") {
        styleText = "border-bottom: 3px solid black";
    }
    for (var i = 0; i < highlights.length; i++) {
        var highlight = highlights[i];
        highlight.setAttribute('style', styleText);
        /*removeClass(highlight, "normalHighlight");
               removeClass(highlight, "importantHighlight");
               removeClass(highlight, "sharedHighlight");
               addClass(highlight, color);*/
    }
    if (hasNote) {
      //  console.log("editCustomHighlighthasNote ");
        addNoteOnHighlight(object.name, color, scrollEnabledValue, isfixedlayout, object.backgroundColor);
    }
    ptr.highlightDrawCompleted();
}

function editCustomHighlightCurrent(object, hasNote, color, scrollEnabledValue, isfixedlayout,isMobile,isOreientationPortrait) {

    var highlights = document.getElementsByName(object.name);
    var styleText;
    if (object.font_style == undefined) {
        styleText = "background-color:" + object.backgroundColor + "!important; opacity : 0.5;";
    } else if (object.font_style == "BOLD") {
        styleText = "font-weight:bold";
    } else if (object.font_style == "ITALIC") {
        styleText = "font-style: italic";
    } else if (object.font_style == "UNDERLINE") {
        styleText = "border-bottom: 3px solid black";
    }
    for (var i = 0; i < highlights.length; i++) {
        var highlight = highlights[i];
        highlight.setAttribute('style', styleText);
    }
    if (hasNote) {
        addNoteOnHighlightCurrent(object.name, color, scrollEnabledValue, isfixedlayout, object.backgroundColor,isMobile,isOreientationPortrait);
    }
    ptr.highlightDrawCompleted();
}
//@@@@@@@@
//Custom highlight which is used in SDK
function createHighlightElement(color, bgColor) {
    try {
        var element = document.createElement('span');
        var styletext = "color:" + color + " ;background-color:" + bgColor + ";";

      //  console.log(styletext);

        element.setAttribute('style', styletext);
        return element;
    } catch (err) {
       // console.log("createHighlightElement error : " + err)
    }
}

//Add highlight on page with different colors set by client
function addHighlightOnPage(object) {
    try {
        var selection = window.getSelection();
        var range = selection.getRangeAt(0);

        var highlightElement = createHighlightElement(object.textColor, object.backgroundColor);
        highlightElement.setAttribute('onclick', "kitabooHighlightTapped(this)");

        var safeRange = getSafeRanges(range);
       // console.log("safeRange length : " + safeRange.length);

        for (var i = 0; i < safeRange.length; i++) {
            safeRange[i].surroundContents(highlightElement);
        }
    } catch (err) {
       // console.log("addHighlightOnPage error : " + err)
    }
}
//@@@@@@@@




//@@@@@@@@@@@@
//    Delete or unwrap all highlight spans having given name attribute
//@@@@@@@@@@@@

function clearHighlight(name) {
    //console.log("needToClearData:" + name)
    // console.log("ClearData:" + name)
    var highlights = document.getElementsByName(name);
    //console.log("highlightsClearData:" + highlights)

    while (highlights.length) {
        var highlight = highlights[0];
        var parent = highlight.parentNode;
        while (highlight.firstChild) {
            parent.insertBefore(highlight.firstChild, highlight);
        }
        parent.removeChild(highlight);

    }

    deleteHighlightAnchor(name);
    deleteNoteOnHighlight(name);
}

//@@@@@@@@@@@@
//    Add note on page
//@@@@@@@@@@@@

function getOffset(el) {
    el = el.getBoundingClientRect();
    return {
        left: el.left + window.scrollX,
        top: el.top + window.scrollY
    }
}

function getAbsPosition(el) {
    var el2 = el;
    var bodyRect = document.body.getBoundingClientRect(),
        elemRect = el2.getBoundingClientRect(),
        offset   = elemRect.top+elemRect.height;
         var curtop = offset;
            var curleft = elemRect.left;
   /* var curtop = 0;
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
    }*/
    return [curtop, curleft];
}

function addNoteOnHighlight(name, coloris, ishorizontalview, isfixed, impColor) {
    var color = "#F4D631";
    if (impColor == "") {
        if (coloris == "normalHighlight") {
            color = "#F4D631";
        } else if (coloris == "importantHighlight") {
            color = "#F06666";
        } else if (coloris == "sharedHighlight") {
            color = "#92cadc";
        } else {
            color = "#F4D631";
        }
    } else {
        color = impColor;
    }


    // console.log("add Note on Highlight "+ishorizontalview);
    var highlight = document.getElementsByName(name)[0];
    var noteId = "note_" + name;
    var kitabooNote = document.getElementById(noteId);
    try {
        if (kitabooNote != null) {
            kitabooNote.parentNode.removeChild(kitabooNote);
        }
        kitabooNote = document.createElement("kitabooNote");
        kitabooNote.setAttribute('id', noteId);
        kitabooNote.innerText = "V";
        kitabooNote.setAttribute('onclick', "kitabooNoteTapped(event)");
        var highlightOffSet = getAbsPosition(highlight)[0];

        var pageNumber = Math.ceil(parseInt(highlightOffSet) / window.clientHeight);

        if (false) {
            // console.log("is horizontal ");
            var highlightTop = getAbsPosition(highlight)[0];
            var highlightLeft = "5";
            var highlightOffSet = parseInt(highlightTop);
            var docWidth = window.innerWidth;
            var docHeight = window.innerHeight;
            var leftOffSet = 5;
            var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
            var leftMargin = ((pageNumber - 1) * docWidth) + leftOffSet;
            //   console.log("Page No."+pageNumber);
            var topMargin = parseInt(highlightOffSet - ((pageNumber - 1) * docHeight));
            kitabooNote.setAttribute('style', "text-align:center;padding:5px;vertical-align:middle;font-family:kitabooread !important;position: absolute;margin-left:" + leftMargin + "px;font-size:37px;background-color:" + color + ";display: block;top:" + topMargin + "px");



        } else {
            // console.log("is not horizontal ");

                       var highlightTop1 = window.pageYOffset;
                       var highlightTop2 = highlight.getBoundingClientRect().top;
                       var highlightTop=highlightTop1+highlightTop2;
                        var highlightOffSet = parseInt(highlightTop);
                        var leftOffSet = 5;
                        var left = leftOffSet;
                        color=color+"!important;display:initial;"
                        if (window.innerHeight > 600) {

                           // console.log("window.innerHeight > 600 color " + color);
                            kitabooNote.setAttribute('style', "font-size: 18px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + left/scalePercentage + "px;font-size:30px;background-color:" + color + ";display: block;top:" + highlightTop/scalePercentage + "px");
                            //  console.log("window.innerHeight-val " + left+highlightTop);
                        } else {

                           kitabooNote.setAttribute('style', "font-size: 18px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + left/scalePercentage + "px;font-size:27px;background-color:" + color + ";display: block;top:" + highlightTop/scalePercentage + "px");
                        }


        }

        document.body.appendChild(kitabooNote);
        return "{\"name\":" + kitabooNote + "}";
    } catch (err) {}

}

function addNoteOnHighlightCurrent(name, coloris, ishorizontalview, isfixed, impColor,isMobile,isOreientationPortrait) {
    var color = "#F4D631";
    if (impColor == "") {
        if (coloris == "normalHighlight") {
            color = "#F4D631";
        } else if (coloris == "importantHighlight") {
            color = "#F06666";
        } else if (coloris == "sharedHighlight") {
            color = "#92cadc";
        } else {
            color = "#F4D631";
        }
    } else {
        color = impColor;
    }


    // console.log("add Note on Highlight "+ishorizontalview);
    var highlight = document.getElementsByName(name)[0];
    var noteId = "note_" + name;
    var kitabooNote = document.getElementById(noteId);
    try {
        if (kitabooNote != null) {
            kitabooNote.parentNode.removeChild(kitabooNote);
        }
        kitabooNote = document.createElement("kitabooNote");
        kitabooNote.setAttribute('id', noteId);
        kitabooNote.innerText = "V";
        kitabooNote.setAttribute('onclick', "kitabooNoteTapped(event)");
        var highlightOffSet = highlight.getBoundingClientRect().top;

        var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);

        if (false) {
            // console.log("is horizontal ");
            var highlightTop = getAbsPosition(highlight)[0];
            var highlightLeft = "5";
            var highlightOffSet = parseInt(highlightTop);
            var docWidth = window.innerWidth;
            var docHeight = window.innerHeight;
            var leftOffSet = 5;
            var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
            var leftMargin = ((pageNumber - 1) * docWidth) + leftOffSet;
            //   console.log("Page No."+pageNumber);
            var topMargin = parseInt(highlightOffSet - ((pageNumber - 1) * docHeight));
            kitabooNote.setAttribute('style', "text-align:center;padding:5px;vertical-align:middle;font-family:kitabooread !important;position: absolute;margin-left:" + leftMargin + "px;font-size:37px;background-color:" + color + ";display: block;top:" + topMargin + "px");



        } else {
            // console.log("is not horizontal ");

                       var highlightTop1 = window.pageYOffset;
                       var highlightTop2 = highlight.getBoundingClientRect().top;
                       var highlightTop=highlightTop1+highlightTop2-9;
                        var highlightOffSet = parseInt(highlightTop);
                        var leftOffSet = 5;
                        var left = leftOffSet;
                        color=color+"!important;display:initial;"
                        var scalePercentage=1;
                        if(document.body.clientHeight<window.innerHeight && !isMobile){
                          scalePercentage= window.outerHeight/document.body.clientHeight;
                        }
                        else if(!isMobile){
                                                scalePercentage= document.body.clientHeight/window.innerHeight;
                                               }
                        else if(document.body.clientHeight<window.innerHeight || isMobile){
                        scalePercentage= document.body.clientHeight/window.innerHeight;
                        }


                        /*if(isMobile && isOreientationPortrait){
                        if(window.outerHeight>window.innerHeight){
                         scalePercentage=window.outerHeight/window.innerHeight;
                        }else{
                          scalePercentage=1;
                        }
                        }else{
                        scalePercentage=1;
                        }*/
                        if (window.innerHeight > 600) {

                           // console.log("window.innerHeight > 600 color " + color);
                            kitabooNote.setAttribute('style', "font-size: 18px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + left + "px;font-size:30px;background-color:" + color + ";display: block;top:" + highlightTop*scalePercentage + "px");
                            //  console.log("window.innerHeight-val " + left+highlightTop);
                        } else {

                           kitabooNote.setAttribute('style', "font-size: 18px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + left + "px;font-size:27px;background-color:" + color + ";display: block;top:" + highlightTop*scalePercentage + "px");
                        }


        }

        document.body.appendChild(kitabooNote);
        return "{\"name\":" + kitabooNote + "}";
    } catch (err) {}


    /* console.log("addNoteOnHighlight---values"+name+ coloris+ ishorizontalview+isfixed);
         var color = "#F4D631";
         if (coloris == "normalHighlight") {
             color = "#F4D631";
         } else if (coloris == "importantHighlight") {
             color = "#F06666";
         } else if (coloris == "sharedHighlight") {
             color = "#92cadc";
         } else {
             color = "#F4D631";
         }
         var highlight = document.getElementsByName(name)[0];
         var noteId = "note_" + name;
         var kitabooNote = document.getElementById(noteId);
         try {
             if (kitabooNote != null) {
                 kitabooNote.parentNode.removeChild(kitabooNote);
             }
             kitabooNote = document.createElement("kitabooNote");
             kitabooNote.setAttribute('id', noteId);
             kitabooNote.innerText = "V";
             kitabooNote.setAttribute('onclick', "kitabooNoteTapped(event)");
             var highlightOffSet = getAbsPosition(highlight)[0];

             var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
             console.log("biki", "enter here");
             if (false) {
              console.log("biki_6", "not enter here");
                 console.log("is horizontal ");
                 var highlightTop = getAbsPosition(highlight)[0];
                 var highlightLeft = "5";
                 var highlightOffSet = parseInt(highlightTop);
                 var docWidth = window.innerWidth;
                 var docHeight = window.innerHeight;
                 var leftOffSet = 5;
                 var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
                 var leftMargin = ((pageNumber - 1) * docWidth) + leftOffSet;
                 //   console.log("Page No."+pageNumber);
                 var topMargin = parseInt(highlightOffSet - ((pageNumber - 1) * docHeight));
                 kitabooNote.setAttribute('style', "text-align:center;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + leftMargin + "px;font-size:27px;background-color:" + color + ";display: block;top:" + topMargin + "px");



             } else {
             console.log("biki_2", "not enter here");
                console.log("is not horizontal ");
                            if (isfixed) {
                            console.log("isfixedlayout" + isfixed);
                            var highlightTop1 = window.pageYOffset;
                            console.log("isfixedlayouthighlightTop1" + highlightTop1);
                            var highlightTop2 = highlight.getBoundingClientRect().top;
                            var highlightTop=highlightTop1+highlightTop2;
                            //var highlightTop = highlight.getBoundingClientRect().top;
                            console.log("isfixedlayout:highlightTop" + highlightTop);
                 } else {
                 console.log("biki_3", "not enter here");

                     var highlightTop = getAbsPosition(highlight)[0];

                 }


                 console.log("is not horizontal_top value1" + highlightTop);
               //  var highlightOffSet = parseInt(highlightTop);
                 var leftOffSet = 5;
                 var left = leftOffSet;
                 // var top = highlightOffSet;
                */
    /*  var scaleString = document.body.style.transform;
                       console.log("scaleString value is " + scaleString);
                      var scale = parseFloat(scaleString.match(/-?\d+\.?\d*/
    /**/
    /*)[0]);*/
    /*
                     //console.log("is not horizontal_top value2" + top);
                     console.log("scale value is " + scalePercentage);
                     color=color+"!important;display:initial;"
                     if (window.innerHeight > 600) {
                     console.log("biki_4", "not enter here");
                         console.log("window.innerHeight > 600 color " + color);
                         kitabooNote.setAttribute('style', "text-align:center;z-index: 5;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left/scalePercentage + "px;font-size:40px;background-color:" + color + ";display: block;top:" + highlightTop/scalePercentage + "px");
                           console.log("window.innerHeight-val " + left+highlightTop);
                     } else {
                     console.log("biki_5", "not enter here");
                         console.log("else " + color);
                         kitabooNote.setAttribute('style', "text-align:center;z-index: 5;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left/scalePercentage + "px;font-size:35px;background-color:" + color + ";display: block;top:" + highlightTop/scalePercentage + "px");
                     }

                 }
                 document.body.appendChild(kitabooNote);



                 return "{\"name\":" + kitabooNote + "}";
             } catch (err) {}*/
}

function addNoteOnHighlightHr(name, coloris, ishorizontalview, isfixed, impColor) {
    var color = "#F4D631";
    if (impColor == "") {
        if (coloris == "normalHighlight") {
            color = "#F4D631";
        } else if (coloris == "importantHighlight") {
            color = "#F06666";
        } else if (coloris == "sharedHighlight") {
            color = "#92cadc";
        } else {
            color = "#F4D631";
        }
    } else {
        color = impColor;
    }
    var highlight = document.getElementsByName(name)[0];
    var noteId = "note_" + name;
    var kitabooNote = document.getElementById(noteId);
    try {
        if (kitabooNote != null) {
            kitabooNote.parentNode.removeChild(kitabooNote);
        }
        kitabooNote = document.createElement("kitabooNote");
        kitabooNote.setAttribute('id', noteId);
        kitabooNote.innerText = "V";
        kitabooNote.setAttribute('onclick', "kitabooNoteTapped(event)");
        var left = (cumulativeOffset(highlight).left) + 5;
        var top = cumulativeOffset(highlight).top;
        var leftexact = window.outerWidth;
        if (left > leftexact) {
            left = window.outerWidth + 5;
        } else {
            left = 5;
        }
      //  console.log("addNoteOnHighlight-left" + left);
        color = color + "!important;display:initial;"
        if (window.innerHeight > 600) {
            kitabooNote.setAttribute('style', "text-align:center;z-index: 10;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left + "px;font-size:40px;background-color:" + color + ";display: block;top:" + top + "px");
        } else {
            kitabooNote.setAttribute('style', "text-align:center;z-index: 10;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left + "px;font-size:37px;background-color:" + color + ";display: block;top:" + top + "px");
        }
        document.body.insertBefore(kitabooNote, document.body.firstElementChild);

        return "{\"name\":" + kitabooNote + "}";
    } catch (err) {}

}

function isReadyForPullDown() {
    var result = document.body.scrollTop == 0;
    ptr.isReadyForPullDownResponse(result);
}

function isReadyForPullUp() {
    var result = (window.innerHeight + window.scrollY) >= document.body.offsetHeight;
    ptr.isReadyForPullUpResponse(result);
}

function removeallstickynotes() {
    var notes = document.getElementsByTagName("kitabooNote");
    for (var i = 0; i < notes.length; i++) {
        notes[i].setAttribute('style', "text-align:center;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:5px;font-size:27px;background-color:" + notes[i].getAttribute('background-color') + ";display: block;top:5px");
    }
}

function remove(elem) {
    return elem.parentNode.removeChild(elem);
}

function updatesticksVr() {
    var notes = document.getElementsByTagName("kitaboonote");

    var leftOffset = 5;

    for (var i = 0; i < notes.length; i++) {
        //  console.log('update Vr');
        try {
            var id = notes[i].id;
            var name = id.split("_")[1];
            var requiredName = name + "_" + id.split("_")[2];
            var j = notes.length;
            var id = notes[i].id;
            var name = id.split("_")[1];
            var requiredName = name + "_" + id.split("_")[2];
            var highlight = document.getElementsByName(requiredName)[0];
            var highlightTop = getAbsPosition(highlight)[0];

            var highlightOffSet = parseInt(highlightTop);
            var leftOffSet = 5;
            notes[i].style.left = leftOffSet + "px";
            notes[i].style.top = highlightTop + "px";
        } catch (err) {}
        // console.log('update Vr highlightTop '+highlightTop+" left "+leftOffSet);
    }
}


function updatesticksHr() {
    var notes = document.getElementsByTagName("kitaboonote");
    var leftOffset = 5;

    for (var i = 0; i < notes.length; i++) {
        var id = notes[i].id;
        var name = id.split("_")[1];
        var requiredName = name + "_" + id.split("_")[2];
        var id = notes[i].id;
        var name = id.split("_")[1];
        var requiredName = name + "_" + id.split("_")[2];
        var highlight = document.getElementsByName(requiredName)[0];
        var highlightTop = getAbsPosition(highlight)[0];
        var highlightLeft = "5";
        var highlightOffSet = parseInt(highlightTop);
        var docWidth = window.innerWidth;
        var docHeight = window.innerHeight;
        var leftOffSet = 5;
        var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
        var leftMargin = ((pageNumber - 1) * docWidth) + leftOffSet;
        var topMargin = parseInt(highlightOffSet - ((pageNumber - 1) * docHeight));
        notes[i].style.left = leftMargin + "px";
        notes[i].style.top = topMargin + "px";
    }
}

//@@@@@@@@@@@@
//    Delete note on page
//@@@@@@@@@@@@

function deleteNoteOnHighlight(name) {

    var noteId = "note_" + name;
    var kitabooNote = document.getElementById(noteId);
    if (kitabooNote != null) {
        kitabooNote.parentNode.removeChild(kitabooNote);
    }
}

//@@@@@@@@@@@@
//    Add UGC anchor
//@@@@@@@@@@@@

function addHighlightAnchor(name) {

    var highlight = document.getElementsByName(name)[0];
    var anchorId = "ugc_" + name;
    var anchor = document.getElementById(anchorId);

    if (anchor == null) {
        anchor = document.createElement("a");
        anchor.setAttribute("id", anchorId);
        anchor.setAttribute("name", anchorId);
       // highlight.parentNode.insertBefore(anchor, highlight);
        if (!(typeof highlight === 'undefined')) {
             highlight.parentNode.insertBefore(anchor, highlight);
        }
    }
}

//@@@@@@@@@@@@
//    Delete UGC Anchor
//@@@@@@@@@@@@

function deleteHighlightAnchor(name) {

    var anchorId = "ugc_" + name;
    var anchor = document.getElementById(anchorId);
    if (anchor != null) {
        anchor.parentNode.removeChild(anchor);

    }
}

//@@@@@@@@@@@@
//    Start highlighting rangehighlightTaped
//@@@@@@@@@@@@

function highlightRange(range, name) {
    if (!(typeof range === 'undefined')) {
                 var newNode = document.createElement("kitaboospan");
                     // console.log("newNode  called"+newNode);
                     newNode.setAttribute("name", name);
                     newNode.setAttribute('onclick', "kitabooHighlightTapped(this)");
                      if(range.toString().trim().length > 0)
                         {
                            range.surroundContents(newNode);
                         }
                     return newNode.innerText;
            }
}


//@@@@@@@@@@@@
// Returns an array of range objects obtained from range object
//@@@@@@@@@@@@

function getSafeRanges(range) {

    var commonAncestorContainer = range.commonAncestorContainer;
    var startContainer = range.startContainer;
    var endContainer = range.endContainer;

    var startArray = new Array(0),
        startRange = new Array(0);
    var endArray = new Array(0),
        endRange = new Array(0);

    // @@@@@ If start container and end container is same
    if (startContainer == endContainer) {
        return [range];
    } else {

        for (var i = startContainer; i != commonAncestorContainer; i = i.parentNode) {
            startArray.push(i);
        }

        for (var i = endContainer; i != commonAncestorContainer; i = i.parentNode) {
            endArray.push(i);
        }
    }

    if (0 < startArray.length) {

        for (var i = 0; i < startArray.length; i++) {

            if (i) {

                var node = startArray[i - 1];
                while ((node = node.nextSibling) != null) {
                    startRange = startRange.concat(getRangeOfChildNodes(node));
                }
            } else {

                var xs = document.createRange();
                var s = startArray[i];
                var offset = range.startOffset;
                var ea = (startArray[i].nodeType == Node.TEXT_NODE) ? startArray[i] : startArray[i].lastChild;
                xs.setStart(s, offset);
                xs.setEndAfter(ea);
                startRange.push(xs);
            }
        }
    }

    if (0 < endArray.length) {

        for (var i = 0; i < endArray.length; i++) {

            if (i) {

                var node = endArray[i - 1];
                while ((node = node.previousSibling) != null) {
                    endRange = endRange.concat(getRangeOfChildNodes(node));
                }

            } else {
                var xe = document.createRange();
                var sb = (endArray[i].nodeType == Node.TEXT_NODE) ? endArray[i] : endArray[i].firstChild;

                var end = endArray[i];
                var offset = range.endOffset;
                xe.setStartBefore(sb);
                xe.setEnd(end, offset);
                endRange.unshift(xe);
            }
        }
    }

    var topStartNode = startArray[startArray.length - 1];
    var topEndNode = endArray[endArray.length - 1];

    var middleRange = getRangeOfMiddleElements(topStartNode, topEndNode);

    startRange = startRange.concat(middleRange);
    response = startRange.concat(endRange);

    return response;
}

function getRangeOfMiddleElements(topStartNode, topEndNode) {

    var rangeArray = new Array(0);

    for (var i = topStartNode.nextSibling; i != topEndNode; i = i.nextSibling) {

        rangeArray = rangeArray.concat(getRangeOfChildNodes(i));
    }

    return rangeArray;
}

function getRangeOfChildNodes(node) {
    var rangeArray = new Array(0);
    if (node.nodeType == Node.TEXT_NODE) {
        var xm = document.createRange();
        xm.setStartBefore(node);
        xm.setEndAfter(node);
        rangeArray.push(xm);
    } else {
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNode = node.childNodes[i];
            rangeArray = rangeArray.concat(getRangeOfChildNodes(childNode));
        }
    }

    return rangeArray;
}

function pageLinksLoaded() {
    ptr.noteloaded();
}

function renderHighlight(rangeOffset, hasNote, backgroundColor, textColor, scrollEnabledValue) {

    rangeOffset = JSON.parse(rangeOffset);
    var range = restoreSelection(rangeOffset);
    var name = rangeOffset.start + "_" + rangeOffset.end;
    var safeRanges = getSafeRanges(range);
    var txt;
    for (var i = 0; i < safeRanges.length; i++) {
        txt += highlightRange(safeRanges[i], name);
    }
    addHighlightAnchor(name);
    //editHighlight(name, hasNote, color,scrollEnabledValue);
    var object = {
        "name": name,
        "textColor": textColor,
        "backgroundColor": backgroundColor
    };
    editCustomHighlight(object, hasNote, backgroundColor, scrollEnabledValue, false)

}


function renderAllHighlight(jsonarray, hrmode) {
    var data = JSON.parse(jsonarray);
    for (var i = 0; i < data.length; i++) {
        var strtWord = data[i].mStartWordId;
        var endWord = data[i].mEndWordId;
        var range = {};
        range.start = strtWord;
        range.end = endWord;
        var noteData = data[i].mNoteData === '' ? false : true;
        var currHighlightColor = data[i].backgroundColor;
        var textColor = data[i].textColor
        renderHighlight(JSON.stringify(range), noteData, currHighlightColor, textColor, hrmode);
    }
    pageLinksLoaded();
}

function isBookMarkAvailable(jsonarray, isHrmode) {
    var data = JSON.parse(jsonarray);
    for (var i = 0; i < data.length; i++) {
        var bookmarkPath = data[i].mBookMarkPath;
       // console.log("bookmarkPath called" + bookmarkPath);
        if (bookmarkPath != "") {
            var isBookMarkInScroll = isContextBookmarked(bookmarkPath, isHrmode);
           // console.log("isBookMarkInScroll called" + isBookMarkInScroll);
            if (isBookMarkInScroll) {
                ptr.isBookMarkAvailable(isBookMarkInScroll, data[i].mlocalID);
                break;
            } else {
                ptr.isBookMarkAvailable(isBookMarkInScroll, data[i].mlocalID);
            }
        } else {
            ptr.isBookMarkAvailable(false, data[i].mlocalID);
        }

    }
}



//@@@@@@@@@@@@
//    On click events for note and highlight tap
//@@@@@@@@@@@@

function kitabooNoteTapped(event) {
    //console.log(elm.getAttribute("event" +event));
    event.stopPropagation();
    var kitabooNote = event.target;
    var id = kitabooNote.id.split("_");
    ptr.noteTaped(id[1], id[2]);
}

function kitabooHighlightTapped(elm) {
   // console.log(elm.getAttribute("name"));
    try {
        event.stopPropagation();
        var kitabooHighlight = event.target;
        if (!hasClass(kitabooHighlight, "sharedHighlight")) {


            var id = kitabooHighlight.getAttribute("name").split("_");

            var rect = elm.getBoundingClientRect();

            var top = rect.top;
            var left = rect.left;
            var width = rect.width;
            var height = rect.height;
            var right = rect.right;
            var bottom = rect.bottom;
            //var text = element.textContent;
            ptr.highlightTaped(kitabooHighlight.textContent, id[0], id[1], Number(left), Number(top), Number(right), Number(bottom), Number(width), Number(height));
           // console.log("kitabooHighlightTapped called");
        }

    } catch (err) {
       // console.log("kitabooHighlightTapped " + err);
    }


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

function configureMediaElementsForAnalytics() {
    //console.log("configureMediaElementsForAnalytics called");
    var videos = document.getElementsByTagName('video')
    for (var i = 0; i < videos.length; i++) {
        var _video = videos[i];
        _video.addEventListener("playing", video_play_clicked, false);
    }
}

function video_play_clicked(e) {
    var json = {
        "video": e.target.src
    };
    window.location = "videoAnalytics:" + JSON.stringify(json)
    //alert("play was clicked");
}

function getSearchelement() {
    var ele = document.createElement('span');
    ele.setAttribute('class', 'searchelement');
    return ele;
}

function cleareSelection() {

    if (window.getSelection) {
        if (window.getSelection().empty) { // Chrome
            window.getSelection().empty();
        } else if (window.getSelection().removeAllRanges) { // Firefox
            window.getSelection().removeAllRanges();
        }
    } else if (document.selection) { // IE?
        document.selection.empty();
    }
}

function surroundSelection(keyword) {
    if (document.getSelection) {
        var sel = document.getSelection();
        if (sel.rangeCount) {
            var range = window.getSelection().getRangeAt(0);
            var saferanges = getSafeRanges(range);
            var text = "";
            for (var i = 0; i < saferanges.length; i++) {
                //text += highlightRange(saferanges[i], "rand");
                var newNode = document.createElement("span");
                //newNode.setAttribute("class", "searchhighlight");
                saferanges[i].surroundContents(newNode);
                //console.log('html :'+newNode.innerHTML);
                newNode.innerHTML = newNode.innerHTML.toLowerCase().replace(keyword.toLowerCase(), '<span class="searchhighlight">' + keyword + '</span>');
            }
            cleareSelection();

        }
    }
}

function getExactHeight() {
    return document.body.scrollHeight;
}

function getClientHW() {
    var H = document.body.clientHeight;
    var W = document.body.clientWidth;
    var response = {
        height: H,
        width: W
    };

    return response;
}

function removeAllSearchElelemt(element) {
    if (element) {
        if (element.nodeType == 1) {
            if (element.getAttribute("class") == "searchhighlight") {
                var text = element.removeChild(element.firstChild);
                element.parentNode.insertBefore(text, element);
                element.parentNode.removeChild(element);
                return true;
            } else {
                var normalize = false;
                for (var i = element.childNodes.length - 1; i >= 0; i--) {
                    if (removeAllSearchElelemt(element.childNodes[i])) {
                        normalize = true;
                    }
                }
                if (normalize) {
                    element.normalize();
                }
            }

            if (element.getAttribute("class") == "tempsearch") {
                            if(element.firstChild!=null){
                              var text = element.removeChild(element.firstChild);
                              element.parentNode.insertBefore(text, element);
                              element.parentNode.removeChild(element);
                               return true;
                            }else
                            return false;
                        } else {
                            var normalize = false;
                            for (var i = element.childNodes.length - 1; i >= 0; i--) {
                                if (removeAllSearchElelemt(element.childNodes[i])) {
                                    normalize = true;
                                }
                            }
                            if (normalize) {
                                element.normalize();
                            }
                        }
        }
    }
    return false;
}
function removeAllElasticSearchElelemt(element) {
    if (element) {
        if (element.nodeType == 1) {
            if (element.getAttribute("class") == "searchelement") {
                var text = element.removeChild(element.firstChild);
                element.parentNode.insertBefore(text, element);
                element.parentNode.removeChild(element);
                return true;
            } else {
                var normalize = false;
                for (var i = element.childNodes.length - 1; i >= 0; i--) {
                    if (removeAllElasticSearchElelemt(element.childNodes[i])) {
                        normalize = true;
                    }
                }
                if (normalize) {
                    element.normalize();
                }
            }
        }
    }
    return false;
}

/////////////////////////////////////////////////////
var lastelement = 0;

function jumptolastVisitedPage(stringid, ishr) {
    if (stringid == null) {
        window.scrollTo(0, 0);
        ptr.WebVisibility();
    } else {
        try {
            var rangeobj = JSON.parse(stringid);
            var id = rangeobj.start + "_" + rangeobj.end;
            var highlight = document.getElementById(id);
           // console.log("highlightelement___:_" + highlight);
            var top = getAbsPosition(highlight)[0];
            //jump(highlight);
            highlight.scrollIntoView();
            ptr.WebVisibility();
            var pageno = Math.ceil(parseInt(top) / window.innerHeight);
           // console.log("pageno : " + pageno);
            ptr.loadlastVisitedPage(pageno);
        } catch (err) {

        }
    }
}

var vrpageno = 0;
var webheight = 0;
var webwidth = 0;
var scrollpostion = 0;
var anchorid = 0;

function addlastVisitedAnchor(jsondata) {
    var rangeobj = JSON.parse(jsondata);
    /*var start = parseInt(rangeobj.start) + 100;
    var end = parseInt(rangeobj.send) + 100;
    var string = JSON.stringify({
                                    start: start,
                                    end: end
                                    });
    var rangenew = restoreSelection(JSON.parse(string));*/
    var rangenew = restoreSelection(rangeobj);
    var id = rangeobj.start + "_" + rangeobj.end;
    anchorid = id;
    adddummyNote(rangenew, id);
    //ptr.lastVisitedPageData(anchorid);

}

function adddummyNote(range, id) {
   // console.log("adddummyNote" + id);
    var latvisitedanchortag = document.createElement('mark');
    latvisitedanchortag.setAttribute("id", id);
    latvisitedanchortag.setAttribute("name", id);
    latvisitedanchortag.setAttribute("style", "background-color: transparent !important;display:initial; height: 5px;width: 5px;position: absolute;");
    range.surroundContents(latvisitedanchortag);
   // console.log("latvisited page offsettop value" + latvisitedanchortag.offsetTop);
    //latvisitedanchortag.scrollIntoView();
}

function saveBookMarkAnchor(jsondata) {

    if (jsondata != "") {
        var currenantrange = document.caretRangeFromPoint(0, 0);
        var rangeOffset = JSON.parse(jsondata);
       // console.log("currentrange value" + currenantrange);
        // var rangenew = restoreSelection(rangeOffset);
        var name = rangeOffset.start + "_" + rangeOffset.end;
        adddummyBookMark(currenantrange, name);
        /* var rangeOffset = JSON.parse(jsondata);
         var range = restoreSelection(rangeOffset);
             var name = rangeOffset.start+"_"+rangeOffset.end;
             var text = "";

             var safeRanges = getSafeRanges(range);
             for (var i = 0; i < safeRanges.length; i++) {
                 adddummyBookMark(safeRanges[i],name);
             }*/

    }
    //ptr.lastVisitedPageData(anchorid);

}

function addBookMarkAnchor(jsondata) {

    if (jsondata != "") {

        var currenantrange = document.caretRangeFromPoint(0, 0);
        var rangeOffset = JSON.parse(jsondata);
       // console.log("currentrange value" + currenantrange);
        var rangenew = restoreSelection(rangeOffset);
        var name = rangeOffset.start + "_" + rangeOffset.end;
        adddummyBookMark(rangenew, name);
        /* var rangeOffset = JSON.parse(jsondata);
         var range = restoreSelection(rangeOffset);
             var name = rangeOffset.start+"_"+rangeOffset.end;
             var text = "";

             var safeRanges = getSafeRanges(range);
             for (var i = 0; i < safeRanges.length; i++) {
                 adddummyBookMark(safeRanges[i],name);
             }*/

    }
    //ptr.lastVisitedPageData(anchorid);

}

function adddummyBookMark(range, id) {
   /* console.log("adddummyBookMark" + id);
    console.log("range BookMark value" + range);*/
    var latvisitedanchortag = document.createElement('span');
    latvisitedanchortag.setAttribute("class", id);
    latvisitedanchortag.setAttribute("style", "background-color: transparent !important;display:initial; height: 5px;width: 5px;");
    range.surroundContents(latvisitedanchortag);
   // console.log("latvisitedanchortag offsettop value" + latvisitedanchortag.offsetTop);
    //latvisitedanchortag.scrollIntoView();
}

function sleep(seconds) {
    var waitUntil = new Date().getTime() + seconds * 1000;
    while (new Date().getTime() < waitUntil) true;
}

function currentPosition(isHrmode) {

    var range = document.caretRangeFromPoint(0, 0);
    //console.log(" origin current postion " + range.startOffset + " " + range.endOffset);
    var preSelectionRange = range.cloneRange();
    preSelectionRange.selectNodeContents(document.body);
    preSelectionRange.setEnd(range.startContainer, range.startOffset);
    var start = preSelectionRange.toString().length;
    var string = JSON.stringify({
        start: start,
        end: start + range.toString().length
    });

    //console.log("current postion " + string);
    return string;
}

function currentPositionBookMark(isHrmode) {
    try {
        var range = document.caretRangeFromPoint(0, i).cloneRange();
        do {
            i = i + 10;
            range = document.caretRangeFromPoint(0, i).cloneRange();
        }
        while (range == undefined || range.startOffset > 0)
        var preSelectionRange = range.cloneRange();
        preSelectionRange.selectNodeContents(document.body);
        preSelectionRange.setEnd(range.startContainer, range.startOffset);
        var start = preSelectionRange.toString().length;
        var string = JSON.stringify({
            start: start,
            end: start + range.toString().length
        });

        //console.log("jsonstring" + string);
        return string;
    } catch (err) {
        var range = document.caretRangeFromPoint(0, 0);
        var preSelectionRange = range.cloneRange();
        preSelectionRange.selectNodeContents(document.body);
        preSelectionRange.setEnd(range.startContainer, range.startOffset);
        var start = preSelectionRange.toString().length;
        var string = JSON.stringify({
            start: start,
            end: start + range.toString().length
        });

        //console.log("jsonstring" + string);
        return string;
    }
}

function clearAllHighlightAndGetPostion(jsonar, ishrmode) {
    removeallstickynotes();
    //console.log("clearringAllHighlight"+jsonar);
    var data = JSON.parse(jsonar);
    for (var i = 0; i < data.length; i++) {
        var stWord = data[i].mStartWordId;
        var edWord = data[i].mEndWordId;
        // console.log("stWord:" + stWord);
        // console.log("edWord:" + edWord);
        var elemnt = stWord + "_" + edWord;
        // console.log("name:" + elemnt);
        clearHighlight(elemnt);
    }

    stringRet(ishrmode);
}

var visibleY = function(el, isHrmode) {
    var rect = el.getBoundingClientRect(),
        top = rect.top,
        height = rect.height;
    var value = el.offsetTop;
    var minValue = window.pageYOffset == 0 ? window.pageXOffset : window.pageYOffset;
    var maxValue = minValue + window.innerHeight;
    if (isHrmode) {
        var pageNumber = Math.ceil(parseInt(value) / window.innerHeight);
        if (pageNumber == parseInt(value) / window.innerHeight) {
            value = ((pageNumber + 1) * window.innerWidth);
        } else {
            value = ((pageNumber) * window.innerWidth);
        }
    }
    // console.log("visibleY value"+value);
    // console.log("visibleY minValue value"+minValue);
    //console.log("visibleY maxValue value"+maxValue);
    if (value > minValue && value < maxValue) {
        return true;
    } else {
        return false;
    }

};
var elem;
var update = function(elem1, isHrmode) {
    elem = elem1;
    return visibleY(elem1, isHrmode);
};

function isContextBookmarked(bookmarkPath, isHrmode) {
    try {
        var rangeobj = JSON.parse(bookmarkPath);
        var id = rangeobj.start + "_" + rangeobj.end;
        var highlight = document.getElementsByClassName(id)[0];
        //  var highlight = document.getElementById(id);
        //console.log("Book mark element" + highlight);
        var top = highlight.offsetTop + (window.innerHeight / 3);
        /*var highlight1 = document.getElementsByClassName(id)[1];
        var highlightOffSet = getAbsPosition(highlight[0])[0];*/
        var currentTop = window.scrollY;
        var pageHeight = parseInt(currentTop) + window.innerHeight;
        /*console.log("Book mark element top" + top);
        console.log("scrollY value currentTop" + currentTop);
        console.log("Book mark element pageHeight" + pageHeight);*/
        if ((currentTop < top) && (top < pageHeight)) {
            return true;
        } else {
            return false;
        }
    } catch (err) {
        return false;
    }
}

function jumptobookmark(bookmarkPath) {
    var rangeobj = JSON.parse(bookmarkPath);
    var id = rangeobj.start + "_" + rangeobj.end;
    var highlight = document.getElementsByClassName(id)[0];
   // console.log("biki_33 ", +highlight);
    highlight.scrollIntoView();
}

/**
 * Starts the touch and saves the given x and y coordinates as last touch point
 */
function startTouch(x, y) {
   // console.log("startTouch");
    lastTouchPoint = {
        'x': x,
        'y': y
    };
};

function longPressed() {
   // console.log("longTouch selection");
    try {
        //clearSelection();
        var sel = window.getSelection();
       // console.log("Selection " + lastTouchPoint.x + " " + lastTouchPoint.y)
        var range = document.caretRangeFromPoint(lastTouchPoint.x, lastTouchPoint.y);
        range.expand("word");
        var text = range.toString();
       // console.log("range " + text);
        if (text.length == 1) {
            var baseKind = jpntext.kind(text);
            if (baseKind != jpntext.KIND['ascii']) {
                try {
                    do {
                        range.setEnd(range.endContainer, range.endOffset + 1);
                        text = range.toString();
                        var kind = jpntext.kind(text);
                    } while (baseKind == kind);
                    range.setEnd(range.endContainer, range.endOffset - 1);
                } catch (e) {}
                try {
                    do {
                        range.setStart(range.startContainer, range.startOffset - 1);
                        text = range.toString();
                        var kind = jpntext.kind(text);
                    } while (baseKind == kind);
                    range.setStart(range.startContainer, range.startOffset + 1);
                } catch (e) {}
            }
        }
        if (text.length > 0) {
            sel.addRange(range);
            /*android.selection.saveSelectionStart();
            android.selection.saveSelectionEnd();*/
            selectionChanged(true);
        }
    } catch (err) {
       // console.log("selection " + err);
        // window.TextSelection.jsError(err);

    }
    /**
     * Tells the app to show the context menu.
     */
    function selectionChanged(isReallyChanged) {
       // console.log("selectionChanged " + isReallyChanged);
        try {
            var sel = window.getSelection();
            if (!sel) {
                return;
            }
            var range = sel.getRangeAt(0);

            // Add spans to the selection to get page offsets
            var selectionStart = $("<span id=\"selectionStart\">&#xfeff;</span>");
            var selectionEnd = $("<span id=\"selectionEnd\"></span>");

            var startRange = document.createRange();
            startRange.setStart(range.startContainer, range.startOffset);
            startRange.insertNode(selectionStart[0]);

            var endRange = document.createRange();
            endRange.setStart(range.endContainer, range.endOffset);
            endRange.insertNode(selectionEnd[0]);

            var handleBounds = "{'left': " + (selectionStart.offset().left) + ", ";
            handleBounds += "'top': " + (selectionStart.offset().top + selectionStart.height()) + ", ";
            handleBounds += "'right': " + (selectionEnd.offset().left) + ", ";
            handleBounds += "'bottom': " + (selectionEnd.offset().top + selectionEnd.height()) + "}";

            // Pull the spans
            selectionStart.remove();
            selectionEnd.remove();

            // Reset range
            sel.removeAllRanges();
            sel.addRange(range);

            // Rangy
            var rangyRange = getRange();

            // Text to send to the selection
            var text = window.getSelection().toString();

            // Set the content width
            //window.TextSelection.setContentWidth(document.body.clientWidth);

            // Tell the interface that the selection changed
            window.TextSelection.selectionChanged(rangyRange, text, handleBounds, isReallyChanged);
        } catch (e) {
            //window.TextSelection.jsError(e);
           // console.log("selectionChanged " + e);

        }
    };

    function getRange() {
        var serializedRangeSelected = rangy.serializeSelection();
        var serializerModule = rangy.modules.Serializer;
        if (serializedRangeSelected != '') {
            if (rangy.supported && serializerModule && serializerModule.supported) {
                var beginingCurly = serializedRangeSelected.indexOf("{");
                serializedRangeSelected = serializedRangeSelected.substring(0, beginingCurly);
                return serializedRangeSelected;
            }
        }
    }
}
// Font name class
function setFontName(cls) {
    var elm = document.body;
    removeClass(elm, "andada");
    removeClass(elm, "lato");
    removeClass(elm, "lora");
    removeClass(elm, "raleway");
    addClass(elm, cls);

    $('body font').attr('face', cls)
}

function removeFonts() {
    var elm = document.body;
    removeClass(elm, "andada");
    removeClass(elm, "lato");
    removeClass(elm, "lora");
    removeClass(elm, "raleway");
}

function changeTextAlign(textAlign) {
  //  console.log("changeTextAlign : " + textAlign);
    var elm = document.documentElement;
    removeClass(elm, "leftAlign");
    removeClass(elm, "rightAlign");
    removeClass(elm, "centerAlign");
    addClass(elm, textAlign);
}
//Links color handled here
var linkcolor = "color: #0000EE ";
document.addEventListener("DOMContentLoaded", function() {
    var links = document.getElementsByTagName("a");
    for (var i = 0; i < links.length; i++) {
        if (links[i].getAttribute('href')) {
            links[i].setAttribute("onclick", "onLinkClick(this,'" + links[i].href + "')");
            //links[i].removeAttribute("href");
            links[i].setAttribute("style", linkcolor);

        }

    }

}, false);

function removeCSSRule(selector, newRule) {
    var mySheet = document.styleSheets[0];
    if (mySheet != undefined) {
        if (mySheet.addRule) {
            for (var i = 0; i < mySheet.rules.length; i++) {
                if (mySheet.rules[i].selectorText === 'html')
                    mySheet.removeRule(i);
            }
            //mySheet.removeRule(mySheet.rules.length - 1);
            mySheet.addRule(selector, newRule);
        } else {
            ruleIndex = mySheet.cssRules.length;
            mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
        }
    } else {
        var nul = 10;
    }
}

function removeCSSRuleStyle() {
    removeCSSRule('html', 'width:' + window.innerWidth + 'px;');
}
//Currently not in use.
/*function createFontStyleElement(type) {

    var element;
    switch(type) {
    case "BOLD":
        element = document.createElement("span");
        element.setAttribute('style','font-weight:bold');
    break;
    case "ITALIC":
        element = document.createElement("span");
        element.setAttribute('style','font-style: italic');
    break;
    case "UNDERLINE":
        element = document.createElement("span");
        element.setAttribute('style','border-bottom: 3px solid black');
    break;

    default:
    break;
    }
    return element;

}
//Change font style ie. Bold, Italic, Underline
function changeFontStyle(type) {


   */
/* var sel = window.getSelection();
    var range = sel.getRangeAt(0);
    var safeRanges = getSafeRanges(range);
    var name = range.startOffset+"_"+range.endOffset;
    */
/*
    var range = window.getSelection().getRangeAt(0);
    var safeRanges = getSafeRanges(range);
        var preSelectionRange = range.cloneRange();
        preSelectionRange.selectNodeContents(document.body);
        preSelectionRange.setEnd(range.startContainer, range.startOffset);
        var start = preSelectionRange.toString().length;
        var string = JSON.stringify({
                                    start: start,
                                    end: start + range.toString().length
                                    });
        var jsonrange =  JSON.parse(string);
         var restorerange = restoreSelection(jsonrange);
         var oldname = range.startOffset+"_"+range.endOffset;
         var name = jsonrange.start+"_"+jsonrange.end;


    console.log("changeFontStyle : "+oldname + " new name"+name);
    for(var i = 0; i< safeRanges.length; i++) {
        var element = createFontStyleElement(type);
        element.setAttribute('onclick', "kitabooHighlightTapped(this)");
        element.setAttribute('name', name);
        safeRanges[i].surroundContents(element);
   }
}*/

var cumulativeOffset = function(element) {
    var top = 0,
        left = 5;
    return {
        top: element.getBoundingClientRect().height,
        left: element.getBoundingClientRect().x
    };
};




function buildRange(){
var sel = window.getSelection();
var range = sel.getRangeAt(0);
var startNode = range.startContainer;
var endNode = range.endContainer;
var startNodetxt = range.startContainer;
var endNodetxt = range.endContainer;
if (startNode.nodeType == 3) {
  var startIsText = true;
  var startFlag = startNode.parentNode;
  startNode = startNode.nodeValue.wholeText;
} else {
  var startIsText = false;
  var startFlag = startNode.wholeText;
}
if (endNode.nodeType == 3) {
  var endIsText = true;
  var endFlag = endNode.parentNode;
  endNode = endNode.nodeValue.wholeText;
} else {
  var endIsText = false;
  var endFlag = endNode;
}

var startOffset = range.startOffset;
var endOffset = range.endOffset;

var startTagName = startFlag.nodeName.wholeText;
var startHTML = startFlag.innerHTML;

var endTagName = endFlag.nodeName.wholeText;
var endHTML = endFlag.innerHTML;
//you can store this in database and use it
var rInfo = {
  startNode: startNode,
  startOffset: startOffset,
  startIsText: startIsText,
  startTagName: startTagName,
  startHTML: startHTML,

  endNode: endNode,
  endOffset: endOffset,
  endIsText: endIsText,
  endTagName: endTagName,
  endHTML: endHTML
};
ptr.saveHighlightRange(JSON.stringify(rInfo));
}

function findEleH(tagName, innerHTML) {
  var list = document.getElementsByTagName(tagName);
  for (var i = 0; i < list.length; i++) {
    if (list[i].innerHTML == innerHTML) {
	if (list[i].hasChildNodes()) {
	 for (var i = 0; i < list[i].childNodes.length; i++) {
	 if (list[i].childNodes[i].nodeType==3) {
	 return list[i].childNodes[i];
	 }
	 }
	}else{
	if (list[i].nodeType==3) {
	 return list[i];
	}
	}
    }
  }
}

function show(startNode,startIsText,startOffset,
          endNode,endIsText,endOffset,sP,eP) {
  var s, e,mstartNode,mendNode;
  s=sP;
  e=eP;
  if (startIsText) {
    var childs = sP.childNodes;
    //console.log(childs);
    for (var i = 0; i < childs.length; i++) {
      /*console.log(childs[i].nodeValue);
      console.log(startNode);*/
      if (childs[i].nodeType == 3 && childs[i].nodeValue == startNode)
        s = childs[i];
		mstartNode=i;
     // console.log(s);
    }
  } else {
    s = startNode;
	mstartNode=startOffset;
  }
  if (endIsText) {
    var childs = eP.childNodes;
   // console.log(childs);
    for (var i = 0; i < childs.length; i++) {
      if (childs[i].nodeType == 3 && childs[i].nodeValue == endNode)
        e = childs[i];
		mendNode=i;
    //  console.log(e);
    }
  } else {
    e = startNode;
	mendNode=endOffset;
  }
  var range = document.createRange();
  range.setStart(s, startOffset);
  range.setEnd(e, endOffset);

  var sel = window.getSelection();
  sel.removeAllRanges();
  sel.addRange(range);
}

function callrestoreSelection(rangeobjdata){
//log("rangeobjdata " + rangeobjdata);
//var mainstring=rangeobjdata;
//var newendNode =mainstring.replace(/("http)/gm,"'http");
//var cleanString = newendNode.replace(/(tml\")/gm,'tml\'');
//var requiredjson=JSON.parse(cleanString);
//console.log("requiredjson " + requiredjson);

var requiredjson=JSON.parse(rangeobjdata);
//console.log("requiredjsonobj " + requiredjson);
use(requiredjson);
}

function use(obj) {
/*var dddd=obj.startHTML;
var et1 =dddd.replace(/('http)/gm,"\"http");
var cleanString1 = et1.replace(/(tml\')/gm,'tml\"');

var dddd1=obj.endHTML;
var et2 =dddd.replace(/('http)/gm,"\"http");
var cleanString2 = et2.replace(/(tml\')/gm,'tml\"');

  var sP = findEleH(obj.startTagName,cleanString1);
  var eP = findEleH(obj.endTagName, cleanString2);*/

  var sP = findEleH(obj.startTagName,obj.startHTML);
    var eP = findEleH(obj.endTagName,obj.endHTML);

  show(
    obj.startNode,
    obj.startIsText,
    obj.startOffset,
    obj.endNode,
    obj.endIsText,
    obj.endOffset,
    sP,
    eP
  );
}

function getRangeforHighlight(){
var mainstring=JSON.stringify(buildRangeH());
var newendNode =mainstring.replace(/("http)/gm,"'http");
var cleanString = newendNode.replace(/(tml\")/gm,'tml\'');
//var requiredjson=JSON.parse(cleanString);
ptr.saveHighlightRange(cleanString);
}
/*function scaleContentToContainerSize(requiredHeight,requiredWidth,mobileLandscape){
    var currentHeight = document.body.clientHeight;
    var currentWidth = document.body.clientWidth;
    var viewportContentArray =document.querySelector("meta[name=viewport]").getAttribute("content").split(",")
    var i=0;
    for (i=0;i<viewportContentArray.length;i++)
    {
        var property=viewportContentArray[i].split("=")[0].replace(/\s/g, "").toLowerCase();
        if(property=="height")
        {
           currentHeight=viewportContentArray[i].split("=")[1];
        }
        else if(property=="width")
        {
            currentWidth=viewportContentArray[i].split("=")[1];
        }
    }
    if(isNaN(currentWidth))
    {
        currentWidth = document.body.clientWidth;
    }
    if(isNaN(currentHeight))
    {
        currentHeight = document.body.clientHeight;
    }
    console.log("required height " + requiredHeight + "currentHeight"+currentHeight);
    console.log("required Width " + requiredWidth + "currentWidth"+currentWidth);
    //Try Scale to Height, if fails, do fit to width
    //Fit to Height
    scaleH = requiredWidth / currentWidth;
    scaleV = requiredHeight / currentHeight;
    console.log("Scale Height "+scaleH + "Scale Width " + scaleV);
    scalePercentage = Math.abs(currentWidth > currentHeight ? Math.min(scaleV, scaleH) : Math.max(scaleV, scaleH))

    //scalePercentage= ((requiredHeight*100)/currentHeight)/100;
    if((scalePercentage*currentWidth)>requiredWidth)
    {

     if(!mobileLandscape)
        {
        //Fit to Width
       // scalePercentage= ((requiredWidth*100)/currentWidth)/100;
        }
    }
     if((scalePercentage*currentHeight)>requiredHeight && mobileLandscape)
             {
               console.log("more than requiredHeight " + scalePercentage);
               scalePercentage = scaleV;
             }
    if(!mobileLandscape)
    {
        if(scalePercentage < 1)
        {
          scalePercentage = 1;
        }
     }

    console.log("required scale value " + scalePercentage);
    document.body.style.transform="scale("+scalePercentage+")";
    document.body.style.webkitTransform="scale("+scalePercentage+")";

    if(!mobileLandscape)
    {
     document.body.style.transformOrigin = "0 0";
     document.body.style.webkitTransformOrigin = "0 0";
     }else
     {
     document.body.style.transformOrigin = "top center";
          document.body.style.webkitTransformOrigin = "top center";
          *//*document.body.ontouchend = (e) => {
                            e.preventDefault();
                        };*//*
     }


    var newHeight =  Math.round(document.body.getBoundingClientRect().height+0.01);
    var newWidth = Math.round(document.body.getBoundingClientRect().width+0.01);
    var htmlContentWidthHeight= newWidth.toString()+","+newHeight.toString();
    console.log("required height value " + htmlContentWidthHeight);
}*/

function isInViewRect(elem) {

    var bounding = elem.getBoundingClientRect();

    return ( bounding.top >= 0 && bounding.left >= 0 && bounding.bottom <= (window.innerHeight || document.documentElement.clientHeight) && bounding.right <= (window.innerWidth || document.documentElement.clientWidth) );
};



//highlight search text by index

function highlightTextByIndex(word,indx,color,defaultColor)

{
    if(highlightdoc(word,color)){
    var CurrIndx=parseInt(indx);



    var highlights = document.getElementsByClassName("searchelement");

    //var highlight = highlights[CurrIndx];

    if(highlights.length)

    {
        for(i=0;i<highlights.length;i++){

            var currentElement = highlights[i];

            if(currentElement)

            {

                if(i==indx)

                {
                    currentElement.style.backgroundColor = color;

                    var isElemInView =isInViewRect(currentElement);

                    if(!isElemInView){

                    scrollElementInToView(currentElement);

                    }

                }

                else

                {
                    currentElement.style.backgroundColor = defaultColor;

                }

            }

        }

    }
}


}


function highlightdoc(word,color) {
   var bolean = removeAllElasticSearchElelemt(document.body);
    var elem=document.body;
    var content = elem.innerHTML,
    pattern = new RegExp(word,'ig'),
    replaceWith = '<span style="background-color:'+ color +'" class="searchelement">'+ word +'</span>',
    highlighted = content.replace(pattern,replaceWith);
    return (elem.innerHTML = highlighted) !== content;
}
var mElasticTmpBackgroundColor = "" ;
var mSearchHighlightedColor = "" ;
var mSearchPosition = 0 ;
var searchResultsInvisible = true;
var searchResults = [];
var lastSearchQuery = null;
var testCounter = 0;
var loadedUrl = "" ;



function highlightSearchResult(searchQuery , tmpBackgroundColor , searchHighlightedColor , searchPosition) {

    mElasticTmpBackgroundColor = tmpBackgroundColor ;
    mSearchHighlightedColor = searchHighlightedColor ;
    mSearchPosition = searchPosition ;
    cleareSelection();
    removeSearchElelemt(document.body);
    makeSearchResultsInvisible();
    resetSearchResults();
    searchResults = applySearchResultClass(searchQuery);
    applySearchResultVisibleClass();
}

function removeSearchElelemt(element) {
    if (element) {
        if (element.nodeType == 1) {
            if (element.getAttribute("class") == "search-result-visible") {

                var text = element.removeChild(element.firstChild);
                element.parentNode.insertBefore(text, element);
                element.parentNode.removeChild(element);
                return true;
            } else {
                var normalize = false;
                for (var i = element.childNodes.length - 1; i >= 0; i--) {
                    if (removeAllSearchElelemt(element.childNodes[i])) {
                        normalize = true;
                    }
                }
                if (normalize) {
                    element.normalize();
                }
            }
        }
    }
    return false;
}
function makeSearchResultsInvisible() {

    if (searchResultsInvisible)
        return;
    for (var i = 0 ; i < searchResults.length ; i++) {
        if (searchResults[i].className == "search-result-visible")
            searchResults[i].className = "search-result-invisible";
    }
    searchResultsInvisible = true;
}
function resetSearchResults() {

    for (var i = 0 ; i < searchResults.length ; i++) {
        searchResults[i].outerHTML = searchResults[i].innerHTML;
    }

    searchResults = [];
    lastSearchQuery = null;
    searchResultsInvisible = true;
}

function applySearchResultClass(searchQuery) {

    var searchQueryRegExp = new RegExp(escapeRegExp(searchQuery), "i");

    var searchResults = [];
    var searchChildNodesArray = [];
    var elementArray = [];
    var textNodeArray = [];

    var bodyElement = document.getElementsByTagName('body')[0];
    var elementsInBody = bodyElement.getElementsByTagName('*');

    for (var i = 0 ; i < elementsInBody.length ; i++) {

        var childNodes = elementsInBody[i].childNodes;

        for (var j = 0; j < childNodes.length; j++) {

            if (childNodes[j].nodeType == Node.TEXT_NODE &&
                childNodes[j].nodeValue.trim().length) {


                if (childNodes[j].nodeValue.match(searchQueryRegExp)) {


                    searchChildNodesArray.push(
                        getSearchChildNodes(childNodes[j].nodeValue, searchQuery));

                    elementArray.push(elementsInBody[i]);
                    textNodeArray.push(childNodes[j]);
                }
            }
        }
    }

    for (var i = 0 ; i < searchChildNodesArray.length ; i++) {

        var searchChildNodes = searchChildNodesArray[i];

        for (var j = 0 ; j < searchChildNodes.length ; j++) {

            if (searchChildNodes[j].className == "search-result")
                searchResults.push(searchChildNodes[j]);
            elementArray[i].insertBefore(searchChildNodes[j], textNodeArray[i]);
        }

        elementArray[i].removeChild(textNodeArray[i]);
    }

    lastSearchQuery = searchQuery;
    return searchResults;
}

function applySearchResultClass(searchQuery) {

    var searchQueryRegExp = new RegExp(escapeRegExp(searchQuery), "i");

    var searchResults = [];
    var searchChildNodesArray = [];
    var elementArray = [];
    var textNodeArray = [];

    var bodyElement = document.getElementsByTagName('body')[0];
    var elementsInBody = bodyElement.getElementsByTagName('*');

    for (var i = 0 ; i < elementsInBody.length ; i++) {

        var childNodes = elementsInBody[i].childNodes;

        for (var j = 0; j < childNodes.length; j++) {

            if (childNodes[j].nodeType == Node.TEXT_NODE &&
                childNodes[j].nodeValue.trim().length) {


                if (childNodes[j].nodeValue.match(searchQueryRegExp)) {


                    searchChildNodesArray.push(
                        getSearchChildNodes(childNodes[j].nodeValue, searchQuery));

                    elementArray.push(elementsInBody[i]);
                    textNodeArray.push(childNodes[j]);
                }
            }
        }
    }

    for (var i = 0 ; i < searchChildNodesArray.length ; i++) {

        var searchChildNodes = searchChildNodesArray[i];

        for (var j = 0 ; j < searchChildNodes.length ; j++) {

            if (searchChildNodes[j].className == "search-result")
                searchResults.push(searchChildNodes[j]);
            elementArray[i].insertBefore(searchChildNodes[j], textNodeArray[i]);
        }

        elementArray[i].removeChild(textNodeArray[i]);
    }

    lastSearchQuery = searchQuery;
    return searchResults;
}

function applySearchResultVisibleClass() {

for(var i = 1 ; i <= searchResults.length ; i++){
    searchResult = searchResults[i - 1];
    if(searchResult != undefined){
        searchResult.className = "search-result-visible";
        searchResultsInvisible = false;
         var styleText ;
        if(i == mSearchPosition){
             styleText =  "background-color:"+ mSearchHighlightedColor + "!important;";
        }else{
             styleText =  "background-color:"+ mElasticTmpBackgroundColor + "!important;";
        }

        searchResult.setAttribute("style", styleText);
    }
}

    if(searchResults[mSearchPosition - 1]){
        scrollToElement(searchResults[mSearchPosition - 1]);
    }


}
function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function getSearchChildNodes(text, searchQuery) {
    var arrayIndex = [];
    var matchIndexStart = -1;
    var textChunk = "";
    var searchChildNodes = [];

    for (var i = 0, j = 0 ; i < text.length ; i++) {

        textChunk += text[i];

        if (text[i].match(new RegExp(escapeRegExp(searchQuery[j]), "i"))) {

            if (matchIndexStart == -1)
                matchIndexStart = i;

            if (searchQuery.length == j + 1) {

                var textNode = document.createTextNode(
                    textChunk.substring(0, textChunk.length - searchQuery.length));

                var searchNode = document.createElement("span");
                searchNode.className = "search-result";
                var queryTextNode = document.createTextNode(
                    text.substring(matchIndexStart, matchIndexStart + searchQuery.length));
                searchNode.appendChild(queryTextNode);

                searchChildNodes.push(textNode);
                searchChildNodes.push(searchNode);

                arrayIndex.push(matchIndexStart);
                matchIndexStart = -1;
                j = 0;
                textChunk = "";

            } else {
                j++;
            }

        } else {
            matchIndexStart = -1;
            j = 0;
        }
    }

    if (textChunk !== "") {
        var textNode = document.createTextNode(textChunk);
        searchChildNodes.push(textNode);
    }

    return searchChildNodes;
}

function clearAllElasticSearchResult(){
    for(var i = 1 ; i <= searchResults.length ; i++){
            searchResult = searchResults[i - 1];
            if(searchResult != undefined){
                searchResult.className = "search-result-invisible";
                searchResultsInvisible = false;
                searchResult.style.backgroundColor = 'transparent';
                var styleText =  "background-color:#ffffff00 !important;";
                searchResult.setAttribute("style", styleText);
            }
    }

      makeSearchResultsInvisible();
      removeSearchElelemt(document.body);
      cleareSelection();
}

function scaleContentToContainerSize(requiredHeight,requiredWidth)
{
//console.log("scaleContentToContainerSize " + requiredHeight+"___" + requiredWidth);
    var currentHeight = document.body.clientHeight;
    var currentWidth = document.body.clientWidth;
    var viewportContentArray =document.querySelector("meta[name=viewport]").getAttribute("content").split(",")
    var i=0;
    for (i=0;i<viewportContentArray.length;i++)
    {
        var property=viewportContentArray[i].split("=")[0].replace(/\s/g, "").toLowerCase();
        if(property=="height")
        {
           currentHeight=viewportContentArray[i].split("=")[1];
        }
        else if(property=="width")
        {
            currentWidth=viewportContentArray[i].split("=")[1];
        }
    }
    if(isNaN(currentWidth))
    {
        currentWidth = document.body.clientWidth;
    }
    if(isNaN(currentHeight))
    {
        currentHeight = document.body.clientHeight;
    }
   // console.log("required height " + requiredHeight + "currentHeight"+currentHeight);
  //  console.log("required Width " + requiredWidth + "currentWidth"+currentWidth);
    //Try Scale to Height, if fails, do fit to width
    //Fit to Height
    scalePercentage= ((requiredHeight*100)/currentHeight)/100;
    if((scalePercentage*currentWidth)>requiredWidth)
    {
        //Fit to Width
        scalePercentage= ((requiredWidth*100)/currentWidth)/100;
    }
    /*if(scalePercentage < 1)
    {
      scalePercentage = 1;
    }*/
    var leftMargin = (requiredWidth-(currentWidth*scalePercentage))/2;
    var topMargin = (requiredHeight-(currentHeight*scalePercentage))/2;
  //  console.log("required scale value " + scalePercentage);
  var scalePercentageX=window.innerWidth/currentWidth;
  var scalePercentageY=window.innerHeight/currentHeight;
    document.body.style.transform="scale("+scalePercentageX+","+scalePercentageY+")";
    document.body.style.webkitTransform="scale("+scalePercentageX+","+scalePercentageY+")";
    document.body.style.transformOrigin = "0 0";
    document.body.style.webkitTransformOrigin = "0 0";

    ptr.scaleValueForFixedLayout(scalePercentage);
}

function addWaterMark(isMobile, waterMarkText,isPortraitPrint) {

                var watermarkGap = 300;
                var watermarkTop = watermarkGap;
                var waterMarkRage = document.documentElement.clientHeight / 2;
                //do{
                    kitabooWatermark = document.createElement("kitabooWatermark");
        		    kitabooWatermark.innerText = waterMarkText;
        		    kitabooWatermark.setAttribute('style', "font-size: 20px !important ;top:" + watermarkTop + "px; color: #668A96; -webkit-transform: rotate(330deg); left: 40px; right: 40px");
        	    	document.body.appendChild(kitabooWatermark)
        		    watermarkTop = watermarkTop + 300;
        		//}
        		//while (watermarkTop < waterMarkRage);
}


/*
function setContentToContainerSize(requiredHeight,requiredWidth){
  console.log("setContentToContainerSize " + requiredHeight+"___" + requiredWidth);
  scaleContentToContainerSize(requiredHeight,requiredWidth);
}*/
