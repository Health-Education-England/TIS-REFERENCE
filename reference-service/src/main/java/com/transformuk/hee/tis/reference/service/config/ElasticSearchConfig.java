package com.transformuk.hee.tis.reference.service.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfig {

  private static final String CLUSTER_NAME_KEY = "cluster.name";
  private static final String CLUSTER_SNIFF_KEY = "client.transport.sniff";

  @Value("${elasticsearch.name}")
  private String clusterName;

  @Value("${elasticsearch.clustersniffing}")
  private boolean clusterSniffing;

  @Value("${elasticsearch.hostname}")
  private String hostname;

  @Value("${elasticsearch.port}")
  private int port;

  @Bean
  public TransportClient transportClient() throws UnknownHostException {
    Settings settings = Settings.settingsBuilder()
        .put(CLUSTER_NAME_KEY, clusterName)
        .put(CLUSTER_SNIFF_KEY, clusterSniffing)
        .put("client.transport.ignore_cluster_name", false)
        .put("node.client", true)
        .build();
    return TransportClient.builder().settings(settings).build()
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), port));
  }
}
