package com.SocialNetSys.NetSys.Providers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws.acess.key}")
    protected String ACESS_KEY;
    @Value("${aws.secret.key}")
    protected String SECRET_KEY;
    protected  String ENDPOINT_NAME = "s3.us-east-2.amazonaws.com";
    protected  String REGION = "us-east-2";

    @Bean
    public AmazonS3 AmazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACESS_KEY, SECRET_KEY)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT_NAME, REGION))
                .build();
    }
}
