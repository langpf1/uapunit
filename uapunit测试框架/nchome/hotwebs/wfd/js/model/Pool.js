/**
 * Class: Pool
 */
function Pool(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.processRef = "";
	this.name = "Process";
	this.label = "Process";
	this.infoClass = "PoolInfo";
};

Pool.prototype = new BaseElement();
Pool.prototype.constructor = Pool;



/**
 * Class: PoolInfo
 */
function PoolInfo(id)
{
	BaseElementInfo.call(this, id);
};

PoolInfo.prototype = new BaseElementInfo();
PoolInfo.prototype.constructor = PoolInfo;

