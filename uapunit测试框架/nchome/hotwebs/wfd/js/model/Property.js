/**
 * Class: Property
 */
function Property(name, group, isReadOnly, editorType, editorOptions, displayName, hints)
{
	this.name = name;
	this.displayName = displayName;
	this.group = group;
	this.isReadOnly = isReadOnly;
	this.editorType = editorType;
	this.editorOptions = editorOptions;
	this.hints = hints;
};

Property.prototype.constructor = Property;

Property.prototype = {
	get_name : Property$get_name,
	get_displayName : Property$get_displayName,
 	get_group : Property$get_group,
 	get_isReadOnly : Property$get_isReadOnly,
 	get_editorType : Property$get_editorType,
 	get_editorOptions : Property$get_editorOptions,
 	get_hints : Property$get_hints
};

function Property$get_name() {
    /// <value type="Property" locid="P:J#Property.name"></value>
    return this.name;
};

function Property$get_displayName() {
    /// <value type="Property" locid="P:J#Property.name"></value>
    return this.displayName;
};

function Property$get_group() {
    /// <value type="Property" locid="P:J#Property.group"></value>
    return this.group;
};

function Property$get_isReadOnly() {
    /// <value type="Property" locid="P:J#Property.isReadOnly"></value>
    return this.isReadOnly;
};

function Property$get_editorType() {
    /// <value type="Property" locid="P:J#Property.editorType"></value>
    return this.editorType;
};

function Property$get_editorOptions() {
    /// <value type="Property" locid="P:J#Property.editorOptions"></value>
    return this.editorOptions;
};

function Property$get_hints() {
    /// <value type="Property" locid="P:J#Property.hints"></value>
    return this.hints;
};

$.extend($.fn.datagrid.defaults.editors, { 
	refbox: { 
			init: function(container, options){
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container);
			var inputId = PluginService.getIdFromOptions(options);
			var innerID = (typeof inputId != 'undefined' && inputId)? ('id="'+inputId+'"'):'';
			var inputInner='<input class="ref-text" style="width: 85px;" '+ innerID +'autocomplete="off" value=""/>';
			var input = $(inputInner).appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				if(PluginService.isSWTBrowserDeal(options)){
					PluginService.setSWTBrowserStatus(options.id);		
				}else{
					var returnValue = showModalDialog(options.url, input.val(), options.features);
					if(returnValue!=undefined)
					{
						input.val(JSON.stringify(returnValue));
					}
				}
			});
			return span; 
		}, 
		getValue: function(target){
			var value = $(target)[0].children[0].value;
			var pattern=new RegExp("\[{((n*:n*),)*(n*:n*)}\]");
			if(value != undefined && value!=""&&pattern.test(value)){
				return JSON.parse(value);
			}
			return value; 
		}, 
		setValue: function(target, value){
			if(value != undefined && value!=""){
				$(target)[0].children[0].value = JSON.stringify(value);
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});
$.extend($.fn.datagrid.defaults.editors, { 
	refcolumnbox: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var returnValue = showModalDialog(options.url, input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(JSON.stringify(returnValue));
				}
			});
			return span; 
		}, 
		getValue: function(target){
			var value = $(target)[0].children[0].value;
			if(value != undefined && value!=""){
				return JSON.parse(value);
			}
			return value; 
		}, 
		setValue: function(target, value){
			if(value != undefined && value!=""){
				$(target)[0].children[0].value = JSON.stringify(value);
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

$.extend($.fn.datagrid.defaults.editors, { 
	refcolumn: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var selectkind = new Object();
				selectkind.name=options.kind;
				var returnValue = showModalDialog(options.url,selectkind,input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(returnValue.name);
					var gridid = options.gridid;
					var selectRow = $('#'+gridid).datagrid('getSelected');
					var selectIndex = $('#'+gridid).datagrid('getRowIndex',selectRow);
					
					var keyEditor = $('#'+gridid).datagrid('getEditor',{index:selectIndex,field:options.keycolumn});
					if(keyEditor != null && keyEditor != undefined)
						keyEditor.target.val(returnValue.id);
						
					var codeEditor = $('#'+gridid).datagrid('getEditor',{index:selectIndex,field:options.codecolumn});
					if(codeEditor != null && codeEditor != undefined)
						codeEditor.target.val(returnValue.code);
				}
			});
			return span; 
		}, 
		getValue: function(target){ 
			return $(target)[0].children[0].value; 
		}, 
		setValue: function(target, value){
			if(value != undefined)
			{
				$(target)[0].children[0].value = value;
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

$.extend($.fn.datagrid.defaults.editors, { 
	expbox: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var returnValue = showModalDialog(options.url, input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(returnValue);
				}
			});
			return span; 
		}, 
		getValue: function(target){
			return $(target)[0].children[0].value;
		}, 
		setValue: function(target, value){
			if(value != undefined){
				$(target)[0].children[0].value = value;
			}
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

function EditorType(){};
EditorType.prototype.constructor = EditorType;
EditorType.text = "text";
EditorType.textarea = "textarea";
EditorType.numberbox = "numberbox";
EditorType.datebox = "datebox";
EditorType.validatebox = "validatebox";
EditorType.combobox = "combobox";
EditorType.combotree = "combotree";
EditorType.checkbox = "checkbox";
EditorType.refbox = "refbox";
EditorType.refcolumn = "refcolumn";
EditorType.expbox = "expbox";
EditorType.refcolumnbox = "refcolumnbox";

