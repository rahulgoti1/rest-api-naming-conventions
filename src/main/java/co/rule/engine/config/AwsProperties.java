package co.rule.engine.config;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Component used to load SQS environment properties from spring application properties.
 */
@Validated
@Configuration
@ConfigurationProperties
public class AwsProperties {
  public static final Logger LOG = LoggerFactory.getLogger(AwsProperties.class);

  @Value("${aws.region}")
  private String region;

  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.accessKeySecret}")
  private String accessKeySecret;

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
    return accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }

  @Bean
  public AWSCredentialsProviderChain awsCredentialsProviderChain() {
    DefaultAWSCredentialsProviderChain credentialProvider = new DefaultAWSCredentialsProviderChain();
    LOG.info("credentialProvider.getCredentials().getAWSAccessKeyId(): {}",
        credentialProvider.getCredentials().getAWSAccessKeyId());

    return credentialProvider;
  }

  @Bean
  public AmazonSQS amazonSqs(AWSCredentialsProviderChain credentials) {
    AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard();
    builder.setCredentials(credentials);
    builder.setRegion(region);
    return builder.build();
  }

  // https://docs.aws.amazon.com/AmazonS3/latest/dev/UploadObjSingleOpJava.html
  @Bean
  public AmazonS3 amazonS3(AWSCredentialsProviderChain credentials) {
    AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
    builder.setCredentials(credentials);
    builder.setRegion(region);
    return builder.build();
  }

}
