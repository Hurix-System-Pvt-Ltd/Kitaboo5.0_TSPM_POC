var highlightVOColl = [];
var touchendStartStick = false ,touchendEndStick = false;

var currHVO = null;
var firstWordId = -1;
var lastWordId = -1;

var touchOffset = 40;

function HighlightVO()
{
    var startWordID;
    var endWordID;
    var sX;
    var sY;
    var sW;
    var sH;
    var eX;
    var eY;
    var eW;
    var eH;
    var selectedText;
    var color;
    var hasNote;
};


function bindDocumentTouch()
{
    $(document).bind('touchstart',onTouchStart);
    
    $(document).bind('touchend',onTouchEnd);
    
    $(document).bind('touchmove',onTouchMove);
    touchendEndStick = true;
    touchendStartStick = false;
    jsInterface.log('bind successfull');
}

function unbindDocumentTouch()
{
    $(document).unbind('touchstart');
    $(document).unbind('touchmove');
    $(document).unbind('touchend');
    touchendEndStick = false;
    touchendStartStick = false;
    jsInterface.log('unbind successfull');
}


var currentPageIndex= 0;

function setCurrentPageIndex(pageIndex)
{
    currentPageIndex = pageIndex;
}

var currentPageWidth = 0;

function setCurrentPageWidth(pageWidth)
{
    currentPageWidth = pageWidth;
}

function triggerHighlight(pageX,pageY,color)
{
    console.log('triggerHighlight');
    var spanIDNumber;
    var obj = document.elementFromPoint(pageX,pageY);
    if($(obj).is('span'))
    {
        spanIDNumber = obj.id.split('-')[1];
    }
    if(spanIDNumber != undefined)
    {
    	currHVO = null;
    	var count = 0;
    	for(count = 0; count < highlightVOColl.length; count++)
    	{
	    	var vo = highlightVOColl[count];
	    	
	    	var startRange = Number(vo.startWordID);
	    	var endRange = Number(vo.endWordID);
	    	var span = Number(spanIDNumber);
	    	
	    	
	    	console.log('Iterating through -->' + startRange + ':' + endRange + ':' + span);
	    	
    		if(span>=startRange && span<=endRange)
    		{
    			currHVO = vo;
    			highlightVOColl.splice( count, 1 ); //highlightVOColl.pop(highlightVOColl[count]);;
				lastHoverdWordID = vo.startWordID;
				var setSelected = '{"MethodName":"setSelectedHighlight","MethodArguments":{"startWordID":'+startRange+',"endWordID": '+endRange+'}}';
        		jsInterface.callNativeMethod('jstoobjc:'+setSelected);
    			console.log('Found : ' + vo.startWordID + ':' + vo.endWordID + ':' + spanIDNumber);
    			break;
    		}
    		
    		console.log('Continuing Loop');
    		
    	}
    	
    	if(currHVO == null)
		{
			currHVO = new HighlightVO();
    		currHVO.startWordID =spanIDNumber;
    		currHVO.endWordID =spanIDNumber;
    		currHVO.hasNote = false;
            currHVO.color = color;
    		lastHoverdWordID = spanIDNumber;
		}
    	
        
        //initiate highlight vo
        
        clearAllHighlights();
        //console.log('Before calling updateHighlightSticksPositions : ' + vo.startWordID + ':' + vo.endWordID + ':' + spanIDNumber);
        updateHighlightSticksPositions(currHVO.startWordID,currHVO.endWordID,currHVO.color);
        highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
        drawSavedHighlights();
        
        var jsCall1 = '{"MethodName":"onTouchStart","MethodArguments":{}}';
        jsInterface.callNativeMethod('jstoobjc:'+jsCall1);
        
        var jsCall2 = '{"MethodName":"onTouchEnd","MethodArguments":{}}';
        jsInterface.callNativeMethod('jstoobjc:'+jsCall2);
    }
    else
    {
        var jsCall2 = '{"MethodName":"noWordFoundToHighlightOnLongPress","MethodArguments":{}}';
        jsInterface.callNativeMethod('jstoobjc:'+jsCall2);
    }
}

var touchStarted = false, // detect if a touch event is sarted
    currX = 0,
    currY = 0,
    cachedX = 0,
    cachedY = 0;

