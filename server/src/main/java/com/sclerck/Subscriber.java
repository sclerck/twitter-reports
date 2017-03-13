/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck;

import io.vertx.core.AbstractVerticle;

/**
 * Purpose:
 * 
 * @author Simon
 * @date 12 Mar 2017
 *
 */
public class Subscriber extends AbstractVerticle {

	public void start() {

		// Setting host as localhost is not strictly necessary as it's the
		// default
		vertx.createHttpClient().websocket(8080, "localhost", "/", ws -> {
			ws.handler(data -> {
				System.out.println("Received " + data);
			});
		});

	}

}
