<!DOCTYPE html>
<%@page import="org.osmsurround.ae.filter.FilterManager;"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wt" uri="http://www.grundid.de/webtools" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb" %>
<html>
<head>
<title>Amenity Editor for OpenStreetMap</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" lang="en" content="Simple online editing tool for amenities/POIs in the OpenStreetMap.">
<meta name="keywords" lang="en" content="osm, openstreetmap, amenity, editor, poi, pois, form">
<meta name="description" lang="de" content="Einfaches Online Werkzeug um Amenity/POIs Knoten in OpenStreetMap zu bearbeiten.">
<meta name="keywords" lang="de" content="osm, openstreetmap, amenity, editor, poi, pois, formular">
<meta name="author" content="GrundID GmbH, www.grundid-gmbh.de">

<link rel="StyleSheet" type="text/css" href="<wt:ue>/stylesheet.css</wt:ue>"
	media="screen" />
<link rel="icon" type="image/png" href="favicon.png" />
<script src="<wt:ue>/OpenLayers.js</wt:ue>" type="text/javascript"></script>
<script src="http://openstreetmap.org/openlayers/OpenStreetMap.js"
	type="text/javascript"></script>
<script src="<wt:ue>/js/prototype-1.6.1_rc2.js</wt:ue>" type="text/javascript"></script>
<script src="<wt:ue>/js/scriptaculous.js?load=effects,controls</wt:ue>" type="text/javascript"></script>
<script src="<wt:ue>/js/ae.js</wt:ue>" type="text/javascript"></script>

