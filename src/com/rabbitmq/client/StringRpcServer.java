//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is VMware, Inc.
//  Copyright (c) 2007-2011 VMware, Inc.  All rights reserved.
//


package com.rabbitmq.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Subclass of RpcServer which accepts UTF-8 string requests.
 */
public class StringRpcServer extends RpcServer {
    public StringRpcServer(Channel channel) throws IOException
    { super(channel); }

    public StringRpcServer(Channel channel, String queueName) throws IOException
    { super(channel, queueName); }

    /**
     * Overridden to do UTF-8 processing, and delegate to
     * handleStringCall. If UTF-8 is not understood by this JVM, falls
     * back to the platform default.
     */
    public byte[] handleCall(byte[] requestBody, AMQP.BasicProperties replyProperties)
    {
        String request;
        try {
            request = new String(requestBody, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            request = new String(requestBody);
        }
        String reply = handleStringCall(request, replyProperties);
        try {
            return reply.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return reply.getBytes();
        }
    }

    /**
     * Delegates to handleStringCall(String).
     */
    public String handleStringCall(String request, @SuppressWarnings("unused") AMQP.BasicProperties replyProperties)
    {
        return handleStringCall(request);
    }

    /**
     * Default implementation - override in subclasses. Returns the empty string.
     */
    public String handleStringCall(@SuppressWarnings("unused") String request)
    {
        return "";
    }

    /**
     * Overridden to do UTF-8 processing, and delegate to
     * handleStringCast. If UTF-8 is not understood by this JVM, falls
     * back to the platform default.
     */
    public void handleCast(byte[] requestBody)
    {
        try {
            handleStringCast(new String(requestBody, "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            handleStringCast(new String(requestBody));
        }
    }

    /**
     * Default implementation - override in subclasses. Does nothing.
     */
    public void handleStringCast(@SuppressWarnings("unused") String requestBody) {
        // Do nothing.
    }
}