function onTouchStart(e)
{
    console.log('onTouchStart');
    var jsonSaveHighlight = '{"MethodName":"onTouchStart","MethodArguments":{}}';
    jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
    var pointer = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
    // caching the current x
    cachedX = currX = pointer.pageX;
    // caching the current y
    cachedY = currY = pointer.pageY;
    // a touch event is detected      
    touchStarted = true;

    e.preventDefault();
    // if(touchendStartStick == true || touchendEndStick == true)
    // {
    //     e.preventDefault();
    //     var spanIDNumber;
    //     var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
    //     var obj = document.elementFromPoint(touch.pageX-window.pageXOffset,touch.pageY-window.pageYOffset);
    //     if($(obj).is('span'))
    //     {
    //         spanIDNumber = obj.id.split('-')[1];
    //     }
    //     if(spanIDNumber != undefined)
    //     {
    //         if(currHVO == null || currHVO == undefined)
    //         {
    //             //initiate highlight vo
    //             currHVO = new HighlightVO();
    //             currHVO.hasNote = false;
    //             //check for existed highlight
    //             var seletedHVO = checkIsIntersectingWithExistedHighlights(spanIDNumber);
    //             if(seletedHVO)
    //             {
    //                 currHVO = seletedHVO;
    //                 clearAllHighlights();
    //                 updateHighlightSticksPositions(currHVO.startWordID,currHVO.endWordID,currHVO.color);
    //                 highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
    //                 drawSavedHighlights();
    //             }
    //             else
    //             {
    //                 currHVO.startWordID =spanIDNumber;
    //                 currHVO.endWordID =spanIDNumber;
    //             }
    //         }
    //         else
    //         {
    //             //update current highlight vo
    //             var diff =Number(currHVO.endWordID)-Number(currHVO.startWordID);
    //             var centerWID = Number(Number(currHVO.startWordID)+(Number(diff)/2));
                
    //             if(Number(spanIDNumber)<=Number(centerWID))
    //             {
    //                 currHVO.startWordID = spanIDNumber;
    //                 lastHoverdWordID = spanIDNumber;
    //             }
    //             else
    //             {
    //                 currHVO.endWordID = spanIDNumber;
    //                 lastHoverdWordID = spanIDNumber;
    //             }
                
    //         }
            
    //     }
    // }
};

var lastHoverdWordID = -1;

function onTouchMove(e)
{
    e.preventDefault();
    console.log('onTouchMove');
    if((touchendStartStick == true || touchendEndStick == true) && currHVO != undefined)
    {
        var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];

        var obj = document.elementFromPoint(touch.pageX-window.pageXOffset,touch.pageY-window.pageYOffset- touchOffset);
       console.log('Object is span' + $(obj).is('span'));
        if($(obj).is('span'))
        {
            var spanIDNumber = obj.id.split('-')[1];
            if(lastHoverdWordID != spanIDNumber)
            {
                if(Number(lastHoverdWordID)>Number(spanIDNumber))
                {
                    //moving backward
                    if(Number(spanIDNumber)<Number(currHVO.startWordID))
                    {
                        //crossed start word
                        currHVO.startWordID = spanIDNumber;
                    }
                    else
                    {
                        currHVO.endWordID = spanIDNumber;
                    }
                }
                else
                {
                    //moving forward
                    if(Number(spanIDNumber)>Number(currHVO.endWordID))
                    {
                        //crossed the end word
                        currHVO.endWordID = spanIDNumber;
                    }
                    else
                    {
                        currHVO.startWordID = spanIDNumber;
                    }
                }
                
                lastHoverdWordID = spanIDNumber;
                
                if(Number(currHVO.startWordID)>Number(currHVO.endWordID))
                {
                    currHVO.startWordID = Number(currHVO.startWordID)+Number(currHVO.endWordID);
                    currHVO.endWordID = Number(currHVO.startWordID)-Number(currHVO.endWordID);
                    currHVO.startWordID = Number(currHVO.startWordID) - Number(currHVO.endWordID);
                }
                
                clearAllHighlights();
                updateHighlightSticksPositions(currHVO.startWordID,currHVO.endWordID,currHVO.color);
                highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
                drawSavedHighlights();
            }
        }
    }
};