<script type="text/javascript">

		var MSG = {
			buttonStopCreating : '<spring:message code="button.stop.creating" />',
			buttonCreateNode : '<spring:message code="button.create.node" />', 
			templateInfo : '<spring:message code="template.info" />',  
			ebOsmButton : '<spring:message code="eb.osm.button" />', 
			ebOsmButtonTitle : '<spring:message code="eb.osm.button.label" />', 
			ebMoveButton : '<spring:message code="eb.move.button" />', 
			ebDeleteButton : '<spring:message code="eb.delete.button" />', 
			ebSaveButton : '<spring:message code="eb.save.button" />', 
			ebCloseButton : '<spring:message code="eb.close.button" />'
		};

		var URL = {
			search : "<wt:ue>/ae/search</wt:ue>",
			amenity : "<wt:ue>/ae/amenity</wt:ue>",
			acKey : "<wt:ue>/ae/ac/key</wt:ue>",
			acValue: "<wt:ue>/ae/ac/value</wt:ue>",
			osmUpdate : "<wt:ue>/ae/osmUpdate</wt:ue>",
			templates : "<wt:ue>/ae/templates</wt:ue>"
		};

		var keyValueTemplates = {

		};
		
        var wizardData = { 

		};

        var map;
        var layerMapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");

		var idCounter = 0;
		var addNewNode = false;
		var movingNode = false;	
		var MIN_ZOOM_FOR_EDIT = 14;
		var MAX_ZOOM = 18;

		var contextPath = '<wt:ue ignoreServletName="true"></wt:ue>';

		var oauthTokensAvailable = false;
		<c:if test="${startParameters.oauthTokenAvailable}">oauthTokensAvailable = true;</c:if>


        OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {
            defaultHandlerOptions: {
                'single': true,
                'double': false,
                'pixelTolerance': 0,
                'stopSingle': false,
                'stopDouble': false
            },
            initialize: function(options) {
                this.handlerOptions = OpenLayers.Util.extend(
                    {}, this.defaultHandlerOptions
                );
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
                this.handler = new OpenLayers.Handler.Click(
                    this, {
                        'click': this.trigger
                    }, this.handlerOptions
                );
            },
            trigger: function(e) {
                try
                {
					if (addNewNode)
					{
						AE.removeNewAmenityFeature();
						var lonlat = map.getLonLatFromViewPortPx(e.xy);
						
						var amenity = new Amenity(x2lon(lonlat.lon),y2lat(lonlat.lat));
						
						var feature = AE.createFeature(amenity);
						feature.popupClicked = true;
						AE.layerNew.addMarker(feature.marker);
						AE.newAmenityFeature = feature;
					}
					if (AE.isMoving())
					{
						AE.removeNewAmenityFeature();
						var lonlat = map.getLonLatFromViewPortPx(e.xy);

						AE.movingAmenity.newLon = x2lon(lonlat.lon);
						AE.movingAmenity.newLat = y2lat(lonlat.lat); 

						var feature = AE.createFeature(AE.movingAmenity,true);
						feature.popupClicked = true;
						AE.layerNew.addMarker(feature.marker);
						AE.newAmenityFeature = feature;
					}
				}
				catch (e)
				{
					alert(e);
				}
              }
              
            
          });
		

        function switchAdding()
        {
        	addNewNode = !addNewNode;
        	$("newNodeButton").value = addNewNode ? MSG.buttonStopCreating : MSG.buttonCreateNode; 
        	if (!addNewNode)
        	{
        		AE.removeNewAmenityFeature();
        	}

        }

        function Amenity(lon,lat)
        {
            this.nodeId = 0;
            this.lon = lon;
            this.lat = lat;
			this.keyValues = new Object();
			this.keyValues["amenity"] = "";
        }

        function plusfacteur(a) { return a * (20037508.34 / 180); }
        function moinsfacteur(a) { return a / (20037508.34 / 180); }
        function y2lat(a) { return 180/Math.PI * (2 * Math.atan(Math.exp(moinsfacteur(a)*Math.PI/180)) - Math.PI/2); }
        function lat2y(a) { return plusfacteur(180/Math.PI * Math.log(Math.tan(Math.PI/4+a*(Math.PI/180)/2))); }
        function x2lon(a) { return moinsfacteur(a); }
        function lon2x(a) { return plusfacteur(a); }
        function lonLatToMercator(ll) {
          return new OpenLayers.LonLat(lon2x(ll.lon), lat2y(ll.lat));
        }
        
        
        function init()
        {

			new Ajax.Request(URL.templates, {
		  		  method: 'get', 
		  		  onSuccess: function(transport) {
		  		  	var jsonData = transport.responseJSON;

			  		  keyValueTemplates = jsonData.keyValueTemplates;
			  		  wizardData = jsonData.wizardData;
		  		  	
		  		  }
		  		});
            

	        	map = new OpenLayers.Map('map', {
	                    maxExtent: new OpenLayers.Bounds(-20037508,-20037508,20037508,20037508),
	                    restrictedExtent: new OpenLayers.Bounds(-20037508,-20037508,20037508,20037508),
	                      numZoomLevels: 18,
	                      displayProjection: new OpenLayers.Projection("EPSG:4326"),
	                      units: 'm'
	                     });
	
	            AE.init(map, contextPath);
	            layerMapnik.attribution = null;
	            map.addLayers([layerMapnik, AE.layerNew, AE.layerMarkers]);
	            map.addControl(new OpenLayers.Control.LayerSwitcher());
	            map.addControl(new OpenLayers.Control.ScaleLine());
	            map.addControl(new OpenLayers.Control.Permalink());
	            map.addControl(new OpenLayers.Control.MousePosition());
	            var panel = new OpenLayers.Control.Panel();

	            map.setCenter(lonLatToMercator(new OpenLayers.LonLat(${startParameters.geoLocation.longitude},${startParameters.geoLocation.latitude})), ${startParameters.zoom}); 
	            map.events.register('moveend', map, updateAmenities);
	            map.events.register('zoomend', map, updateZoomStatus);
	
	            var click = new OpenLayers.Control.Click();
	            map.addControl(click);
	            click.activate();
	
	            loadSettingsFromCookie();
	            updateZoomStatus();
	            updateAmenities(null);
        }

        function autoCompleteCallBack(inputField, queryString)
        {
            var prev = inputField.previousSiblings();
            var keyValue = "";
            for (var x = 0; x < prev.length;x++)
            {
				var elem = prev[x];
				if (elem.tagName == "INPUT")
				{
					keyValue = elem.value;
					break;
				}
					
            }
			return queryString+"&key="+keyValue;
        }

        var extWest = 0;
        var extEast = 0;
        var extNorth = 0;
        var extSouth = 0;        

        function updateAmenities(event,url,forceUpdate) 
        {
			if (AE.isMoving())
				return;
			
        	url = url || URL.search;
			var coords = map.getCenter();
			var lon = x2lon(coords.lon);
			var lat = y2lat(coords.lat);
			var zoom = map.getZoom();
			if (zoom > MIN_ZOOM_FOR_EDIT)
			{
			
				var bounds = map.getExtent().toArray();
				var south = y2lat(bounds[1]);
				var north = y2lat(bounds[3]);
				var west = x2lon(bounds[0]);
				var east = x2lon(bounds[2]);

				if (forceUpdate || (extWest > west || extEast < east || extSouth > south || extNorth < north) || (url != URL.search))
				{
					south = extSouth = (Math.floor(south*100)/100)-0.015;
					north = extNorth = (Math.ceil(north*100)/100)+0.015;
					west = extWest = (Math.floor(west*100)/100)-0.015;
					east = extEast = (Math.ceil(east*100)/100)+0.015;
					
					var params = $("filterform").serialize(true);
					params["north"] = north;
					params["south"] = south;
					params["west"] = west;
					params["east"] = east;
					delete params["saveincookie"];
					
					$("loading").show();

					new Ajax.Request(url, {
				  		  method: 'get', parameters : params, 
				  		  onSuccess: function(transport) {
				  		  	var jsonData = transport.responseJSON;
				  		  	if (jsonData.message)
				  		  	{
					  		  	$("loading").hide();					  		  	
					  		  	alert(jsonData.message);
				  		  	}
				  		  	else
								AE.refreshAmenities(jsonData);
				  		  }
				  		});
				}
			}
		}

        function createKeyValueTable(amenity)
        {
            var nodeId = amenity.nodeId;

            var formTag = new Element("form",{"id":"form_"+nodeId,"action":URL.amenity,"method":"post"});
            formTag.insert(new Element("input",{"type":"hidden","name":"_nodeId","value":nodeId}));
            formTag.insert(new Element("input",{"type":"hidden","name":"lon","value":amenity.lon}));
            formTag.insert(new Element("input",{"type":"hidden","name":"lat","value":amenity.lat}));
            if (amenity.newLon && amenity.newLat)
            {
                formTag.insert(new Element("input",{"type":"hidden","name":"newlon","value":amenity.newLon}));
	            formTag.insert(new Element("input",{"type":"hidden","name":"newlat","value":amenity.newLat}));
            }   
            formTag.insert(new Element("input",{"type":"hidden","name":"_method","value":nodeId > 0 ? "put" : "post"}));

			for (var key in amenity.keyValues)
			{
				var keyId = "k_"+(idCounter)+"_"+nodeId;
				var valueId = "v_"+(idCounter++)+"_"+nodeId;
				formTag.insert(createTagValue(nodeId, key, amenity.keyValues[key]));
			}

			return formTag;
        }

        function updateKeyValueTable(nodeId)
        {
			var params = new Object();
			params["nodeId"] = nodeId;

        	new Ajax.Request(URL.amenity, {
      		  method: 'get', parameters : params, 
      		  onSuccess: function(transport) {
                createEditBox($("amenity_"+transport.responseJSON.nodeId), transport.responseJSON);
                alert("OK");
      		  }
      		});

        }

        function createLinkIcon(iconName, url, title)
        {
        	var elem = new Element("a",{"href":url,target:"_blank","class":"ae-url-icon"});
	    	elem.insert(new Element("img",{src: contextPath+"/img/icons/"+iconName,"title":title}));
	    	return elem;
        }

        function createTagValue(nodeId, key, value)
        {
			var keyId = "k_"+(idCounter)+"_"+nodeId;
			var valueId = "v_"+(idCounter++)+"_"+nodeId;
			var keyIdChoices = keyId+"_choices";
            var newDiv = new Element("div");
			newDiv.insert(new Element("input", {type:"text", id:keyId , name: "key", "class":"inputkey", size:24, "value":key}));	
			newDiv.insert(new Element("div", {id: keyIdChoices, "class":"autocomplete"}));
			newDiv.insert(new Element("script").update("new Ajax.Autocompleter('"+keyId+"', '"+keyId+"_choices', '"+URL.acKey+"', {paramName: 'input', method: 'get', minChars: 1, frequency: 0.5});"));
			newDiv.insert(new Element("span").update("&nbsp;"));
			var valueInput = new Element("input", {type:"text", id:valueId, name: "value", "class":"inputvalue", size:32, "value":value});
			newDiv.insert(valueInput);
			if (key == "url" && value != "")
			{
				newDiv.insert(createLinkIcon("world.png",value,"Show URL"));				
			}
			if ((key == "amenity") || (key == "shop"))
			{
				newDiv.insert(createLinkIcon("anchor.png","http://wiki.openstreetmap.org/wiki/Tag:"+key+"="+value,"Show Wiki"));
			}
			newDiv.insert(new Element("div", {id:valueId+"_choices", "class":"autocomplete"}));
			newDiv.insert(new Element("script").update("new Ajax.Autocompleter('"+valueId+"', '"+valueId+"_choices', '"+URL.acValue+"', {paramName: 'input', method: 'get', minChars: 1, frequency: 0.5, callback:autoCompleteCallBack});"));

			return newDiv;
        }

        function addTag(nodeId)
        {
			var keyId = "k_"+(idCounter)+"_"+nodeId;
			var newDiv = $("form_"+nodeId).insert(createTagValue(nodeId, "", ""));
			$(keyId).focus();
        }

        function addTags(nodeId, tags)
        {
            if (tags[""] == "")	// adding an empty key value pair?
            {
                addTag(nodeId);
            }
            else
            {
            var firstKey = null;
            var amenity = AE.getAmenity(nodeId);
			for (var key in tags)
			{
				if ((amenity) && (amenity.keyValues[key] == null))
				{
		        	if (firstKey == null)
			        	firstKey = "v_"+(idCounter)+"_"+nodeId;
					var newDiv = $("form_"+nodeId).insert(createTagValue(nodeId, key, tags[key]));
					amenity.keyValues[key] = tags[key];
				}
			}
			if (firstKey)
				$(firstKey).focus();
            }
        }

        

        function createAddTagIcon(nodeId, iconUrl, iconTitle, tags)
        {
        	var elem = new Element("a",{"href":"#","class":"ae-add-tag-icon",onclick:"addTags('"+nodeId+"',"+Object.toJSON(tags)+")"});
        	elem.insert(new Element("img",{"src":iconUrl,"title":iconTitle}));
        	return elem;
        }


        function createNewAmenityWizard(amenity)
        {
            var nodeId = amenity.nodeId;
        	var elem = new Element("div");
        	elem.insert(new Element("div",{"class":"ae-simple-text"}).update(MSG.templateInfo));

			for (var key in wizardData)
			{
				elem.insert(new Element("a",{"href":"#","class":"ae-create-amenity",onclick:"addDefaultTags('"+nodeId+"',"+Object.toJSON(wizardData[key])+")"}).update(key));
			}
        	
        	return elem;			
        }

        function addDefaultTags(nodeId, tags)
        {
            var amenity = AE.getAmenity(nodeId);
            amenity.keyValues = new Object();
            Object.extend(amenity.keyValues,tags);
            $("keyvaluetab_"+nodeId).update(createKeyValueTable(amenity));
            $("naw_"+nodeId).hide();
            $("keyvaluetab_"+nodeId).show();
            $("form_"+nodeId).focusFirstElement();
            
        }

        function createTitleDiv(amenity)
        {
            var div = new Element("div",{"class":"ae-nodedetails"});
            if (amenity.nodeId != 0)
	            div.insert(new Element("a",{"href":"http://www.openstreetmap.org/browse/node/"+amenity.nodeId,"target":"_blank"}).update("Id: "+amenity.nodeId));
            else
                div.insert("New Node");
            div.insert(" lon: "+amenity.lon+" lat: "+amenity.lat);
            return div;
        }

        function createEditBox(newDiv, amenity, feature)
        {
			newDiv.update(new Element("div",{"class":"ae-nodename"}).update("Node name: "+amenity.name));
			newDiv.insert(createTitleDiv(amenity));
			var editArea = new Element("div",{"class":"ae-editarea"});
			var keyValueTab = new Element("div",{"class":"ae-keyvaluetab","id":"keyvaluetab_"+amenity.nodeId}).update(createKeyValueTable(amenity));
			if (amenity.nodeId == 0)
			{
				keyValueTab.hide();
				var newAmenityWizard = new Element("div",{"class":"ae-keyvaluetab","id":"naw_"+amenity.nodeId}).update(createNewAmenityWizard(amenity));
				editArea.insert(newAmenityWizard);
			}
			editArea.insert(keyValueTab);
			newDiv.insert(editArea);

			var buttonDiv1 = new Element("div",{"class":"ae-buttons-top"});

			for (var x = 0; x < keyValueTemplates.length;x++)
			{
				var template = keyValueTemplates[x];
				buttonDiv1.insert(createAddTagIcon(amenity.nodeId,contextPath+template.iconUrl,
						template.iconTitle,template.tags));
			}

			newDiv.insert(buttonDiv1); 

			var buttonDiv = new Element("div",{"class":"ae-buttons"});
			var updateOsmButton = new Element("input",{"type":"button","class":"ae-edit-button","value":MSG.ebOsmButton,"title":MSG.ebOsmButtonTitle,"onclick":"updateKeyValueTable("+amenity.nodeId+")"});
			if ((amenity.nodeId == 0) || AE.isMoving())
				updateOsmButton.setAttribute("disabled","disabled");
			buttonDiv.insert(updateOsmButton);

			var moveButton = new Element("input",{type:"button","class":"ae-edit-button",value:MSG.ebMoveButton,onclick:"moveAmenity("+amenity.nodeId+")"});
			if ((amenity.nodeId == 0) || AE.isMoving())
				moveButton.setAttribute("disabled","disabled");
			buttonDiv.insert(moveButton);

			var deleteButton = new Element("input",{type:"button","class":"ae-edit-button",value:MSG.ebDeleteButton,onclick:"deleteAmenity("+amenity.nodeId+")"});
			if ((amenity.nodeId == 0) || AE.isMoving())
				deleteButton.setAttribute("disabled","disabled");
			buttonDiv.insert(deleteButton);
			buttonDiv.insert(new Element("input",{type:"button","class":"ae-edit-button",value:MSG.ebSaveButton,onclick:"saveAmenity("+amenity.nodeId+")"}));
			var closeButton = new Element("input",{type:"button","class":"ae-edit-button",value:MSG.ebCloseButton});
			buttonDiv.insert(closeButton);			

   			var closeIcon = new Element("div",{"class":"ae-closeicon"});
			closeIcon.update(new Element("a",{"href":"#"}));
			buttonDiv.insert(closeIcon);
			newDiv.insert(buttonDiv);   
			closeIcon.observe('click', AE.closePopupHandler.bindAsEventListener(amenity));
			closeButton.observe('click', AE.closePopupHandler.bindAsEventListener(amenity));
        }

        function checkboxesChanged(cbs)
        {
        	for (var x = 0; x < cbs.length;x++)
        	{
				if (cbs[x].defaultChecked != cbs[x].checked)
				{
					return true;
				}
        	}
        	return false;
        }

        function saveFilterSettings()
        {
        	$('filter').hide();

        	var f = $('filterform');

        	if (f.lon.value)
            	f.lon.value = f.lon.value.replace(/,/,".");
        	if (f.lat.value)
            	f.lat.value = f.lat.value.replace(/,/,".");
        	
        	var filterChanged = checkboxesChanged(f.getInputs("checkbox","show")) || checkboxesChanged(f.getInputs("checkbox","hide"));
        	
        	if (filterChanged)
        	{
	        	updateAmenities(null,null,true);
        	}

        	if (f.saveincookie.checked)
        	{
	        	var formJson = Object.toJSON(f.serialize(true));
	    		var cookie_str = escape(formJson);
	
	    		var date = new Date();
				date.setTime(date.getTime() + (1000*60*60*24*28));	// 28 tage
				var cookie_expires = '; expires=' + date.toGMTString();
	    		try {
	    			document.cookie = "settings="+cookie_str + cookie_expires;
	    		} catch (e) {
					alert(e);
	    		}
        	}
        }

        function loadSettingsFromCookie()
        {
            var formData = null;
    		var cookies = document.cookie.match('settings=(.*?)(;|$)');
    		if (cookies) 
        	{
    			formData = (unescape(cookies[1])).evalJSON();
	    		var f = $('filterform');
	
				f.saveincookie.checked = formData.saveincookie && formData.saveincookie == 1;
				f.lon.value = formData.lon ? formData.lon : "";
				f.lat.value = formData.lat ? formData.lat : "";

				var cbs = f.getInputs("checkbox","show");
	        	for (var x = 0; x < cbs.length;x++)
	        	{
					cbs[x].checked = (cbs[x].value == formData.show) || (formData.show instanceof Array && (formData.show.indexOf(cbs[x].value) != -1));
	        	}
				cbs = f.getInputs("checkbox","hide");
	        	for (var x = 0; x < cbs.length;x++)
	        	{
					cbs[x].checked = (cbs[x].value == formData.hide) || (formData.hide instanceof Array && (formData.hide.indexOf(cbs[x].value) != -1));
	        	}
				
				
<c:if test="${!startParameters.permalink}">	
			if (formData.lon && formData.lat)
		            map.setCenter(lonLatToMercator(new OpenLayers.LonLat(formData.lon,formData.lat)),MIN_ZOOM_FOR_EDIT+1);
</c:if>
    		}
        }

        function goToHomeBase()
        {
    		var f = $('filterform');
			if (f.lon.value && f.lat.value)
			{
	            map.setCenter(lonLatToMercator(new OpenLayers.LonLat(f.lon.value,f.lat.value)),MIN_ZOOM_FOR_EDIT+1);
			}
			else
			{
				alert("<spring:message code="alert.no.base" />");
			}
            
        }

        function showFilterSettings()
        {
        	var f = $('filterform');
        	var cbs = f.getInputs("checkbox");
        	for (var x = 0; x < cbs.length;x++)
        	{
				cbs[x].defaultChecked = cbs[x].checked;
        	}
            
        	$('filter').toggle();
        }     

        function moveAmenity(nodeId)
        {
            $("moving").show();
        	document.body.style.cursor='crosshair';
            AE.movingAmenity = AE.getAmenity(nodeId);
            AE.removeFeature(nodeId);
        }

        function cancelMoving()
        {
        	$("moving").hide();
        	document.body.style.cursor='auto';        	
        	AE.movingAmenity.newLon = null;
        	AE.movingAmenity.newLat = null;
        	AE.addFeature(AE.createFeature(AE.movingAmenity));
        	AE.movingAmenity = null;      
    		AE.removeNewAmenityFeature();
        }

        function checkAccessRights()
        {
            if (!oauthTokensAvailable)
            {
                alert("<spring:message code="alert.acceptlicense" />");
            }
            else
              return true;
        }
        

        function saveAmenity(nodeId)
        {
			var f = $("form_"+nodeId);
			if (checkAccessRights())
        	{
            	var params = new Object();
            	params = Object.extend(params, f.serialize(true));

            	if ((params["_nodeId"] == 0 || AE.isMoving()) && (params.key.indexOf("highway") != -1 || params.key.indexOf("railway") != -1))
            	{
                	alert("<spring:message code="no.highway.edit" />");
                	return;	
            	}

            	var requestMethod = 'post';
            	if (params["_nodeId"] != 0)
                	requestMethod = 'put';
            	
            	
				$("storing").show();            	            	

            	new Ajax.Request(URL.amenity, {
          		  method: requestMethod, parameters : params, 
          		  onSuccess: function(transport) {
        			AE.closePopup(nodeId);
					updateAmenities(null,null,true);
					$("storing").hide();
					$("moving").hide();
		        	document.body.style.cursor='auto';					
        			AE.removeNewAmenityFeature();
        			AE.movingAmenity = null;
        			alert(transport.responseJSON.message+"\n\n<spring:message code="save.action.info" />");
          		  }
          		});
        	}
        }            

        function deleteAmenity(nodeId)
        {
            if (confirm("<spring:message code="confirm.delete" />"))
            {
				var f = $("form_"+nodeId);
				if (checkAccessRights())
	        	{
	            	var params = new Object();
	            	params = Object.extend(params, f.serialize(true));
	            	
	            	new Ajax.Request(URL.amenity, {
	          		  method: 'delete', parameters : params, 
	          		  onSuccess: function(transport) {
	        			AE.closePopup(nodeId);
						updateAmenities(null,null,true);
	        			alert(transport.responseJSON.message);
	          		  }
	          		});
	        	}
            }
        }      

        function updateZoomStatus()
        {
			var zoom = map.getZoom();
			if (zoom <= MIN_ZOOM_FOR_EDIT)
			{
				$("zoomStatus").show();
				$("newNodeButton").disable();
				$("loadOsmDataButton").disable();
			}
			else
			{
				$("zoomStatus").hide();
				$("newNodeButton").enable();
				$("loadOsmDataButton").enable();
			}            
        }      

        function loadBbox()
        {
        	updateAmenities(null,URL.osmUpdate,true); 
        }
        function setMapCenterAsHomeBase()
        {
			var coords = map.getCenter();
			$("filterform").lon.value = x2lon(coords.lon);
			$("filterform").lat.value = y2lat(coords.lat);
        }

        function setMaxZoom()
        {
        	map.zoomTo(MAX_ZOOM);
        }        

        function selectAll()
        {
            var elements = new Selector('.filter-positive-cb').findElements($('filterform'));
			$A(elements).each(function(element) {
				element.checked = true;
			});
        }

        function deselectAll()
        {
            var elements = new Selector('.filter-positive-cb').findElements($('filterform'));
			$A(elements).each(function(element) {
				element.checked = false;
			});
        }

        function invertSelection()
        {
            var elements = new Selector('.filter-positive-cb').findElements($('filterform'));
			$A(elements).each(function(element) {
				element.checked = !element.checked;
			});
        }    

        window.onload=init;
    </script>
