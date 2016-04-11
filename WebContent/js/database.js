var storage = {
	//Database driver defined for Cloudant
	db: new PouchDB('https://455678c4-5f99-426a-80ce-3c1694c262c1-bluemix.cloudant.com/flexmash', {
		ajax: {
			cache: false,
			timeout: 10000,
			headers: {
				//'Access-Control-Allow-Origin': 'true'
			},
		},
		auth: {
			username: '455678c4-5f99-426a-80ce-3c1694c262c1-bluemix',
			password: '4dfde9d9855c8e07a5479af74f769d253ce0b03014e592f418215e0878512aa6'
		}
	}),
	initializeData: function(Y) {
		/*var mask = new Ext.LoadMask({
			msg    : 'Please wait...',
			target : Ext.getBody()
		});
		mask.show();*/
		//perform async request to the database and initializ data
		storage.db.get('nodes', function (err, response) {
			if (response != null && response.info) {
				app.dynamicNodes = response.info;
				storage.lastRevision = response;
			} else {
				app.dynamicNodes = [];
                storage.lastRevision = [];
			}
			//Lunch the application as the data is initialized
			application.luncher(Y);
			//mask.hide();
		});
	}
};