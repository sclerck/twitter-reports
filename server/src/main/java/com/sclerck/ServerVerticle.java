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
public class ServerVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		vertx.deployVerticle(new TwitterStreamVerticle());
		vertx.deployVerticle(new WebServerVerticle());
	}

}