</head>

<body>
<div id="content" style="overflow: hidden; height: 100%">
<div style="padding:5px;height: 32px; position:absolute; left: 80px; top: 0px; right:60px; z-index:750; -moz-border-radius:0px 0px 6px 6px; border-radius:0px 0px 6px 6px; background-color: #FFFFFF; background-repeat: repeat-x; border:1px solid #000000;border-top:none">
<div style="font-weight:bold;font-size:14px;">OSM Amenity Editor (${startParameters.version})</div>
<div style="position:relative; bottom:-5px; left:0px;font-size:9px;font-weight: normal">Made by <a href="http://www.grundid-gmbh.de">GrundID GmbH</a>, Data by <a href="http://www.openstreetmap.org">OpenStreetMap</a> under <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, with <a href="http://code.google.com/p/google-maps-icons/">Maps icons collection (CC BY SA 3.0)</a>, Contact: as(a t)osmsurround(d o t)org, <a href="http://www.openstreetmap.org/user/nitegate">OSM User: nitegate</a>, Source Code: <a href="https://github.com/grundid/amenity-editor">@GitHub</a></div>
<div style="position:absolute; right:5px; top: 5px; width:780px;text-align: right">
<c:if test="${startParameters.oauthTokenAvailable}"><span style="padding:2px;background-color:#ffe7cd;font-size:14px">OAuth OK!</span></c:if>
<input type="button" class="ae-small-button" value="<spring:message code="button.max.zoom" />" title="<spring:message code="button.max.zoom.hint" />" onclick="setMaxZoom();" />
<input type="button" class="ae-small-button" value="<spring:message code="button.home.base" />" title="<spring:message code="button.home.base.hint" />" onclick="goToHomeBase();" />
<input type="button" class="ae-small-button" value="<spring:message code="button.create.node" />" id="newNodeButton" title="<spring:message code="button.create.node.hint" />" onclick="switchAdding();" style="width:150px" />
<input type="button" class="ae-small-button" value="<spring:message code="button.rod" />" id="loadOsmDataButton" title="<spring:message code="button.rod.hint" />" onclick="loadBbox();"/>
<input type="button" class="ae-small-button" value="<spring:message code="button.settings" />" title="<spring:message code="button.settings.hint" />" onclick="showFilterSettings();">
<input type="button" class="ae-small-button" value="<spring:message code="button.help" />" title="<spring:message code="button.help.hint" />" onclick="$('help').toggle();">
<input type="button" class="ae-small-button" value="<spring:message code="button.oauth" />" title="<spring:message code="button.oauth.hint" />" onclick="window.location.href='<wt:ue>/ae/oauthRequest</wt:ue>';" />
</div>
</div>
<div style="position:absolute; left: 80px; top: 55px; right:60px; z-index:1000;">
<center>
<div class="infobox" style="width:800px;display:none;text-align: left" id="filter">
<form id="filterform">
<fieldset style="width:520px;float:left">
<legend><spring:message code="settings.show" /></legend>
<c:forEach items="<%= FilterManager.getInstance().getFiltersPositive() %>" var="filter">
<div style="width:170px;float:left;">
<input type="checkbox" class="filter-positive-cb" name="show" value="${filter.name}" <c:if test="${filter.defaultSelected}">checked="checked" </c:if> id="cb_${filter.name}" />
&nbsp;<label for="cb_${filter.name}"><spring:message code="filter.${filter.name}" />&nbsp;<img src="<wt:ue>/img/${filter.icon}</wt:ue>" width="12" height="12"></label></div>
</c:forEach>
<div style="clear:both"><input type="button" class="ae-small-button" value="<spring:message code="button.select.all" />" onclick="selectAll();" />
<input type="button" class="ae-small-button" value="<spring:message code="button.deselect.all" />" onclick="deselectAll();"/>
<input type="button" class="ae-small-button" value="<spring:message code="button.invert.selection" />" onclick="invertSelection();"/></div>
</fieldset>
<fieldset style="width:240px">
<legend><spring:message code="settings.hide" /></legend>
<c:forEach items="<%= FilterManager.getInstance().getFiltersNegative() %>" var="filter">
<input type="checkbox" name="hide" value="${filter.name}" id="cb_${filter.name}" />
&nbsp;<label for="cb_${filter.name}"><spring:message code="filter.${filter.name}" /></label><br/>
</c:forEach>
</fieldset>
<fieldset style="clear:both">
<legend><spring:message code="settings.userpass" /></legend>
<div><spring:message code="settings.noaccount" /></div>
</fieldset>
<fieldset>
<legend><spring:message code="settings.base" /></legend>
<div style="height: 22px;position: relative;">
<span style="vertical-align: middle;"><spring:message code="settings.lon" />:&nbsp;</span><input type="text" name="lon" size="12" style="width:100px;">
<span style="vertical-align: middle;"><spring:message code="settings.lat" />:&nbsp;</span><input type="text" name="lat" size="12" style="width:100px;"></div> 
<input type="button" class="ae-small-button" value="<spring:message code="settings.button.center" />" onclick="setMapCenterAsHomeBase();">
</fieldset>
<fieldset>
<legend><spring:message code="settings.cookies" /></legend>
<input type="checkbox" name="saveincookie" value="1" id="cb_saveincookie" />&nbsp;<label for="cb_saveincookie"><spring:message code="settings.saveincookie" /></label>
</fieldset>
</form>
<p>
<input type="button" class="ae-small-button" value="<spring:message code="settings.button.apply" />" onclick="saveFilterSettings();">
<input type="button" class="ae-small-button" value="<spring:message code="settings.button.close" />" onclick="$('filter').hide();">
</div>
<%@ include file="includes/infobox.jspf" %>
<div class="infobox" style="width:250px;display:none" id="feamenities">
<spring:message code="info.fewamenities" />
</div>
<div class="infobox" style="width:350px;display:none" id="moving">
<spring:message code="move.info" /><br/>
<input type="button" class="ae-small-button" value="<spring:message code="move.button" />" title="<spring:message code="move.button.hint" />" onclick="cancelMoving()">
</div>
<div class="infobox" style="width:170px;display:none" id="loading">
<spring:message code="status.loading.data" /><br><img src="<wt:ue>/img/throbber.gif</wt:ue>" >
</div>
<div class="infobox" style="width:170px;display:none" id="storing">
<spring:message code="status.saving.data" /><br><img src="<wt:ue>/img/throbber.gif</wt:ue>" >
</div>
<div class="infobox" style="width:400px;display:none" id="zoomStatus">
<spring:message code="status.zoom.to.small" /><br>
<input type="button" class="ae-small-button" value="<spring:message code="button.adujst.zoom" />" title="<spring:message code="button.adujst.zoom.hint" />" onclick="map.zoomTo(MIN_ZOOM_FOR_EDIT+1)">
</div>
</center>
</div>
<div style="height: 100%" id="map"></div>
</div>
<noscript><spring:message code="no.javascript" /></noscript>
</body>
</html>