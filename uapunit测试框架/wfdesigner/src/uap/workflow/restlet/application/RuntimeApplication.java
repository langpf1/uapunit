package uap.workflow.restlet.application;  

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.routing.Router;  

import uap.workflow.restlet.resources.ActivateProDefResource;
import uap.workflow.restlet.resources.AddSignTaskResource;
import uap.workflow.restlet.resources.ApproveResource;
import uap.workflow.restlet.resources.DeleteProDefResource;
import uap.workflow.restlet.resources.FormResource;
import uap.workflow.restlet.resources.HistoricResource;
import uap.workflow.restlet.resources.OperatorProDefResource;
import uap.workflow.restlet.resources.OperatorResource;
import uap.workflow.restlet.resources.ProcessDefinitionsResource;
import uap.workflow.restlet.resources.ReAssignTaskResource;
import uap.workflow.restlet.resources.RejectTaskResource;
import uap.workflow.restlet.resources.RoleResource;
import uap.workflow.restlet.resources.AssignTaskResource;
import uap.workflow.restlet.resources.SuspendProDefResource;
import uap.workflow.restlet.resources.TaskAll;
import uap.workflow.restlet.resources.TaskCompleteResource;
import uap.workflow.restlet.resources.TaskProcessingResource;
import uap.workflow.restlet.resources.TaskToDoResource;
import uap.workflow.restlet.resources.TerminateProDefResource;
import uap.workflow.test.CustomerResource;
public class RuntimeApplication extends Application {   
    public  Restlet createRoot() {     
        // Create a router Restlet that routes each call to a     
        // new instance of HelloWorldResource.     
        Router router = new Router(getContext());     
        // Defines only one route     
        /*TODO：声明多个application,把资源路径分组*/
        router.attach("/task/",TaskAll.class);
        router.attach("/task/todo/", TaskToDoResource.class);
        router.attach("/task/processing/", TaskProcessingResource.class);
        router.attach("/task/complete/", TaskCompleteResource.class);
            
        router.attach("/task/approve/", ApproveResource.class);
        router.attach("/task/reject/", RejectTaskResource.class);
        router.attach("/task/assign/", AssignTaskResource.class);
        router.attach("/task/addsign/", AddSignTaskResource.class);
        router.attach("/task/reassign/", ReAssignTaskResource.class);
        
        router.attach("/history/", HistoricResource.class);           
        router.attach("/wfform/approve/",FormResource.class);
        
        router.attach("/wfform/bill/",CustomerResource.class);
        
        router.attach("/console/processdefinitions/",ProcessDefinitionsResource.class);
        router.attach("/console/processdefinitions/operator/{operator}/",OperatorProDefResource.class);
        
        router.attach("/participant/operator/",OperatorResource.class);
        router.attach("/participant/role/",RoleResource.class);
        
        router.attach("/login/",LoginResource.class);
        router.attach("/logout/", LogoutResource.class);
        return router;     
    }     
}    