/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_GeodbRecordsGrid (target, cfg) {	
	this.target = target;
	var error = this.initCfg(cfg);
	if(error){
		
	} else {
	
	}
	
	return this;
}

WELOCALLY_GeodbRecordsGrid.prototype.initCfg = function(cfg) {
	var _instance = this;
};


/*
 * {
        "id": "New York Taylor Shop",
        "properties": [
            {
                "name": "place_name",
                "value": "New York Taylor Shop"
            },
            {
                "name": "place_address",
                "value": "60 Kenmare St"
            },
            {
                "name": "place_city",
                "value": "New York"
            },
            {
                "name": "place_province",
                "value": "NY"
            },
            {
                "name": "place_postcode",
                "value": "10012"
            },
            {
                "name": "place_country",
                "value": "US"
            },
            {
                "name": "place_website",
                "value": "foobar"
            },
            {
                "name": "place_phone",
                "value": "+1 212 343 8790"
            },
            {
                "name": "place_type",
                "value": "Retail Goods"
            },
            {
                "name": "place_category",
                "value": "Shopping"
            },
            {
                "name": "place_subcategory",
                "value": "Clothing Apparel & Fashions"
            },
            {
                "name": "place_lat",
                "value": "40.7208609"
            },
            {
                "name": "place_lng",
                "value": "-73.995966"
            }
        ],
        "geometry": {
            "type": "Point",
            "coordinates": [
                -73.995966,
                40.7208609
            ]
        }
    }
 */
WELOCALLY_GeodbRecordsGrid.prototype.setRecords = function(data) {
	var _instance = this;
	var headers = [];
	var table = $('<table class="geogrid table"></table>');
	$(data).each(function (rindex, record) {
		var cols = [];
		
		//iterate the properties
		$(record.properties).each(function (findex, nv) {
			if(rindex==0) {
				headers.push(nv.name);
			} 
			cols.push(nv.value);	
				
		});
		
		//do the header
		if(headers.length>0 && rindex==0)
			$(table).append(Mustache.render('<thead><tr>{{#.}}<th>{{.}}</th>{{/.}}</tr></thead>', headers));
		
		$(table).append(Mustache.render('<tr>{{#.}}<td>{{.}}</td>{{/.}}</tr>', cols));
			
	});
	$(_instance.target).html(table);	
		
};