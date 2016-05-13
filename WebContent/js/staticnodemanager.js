var staticnodemanager = {
	initialize: function(Y) {
		//Initialize the Merge node to Aloye UI 
		Y.DiagramNodeMerge = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'Merge'
                },
                criteria: {
                    validator: Y.Lang.isString,
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeJoin,
            prototype: {
				//CONTROLS_TEMPLATE: '<div id="ddd" class="' + CSS_DB_CONTROLS + '"></div>',
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('criteria');
                },
                getPropertyModel: function () {
                    var instance = this;
                    var model = Y.DiagramNodeMerge.superclass.getPropertyModel.apply(instance, arguments);
                        model.splice(0, 1);
                        model.push({ 
                            attributeName: 'criteria', 
                            name: 'Criteria'
                        });
                    return model;
                },
            }
        });

		//Initialize the NYT node to Aloye UI 
        Y.DiagramNodedataSource_NYT = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'dataSource_NYT'
                },
                dataSource_NYTName: {
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeTask,
            prototype: {
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('dataSource_NYTName');
                },
                getPropertyModel: function () {
                    var instance = this;

                    var model = Y.DiagramNodedataSource_NYT.superclass.getPropertyModel.apply(instance, arguments);

                   model.splice(0, 1);
                   model.push({ 
                       attributeName: 'dataSource_NYTName', 
                       name: 'Category',
                       editor:  new Y.DropDownCellEditor({
                           options: {
                               Sports: 'Sports',
                               Business: 'Business',
                               Technology: 'Technology',
                               Science: 'Science',
                               Health: 'Health',
                               World: 'World'
                           }
                       }) 
                   });
                    return model;
                }
            }
        });

	    //Initialize the Twitter node to Aloye UI 
        Y.DiagramNodeDataSource_twitter = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'dataSource_twitter'
                },
                dataSource_twitterHashtag: {
                    validator: Y.Lang.isString,
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeTask,
            prototype: {
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('dataSource_twitterHashtag');
                },
                getPropertyModel: function () {
                    var instance = this;
                    var model = Y.DiagramNodeDataSource_twitter.superclass.getPropertyModel.apply(instance, arguments);
                        model.splice(0, 1);
                        model.push({
                            attributeName: 'dataSource_twitterHashtag', 
                            name: 'Keywords',
                        });
                    return model;
                }
            }
        });

		//Initialize the Facebook node to Aloye UI 
		Y.DiagramNodeDataSource_facebook = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'dataSource_facebook'
                },
                dataSource_facebookKey: {
                    validator: Y.Lang.isString,
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeTask,
            prototype: {
                initializer: function() {
                    var instance = this;
					this.SERIALIZABLE_ATTRS.push('dataSource_facebookKey');
                },
				getPropertyModel: function () {
					var instance = this;
					var model = Y.DiagramNodeDataSource_facebook.superclass.getPropertyModel.apply(instance, arguments);
						model.splice(0, 1);
						model.push({
							attributeName: 'dataSource_facebookKey',
							name: 'Keywords'
						});
					return model;
				}
            }
        });

		//Initialize the Google plus node to Aloye UI 
		Y.DiagramNodeDataSource_googleplus = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'dataSource_googleplus'
                },
                dataSource_googleplusKey: {
                    validator: Y.Lang.isString,
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeTask,
            prototype: {
                initializer: function() {
                    var instance = this;
				    this.SERIALIZABLE_ATTRS.push('dataSource_googleplusKey');
                },
				getPropertyModel: function () {
					var instance = this;
					var model = Y.DiagramNodeDataSource_googleplus.superclass.getPropertyModel.apply(instance, arguments);
						model.splice(0, 1);
						model.push({
							attributeName: 'dataSource_googleplusKey',
							name: 'Keywords'
						});
					return model;
				}
            }
        });

		//Initialize the Filter node to Aloye UI 		
        Y.DiagramNodeFilter = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'filter'
                },
                filtertype: {
                    value: ''
                },
                filter_criteria: {
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeFork,
            prototype: {
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('filtertype');
                    this.SERIALIZABLE_ATTRS.push('filter_criteria');
                },
                getPropertyModel: function () {
                    var instance = this;
                    var model = Y.DiagramNodeFilter.superclass.getPropertyModel.apply(instance, arguments);
                    model.splice(0, 1);
                    model.push({
                        attributeName: 'filtertype',
                        name: 'Filter Type',
                        editor: new Y.DropDownCellEditor({
                            options: {
                            	NYT: 'NYT Filter',
                                tbd: 'tdb'
                            }
                        })
                    });
                    model.push({
                        attributeName: 'filter_criteria', 
                        name: 'Keywords',
                        editor: new Y.TextCellEditor  
                    });

                    return model;
                }
            }
        });

		//Initialize the Analytics node to Aloye UI 
        Y.DiagramNodeAnalytics = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'analytics'
                },
                filtertype: {
                    value: ''
                },
                filter_criteria: {
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeFork,
            prototype: {
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('analyticstype');
                    this.SERIALIZABLE_ATTRS.push('analytics_properties');
                },
                getPropertyModel: function () {
                    var instance = this;
                    var model = Y.DiagramNodeFilter.superclass.getPropertyModel.apply(instance, arguments);
                    model.splice(0, 1);
                    model.push({
                        attributeName: 'analyticstype',
                        name: 'Analytics Type',
                        editor: new Y.DropDownCellEditor({
                            options: {
                                Sentiment: 'Sentiment Analysis',
                                NE: 'Named Entity Recognition'
                            }
                        })
                    });
                    model.push({ 
                        attributeName: 'analytics_properties', 
                        name: 'Properties',
                        editor: new Y.TextCellEditor  
                    });
					return model;
                }
            }
        });
		Y.DiagramNodeCustom = Y.Component.create({
            NAME: 'diagram-node',
            ATTRS: {
                type: {
                    value: 'customNode'
                },
                criteria: {
                    validator: Y.Lang.isString,
                    value: ''
                }
            },
            EXTENDS: Y.DiagramNodeTask,
            prototype: {
                initializer: function() {
                    var instance = this;
                    this.SERIALIZABLE_ATTRS.push('criteria');
					this.on("click", app.addNewNode, this);
					
                }
            }
        });
		//provide the appropriate object with keyword mapping
        Y.DiagramBuilder.types['customNode'] = Y.DiagramNodeCustom;
        Y.DiagramBuilder.types['analytics'] = Y.DiagramNodeAnalytics;
        Y.DiagramBuilder.types['merge'] = Y.DiagramNodeMerge;
        Y.DiagramBuilder.types['dataSource_NYT'] = Y.DiagramNodedataSource_NYT;
        Y.DiagramBuilder.types['dataSource_twitter'] = Y.DiagramNodeDataSource_twitter;
		Y.DiagramBuilder.types['dataSource_googleplus'] = Y.DiagramNodeDataSource_googleplus;
		Y.DiagramBuilder.types['dataSource_facebook'] = Y.DiagramNodeDataSource_facebook;
        Y.DiagramBuilder.types['filter'] = Y.DiagramNodeFilter;
		
		//return the initialized static nodes to the application, to be created
		return [
            {
                id: 'customNode',
                iconClass: 'diagram-node-customNode-icon',
                label: 'Custom',
                type: 'customNode'
            }, {
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
            }
        ];
	}
};
var snm = staticnodemanager;