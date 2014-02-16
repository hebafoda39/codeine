package codeine.servlets.api_servlets.angular;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import codeine.jsons.global.GlobalConfigurationJson;
import codeine.jsons.global.GlobalConfigurationJsonStore;
import codeine.servlet.AbstractServlet;

public class CodeineConfigurationApiServlet extends AbstractServlet {

	
	private static final Logger log = Logger.getLogger(CodeineConfigurationApiServlet.class);

	private static final long serialVersionUID = 1L;

	@Inject private GlobalConfigurationJsonStore configurationJsonStore;
	
	@Override
	protected boolean checkPermissions(HttpServletRequest request) {
		return true;
	}
	
	@Override
	protected void myGet(HttpServletRequest request, HttpServletResponse response) {
		writeResponseJson(response, configurationJsonStore.get());
	}
	
	@Override
	protected void myPut(HttpServletRequest req, HttpServletResponse resp) {
		GlobalConfigurationJson config = readBodyJson(req, GlobalConfigurationJson.class);
		log.info("Will update codeine configuration. New Config is: " + config);
		configurationJsonStore.store(config);
		writeResponseJson(resp, configurationJsonStore.get());
	}

}