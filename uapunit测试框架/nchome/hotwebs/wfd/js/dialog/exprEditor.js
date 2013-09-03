/**
 * Class: Expression Editor
 */
function ExprEditor(id)
{
	this.id = id;
	this.url = "./window/expression/expression.html";
};

ExprEditor.prototype.constructor = ExprEditor;

ExprEditor.prototype = {
	get_id : ExprEditor$get_id,
   set_id: ExprEditor$set_id,
   get_url: ExprEditor$get_url,
   set_id: ExprEditor$set_url
};

function doReference(){
   window.showModalDialog("./window/expression/expression.html", "reference", 
      "status=no;scroll=no;resizable=no;dialogHeight=600px;dialogWidth=800px");
}


$.extend($.fn.datagrid.defaults.editors,{
   ExpressionEditor: {
      init: function(container, options){
         var input = $('<table><tr><td id="textArea" style="width:120px"><input type="text" id="txtExpression" class="datagrid-editable-input"/></td><td id="btnArea" style="width:24px;"><input type="button"  id="btnReference" value="..." name="btnReference" onclick="doReference();"/></td></tr></table>').appendTo(container);
         return input;
      },
      getValue: function(target){
         return $(target).val();
      },
      setValue: function(target, value){
         $(target).val(value);
      },
      resize: function(target, width){
         var input=$(target);
         if($.boxModel == true){
            input.width(width - (input.outerWidth() - input.width()));
         }else{
            input.width(width);
         }
      }
   }
});


function ExprEditor$get_id() {
    return this.id;
};

function ExprEditor$set_id(id) {
    this.id = id;
};

function ExprEditor$get_url() {
    return this.url;
};

function ExprEditor$set_url(url) {
    this.url = url;
};

