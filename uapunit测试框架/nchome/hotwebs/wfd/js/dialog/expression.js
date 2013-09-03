
/*
	Expression editor
*/
function ExprEditor(){
	this.buttonCols = 4;
	this.buttonRows = 7;
	this.initFunctionPage = false;
	this.objectType = "nc.vo.arap.receivable.ReceivableBillVO";
	this.editor = null;
	this.prompt = null;
	this.pos = new Object();
	this.url = "/wfd/wf";
	this.editingItemMgr = new EditingItemMgr();
	this.refNode = null;
}

ExprEditor.prototype.constructor = ExprEditor;

ExprEditor.prototype.onOK = function(){
	window.returnValue = this.editor.value;
	window.close();
}

ExprEditor.prototype.onCancel = function(){
	window.returnValue = undefined;
	window.close();
}

ExprEditor.prototype.onVerify = function (){
	var expression = this.editor.value;
	var encode = encodeURIComponent(expression);
	var req = new mxXmlRequest(this.url, "action=exprUtil&expression="+encode, null, false);
	req.send(null, null);
	var result = req.request.responseText;
	alert(result);
}

ExprEditor.prototype.onSelectAll = function(){
	var textRange = this.editor.createTextRange();
	textRange.moveStart("character", 0);
	textRange.moveEnd("character", this.editor.value.length);
	textRange.select();
}

ExprEditor.prototype.onClearAll = function(){
	this.editor.value = "";
	this.pos.start = this.pos.end = 0;
	this.editor.focus();
}

ExprEditor.prototype.onReference = function(){
	var returnValue = window.showModalDialog('/wfd/dialog/expression/commonReference.html',this.refNode,'dialogHeight:600px;dialogWidth:800px;center:yes');
	if (returnValue != undefined){
		document.getElementById("refEditor").value = returnValue.code;
	}
}

ExprEditor.prototype.onAddReference = function(){
	var metadataRoot = $('#metadataRoot'); 
	
	var node = metadataRoot.tree('getSelected');
	var parentNode = metadataRoot.tree('getParent',node.target);
	var path = node.attributes.name;
	var promptText = node.attributes.displayName;
	while(parentNode){
		path = parentNode.attributes.name + "." + path;
		promptText = parentNode.attributes.displayName + "." + promptText;
		parentNode = metadataRoot.tree('getParent',parentNode.target);
	}

	var leftValue = " " + node.attributes.id + " ";
	var leftText = node.text;
	var rightValue = document.getElementById("refEditor").value;
	var rightText = rightValue;
	
	this.insertString(new EditingItem(
		leftValue + " == " + rightValue, 
		leftText + " == " + rightText , false));
}

function LocalResources(){}
	   mxResources.loadDefaultBundle = false;
 	   mxResources.add(RESOURCE_BASE); 
 	   LocalResources.ok=mxResources.get("ok");      
	   LocalResources.cancel=mxResources.get("cancel");
	   LocalResources.verify=mxResources.get("verify");
	   LocalResources.selectAll=mxResources.get("selectAll");
	   LocalResources.clearAll=mxResources.get("clearAll");
	   LocalResources.referStyle=mxResources.get("referStyle");
	   LocalResources.organization=mxResources.get("organization");
	   LocalResources.addToCondition=mxResources.get("addToCondition");
	   LocalResources.title=mxResources.get("expressionEditor"); 
	   LocalResources.tabflowVariables=mxResources.get("tabflowVariables");
	   LocalResources.tabfunctions=mxResources.get("tabfunctions");
	   LocalResources.tabmetadata=mxResources.get("tabmetadata");
	   LocalResources.tabbizFunctions=mxResources.get("tabbizFunctions");
	   
