var application = {
	luncher: function(Y) {

		//Initialize the static nodes frm Staticnodemanager.js
		var availableFields = snm.initialize(Y).concat(dnm.initialize(Y));

		//make the diagram for Aloye UI
        diagram = new Y.DiagramBuilder({
            availableFields: availableFields,
            boundingBox: '#myDiagramContainer',
            fields: [
              /*{
                name: 'Start',
                type: 'start',
                xy: [20, 250]
              },
              {
                name: 'NYT',
                type: 'dataSource_NYT',
                xy: [220, 100]
              },
              {
                name: 'Twitter',
                type: 'dataSource_twitter',
                xy: [220, 400]
              },
              {
                name: 'Filter',
                type: 'filter',
                xy: [520, 100]
              },
              {
                name: 'Analytics',
                type: 'analytics',
                xy: [520, 400]
              },
              {
                name: 'Merge',
                type: 'merge',
                xy: [720, 250]
              },
              {
                name: 'End',
                type: 'end',
                xy: [1020, 260]
              }*/
            ],
            srcNode: '#myDiagramBuilder'
        }).render();
		app.diagramBuilder = diagram;
		
        /*diagram.connectAll(
          [
            {
              connector: {
                name: ''
              },
              source: 'Start',
              target: 'NYT'
            },
            {
              connector: {
                name: ''
              },
              source: 'NYT',
              target: 'Filter'
            },
            {
              connector: {
                name: ''
              },
              source: 'Twitter',
              target: 'Analytics'
            },
            {
              connector: {
                name: ''
              },
              source: 'Filter',
              target: 'Twitter'
            },
            {
              connector: {
                name: ''
              },
              source: 'Analytics',
              target: 'Merge'
            },
            {
              connector: {
                name: ''
              },
              source: 'Merge',
              target: 'End'
            }
          ]
        );*/
        
        /*
        Y.one('#getButton').on(
            'click', 

            function() {
                jsonGetString = JSON.stringify(diagram.toJSON());
                jsonGetString = jsonGetString.substring(0, jsonGetString.length - 1);
                jsonGetString = jsonGetString + ",\"wrapper\":\"true\"}"
                Y.io.request(
                    'http://localhost:8080/Data_Mashup/DataMashup',
                    {
                    	method: 'POST',
                        data: jsonGetString,
                        on: {
                            success: function() {
                                var data = this.get('responseData');
                                alert(data);
                            },
                            failure: function() {
                                alert('failure');
                            }
                        }
                    }
                );
            }
        );
		*/

		//Store for Node types combo box
		app.nodeTypesStore = Ext.create('Ext.data.Store', {
			fields: ['key', 'name'],
			data : [
				{"key":"source", "name":"Source"},
				{"key":"subflow", "name":"Subflow"}
			]
		});

		//node type combo box initialization
		app.nodeTypesCombo = Ext.create('Ext.form.ComboBox', {
			fieldLabel: 'Node Types',
			name: 'nodeType',
			store: app.nodeTypesStore,
			queryMode: 'local',
			displayField: 'name',
			valueField: 'key',
			value: 'subflow',
			editable: false,
			listeners: {
				select: function(combo, records, eOpts) {
					if (records.get('key') == 'source') {
						app.sourceURLField.setDisabled(false);
						app.sourceURLField.setVisible(true);
					} else {
						app.sourceURLField.setDisabled(true);
						app.sourceURLField.setVisible(false);
					}
				}
			}
		});

		//Input field for Source URL, made for creating new nodes window
		app.sourceURLField = Ext.create('Ext.form.field.Text', {
				fieldLabel: 'Source URL',
				name: 'sourceURL',
				allowBlank: false,
				disabled: true,
				hidden: true
		});

		//Input field for Icon URL, made for creating new nodes window
		app.iconURLField = Ext.create('Ext.form.field.Text', {
				fieldLabel: 'Icon URL',
				name: 'iconURL',
				value: 'https://cdn0.iconfinder.com/data/icons/robots-expression/512/Robot_18-24.png',
				allowBlank: false
		});

        //Ext js store for saving the templates and connecting to combo box
        app.savedTempletesStore = Ext.create('Ext.data.Store', {
            fields: ['value', 'name'],
            data: storage.lastRevision.templetes
        });

        
		//Combo box for showing the templates
		app.savedTempletes = Ext.create('Ext.form.ComboBox', {
			fieldLabel: 'Saved Templates',
			editable: false,
			store: app.savedTempletesStore,
			renderTo: 'savedTemplates',
			queryMode: 'local',
			displayField: 'name',
			labelWidth: 150,
			valueField: 'value',
			listeners: {
				select: function( combo, record, eOpts ) {
					var nodes = Ext.decode(record.get('value'));
					for (var i = 0; i < nodes.length; i++) {
						app.diagramBuilder.addField(nodes[i]);
					}
				}
			}
		});

		//Form panel for inputing new node information
		app.addCustomNodeForm = Ext.create('Ext.form.Panel', {
			border: false,
			defaultType: 'textfield',
			items: [app.nodeTypesCombo, {
				fieldLabel: 'Node Name',
				name: 'nodeName',
				allowBlank: false
			}, app.iconURLField,
			app.sourceURLField]
		});

		//A window for adding embding the form panel for creating new node
		app.addCustomNodeWindow = Ext.create('widget.window', {
			title: 'Add new node',
			closable: true,
			closeAction: 'hide',
			width: 280,
			buttons: [{
				text: 'Submit',
				formBind: true,
				handler: function() {
					//get the nodes designed inside the diagram and check if it is not empty
					//if valid save to the database
					var diagramNodes = diagram.toJSON();
					diagramNodes = diagramNodes.nodes ? diagramNodes.nodes : [];
					var values = app.addCustomNodeForm.getValues();
					if (((diagramNodes.length > 0 && values['nodeType'] == 'subflow') || values['nodeType'] == 'source') && app.addCustomNodeForm.isValid()) {
						values['id'] = values['nodeName'].replace(/\s/g, '') + Math.floor((Math.random() * 10000000) + 1);
						values['nodes'] = diagramNodes;
						app.dynamicNodes.push(values);
						storage.lastRevision.info = app.dynamicNodes;
						//Save the data to the database
						storage.db.put(storage.lastRevision).then(
							function (response) {
								location.reload();
							}).catch(function (err) {
								location.reload();
							}
						);
						//app.addDynamicField(Y, values);
					} else {
						app.alertMSG('Please configure the Diagram first');
					}
				}
			}],
			height: 200,
			items: [app.addCustomNodeForm]
		});

		//add click listener for the custom node to show new node creation window
		app.customNode = Ext.get('availableFields_field_customNode');
		app.diagram = document.getElementsByClassName("property-builder-canvas")[0];
		if (app.diagram == null) {
			alert('Check the code inside application.js app.diagram = document.getElementsByClassName("property-builder-canvas")[0];, app.diagram must not be null');
		} else {
			app.diagram.firstChild.style.overflow = 'hidden';
		}
		app.customNode.addListener('click', 
			function() {
				app.addCustomNodeWindow.show();
			}
		);

		//The action definition of contex menu for removing the node on right click
		app.removeNodeContextAction = Ext.create('Ext.Action', {
			text: 'Remove',
			icon: 'https://cdn3.iconfinder.com/data/icons/sympletts-free-sampler/128/circle-close-20.png',
			handler: function(widget, event) {
				//get the node inside the panels ad remove the choosen one
				app.removeNodeContextMenu.nodeToRemoveId;
				for (var i = 0; i < app.dynamicNodes.length; i++) {
					var node = app.dynamicNodes[i];
					if (node['id'] == app.removeNodeContextMenu.nodeToRemoveId) {
						app.dynamicNodes.splice(i, 1);
						break;
					}
				}
				storage.lastRevision.info = app.dynamicNodes;
				storage.db.put(storage.lastRevision).then(
					function (response) {
						location.reload();
					}).catch(function (err) {
						location.reload();
					}
				);
				return false;
			}
		});

		//The context menu for the right click over the dynamic nodes
		app.removeNodeContextMenu = Ext.create('Ext.menu.Menu', {
			items: [
				app.removeNodeContextAction
			]
		});

		if (app.dynamicNodes) {
			//loop through dynamic nodes
			for (var i = 0; i < app.dynamicNodes.length; i++) {
					var strTable="<div><table border=1>";
					var node = app.dynamicNodes[i];
					//debugger;
					//check for subflows
					if (node.nodeType == 'subflow') {
						//table of the tooltip
						strTable=strTable + "<tr>Sub Flow Information:</tr>";
						for (var strNode in node){
									if(strNode!='nodes'){
									strTable=strTable+"<tr bgcolor='#81DAF5'><td>" + strNode +":</td><td>"+ node[strNode]+"</td></tr>";
								}
								//loop through the sub nodes
								else{
									for(var j=0 ; j<node.nodes.length;j++){
										var objNodes=node.nodes[j];
										var strNodesName, strNodesValue ;
										for(strNodesName in objNodes){
											//check connections or transitions
											if(strNodesName=='transitions'){
												var objTrans=node.nodes[j].transitions;
												var objTransNodes;
												strTable=strTable+"<tr bgcolor='#F6E3CE'><td >" + strNodesName +":</td><td>";
												//loop through the connections of the node
												for(var k=0 ; k<objTrans.length; k++){
													objTransNodes=objTrans[k];
													var strTransName, strTransValue ;
													for(strTransName in objTransNodes){
														if(strTransName=='target'){
															strTransValue = objTransNodes[strTransName] ;
															strTable=strTable + strTransValue+"</td></tr>";
														}
													}
													strTable=strTable +"</td></tr>";
												}
											}
											else{
												strNodesValue = objNodes[strNodesName] ;
												strTable=strTable+"<tr bgcolor='#F6E3CE'><td >" + strNodesName +":</td><td>"+ strNodesValue+"</td></tr>";
											}
										}
									strTable=strTable+"<tr bgcolor='#FE2E2E'><td>  </td><td>  </td></tr>";
									}
								}
						}					
						
						strTable=strTable+"<img src='"+node.iconURL+"'></table></div>";
						strTable=strTable+"</table></div>";	

                        /*
                        var tooltip = '<div>blub</div>'
						//the tip
						var tip = Ext.create('Ext.tip.ToolTip', {
						target: ('availableFields_field_' + node.id),
						dismissDelay: 0,
						html:tooltip
						});
                        */
                        
                        /*YUI().use('overlay', 'event', 'widget-anim', function (Y) {
                            Y.one('#tooltip').hide();
                        });*/
                        
                        Y.one('#availableFields_field_' + node.id).on('mouseenter', function(event){
                            var id = event.target._node.id;
                            YUI().use('overlay', 'event', 'widget-anim', function (Y) {
                                var targetNode;
                                for (var i = 0; i < app.dynamicNodes.length; i++) {
                                    if (id.indexOf(app.dynamicNodes[i].id) != -1) {
                                        targetNode = app.dynamicNodes[i];
                                    }
                                }
                                
                                var fields = new Array();
                                
                                for (var j = 0; j < targetNode.nodes.length; j++) {
                                    var currNode = targetNode.nodes[j];
                                    var jsonNode = JSON.parse('{}');
                                    jsonNode.name = currNode.name;
                                    jsonNode.type = currNode.type;
                                    jsonNode.xy = currNode.xy;
                                    fields.push(jsonNode);
                                }
                                
                                //document.getElementById('tooltip').innerHTML = event.target._node.id;
                                
                                // name: 'StartNode',
                                // type: 'start',
                                // xy: [10, 10]

                                var moreFields = [{
                                    iconClass: 'diagram-node-start-icon',
                                    label: 'Start',
                                    type: 'start'
                                }, {
                                    iconClass: 'diagram-node-end-icon',
                                    label: 'End',
                                    type: 'end'
                                }, {
                                    iconClass: 'diagram-node-merge-icon',
                                    label: 'Merge',
                                    type: 'merge'
                                },{
                                    iconClass: 'diagram-node-analytics-icon',
                                    label: 'Analytics',
                                    type: 'analytics'
                                },{
                                    iconClass: 'diagram-node-filter-icon',
                                    label: 'Filter',
                                    type: 'filter'
                                }, {
                                    iconClass: 'diagram-node-dataSource_twitter-icon',
                                    label: 'Twitter',
                                    type: 'dataSource_twitter'
                                }, {
                                    iconClass: 'diagram-node-dataSource_NYT-icon',
                                    label: 'NYT',
                                    type: 'dataSource_NYT'
                                }, {
                                    iconClass: 'diagram-node-dataSource_googleplus-icon',
                                    label: 'Google+',
                                    type: 'dataSource_googleplus'
                                }, {
                                    iconClass: 'diagram-node-dataSource_facebook-icon',
                                    label: 'Facebook',
                                    type: 'dataSource_facebook'
                                }, {
                                    id: 'customNode',
                                    iconClass: 'diagram-node-customNode-icon',
                                    label: 'Custom',
                                    type: 'customNode'
                                }];
                                
                                
                                var availableFields = dnm.createAvailableFields(Y);
                                for (var k = 0; k < moreFields.length; k++) {
                                    availableFields.push(moreFields[k]);
                                }
                                
                                //alert(availableFields);
                                
                                YUI().use(
                                  'aui-diagram-builder',
                                  function(Y) {
                                    new Y.DiagramBuilder(
                                      {
                                        availableFields: availableFields,
                                        boundingBox: '#tooltipContainer',
                                        visible: true,
                                        fields: fields,
                                        srcNode: '#tooltip'
                                      }
                                    ).render()
                                  }
                                );
                                
                                Y.one('#tooltip').show();
                            });
                        });
                        
                        Y.one('#availableFields_field_' + node.id).on('mouseleave', function(event){
                            YUI().use('overlay', 'event', 'widget-anim', function (Y) {
                                Y.one('#tooltip').hide();
                            });
                        });
					}
				
				var nodeElement = Ext.get('availableFields_field_' + node['id']);
				nodeElement.addListener('click',
					function(e, t, eOpts) {
						/*var dom = e.target.parentElement;
						var id = dom.id.substring(22, dom.id.length);  //debugger;
						var nodes = app.getDynamicNodeById(id).nodes;
						for (var i = 0; i < nodes.length; i++) {
							app.diagramBuilder.addField(nodes[i]);
						}*/
					}
				);
				//Add the show context meu listener to the node
				nodeElement.dom.childNodes[0].setAttribute('name', (node['id'] + ''));
				nodeElement.on("contextmenu", function(event, element) {
					event.stopEvent();
					var id = element.childNodes[0].getAttribute('name');
					app.removeNodeContextMenu.nodeToRemoveId = id; 
					app.removeNodeContextMenu.showAt(event.getXY());
					return false;
				});
			}
		}

		//The grid panel for showing the patterns
		//Ad corespondig field definitions
		app.patternGridPanel = Ext.create('Ext.grid.Panel', {
			title: 'Patterns',
			store: Ext.create('Ext.data.Store', {
				fields:['name', 'id'],
				data:{'items':[
					{ 'name': 'Time-Critical', 'description':'Description goes here', "id":"timeCritical"},
					{ 'name': 'Robust', 'description':'Robust execution engine', "id":"robust"},
					{ 'name': 'Secure (tbd)', 'description':'Description goes here', "id":"tradeOff"},
					{ 'name': 'Big Data (tbd)', 'description':'Description goes here', "id":"bigData"}
				]},
				proxy: {
					type: 'memory',
					reader: {
						type: 'json',
						root: 'items'
					}
				}
			}),
			listeners: {
				select: function(grid, record, index, eOpts) {
					//on select show the cresponding patterns
					var patternId = record.get('id');
					Ext.Ajax.request({
						url: ('patterns/' + patternId + '.html'),
						success: function(response) {
							app.patternDetailPanel.update(response.responseText);
						},
						failure: function(response) {
							app.patternDetailPanel.update('The selected pattern dows not have a detailed description');
						}
					});
				}
			},
			columns: [
				{ text: 'Name',  dataIndex: 'name' },
				{ text: 'Description',  dataIndex: 'description', flex: 1}
			]
		});
		//The detail panel for patterns to show information inside html files
		app.patternDetailPanel = Ext.create('Ext.panel.Panel', {
			region: 'center',
			title: 'Pattern Details',
			html: 'Please select a pattern from the left panel',
			split: true,
			autoScroll: true
		});
		//view port to nest patern details panel and left grid panel
		app.patternSelectionViewPort = Ext.create('Ext.panel.Panel', {
			layout: 'border',
			items: [{
				region: 'west',
				collapsible: true,
				title: 'Navigation',
				width: 250,
				items: app.patternGridPanel
				// could use a TreePanel or AccordionLayout for navigational items
			}, app.patternDetailPanel]
		});
		//Window to put the pattern selection related viewport iside and show it
		app.patternSelectionWindow = Ext.create('Ext.window.Window', {
			title: 'Pattern Selection',
			height: 530,
			width: 800,
			layout: 'fit',
			items: app.patternSelectionViewPort
		});
		//o click listener for showing the pattern selection panel
		Y.one('#patterSelection').on(
            'click',
            function() {
				app.patternSelectionWindow.show();
            }
        );
		//onclick listener for removing the choosen templete from combo box
		Y.one('#removeTempleteButton').on(
            'click', 
            function() {
				var selection = app.savedTempletes.getSelection();
				if (selection != null) {
					//remove the selected templete and update the database
					app.savedTempletesStore.remove(selection);
					app.savedTempletes.setSelection(null);
					storage.lastRevision.templetes = Ext.pluck(app.savedTempletesStore.data.items, 'data');
					storage.db.put(storage.lastRevision).then(
						function (response) {
							app.savedTempletesStore.add({
								name: text,
								id: id,
								value: diagramNodes
							});
						}).catch(function (err) {
							
						}
					);
				}
			}
		);
		//on click listener for saving the templete
		Y.one('#saveTempleteButton').on(
            'click', 
            function() {
				//get the nodes over the diagram and save inside the templetes store
				var diagramNodes = diagram.toJSON();
				diagramNodes = diagramNodes.nodes ? diagramNodes.nodes : [];
				if (diagramNodes.length > 0) {
					Ext.Msg.prompt('Templete Name', 'Please enter name for templete:', function(btn, text){
						if (btn == 'ok' && text != ''){
							if (storage.lastRevision.templetes == null) {
								storage.lastRevision.templetes = [];
							}
							diagramNodes = Ext.encode(diagramNodes);
							var id = (new Date()).getTime(); 
							storage.lastRevision.templetes.push({
								name: text,
								id: id,
								value: diagramNodes
							});
							//update the innformation inside the database
							storage.db.put(storage.lastRevision).then(
								function (response) {
									app.savedTempletesStore.add({
										name: text,
										id: id,
										value: diagramNodes
									});
								}).catch(function (err) {
									
								}
							);
						}
					});
				} else {
					app.alertMSG('Please configure the Diagram first');
				}
            }
        );
		
		//On click listener for executing data mashup scenario
        Y.one('#postButton').on(
            'click', 
            function() {
				//get the query values from the database
				var values = diagram.toJSON();
                alert(JSON.stringify(diagram.toJSON()));
				var inQuery = '';
				var inQuery1 = '';
				if (values.nodes) {
					var nodes = values.nodes;
					for (var i = 0; i < nodes.length;i++) {
						var node = nodes[i];
						if (node.type == 'dataSource_googleplus') {
							inQuery = node.dataSource_googleplusKey;
						}
						if (node.type == 'dataSource_facebook') {
							inQuery1 = node.dataSource_facebookKey;
						}
					}
				}
				//Construct and get the mock data from server
				var url = '/Data_Mashup/DataMock?inQuery=' + (inQuery + '&inQuery1=' + inQuery1);
				Ext.create('Ext.window.Window', {
					title: 'Result',
					height: 400,
					width: 600,
					layout: 'fit',
					items: Ext.create('Ext.grid.Panel', {
						 store: Ext.create('Ext.data.Store', {
							fields: ['id', 'lastName', 'firstName', 'link'],
							autoLoad: true,
							proxy: {
								type: 'ajax',
								url: url,
								reader: {
									type: 'json'
								}
							 },
							autoLoad: true
						 }),
						columns: [
							{ text: 'ID',  dataIndex: 'id' },
							{ text: 'Last Name', dataIndex: 'lastName'},
							{ text: 'First Name', dataIndex: 'firstName' },
							{ text: 'Link', dataIndex: 'link', flex: 1, 
								renderer: function(value, metaData, record, row, col, store, gridView) {
									return '<a href="' + value + '" target="_blank"><img border="0" alt="W3Schools" src="' + ((value.indexOf('google') > 0)? 'https://cdn3.iconfinder.com/data/icons/free-social-icons/67/google_circle_color-24.png': 'https://cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_circle_color-24.png') + '" width="24" height="24"></a>';
								}
							}
						]
					})
				}).show();
				/*
				document.getElementById("alertDiv").hidden = true;
				document.getElementById("alertDiv2").hidden = true;

            	jsonPostString = JSON.stringify(diagram.toJSON());
            	var dropDownMenu = document.getElementById("selectPattern");
            	var dropDownLength = dropDownMenu.length;
            	var selectedPattern = "";

            	for (var i = 0; i < dropDownLength; i++) {
            		if (dropDownMenu[i].selected == true) {
            			selectedPattern = dropDownMenu[i].value;
            		}
            	}

                Y.io.request(
                    'http://localhost:8080/Data_Mashup/DataMashup',
                    {
                        method: 'POST',
                        data: {flow: jsonPostString, pattern: selectedPattern},
                        on: {
                            success: function(msg) {
                                var data = this.get('responseData');
                                if(data.indexOf("timeCritical" != -1)) {
                                	//<div class="alert alert-success">
                                	//  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                	//  <strong>Success!</strong> Indicates a successful or positive action.
                                	//</div>
									document.getElementById("alertDiv").hidden = false;
                                } else if (data.indexOf("robust" != -1)) {
									document.getElementById("alertDiv2").hidden = false;
                                }
                            },
                            failure: function() {
                                alert('An error ocurred, please check your model.');
                            }
                        }
                    }
                );*/
            }
        );
	},
	//returns a dynamic node by ID
	getDynamicNodeById: function(id) {
		for (var i =0; i < app.dynamicNodes.length; i++) {
			if (id = app.dynamicNodes[i]['id']) {
				return app.dynamicNodes[i];
			}
		}
	},
	//Show alert message with the content and type of alert: Info, Error etc...
	alertMSG: function(content, type) {
		if (!type) {
			type = 'alert-warning';
		}
		YUI().use(
		  'aui-alert',
		  function(Y) {
			new Y.Alert(
			  {
				animated: true,
				bodyContent: content,
				boundingBox: '#myAlert',
				closeable: true,
				cssClass: type,
				destroyOnHide: false,
				duration: 1,
				render: true
			  }
			);
		  }
		);
	}
};
var app = application;
//Initialize the application and prepare for the luncher
YUI().use('aui-io-request', 'aui-diagram-builder', 'aui-button', 'aui-form-builder', 'aui-modal', storage.initializeData);