javascript: var h = 0;
var w = 0;
var tocData = "";
var lastPage = 1;
var objContent = "";
var height = 0;
var paginatePages = "";
var nodes = "";
var lastElmTopPos = 0;
var lastaudioElm;
var isPageInScroll = false;

function changeMode(mode) {
	// console.log("Mode:-changeMode" , JSON.stringify(mode));
	if(JSON.stringify(mode) === JSON.stringify("Day")) {
		//  console.log("Mode:-changeMode" , JSON.stringify(mode) );
		setReaderMode("dayMode");
	} else if(JSON.stringify(mode) === JSON.stringify("Night")) {
		//   console.log("Mode:-changeMode" , JSON.stringify(mode));
		setReaderMode("nightMode");
	} else if(JSON.stringify(mode) === JSON.stringify("Sepia")) {
		setReaderMode("sepiaMode");
		//  console.log("Sepia");
	}
}

function setReaderMode(classes) {
	var elm = document.body;
	var htmlel = document.getElementsByTagName('html')[0]
	removeClass(elm, "nightMode");
	removeClass(elm, "sepiaMode");
	removeClass(elm, "dayMode");
	addClass(elm, classes);
	removeClass(htmlel, "nightMode");
	removeClass(htmlel, "sepiaMode");
	removeClass(htmlel, "dayMode");
	addClass(htmlel, classes);
	var fonts = document.getElementsByTagName('font');
	// console.log(fonts.length);
	for(var a = 0; a < fonts.length; a++) {
		var elm = fonts[a];
		// console.log(elm);
		removeClass(elm, "nightMode");
		removeClass(elm, "sepiaMode");
		removeClass(elm, "dayMode");
		addClass(elm, classes);
		removeClass(htmlel, "nightMode");
		removeClass(htmlel, "sepiaMode");
		removeClass(htmlel, "dayMode");
		addClass(htmlel, classes);
	}
	var fonts = document.getElementsByTagName('main');
	//   console.log(fonts.length);
	for(var b = 0; b < fonts.length; b++) {
		var elm = fonts[b];
		//   console.log(elm);
		removeClass(elm, "nightMode");
		removeClass(elm, "sepiaMode");
		removeClass(elm, "dayMode");
		addClass(elm, classes);
		removeClass(htmlel, "nightMode");
		removeClass(htmlel, "sepiaMode");
		removeClass(htmlel, "dayMode");
		addClass(htmlel, classes);
	}
}
// Class manipulation
function hasClass(ele, cls) {
	return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

function addClass(ele, cls) {
	if(!hasClass(ele, cls)) ele.className += " " + cls;
}

function removeClass(ele, cls) {
	if(hasClass(ele, cls)) {
		var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
		ele.className = ele.className.replace(reg, ' ');
	}
}

function loadToMyDataAncher(inputhref) {
	var top = document.getElementsByName(inputhref)[0];
	var topoffset = getAbsPosition(top)[0];
	window.scrollTo(0, topoffset);
}

function jumpToTOC(inputhref) {
	var top = document.getElementById(inputhref);
	var topoffset = getAbsPosition(top)[0];
	window.scrollTo(0, topoffset);
}

function jumpToTOCHr(inputhref) {
	var top = document.getElementById(inputhref);
	top.scrollIntoView();
}

function scrollinHorizontal(id) {
	var highlight = document.getElementsByName(id)[0];
	var highlightTop = highlight.offsetTop;
	//console.log("highlightTop value"+highlightTop);
	var highlightOffSet = parseInt(highlightTop);
	var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.scrollHeight);
	//console.log("pageNumber in scrollinHorizontal"+pageNumber);
	//return pageNumber;
	ptr.onScrollInHorizantal(pageNumber);
}

function getAbsPosition(el) {
	var el2 = el;
	var curtop = 0;
	var curleft = 0;
	if(document.getElementById || document.all) {
		do {
			curleft += el.offsetLeft - el.scrollLeft;
			curtop += el.offsetTop - el.scrollTop;
			el = el.offsetParent;
			el2 = el2.parentNode;
			while(el2 != el) {
				curleft -= el2.scrollLeft;
				curtop -= el2.scrollTop;
				el2 = el2.parentNode;
			}
		} while (el.offsetParent);
	} else if(document.layers) {
		curtop += el.y;
		curleft += el.x;
	}
	return [curtop, curleft];
}

