package uap.workflow.modeler.uecomponent;


public class StringCellMeta implements GraphElementMeta {
	
	String[] metas = null;
	public StringCellMeta(String[] metas)  {
		this.metas = metas;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return Integer.valueOf(metas[4]);
	}

	@Override
	public String getImageURL() {
		// TODO Auto-generated method stub
		return metas[1];
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return metas[0];
	}

	@Override
	public String getStyle() {
		// TODO Auto-generated method stub
		return metas[2];
	}

	@Override
	public String getUserObjectClass() {
		// TODO Auto-generated method stub
		return metas[5];
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return Integer.valueOf(metas[3]);
	}

	public String getSubClass(){
		return metas[6];
	}
	@Override
	public boolean isVertex() {
		// TODO Auto-generated method stub
		return "Y".equals(metas[7]);
	}

	@Override
	public String getNotationGroup() {
		// TODO Auto-generated method stub
		return metas[8];
	}
}
