/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_GeodbRecordSearch (cfg) {		
	var cfg;
	var error = this.initCfg(cfg);
	if(error){
		
	} else {
	
	}
	
	return this;
}

WELOCALLY_GeodbRecordSearch.prototype.initCfg = function(cfg) {
	var _instance = this;
	_instance.cfg = cfg;
};

WELOCALLY_GeodbRecordSearch.prototype.search = function() {
	//requires event data has the search instance
	var _instance = this;
		
	/*var query= {
		indexId: 'c26b3722e7e1',
		q: 'shopping',
		loc: '40.721417_-73.988274',
		radiusKm: 10.0
	};*/
		
	//var surl = _instance.cfg.endpoint +
	//	_instance.cfg.requestPath+'?'+WELOCALLY_Util.util.serialize(query)+"&callback=?";
	//curl -XGET --header "Content-Type: application/json" "http://localhost:8082/geodb/georecords/1_0/search.json?indexId=c26b3722e7e1&q=shopping&loc=40.721417_-73.988274&radiusKm=10"
	var surl = 'http://localhost:8082/geodb/georecords/1_0/search.json'+'?'+WELOCALLY_Util.util.serialize(_instance.cfg.query)+"&callback=?";
		
		
//	//notify all observers
//	jQuery.each(_instance.cfg.observers, function(i,observer) {
//		observer.setStatus(observer.getStatusArea(), 'Finding places','wl_update',true);
//	});	
	
					
	jQuery.ajax({
			  url: surl,
			  dataType: "json",
			  success: function(data) {
				//set to result bounds if enough results
				if(data != null && data.errors != null) {
					//notify all observers
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						//observer.setStatus(observer.getStatusArea(),'ERROR:'+WELOCALLY_Util.util.getErrorString(data.errors), 'wl_error', false);
					});
					
					
				} else if(data != null && data.length>0){							
					//notify all observers
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						//observer.setStatus(observer.getStatusArea(), '','wl_message',false);
						observer.setRecords(data);
					});
					
				} else {				
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						//observer.setStatus(observer.getStatusArea(), 'No results were found matching your search.','wl_warning',false);						
					});	
					
				}														
			}
	});
	
	
	return false;
	
};