function onTouchEnd(e)
{
    console.log('onTouchEnd');

    var pointer = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
    
    currX = pointer.pageX;
    // caching the current y
    currY = pointer.pageY;
    
    // here we can consider finished the touch event
    touchStarted = false;
    var jsonSaveHighlight = '{"MethodName":"onTouchEnd","MethodArguments":{}}';
    jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
    e.preventDefault();

    if ((cachedX === currX) && !touchStarted && (cachedY === currY)) 
    {
        var jsonSaveHighlight = '{"MethodName":"onTap","MethodArguments":{}}';
        jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
    }
    else
    {
        
        if(touchendStartStick == true || touchendEndStick == true)
        {
            var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
            var obj = document.elementFromPoint(touch.pageX-window.pageXOffset,touch.pageY-window.pageYOffset);
            if($(obj).is('span'))
            {
                var spanIDNumber = obj.id.split('-')[1];
                //currHVO.endWordID =spanIDNumber;
            }
            //saveHighlight();
            clearAllHighlights();
            if(currHVO != undefined)
            {
                updateHighlightSticksPositions(currHVO.startWordID,currHVO.endWordID,currHVO.color);
                highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
            }
            drawSavedHighlights();
        }   
    }
    
};

function clearAllHighlights()
{
    $('span').css('background-color','rgba(0, 0, 0, 0)');
}

function setColorToCurrentHighlight(colorStr)
{
    console.log('setColorToCurrentHighlight');
    clearAllHighlights();
    if(currHVO != undefined)
    {
        currHVO.color = colorStr;
        highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
    }
    drawSavedHighlights();
}

function checkIsIntersectingWithExistedHighlights(wordID)
{
    var seletedHVO = null;
    for(j in highlightVOColl)
    {
        if(Number(wordID)==Number(highlightVOColl[j].startWordID) || Number(wordID) == Number(highlightVOColl[j].endWordID))
        {
            return highlightVOColl.pop(highlightVOColl[j]);
        }
        else if(Number(wordID)>Number(highlightVOColl[j].startWordID) &&  Number(wordID) < Number(highlightVOColl[j].endWordID))
        {
            return highlightVOColl.pop(highlightVOColl[j]);
        }
    }
    return null;
}

function showConsole(message)
{
    console.log(message);
}

function saveCurrentHighlight(hasNote)
{
    console.log('saveCurrentHighlight');
    if(currHVO != null || currHVO != undefined)
    {
        console.log('on Save Color : ' + currHVO.color);
        highlightText(currHVO.startWordID,currHVO.endWordID,currHVO.color);
        var formattedText = JSON.stringify(getSelectedText(currHVO.startWordID,currHVO.endWordID));
        currHVO.selectedText = formattedText;
        currHVO.hasNote = hasNote;
        var jsonSaveHighlight = '{"MethodName":"saveTextHighlightToPersistantStorage","MethodArguments":{"arg1":"'+currHVO.startWordID+'","arg2":"'+currHVO.endWordID+'","arg3":'+formattedText+'}}';
        jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
        
        var vo = new HighlightVO();
        vo.startWordID = currHVO.startWordID;
        vo.endWordID = currHVO.endWordID;
        vo.hasNote = currHVO.hasNote;
        vo.color = currHVO.color;
        highlightVOColl.push(vo);
        currHVO = null;
    }

    drawSavedHighlights();
}

function clearCurrentHighlight()
{
    currHVO = null;
    lastHoverdWordID = -1;
    clearAllHighlights();
    drawSavedHighlights();
}

function highlightText(sWordID,eWordID,colorVal)
{
    console.log("Color is " + colorVal);
    for(var i=Number(sWordID);i<=Number(eWordID);i++)
    {
        var spanIdToHighlight = 'wordID-'+i;
        $('#'+spanIdToHighlight).css('background-color',convertHex(colorVal));
    }
}

function convertHex(hex){
    if(hex == undefined) return 'red';
    hex = hex.replace('#','');
    a = parseInt(hex.substring(0,2), 16);
    r = parseInt(hex.substring(2,4), 16);
    g = parseInt(hex.substring(4,6), 16);
    b = parseInt(hex.substring(6,8), 16);
    result = 'rgba('+r+','+g+','+b+','+a/255+')';
    return result;
}

function getSelectedText(sWordID,eWordID)
{
    var selectedText = '';
    for(var i=Number(sWordID);i<=Number(eWordID);i++)
    {
        var spanIdToHighlight = 'wordID-'+i;
        selectedText = selectedText+$('#'+spanIdToHighlight).text();
    }
    return selectedText;
}

