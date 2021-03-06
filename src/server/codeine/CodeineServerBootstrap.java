package codeine;

import java.util.List;

import org.apache.log4j.Logger;

import codeine.jsons.global.GlobalConfigurationJsonStore;
import codeine.utils.ExceptionUtils;
import codeine.utils.ThreadUtils;

import com.google.common.collect.Lists;
import com.google.inject.Module;

public class CodeineServerBootstrap extends CodeineWebServerBootstrap {

	
		
	
	private final Logger log = Logger.getLogger(CodeineServerBootstrap.class);

	public static void main(String[] args) {
		System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "10000000");
		boot(Component.SERVER, CodeineServerBootstrap.class);
	}
	@Override
	protected List<Module> getGuiceModules() {
		return Lists.<Module>newArrayList(new ServerModule(), new ServerServletModule());
	}

	@Override
	public int getHttpPort() {
		return injector().getInstance(GlobalConfigurationJsonStore.class).get().web_server_port();
	}

	@Override
	protected void execute() {
		log.info("execute()");
		List<AbstractCodeineBootstrap> bootstraps = Lists.newArrayList(
				new CodeineDirectoryBootstrap(injector()),
				new CodeineWebServerBootstrap(injector())
				);
		for (AbstractCodeineBootstrap b : bootstraps) {
			try {
				b.execute();
			} catch (Exception e) {
				throw ExceptionUtils.asUnchecked(e);
			}
			ThreadUtils.sleep(100);
		}
	}

}
