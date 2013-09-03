/**
 * Class: Annotation
 */
function Annotation(id)
{ 
	Artifact.call(this, id);
	this.name = "Annotation";
	this.text="";
	this.label="Annotation";
	this.infoClass = "AnnotationInfo";
};

Annotation.prototype = new Artifact();
Annotation.prototype.constructor = Annotation;


/**
  * Class: AnnotationInfo
  */
function AnnotationInfo(id)
{
	FlowElementInfo.call(this, id);					
	this.properties.push(new Property('text', 'Main Config', false, 
		EditorType.text, '','text','textHints'));
};

AnnotationInfo.prototype = new FlowElementInfo();
AnnotationInfo.prototype.constructor = AnnotationInfo;