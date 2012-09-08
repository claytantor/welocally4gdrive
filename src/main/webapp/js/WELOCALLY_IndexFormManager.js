function WELOCALLY_IndexFormManager () {	
}

WELOCALLY_IndexFormManager.prototype.initCfg = function( cfg ) { 
	this.saveEndpoint= cfg.saveEndpoint;
	this.indexId = cfg.indexId;
	this.spreadsheetKey=cfg.spreadsheetKey,
	this.worksheetFeed=cfg.worksheetFeed,	
	this.searchFields = cfg.searchFields;
	this.primaryKey = cfg.primaryKey;
	this.lat = cfg.lat;
	this.lng = cfg.lng;
};

WELOCALLY_IndexFormManager.prototype.handleDrop = function(dropped, ui )  {
	//ok
	var instance = this;
	  
	if($(dropped).parent().attr("id") == 'fieldsearchable' &&
			!WELOCALLY_Util.util.listContains(instance.searchFields,ui.draggable.text())){	
			$( dropped ).find( ".phold" ).remove();
			
			instance.searchFields.push(ui.draggable.text());
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', ui.draggable.text()));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance, val:ui.draggable.text()},instance.closeSearchItem);								
			$(li).append(close);				
			$(li).appendTo( dropped );
			
		} else if($(dropped).parent().attr("id") == 'fieldpk' && instance.primaryKey=='') {

			$( dropped ).find( ".phold" ).remove();
			
			instance.primaryKey = ui.draggable.text();
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', ui.draggable.text()));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance},instance.closePkItem);
			$(li).append(close);				
			$(li).appendTo( dropped );
			
		} else if($(dropped).parent().attr("id") == 'fieldlat' && instance.lat=='') {

			$( dropped ).find( ".phold" ).remove();
			
			instance.lat = ui.draggable.text();
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', ui.draggable.text()));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance},instance.closeLat);
			$(li).append(close);				
			$(li).appendTo( dropped );
			
		} else if($(dropped).parent().attr("id") == 'fieldlng' && instance.lng=='') {

			$( dropped ).find( ".phold" ).remove();
			
			instance.lng = ui.draggable.text();
			
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', ui.draggable.text()));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance},instance.closeLng);
			$(li).append(close);				
			$(li).appendTo( dropped );
			
		}
	
};

WELOCALLY_IndexFormManager.prototype.makeDroplist = function( target, name ) {
	var instance = this;
	$(target).html('');
	if(name=='fieldsearchable'){
		if(instance.searchFields.length==0){
			$(target).append('<li class="phold fselect">Drop searchable fields here</li>');
		} else {
			$(instance.searchFields).each(function(index,item){
				var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', item));
				var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
				$(li).bind('closed',{instance:instance, val:item},instance.closeSearchItem);								
				$(li).append(close);				
				$(li).appendTo( target );
			});	
		}			
	} else if(name=='fieldpk'){
		if(instance.primaryKey==''){
			$(target).append('<li class="phold fselect">Drop primary key field here</li>');
		} else {
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', instance.primaryKey));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance, val:instance.primaryKey},instance.closePkItem);								
			$(li).append(close);				
			$(li).appendTo( target );
		}
		

	} else if(name=='fieldlat'){
		if(instance.lat==''){
			$(target).append('<li class="phold fselect">Drop latitude field here</li>');
		} else {
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', instance.lat));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance, val: instance.lat},instance.closeLat);								
			$(li).append(close);				
			$(li).appendTo( target );
		}

	} else if(name=='fieldlng'){
		if(instance.lng==''){
			$(target).append('<li class="phold fselect">Drop longitude field here</li>');
		} else {
			var li = $( '<li class="fselect label label-info"></li>' ).append(Mustache.render('<span class="fitem_text">{{.}}</span>', instance.lng));
			var close = $('<button type="button" class="close" data-dismiss="alert" style="color:white; line-height: 10px; font-size: 12px; opacity: 1.0;">x</button>');
			$(li).bind('closed',{instance:instance, val: instance.lng},instance.closeLng);								
			$(li).append(close);				
			$(li).appendTo( target );
		}

	}
};