function addNoteIconToPage(sWordID ,eWordID)
{
    var sID = 0;
    var sX = 0;
    var sY = 0;
    var sW = 0;
    var sH = 0;
    
    var eID = 0;
    var eX = 0;
    var eY = 0;
    var eW = 0;
    var eH = 0;
    
    sID = sWordID;
    sX = $('#wordID-'+sWordID).position().left-window.pageXOffset;
    sY = $('#wordID-'+sWordID).position().top-window.pageYOffset;
    sW = $('#wordID-'+sWordID).width();
    sH = $('#wordID-'+sWordID).height();
    
    eID = eWordID
    eX = $('#wordID-'+eWordID).position().left + $('#wordID-'+eWordID).width()-window.pageXOffset;
    eY = $('#wordID-'+eWordID).position().top + $('#wordID-'+eWordID).height()-window.pageYOffset;
    eW = $('#wordID-'+eWordID).width();
    eH = $('#wordID-'+eWordID).height();
    
    var text = getSelectedText(sWordID,eWordID);
    var formattedText = JSON.stringify(text);
    var jsonSaveHighlight = '{"MethodName":"addNoteIconToPage","MethodArguments":{"arg1":"'+sID+'","arg2":"'+sX+'","arg3":"'+sY+'","arg4":"'+sW+'","arg5":"'+sH+'","arg6":"'+eID+'","arg7":"'+eX+'","arg8":"'+eY+'","arg9":"'+eW+'","arg10":"'+eH+'","arg11":'+formattedText+'}}';
    jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
}

function updateHighlightSticksPositions(sWordID ,eWordID,color)
{

	console.log('updateHighlightSticksPositions : ' + sWordID + ':' + eWordID);
    var sID = 0;
    var sX = 0;
    var sY = 0;
    var sW = 0;
    var sH = 0;
    
    var eID = 0;
    var eX = 0;
    var eY = 0;
    var eW = 0;
    var eH = 0;
    
    sID = sWordID;
    sX = $('#wordID-'+sWordID).position().left-window.pageXOffset;
    sY = $('#wordID-'+sWordID).position().top-window.pageYOffset;
    sW = $('#wordID-'+sWordID).width();
    sH = $('#wordID-'+sWordID).height();
    
    eID = eWordID
    eX = $('#wordID-'+eWordID).position().left + $('#wordID-'+eWordID).width()-window.pageXOffset;
    eY = $('#wordID-'+eWordID).position().top + $('#wordID-'+eWordID).height()-window.pageYOffset;
    eW = $('#wordID-'+eWordID).width();
    eH = $('#wordID-'+eWordID).height();
    
    var jsonSaveHighlight = '{"MethodName":"updateHighlightSticksPositions","MethodArguments":{"arg1":"'+sID+'","arg2":"'+sX+'","arg3":"'+sY+'","arg4":"'+sW+'","arg5":"'+sH+'","arg6":"'+eID+'","arg7":"'+eX+'","arg8":"'+eY+'","arg9":"'+eW+'","arg10":"'+eH+'","arg11":"'+color+'"}}';
    jsInterface.callNativeMethod('jstoobjc:'+jsonSaveHighlight);
    
}

function drawSavedHighlights()
{
    console.log("in drawSavedHighlights")
    for(j in highlightVOColl)
    {
        for(var i=Number(highlightVOColl[j].startWordID);i<=Number(highlightVOColl[j].endWordID);i++)
        {
            var spanIdToHighlight = 'wordID-'+i;

            $('#'+spanIdToHighlight).css('background-color',convertHex(highlightVOColl[j].color));

        }
        if(highlightVOColl[j].hasNote)
        {
        	addNoteIconToPage(Number(highlightVOColl[j].startWordID), Number(highlightVOColl[j].endWordID));
        }
    }
}

function removeCurrHighlightFromColl()
{
    if(currHVO != null || currHVO != undefined)
    {
        var index = highlightVOColl.indexOf(currHVO);
        if (index > -1) 
        {
            highlightVOColl.splice(index, 1);
        }
    }
    currHVO = null;
    clearAllHighlights();
    drawSavedHighlights();
}

function clearHighlightsArray()
{
    highlightVOColl = [];
}

function addHightlightToWebPage(startWID,endWID,hasNote,color)
{
    var currHighlightVO = new HighlightVO();
    
    currHighlightVO.startWordID = startWID;
    currHighlightVO.endWordID = endWID;
    currHighlightVO.hasNote = hasNote;
    currHighlightVO.color = color;
    highlightVOColl.push(currHighlightVO);
}

