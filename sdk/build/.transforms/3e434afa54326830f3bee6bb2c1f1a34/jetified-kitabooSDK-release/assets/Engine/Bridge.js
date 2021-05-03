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
window.addEventListener('scroll', debounce(onScrollStop, 300, false));

function clearSelection() {
	if(window.getSelection) window.getSelection().removeAllRanges();
	else if(document.selection) document.selection.empty();
}

function addListner() {
	var body = document.body;
	body.setAttribute("onselectionchange", "onSelectionChange()");
}

function stringRet(isHrmode) {
	var lastvistedpageobject = currentPosition(isHrmode);
	var lastVisitedCfiPagepath = getCFINodeString(isHrmode);
	//ReflowablePageFragment.lastVisitedPageData(lastvistedpageobject, lastVisitedCfiPagepath);
	ReflowablePageFragment.currentRangeAndCFIstring(lastvistedpageobject, lastVisitedCfiPagepath);
}

function currentPosition(isHrmode) {
	var range = document.caretRangeFromPoint(0, 0);
	// console.log(" origin current postion " + range.startOffset + " " + range.endOffset);
	var preSelectionRange = range.cloneRange();
	preSelectionRange.selectNodeContents(document.body);
	preSelectionRange.setEnd(range.startContainer, range.startOffset);
	var start = preSelectionRange.toString().length;
	var string = JSON.stringify({
		start: start,
		end: start + range.toString().length
	});
	// console.log("current postion " + string);
	return string;
}

function clearSearch() {
	removeAllSearchElelemt(document.body);
}

function removeAllSearchElelemt(element) {
	if(element) {
		if(element.nodeType == 1) {
			if(element.getAttribute("class") == "searchhighlight") {
				var text = element.removeChild(element.firstChild);
				element.parentNode.insertBefore(text, element);
				element.parentNode.removeChild(element);
				return true;
			} else {
				var normalize = false;
				for(var i = element.childNodes.length - 1; i >= 0; i--) {
					if(removeAllSearchElelemt(element.childNodes[i])) {
						normalize = true;
					}
				}
				if(normalize) {
					element.normalize();
				}
			}
		}
	}
	return false;
}

function removeElementsByClass(className) {
	var elements = document.getElementsByClassName(className);
	while(elements.length > 0) {
		elements[0].parentNode.removeChild(elements[0]);
	}
}

function doSearch(needle, keyword, isHrMode, isOreientationPortrait, isMobile,color) {
	removeAllSearchElelemt(document.body);
	removeTempSearch();
	removeAllSearchElelemt(document.body);
	needle = needle.replace(/\s/g, '').toLowerCase();
	var textNodes = visibleTextNodes();
	for(var i = 0, texts = []; i < textNodes.length; i++) {
		texts.push(textNodes[i].nodeValue.replace(/\s/g, '').toLowerCase());
	}
	var matchStart = texts.join('').indexOf(needle);
	if(matchStart < 0) {
		return false;
	}
	var nodeAndOffsetAtPosition = function(position) {
		for(var i = 0, consumed = 0; consumed + texts[i].length < position; i++) {
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
	for(var i = 0; i < safeRanges.length; i++) {
		var element = document.createElement('kitaboospan');
		element.setAttribute('class', 'tempsearch');
		safeRanges[i].surroundContents(element);
	}
	var getAll = document.getElementsByClassName('tempsearch');
	for(var i = 0; i < getAll.length; i++) {
		var text = getAll[i].innerHTML;
		var searchMask = keyword;
		var regEx = new RegExp(searchMask, "ig");
		var replaceMask = '<kitaboospan class="searchhighlight" style="background:' + color + ' !important;color: white !important;">' + keyword + '</kitaboospan>';
		var result = text.replace(regEx, replaceMask);
		getAll[i].innerHTML = result;
	}
	var searchele = getAll[0];
	if(isHrMode) {
		/*var scrollingElement = bodyOrHtml();

		//searchele.scrollIntoView();
		var clientWidth = window.innerWidth;
		var pageIndex = Math.ceil((searchele.offsetLeft) / clientWidth);
		var newScrollLeft = clientWidth * (pageIndex - 1);
		//console.log("-> newScrollLeft = " + newScrollLeft);
		scrollingElement.scrollLeft = newScrollLeft;
		ReflowableViewPager.setCurrentPage(pageIndex - 1);*/
		var highlightTop = getAbsPosition(searchele)[0];
		var scrollingElement = bodyOrHtml();
		var clientWidth = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			var pageIndex = Math.floor(highlightTop / window.innerHeight);
			var newScrollLeft = clientWidth * (pageIndex);
			scrollingElement.scrollLeft = newScrollLeft;
		} else {
			var pageIndex = Math.floor(highlightTop / clientWidth);
			var newScrollLeft = clientWidth * (pageIndex - 1);
			scrollingElement.scrollLeft = newScrollLeft;
			ReflowableViewPager.setCurrentPage(pageIndex - 1);
		}
	} else {
		searchele.scrollIntoView();
	}
	setTimeout(function() {
		removeAllSearchElelemt(document.body);
		removeTempSearch();
	}, 10000 * 5);
	cleareSelection();
}

function removeTempSearch() {
	var allElements = document.getElementsByClassName('tempsearch');
	while(allElements.length) {
		var parent = allElements[0].parentNode;
		while(allElements[0].firstChild) {
			parent.insertBefore(allElements[0].firstChild, allElements[0]);
		}
		parent.removeChild(allElements[0]);
	}
}

function visibleTextNodes() {
	var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, function(node) {
		if(node.nodeType == node.TEXT_NODE) {
			return NodeFilter.FILTER_ACCEPT;
		} else {
			return NodeFilter.FILTER_REJECT;
		}
	}, false);
	for(var nodes = []; walker.nextNode();) {
		nodes.push(walker.currentNode);
	}
	return nodes;
}
window.onload = function() {

  updatetables();
}

function updateimages(isCover) {
  var images = document.getElementsByTagName('img');
  var pageHeight = document.documentElement.clientHeight
  var pageWidth = document.documentElement.clientWidth
  for (var i = 0; i < images.length; i++) {
    if (images[i].getBoundingClientRect().height >= window.innerHeight)
      images[i].setAttribute('style', "height:" + (pageHeight * .9) + "px;" + "max-height:" + (pageHeight * .9) + "px;");
    if (images[i].getBoundingClientRect().width >= window.innerWidth)
      images[i].setAttribute('style', "width:" + (pageWidth * .8) + "px !important;" + "max-width:" + (pageWidth * .8) + "px !important;" + "min-width:" + (pageWidth * .8) + "px !important;");

    if (isCover)
      addClass(images[i], "coverMode")
  }
}

function updatetables() {
	var tables = document.getElementsByTagName('table');
	var pageHeight = document.documentElement.clientHeight
	for(var i = 0; i < tables.length; i++) {
		if(tables[i].getBoundingClientRect().height > window.innerHeight) tables[i].setAttribute('style', "height:" + (pageHeight * .9) + "px;" + "max-height:" + (pageHeight * .9) + "px;");
	}
}

function updateimageWidth() {
	var images = document.getElementsByTagName('img');
	var pageWidth = (document.documentElement.clientWidth)
	for(var i = 0; i < images.length; i++) {
		images[i].setAttribute('style', "width:" + (pageWidth * .82) + "px;" + "max-width:" + (pageWidth * .82) + "px;");
	}
}

function updateimageHeightMobilePort() {
	var images = document.getElementsByTagName('img');
	var pageHeight = document.documentElement.clientHeight
	for(var i = 0; i < images.length; i++) {
		images[i].setAttribute('style', "height:" + (pageHeight * .7) + "px;" + "max-height:" + (pageHeight * .7) + "px;");
	}
}
var pageno = 0;
var linkcolor = "color: #0000EE ";
document.addEventListener("DOMContentLoaded", function() {
	addListner();
	var links = document.getElementsByTagName("a");
	for(var i = 0; i < links.length; i++) {
		if(links[i].getAttribute('href')) {
			links[i].setAttribute("onclick", "onLinkClick(this,'" + links[i].href + "')");
			// Need to remove href for blocking chapter navigation of book package
			links[i].removeAttribute("href");
			links[i].setAttribute("style", linkcolor);
		}
	}
}, false);

function onImageLinkClick(event, data) {
	// console.log("onImageLinkClick called");
	if(event.getAttribute("data-dblclick") == null) {
		event.setAttribute("data-dblclick", 1);
		setTimeout(function() {
			if(event.getAttribute("data-dblclick") == 1) {}
			event.removeAttribute("data-dblclick");
		}, 500);
	} else {
		event.removeAttribute("data-dblclick");
		onLinkClick(event, data);
	}
}

function onLinkClick(event, data) {
	// if data have www or http or https then
	if((data.indexOf("http:") != -1) || (data.indexOf("https:") != -1)) {
		ReflowablePageFragment.onLinkClick(true, data);
	} else if(window.location.pathname.indexOf(data) != -1) {
		ReflowablePageFragment.onLinkClick(false, data);
	} else {
		ReflowablePageFragment.onLinkClick(false, data);
	}
	return true;
}

