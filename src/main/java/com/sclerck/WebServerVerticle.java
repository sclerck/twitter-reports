/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Purpose: The web server
 * 
 * If you are on mac and the web server complains of blocked threads, see this:
 * https://thoeni.io/post/macos-sierra-java/
 * 
 * @date 12 Mar 2017
 *
 */
public class WebServerVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		server.requestHandler(router::accept);

		server.websocketHandler(ws -> {

			MessageConsumer<Object> mc = vertx.eventBus().consumer("tweet", tweet -> {
				ws.writeTextMessage(tweet.body().toString());
			});

			ws.closeHandler(ch -> {
				mc.unregister();
			});
		});

		server.listen(8080);

	}

}