function setTouchedStick(isStartStick,isEndStick)
{
    if(isStartStick == true)
    {
        touchendStartStick = true;
        touchendEndStick = false;
    }
    else if(isEndStick == true)
    {
        touchendStartStick = false;
        touchendEndStick = true;
    }
    else
    {
        touchendStartStick = false;
        touchendEndStick = false;
    }
}

function findFirstAndLastWordsOfPage(columnWidth,indexOfCurrPage,indexOfNextPage)
{
    firstWordId = -1;
    lastWordId = -1;
    var arrOfSpans =  $('span');
    if(arrOfSpans.length==0)
    {
        var callNatMethod = '{"MethodName":"didFindFirstAndLastWordsOfPage","MethodArguments":{"arg1":"'+Number(firstWordId)+'","arg2":"'+Number(lastWordId)+'"}}';
        jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
        return;
    }
    
    $.each(arrOfSpans,function(i,obj)
    {
       var spanID = obj.id;
       if(spanID != undefined)
       {
            var spanIDNumber = obj.id.split('-')[1];
            var leftMargin = $(obj).position().left;
            if(firstWordId == -1)
            {
                if(Number(indexOfCurrPage) == 0)
                {
                    firstWordId = spanIDNumber;
                }
                else if(Number(leftMargin)> (Number(columnWidth)*Number(indexOfCurrPage)))
                {
                    firstWordId = spanIDNumber;
                }

                if(firstWordId != -1)
                {
                   //NSLog('    firstWID: '+firstWordId);
    				//var fw = 'wordID-'+firstWordId;
    				//$('#'+fw).css('background-color','rgba(255,0,0,0.4)');
                }
            }
                        
            if(lastWordId == -1 && firstWordId != -1)
            {
                if(indexOfNextPage == -1 && firstWordId != -1)
                {
                   lastWordId = arrOfSpans[arrOfSpans.length-1].id.split('-')[1];
                }
                else if(Number(leftMargin)> (Number(columnWidth)*Number(indexOfNextPage)) && i!= 0)
                {
                    lastWordId = arrOfSpans[i-1].id.split('-')[1];
                }

                if(lastWordId != -1)
                {
                    //NSLog('    lastWID: '+lastWordId);
                    //var lw = 'wordID-'+lastWordId;
                    //$('#'+lw).css('background-color','rgba(0,255,0,0.4)');

                    var callNatMethod = '{"MethodName":"didFindFirstAndLastWordsOfPage","MethodArguments":{"arg1":"'+Number(firstWordId)+'","arg2":"'+Number(lastWordId)+'"}}';
                    jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
                    return;
                }
            }
       }
    });
}

function copySelectedTextToPasteBoard(startWordID,endWordID)
{
    var text = getSelectedText(startWordID,startWordID);
    var formattedText = JSON.stringify(text);
    var callNatMethod = '{"MethodName":"copySelectedTextToPasteBoard","MethodArguments":{"arg1":'+formattedText+'}}';
    jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
}

function bookmarkThisPage()
{
    var text = getSelectedText(firstWordId,Number(firstWordId)+4);
    var formattedText = JSON.stringify(text);
    var callNatMethod = '{"MethodName":"bookmarkThisPage","MethodArguments":{"arg1":'+formattedText+'}}';
    jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
}

function findIndexOfPageUsingWordId(columnWidth , wordID)
{
    var indexOfPage = -1;
    var spanID = 'wordID-'+wordID;
    var leftMargin = $('#'+spanID).position().left;
    var res = Number(leftMargin)/Number(columnWidth);
    indexOfPage = Math.ceil(res)-1;
    
    var callNatMethod = '{"MethodName":"didFindIndexOfPage","MethodArguments":{"arg1":"'+indexOfPage+'"}}';
    jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
}

function findPageIndexOfElement(elementID)
{
    var myElement = document.getElementById(elementID);
    var pageNum = Book.renderer.getPageNumberByElement(myElement);
    var callNatMethod = '{"MethodName":"didFindPageIndexOfElement","MethodArguments":{"arg1":"'+pageNum+'"}}';
    jsInterface.callNativeMethod('jstoobjc:'+callNatMethod);
}

function removeCurrentHighlightFromColl()
{
    
}