function scrollinHorizontalForToc(id) {
	var highlight = document.getElementById(id);
	var postion = getAbsPosition(highlight);
	var highlightTop = postion[0];
	//console.log("highlightTop value"+highlightTop);
	var highlightOffSet = parseInt(highlightTop);
	var pageNumber = Math.ceil(parseInt(highlightOffSet) / window.innerHeight);
	//console.log("pageNumber in scrollinHorizontal"+pageNumber);
	// return pageNumber;
	ptr.onScrollHorizantal(pageNumber);
}
javascript: function jumpHref(inputhref) {
	var top = document.getElementById(inputhref).offsetTop - 50;
	window.scrollTo(0, top);
}
javascript: function changeFont(fontsize, mode) {
		setReaderFont(fontsize, mode);
	}
	/*javascript : function setReaderFont(fontSize,ishr){
	     var elm = document.documentElement;
	     removeClass(elm,"xSmallFont");
	     removeClass(elm,"smallFont");
	     removeClass(elm,"mediumFont");
	     removeClass(elm,"largeFont");
	     addClass(elm, fontSize);
	       updatesticksVr();
	    */
	/* if(ishr){
	      updatesticksHr();
	     }else{
	     updatesticksVr();
	     }*/
	/*
	    // getPageCount();
	}*/
javascript: function setReaderFont(fontSize, ishr) {
	/*console.log("setReaderFont-fontsize" + fontSize);
	console.log("setReaderFont-fontsize" + ishr);*/
	var elm = document.body;
	//  console.log("setReaderFont-elm" + elm);
	removeClass(elm, "xxSmallFont");
	removeClass(elm, "xSmallFont");
	removeClass(elm, "smallFont");
	removeClass(elm, "mediumFont");
	removeClass(elm, "largeFont");
	removeClass(elm, "xLargeFont");
	removeClass(elm, "xxLargeFont");
	addClass(elm, fontSize);
	var htmlel = document.getElementsByTagName('html')[0];
	// console.log("setReaderFont-elm" + htmlel);
	removeClass(htmlel, "xxSmallFont");
	removeClass(htmlel, "xSmallFont");
	removeClass(htmlel, "smallFont");
	removeClass(htmlel, "mediumFont");
	removeClass(htmlel, "largeFont");
	removeClass(htmlel, "xLargeFont");
	removeClass(htmlel, "xxLargeFont");
	addClass(htmlel, fontSize);
	var fonts = document.getElementsByTagName('font');
	// console.log(fonts.length);
	for(var a = 0; a < fonts.length; a++) {
		var elm = fonts[a];
		//   console.log(elm);
		removeClass(elm, "xxSmallFont");
		removeClass(elm, "xSmallFont");
		removeClass(elm, "smallFont");
		removeClass(elm, "mediumFont");
		removeClass(elm, "largeFont");
		removeClass(elm, "xLargeFont");
		removeClass(elm, "xxLargeFont");
		addClass(elm, fontSize);
	}
	/* if(ishr){
	  updatesticksHr();
	 }else{
	 updatesticksVr();
	 }*/
	// getPageCount();
}
javascript: function updateAllTagsFontSize() {
	//console.log("updateAllTagsFontSize");
	var html = document.getElementsByTagName("html")[0];
	html.style.fontSize = "1em";
	var body = document.getElementsByTagName("body")[0];
	body.style.fontSize = "1em";
	updateTagFontSize("div");
	updateTagFontSize("span");
	updateTagFontSize("p");
	updateTagFontSize("a");
	updateTagFontSize("td");
	updateTagFontSize("tr");
}
javascript: function updateTagFontSize(tag) {
	//console.log("updateTagFontSize");
	var tags = document.getElementsByTagName(tag);
	if(tags.length != 0) {
		for(var i = 0; i < tags.length; i++) {
			//Inline font size.
			var inlineSize = '';
			inlineSize = tags[i].style.fontSize;
			if(inlineSize != '') {
				tags[i].style.fontSize = convertToEm(inlineSize);
				continue;
			}
			//Font applied in css class.
			var size = '';
			size = getStyleSheetPropertyValue(tags[i], 'font-size');
			if(size != '') {
				tags[i].style.fontSize = convertToEm(size);
				continue;
			}
			tags[i].style.fontSize = '1em';
		}
	}
}

