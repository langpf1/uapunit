var PluginService =
{
	getIdFromOptions: function(options)
		{
		if(typeof options == "undefined" || !options){
			return null;
		}
		return options.id;
		},
	setPropertyValue: function(propertyId,value)
	   {
			var jsonStr = JSON.stringify(value);
			document.getElementById(propertyId).value=jsonStr;
	   },
	   setSWTBrowserStatus:function(property){
		   window.status = getEvent(property);
		   function getEvent(source){
			   return "event:{source:"+property+","+"type:opentype}";
		   }
	   },
	isSWTBrowserDeal:function(options){
		   return (mxClient.IS_SWTBROWSER && typeof options.id != 'undefined' && options.id);
	   },
	openFile: function()
	   {
	        var xmlDoc;
	        if (window.ActiveXObject) {
	             xmlDoc = new ActiveXObject('Microsoft.XMLDOM');//IEä¯ÀÀÆ÷
	             xmlDoc.async = false;
	             xmlDoc.load("stencils/boundary1.xml");
	         }
	        else if(mxClient.IS_MT){
	            xmlDoc = document.implementation.createDocument('', '', null);
	            xmlDoc.async = false;
	            xmlDoc.load("stencils/boundary1.xml");
	            //xmlDoc=new XMLHttpRequest();
	            //xmlDoc.open("GET","stencils/general.xml",false);
	            //xmlDoc.send(null);
	            var xml=(new XMLSerializer()).serializeToString(xmlDoc);
	            var editorUi = new EditorUi(new Editor(),null,"editorStyle");
	            var doc = mxUtils.parseXml(xml); 
				editorUi.editor.setGraphXml(doc.documentElement);
				editorUi.editor.modified = false;
				editorUi.editor.undoManager.clear();
	         }
	        //var xmlFile=url.substring(url.indexOf("?")+1,url.legnth);
	   }
};

