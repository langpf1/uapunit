package nc.uap.engine.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.delegate.JavaDelegate;
import uap.workflow.engine.el.ExtExpression;

public class Testclass implements JavaDelegate,IInstanceListener {
	private static final Logger LOGGER = Logger.getLogger(Testclass.class.getName());
	private Expression arg1;
	private Expression arg2;
    public String test;
    public  ExtExpression   testNCExpression;
	public  Testclass(){
		test="sdfasdfas";
		testNCExpression= new ExtExpression(null, null, test);
		LOGGER.log(Level.FINE, "new constructor "); //$NON-NLS-1$		
	}
	
    public void testService(){ 
    	LOGGER.log(Level.SEVERE, "test TestService "); //$NON-NLS-1$				    	
}
    public Object execute(IActivityInstance execution) throws Exception {
    	LOGGER.log(Level.FINE, "execution "); //$NON-NLS-1$		
		//System.out.print(arg1.getValue(execution));
//		return new Person("1", "2");
		return null;
	}
	public Expression getArg1() {
		return arg1;
	}
	public void setArg1(Expression arg1) {
		this.arg1 = arg1;
	}
	public Expression getArg2() {
		return arg2;
	}
	public void setArg2(Expression arg2) {
		this.arg2 = arg2;
	}

	@Override
	public void notify(IActivityInstance execution) throws Exception {
		// TODO Auto-generated method stub
		
	}    
}