function getStyleSheetPropertyValue(ele, propertyName) {
	var style = window.getComputedStyle(ele, null).getPropertyValue('font-size');
	return style;
}
/*javascript : function setContentToContainerSize(requiredHeight,requiredWidth){
  scaleContentToContainerSize(requiredHeight,requiredWidth);
}*/
function convertToEm(size) {
	var len = size.length;
	var unit = size.substr(len - 2, len - 1);
	if(unit === 'px') {
		return(parseFloat(size) / 16) + 'em';
	} else if(unit === 'pt') {
		return(parseFloat(size) / 12) + 'em';
	} else {
		return parseFloat(size) + 'em';
	}
}
/*function iResize() {
   var height = window.screen.height;
   if (height >= document.documentElement.clientHeight) {
      document.documentElement.style.height = '101%'
   }
}*/
// Set font size
function setFontSize(cls) {
	var elm = document.documentElement;
	removeClass(elm, "textSizeOne");
	removeClass(elm, "textSizeTwo");
	removeClass(elm, "textSizeThree");
	removeClass(elm, "textSizeFour");
	removeClass(elm, "textSizeFive");
	addClass(elm, cls);
	if(ReflowablePageFragment != undefined) ReflowablePageFragment.updateNotesPosition();
}
var mySheet = document.styleSheets[0];

function addCSSRule(selector, newRule) {
	if(mySheet != undefined) {
		if(mySheet.addRule) {
			mySheet.addRule(selector, newRule);
		} else {
			var ruleIndex = mySheet.cssRules.length;
			mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
		}
	}
}

function setDocumentBodyMode(dayNightMode) {
	if(JSON.stringify(dayNightMode) === JSON.stringify("Night")) {
		document.body.className = "nightMode"
	} else if(JSON.stringify(dayNightMode) === JSON.stringify("Sepia")) {
		document.body.className = "sepiaMode"
	} else if(JSON.stringify(dayNightMode) === JSON.stringify("Day")) {
		document.body.className = "dayMode"
	}
}

function setAlignment(selectedFont) {
	var elm = document.documentElement;
	removeClass(elm, "alignLeft");
	removeClass(elm, "alignCenter");
	removeClass(elm, "alignRight");
	removeClass(elm, "alignJustify");
	switch(selectedFont) {
		case '1':
			addClass(elm, "alignLeft");
			break;
		case '2':
			//  console.log("biki_2");
			addClass(elm, "alignCenter");
			break;
		case '3':
			//console.log("biki_3");
			addClass(elm, "alignRight");
			break;
		case '4':
			addClass(elm, "alignJustify");
			break;
	}
}
/*function changeFontFamily(fontName)
{

    $("div").each(function(){$(this).css('font-family', fontName);});

    $("span").each(function(){$(this).css('font-family', fontName);});

    $("p").each(function(){$(this).css('font-family', fontName);});

    $("a").each(function(){$(this).css('font-family', fontName);});

    $("td").each(function(){$(this).css('font-family', fontName);});

    $("tr").each(function(){$(this).css('font-family', fontName);});

    $("figcaption").each(function(){$(this).css('font-family', fontName);});

}*/
function changeReaderFontFamily(fontName) {
	var elm = document.documentElement;
	removeClass(elm, "noto")
	removeClass(elm, "sans")
	removeClass(elm, "georgia")
	addClass(elm, fontName)
	ReflowablePageFragment.updateNotesPosition();
}

