function Signal(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "Signal";
};

Signal.prototype = new BaseElement();
Signal.prototype.constructor = Signal;



/**
 * Class: SignalInfo
 */
function SignalInfo(id)
{
	BaseElementInfo.call(this, id);
};

SignalInfo.prototype = new BaseElementInfo();
SignalInfo.prototype.constructor = SignalInfo;