ExprEditor.prototype.initialize = function(){
	
   	document.title= LocalResources.title;   
   // update the selected panel with new title and content
   var tab = $('#tabs').tabs('tabs')[0]  // get selected panel
   $('#tabs').tabs('update', {
	tab: tab,
	options: {
		title: LocalResources.tabflowVariables
	}
   });
   var tab = $('#tabs').tabs('tabs')[1]  // get selected panel
   $('#tabs').tabs('update', {
	tab: tab,
	options: {
		title: LocalResources.tabfunctions
	}
   });
   var tab = $('#tabs').tabs('tabs')[2]  // get selected panel
   $('#tabs').tabs('update', {
	tab: tab,
	options: {
		title: LocalResources.tabmetadata
	}
   });
   var tab = $('#tabs').tabs('tabs')[3]  // get selected panel
   $('#tabs').tabs('update', {
	tab: tab,
	options: {
		title: LocalResources.tabbizFunctions
	}
   });
    document.getElementById('label').innerHTML=LocalResources.referStyle
    document.getElementById('labelRefType').innerHTML=LocalResources.organization
    document.getElementById('refAdd2Editor').value=LocalResources.addToCondition
	document.getElementById('btnOK').innerHTML = "<SPAN class=l-btn-left><SPAN class=l-btn-text>"+LocalResources.ok+"</SPAN></SPAN>";//"确认";
	document.getElementById('btnCancel').innerHTML = "<SPAN class=l-btn-left><SPAN class=l-btn-text>"+LocalResources.cancel+"</SPAN></SPAN>";//"放弃";
	document.getElementById('btnVerify').innerHTML = "<SPAN class=l-btn-left><SPAN class=l-btn-text>"+LocalResources.verify+"</SPAN></SPAN>";//"校验";
	document.getElementById('btnSelectAll').innerHTML = "<SPAN class=l-btn-left><SPAN class=l-btn-text>"+LocalResources.selectAll+"</SPAN></SPAN>";//"全选";
	document.getElementById('btnClearAll').innerHTML = "<SPAN class=l-btn-left><SPAN class=l-btn-text>"+LocalResources.clearAll+"</SPAN></SPAN>";//"全清";

	this.editor = document.getElementById('expressionEditor');
	this.prompt = document.getElementById("expressionPrompt")

	this.rendarButtons();
	this.northResize();
   
	if (window.dialogArguments){
		this.editor.value = window.dialogArguments;
	}
   
	window.onunload=function(){
		if(!window.returnValue)
			window.returnValue = undefined;	
	}
	
	var parent = this;
	$('#tabs').tabs({
		onSelect:function(title, index){
			switch(index){
			case 0:
      			parent.initFlowVariables();			
				break;
			case 1:
				parent.initFunctions();
				break;
			case 2:
				parent.initTabMetadata();
				break;
			case 3:
				break;
			}
		}
	});
   
	$('#tabs').tabs('select',0);

	this.editor.onkeydown=function(){parent.savePos();}
	this.editor.onkeyup=function(){parent.savePos();}
	this.editor.onmousedown=function(){parent.savePos();}
	this.editor.onmouseup=function(){parent.savePos();}
	this.editor.onfocus=function(){parent.savePos();}
	
	this.prompt.onmousedown=null;	
	//get process info
	//	var process = mainWnd.editor.graph.model.root.children[0];
	//	objectType = process.objectType;
	//	debugger;
	//	alert(objectType);
}

ExprEditor.prototype.insertString = function(inputting){
	
	var startPos = this.pos.start;
	if (!startPos){
		this.pos.start = this.pos.end = startPos = 0;
	}
    var pre = this.editor.value.substr(0, startPos);
    var post = this.editor.value.substr(this.pos.end);
    this.editor.value = pre + inputting.expr + post;
    startPos += inputting.expr.length;
    
    //计算新的位置
    if(typeof(this.editor.selectionStart) == "number"){	//html5
    	this.editor.selectionStart = startPos;
    	this.editor.selectionEnd = startPos;
    }else{
		var range = this.editor.createTextRange();
        range.collapse(true);
		range.moveStart('character',startPos);
        range.moveEnd('character',0);
		range.select();
    }

    this.editingItemMgr.insertItem(inputting, "", this.pos.end, false);
    
    this.prompt.value = this.editingItemMgr.getDisplay();
    
	this.editor.focus();
}                                                                         