function updatesticksVr() {
	var notes = document.getElementsByTagName("kitaboonote");
	var leftOffset = 5;
	for(var i = 0; i < notes.length; i++) {
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
		} catch(err) {}
		// console.log('update Vr highlightTop '+highlightTop+" left "+leftOffSet);
	}
}

function setMargin(marginValue) {
	var bodyElement = document.getElementsByTagName('body')[0];
	if(marginValue == "") {
		bodyElement.marginLeft = "";
		bodyElement.marginRight = "";
	} else {
		document.getElementsByTagName("body")[0].style.cssText = "margin-Left:" + marginValue + "px !important;margin-Right:" + marginValue + "px !important";
	}
	ReflowablePageFragment.updateNotesPosition();
}

function setLineHeight(level) {
	var elm = document.documentElement;
	removeClass(elm, "heightLevel1")
	removeClass(elm, "heightLevel2")
	removeClass(elm, "heightLevel3")
	addClass(elm, level)
	ReflowablePageFragment.updateNotesPosition();
}

function highlightAudioElm(wordID, isHrmode, isMobile, isOrientationPortrait) {
	var audioClass = "audioSync";
	removehighlightAudioElm()
	var audioElement = document.getElementById(wordID);
	addClass(audioElement, audioClass)
	getCurrentPageStatus(isHrmode, isMobile, isOrientationPortrait, audioElement)
}

function removehighlightAudioElm() {
	var audioClass = "audioSync";
	var audioelmArr = document.getElementsByClassName(audioClass);
	if(audioelmArr != null && audioelmArr.length > 0) {
		for(var a = 0; a < audioelmArr.length; a++) {
			var audioelm = audioelmArr[a];
			removeClass(audioelm, audioClass)
		}
	}
}

function scrollAudioPage(lastWordID) {
	var lastaudioelm = document.getElementById(lastWordID);
	//lastaudioElm.scrollIntoView({ behavior: 'smooth' });
	var pageTopValue = getAbsPosition(lastaudioelm)[0];
	if(pageTopValue > 100) {
		pageTopValue = pageTopValue - 100;
		window.scrollTo({
			top: pageTopValue,
			behavior: 'smooth'
		});
	}
}

function getCurrentPageStatus(isHrmode, isMobile, isOrientationPortrait, quickElm) {
	var pageTopValue = getAbsPosition(quickElm)[0];
	var pageLeftValue = getAbsPosition(quickElm)[1];
	var pageyValue = quickElm.getBoundingClientRect().y;
	if(lastElmTopPos == 0) {
		lastElmTopPos = pageyValue;
		lastaudioElm = quickElm;
	}
	var totalPagesinCurrentChapter = 0;
	var currentPageNumber = 0;
	var elmPageNumber = 0;
	var clientWidth = window.innerWidth;
	var clientHeight = window.innerHeight;
	if(isHrmode) {
		if(isOrientationPortrait) {
			var currPage = getCurrentAudioPage(isHrmode, isMobile, isOrientationPortrait)
			var pagetop = (currPage * window.innerHeight)
			var eletop = getAbsPosition(quickElm)[0]
			if(isMobile) {
				if(eletop > pagetop) {
					ReflowableViewPager.setAudioCurrentPage(currPage + 1);
				}
			} else {
				eletop = eletop /*+getPadding()*/ ;
				pagetop = (currPage * window.innerHeight);
				if((eletop > pagetop) && (!isInViewRect(quickElm))) {
					ReflowableViewPager.setAudioCurrentPage(currPage + 1);
				}
			}
		} else {
			var currPage = window.pageXOffset / window.innerWidth;
			currPage = currPage + 1;
			//var currJumpPage=getCurrentJumpPage(quickElm, isHrmode,isOrientationPortrait,isMobile);
			var pagetop = (currPage * window.innerHeight * 2)
			var eletop = getAbsPosition(quickElm)[0]
				/*if( currJumpPage>currPage){
				ReflowableViewPager.setAudioCurrentPage(currJumpPage);
				}*/
			if(eletop > pagetop) {
				ReflowableViewPager.setAudioCurrentPage(currPage + 1);
			}
		}
	} else {
		totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
		elmPageNumber = Math.round(pageTopValue / window.innerHeight) + 1;
		currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
		if(!isInViewRect(quickElm) && !isPageInScroll) {
			/*if(quickElm.getBoundingClientRect().y>(window.scrollY+window.innerHeight)){
		quickElm.scrollIntoView({
                				behavior: 'smooth'
                			});
		}else{

        			quickElm.scrollIntoView();
		}*/
			quickElm.scrollIntoView();
		} else {
			isPageInScroll = false;
		}
		// ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
	}
}

