package org.loganalyzer.center.model.wsimpl;

import org.loganalyzer.center.collector.wsimpl.model.Source;
import org.loganalyzer.center.model.ILogMeta;

public class WSLogMeta implements ILogMeta{

	private Source source;

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

}
