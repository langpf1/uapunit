/**
 * Class: FormProperty
 */
function FormProperty(id)
{
	BaseElement.call(this, id);
	this.id="";
	this.name="";
	this.type= "";
	this.value="";
	this.expression="" ;
	this.variable = "";
	this.defaultExpression= "";
	this.pattern= "" ;
	this.required=false;
	this.readable= false ;
	this.writeable=false;
	this.formValues=null;
	this.label= "FormProperty";
};

FormProperty.prototype = new BaseElement();
FormProperty.prototype.constructor = FormProperty;

/**
 * Class: FormPropertyInfo
 */
function FormPropertyInfo(id)
{
	BaseElementInfo.call(this, id);
};

FormPropertyInfo.prototype = new BaseElementInfo();
FormPropertyInfo.prototype.constructor = FormPropertyInfo;