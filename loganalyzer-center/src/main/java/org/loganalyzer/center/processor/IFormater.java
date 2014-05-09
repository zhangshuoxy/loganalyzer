package org.loganalyzer.center.processor;

import org.loganalyzer.center.model.ILogEntity;

public interface IFormater {

	public ILogEntity format(ILogEntity oldEntity);
	
}
