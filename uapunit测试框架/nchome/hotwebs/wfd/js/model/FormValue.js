function FormValue(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "FormValue";
};

FormValue.prototype = new BaseElement();
FormValue.prototype.constructor = FormValue;


/**
 * Class: FormValueInfo
 */
function FormValueInfo(id)
{
	BaseElementInfo.call(this, id);
};

FormValueInfo.prototype = new BaseElementInfo();
FormValueInfo.prototype.constructor = FormValueInfo;