ExprEditor.prototype.savePos = function(){
	var textBox = this.editor;
	var start = 0, end = 0;
	if(typeof(textBox.selectionStart) == "number"){	//html5
		start = textBox.selectionStart;
		end = textBox.selectionEnd;
	}else if(document.selection){		//下面是IE(6.0)的方法，麻烦得很，还要计算上'\n'
		var range = document.selection.createRange();
		if(range.parentElement().id == textBox.id){
			// create a selection of the whole textarea
			var range_all = document.body.createTextRange();
			range_all.moveToElementText(textBox);
			//两个range，一个是已经选择的text(range)，一个是整个textarea(range_all)
			//range_all.compareEndPoints()比较两个端点，如果range_all比range更往左(further to the left)，则                
			//返回小于0的值，则range_all往右移一点，直到两个range的start相同。
			// calculate selection start point by moving beginning of range_all to beginning of range
			for (start=0; range_all.compareEndPoints("StartToStart", range) < 0; start++)
				range_all.moveStart('character', 1);
			// get number of line breaks from textarea start to selection start and add them to start
			// 计算一下n
			for (var i = 0; i <= start; i ++){
				if (textBox.value.charAt(i) == '\n')
					start++;
			}
			// create a selection of the whole textarea
			var range_all = document.body.createTextRange();
			range_all.moveToElementText(textBox);
			// calculate selection end point by moving beginning of range_all to end of range
			for (end = 0; range_all.compareEndPoints('StartToEnd', range) < 0; end ++)
				range_all.moveStart('character', 1);
				// get number of line breaks from textarea start to selection end and add them to end
			for (var i = 0; i <= end; i ++){
				if (textBox.value.charAt(i) == '\n')
					end ++;
			}
		}
	}
    this.pos.start = start;
    this.pos.end = end;
}

/*
      resize process
*/
ExprEditor.prototype.northResize = function(){
	//resizeEditorPnl();
	this.resizeExpressionPnl();
	this.resizeButtonsPnl();
}


ExprEditor.prototype.resizeEditorPnl = function(){
//	document.getElementById("editorContainer").style.height = 
//		document.getElementById("editorContainer").offsetHeight-document.getElementById("expressionEditor").offsetTop-4;
//	document.getElementById("expressionEditor").style.width = 
//		document.getElementById("editorContainer").offsetWidth-document.getElementById("expressionContainer").offsetLeft-4;
	this.resizeExpressionPnl();
}

/*
   input box resize
*/                                      
ExprEditor.prototype.resizeExpressionPnl = function(){
/*   document.getElementById("expressionEditor").style.height = 
      document.getElementById("expressionContainer").offsetHeight-document.getElementById("expressionEditor").offsetTop-4;
   document.getElementById("expressionEditor").style.width = 
      document.getElementById("expressionContainer").offsetWidth-document.getElementById("expressionContainer").offsetLeft-4;
*/
}

ExprEditor.prototype.resizeButtonsPnl = function(){
   var width = document.getElementById("buttonsContainer").offsetWidth - 4;
   var height = document.getElementById("buttonsContainer").offsetHeight - 16;
   var td = null;
   for (row = 0; row < this.buttonRows; row++)
      for(col = 0; col < this.buttonCols; col++){
         td = document.getElementById("cell"+row+col);
         td.width = (width / this.buttonCols);
         td.height = (height / this.buttonRows) - 3;
      }   
}

