package com.ahdms.framework.thrift.server.argument;

import com.ahdms.framework.thrift.server.exception.ThriftServerException;
import com.ahdms.framework.thrift.server.processor.TRegisterProcessor;
import com.ahdms.framework.thrift.server.processor.TRegisterProcessorFactory;
import com.ahdms.framework.thrift.server.properties.ThriftServerProperties;
import com.ahdms.framework.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSimpleServerArgument extends TSimpleServer.Args {

    private Map<String, ThriftServiceWrapper> processorMap = new HashMap<>();

    public TSimpleServerArgument(List<ThriftServiceWrapper> serviceWrappers, ThriftServerProperties properties)
            throws TTransportException, IOException {
        super(new TServerSocket(new ServerSocket(properties.getPort())));

        transportFactory(new TFastFramedTransport.Factory());
        protocolFactory(new TCompactProtocol.Factory());

        try {
            TRegisterProcessor registerProcessor = TRegisterProcessorFactory.registerProcessor(serviceWrappers);

            processorMap.clear();
            processorMap.putAll(registerProcessor.getProcessorMap());

            processor(registerProcessor);
        } catch (Exception e) {
            throw new ThriftServerException("Can not create multiplexed processor for " + serviceWrappers, e);
        }
    }

    public Map<String, ThriftServiceWrapper> getProcessorMap() {
        return processorMap;
    }

}
