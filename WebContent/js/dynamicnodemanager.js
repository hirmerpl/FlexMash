var dynamicnodemanager = {
	initialize: function(Y) {
		var availableFields = [];
		//check and see if there are dynamic nodes to be created
		if (app.dynamicNodes) {
			for (var i = 0; i < app.dynamicNodes.length; i++) {
				var node = app.dynamicNodes[i];
				//Define a namespace for the dynamic node ad characterize it
				Y['DiagramNode' + node['id']] = Y.Component.create({
					NAME: 'diagram-node',
					config: node['nodes'],
					ATTRS: {
						type: {
							value: node['id']
						},
						criteria: {
							validator: Y.Lang.isString,
							value: ''
						}
					},
					EXTENDS: Y.DiagramNodeTask,
					prototype: {
						initializer: function() {
							
						}
					}
				});
				//Define the mapings dynamically and add the new node to available fields to be preocessed by application
				Y.DiagramBuilder.types[node['id']] = Y['DiagramNode' + node['id']];
				availableFields.push({
					id: node['id'],
					iconClass: 'diagram-node-' + node['id'] + '-icon',
					label: node['nodeName'],
					type: node['id']
				});
				//Add CSS rules to the document for the newly created node types
				dnm.addNodeCSSClass(node['id'], node['iconURL']);
			}
		}
		return availableFields;
	}, createAvailableFields: function(Y) {
    		var availableFields = [];
		//check and see if there are dynamic nodes to be created
		if (app.dynamicNodes) {
			for (var i = 0; i < app.dynamicNodes.length; i++) {
				var node = app.dynamicNodes[i];

				//Define the mapings dynamically and add the new node to available fields to be preocessed by application
				availableFields.push({
					id: node['id'],
					iconClass: 'diagram-node-' + node['id'] + '-icon',
					label: node['nodeName'],
					type: node['id']
				});
			}
		}
		return availableFields;
    },
	addNewNode: function() {
		alert("TODO add new node");
	},
	//append a style elemet to the document for newly created node type 
	addNodeCSSClass: function(name, imageURL) {
		var style = document.createElement('style');
		style.type = 'text/css';
		style.innerHTML = '.diagram-node-' + name + ' .diagram-node-content {background: url(' + imageURL + ') no-repeat scroll center transparent;} .diagram-node-' + name + '-icon {background: url(' + imageURL + ') no-repeat scroll center transparent;}';
		document.getElementsByTagName('head')[0].appendChild(style);
	}
};
var dnm = dynamicnodemanager;