function onSelectionChange() {
	//console.log("onSelectionChange_ called");
	var selection = window.getSelection();
	if(selection) {
		//console.log("rangecount" + selection.rangeCount);
		if(selection.rangeCount != 0) {
			var range = selection.getRangeAt(0);
			if(range) {
				var containerElement = range.commonAncestorContainer;
				if(containerElement.nodeType != 1) {
					containerElement = containerElement.parentNode;
				}
				if(containerElement) {
					if(containerElement.id == "") {
						var wordID = containerElement.parentElement.id
						if(wordID != "") {
							ReflowablePageFragment.selectedWordId(wordID);
						}
					} else {
						ReflowablePageFragment.selectedWordId(containerElement.id);
					}
				}
			}
			if(!(range.startContainer == range.endContainer && range.startOffset == range.endOffset)) {
				var rect = range.getBoundingClientRect();
				if(rect.width != 0 && rect.height != 0) {
					var clonedSelection = range.cloneContents();
					ReflowablePageFragment.selectionRect(clonedSelection.textContent.replace(/[\n\t\r]/g, ""), rect.left, rect.top, rect.right, rect.bottom, rect.width, rect.height);
					// console.log("selectionRect:-" + rect.left + rect.top + rect.right + rect.bottom + rect.width + rect.height);
				}
			}
		}
	}
  //console.log("onSelectionChange_ called");
  var selection = window.getSelection();
  if (selection) {
    //console.log("rangecount" + selection.rangeCount);
    if (selection.rangeCount != 0)

    {
      var range = selection.getRangeAt(0);
      if (!(range.startContainer == range.endContainer && range.startOffset == range.endOffset)) {
        var rect = range.getBoundingClientRect();
        if (rect.width != 0 && rect.height != 0) {
          var clonedSelection = range.cloneContents();
          ReflowablePageFragment.selectionRect(clonedSelection.textContent.replace(/[\n\t\r]/g, ""), rect.left, rect.top, rect.right, rect.bottom, rect.width, rect.height);
          // console.log("selectionRect:-" + rect.left + rect.top + rect.right + rect.bottom + rect.width + rect.height);
        }
      }
    }
  }
}
//@@@@@@@@@@@@
//    Returns selection range object
//    Exp: {start: 10, end: 20}
//    start: start offset of range
//    end: end offset of range
//@@@@@@@@@@@@
function getSelectionRange() {
	//console.log("getSelectionRange_called :");
	var range = window.getSelection().getRangeAt(0);
	var preSelectionRange = range.cloneRange();
	preSelectionRange.selectNodeContents(document.body);
	preSelectionRange.setEnd(range.startContainer, range.startOffset);
	var start = preSelectionRange.toString().length;
	var string = JSON.stringify({
		start: start,
		end: start + range.toString().length
	});
	ReflowablePageFragment.selectionRange(string);
}

function getSelectionText() {
	var range = window.getSelection().getRangeAt(0);
	range.cloneRange();
	var text = range.cloneContents().textContent;
	ReflowablePageFragment.selectionText(text);
	//return text;
	ReflowablePageFragment.onSelectionText(text);
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
	ReflowablePageFragment.selectionRangeForNote(string, highlightText);
}

function getRangeText(safeRanges) {
	var text = '';
	for(var i = 0; i < safeRanges.length; i++) {
		text += highlightRange(safeRanges[i], name);
	}
	return text;
}

function getHighlightedText(rangeOffset) {
	rangeOffset = JSON.parse(rangeOffset);
	var range = restoreSelection(rangeOffset);
	var highlightText = range.cloneContents().textContent;
	ReflowablePageFragment.highlightedTextForNote(highlightText);
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
	while(!stop && (node = nodeStack.pop())) {
		if(node.nodeType == 3) {
			var nextCharIndex = charIndex + node.length;
			if(!foundStart && savedSel.start >= charIndex && savedSel.start <= nextCharIndex) {
				range.setStart(node, savedSel.start - charIndex);
				foundStart = true;
			}
			if(foundStart && savedSel.end >= charIndex && savedSel.end <= nextCharIndex) {
				range.setEnd(node, savedSel.end - charIndex);
				stop = true;
			}
			charIndex = nextCharIndex;
		} else {
			var i = node.childNodes.length;
			while(i--) {
				nodeStack.push(node.childNodes[i]);
			}
		}
	}
	return range;
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
	for(var i = 0; i < safeRanges.length; i++) {
		text += highlightRange(safeRanges[i], name);
	}
	addHighlightAnchor(name);
	ReflowablePageFragment.textHighlighted(text);
	cleareSelection();
}
//@@@@@@@@@@@@
//    Change Highlight Color
//@@@@@@@@@@@@
var normalHighlight = "background-color: #F4D631 !important;display:initial;";
var importantHighlight = "background-color: #F06666 !important;display:initial;";
var sharedHighlight = "background-color: #92cadc !important;";

