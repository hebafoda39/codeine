package codeine.servlets.api_servlets.angular;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import codeine.api.NodeGetter;
import codeine.api.NodeWithMonitorsInfo;
import codeine.configuration.Links;
import codeine.model.Constants;
import codeine.servlet.AbstractApiServlet;
import codeine.utils.network.HttpUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;

public class MonitorStatusApiServlet extends AbstractApiServlet {

	private static final Logger log = Logger
			.getLogger(MonitorStatusApiServlet.class);
	private static final long serialVersionUID = 1L;
	@Inject	private NodeGetter nodesGetter;
	@Inject	private Links links;
	@Inject	private Gson gson;
	
	@Override
	protected boolean checkPermissions(HttpServletRequest request) {
		return canReadProject(request);
	}
	
	@Override
	protected void myGet(HttpServletRequest request, HttpServletResponse response) {
		String projectName = getParameter(request, Constants.UrlParameters.PROJECT_NAME);
		String nodeName = getParameter(request, Constants.UrlParameters.NODE);
		String monitorName = getParameter(request, Constants.UrlParameters.MONITOR);
		NodeWithMonitorsInfo node = nodesGetter.getNodeByNameOrNull(projectName, nodeName);
		String peerMonitorResultLink = links.getPeerMonitorResultLink(node.peer_address(), projectName, monitorName, nodeName);
		log.info("accessing url " + peerMonitorResultLink);
		String outputFromPeer = HttpUtils.doGET(peerMonitorResultLink,null, HttpUtils.MEDIUM_READ_TIMEOUT_MILLI);
//		String encodeOutput = HttpUtils.encodeHTML(outputFromPeer);
		MonitorExecutionResult monitorResult = null;
		try {
			monitorResult = gson.fromJson(outputFromPeer, MonitorExecutionResult.class);
		} catch (JsonSyntaxException e) {
			log.info("falling back to raw format");
			monitorResult = new MonitorExecutionResult(outputFromPeer);
		}
		writeResponseGzipJson(monitorResult, request, response);
	}
	
	

}