ExprEditor.prototype.initTabMetadata = function(){
	var ulRoot = document.getElementById('metadataRoot');
	if (ulRoot.innerHTML &&  ulRoot.innerHTML.length > 0)
		return;
	//var returnValue = mxUtils.load("/wfd/metadataservlet?action=init;type=10GY");
	//var returnValue = mxUtils.load("/wfd/metadataservlet","action=init;type=10GY");
	
	var req = new mxXmlRequest(this.url, "action=metadataUtil&function=loadProperty&type=" + this.objectType, null, false);
	req.send(null, null);
	if (req.request.status == 404){
		alert("不能得到元数据，请确认服务是否启动。");
		return;
	}
	var json = eval(req.request.responseText);
	if (json.error != undefined){
		alert(json.errorDesc);
		return;
	}
		
	var metadataRoot = $('#metadataRoot'); 
	metadataRoot.tree('loadData', json);

	var parent = this;
	//add event process
	metadataRoot.tree({
		onDblClick:function(node){
			var parentNode = metadataRoot.tree('getParent',node.target);
			var path = node.attributes.name;
			var promptText = node.attributes.displayName;
			while(parentNode){
				path = parentNode.attributes.name + "." + path;
				promptText = parentNode.attributes.displayName + "." + promptText;
				parentNode = metadataRoot.tree('getParent',parentNode.target);
			}
			parent.insertString(new EditingItem(" " + path + " ",promptText,false));
		}
	});
	metadataRoot.tree({
		onClick:function(node){
			var refClassName = node.attributes.className;
			var refLabel = document.getElementById("labelRefType");
			var refEditor = document.getElementById("refEditor");
			var refButton = document.getElementById("refButton"); 
			if (refClassName && refClassName != undefined){
				refLabel.innerHTML = node.attributes.displayName;
				refEditor.refClass = refClassName;
				refButton.parentElement.disabled = false;
				parent.refNode = node;
			}else{
				refLabel.innerHTML = "";
				refEditor.refClass = "";
				refButton.parentElement.disabled = true;
				parent.refNode = null;
			}
		}
	});	
	
	//add event process
	metadataRoot.tree({
		onExpand:function(node){
			var children = metadataRoot.tree('getChildren', node.target);
			if (children.length <= 1){
				metadataRoot.tree('remove', children[0].target);
				
				//var returnValue = mxUtils.load("/wfd/metadataservlet?action=load;type=" + node.attributes);
				var req =  new mxXmlRequest(parent.url,"action=metadataUtil&function=loadPropertySub&type=" + node.attributes.className,null,false);
				req.send(null,null);
				var data_json = req.request.responseText;
				metadataRoot.tree('append',{
					parent:node.target,
					data:eval(data_json)
				});
			}
		}
	});

}

/*
 * tab flow variables process 
 */
ExprEditor.prototype.initFlowVariables = function(){
	//process node
	var ulRoot = document.getElementById('flowVariablesRoot');
	if (ulRoot.innerHTML &&  ulRoot.innerHTML.length > 0)
		return;
		
	var flowVariablesRoot = $('#flowVariablesRoot');
	flowVariablesRoot.tree('append',{
		parent:'flowVariablesRoot',
		data:[
			{
				id:'processVariables',
				text:'流程变量',
				state:'closed',
				children:[{
					id:'processInitVariables',
					text:'初始化变量',
					attributes:{
						id:'processInitVariables',
						name:'processInitVariables'
					}
				}]
			},
			{
				id:'serviceVariables',
				text:'服务变量',
				state:'closed',
				children:[{
					id:'variables1',
					text:'服务返回值',
					attributes:{
						id:'variables1',
						name:'variables1'
					}
				}]
			},
			{
				id:'multiInstanceVariables',
				text:'多实例控制变量',
				state:'closed',
				children:[
					{
					id:'ncrInstanceof',
					text:'实例总数',
					attributes:{
						id:'ncrInstanceof',
						name:'ncrInstanceof'
					}
					},
					{
					id:'ncrCompleted',
					text:'实例完成数',
					attributes:{
						id:'ncrCompleted',
						name:'ncrCompleted'
					}
					
				}]
			}

		]
	});
	
	//service node
	
	//etc...
	
	//add event process
	var parent = this;
	flowVariablesRoot.tree({
		onDblClick:function(node){
			if(!(flowVariablesRoot.tree('isLeaf',node.target)))
				return;
			
			parent.insertString(new EditingItem(" " + node.attributes.id + " ",node.text,false));
		}
	});
}
/*
   input button process
*/
ExprEditor.prototype.tdMouseHover = function(){
   var eventElement = window.event.srcElement;
   eventElement.style.borderBottomColor="blue";
   eventElement.style.borderRightColor="blue";
}

ExprEditor.prototype.tdMouseOut = function(){
   var eventElement = window.event.srcElement;   
   eventElement.style.borderBottomColor="black";
   eventElement.style.borderRightColor="black";
}   

