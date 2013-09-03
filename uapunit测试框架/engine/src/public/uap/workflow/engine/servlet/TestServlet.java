package uap.workflow.engine.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.NCSysOutWrapper;

import com.ibm.db2.jcc.am.hm;

import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.context.AddSignUserInfoCtx;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.ReStartBeforeAddSignTaskInsCtx;
import uap.workflow.engine.context.StopBeforeAddSignTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.repository.DeploymentBuilder;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.service.RepositoryService;
import uap.workflow.engine.utils.ActivityInstanceUtil;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.ui.workitem.FlowStateViewDlg;
public class TestServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String proDefId = req.getParameter("proDefId");
		String proDefPk = req.getParameter("proDefPk");
		String taskPk = req.getParameter("taskPk");
		String fileName = req.getParameter("file");
		String executionId = req.getParameter("executionId");
		String cmd = req.getParameter("cmd");
		
		PrintStream pw = System.out;
		System.setOut(new NCSysOutWrapper(pw, true));
		pw.println("adfasdfsd");
		
		if (proDefPk != null) {
			this.startByPk(proDefPk);
		}
		if (proDefId != null) {
			this.startById(proDefId);
		}
		if ("deploy".equalsIgnoreCase(cmd)) {
			this.deploy(fileName);
		}
		if ("next".equalsIgnoreCase(cmd)) {
			this.next(taskPk);
		}
		if ("nextnext".equalsIgnoreCase(cmd)) {
			this.nextnext();
		}
		if ("beforeaddsign".equalsIgnoreCase(cmd)) {
			this.testBeforeAddSign(taskPk);
		}
		if ("stopbeforeaddsign".equalsIgnoreCase(cmd)) {
			this.testStopBeforeAddSign(taskPk);
		}
		if ("restartbeforeaddsign".equalsIgnoreCase(cmd)) {
			this.testRestartBeforeAddSign(taskPk);
		}
		if ("receivetask".equalsIgnoreCase(cmd)) {
			this.receiveTask(executionId);
		}
		if("afteraddsign".equalsIgnoreCase(cmd)){
			this.testAfterAddSign(taskPk);
		}
	}
	@Override
	public void init() throws ServletException {
		super.init();
		BizProcessServer.getInstance().start();
	}
	private void receiveTask(String executionId) {
		
		NextReceiveTaskCtx receiveTaskCtx = new NextReceiveTaskCtx();
		IActivityInstance execution = ActivityInstanceUtil.getActInsByActInsPk(executionId);
		List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(execution.getActivity(), null);
		if (userTaskPrepCtx.size() > 0) {
			List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
			for (int i = 0; i < userTaskPrepCtx.size(); i++) {
				UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
				UserTaskPrepCtx oneCtx = userTaskPrepCtx.get(i);
				runTimeCtx.setActivityId(oneCtx.getActivityId());
				runTimeCtx.setUserPks(oneCtx.getUserPks());
				runTimeCtx.setSequence(false);
				nextInfo.add(runTimeCtx);
			}
			receiveTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
			receiveTaskCtx.setUserPk("timer-transi");
			receiveTaskCtx.setExecutionId(executionId);
		}
		IFlowRequest request = BizProcessServer.createFlowRequest(null, receiveTaskCtx);
		IFlowResponse response = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, response);
	}
	private void deploy(String xmlStr) {
		RepositoryService reposService = BizProcessServer.processEngine.getRepositoryService();
		DeploymentBuilder deployBuild = reposService.createDeployment();
		File file = new File("c:\\" + xmlStr);
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
			deployBuild.addInputStream(xmlStr, fileInput);
			deployBuild.deploy();
		} catch (FileNotFoundException e) {
			WorkflowLogger.error(e.getMessage(), e);
		} finally {
			try {
				fileInput.close();
			} catch (IOException e) {
				WorkflowLogger.error(e.getMessage(), e);
			}
		}
	}
	
	private void nextnext()
	{
		 String processInstancePk=null;
		 ITaskInstanceQry taskInsQr=NCLocator.getInstance().lookup(ITaskInstanceQry.class);
		 List<TaskInstanceVO> taskInsVos=taskInsQr.getTaskByProcessInstancePk(processInstancePk);
		final  FlowStateViewDlg test=new FlowStateViewDlg(null, "10GY", "1001AA100000000092E7", 0);
		test.setVisible(true);
	}
	
	
	private void next(String taskPk) {
//		try {
//			//BizProcessServer.getInstance().start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
	/*	taskPk="0001AA1000000000GV99";
		NextUserTaskInfoUtil util=new NextUserTaskInfoUtil();
		List<UserTaskPrepCtx>  ctx=	util.getNextUserTaskInfo(taskPk, null);
		final BranchPane test=new BranchPane();
		JFrame temp=new JFrame ();
		temp.add(test);
		temp.setVisible(true);
		HashMap<String, List<String>> activitiUserNames=test.getActivitiUserNames();*/
		
	//	test.destroy();
  
	/*	HashMap<String, List<String>> testBranch=test.getActivitiUserNames();
		Iterator iterator = testBranch.keySet().iterator();
		while(iterator.hasNext()) {
		Entry  obj = (Entry) iterator.next();
		System.out.println(obj.getKey()+" "+obj.getValue());
		}
   */
		
	//	SwingUtilities.invokeLater(new Runnable(){

   //		@Override
	//	public void run() {
			//	test.show();
	//			test.setVisible(true);
	//		}
			
	//	});
		
		
		List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(taskPk, null,null);
		// 够造下一步的执行信息
		NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
		// 设置当前任务
		nextTaskCtx.setTaskPk(taskPk);
		// 设置当前用户
		nextTaskCtx.setUserPk("tianchuanwu");
		if (userTaskPrepCtx.size() > 0) {
			List<UserTaskRunTimeCtx> userTaskCtx = new ArrayList<UserTaskRunTimeCtx>();
			for (int i = 0; i < userTaskPrepCtx.size(); i++) {
				UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
				nextInfo.setActivityId(userTaskPrepCtx.get(i).getActivityId());
				nextInfo.setUserPks(userTaskPrepCtx.get(i).getUserPks());
				nextInfo.setSequence(false);
				userTaskCtx.add(nextInfo);
			}
			nextTaskCtx.setNextInfo(userTaskCtx.toArray(new UserTaskRunTimeCtx[0]));
		}
		// 执行指令
		IFlowRequest request1 = BizProcessServer.createFlowRequest(null, nextTaskCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		//BizProcessServer.exec(request1, respone1);
	}
	private void startByPk(String proDefPk) {
		IProcessDefinition proDef = ProcessDefinitionUtil.getProDefByProDefPk(proDefPk);
		List<UserTaskPrepCtx> userTaskPropCtx = NextUserTaskInfoUtil.getStartNextUserTaskInfo(proDef.getProDefPk(), null,null);
		CommitProInsCtx flowInfo = new CommitProInsCtx();
		flowInfo.setProDefPk(proDefPk);
		flowInfo.setUserPk("tianchuanwu");
		if (userTaskPropCtx != null) {
			List<UserTaskRunTimeCtx> userTaskCtx = new ArrayList<UserTaskRunTimeCtx>();
			for (int i = 0; i < userTaskPropCtx.size(); i++) {
				UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
				nextInfo.setActivityId(userTaskPropCtx.get(i).getActivityId());
				nextInfo.setUserPks(userTaskPropCtx.get(i).getUserPks());
				nextInfo.setSequence(false);
				userTaskCtx.add(nextInfo);
			}
			flowInfo.setNextInfo(userTaskCtx.toArray(new UserTaskRunTimeCtx[0]));
		}
		IFlowRequest request = BizProcessServer.createFlowRequest(null, flowInfo);
		request.add2Property("biiytype", "");
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);
	}
	private void startById(String proDefId) {
		// BizProcessServer.getInstance().start();
		IProcessDefinition proDef = null;// ProDefUtil.getProDefByProDefId("adhoc_Expense_process");
		proDef = ProcessDefinitionUtil.getProDefByProDefId(proDefId);
		List<UserTaskPrepCtx> userTaskPropCtx = NextUserTaskInfoUtil.getStartNextUserTaskInfo(proDef.getProDefPk(), null,null);
		UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
		nextInfo.setActivityId(userTaskPropCtx.get(0).getActivityId());
		nextInfo.setUserPks(userTaskPropCtx.get(0).getUserPks());
		nextInfo.setSequence(false);
		CommitProInsCtx flowInfo = new CommitProInsCtx();
		flowInfo.setProDefId(proDefId);
		flowInfo.setUserPk("tianchuanwu");
		flowInfo.setNextInfo(new UserTaskRunTimeCtx[] { nextInfo });
		IFlowRequest request = BizProcessServer.createFlowRequest(null, flowInfo);
		request.add2Property("biiytype", "");
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);
	}
	public void testBeforeAddSign(String taskPk) {
		CreateBeforeAddSignCtx beforeAddSignCtx = new CreateBeforeAddSignCtx();
		beforeAddSignCtx.setTaskPk(taskPk);
		{
			AddSignUserInfoCtx addSignUserCtx1 = new AddSignUserInfoCtx();
			addSignUserCtx1.setUserPk("00012410000000001TXV");
			AddSignUserInfoCtx addSignUserCtx2 = new AddSignUserInfoCtx();
			addSignUserCtx2.setUserPk("000124100000000020FK");
			beforeAddSignCtx.setAddSignUsers(new AddSignUserInfoCtx[] { addSignUserCtx1, addSignUserCtx2 });
		}
		beforeAddSignCtx.setUserPk("tianchuanwu");
		beforeAddSignCtx.setLogic(Logic.Sequence);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(null, beforeAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	public void testStopBeforeAddSign(String taskPk) {
		StopBeforeAddSignTaskInsCtx beforeAddSignCtx = new StopBeforeAddSignTaskInsCtx();
		beforeAddSignCtx.setTaskPk(taskPk);
		beforeAddSignCtx.setUserPk("tianchuanwu");
		IFlowRequest request1 = BizProcessServer.createFlowRequest(null, beforeAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	public void testRestartBeforeAddSign(String taskPk) {
		ReStartBeforeAddSignTaskInsCtx reStartAddSignCtx = new ReStartBeforeAddSignTaskInsCtx();
		reStartAddSignCtx.setTaskPk(taskPk);
		reStartAddSignCtx.setUserPk("tianchuanwu");
		IFlowRequest request1 = BizProcessServer.createFlowRequest(null, reStartAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	public void testAfterAddSign(String taskPk){
		CreateAfterAddSignCtx ctx=new CreateAfterAddSignCtx();
		ctx.setTaskPk(taskPk);
		{
			AddSignUserInfoCtx addSignUserCtx1 = new AddSignUserInfoCtx();
			addSignUserCtx1.setUserPk("00012410000000001TXV");
			AddSignUserInfoCtx addSignUserCtx2 = new AddSignUserInfoCtx();
			addSignUserCtx2.setUserPk("000124100000000020FK");
			ctx.setAddSignUsers(new AddSignUserInfoCtx[] { addSignUserCtx1, addSignUserCtx2 });
		}
		
		ctx.setUserPk("tianchuanwu");
		ctx.setLogic(Logic.Sequence);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(null, ctx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	
}
