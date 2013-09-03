function Artifact(id)
{
	BaseElement.call(this, id);
	this.name = "";
};

Artifact.prototype = new BaseElement();
Artifact.prototype.constructor = Artifact;


/**
  * Class: ArtifactInfo
  */
function ArtifactInfo(id)
{
	BaseElementInfo.call(this, id);
			
};

ArtifactInfo.prototype = new BaseElementInfo();
ArtifactInfo.prototype.constructor = ArtifactInfo;