function editHighlight(name, hasNote, color, ishorizontalview, isMobile, isOreientationPortrait) {
	//console.log("biki_editHighlight");
	var highlights = document.getElementsByName(name);
	var rgbColor = hexToRgb(color , 0.5 );
	var styleText = "background-color:" + rgbColor + "!important; ";
	for(var i = 0; i < highlights.length; i++) {
		var highlight = highlights[i];
		highlight.setAttribute("style", styleText);
	}
	if(hasNote) {
		addNoteOnHighlight(name, color, ishorizontalview, isMobile, isOreientationPortrait);
	}
	ReflowablePageFragment.highlightDrawCompleted();
}
//@@@@@@@@@@@@
//    Delete or unwrap all highlight spans having given name attribute
//@@@@@@@@@@@@
function clearHighlight(name) {
	var highlights = document.getElementsByName(name);
	while(highlights.length) {
		var highlight = highlights[0];
		var parent = highlight.parentNode;
		while(highlight.firstChild) {
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

function clearAllHighlight(jsonar, ishrmode) {
	removeallstickynotes();
	var data = JSON.parse(jsonar);
	for(var i = 0; i < data.length; i++) {
		var stWord = data[i].mStartWordId;
		var edWord = data[i].mEndWordId;
		var elemnt = stWord + "_" + edWord;
		clearHighlight(elemnt);
	}
}

function addNoteOnHighlight(name, coloris, ishorizontalview, isMobile, isOreientationPortrait) {
	var color = coloris;
	var highlight = document.getElementsByName(name)[0];
	var noteId = "note_" + name;
	var kitabooNote = document.getElementById(noteId);
	try {
		if(kitabooNote != null) {
			kitabooNote.parentNode.removeChild(kitabooNote);
		}
		kitabooNote = document.createElement("kitabooNote");
		kitabooNote.setAttribute('id', noteId);
		kitabooNote.innerText = "V";
		kitabooNote.setAttribute('onclick', "kitabooNoteTapped(event)");
		var highlightOffSet = getAbsPosition(highlight)[0];
		var pageNumber = Math.ceil(parseInt(highlightOffSet) / document.body.clientHeight);
		var rgbColor = hexToRgb(color , 0.5 );
		if(ishorizontalview) {
			//  console.log("is horizontal ");
			var highlightTop = cumulativeOffset(highlight).top;
			var highlightLeft = "5";
			var highlightOffSet = parseInt(highlightTop);
			var docWidth = document.documentElement.clientWidth;
			var docHeight = window.innerHeight;
			var leftOffSet = 5;
			var clientWidth = 0;
			var leftMargin;
			clientWidth = (window.innerHeight);
			var pageLeftValue = getAbsPosition(highlight)[0];
			var pageIndex = Math.floor(getAbsPosition(highlight)[0] / clientWidth);
			// var leftMargin = (pageIndex * window.innerWidth) + leftOffSet;
			if(isOreientationPortrait || isMobile) {
				if(pageIndex != 0) {
					if(pageLeftValue > window.innerWidth && pageLeftValue < window.innerWidth * 2) {
						pageIndex = 1;
					}
				}
				leftMargin = (pageIndex * window.innerWidth) + leftOffSet;
			} else {
				var pageScrollValue = window.innerWidth / 2;
				leftMargin = (pageIndex * pageScrollValue) + leftOffSet;
			}
			if(window.innerHeight > 600) {
				kitabooNote.setAttribute('style', "font-size: 20px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + leftMargin + "px;font-size:30px;background-color:" + rgbColor + ";display: block;top:" + highlightOffSet + "px");
			} else {
				kitabooNote.setAttribute('style', "font-size: 20px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;left:" + leftMargin + "px;font-size:27px;background-color:" + rgbColor + ";display: block;top:" + highlightOffSet + "px");
			}
		} else {
			//  console.log("is not horizontal ");
			var highlightTop = getAbsPosition(highlight)[0];
			//  console.log("is not horizontal_top value1" + highlightTop);
			var highlightOffSet = parseInt(highlightTop);
			var leftOffSet = 0;
			var left = leftOffSet;
			// console.log("scale value is " + scalePercentage);
			color = color + "!important;display:initial;"
			if(window.innerHeight > 600) {
				//  console.log("window.innerHeight > 600 color " + color);
				kitabooNote.setAttribute('style', "font-size: 20px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left + "px;background-color:" + rgbColor + ";display: block;top:" + highlightTop / scalePercentage + "px");
				//  console.log("window.innerHeight-val " + left + highlightTop);
			} else {
				//  console.log("else " + color);
				kitabooNote.setAttribute('style', "font-size: 20px !important;text-align:center;z-index: 1;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:" + left + "px;background-color:" + rgbColor + ";display: block;top:" + highlightTop / scalePercentage + "px");
			}
		}
		if(ishorizontalview) {
			document.documentElement.appendChild(kitabooNote);
		} else {
			document.body.appendChild(kitabooNote);
		}
		return "{\"name\":" + kitabooNote + "}";
	} catch(err) {}
}

function removeallstickynotes() {
	var notes = document.getElementsByTagName("kitabooNote");
	for(var i = 0; i < notes.length; i++) {
		notes[i].setAttribute('style', "text-align:center;vertical-align:middle;font-family:kitabooread;position: absolute;margin-left:5px;font-size:27px;background-color:" + notes[i].getAttribute('background-color') + ";display: block;top:5px");
	}
}

function remove(elem) {
	return elem.parentNode.removeChild(elem);
}
//@@@@@@@@@@@@
//    Delete note on page
//@@@@@@@@@@@@
function deleteNoteOnHighlight(name) {
	var noteId = "note_" + name;
	var kitabooNote = document.getElementById(noteId);
	if(kitabooNote != null) {
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
	if(anchor == null) {
		anchor = document.createElement("kitaboospan");
		anchor.setAttribute("id", anchorId);
		anchor.setAttribute("name", anchorId);
		if(!(typeof highlight === 'undefined')) {
			highlight.parentNode.insertBefore(anchor, highlight);
		}
		//  highlight.parentNode.insertBefore(anchor, highlight);
	}
}
//@@@@@@@@@@@@
//    Delete UGC Anchor
//@@@@@@@@@@@@
function deleteHighlightAnchor(name) {
	var anchorId = "ugc_" + name;
	var anchor = document.getElementById(anchorId);
	if(anchor != null) {
		anchor.parentNode.removeChild(anchor);
	}
}
//@@@@@@@@@@@@
//    Start highlighting rangehighlightTaped
//@@@@@@@@@@@@
function highlightRange(range, name) {
	var newNode = document.createElement("kitaboospan");
	newNode.setAttribute("name", name);
	newNode.setAttribute('onclick', "kitabooHighlightTapped(event)");
	if(range.toString().trim().length > 0) {
		range.surroundContents(newNode);
	}
	return newNode.innerText;
}
//@@@@@@@@@@@@
//    Returns an array of range objects obtained from range object
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
	if(startContainer == endContainer) {
		return [range];
	} else {
		for(var i = startContainer; i != commonAncestorContainer; i = i.parentNode) {
			startArray.push(i);
		}
		for(var i = endContainer; i != commonAncestorContainer; i = i.parentNode) {
			endArray.push(i);
		}
	}
	if(0 < startArray.length) {
		for(var i = 0; i < startArray.length; i++) {
			if(i) {
				var node = startArray[i - 1];
				while((node = node.nextSibling) != null) {
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
	if(0 < endArray.length) {
		for(var i = 0; i < endArray.length; i++) {
			if(i) {
				var node = endArray[i - 1];
				while((node = node.previousSibling) != null) {
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
	for(var i = topStartNode.nextSibling; i != topEndNode; i = i.nextSibling) {
		rangeArray = rangeArray.concat(getRangeOfChildNodes(i));
	}
	return rangeArray;
}

function getRangeOfChildNodes(node) {
	var rangeArray = new Array(0);
	if(node.nodeType == Node.TEXT_NODE) {
		var xm = document.createRange();
		xm.setStartBefore(node);
		xm.setEndAfter(node);
		rangeArray.push(xm);
	} else {
		for(var i = 0; i < node.childNodes.length; i++) {
			var childNode = node.childNodes[i];
			rangeArray = rangeArray.concat(getRangeOfChildNodes(childNode));
		}
	}
	return rangeArray;
}
//@@@@@@@@@@@@
//    On click events for note and highlight tap
//@@@@@@@@@@@@
function kitabooNoteTapped(event) {
	event.stopPropagation();
	var kitabooNote = event.target;
	var id = kitabooNote.id.split("_");
	ReflowablePageFragment.noteTaped(id[1], id[2]);
}

function kitabooHighlightTapped(event) {
	event.stopPropagation();
	// console.log("kitabooHighlightTapped called");
	var kitabooHighlight = event.target;
	if(!hasClass(kitabooHighlight, "sharedHighlight")) {
		var y = event.clientY;
		var x = event.clientX;
		var id = kitabooHighlight.getAttribute("name").split("_");
		ReflowablePageFragment.highlightTaped(kitabooHighlight.textContent, id[0], id[1], Number(x), Number(y));
	}
}

function getSearchelement() {
	var ele = document.createElement('kitaboospan');
	ele.setAttribute('class', 'searchelement');
	return ele;
}

function cleareSelection() {
	if(window.getSelection) {
		if(window.getSelection().empty) { // Chrome
			window.getSelection().empty();
		} else if(window.getSelection().removeAllRanges) { // Firefox
			window.getSelection().removeAllRanges();
		}
	} else if(document.selection) { // IE?
		document.selection.empty();
	}
}
var lastelement = 0;

function scrollToSpan(usingId, value) {
	if(usingId) {
		var spanElement = document.getElementsByName(value);
		if(spanElement) scrollToElement(spanElement);
	} else {
		var spanCollection = document.querySelectorAll("span.sentence");
		if(spanCollection.length == 0 || value < 0 || value >= spanCollection.length || value == null) {
			//console.log("return", spanCollection.length);
			return;
		}
		scrollToElement(spanCollection[value]);
	}
}

function scrollToElement(element) {
	var scrollingElement = bodyOrHtml();
	if(ReflowablePageFragment.getDirection() == "VERTICAL") {

	if(!isElementVisible(element)) {
                element.scrollIntoView();
    		}

		/*var topDistraction = ReflowablePageFragment.getTopDistraction();
		var pageTop = 1234 + topDistraction;
		//ReflowablePageFragment.spanElement(scrollingElement.scrollTop);
		var pageBottom = scrollingElement.scrollTop + document.documentElement.clientHeight - ReflowablePageFragment.getBottomDistraction();
		var elementTop = element.offsetTop - 20;
		elementTop = elementTop < 0 ? 0 : elementTop;
		var elementBottom = element.offsetTop + element.offsetHeight + 20;
		var needToScroll = (elementTop < pageTop || elementBottom > pageBottom);
		if(needToScroll) {
			var newScrollTop = elementTop - topDistraction;
			newScrollTop = newScrollTop < 0 ? 0 : newScrollTop;
			scrollingElement.scrollTop = newScrollTop;
		}else if(!isElementVisible(element)) {
            var newScrollTop = elementTop - topDistraction;
			newScrollTop = newScrollTop < 0 ? 0 : newScrollTop;
			scrollingElement.scrollTop = newScrollTop;
		}*/

	} else if(ReflowablePageFragment.getDirection() == "HORIZONTAL") {
		var clientWidth = document.documentElement.clientWidth;
		var pageIndex = Math.floor(element.offsetLeft / clientWidth);
		var newScrollLeft = clientWidth * pageIndex;
		scrollingElement.scrollLeft = newScrollLeft;
		ReflowableViewPager.setCurrentPage(pageIndex);
	}
	return element;
}

function jumptolastVisitedPage(stringid, ishr) {
	if(stringid == null) {
		window.scrollTo(0, 0);
	} else {
		try {
			var rangeobj = JSON.parse(stringid);
			var id = rangeobj.start + "_" + rangeobj.end;
			var highlight = document.getElementsByName(id)[0];
			var top = getAbsPosition(highlight)[0];
			highlight.scrollIntoView();
		} catch(err) {}
	}
}
var vrpageno = 0;
var webheight = 0;
var webwidth = 0;
var scrollpostion = 0;
var anchorid = 0;

function clearAllHighlightAndGetPostion(jsonar, ishrmode) {
	removeallstickynotes();
	var data = JSON.parse(jsonar);
	for(var i = 0; i < data.length; i++) {
		var stWord = data[i].mStartWordId;
		var edWord = data[i].mEndWordId;
		var elemnt = stWord + "_" + edWord;
		clearHighlight(elemnt);
	}
	stringRet(ishrmode);
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
	if(!isPortrait && isNeedBlankPage && isCoverPage && !isMobile) {
		//updateimages()
		var pageWidth = (document.documentElement.clientWidth / 2)
		var pageHeight = document.documentElement.clientHeight
		var emptypage = document.createElement('kitaboospan');
		emptypage.setAttribute('id', 'kitabooemptypage');
		emptypage.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
		var emptypage2 = document.createElement('kitaboospan');
		emptypage2.setAttribute('id', 'kitabooemptypage2');
		emptypage2.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
		document.body.insertBefore(emptypage, document.body.firstChild);
	} else if(isNeedBlankPage && isCoverPage && isMobile) {
		//updateimages()
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
	if(isPortrait || isMobile) {
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
	if(scrollWidth > clientWidth && scrollWidth > window.offsetWidth) {
		scrollWidth += paddingRight;
	}
	var newBodyWidth = clientWidth - (paddingLeft + paddingRight);
	window.scrollWidth = scrollWidth;
	if(isNeedBlankPage) {
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
	if(pageCount != pageCountFloat) {
		/*console.warn("-> pageCount = " + pageCount + ", pageCountFloat = " + pageCountFloat +
		    ", Something wrong in pageCount calculation");*/
	}
	ReflowablePageFragment.setHorizontalPageCount(pageCount);
}

function horizontalRecheck() {
	horizontalIntervalCounter += horizontalIntervalPeriod;
	if(window.scrollWidth != document.documentElement.scrollWidth) {
		// Rare condition
		// This might happen when document.documentElement.scrollWidth gives incorrect value
		// when the webview is busy re-drawing contents.
		/* console.warn("-> scrollWidth changed from " + window.scrollWidth + " to " +
		     document.documentElement.scrollWidth);*/
		postInitHorizontalDirection(needBlankPage);
	}
	if(horizontalIntervalCounter >= horizontalIntervalLimit) clearInterval(horizontalInterval);
}

function bodyOrHtml() {
	if('scrollingElement' in document) {
		return document.scrollingElement;
	}
	// Fallback for legacy browsers
	if(navigator.userAgent.indexOf('WebKit') != -1) {
		return document.body;
	}
	return document.documentElement;
}

function scrollToLast(direction) {
	var scrollingElement = bodyOrHtml();
	if(direction == "VERTICAL") {
		scrollingElement.scrollTop = scrollingElement.scrollHeight - document.documentElement.clientHeight;
	} else if(direction == "HORIZONTAL") {
		scrollingElement.scrollLeft = scrollingElement.scrollWidth - document.documentElement.clientWidth;
		ReflowableViewPager.setPageToLast();
	}
}

function jumpToPage(rangeOffset, ishrMode, isOreientationPortrait, isMobile) {
	rangeOffset = JSON.parse(rangeOffset);
	var range = restoreSelection(rangeOffset);
	var name = rangeOffset.start + "_" + rangeOffset.end;
	var text = "";
	var safeRanges = getSafeRanges(range);
	for(var i = 0; i < safeRanges.length; i++) {
		text += highlightRange(safeRanges[i], name);
	}
	var highlight = document.getElementsByName(name)[0];
	var anchorId = "ugc_" + name;
	var anchor = document.getElementById(anchorId);
	if(anchor == null) {
		anchor = document.createElement("kitaboospan");
		anchor.setAttribute("id", anchorId);
		anchor.setAttribute("name", anchorId);
		highlight.parentNode.insertBefore(anchor, highlight);
	}
	if(ishrMode) {
		var highlightTop = getAbsPosition(highlight)[0];
		var scrollingElement = bodyOrHtml();
		var clientWidth = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			var pageIndex = Math.floor(highlightTop / window.innerHeight);
			var newScrollLeft = clientWidth * (pageIndex);
			scrollingElement.scrollLeft = newScrollLeft;
			ReflowableViewPager.setCurrentPage(pageIndex);
		} else {
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
	for(var i = 0; i < data.length; i++) {
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
	for(var i = 0; i < safeRanges.length; i++) {
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

function highlightSearchResult(searchQuery, tmpBackgroundColor, searchHighlightedColor, searchPosition) {
	mElasticTmpBackgroundColor = tmpBackgroundColor;
	mSearchHighlightedColor = searchHighlightedColor;
	mSearchPosition = searchPosition;
	cleareSelection();
	removeSearchElelemt(document.body);
	makeSearchResultsInvisible();
	resetSearchResults();
	searchResults = applySearchResultClass(searchQuery);
	ReflowablePageFragment.searchCount(searchResults.length);
	applySearchResultVisibleClass();
}

function clearAllElasticSearchResult() {
	for(var i = 1; i <= searchResults.length; i++) {
		searchResult = searchResults[i - 1];
		if(searchResult != undefined) {
			searchResult.className = "search-result-invisible";
			searchResultsInvisible = false;
			searchResult.style.backgroundColor = 'transparent';
			var styleText = "background-color:#00ff0000 !important;";
			searchResult.setAttribute("style", styleText);
		}
	}
	makeSearchResultsInvisible();
	removeSearchElelemt(document.body);
	cleareSelection();
}

function makeSearchResultsInvisible() {
	if(searchResultsInvisible) return;
	for(var i = 0; i < searchResults.length; i++) {
		if(searchResults[i].className == "search-result-visible") searchResults[i].className = "search-result-invisible";
	}
	searchResultsInvisible = true;
}

function resetSearchResults() {
	for(var i = 0; i < searchResults.length; i++) {
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
	for(var i = 0; i < elementsInBody.length; i++) {
		var childNodes = elementsInBody[i].childNodes;
		for(var j = 0; j < childNodes.length; j++) {
			if(childNodes[j].nodeType == Node.TEXT_NODE && childNodes[j].nodeValue.trim().length) {
				if(childNodes[j].nodeValue.match(searchQueryRegExp)) {
					searchChildNodesArray.push(getSearchChildNodes(childNodes[j].nodeValue, searchQuery));
					elementArray.push(elementsInBody[i]);
					textNodeArray.push(childNodes[j]);
				}
			}
		}
	}
	for(var i = 0; i < searchChildNodesArray.length; i++) {
		var searchChildNodes = searchChildNodesArray[i];
		for(var j = 0; j < searchChildNodes.length; j++) {
			if(searchChildNodes[j].className == "search-result") searchResults.push(searchChildNodes[j]);
			elementArray[i].insertBefore(searchChildNodes[j], textNodeArray[i]);
		}
		elementArray[i].removeChild(textNodeArray[i]);
	}
	lastSearchQuery = searchQuery;
	return searchResults;
}
var searchResult;

function applySearchResultVisibleClass() {
	for(var i = 1; i <= searchResults.length; i++) {
		searchResult = searchResults[i - 1];
		if(searchResult != undefined) {
			searchResult.className = "search-result-visible";
			searchResultsInvisible = false;
			var styleText;
			if(i == mSearchPosition) {
				styleText = "background-color:" + mSearchHighlightedColor + "!important;";
			} else {
				styleText = "background-color:" + mElasticTmpBackgroundColor + "!important;";
			}
			searchResult.setAttribute("style", styleText);
		}
	}
	if(searchResults[mSearchPosition - 1]) {
		scrollToElement(searchResults[mSearchPosition - 1]);
	}
	applySearchResultonCurrentClass()
}

function applySearchResultonCurrentClass() {
	for(var i = 1; i <= searchResults.length; i++) {
		searchResult = searchResults[i - 1];
		if(searchResult != undefined) {
			searchResult.className = "search-result-visible";
			searchResultsInvisible = false;
			var styleText;
			if(i == mSearchPosition) {
			//scrollToElement(searchResult);
				styleText = "background-color:" + '#800080' + "!important;";
				searchResult.setAttribute("style", styleText);
			}

		}
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
	for(var i = 0, j = 0; i < text.length; i++) {
		textChunk += text[i];
		if(text[i].match(new RegExp(escapeRegExp(searchQuery[j]), "i"))) {
			if(matchIndexStart == -1) matchIndexStart = i;
			if(searchQuery.length == j + 1) {
				var textNode = document.createTextNode(textChunk.substring(0, textChunk.length - searchQuery.length));
				var searchNode = document.createElement("kitaboospan");
				searchNode.className = "search-result";
				var queryTextNode = document.createTextNode(text.substring(matchIndexStart, matchIndexStart + searchQuery.length));
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
	if(textChunk !== "") {
		var textNode = document.createTextNode(textChunk);
		searchChildNodes.push(textNode);
	}
	return searchChildNodes;
}

function removeSearchElelemt(element) {
	if(element) {
		if(element.nodeType == 1) {
			if(element.getAttribute("class") == "search-result-visible") {
				var text = element.removeChild(element.firstChild);
				element.parentNode.insertBefore(text, element);
				element.parentNode.removeChild(element);
				return true;
			} else {
				var normalize = false;
				for(var i = element.childNodes.length - 1; i >= 0; i--) {
					if(removeAllSearchElelemt(element.childNodes[i])) {
						normalize = true;
					}
				}
				if(normalize) {
					element.normalize();
				}
			}
		}
	}
	return false;
}

function jumpToTOCAnchor(inputhref, ishrMode, isOreientationPortrait, isMobile) {
	var clientWidth = (window.innerHeight);
	try {
		var pageScrollValue = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			pageScrollValue = window.innerHeight;
		}
		var top = document.getElementById(inputhref);
		var topoffset = getAbsPosition(top)[0];
		var pageIndex = Math.floor(getAbsPosition(top)[0] / pageScrollValue);
		if(ishrMode) {
			/*if (isOreientationPortrait || isMobile) {
			     var  leftvalue = (pageIndex * pageScrollValue)
			   } else {
			     var  leftvalue = (pageIndex * pageScrollValue);
			   }*/
			var leftvalue = (pageIndex * pageScrollValue);
			if(pageIndex > 0 && !isMobile) pageIndex = pageIndex - 1;
			ReflowablePageFragment.setPageIndex(pageIndex);
			/*var horzDistance = getAbsPosition(top)[1];
			var scrollingElement = bodyOrHtml();
			var clientWidth = window.innerWidth;
			var pageIndex = Math.floor(horzDistance / clientWidth);
			var newScrollLeft = clientWidth * pageIndex;
			//console.log("-> newScrollLeft = " + newScrollLeft);
			scrollingElement.scrollLeft = newScrollLeft;
			ReflowableViewPager.setCurrentPage(pageIndex);*/
		} else {
			window.scrollTo(0, topoffset);
		}
	} catch(err) {
		var pageScrollValue = window.innerWidth;
		if(isOreientationPortrait || isMobile) {
			pageScrollValue = window.innerHeight;
		}
		var top = document.getElementsByName(inputhref)[0];
		var pageIndex = Math.floor(getAbsPosition(top)[0] / pageScrollValue);
		if(ishrMode) {
			/* var distance = getAbsPosition(top)[1]
			 var scrollingElement = bodyOrHtml();
			 var clientWidth = window.innerWidth;
			 var pageIndex = Math.floor(distance / clientWidth);
			 var newScrollLeft = clientWidth * pageIndex;
			 //console.log("-> newScrollLeft = " + newScrollLeft);
			 scrollingElement.scrollLeft = newScrollLeft;
			 ReflowableViewPager.setCurrentPage(pageIndex);*/
			/* if (isOreientationPortrait || isMobile) {
			                 var  leftvalue = (pageIndex * pageScrollValue)
			               } else {
			                 var  leftvalue = (pageIndex * pageScrollValue);
			               }*/
			var leftvalue = (pageIndex * pageScrollValue);
			if(pageIndex > 0 && !isMobile) pageIndex = pageIndex - 1;
			// window.scrollTo(leftvalue, 0);
			ReflowablePageFragment.setPageIndex(pageIndex);
		} else {
			top.scrollIntoView();
		}
	}
}

function stopAudioVideo() {
	var audioPlayer = document.getElementsByTagName('audio')[0];
	if(audioPlayer != undefined) {
		audioPlayer.pause();
	}
	var videoPlayer = document.getElementsByTagName('video')[0];
	if(videoPlayer != undefined) {
		videoPlayer.pause();
	}
}

function addBookMarkAnchor(jsondata) {
	if(jsondata != "") {
		var currenantrange = document.caretRangeFromPoint(0, 0);
		var rangeOffset = JSON.parse(jsondata);
		//  console.log("currentrange value" + currenantrange);
		var rangenew = restoreSelection(rangeOffset);
		var name = rangeOffset.start + "_" + rangeOffset.end;
		adddummyBookMark(rangenew, name);
	}
}

function adddummyBookMark(range, id) {
	/*console.log("adddummyBookMark" + id);
	console.log("range BookMark value" + range);*/
	var latvisitedanchortag = document.createElement('kitaboospan');
	latvisitedanchortag.setAttribute("class", id);
	latvisitedanchortag.setAttribute("style", "background-color: transparent !important;display:initial; height: 5px;width: 5px;");
	range.surroundContents(latvisitedanchortag);
	//  console.log("latvisitedanchortag offsettop value" + latvisitedanchortag.offsetTop);
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
	while(!stop && (node = nodeStack.pop())) {
		if(node.nodeType == 3) {
			var nextCharIndex = charIndex + node.length;
			if(!foundStart && savedSel.start >= charIndex && savedSel.start <= nextCharIndex) {
				range.setStart(node, savedSel.start - charIndex);
				foundStart = true;
			}
			if(foundStart && savedSel.end >= charIndex && savedSel.end <= nextCharIndex) {
				range.setEnd(node, savedSel.end - charIndex);
				stop = true;
			}
			charIndex = nextCharIndex;
		} else {
			var i = node.childNodes.length;
			while(i--) {
				nodeStack.push(node.childNodes[i]);
			}
		}
	}
	return range;
}

function isBookMarkAvailable(jsonarray, isHrmode, IsTwoPageModeEnable, isMobile, orientation) {
	//console.log("isBookMarkAvailable jsonarray" + jsonarray);
	if(jsonarray != null) {
		var data = JSON.parse(jsonarray);
		//console.log("isBookMarkAvailable data" + jsonarray);
		for(var i = 0; i < data.length; i++) {
			var bookmarkPath = data[i].mBookMarkPath;
			var bookMarkCFIPath = data[i].getCFIBookMarkPath;
			/*console.log("bookmarkPath called" + bookmarkPath);
			console.log("bookMarkCFIPath called" + bookMarkCFIPath);*/
			if(bookMarkCFIPath != "") {
				var isBookMarkAvailable = isCFIContextBookmarked(bookMarkCFIPath, isHrmode, IsTwoPageModeEnable, isMobile, orientation);
				if(isBookMarkAvailable && CFIarr.length == 1) {
					ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(true, bookMarkCFIPath);
				} else if(isCFIBookMarkAvailable && CFIarr.length > 1) {
					cfiElementList.push(CFIarr[i]);
				} else {
					ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(false, bookMarkCFIPath);
				}
			} else if(bookmarkPath != "") {
				var isBookMarkInScroll = isContextBookmarked(bookmarkPath, isHrmode);
				// console.log("isBookMarkInScroll called" + isBookMarkInScroll);
				if(isBookMarkInScroll) {
					ReflowablePageFragment.isBookMarkInScroll(isBookMarkInScroll, data[i].mlocalID);
					break;
				} else {
					ReflowablePageFragment.isBookMarkInScroll(isBookMarkInScroll, data[i].mlocalID);
				}
			} else {
				ReflowablePageFragment.isBookMarkInScroll(false, data[i].mlocalID);
			}
		}
		var smallestTopOffset = undefined;
		var topmostCFI = undefined;
		for(var i = 0; i < cfiElementList.length; i++) {
			var topoffset = jumpToCFI(cfiElementList[i], isHrmode, IsTwoPageModeEnable, isMobile, orientation, true);
			if(smallestTopOffset == undefined) {
				smallestTopOffset = topoffset;
				topmostCFI = cfiElementList[i];
			} else if(topoffset < smallestTopOffset) {
				smallestTopOffset = topoffset;
				topmostCFI = cfiElementList[i];
			}
			if(i == cfiElementList.length - 1) {
				cfiElementList.splice(0, cfiElementList.length);
				ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(true, topmostCFI);
			}
		}
		if(data.length == 0) {
			ReflowablePageFragment.isBookMarkInScroll(false, 0);
		}
	}
}

function isContextBookmarked(bookmarkPath, isHrmode) {
	try {
		var rangeobj = JSON.parse(bookmarkPath);
		var id = rangeobj.start + "_" + rangeobj.end;
		var highlight = document.getElementsByClassName(id)[0];
		//  console.log("Book mark element" + highlight);
		var top = highlight.offsetTop + (window.innerHeight / 3);
		var currentTop = window.scrollY;
		var pageHeight = parseInt(currentTop) + window.innerHeight;
		/*console.log("Book mark element top" + top);
		console.log("scrollY value currentTop" + currentTop);
		console.log("Book mark element pageHeight" + pageHeight);*/
		if((currentTop < top) && (top < pageHeight)) {
			return true;
		} else {
			return false;
		}
	} catch(err) {
		return false;
	}
}

function debounce(func, wait, immediate) {
	isPageInScroll = true;
	// 'private' variable for instance
	// The returned function will be able to reference this due to closure.
	// Each call to the returned function will share this common timer.
	var timeout;
	// Calling debounce returns a new anonymous function
	return function() {
		// reference the context and args for the setTimeout function
		var context = this,
			args = arguments;
		// Should the function be called now? If immediate is true
		//   and not already in a timeout then the answer is: Yes
		var callNow = immediate && !timeout;
		// This is the basic debounce behaviour where you can call this
		//   function several times, but it will only execute once
		//   [before or after imposing a delay].
		//   Each time the returned function is called, the timer starts over.
		clearTimeout(timeout);
		// Set the new timeout
		timeout = setTimeout(function() {
			// Inside the timeout function, clear the timeout variable
			// which will let the next execution run when in 'immediate' mode
			timeout = null;
			// Check if the function already ran with the immediate flag
			if(!immediate) {
				// Call the original function with apply
				// apply lets you define the 'this' object as well as the arguments
				//    (both captured before setTimeout)
				func.apply(context, args);
			}
		}, wait);
		// Immediate mode and no wait timer? Execute the function..
		if(callNow) func.apply(context, args);
	};
};

function onScrollStop() {
	//  console.log("after stop scrolling");
	last_known_scroll_position = document.scrollingElement.scrollTop;
	ReflowablePageFragment.OnScrollStopCallback(last_known_scroll_position);
}

function stringRet(isHrmode) {
	var lastvistedpageobject = currentPosition(isHrmode);
	var lastVisitedCfiPagepath = getCFINodeString(isHrmode);
	//ReflowablePageFragment.lastVisitedPageData(lastvistedpageobject, lastVisitedCfiPagepath);
	ReflowablePageFragment.currentRangeAndCFIstring(lastvistedpageobject, lastVisitedCfiPagepath);
}



function currentPositionForBookMark(isHrmode) {
	var path = "";
	var lastvistedpageobject = currentPositionBookMark(isHrmode);
	var lastvistedCFIpageobject = getCFINodeString(isHrmode);
	//  console.log("RAVITEJA  " + lastvistedCFIpageobject);
	if(lastvistedCFIpageobject != null) {
		ReflowablePageFragment.onCFIBookMarkPageData(lastvistedCFIpageobject, lastvistedpageobject);
	} else {
		ReflowablePageFragment.onBookMarkPageData(lastvistedpageobject);
	}
}


    function currentPositionBookMark(isHrmode) {
	try {
		// if(document.caretRangeFromPoint(0, i) != null) {
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
		// console.log("jsonstring" + string);
		return string;
	} catch(err) {
		var range = document.caretRangeFromPoint(0, 0);
		var preSelectionRange = range.cloneRange();
		preSelectionRange.selectNodeContents(document.body);
		preSelectionRange.setEnd(range.startContainer, range.startOffset);
		var start = preSelectionRange.toString().length;
		var string = JSON.stringify({
			start: start,
			end: start + range.toString().length
		});
		// console.log("jsonstring" + string);
		return string;
		// }
	}
}

function saveBookMarkAnchor(jsondata) {
	if(jsondata != "") {
		var currenantrange = document.caretRangeFromPoint(0, 0);
		var rangeOffset = JSON.parse(jsondata);
		//  console.log("currentrange value" + currenantrange);
		var name = rangeOffset.start + "_" + rangeOffset.end;
		adddummyBookMark(currenantrange, name);
	}
}

function isBookMarkAvailableAtCurrentPosition(bookmarkPath, isHrmode) {
	if(bookmarkPath != "") {
		var isBookMarkInScroll = isContextBookmarked(bookmarkPath, isHrmode);
		// console.log("isBookMarkInScroll called" + isBookMarkInScroll);
		ReflowablePageFragment.drawBookmarkAtCurrentPosition(isBookMarkInScroll, bookmarkPath);
	} else {
		ReflowablePageFragment.drawBookmarkAtCurrentPosition(false, bookmarkPath);
	}
}

function jumptobookmark(bookmarkPath) {
	var rangeobj = JSON.parse(bookmarkPath);
	var id = rangeobj.start + "_" + rangeobj.end;
	var highlight = document.getElementsByClassName(id)[0];
	highlight.scrollIntoView();
}

function jumptobookmarkHorizontal(bookmarkPath) {
	var rangeobj = JSON.parse(bookmarkPath);
	var id = rangeobj.start + "_" + rangeobj.end;
	var highlight = document.getElementsByClassName(id)[0];
	var highlightTop = getAbsPosition(highlight)[1];
	ReflowablePageFragment.heightToMoved(highlightTop);
}

function isBookmarkAvailableAtCurrentRange(bookmarkPath) {
	var rangeobj = JSON.parse(bookmarkPath);
	var id = rangeobj.start + "_" + rangeobj.end;
	var highlight = document.getElementsByClassName(id)[0];
	if(highlight == null || highlight == undefined) {
		//  console.log("BookMarkAvailable_none");
		ReflowablePageFragment.drawBookmarkAtCurrentPosition(false, bookmarkPath);
	} else {
		//   console.log("BookMarkAvailable");
		ReflowablePageFragment.drawBookmarkAtCurrentPosition(true, bookmarkPath);
	}
}

function isCFIAvailableAtCurrentRange(CFIarr, isHrmode, IsTwoPageModeEnable, isMobile, orientation) {
	//  console.log("isCFIAvailableAtCurrentRange" + CFIarr);
	for(var i = 0; i < CFIarr.length; i++) {
		var isCFIBookMarkAvailable = isCFIContextBookmarked(CFIarr[i], isHrmode, IsTwoPageModeEnable, isMobile, orientation);
		if(isCFIBookMarkAvailable && CFIarr.length == 1) {
			ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(true, CFIarr[i]);
			break;
		} else if(isCFIBookMarkAvailable && CFIarr.length > 1) {
			cfiElementList.push(CFIarr[i]);
		} else {
			ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(false, CFIarr[i]);
		}
	}
	var smallestTopOffset = undefined;
	var topmostCFI = undefined;
	for(var i = 0; i < cfiElementList.length; i++) {
		var topoffset = jumpToCFI(cfiElementList[i], isHrmode, IsTwoPageModeEnable, isMobile, orientation, true);
		if(smallestTopOffset == undefined) {
			smallestTopOffset = topoffset;
			topmostCFI = cfiElementList[i];
		} else if(topoffset < smallestTopOffset) {
			smallestTopOffset = topoffset;
			topmostCFI = cfiElementList[i];
		}
		if(i == cfiElementList.length - 1) {
			cfiElementList.splice(0, cfiElementList.length);
			ReflowablePageFragment.drawCFIBookmarkAtCurrentPosition(true, topmostCFI);
		}
	}
}

function jumtomark(bookmarkPath) {
	var rangeobj = JSON.parse(bookmarkPath);
	var id = rangeobj.start + "_" + rangeobj.end;
	var highlight = document.getElementsByClassName(id)[0];
	if(highlight == null || highlight == undefined) {
		// console.log("11_Available_none");
	} else {
		//   console.log("11_Available");
	}
	var scrollingElement = bodyOrHtml();
	ReflowablePageFragment.heightToMoved(highlight.offsetLeft, window.innerWidth);
	var clientWidth = document.documentElement.clientWidth;
	var pageIndex = Math.ceil((highlight.offsetLeft) / clientWidth);
	var newScrollLeft = clientWidth * (pageIndex - 1);
	scrollingElement.scrollLeft = newScrollLeft;
	ReflowableViewPager.setCurrentPage(pageIndex - 1);
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
	if(currentrange.startContainer.nodeType == 3 && currentrange.startOffset != 0) {
		cfi = EPUBcfi.Generator.generateCharOffsetRangeComponent(currentrange.startContainer, currentrange.startOffset, currentrange.endContainer, currentrange.endOffset, null, null, null);
		//  console.log("Dhananjay" + 2);
	} else {
		//  console.log("Dhananjay" + 3);
		var node = getFirstVisibleNode(document.body) || document.body;
		if(node.nodeType === Node.TEXT_NODE) {
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
	if(rect == null) return null;
	var viewportRect = getViewportRect();
	var intersects = rectIntersects(viewportRect, rect);
	var contains = rectContains(viewportRect, rect);
	if(contains) {
		return node;
	} else if(intersects) {
		var childNodes = node.childNodes;
		for(var i = 0; i < childNodes.length; i++) {
			if(childNodes[i].nodeType === Node.ELEMENT_NODE || childNodes[i].nodeType === Node.TEXT_NODE) {
				var childNode = getFirstVisibleNode(childNodes[i]);
				if(childNode) {
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
	return a.left < a.right && a.top < a.bottom && a.left <= b.left && a.top <= b.top && a.right >= b.right && a.bottom >= b.bottom;
}

function jumpToCFI(CFIString, isHrMode, IsTwoPageModeEnable, isMobile, orientation, isTopOffsetRequird) {
	try {
		if(!EPUBcfi.isRangeCfi(CFIString)) {
			var node = EPUBcfi.Interpreter.getTargetElement(CFIString, document, null, null, null);
			var offsetLeft = scrollIntoCFINode(node[0], isHrMode, IsTwoPageModeEnable, isMobile, orientation, isTopOffsetRequird);
			return offsetLeft;
		} else {
			var range = EPUBcfi.Interpreter.getRangeTargetElements(CFIString, document, null, null, null);
			var finalSafeIndex = 0;
			var startSafeIndex = 0;
			var endSafeIndex = 0;
			var startOffset = 0;
			var endOffset = 0;
			if(range.startElement.length <= range.startOffset) {
				for(i = 0; i < range.startElement.parentElement.childNodes.length; i++) {
					if(range.startElement.parentElement.childNodes[i] == range.startElement && range.startElement.parentElement.childNodes[i + 1]) {
						range = range.startElement.parentElement.childNodes[i + 1];
						var offsetLeft = scrollIntoCFINode(range, isHrMode, IsTwoPageModeEnable, isMobile, orientation, isTopOffsetRequird);
						return offsetLeft;
					}
				}
			}
			if(range.startOffset) {
				startOffset = range.startOffset;
			} else {
				startOffset = 0;
			}
			if(range.endOffset) {
				endOffset = range.endOffset;
			} else {
				endOffset = 0;
			}
			if(range.startElement.length > 0 && range.startElement.length != range.startOffset && range.startElement.length > startOffset) {
				var substringLength = range.startElement.length - startOffset;
				var substringValue = range.startElement.substringData(startOffset, substringLength);
				var trimmedString = substringValue.trim();
				var spaceIndex = trimmedString.indexOf(" ");
				var firstWord = trimmedString.substring(0, spaceIndex);
				var firstWordIndex = substringValue.indexOf(firstWord);
				finalSafeIndex = firstWordIndex + firstWord.length;
				startSafeIndex = startOffset + finalSafeIndex;
				endSafeIndex = endOffset + finalSafeIndex;
			} else if(range.startElement.length <= startOffset) {
				startSafeIndex = range.startElement.length;
				endSafeIndex = range.startElement.length;
			}
			var convertedrange = document.createRange();
			convertedrange.setStart(range.startElement, startSafeIndex);
			convertedrange.setEnd(range.endElement, endSafeIndex);
			var dummylastvisiter = document.createElement('kitaboobookmark');
			dummylastvisiter.setAttribute('style', 'position:-webkit-sticky;height: 0px; width: 0px; overflow: hidden; float: none;  display: inline-block; clear: both;');
			convertedrange.cloneRange().surroundContents(dummylastvisiter);
			var offsetLeft = scrollIntoCFINode(dummylastvisiter, isHrMode, IsTwoPageModeEnable, isMobile, orientation, isTopOffsetRequird);
			var pa = dummylastvisiter.parentNode;
			while(dummylastvisiter.firstChild) pa.insertBefore(dummylastvisiter.firstChild, dummylastvisiter);
			pa.removeChild(dummylastvisiter);
			pa.normalize();
			return offsetLeft;
		}
	} catch(err) {
		return 0;
	}
}
//scroll in to cfi node
function scrollIntoCFINode(nodeOrRange, isHrMode, IsTwoPageModeEnable, isMobile, orientation, isTopOffsetRequird) {
	var scrollingElement = bodyOrHtml();
	// For Direction.VERTICAL
	var nodeOffsetTop, nodeOffsetHeight;
	// For Direction.HORIZONTAL
	var nodeOffsetLeft;
	if(nodeOrRange instanceof Range || nodeOrRange.nodeType === Node.TEXT_NODE) {
		var rect;
		if(nodeOrRange.nodeType && nodeOrRange.nodeType === Node.TEXT_NODE) {
			var range = document.createRange();
			range.selectNode(nodeOrRange);
			rect = RangeFix.getBoundingClientRect(range);
		} else {
			rect = RangeFix.getBoundingClientRect(nodeOrRange);
		}
		//return left or top as per requirement
		nodeOffsetTop = scrollingElement.scrollTop + rect.top;
		nodeOffsetHeight = rect.height;
		nodeOffsetLeft = scrollingElement.scrollLeft + rect.left;
		if(isTopOffsetRequird) {
			return nodeOffsetTop;
		} else if(isHrMode) {
			return nodeOffsetLeft;
		} else {
			return nodeOffsetTop;
		}
	} else if(nodeOrRange.nodeType === Node.ELEMENT_NODE) {
		//return left or top as per requirement
		try {
			var absoluteOffset = getAbsPosition(nodeOrRange);
			if(absoluteOffset[0] > nodeOrRange.offsetTop) {
				nodeOffsetTop = absoluteOffset[0];
			} else {
				nodeOffsetTop = nodeOrRange.offsetTop;
			}
			if(absoluteOffset[1] > nodeOrRange.offsetLeft) {
				nodeOffsetLeft = absoluteOffset[1];
			} else {
				nodeOffsetLeft = nodeOrRange.offsetLeft;
			}
		} catch(err) {
			nodeOffsetTop = nodeOrRange.offsetTop;
			nodeOffsetLeft = nodeOrRange.offsetLeft;
		}
		// nodeOffsetTop = nodeOrRange.offsetTop;
		nodeOffsetHeight = nodeOrRange.offsetHeight;
		if(isTopOffsetRequird) {
			return nodeOffsetTop;
		} else if(isHrMode) {
			if(nodeOffsetLeft < nodeOffsetTop) {
				var scalefactor = window.innerWidth / window.innerHeight;
				if(nodeOffsetTop < window.innerHeight) var totallength = nodeOffsetTop;
				else var totallength = nodeOffsetTop * scalefactor;
				var pageIndex = totallength / window.innerWidth;
				totallength = totallength + (pageIndex * 20);
				return totallength;
			} else {
				return nodeOffsetLeft;
			}
		} else {
			return nodeOffsetTop;
		}
	} else {
		throw("-> Illegal Argument Exception, nodeOrRange -> " + nodeOrRange);
	}
	//return nodeOffsetTop;
	var highlightOffSet = nodeOffsetTop;
	var pageNumber = (parseInt(highlightOffSet) / window.innerHeight);
	var leftMargin = 0;
	var topMargin = 0;
	if(IsTwoPageModeEnable) {
		leftMargin = ((pageNumber) * (window.innerWidth / 2));
	} else {
		leftMargin = ((pageNumber) * window.innerWidth);
	}
	return leftMargin;
}

function isCFIContextBookmarked(CFINodeOrRange, isHrMode, IsTwoPageModeEnable, isMobile, orientation) {
	var value = jumpToCFI(CFINodeOrRange, isHrMode, IsTwoPageModeEnable, isMobile, orientation, false);
	if(isHrMode) {
		var CFIPage = Math.floor(value / window.innerWidth);
		var actualPosition = value / window.innerWidth;
		if(isMobile) {
			if(CFIPage > 0) {
				CFIPage = CFIPage + 1;
			}
		} else {
			if((actualPosition - CFIPage) > 0.5) {
				CFIPage = CFIPage + 1;
			}
		}
		if(isMobile || IsTwoPageModeEnable) {
			var currPage = Math.floor(window.pageXOffset / window.innerWidth);
			if(((window.pageXOffset / window.innerWidth) - Math.floor(window.pageXOffset / window.innerWidth)) > 0.8) {
				currPage = currPage + 1;
			}
		} else {
			var currPage = Math.floor(window.pageXOffset / window.innerWidth);
		}
		if(currPage == CFIPage) {
			return true;
		} else {
			return false;
		}
	} else {
		var minValue = window.pageYOffset;
		var maxValue = minValue + window.innerHeight;
		if(value >= minValue && value < maxValue) {
			return true;
		} else {
			return false;
		}
	}
}

function jumpToBookmarkWithCFI(CFINodeOrRange, isHrMode, IsTwoPageModeEnable, isMobile, orientation) {
	var value = jumpToCFI(CFINodeOrRange, isHrMode, IsTwoPageModeEnable, isMobile, orientation, false);
	if(isHrMode) {
		var scrollingElement = bodyOrHtml();
		var clientWidth = window.innerWidth;
		var pageIndex = Math.floor(value / clientWidth);
		var actualPosition = value / window.innerWidth;
		if(isMobile) {
			if(pageIndex > 0) {
				pageIndex = pageIndex + 1;
			}
		} else {
			if((actualPosition - pageIndex) > 0.5) {
				pageIndex = pageIndex + 1;
			}
		}
		//ReflowablePageFragment.heightToMoved(highlightTop , clientWidth);
		var newScrollLeft = clientWidth * pageIndex;
		scrollingElement.scrollLeft = newScrollLeft;
		ReflowableViewPager.setCurrentPage(pageIndex);
	} else {
		document.scrollingElement.scrollTop = value - 20;
	}
	ReflowablePageFragment.OnPageJumpComplete(value);
}

function getCurrentVisibleCFIs() {
	var allCFIElements = document.querySelectorAll('[data-cfi]')
	var currentCFIList = [];
	//  console.log(" mmmallCFIElements  length " + allCFIElements.length);
	for(var i = 0; i < allCFIElements.length; i++) {
		var elem = allCFIElements[i];
		if(isInViewRect(elem) && !((elem.style.display === 'none') || (elem.style.visibility === 'hidden'))) {
			var cfiString = elem.getAttribute('data-cfi');
			//    console.log("cfid list " + cfiString);
			currentCFIList.push(cfiString);
		}
	}
	// console.log("currentCFIList " + currentCFIList.toString() + "... is");
	//return currentCFIList.toString();
	// console.log("currentCFIList " + currentCFIList.toString());
	ReflowablePageFragment.getVisibleCfidForPage(currentCFIList.toString());
	//  return currentCFIList.toString();
}
/*function isInViewRect(elem) {
    var bounding = elem.getBoundingClientRect();
    if (bounding.top < window.innerHeight && bounding.bottom >= 0) {
        return true;
    } else {
        return false;
    }
}*/
function isInViewRect(elem) {
	var bounding = elem.getBoundingClientRect();
	if(bounding.bottom == 0) {
		return false;
	}
	return(bounding.top >= 0 && bounding.left >= 0 && bounding.bottom <= (window.innerHeight || document.documentElement.clientHeight) && bounding.right <= (window.innerWidth || document.documentElement.clientWidth));
}
document.addEventListener('click', function(e) {
	e = e || window.event;
	var target = e.target || e.srcElement,
		text = target.textContent || target.innerText;
	//  console.log("CLICKED_CONTENT  " + text);
}, false);

function setReaderModeBackgroundColor(mode) {
	if(JSON.stringify(mode) === JSON.stringify("Day")) {
		readerModeBackgroundColor = "#ffffff";
	} else if(JSON.stringify(mode) === JSON.stringify("Night")) {
		readerModeBackgroundColor = "#222 ";
	} else if(JSON.stringify(mode) === JSON.stringify("Sepia")) {
		readerModeBackgroundColor = "#FAF3E8";
	}
}

function setClickEventForElementModal(mode) {
	setReaderModeBackgroundColor(mode);
	var images = document.getElementsByTagName("img");
	for(var img = 0; img < images.length; img++) {
		if(images[img].id != "modalElement") {
			images[img].style.pointerEvents = "";
			images[img].setAttribute('onclick', "elementClickedTable(event)");
		}
	}
	var tables = document.getElementsByTagName("table");
	for(var table = 0; table < tables.length; table++) {
		if(tables[table].id != "modalElement") {
			tables[table].style.pointerEvents = "";
			tables[table].setAttribute('onclick', "elementClickedTable(event)");
		}
	}
}
var timeout;
var lastTap = 0;
var lastTapImage = 0;
var currentTarget;

function elementClickedTable(event) {
	var currentTime = new Date().getTime();
	var tapLength = currentTime - lastTap;
	if(tapLength < 500 && tapLength > 0 && currentTarget == event.currentTarget) {
		if(event.currentTarget.tagName.toLowerCase() == "table") {
			var modalPopup = document.getElementById("modalPopup");
			if(modalPopup != undefined) {
				modalPopup.parentElement.removeChild(modalPopup);
			}
			modalPopup = document.createElement("div");
			modalPopup.className = "modalPopup";
			modalPopup.setAttribute('id', "modalPopup");
			modalPopup.setAttribute('style', 'background-color:#a1a1a1 !important;');
			var modalChild = document.createElement("div");
			modalChild.className = "modalChild";
			var closeSpan = document.createElement("span");
			closeSpan.className = "modalCloseSpan";
			closeSpan.textContent = "2";
			closeSpan.setAttribute('onclick', "closeModalPopup(event)");
			var elementDiv = document.createElement("div");
			elementDiv.className = "modalElementDiv";
			var elementCopy = event.currentTarget.cloneNode(true);
			elementCopy.removeAttribute("onclick");
			elementCopy.style.height = "";
			elementCopy.style.width = "";
			elementCopy.id = "modalElement";
			if(elementCopy.tagName.toLowerCase() == "table") {
				var highlights = elementCopy.getElementsByTagName("kitaboospan");
				var highlightsLength = highlights.length;
				elementCopy.classList.add("modalChild")
				for(var i = 0; i < highlightsLength; i++) {
					var highlightElement = highlights[0];
					var parentElement = highlightElement.parentElement;
					if(highlightElement.firstChild) {
						parentElement.insertBefore(highlightElement.firstChild, highlightElement);
						parentElement.removeChild(highlightElement);
						parentElement.normalize();
					} else {
						parentElement.removeChild(highlightElement);
						parentElement.normalize();
					}
				}
			} else {
				elementCopy.className = "modalChild";
			}
			modalPopup.appendChild(modalChild);
			modalChild.appendChild(closeSpan);
			modalChild.appendChild(elementDiv);
			elementDiv.appendChild(elementCopy);
			document.body.appendChild(modalPopup);
			elementDiv.setAttribute('style', 'background-color:' + readerModeBackgroundColor + ' !important;');
			modalPopup.style.display = "block";
			//  window.location = "modalPopupOpened"
			var htmlel = document.getElementsByTagName('html')[0]
			htmlel.setAttribute('style', 'overflow: hidden');
			ReflowablePageFragment.modalPopupOpened(true);
		} else {
			var captionName = getCaptionOfImage(event.currentTarget, document)
			ReflowablePageFragment.onImageDoublerClick(event, event.currentTarget.src, captionName);
		}
	}
	lastTap = currentTime;
	currentTarget = event.currentTarget;
}

function getCaptionOfImage(img, doc) {
	var _imgParent = img;
	var _caption = "";
	while(_imgParent.nextElementSibling === null && _imgParent != doc.body) { //Identify Parent Element of Img Tag
		_imgParent = _imgParent.parentElement;
	}
	if(_imgParent != doc.body) { //To Check for Next Or Previous Sibling
		_caption = (_imgParent.nextElementSibling && _imgParent.parentElement.innerText.trim().endsWith(_imgParent.nextElementSibling.innerText.trim())) ? _imgParent.nextElementSibling.innerText.trim() : (_imgParent.parentElement.innerText.trim().startsWith(_imgParent.nextElementSibling.innerText.trim())) ? _imgParent.parentElement.innerText.trim() : _imgParent.innerText.trim();
	}
	return _caption;
}

function elementClickedImgTable(event) {
	var currentTime = new Date().getTime();
	var tapLength = currentTime - lastTapImage;
	if(tapLength < 500 && tapLength > 0) {
		var captionName = getCaptionOfImage(event.currentTarget, document)
		if(event.currentTarget.tagName.toLowerCase() == "img") {
			ReflowablePageFragment.onImageDoublerClick(event, event.currentTarget.src , captionName);
		} else {
			// Table
		}
	}
	lastTapImage = currentTime;
}

function closeModalPopup(event) {
	var modalPopup = document.getElementById("modalPopup");
	modalPopup.style.display = "";
	modalPopup.parentElement.removeChild(modalPopup);
	var htmlel = document.getElementsByTagName('html')[0]
	htmlel.setAttribute('style', 'overflow: scroll');
	ReflowablePageFragment.modalPopupOpened(false);
}
var needBlankPageNew;

function initHorizontalDirectionUpdate(isPortrait, isMobile, isNeedBlankPage, isCoverPage, spaceRequired, fileName,iscover,isTwoPageLineEnable) {
	var marginValue = spaceRequired;
	needBlankPageNew = isNeedBlankPage;
	if(!isPortrait && isNeedBlankPage && isCoverPage && !isMobile) {
		var pageWidth = (document.documentElement.clientWidth / 2)
		pageWidth = pageWidth - spaceRequired;
		var pageHeight = document.documentElement.clientHeight
		var emptypage = document.createElement('kitaboospan');
		emptypage.setAttribute('id', 'kitabooemptypage');
		emptypage.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
		var emptypage2 = document.createElement('kitaboospan');
		emptypage2.setAttribute('id', 'kitabooemptypage2');
		emptypage2.setAttribute('style', "height:" + pageHeight + "px;width:" + pageWidth + "px;display:block;");
		document.body.insertBefore(emptypage, document.body.firstChild);
	} else if(isNeedBlankPage && isCoverPage && isMobile) {
		  updateimages(iscover)
	}
	// preInitHorizontalDirectionUpdate(isPortrait, isMobile,marginValue);
	//postInitHorizontalDirectionUpdate(false,marginValue,isPortrait,isMobile);
	//horizontalInterval = setInterval(horizontalRecheck, horizontalIntervalPeriod);
	if(isPortrait || isMobile) {
		var requiredHeight = window.innerHeight;
		var requiredWidth = (window.innerWidth);
	} else {
		var requiredHeight = window.innerHeight;
		var requiredWidth = (window.innerWidth / 2);
	}
//	addCSSRuleStyle('html', 'height:' + requiredHeight + 'px; column-gap: 0px; column-width:' + requiredWidth + 'px;', fileName,iscover);
    if(isTwoPageLineEnable){
	addCSSRuleStyle('html', 'height:' + requiredHeight + 'px; column-gap: 0px; column-width:' + requiredWidth + 'px; column-rule: 1px solid #A9A9A9;',fileName);
	}else{
	addCSSRuleStyle('html', 'height:' + requiredHeight + 'px; column-gap: 0px; column-width:' + requiredWidth + 'px;', fileName);
	}
}

function preInitHorizontalDirectionUpdate(isPortrait, isMobile, marginValue) {
	var defaultSpace = 40;
	var calCulatedSpace = defaultSpace + marginValue;
	var pageWidthReq = window.innerWidth / 2;
	pageWidthReq = pageWidthReq - calCulatedSpace;
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
	if(isPortrait || isMobile) {
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

function postInitHorizontalDirectionUpdate(isNeedBlankPage, marginValue, isPortrait, isMobile) {
	var calCulatedSpace = 40 + marginValue;
	if(isPortrait || isMobile) var pageWidthReq = parseInt((window.innerWidth) - calCulatedSpace);
	else var pageWidthReq = parseInt((window.innerWidth / 2) - calCulatedSpace);
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
	if(isNeedBlankPage) {
		htmlElement.style.width = pageWidthReq + 'px';
		bodyElement.style.width = pageWidthReq + 'px';
	} else {
		htmlElement.style.width = pageWidthReq + 'px';
		bodyElement.style.width = pageWidthReq + 'px';
	}
	var pageCount = Math.round(scrollWidth / (pageWidthReq));
	var pageCountFloat = scrollWidth / pageWidthReq;
	ReflowablePageFragment.setHorizontalPageCount(pageCount);
}

function addCSSRuleStyle(selector, newRule, fileName,iscover) {
	var stylesheetLength = document.styleSheets.length;
	var stylesheetLastEle = stylesheetLength - 1;
	var mySheet = document.styleSheets[stylesheetLastEle];
	if(mySheet != undefined) {
		if(mySheet.addRule) {
			if(mySheet.cssRules) {
				for(var i = 0; i < mySheet.cssRules.length; i++) {
					if(mySheet.cssRules[i].selectorText === 'html') {
						mySheet.removeRule(i);
					}
				}
			}
			mySheet.addRule(selector, newRule);
		} else {
			ruleIndex = mySheet.cssRules.length;
			mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
		}
	} else {
		console.log('stylesheet not available');
	}
	var clientWidth = window.innerWidth;
	var scrollWidth = document.documentElement.scrollWidth;
	var pageCount = Math.round(scrollWidth / clientWidth);
	var pageCountFloat = scrollWidth / clientWidth;
    updateimages(iscover);
	updatetables();
	getPages(fileName);
	if(ReflowablePageFragment != undefined) ReflowablePageFragment.setHorizontalPageCount(pageCount, fileName);
}

function getPages(fileName) {
	var clientWidth = window.innerWidth;
	var scrollWidth = document.documentElement.scrollWidth;
	var pageCount = Math.round(scrollWidth / clientWidth);
	var pageCountFloat = scrollWidth / clientWidth;
	ReflowablePageFragment.setPageCount(pageCount, fileName);
	clearSearch();
}
function updateSelectionRule(selectionColor) {
   addCSSRuleSelectionStyle('::selection', 'background: ' + selectionColor);
}


function addCSSRuleSelectionStyle(selector, newRule) {

  var stylesheetLength = document.styleSheets.length;
  var stylesheetLastEle = stylesheetLength - 1;
  var mySheet = document.styleSheets[stylesheetLastEle];
  if (mySheet != undefined) {
    if (mySheet.addRule) {
      mySheet.addRule(selector, newRule);
    } else {
      ruleIndex = mySheet.cssRules.length;
      mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);
    }
  } else {
    console.log('stylesheet not available');
  }

}



var cumulativeOffset = function(element) {
	var top = 0,
		left = 5;
	return {
		top: element.getBoundingClientRect().top,
		left: element.getBoundingClientRect().x
	};
};

function loadPopup(event, data, url) {
	ReflowablePageFragment.loadPopup(event, data, url);
}

function updateNotesPosition(isOreientationPortrait, isMobile) {
	var notes = document.getElementsByTagName("kitabooNote");
	var leftOffset = 5;
	for(var i = 0; i < notes.length; i++) {
		try {
			var id = notes[i].id;
			var name = id.split("_")[1];
			var requiredName = name + "_" + id.split("_")[2];
			var j = notes.length;
			var id = notes[i].id;
			var name = id.split("_")[1];
			var requiredName = name + "_" + id.split("_")[2];
			var highlight = document.getElementsByName(requiredName)[0];
			var highlightTop = cumulativeOffset(highlight).top;
			var highlightLeft = "5";
			var highlightOffSet = parseInt(highlightTop);
			var docWidth = document.documentElement.clientWidth;
			var docHeight = window.innerHeight;
			var leftOffSet = 5;
			var clientWidth = 0;
			var leftMargin;
			clientWidth = (window.innerHeight);
			var pageLeftValue = getAbsPosition(highlight)[0];
			var pageIndex = Math.floor(getAbsPosition(highlight)[0] / clientWidth);
			if(isOreientationPortrait || isMobile) {
				if(pageIndex != 0) {
					if(pageLeftValue > window.innerWidth && pageLeftValue < window.innerWidth * 2) {
						pageIndex = 1;
					}
				}
				leftMargin = (pageIndex * window.innerWidth) + leftOffSet;
			} else {
				var pageScrollValue = window.innerWidth / 2;
				leftMargin = (pageIndex * pageScrollValue) + leftOffSet;
			}
			notes[i].style.left = leftMargin + "px";
			notes[i].style.top = highlightOffSet + "px";
		} catch(err) {}
	}
}

function updateNotesPositionVr() {
	var notes = document.getElementsByTagName("kitabooNote");
	var leftOffset = 5;
	for(var i = 0; i < notes.length; i++) {
		try {
			var id = notes[i].id;
			var name = id.split("_")[1];
			var requiredName = name + "_" + id.split("_")[2];
			var j = notes.length;
			var id = notes[i].id;
			var name = id.split("_")[1];
			var requiredName = name + "_" + id.split("_")[2];
			var highlight = document.getElementsByName(requiredName)[0];
			var leftOffSet = 5;
			var pageTopValue = getAbsPosition(highlight)[0];
			notes[i].style.left = leftOffSet + "px";
			notes[i].style.top = pageTopValue + "px";
		} catch(err) {}
	}
}

function getCurrentPage(isHrmode, isMobile, isOrientationPortrait) {

  var totalPagesinCurrentChapter = 0;
  var currentPageNumber = 0;
  var clientWidth = window.innerWidth;
  var clientHeight = window.innerHeight;
  var docLength=0

  if (isHrmode) {
    if (isOrientationPortrait || isMobile) {
      docLength=document.documentElement.scrollWidth;
      totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
      currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
      ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);

    } else {
      docLength=document.documentElement.scrollWidth;
      clientWidth = Math.round(clientWidth / 2);
      totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
      currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
      ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);

    }
  } else {
    docLength=document.body.scrollHeight;
    totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
    currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
    ReflowablePageFragment.setCurrentPageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);
  }

}

function updateCurrentPage(isHrmode, isMobile, isOrientationPortrait) {

  var totalPagesinCurrentChapter = 0;
  var currentPageNumber = 0;
  var clientWidth = window.innerWidth;
  var clientHeight = window.innerHeight;
  var docLength=0

  if (isHrmode) {
  docLength=document.documentElement.scrollWidth;
    if (isOrientationPortrait || isMobile) {
      totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
      currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
      ReflowablePageFragment.updatePageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);

    } else {
    docLength=document.documentElement.scrollWidth;
      clientWidth = Math.round(clientWidth / 2);
      totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
      currentPageNumber = Math.round(window.pageXOffset / clientWidth) + 1;
      ReflowablePageFragment.updatePageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);

    }
  } else {
    docLength=document.body.scrollHeight;
    totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
    currentPageNumber = Math.round(window.pageYOffset / window.innerHeight) + 1;
    ReflowablePageFragment.updatePageNumber(currentPageNumber, totalPagesinCurrentChapter,docLength);
  }

}

function getLoadedFilePageCount(isHrmode, isMobile, fileName) {
	var totalPagesinCurrentChapter = 0;
	var currentPageNumber = 0;
	var clientWidth = window.innerWidth;
	var clientHeight = window.innerHeight;
	if(isHrmode) {
		totalPagesinCurrentChapter = Math.round(document.documentElement.scrollWidth / clientWidth);
		ReflowablePageFragment.setChapterWiseTotalPageCount(fileName, totalPagesinCurrentChapter);
	} else {
		totalPagesinCurrentChapter = Math.round(document.body.scrollHeight / window.innerHeight);
		ReflowablePageFragment.setChapterWiseTotalPageCount(fileName, totalPagesinCurrentChapter);
	}
}

function hexToRgb(hex, alpha) {
    hex   = hex.replace('#', '');
    var r = parseInt(hex.length == 3 ? hex.slice(0, 1).repeat(2) : hex.slice(0, 2), 16);
    var g = parseInt(hex.length == 3 ? hex.slice(1, 2).repeat(2) : hex.slice(2, 4), 16);
    var b = parseInt(hex.length == 3 ? hex.slice(2, 3).repeat(2) : hex.slice(4, 6), 16);
    if ( alpha ) {
        return 'rgba(' + r + ', ' + g + ', ' + b + ', ' + alpha + ')';
    }
    else {
        return 'rgb(' + r + ', ' + g + ', ' + b + ')';
    }
}


function isCoverPageElement(coverPage) {
  this.isCoverPageElm = coverPage;
}

function addWaterMark(isMobile, waterMarkText, isPortraitPrint) {

    var watermarkGap = 500;
    var watermarkTop = watermarkGap;
    var totalwatermarkCount = 0;
    var currentwatermarkCount = 0;
    if (isMobile) {
        totalwatermarkCount = Math.round((document.body.clientHeight * document.body.clientWidth) / 600000);
    } else {

        if (isPortraitPrint) {

            totalwatermarkCount = Math.round((document.body.clientHeight * document.body.clientWidth) / 650000);
        } else {

            totalwatermarkCount = Math.round((document.body.clientHeight * document.body.clientWidth) / 750000);
        }

    }

    var waterMarkRage = document.body.clientHeight;

    do {
        kitabooWatermark = document.createElement("kitabooWatermark");
        kitabooWatermark.innerText = waterMarkText;
        kitabooWatermark.setAttribute('style', "font-size: 20px !important ;top:" + watermarkTop + "px; color: #668A96; -webkit-transform: rotate(330deg); left: 40px; right: 40px");
        document.body.appendChild(kitabooWatermark)
        currentwatermarkCount = currentwatermarkCount + 1;
        watermarkTop = watermarkTop + 1000;
    }
    while ((currentwatermarkCount < totalwatermarkCount));
}


/*
function removeAllSearchElelemt() {
var elements = document.getElementsByClassName('search-result-visible');
if(elements.length > 0){
for (var i = 0; i < elements.length; i++) {
 elements[i].parentNode.removeChild(elements[i]);
}
}
}*/

function isElementVisible(el) {
    var rect     = el.getBoundingClientRect(),
        vWidth   = window.innerWidth || document.documentElement.clientWidth,
        vHeight  = window.innerHeight || document.documentElement.clientHeight,
        efp      = function (x, y) { return document.elementFromPoint(x, y) };

    // Return false if it's not in the viewport
    if (rect.right < 0 || rect.bottom < 0
            || rect.left > vWidth || rect.top > vHeight)
        return false;

    // Return true if any of its four corners are visible
    return (
          el.contains(efp(rect.left,  rect.top))
      ||  el.contains(efp(rect.right, rect.top))
      ||  el.contains(efp(rect.right, rect.bottom))
      ||  el.contains(efp(rect.left,  rect.bottom))
    );
}