function jumpToAudioElm(elm, ishrMode, isOreientationPortrait, isMobile) {
	if(ishrMode) {
		var highlightTop = getAbsPosition(elm)[0];
		var scrollingElement = bodyOrHtml();
		var clientWidth = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			clientWidth = window.innerHeight;
			var currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			var pageIndex = Math.floor(highlightTop / clientWidth);
			if(pageIndex > 0 && !isMobile) pageIndex = pageIndex - 1;
			if(pageIndex > currentPageNumber) {
				jumpCurrentPage(ishrMode, isMobile, isOreientationPortrait)
			}
		} else {
			var pageIndex = Math.floor(highlightTop / clientWidth);
			var newScrollLeft = clientWidth * (pageIndex - 1);
			// scrollingElement.scrollLeft = newScrollLeft;
			ReflowableWebViewPager.setAudioCurrentPage(pageIndex - 1);
		}
		ReflowablePageFragment.heightToMoved(highlightTop, clientWidth);
	} else {
		// highlight.scrollIntoView();
	}
}

function firstVisibleElement(isHrmode, isMobile, isOrientationPortrait, allAudioData) {
	/*var visibleElement=*/
	findVisibleElement(isHrmode, isMobile, isOrientationPortrait, allAudioData);
	//ReflowablePageFragment.highlightFirstVisibleAudioText(visibleElement);
}

function findVisibleElement(isHrmode, isMobile, isOrientationPortrait, allAudioData) {
	var kdns = JSON.parse(allAudioData);
	//var kdns=document.getElementsByTagName('kdn');
	var visibleElement;
	var firstElement;
	if(isHrmode) {
		if(kdns != null && kdns.length > 0) {
			for(var a = 0; a < kdns.length; a++) {
				visibleElement = document.getElementById(kdns[a].wordID)
				if(a != 0 && visibleElement != null /*&&  visibleElement.getBoundingClientRect().y < visibleElement.getBoundingClientRect().y*/ ) {
					// getTopElement(isHrmode, isMobile, isOrientationPortrait,visibleElement)
					if(isInViewRect(visibleElement)) {
						ReflowablePageFragment.highlightFirstVisibleAudioText(visibleElement.id);
						break;
					}
				}
			}
			// return firstElement;
		}
	} else {
		if(kdns != null && kdns.length > 0) {
			for(var a = 0; a < kdns.length; a++) {
				visibleElement = document.getElementById(kdns[a].wordID)
				if(a != 0 && visibleElement != null) {
					var prevElement = document.getElementById(kdns[a - 1].wordID)
					if(isInViewRect(visibleElement)) {
						ReflowablePageFragment.highlightFirstVisibleAudioText(visibleElement.id);
						break;
					}
				}
			}
			// return firstElement;
		}
	}
}

function getCurrentAudioPage(isHrmode, isMobile, isOrientationPortrait) {
	var totalPagesinCurrentChapter = 0;
	var currentPageNumber = 0;
	var clientWidth = window.innerWidth;
	var clientHeight = window.innerHeight;
	if(isHrmode) {
		if(isOrientationPortrait || isMobile) {
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			return currentPageNumber;
			// ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
		} else {
			clientWidth = Math.round(clientWidth);
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			return currentPageNumber;
			//ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
		}
	} else {
		totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
		currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
		return currentPageNumber;
		//ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
	}
}