function isdigit(s){
	var r,re;
	re = /\d*/i; //\d表示数字,*表示匹配多个数字
	r = s.match(re);
	return (r==s)?1:0;
}

ExprEditor.prototype.tdOnClick = function(){
	var eventElement = window.event.srcElement;
	if (eventElement){
		var expr = eventElement.innerHTML;
		if (!isdigit(expr)){
			expr = " " + expr + " ";
		}
		this.insertString(new EditingItem(expr, expr, false));
	}
}

ExprEditor.prototype.rendarButtons = function(){
   var buttons = [
      "1","2","3","+",
      "4","5","6","-",
      "7","8","9","*",
      "0","(",")","/",
      "==","!=","$","#",
      ">","<",">=","<=",
      "like","in","between","conatins"
      ];
   var width = document.getElementById("buttonsContainer").offsetWidth - 4;
   var height = document.getElementById("buttonsContainer").offsetHeight - 16;
   var table = document.getElementById("buttons");
   var tbody = document.createElement("tbody");
   var tr = null, td = null;
   var parent = this;
   for(row = 0; row < this.buttonRows; row++){
      tr = document.createElement("tr");
      tr.style.height = (height / this.buttonRows);
      for(col = 0; col < this.buttonCols; col++){
         td = document.createElement("td");
         td.innerHTML = buttons[row * this.buttonCols + col];
         td.nodeValue = buttons[row * this.buttonCols + col];
         td.id = "cell" + row + col; 
         td.style.borderBottomStyle="solid";
         td.style.borderRightStyle="solid";
         td.style.borderBottomWidth="1px";
         td.style.borderRightWidth="1px";
         td.style.textAlign = "center";
         td.width = (width / this.buttonCols);
         td.height = (height / this.buttonRows) - 3;
         
         td.onmouseover = function(){parent.tdMouseHover();};
         td.onmouseout = function(){parent.tdMouseOut();};
         td.onclick = function(){parent.tdOnClick();};
         
         tr.appendChild(td);
      }
      tbody.appendChild(tr);
  }
  table.appendChild(tbody);
}   

/*
   rendar functions
*/
ExprEditor.prototype.initFunctions = function(){
	var req = new mxXmlRequest("functions.xml",null,"GET",false);
	req.send(null,null);
	var xmlDoc = req.getXml();
	var root = xmlDoc.documentElement;
	var funcGroup = root.firstChild;
	var groupID = null;
	var groupName = null;
	var functionRoot = $('#functionRoot');
	
	if (!(root && funcGroup)){
		alert("不能下载文件: functions.xml ");
   		return;
	}
	var ulRoot = document.getElementById('functionRoot');
	if (ulRoot.innerHTML &&  ulRoot.innerHTML.length > 0)
		return;

	var parent = this;
   //add function list
	while(funcGroup){
		if (!(funcGroup  instanceof Text)){
			groupID = funcGroup.getAttribute("funcname");
			groupName = funcGroup.getAttribute("name"); 
			functionRoot.tree('append',{
				parent:'functionRoot',
				data:[{
					'state':'closed',
					'id':groupID,
					'text':groupName+"(" + groupID + ")",
					'children':[{'text':'loading ...'}]
				}]
			});
		}
		funcGroup = funcGroup.nextSibling;
	}
	   
	//add event process
	functionRoot.tree({
		onDblClick:function(node){
			if(!(functionRoot.tree('isLeaf',node.target)))
				return;
			var prototype = node.attributes.prototype;
			var desc = node.attributes.desc;
			var inputValue = (prototype && prototype !="") ? prototype : desc ;
			parent.insertString(new EditingItem(" " + inputValue + " ", node.text, false));
		}
	});
	
	
	functionRoot.tree({
		onExpand:function(node){
			//var req = mxUtils.load("functions.xml");
			var req =  new mxXmlRequest("functions.xml",null,"GET",false);
			req.send(null,null);
			var xmlDoc = req.getXml();
			var root = xmlDoc.documentElement;
			var funcGroup = null;
			var func = null;
			var funcID = null;
			var funcName = null;
			var funcPrototype = null;
			var funcDesc = null;
		
			//判断节点是否已经加载过，加载过就不再加载
			var children = functionRoot.tree('getChildren', node.target);
			if (children.length > 1){
				return;
			}else{
				functionRoot.tree('remove', children[0].target);
			}
			
			//是否加载正确或文件内容是否正确
			if (!root){
				alert("cannot load configure file: functions.xml ");
		   		return;
			}
		
			//找到group节点
			//xmlDoc.setProperty("SelectionLanguage","XPath");
			//funcGroup = root.selectSingleNode("//functionGroup[@funcname='"+ node.id +"']");
			funcGroup = root.firstChild;
			while(funcGroup){
				if(!(funcGroup instanceof Text) &&  funcGroup.getAttribute("funcname")==node.id){
					break;
				}
				funcGroup = funcGroup.nextSibling;
			}
			
			if (!funcGroup)
				return;
			func = funcGroup.firstChild;
			var funcs = [];
			while(func){
				if (!(func  instanceof Text)){
					funcID = func.getAttribute("funcname");
					funcName = func.getAttribute("name");
					funcPrototype = func.getAttribute("prototype");;
					funcDesc = func.getAttribute("desc");;

					funcs.push({id:funcID,
						text:(funcName+"("+funcID+")"),
						attributes:{prototype:funcPrototype,desc:funcDesc}});
				}
				func = func.nextSibling;         
			}		      
			functionRoot.tree('append',{
				parent:node.target,
				data:funcs
			});
		}
	});
} 

