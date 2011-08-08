var AE = {
	contextPath : "",
	userSettings : new Object(),
	features : new Object(),
	layerMarkers : new OpenLayers.Layer.Markers("OSM amenities"),
	layerNew : new OpenLayers.Layer.Markers("New amenity"),

	iconAmenity : null,
	iconNewAmenity : null,

	newAmenityFeature : null,
	movingAmenity : null,

	map : null,

	init : function(map, contextPath) {
		this.layerMarkers.setOpacity(0.9);
		this.map = map;
		this.contextPath = contextPath;

		this.iconAmenity = new OpenLayers.Icon(contextPath + '/img/marker-blue.png',
				new OpenLayers.Size(21, 25), new OpenLayers.Pixel(-10, -25));
		this.iconNewAmenity = new OpenLayers.Icon(contextPath + '/img/marker-gold.png',
				new OpenLayers.Size(21, 25), new OpenLayers.Pixel(-10, -25));
		
	},

	showPopup : function(feature) {
		if (feature.popup == null) {
			feature.data.popupContentHTML = "<div id=\"amenity_" + feature.amenity.nodeId
					+ "\"  style=\"padding:5px\"><\/div>";
			feature.popup = feature.createPopup();
			this.map.addPopup(feature.popup);

			createEditBox($("amenity_" + feature.amenity.nodeId), feature.amenity, feature);
			feature.popup.updateSize();
		} else {
			feature.popup.show();
		}
		feature.popupVisible = true;
	},

	hidePopup : function(feature) {
		// TODO Check for unsaved changes
		feature.popup.hide();
		feature.popupVisible = false;
		feature.popupClicked = false;
	},

	closePopupHandler : function(event) {
		// special case: this is an amenity object
		var nodeId = this.nodeId;
		AE.closePopup(nodeId);
	},

	closePopup : function(nodeId) {
		var feature = null;
		if (nodeId > 0)
			feature = AE.features[nodeId];
		else
			feature = this.newAmenityFeature;
		if (feature == null) {
			// wir verschieben gerade einen Knoten
			if (this.newAmenityFeature.amenity.nodeId == nodeId)
				feature = this.newAmenityFeature;
		}
		if (feature != null) {
			AE.hidePopup(feature);
		}
	},

	createFeature : function(amenity, moving) {
		var lon = amenity.newLon ? lon2x(amenity.newLon) : lon2x(amenity.lon);
		var lat = amenity.newLat ? lat2y(amenity.newLat) : lat2y(amenity.lat);
		var iconclone = null;
		moving = moving || amenity.nodeId == 0;
		if (!moving) {
			if (amenity.matchedIcon) {
				var iconSize = new OpenLayers.Size(21, 25);
				var iconOffset = new OpenLayers.Pixel(-10, -25);
				if (amenity.matchedIcon.indexOf('map/') != -1) {
					iconSize = new OpenLayers.Size(32, 37);
					iconOffset = new OpenLayers.Pixel(-16, -37);
				}
				iconclone = new OpenLayers.Icon(this.contextPath + '/img/' + amenity.matchedIcon,
						iconSize, iconOffset);
			}

			if (iconclone == null)
				iconclone = this.iconAmenity.clone();
		} else
			iconclone = this.iconNewAmenity.clone();

		var feature = new OpenLayers.Feature(!moving ? this.layerMarkers : this.layerNew,
				new OpenLayers.LonLat(lon, lat), {
					icon : iconclone
				});
		feature.closeBox = false;
		feature.popupClass = OpenLayers.Class(OpenLayers.Popup.FramedCloud);
		feature.data.overflow = "hidden";
		feature.popupVisible = false;
		feature.popupClicked = false;
		feature.amenity = amenity;
		var marker = feature.createMarker();

		feature.markerClick = function(evt) {
			if (this.popupVisible) {
				if (this.popupClicked) {
					AE.hidePopup(this);
				} else {
					this.popupClicked = true;
				}
			} else {
				AE.showPopup(this);
				this.popupClicked = true;
			}
			OpenLayers.Event.stop(evt);
		};
		feature.markerOver = function(evt) {
			if (!AE.isMoving()) {
				document.body.style.cursor = 'pointer';
				AE.showPopup(this);
			}
			OpenLayers.Event.stop(evt);
		};
		feature.markerOut = function(evt) {
			document.body.style.cursor = 'auto';
			if (this.popup != null && !this.popupClicked)
				AE.hidePopup(this);
			OpenLayers.Event.stop(evt);
		};
		marker.events.register("mousedown", feature, feature.markerClick);
		marker.events.register("mouseover", feature, feature.markerOver);
		marker.events.register("mouseout", feature, feature.markerOut);

		return feature;

	},

	isMoving : function() {
		return this.movingAmenity != null;
	},

	getAmenity : function(nodeId) {
		var feature = this.features[nodeId];
		if (feature != null) {
			return feature.amenity;
		} else {
			if (this.newAmenityFeature.amenity.nodeId == nodeId)
				return this.newAmenityFeature.amenity;
			else
				return null;
		}
	},

	removeFeature : function(nodeId) {
		var feature = this.features[nodeId];
		if (feature != null) {
			this.layerMarkers.removeMarker(feature.marker);
			feature.destroy();
			delete this.features[nodeId];
		}
	},
	addFeature : function(feature) {
		this.layerMarkers.addMarker(feature.marker);
		this.features[feature.amenity.nodeId] = feature;
	},
	refreshAmenities : function(amenityArray) {
		try {
			var feature = null;
			// Delete all features
			for ( var nodeId in this.features) {
				this.removeFeature(nodeId);
			}
			this.features = new Object();

			// create new features

			for ( var x = 0; x < amenityArray.length; x++) {
				var amenity = amenityArray[x];
				this.addFeature(this.createFeature(amenity));
			}

			if (amenityArray.length < 1) {
				$("feamenities").show();
			} else {
				$("feamenities").hide();
			}

		} catch (e) {
			alert(e);
		}
		$("loading").hide();
	},
	removeNewAmenityFeature : function() {
		if (this.newAmenityFeature != null) {
			this.layerNew.removeMarker(this.newAmenityFeature.marker);
			this.newAmenityFeature.destroy();
			this.newAmenityFeature = null;
		}
	}

};
