var API ={};
var kitabooSCODatamodel = [];
var scormdatamodels = JSON.stringify({
	  "cmi._version": "",
	  "cmi.comments_from_learner._children": "",
	  "cmi.comments_from_learner._count": "",
	  "cmi.comments_from_learner.n.comment": "",
	  "cmi.comments_from_learner.n.location": "",
	  "cmi.comments_from_learner.n.timestamp": "",
	  "cmi.comments_from_lms._children": "",
	  "cmi.comments_from_lms._count": "",
	  "cmi.comments_from_lms.n.comment": "",
	  "cmi.comments_from_lms.n.location": "",
	  "cmi.comments_from_lms.n.timestamp": "",
	  "cmi.completion_status": "",
	  "cmi.completion_threshold": "",
	  "cmi.credit": "",
	  "cmi.entry": "",
	  "cmi.exit": "",
	  "cmi.interactions._children": "",
	  "cmi.interactions._count": "",
	  "cmi.interactions.n.id": "",
	  "cmi.interactions.n.type": "",
	  "cmi.interactions.n.objectives._count": "",
	  "cmi.interactions.n.objectives.n.id": "",
	  "cmi.interactions.n.timestamp": "",
	  "cmi.interactions.n.correct_responses._count": "",
	  "cmi.interactions.n.correct_responses.n.pattern": "",
	  "cmi.interactions.n.weighting": "",
	  "cmi.interactions.n.learner_response": "",
	  "cmi.interactions.n.result": "",
	  "cmi.interactions.n.latency": "",
	  "cmi.interactions.n.description": "",
	  "cmi.launch_data": "",
	  "cmi.learner_id": "",
	  "cmi.learner_name": "",
	  "cmi.learner_preference._children": "",
	  "cmi.learner_preference.audio_level": "",
	  "cmi.learner_preference.language": "",
	  "cmi.learner_preference.delivery_speed": "",
	  "cmi.learner_preference.audio_captioning": "",
	  "cmi.location": "",
	  "cmi.max_time_allowed": "",
	  "cmi.mode": "",
	  "cmi.objectives._children": "",
	  "cmi.objectives._count": "",
	  "cmi.objectives.n.id": "",
	  "cmi.objectives.n.score._children": "",
	  "cmi.objectives.n.score.scaled": "",
	  "cmi.objectives.n.score.raw": "",
	  "cmi.objectives.n.score.min": "",
	  "cmi.objectives.n.score.max": "",
	  "cmi.objectives.n.success_status": "",
	  "cmi.objectives.n.completion_status": "",
	  "cmi.objectives.n.progress_measure": "",
	  "cmi.objectives.n.description": "",
	  "cmi.progress_measure": "",
	  "cmi.scaled_passing_score": "",
	  "cmi.score._children": "",
	  "cmi.score.scaled": "",
	  "cmi.core.score.raw": "",
	  "cmi.score.raw": "",
	  "cmi.score.min": "",
	  "cmi.score.max": "",
	  "cmi.session_time": "",
	  "cmi.success_status": "",
	  "cmo.completion_status": "",
	  "cmi.suspend_data": "",
	  "cmi.time_limit_action": "",
	  "cmi.attempt": "",
	  "cmi.attempt": "",
	  "cmi.total_time": "","cmi.core.session_time":""});

API.LMSInitialize =  function()
{
    console.log("LMSInitialize");
	return true;
}

API.LMSGetValue = function(key)
{
	for(var i = 0 ;i < kitabooSCODatamodel.length;i++){
		if(kitabooSCODatamodel[i].key == key)
		{
			return ""+kitabooSCODatamodel[i].value;
		}
	}
	
	return "";
}

API.LMSSetValue = function(inkey,invalue)
{
	console.log("LMSSetValue :"+inkey +" : "+invalue);
	
	for(var i = 0 ;i < kitabooSCODatamodel.length;i++){
		if(kitabooSCODatamodel[i].key == inkey)
		{
			kitabooSCODatamodel[i].value = ""+invalue;
			
		}
	}
	return "true";
}

API.LMSCommit = function(values)
{
	console.log("LMSCommit"+values);
	return true;
}

API.LMSGetLastError =  function()
{
	console.log("LMSGetLastError");
	return 0;
}

API.LMSGetErrorString = function(errorCode)
{
	console.log("LMSGetErrorString :"+errorCode);
	return errorCode;
}

API.LMSGetDiagnostic = function(errorCode)
{
	console.log("LMSGetDiagnostic :"+errorCode);
	return errorCode;
}

API.LMSFinish = function(result)
{	
	console.log("LMSFinish");
	window.android.close(JSON.stringify(kitabooSCODatamodel));
	return "";
}

init = function()
{
	var json = JSON.parse(scormdatamodels);
	for (var key in json)
	{
		var obj = {key : key,value : json[key]};
		kitabooSCODatamodel.push(obj);
	}

	console.log("init api" +JSON.stringify(kitabooSCODatamodel));
}

update = function(json)
{
    var jsonobj = JSON.parse(json);
    	for(var inkey in jsonobj){

    		for (var i=0;i<kitabooSCODatamodel.length;i++)
    		{
    			if(inkey == kitabooSCODatamodel[i].key)
    			{
    			    console.log("key :" +inkey +" = "+kitabooSCODatamodel[i].key +" value "+jsonobj[inkey]);
    				kitabooSCODatamodel[i].value =  jsonobj[inkey];

    			}
    		}
    	}

	console.log("after update api" +JSON.stringify(kitabooSCODatamodel));
}

closePlyer = function()
{
    window.close();
}
