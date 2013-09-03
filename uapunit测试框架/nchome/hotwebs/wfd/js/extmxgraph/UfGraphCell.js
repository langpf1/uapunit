UfGraphCell = function (value,geometry,style)
{
  mxCell.call(this,value,geometry,style);
  this.boundaryCell=null;
};

// BpmnGraphHandler inherits from mxGraphHandler
mxUtils.extend(UfGraphCell, mxCell);

/*UfGraphCell.prototype = {
	get_boundaryCell : UfGraphCell$get_boundaryCell,
    set_boundaryCell: UfGraphCell$set_boundaryCell,
	clone : UfGraphCell$clone,
    cellValueChanged: UfGraphCell$cellValueChanged
};
*/

function UfGraphCell$get_boundaryCell(){
	return boundaryCell;
}
function UfGraphCell$set_boundaryCell(boundaryCell){
	this.boundaryCell=boundaryCell;
}
function UfGraphCell$clone(){
	    var c = mxCell.clone();
		// only IUfGraphUserOjbect need replicate
		if (this.getValue() != null ) {
			if(this.getValue() instanceof IUfGraphUserOjbect){
				c.setValue(this.getValue().replicate());
			}else if(this.getValue() instanceof IUserObjectClone){
				c.setValue(this.getValue().replicate());
			}
			
			else{
				c.setValue(this.getValue());
			}
			
		}
		c.setId(parseInt(Math.random()*10000000000));
		return c;
}
function UfGraphCell$cellValueChanged(o){
	if (this.getValue() != null && (this.getValue() instanceof IUfGraphUserOjbect))
			this.getValue().cellValueChanged(o);
}