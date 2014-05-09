package org.loganalyzer.center.model.wsimpl;

import org.loganalyzer.center.model.ILogEntity;
import org.loganalyzer.center.model.ILogMeta;

public class WSLogEntity implements ILogEntity {

	private ILogMeta meta;
	
	private Object value;
	
	public void setMeta(ILogMeta meta) {
		this.meta = meta;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public ILogMeta getMeta() {
		return meta;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
