//add by kongml
define(['underscore', 'backbone'], function(_, Backbone) {
	var Customer = Backbone.Model.extend({
		defaults: {
			apply_org: '000000009',
			billcode: 'KH201211220049',
			billtype:'10kH',
			applicant: null,
			apply_time: '2013-4-1',
			apply_state: '自由',
			approval: null,
			//客户信息
			org: '用友',
			code: '001',
			name: null,
			simple_code: null,
			customer_grp: null,
			region_grp: null,
			//客户状态
			customer_state: '核准',
			ispartner: false,
			owner: null,
			money: 0,
			tax: null,
			addr: null,
			actioncode:null
		},
		setActionCode:function(action){
			this.set({ actioncode:action}); 
		},
		url:'../wfd/runtime/wfform/bill/',
		initialize: function() {
			console.log("Model Customer has initialized");
		}
	});
	return Customer;
});