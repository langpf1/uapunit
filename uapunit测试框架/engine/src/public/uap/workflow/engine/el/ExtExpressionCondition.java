package uap.workflow.engine.el;


import uap.workflow.bizitf.exception.BizException;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.query.Condition;

public class ExtExpressionCondition implements Condition {

	protected Expression expression;
	
	public ExtExpressionCondition(Expression expression){
		this.expression = expression;
	}
	
	public Expression getExpression(){
		return expression;
	}
	
	public void setExpression(Expression expression){
		this.expression = expression;
	}
	
	public boolean evaluate(IActivityInstance execution) {
	    Object result = expression.getValue(execution);
	    
	    if (result==null) {
	      throw new BizException("condition expression returns null");
		}
	    if (! (result instanceof Boolean)) {
	      throw new BizException("condition expression returns non-Boolean: "+result+" ("+result.getClass().getName()+")");
	    }
	    return (Boolean) result;
	}
}