function getAudioElmPage(elm, ishrMode, isMobile, isOreientationPortrait) {
	var highlightTop = getAbsPosition(elm)[0];
	if(ishrMode) {
		var scrollingElement = bodyOrHtml();
		var clientWidth = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			var pageIndex = Math.floor(highlightTop / window.innerHeight);
			//var newScrollLeft = clientWidth * (pageIndex);
			//scrollingElement.scrollLeft = newScrollLeft;
			return pageIndex;
		} else {
			var pageIndex = Math.floor(highlightTop / clientWidth);
			//var newScrollLeft = clientWidth * (pageIndex - 1);
			// scrollingElement.scrollLeft = newScrollLeft;
			return(pageIndex - 1);
		}
	} else {
		//window.scrollTo({top: highlightTop, behavior: 'smooth'});
	}
}

function jumpCurrentPage(isHrmode, isMobile, isOrientationPortrait) {
	var totalPagesinCurrentChapter = 0;
	var currentPageNumber = 0;
	var clientWidth = window.innerWidth;
	var clientHeight = window.innerHeight;
	if(isHrmode) {
		if(isOrientationPortrait || isMobile) {
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			// ReflowablePageFragment.setAudioCurrentPage(currentPageNumber, totalPagesinCurrentChapter);
			ReflowableViewPager.setAudioCurrentPage(currentPageNumber + 1);
		} else {
			clientWidth = Math.round(clientWidth / 2);
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			// ReflowablePageFragment.setAudioCurrentPage(currentPageNumber, totalPagesinCurrentChapter);
			ReflowableViewPager.setAudioCurrentPage(currentPageNumber + 1);
		}
	} else {
		/* totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
		 currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
		 ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);*/
	}
}

/*function getCurrentPage(isHrmode, isMobile, isOrientationPortrait) {
	var totalPagesinCurrentChapter = 0;
	var currentPageNumber = 0;
	var clientWidth = window.innerWidth;
	var clientHeight = window.innerHeight;
	if(isHrmode) {
		if(isOrientationPortrait || isMobile) {
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
		} else {
			clientWidth = Math.round(clientWidth / 2);
			totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
			currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
			ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
		}
	} else {
		totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
		currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
		ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter);
	}
}*/

function getCurrentJumpPage(audelement, ishrMode, isOreientationPortrait, isMobile) {
	var clientWidth = (window.innerHeight);
	try {
		var pageScrollValue = window.innerWidth;
		var top = audelement;
		var topoffset = getAbsPosition(top)[0];
		var pageIndex = Math.floor(getAbsPosition(top)[0] / pageScrollValue);
		if(ishrMode) {
			return pageIndex + 1
		} else {
			// window.scrollTo(0, topoffset);
		}
	} catch(err) {
		var pageScrollValue = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			pageScrollValue = window.innerHeight;
		}
		var top = audelement;
		var pageIndex = Math.floor(getAbsPosition(top)[0] / pageScrollValue);
		if(ishrMode) {
			var leftvalue = (pageIndex * pageScrollValue);
			if(pageIndex > 0 && !isMobile) pageIndex = pageIndex - 1;
			return pageIndex + 1
				// ReflowablePageFragment.setPageIndex(pageIndex);
		} else {
			//top.scrollIntoView();
		}
	}
}

function isInViewRect(elem) {
	var bounding = elem.getBoundingClientRect();
	if(bounding.bottom == 0) {
		return false;
	}
	return(bounding.top >= 0 && bounding.left >= 0 && bounding.bottom <= (window.innerHeight || document.documentElement.clientHeight) && bounding.right <= (window.innerWidth || document.documentElement.clientWidth))
}

function getPadding() {
	var currentPadding = parseInt(window.getComputedStyle(document.body).paddingTop) + parseInt(window.getComputedStyle(document.body).paddingBottom);
	return currentPadding;
}

function isElementInView(id) {
    var audioE = document.getElementById(id);
	var bounding = audioE.getBoundingClientRect();
    	if(bounding.bottom == 0) {
    		ReflowablePageFragment.getElementState(false);
    	}
    	ReflowablePageFragment.getElementState((bounding.top >= 0 && bounding.left >= 0 && bounding.bottom <= (window.innerHeight || document.documentElement.clientHeight) && bounding.right <= (window.innerWidth || document.documentElement.clientWidth)));
}