WELOCALLY_IndexFormManager.prototype.closeSearchItem = function( event, ui ) {

	var instance = event.data.instance;
	var val = event.data.val; 
	
	instance.searchFields.splice(instance.searchFields.indexOf(val), 1);
	
	if($(this).parent().find('.fselect').length==1){
		$(this).parent().append('<li class="phold fselect">Drop searchable fields here</li>');
	}
};

WELOCALLY_IndexFormManager.prototype.closePkItem = function( event, ui ) {
	var instance = event.data.instance;
	instance.primaryKey = '';
	if($(this).parent().find('.fselect').length==1){
		$(this).parent().append('<li class="phold fselect">Drop primary key field here</li>');
	}
};

WELOCALLY_IndexFormManager.prototype.closeLat = function( event, ui ) {
	var instance = event.data.instance;
	instance.lat = '';
	if($(this).parent().find('.fselect').length==1){
		$(this).parent().append('<li class="phold fselect">Drop latitude key field here</li>');
	}
};

WELOCALLY_IndexFormManager.prototype.closeLng = function( event, ui ) {
	var instance = event.data.instance;
	instance.longfield = '';
	if($(this).parent().find('.fselect').length==1){
		$(this).parent().append('<li class="phold fselect">Drop longitude key field here</li>');
	}
};


WELOCALLY_IndexFormManager.prototype.makeSelectionModel = function( target ) {
	var model = {
			searchFields:[],
			primaryKey:'',
			lat:'',
			lng:''		
	};
	var drops = $(target).find('.fdrop');
	$(drops).each(function(index,list){	
		var name = $(list).parent().attr('id');
		var phold = $(list).find('.phold');
		if(phold.length==0){
			$(list).find('.fselect').each(function(index,item){	
				if(name=='fieldsearchable'){ model.searchFields.push($(item).find('.fitem_text').text()) }
				else if(name=='fieldpk'){ model.primaryKey = $(item).find('.fitem_text').text() }
				else if(name=='fieldlat'){ model.lat = $(item).find('.fitem_text').text() }
				else if(name=='fieldlng'){ model.lng = $(item).find('.fitem_text').text()  }				
			});			
		}		
	});
		
	return model;
	
};

////{"errors":[{"errorMessage":"Please provide the name for your site.","errorCode":104}]}
WELOCALLY_IndexFormManager.prototype.validateModel = function( model ) {
	var messages = {errors:[]};
	
	if(model.primaryKey==''){
		messages.errors.push({errorMessage:'A primary key field is required, choose something that has a unique value for each row.',errorCode:104});
	}
	
	if(model.lat==''){
		messages.errors.push({errorMessage:'The location field for Latitude is missing.',errorCode:108});
	}
	
	if(model.lng==''){
		messages.errors.push({errorMessage:'The location field for Longitude is missing.',errorCode:112});
	}
	
	if(model.searchFields.length==0){
		messages.errors.push({errorMessage:'At least one search field is required to create an index.',errorCode:106});
	}
	
	return messages;	
};


WELOCALLY_IndexFormManager.prototype.saveIndex = function( model, messageTarget ) {
	
	var instance = this;
	var messages = instance.validateModel(model);	
	if(messages.errors.length==0){		
		model.indexId = instance.indexId;
		model.spreadsheetKey=instance.spreadsheetKey;
		model.worksheetFeed=instance.worksheetFeed;
		
		instance.jqxhr = jQuery.ajax({
			  type: 'POST',		  
			  url: instance.saveEndpoint,
			  dataType : 'json',
			  data: model,
			  beforeSend: function(jqXHR){
				instance.jqxhr = jqXHR;
			  },
			  error : function(jqXHR, textStatus, errorThrown) {
				if(textStatus != 'abort'){
					var messages = {errors:[{errorMessage:"Problem when attempting to save.",errorCode:204}]};
					return messages;
				}		
			  },		  
			  success : function(data, textStatus, jqXHR) {
				  $(messageTarget).html('<div class="alert alert-success"><strong>Success!</strong> Your index has been posted to the queue and will begin to be loaded very soon. Look to check that the status is PUBLISHED before attempting to create searches.</div>');
			  }
		});
		
		
	} else {
		return messages;
	}	
};