function EditingItem(expr, display, manual){
	this.expr = expr;
	this.display = display;
	this.manual = manual;
}

EditingItem.prototype.constructor = EditingItem;

function EditingItemMgr(){
	this.list = [];
	this.expr = null;
	this.display = null;
}

EditingItemMgr.prototype.constructor = EditingItemMgr;

EditingItemMgr.prototype.getIndex = function(pos, isDisplay){
	var length = 0; 
	var item = null;
	for(var i=0; i < this.list.length; i++){
		item = this.list[i];
		length = isDisplay ? item.display.length : item.expr.length; 
		if (length > pos)
			break;
	}
	return i;
}

EditingItemMgr.prototype.insertItem = function(editingItem, expr, pos, isDisplay){
	var i = this.getIndex(pos, isDisplay); 
	this.list.splice(i, 0, editingItem);	
}

EditingItemMgr.prototype.isItemEditable = function(editingItem, expr, pos, isDisplay){
	var i = this.getIndex(pos, isDisplay); 
	return this.list[i].manual;
}

EditingItemMgr.prototype.toExpr = function(){
	var exprStr = "";
	var item = null;
	for(var i = 0; i < this.list.length; i++){
		if (exprStr.length > 0)
			exprStr += "|";
		item = this.list[i];
		exprStr += item.expr + "," + item.display + "," + item.manual;
	}
	return exprStr;
}

EditingItemMgr.prototype.fromExpr = function(exprStr){
	var exprList = exprStr.split("|");
	var item = null;
	for(var i = 0; i < exprList.length; i++){
		item = exprList[i].split(",");
		this.list.push(new EditingItem(item[0],item[1],item[2]));
	}
}

EditingItemMgr.prototype.getDisplay = function(){
	var display = "";
	for(var i=0; i < this.list.length; i++){
		display += this.list[i].display;
	}
	return display;
}


function ReferenceSource(){
	this.referenceType = [];
	this.referenceType.push({classType:'nc.vo.org.GroupVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.sm.UserVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.vorg.FinanceOrgVersionVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.pub.billtype.BilltypeVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.org.DeptVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.org.SalesOrgVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.address.AddressVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.psn.PsndocVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.cust.CustomerVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.currtype.CurrtypeVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.bankaccount.BankAccSubVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.arap.receivable.ReceivableBillItemVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.resa.costcenter.CostCenterVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.countryzone.CountryZoneVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.cust.CustomerVO', idField:'id', nameField:'name', codeField:'code'});
	this.referenceType.push({classType:'nc.vo.bd.cashflow.CashflowVO', idField:'id', nameField:'name', codeField:'code'});
};

ReferenceSource.prototype.constructor = ReferenceSource;

