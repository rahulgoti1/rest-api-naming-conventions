package co.rule.engine.service;

import co.rule.engine.config.SqsProperties;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by grahul on 01-04-2019.
 */
@Service
public class NotificationService {

  private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);
  private final AmazonSQS amazonSqs;
  private final SqsProperties awsConfig;

  public NotificationService(AmazonSQS amazonSqs, SqsProperties awsConfig) {
    this.amazonSqs = amazonSqs;
    this.awsConfig = awsConfig;
  }

  public SendMessageResult sendMessage(String message) {
    SendMessageRequest request = new SendMessageRequest();
    request.setQueueUrl(awsConfig.getQueueUrl());
    request.setMessageBody(message);
    return amazonSqs.sendMessage(request);
  }


}
