package uap.workflow.modeler.bpmn.bpmn2Diagram;

import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.event.BoundaryEvent;

public class BoundaryEventModel
{
  public static final String TIMEEVENT = "timeevent";
  public static final String ERROREVENT = "errorevent";
  public static final String SIGNALEVENT = "signalevent";
  public BoundaryEvent boundaryEvent;
  public String attachedRef;
  public String type;
  public Process parentProcess;
}